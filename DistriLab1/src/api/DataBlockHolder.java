package api;

/**
* api/DataBlockHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��5��23�� ���ڶ� ����10ʱ46��29�� CST
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
