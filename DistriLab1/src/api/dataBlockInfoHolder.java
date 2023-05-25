package api;


/**
* api/dataBlockInfoHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年5月23日 星期二 上午10时46分29秒 CST
*/

public final class dataBlockInfoHolder implements org.omg.CORBA.portable.Streamable
{
  public String value[] = null;

  public dataBlockInfoHolder ()
  {
  }

  public dataBlockInfoHolder (String[] initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.dataBlockInfoHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.dataBlockInfoHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.dataBlockInfoHelper.type ();
  }

}
