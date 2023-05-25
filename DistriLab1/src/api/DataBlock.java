package api;


import java.util.ArrayList;
import java.util.List;

/**
 * api/DataBlock.java .
 * ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
 * ��api.idl
 * 2023��5��13�� ������ ����11ʱ23��56�� CST
 */

public final class DataBlock implements org.omg.CORBA.portable.IDLEntity
{
  public String dataNodeIP = "";
  public long dtBlockId = (long)0;
  public long offsize = (long)0;
  //TODO:Ϊÿһ��datablock����Ӹ����洢�ṹ��
  //CHANGE:Ϊÿһ��block����ĸ�����
  // filename�������ļ�������
  // fileBlockId�����ļ��ĵڼ���block��,
  // isCopy���Ƿ��Ǹ�����
  // copyDataBlock�洢�����洢λ��
  public String filename = "";
  public int fileBlockId = 0;
  public boolean isCopy = false;
  public DataBlock copyDataBlock[] = new DataBlock[0];

  public void printCopyDataBlock(){
    for(int i=0;i<copyDataBlock.length;i++){
      System.out.println(copyDataBlock[i].dataNodeIP);
    }
  }

  //CHANGE:��ӹ��캯��
  public DataBlock(String dataNodeIP,long dtBlockId, long offsize,String filename,int fileBlockId,boolean isCopy){
    this.dataNodeIP = dataNodeIP;
    this.dtBlockId = dtBlockId;
    this.offsize = offsize;
    this.filename = filename;
    this.fileBlockId = fileBlockId;
    this.isCopy = isCopy;
  }


  public DataBlock ()
  {

  } // ctor

  public DataBlock (String _dataNodeIP, long _dtBlockId, long _offsize)
  {
    dataNodeIP = _dataNodeIP;
    dtBlockId = _dtBlockId;
    offsize = _offsize;
  } // ctor

} // class DataBlock
