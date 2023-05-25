package api;


/**
* api/FileMetaDataHelper.java .
* 由IDL-to-Java 编译器 (可移植), 版本 "3.2"生成
* 从api.idl
* 2023年5月23日 星期二 上午10时46分29秒 CST
*/

abstract public class FileMetaDataHelper
{
  private static String  _id = "IDL:api/FileMetaData:1.0";

  public static void insert (org.omg.CORBA.Any a, api.FileMetaData that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static api.FileMetaData extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [9];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[0] = new org.omg.CORBA.StructMember (
            "fileType",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[1] = new org.omg.CORBA.StructMember (
            "fileName",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_longlong);
          _members0[2] = new org.omg.CORBA.StructMember (
            "fileSize",
            _tcOf_members0,
            null);
          _tcOf_members0 = api.DataBlockHelper.type ();
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_sequence_tc (0, _tcOf_members0);
          _members0[3] = new org.omg.CORBA.StructMember (
            "dataMap",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_longlong);
          _members0[4] = new org.omg.CORBA.StructMember (
            "accessTime",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_longlong);
          _members0[5] = new org.omg.CORBA.StructMember (
            "createTime",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_longlong);
          _members0[6] = new org.omg.CORBA.StructMember (
            "modifyTime",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_recursive_tc ("");
          _members0[7] = new org.omg.CORBA.StructMember (
            "childFileMetaData",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[8] = new org.omg.CORBA.StructMember (
            "fileBlockId",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (api.FileMetaDataHelper.id (), "FileMetaData", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static api.FileMetaData read (org.omg.CORBA.portable.InputStream istream)
  {
    api.FileMetaData value = new api.FileMetaData ();
    value.fileType = istream.read_long ();
    value.fileName = istream.read_string ();
    value.fileSize = istream.read_longlong ();
    int _len0 = istream.read_long ();
    value.dataMap = new api.DataBlock[_len0];
    for (int _o1 = 0;_o1 < value.dataMap.length; ++_o1)
      value.dataMap[_o1] = api.DataBlockHelper.read (istream);
    value.accessTime = istream.read_longlong ();
    value.createTime = istream.read_longlong ();
    value.modifyTime = istream.read_longlong ();
    int _len1 = istream.read_long ();
    value.childFileMetaData = new api.FileMetaData[_len1];
    for (int _o2 = 0;_o2 < value.childFileMetaData.length; ++_o2)
      value.childFileMetaData[_o2] = api.FileMetaDataHelper.read (istream);
    value.fileBlockId = istream.read_long ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, api.FileMetaData value)
  {
    ostream.write_long (value.fileType);
    ostream.write_string (value.fileName);
    ostream.write_longlong (value.fileSize);
    ostream.write_long (value.dataMap.length);
    for (int _i0 = 0;_i0 < value.dataMap.length; ++_i0)
      api.DataBlockHelper.write (ostream, value.dataMap[_i0]);
    ostream.write_longlong (value.accessTime);
    ostream.write_longlong (value.createTime);
    ostream.write_longlong (value.modifyTime);
    ostream.write_long (value.childFileMetaData.length);
    for (int _i1 = 0;_i1 < value.childFileMetaData.length; ++_i1)
      api.FileMetaDataHelper.write (ostream, value.childFileMetaData[_i1]);
    ostream.write_long (value.fileBlockId);
  }

}
