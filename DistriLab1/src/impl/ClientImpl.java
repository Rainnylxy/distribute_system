package impl;
//TODO: your implementation
import api.*;
import config.Config;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import utils.FileDesc;

import java.util.*;

public class ClientImpl implements Client{
    NameNode nameNode;
    DataNode dataNode;
    String filepath;
    FileMetaData fileMetaData;
    ArrayList<FileDesc> fileDescArrayList = new ArrayList<>();
    int fdId = 4*1024;

    public ClientImpl(NameNode nameNode) {
        this.nameNode = nameNode;
    }

    public ClientImpl() {
        try {
            String[] args = new String[6];
            args[0]="-port";
            args[2]="-ORBInitialPort";
            args[4]="-ORBInitialHost";
            args[1]="1049";
            args[3]="1050";
            args[5]="localhost";
            // ORB initial
            ORB orb = ORB.init(args, null);

            // Get the context
            org.omg.CORBA.Object obRef = orb
                    .resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(obRef);

            NameComponent nc = new NameComponent("NameNodeLancher", "");
            NameComponent[] path = { nc };

            nameNode = NameNodeHelper.narrow(ncRef.resolve(path));
        } catch (Exception e) {
            System.out.println("Calling CORBA fails." + e);
        }
    }

    public boolean connectDataNode(String[] ports){
        try {
            String[] args = new String[6];
            args[0]="-port";
            args[2]="-ORBInitialPort";
            args[4]="-ORBInitialHost";
            args[1]=ports[0];
            args[3]=ports[1];
            args[5]=ports[2];
            // ORB initial
            ORB orb = ORB.init(args, null);

            // Get the context
            org.omg.CORBA.Object obRef = orb
                    .resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(obRef);

            NameComponent nc = new NameComponent("DataNodeLancher", "");
            NameComponent[] path = { nc };

            this.dataNode=DataNodeHelper.narrow(ncRef.resolve(path));
            return true;
        }catch (Exception e){
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    public int open(String filepath, int mode) {
        //根据接收到的文件元数据信息和读写模式生成文件描述符
        this.filepath = filepath;
        FileMetaData fileMetaData = nameNode.open(filepath,mode);
        if(fileMetaData.fileName.equals("")&&fileMetaData.fileType==0){
            return 0;
        }else {
            FileDesc fileDesc = new FileDesc(fdId,mode,fileMetaData,filepath);
            fileDescArrayList.add(fdId-4096,fileDesc);
            return fdId++;
        }
    }


    @Override
    public boolean append(int fd, byte[] bytes) throws InterruptedException {
        FileDesc fileDesc = fileDescArrayList.get(fd-4096);
        //检查是否有权限
        if(fileDesc==null||fileDesc.getMode() == 0){
            System.out.println("INFO: APPEND NOT ALLOWED");
            return false;
        }
        fileDesc.setFileMetaData(nameNode.open(fileDesc.getFilepath(),0));
        fileMetaData = fileDesc.getFileMetaData();
        DataBlock dataBlock = fileMetaData.getLastDataBlock();
        int initLength = fileMetaData.dataMap.length;
        //获得DataNode的地址
        String[] dnIPs = nameNode.allocateDN(fileDesc.getFilepath(),bytes.length);
        fileMetaData = nameNode.open(fileDesc.getFilepath(),0);
        if(dnIPs == null){
            System.out.println("the file doesn't exists!");
            return false;
        }
        int headLength=0;
        for(int i=0; i < dnIPs.length;i++){

            System.out.println("client append for datanode start...");
            int length;

            if(bytes.length + (dataBlock==null?0:dataBlock.offsize)<=Config.BLOCKSIZE){
                length = bytes.length;
            }else{
                if(i == 0){
                    length = (int) (Config.BLOCKSIZE - dataBlock.offsize);
                    headLength = length;
                }else{
                    length = Math.min(Config.BLOCKSIZE,bytes.length-Config.BLOCKSIZE*i-headLength);
                }
            }
            byte[] b1 = new byte[Config.BLOCKSIZE];

            System.arraycopy(bytes,Config.BLOCKSIZE*i,b1,0,length);
            int fileBlockId;
            if(initLength+i-1<0){
                fileBlockId = 0;
            }else {
                fileBlockId = initLength+i-1;
            }

            // CHANGE:连接所有DataNode
            String[] ports_init = dnIPs[i].split(",");
            int flag=0;
            for(int k=0;k<ports_init.length;k++){
                String[] ports = ports_init[k].split("/");
                if(!(connectDataNode(ports))){
                    flag=1;
                    break;
                };
                try {
                    String returnStatus=dataNode.returnStatus("prepare");
                    if("abort".equals(returnStatus)){
                        flag=1;
                        break;
                    }
                }catch (Exception e){
                    flag=1;
                    break;
                }
            }
            if(flag==1){
                //CHANGE: rollback，放弃对NameNode的修改
                for(int k=0;k<ports_init.length;k++){
                    String[] ports = ports_init[k].split("/");
                    if(!(connectDataNode(ports))){
                        continue;
                    };
                    try {
                        dataNode.returnStatus("abort");
                    }catch (Exception ignored){
                    }
                }
                nameNode.open(filepath,5);
                System.out.println("not all datanodes are prepared, exit...");
                return false;
            }
            for(int k=0;k<ports_init.length;k++){
                String[] ports = ports_init[k].split("/");
                if(!(connectDataNode(ports))){
                    System.out.println("not all datanodes are prepared, exit...");
                    return false;
                };
                dataNode.returnStatus("commit");
                dataNode.append((int)fileMetaData.dataMap[fileBlockId].dtBlockId,b1,fileDesc.getFilepath(),i);
            }
        }
        //CHANGE：来模拟同时写，第一个client写请求还未结束
        Thread.sleep(3000);
        //CHANGE: client1写完后修改DataNode状态为abort，释放锁
        for(int i=0; i < dnIPs.length;i++){
            String[] ports_init = dnIPs[i].split(",");
            for(int k=0;k<ports_init.length;k++){
                String[] ports = ports_init[k].split("/");
                connectDataNode(ports);
                dataNode.returnStatus("abort");
            }
        }
        return true;
    }

    @Override
    public byte[] read(int fd) {
        FileDesc fileDesc = fileDescArrayList.get(fd-4096);
        if(fileDesc.getMode()==1){System.out.println("INFO: READ NOT ALLOWED");return null;}
        FileMetaData fileMetaData = nameNode.open(fileDesc.getFilepath(),0);
        DataBlock[] dataBlocks = fileMetaData.dataMap;
        byte[] resBytes = new byte[0];
        //CHANGE: 读文件时，读取每一个副本，直到读取到数据
        for (DataBlock dataBlock:dataBlocks){
            String[] ports;
            byte[] bytes = new byte[Config.BLOCKSIZE];
            for(int i=0;i<4;i++){
                if(i==0){
                    ports = dataBlock.dataNodeIP.split("/");
                }else {
                    ports = dataBlock.copyDataBlock[i-1].dataNodeIP.split("/");
                }
                try {
                    connectDataNode(ports);
                    bytes = dataNode.read((int) dataBlock.dtBlockId);
                    break;
                }catch (Exception ignored){
                }
            }

            ArrayList<Byte> bytes1 = new ArrayList<>();
            for(int i=0;i<bytes.length;i++){
                if(bytes[i]!=0x00){
                    bytes1.add(bytes[i]);
                }
            }
            byte[] tempResBytes = new byte[bytes1.size()+resBytes.length];
            for(int i=0;i<resBytes.length;i++){
                tempResBytes[i] = resBytes[i];
            }
            for(int i=0;i<bytes1.size();i++){
                tempResBytes[i+resBytes.length]=bytes1.get(i);
            }
            resBytes = tempResBytes.clone();
        }
        return resBytes;
    }

    @Override
    public void close(int fd) {
        try {
            FileDesc fileDesc = fileDescArrayList.get(fd-4096);
            nameNode.close(fileDesc.getFilepath());
            fileDescArrayList.set(fd-4096,null);
        }catch (Exception ignored){
        }
    }

    public FileDesc getFdesc(int fd){
        try {
            return fileDescArrayList.get(fd-4096);
        }catch (Exception e){
            return null;
        }
    }
}
