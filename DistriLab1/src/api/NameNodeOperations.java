package api;


/**
* api/NameNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年5月23日 星期二 上午10时46分29秒 CST
*/

public interface NameNodeOperations 
{

  //TODO: complete the interface design
  api.FileMetaData open (String filepath, int mode);

  //allocateBlock(string filepath,int filesize);
  void close (String fileInfo);
  String[] allocateDN (String filepath, int filesize);
} // interface NameNodeOperations
