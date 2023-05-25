package api;

/**
* api/FileMetaDataHolder.java .
* ��IDL-to-Java ������ (����ֲ), �汾 "3.2"����
* ��api.idl
* 2023��5��23�� ���ڶ� ����10ʱ46��29�� CST
*/

public final class FileMetaDataHolder implements org.omg.CORBA.portable.Streamable
{
  public api.FileMetaData value = null;

  public FileMetaDataHolder ()
  {
  }

  public FileMetaDataHolder (api.FileMetaData initialValue)
  {
    value = initialValue;
  }

  public void _read (org.omg.CORBA.portable.InputStream i)
  {
    value = api.FileMetaDataHelper.read (i);
  }

  public void _write (org.omg.CORBA.portable.OutputStream o)
  {
    api.FileMetaDataHelper.write (o, value);
  }

  public org.omg.CORBA.TypeCode _type ()
  {
    return api.FileMetaDataHelper.type ();
  }

}
