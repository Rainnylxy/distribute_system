package impl;
//TODO: your implementation
import api.DataBlock;
import api.NameNodePOA;
import api.FileMetaData;

import config.Config;
import utils.FileSystem;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


/**
 *
 */
public class NameNodeImpl extends NameNodePOA{
    public NameNodeImpl(){
    };
    //��¼�ļ�״̬����hashmap�д�����˵��д������ռ�ݣ���Ҫ�ȴ�
    //CHANGE:ȡ��usingfileʹ�����׶��ύ��ͬclient�������ύд����
    //ArrayList<String> usingFile = new ArrayList<>();
    FileSystem fileSystem = new FileSystem("/","distrifs");

    /**
     * @param filepath
     * @param mode
     * @return
     */
    @Override
    public api.FileMetaData open(String filepath, int mode) {
        /*
        mode: 0ֻ���� 1ֻд�� 2�ɶ���д
         */
        FileMetaData fileMetaData = fileSystem.searchFile(filepath,null,0);
        if(fileMetaData == null){
            fileMetaData = fileSystem.addNewFile(filepath);
            writeFS();
        }
        //CHANGE:�޸��ļ�Ԫ����Ϊ�ɵ�
        if(mode == 5){
            fileMetaData=fileSystem.searchFile(filepath,fileMetaData.oldFileMetaData,1);
            fileSystem.dNBlockNum=new ArrayList<>(fileSystem.olddNBlockNum);
            return fileMetaData;
        }
        if(mode != 0){
            /*if(usingFile.contains(filepath)){
                return new FileMetaData("",0);
            }else{
                usingFile.add(filepath);
            }*/
        }else {
            fileMetaData.accessTime = System.currentTimeMillis();
            writeFS();
        }
        return fileMetaData;
    }

    @Override
    public void close(String fileInfo) {
        //usingFile.remove(fileInfo);
    }


    /**
     * @param filepath
     * @param filesize
     * @return ��file�ֿ飬���Է��ط����DataNode   ���䷽������Ѳ
     */
    @Override
    public String[] allocateDN(String filepath, int filesize) {
        FileMetaData fileMetaData = fileSystem.searchFile(filepath,null,0);
        if(fileMetaData == null){
            return null;
        }
        //CHANGE:�洢ԭʼDataNode���ݣ����ڻع�
        fileMetaData.oldFileMetaData=new FileMetaData(fileMetaData);
        fileSystem.olddNBlockNum=new ArrayList<>(fileSystem.dNBlockNum);

        fileMetaData.modifyTime = System.currentTimeMillis();
        DataBlock dataBlock = fileMetaData.getLastDataBlock();
        fileMetaData.fileSize+=filesize;

        //filesize+offsizeû�г���һ���飬ʹ�õ������һ��datablock��DataNode��block
        if(dataBlock!=null && filesize+dataBlock.offsize<Config.BLOCKSIZE){
            fileMetaData.dataMap[fileMetaData.dataMap.length-1].offsize = filesize+dataBlock.offsize;
            String[] r = new String[1];
            r[0] = dataBlock.dataNodeIP;
            for(int index=0;index<dataBlock.copyDataBlock.length;index++){
                r[0]=r[0]+","+dataBlock.copyDataBlock[index].dataNodeIP;
            }
            writeFS();
            return r;
        }

        //�����µ�filesize����ȥǰ���offsize
        if(dataBlock!=null){
            filesize = filesize + (int) dataBlock.offsize - Config.BLOCKSIZE;
        }
        //������Ҫ��block��Ŀ
        int blocks = filesize / Config.BLOCKSIZE + (filesize % Config.BLOCKSIZE != 0 ? 1 : 0);
        //���ϵ�һ��δ����block
        if(dataBlock!=null && dataBlock.offsize>0 && dataBlock.offsize<Config.BLOCKSIZE){
            blocks=blocks+1;
        }
        String[] results = new String[blocks];

        int startIndex=0;
        if(dataBlock!=null&&dataBlock.offsize>0&&dataBlock.offsize<Config.BLOCKSIZE){
            results[startIndex] = dataBlock.dataNodeIP;
            for(int index=0;index<dataBlock.copyDataBlock.length;index++){
                results[startIndex]=results[startIndex]+","+dataBlock.copyDataBlock[index].dataNodeIP;
            }
            startIndex++;
        }

        //TODO:ʹ����Ѳ�ķ�������DataNode��
        // startIndexΪDataNode�б���block��Ŀ���ٵ�DataNode�±꣬����Ѳ�Ŀ�ʼλ��
        int shortestId=0;
        for(int j = 0;j<fileSystem.dNBlockNum.size()-1;j++){
            if(fileSystem.dNBlockNum.get(j)>fileSystem.dNBlockNum.get(j+1)){
                shortestId = j+1;
            }
        }
        for(int i=0;i<blocks;i++){
            int index = (4*i+shortestId)%fileSystem.dataNodeData.size();
            results[startIndex+i] = fileSystem.dataNodeData.get(index);
            fileSystem.dNBlockNum.set(index,fileSystem.dNBlockNum.get(index)+1);
            DataBlock newdataBlock = new DataBlock(fileSystem.dataNodeData.get(index),fileSystem.dNBlockNum.get(index),0,fileMetaData.fileName,i,false);

            //CHANGE:������blockʱ��ͬʱ����3������
            newdataBlock.copyDataBlock = new DataBlock[3];
            for(int j = 4*i+1;j<4*i+4;j++){
                index = (j+shortestId)%fileSystem.dataNodeData.size();
                fileSystem.dNBlockNum.set(index,fileSystem.dNBlockNum.get(index)+1);
                newdataBlock.copyDataBlock[j-4*i-1]=new DataBlock(fileSystem.dataNodeData.get(index),fileSystem.dNBlockNum.get(index),0,fileMetaData.fileName,i,true);
            }

            for(int k=0;k<newdataBlock.copyDataBlock.length;k++){
                results[startIndex+i]=results[startIndex+i]+","+newdataBlock.copyDataBlock[k].dataNodeIP;
            }

            System.out.println(newdataBlock.dataNodeIP);
            System.out.println("print copy dataNlock");
            newdataBlock.printCopyDataBlock();

            fileMetaData.addOneDataBlock(newdataBlock,fileMetaData.dataMap.length);
            //�޸����һ��block��offsize
            if(i==blocks-1){
                fileMetaData.dataMap[fileMetaData.dataMap.length-1].offsize = filesize - i*Config.BLOCKSIZE;
                //CHANGE:��ͬ����һ���޸�offsize
                for(int t=0;t<3;t++){
                    fileMetaData.dataMap[fileMetaData.dataMap.length-1].copyDataBlock[t].offsize = filesize - i*Config.BLOCKSIZE;
                }
            }
        }
        writeFS();
        return results;
    }

    public boolean writeCopyOfBlock(DataBlock dataBlock){
        return true;
    };

    /**
     * ���ļ�ϵͳд�������
     */
    public void writeFS() {
        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("fileSystem.txt"));
            os.writeObject(this.fileSystem);
            os.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * ��ȡ�����е�Fsimage�ļ����������ļ�ϵͳ
     */
    public void readLoadFS() {
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("fileSystem.txt"));
            this.fileSystem = (FileSystem) is.readObject();
        }catch (Exception ignored){
        }
    }
}
