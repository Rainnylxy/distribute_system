package api;


/**
* api/dataBlockInfoHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��5��23�� ���ڶ� ����10ʱ46��29�� CST
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
