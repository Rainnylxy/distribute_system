package impl;
//TODO: your implementation
import api.DataBlock;
import api.DataNodePOA;
import config.Config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataNodeImpl extends DataNodePOA {
    int BLOCK_SIZE = Config.BLOCKSIZE;
    String dir = System.getProperty("user.dir");
    String status = "abort";
    //CHANGE:为每一个datanode添加datablockList,便于打印出相关信息
    ArrayList<String> filenames = new ArrayList<>();
    ArrayList<Integer> fileBlockIds = new ArrayList<>();

    /*public DataNodeImpl(int id) {
        this.dir = this.dir+"/datanode_"+id+"/";
    }*/

    public DataNodeImpl(){}

    public DataNodeImpl(String port){
        if(port.equals("1051")){
            dir = dir + "/datanodeFiles/datanode1/";
        }else if(port.equals("1053")){
            dir = dir +"/datanodeFiles/datanode2/";
        }else if(port.equals("1055")){
            dir = dir +"/datanodeFiles/datanode3/";
        }else if(port.equals("1057")){
            dir = dir +"/datanodeFiles/datanode4/";
        }
    }

    @Override
    //CHANGE:实现两阶段提交同一时间只能成功实现一个写请求
    public String returnStatus(String recv){
        if("prepare".equals(recv)){
            if("commit".equals(status)||"prepare".equals(status)){
                return "abort";
            }
            status="prepare";
            return "ok";
        }else if("abort".equals(recv)){
            status="abort";
            return "abort";
        }else if("commit".equals(recv)){
            status="commit";
        }
        return "abort";
    }

    @Override
    public byte[] read(int block_id) {
        try {
            FileInputStream fis = new FileInputStream(dir + String.valueOf(block_id)+".txt");
            byte[] bytes = new byte[BLOCK_SIZE];
            fis.read(bytes);
            fis.close();
            ArrayList<Byte> bytes1 = new ArrayList<>();
            for(int i=0;i<bytes.length;i++){
                if(bytes[i]!=0x00){
                    bytes1.add(bytes[i]);
                }
            }
            byte[] bytes2 = new byte[Config.BLOCKSIZE];
            for(int i=0;i<bytes1.size();i++){
                bytes2[i]=bytes1.get(i);
            }
            return bytes2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }


    //TODO:为DataNode添加dump()函数，需要知道存储的是哪个文件的哪一个block的副本
    public void dump(){
        for(int i=0;i<filenames.size();i++){
            System.out.println("DataNode的数据如下:");
            System.out.println("Block"+i);
            System.out.println("来自于文件: "+filenames.get(i)+" 的第 "+fileBlockIds.get(i)+" 个block");
        }
    }

    /**
     * @param block_id 指的是在该DataNode里的blockID，由NameNode分配，而不是文件自身的blockid
     * @param bytes 需要写入的数据
     */
    @Override
    public void append(int block_id, byte[] bytes,String filename,int blockId){
        if("commit".equals(status)){
            if(block_id>fileBlockIds.size()){
                filenames.add(filename);
                fileBlockIds.add(blockId);
            }
            try {
                FileOutputStream fos = new FileOutputStream(dir+ String.valueOf(block_id)+".txt",true);
                ArrayList<Byte> bytes1 = new ArrayList<>();
                for(int i=0;i<bytes.length;i++){
                    if(bytes[i]!=0x00){
                        bytes1.add(bytes[i]);
                    }
                }
                byte[] bytes2 = new byte[bytes1.size()];
                for(int i=0;i<bytes1.size();i++){
                    bytes2[i]=bytes1.get(i);
                }
                fos.write(bytes2);
                fos.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            dump();
        }
    }

    @Override
    public int randomBlockId() {
        return 0;
    }
}
