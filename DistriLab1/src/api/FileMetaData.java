package api;


/**
 * api/FileMetaData.java .
 * 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
 * 从api.idl
 * 2023年3月26日 星期日 下午12时53分41秒 CST
 */

public final class FileMetaData implements org.omg.CORBA.portable.IDLEntity
{
  public int fileType = (int)0;
  public String fileName = "";
  public long fileSize = (long)0;
  public api.DataBlock dataMap[] = new DataBlock[0];
  public long accessTime = (long)0;
  public long createTime = (long)0;
  public long modifyTime = (long)0;
  public api.FileMetaData childFileMetaData[] = new FileMetaData[0];
  public FileMetaData oldFileMetaData;
  public int fileBlockId = (int)0;

  public FileMetaData ()
  {
  } // ctor

  public FileMetaData (int _fileType, String _fileName, long _fileSize, api.DataBlock[] _dataMap, long _accessTime, long _createTime, long _modifyTime, api.FileMetaData[] _childFileMetaData, int _fileBlockId)
  {
    fileType = _fileType;
    fileName = _fileName;
    fileSize = _fileSize;
    dataMap = _dataMap;
    accessTime = _accessTime;
    createTime = _createTime;
    modifyTime = _modifyTime;
    childFileMetaData = _childFileMetaData;
    fileBlockId = _fileBlockId;
  } // ctor
  public FileMetaData(String fileName,int fileType)
  {
    this.fileName=fileName;
    this.fileType=fileType;
    this.createTime = System.currentTimeMillis();
  }
  public FileMetaData(String fileName,int fileType,api.FileMetaData oneChild){
    this.fileName=fileName;
    this.fileType=fileType;
    addOneChild(oneChild);
  }

  public FileMetaData(FileMetaData fileMetaData){
    fileType = fileMetaData.fileType;
    fileName = fileMetaData.fileName;
    fileSize = fileMetaData.fileSize;
    dataMap = fileMetaData.dataMap;
    accessTime = fileMetaData.accessTime;
    createTime = fileMetaData.createTime;
    modifyTime = fileMetaData.modifyTime;
    childFileMetaData = fileMetaData.childFileMetaData;
    fileBlockId = fileMetaData.fileBlockId;
    oldFileMetaData = fileMetaData.oldFileMetaData;
  }

  public void setValue(FileMetaData fileMetaData){
    fileType = fileMetaData.fileType;
    fileName = fileMetaData.fileName;
    fileSize = fileMetaData.fileSize;
    dataMap = fileMetaData.dataMap;
    accessTime = fileMetaData.accessTime;
    createTime = fileMetaData.createTime;
    modifyTime = fileMetaData.modifyTime;
    childFileMetaData = fileMetaData.childFileMetaData;
    fileBlockId = fileMetaData.fileBlockId;
    oldFileMetaData = fileMetaData.oldFileMetaData;
  }

  public void addOneChild(api.FileMetaData childMD){
    int length = childFileMetaData==null ? 0:childFileMetaData.length;
    api.FileMetaData[] newFMD=new FileMetaData[length+1];
    int i;
    for(i=0;i<length;i++){
      newFMD[i]=childFileMetaData[i];
    }
    newFMD[i]=childMD;
    childFileMetaData = newFMD;
  }

  public void addOneDataBlock(DataBlock dataBlock,int fileBlockId){
    DataBlock[] newDataMap = dataMap;
    if(dataMap.length == fileBlockId){
      newDataMap = new DataBlock[dataMap.length+1];
      System.arraycopy(dataMap, 0, newDataMap, 0, dataMap.length);
      newDataMap[fileBlockId]=dataBlock;
      dataMap = newDataMap.clone();
    }else {
      DataBlock[] tempDB = new DataBlock[dataMap.length+1];
      tempDB = dataMap.clone();
      tempDB[dataMap.length]=dataBlock;
      dataMap = tempDB.clone();
    }
  }

  public DataBlock getLastDataBlock(){
    if(dataMap.length == 0){
      return null;
    }else {
      return dataMap[dataMap.length-1];
    }
  }
} // class FileMetaData
