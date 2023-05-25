// 内存存储：在内存中暂存写入的数据，直到达到一定阈值后刷入Store。

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class MemStore {
    private TreeMap<String, TreeMap<String, String>> data;

    public MemStore() {
        this.data = new TreeMap<>();
    }

    /**
     * 写入数据
     * 
     * @param rowKey          行键
     * @param columnQualifier 列限定符
     * @param value           值
     */
    public void put(String rowKey, String columnQualifier, String value) {
        TreeMap<String, String> columnData = data.get(rowKey);

        if (columnData == null) {
            columnData = new TreeMap<>();
            data.put(rowKey, columnData);
        }

        columnData.put(columnQualifier, value);
    }

    /**
     * 读取数据
     * 
     * @param rowKey          行键
     * @param columnQualifier 列限定符
     * @return 值
     */
    public String get(String rowKey, String columnQualifier) {
        TreeMap<String, String> columnData = data.get(rowKey);

        if (columnData != null) {
            return columnData.get(columnQualifier);
        }

        return null;
    }

    /**
     * 扫描数据
     * 
     * @param startRowKey 起始行键
     * @param endRowKey   结束行键
     * @return 扫描结果
     */
    public List<String> scan(String startRowKey, String endRowKey) {
        List<String> results = new ArrayList<>();

        SortedMap<String, TreeMap<String, String>> subMap = data.subMap(startRowKey, endRowKey);

        for (TreeMap<String, String> columnData : subMap.values()) {
            results.addAll(columnData.values());
        }

        return results;
    }

    /**
     * 删除数据
     * 
     * @param rowKey          行键
     * @param columnQualifier 列限定符
     */
    public void delete(String rowKey, String columnQualifier) {
        TreeMap<String, String> columnData = data.get(rowKey);

        if (columnData != null) {
            columnData.remove(columnQualifier);

            if (columnData.isEmpty()) {
                data.remove(rowKey);
            }
        }
    }

    /**
     * 判断MemStore是否为空
     * 
     * @return true为空，false不为空
     */
    public boolean isEmpty() {
        return data.isEmpty();
    }

    /**
     * 清空MemStore
     */
    public void clear() {
        data.clear();
    }

    /**
     * 获取MemStore中的数据
     * 
     * @return MemStore中的数据
     */
    public TreeMap<String, TreeMap<String, String>> getData() {
        return data;
    }
}
