package api;


/**
* api/dataBlockArrayHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年3月21日 星期二 上午10时22分30秒 CST
*/

public final class dataBlockArrayHolder implements org.omg.CORBA.portable.Streamable
{
  public api.DataBlock value[] = null;

  public dataBlockArrayHolder ()
  {
  }

  public dataBlockArrayHolder (api.DataBlock[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.dataBlockArrayHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.dataBlockArrayHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.dataBlockArrayHelper.type ();
  }

}
