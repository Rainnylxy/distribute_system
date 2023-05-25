
import java.util.Map;
import java.util.TreeMap;

// 存储文件：HBase中的底层存储文件，用于持久化存储已排序的键值对数据
public class HFile {
    private TreeMap<String, TreeMap<String, String>> data;

    public HFile(Map<String, TreeMap<String, String>> data) {
        this.data = new TreeMap<>(data);
    }

    /**
     * 从HFile中获取数据
     * 
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @return 值
     */
    public String get(String rowKey, String columnFamily, String columnQualifier) {
        TreeMap<String, String> columnData = data.get(rowKey);

        if (columnData != null) {
            return columnData.get(columnFamily + ":" + columnQualifier);
        }

        return null;
    }

    /**
     * 将数据写入HFile
     * 
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @param value           值
     */
    public void put(String rowKey, String columnFamily, String columnQualifier, String value) {
        TreeMap<String, String> columnData = data.get(rowKey);

        if (columnData == null) {
            columnData = new TreeMap<>();
            data.put(rowKey, columnData);
        }

        columnData.put(columnFamily + ":" + columnQualifier, value);
    }

    /**
     * 从HFile中删除数据
     * 
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     */
    public void delete(String rowKey, String columnFamily, String columnQualifier) {
        TreeMap<String, String> columnData = data.get(rowKey);

        if (columnData != null) {
            columnData.remove(columnFamily + ":" + columnQualifier);

            if (columnData.isEmpty()) {
                data.remove(rowKey);
            }
        }
    }

    /**
     * 获取HFile中的所有数据
     * 
     * @return 数据映射
     */
    public TreeMap<String, TreeMap<String, String>> getData() {
        return data;
    }
}
