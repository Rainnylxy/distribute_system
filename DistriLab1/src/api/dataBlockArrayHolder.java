package api;


/**
* api/dataBlockArrayHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��3��21�� ���ڶ� ����10ʱ22��30�� CST
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
