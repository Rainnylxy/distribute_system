package api;


/**
* api/DataNodeOperations.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年5月23日 星期二 上午10时46分29秒 CST
*/

public interface DataNodeOperations 
{
  byte[] read (int block_id);
  void append (int block_id, byte[] bytes, String filename, int fileBlockId);
  int randomBlockId ();
  String returnStatus (String recv);
} // interface DataNodeOperations
