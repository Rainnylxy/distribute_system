package api;


/**
* api/NameNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��5��23�� ���ڶ� ����10ʱ46��29�� CST
*/

public interface NameNodeOperations 
{

  //TODO: complete the interface design
  api.FileMetaData open (String filepath, int mode);

  //allocateBlock(string filepath,int filesize);
  void close (String fileInfo);
  String[] allocateDN (String filepath, int filesize);
} // interface NameNodeOperations
