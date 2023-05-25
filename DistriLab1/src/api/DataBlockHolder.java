package api;

/**
* api/DataBlockHolder.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年5月23日 星期二 上午10时46分29秒 CST
*/

public final class DataBlockHolder implements org.omg.CORBA.portable.Streamable
{
  public api.DataBlock value = null;

  public DataBlockHolder ()
  {
  }

  public DataBlockHolder (api.DataBlock initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.DataBlockHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.DataBlockHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.DataBlockHelper.type ();
  }

}
