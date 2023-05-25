package utils;

import api.FileMetaData;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 文件系統类
 */
public class FileSystem implements Serializable {
    api.FileMetaData rootFileData;
    String fileSystemName;
    public ArrayList<Integer> olddNBlockNum;
    public ArrayList<String> olddataNodeData;
    public ArrayList<Integer> dNBlockNum = new ArrayList<>();
    public ArrayList<String> dataNodeData = new ArrayList<>();


    /**
     * @param rootpath
     * @param fileSystemName
     */
    public FileSystem(String rootpath,String fileSystemName) {
        this.fileSystemName = fileSystemName;
        rootFileData = new FileMetaData(rootpath,0);
        //写死DataNode的ip和端口号（有两个），形式类似于"<port1>/<port2>/<ip>"
        //CHANGE:设置DataNode为4个
        /*dataNodeData.add("1051/1052/localhost");
        dataNodeData.add("1053/1054/localhost");
        dataNodeData.add("1055/1056/localhost");
        dataNodeData.add("1057/1058/localhost");*/
        for(int i=0;i<4;i++){
            dataNodeData.add(String.valueOf(1051+2*i)+"/"+String.valueOf(1052+2*i)+"/localhost");
            dNBlockNum.add(0);
        }
    }


    public static String newFilename(){
        /* Wish the name has not appeared before in your NameNode, Good Luck _< */
        return String.valueOf((int) (Math.random() * Integer.MAX_VALUE))+"TEST";
    }


    //实现根据文件路径找到对应文件的元数据，该方法在filesystem中实现
    public api.FileMetaData searchFile(String filepath,FileMetaData fileMetaData,int mode){
        String[] splitStrs = filepath.split("/");
        FileMetaData tempFMD = rootFileData;
        //splitStrs = ["test","file1"]
        for (String splitStr : splitStrs) {
            int flag = 0;
            int length = tempFMD.childFileMetaData==null ? 0:tempFMD.childFileMetaData.length;
            for (int i = 0;i < length; i++) {
                if (splitStr.equals(tempFMD.childFileMetaData[i].fileName)) {
                    tempFMD = tempFMD.childFileMetaData[i];
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                return null;
            }
        }
        if(tempFMD.fileType == 1){
            if(mode==1){
                tempFMD.setValue(fileMetaData);
            }
            return tempFMD;
        }
        return null;
    }

    //实现根据文件路径创造新的文件
    public api.FileMetaData addNewFile(String filepath){
        String[] splitStrs = filepath.split("/");
        FileMetaData tempFMD = rootFileData;
        int addIndex;
        FileMetaData fatherFMD = tempFMD;
        for(addIndex=0;addIndex<splitStrs.length;addIndex++){
            int flag = 0;
            int length = tempFMD.childFileMetaData==null ? 0:tempFMD.childFileMetaData.length;
            for(int i=0;i<length;i++){
                System.out.println("error i: "+i);
                System.out.println("error length: "+tempFMD.childFileMetaData.length);
                if (splitStrs[addIndex].equals(tempFMD.childFileMetaData[i].fileName)) {
                    fatherFMD = tempFMD;
                    tempFMD = tempFMD.childFileMetaData[i];
                    flag = 1;
                    break;
                }
            }
            //在当前路径没有找到
            if(flag == 0){
                FileMetaData lastFile = new FileMetaData(splitStrs[splitStrs.length-1],1);
                for(int i= splitStrs.length-2;i>=addIndex;i--){
                    lastFile = new FileMetaData(splitStrs[i],0,lastFile);
                }
                tempFMD.addOneChild(lastFile);
                return new FileMetaData(splitStrs[splitStrs.length-1],1);
            }
        }
        //找到了但是是目录，则新建文件
        if(tempFMD.fileType == 0){
            FileMetaData lastFile = new FileMetaData(splitStrs[splitStrs.length-1],1);
            fatherFMD.addOneChild(lastFile);
            return lastFile;
        }
        return null;
    }

}

