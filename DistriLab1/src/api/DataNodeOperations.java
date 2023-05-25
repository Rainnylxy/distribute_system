package api;


/**
* api/DataNodeOperations.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��5��23�� ���ڶ� ����10ʱ46��29�� CST
*/

public interface DataNodeOperations 
{
  byte[] read (int block_id);
  void append (int block_id, byte[] bytes, String filename, int fileBlockId);
  int randomBlockId ();
  String returnStatus (String recv);
} // interface DataNodeOperations