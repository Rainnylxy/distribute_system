package api;


import java.util.ArrayList;
import java.util.List;

/**
 * api/DataBlock.java .
 * 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
 * 从api.idl
 * 2023年5月13日 星期六 上午11时23分56秒 CST
 */

public final class DataBlock implements org.omg.CORBA.portable.IDLEntity
{
  public String dataNodeIP = "";
  public long dtBlockId = (long)0;
  public long offsize = (long)0;
  //TODO:为每一个datablock类添加副本存储结构，
  //CHANGE:为每一个block添加四个属性
  // filename（所属文件名），
  // fileBlockId（在文件的第几个block）,
  // isCopy（是否是副本）
  // copyDataBlock存储副本存储位置
  public String filename = "";
  public int fileBlockId = 0;
  public boolean isCopy = false;
  public DataBlock copyDataBlock[] = new DataBlock[0];

  public void printCopyDataBlock(){
    for(int i=0;i<copyDataBlock.length;i++){
      System.out.println(copyDataBlock[i].dataNodeIP);
    }
  }

  //CHANGE:添加构造函数
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
