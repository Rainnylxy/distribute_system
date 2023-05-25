package api;


/**
* api/dataBlockArrayHelper.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年3月21日 星期二 上午10时22分30秒 CST
*/

abstract public class dataBlockArrayHelper
{
  private static String  _id = "IDL:api/dataBlockArray:1.0";

  public static void insert (org.omg.CORBA.Any a, api.DataBlock[] that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static api.DataBlock[] extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = api.DataBlockHelper.type ();
      __typeCode = org.omg.CORBA.ORB.init ().create_sequence_tc (0, __typeCode);
      __typeCode = org.omg.CORBA.ORB.init ().create_alias_tc (api.dataBlockArrayHelper.id (), "dataBlockArray", __typeCode);
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static api.DataBlock[] read (org.omg.CORBA.portable.InputStream istream)
  {
    api.DataBlock value[] = null;
    int _len0 = istream.read_long ();
    value = new api.DataBlock[_len0];
    for (int _o1 = 0;_o1 < value.length; ++_o1)
      value[_o1] = api.DataBlockHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, api.DataBlock[] value)
  {
    ostream.write_long (value.length);
    for (int _i0 = 0;_i0 < value.length; ++_i0)
      api.DataBlockHelper.write (ostream, value[_i0]);
  }

}
