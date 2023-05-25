
import java.util.ArrayList;
import java.util.List;

// 存储：在HRegion中存储数据。
public class Store {
    private String columnFamily;
    private MemStore memStore;
    private List<HFile> hFiles;

    public Store(String columnFamily) {
        this.columnFamily = columnFamily;
        this.memStore = new MemStore();
        this.hFiles = new ArrayList<>();
    }

    /**
     * 写入数据
     * 
     * @param rowKey          行键
     * @param columnQualifier 列限定符
     * @param value           值
     */
    public void put(String rowKey, String columnQualifier, String value) {
        memStore.put(rowKey, columnQualifier, value);
    }

    /**
     * 读取数据
     * 
     * @param rowKey          行键
     * @param columnQualifier 列限定符
     * @return 值
     */
    public String get(String rowKey, String columnQualifier) {
        return memStore.get(rowKey, columnQualifier);
    }

    /**
     * 扫描数据
     * 
     * @param startRowKey 起始行键
     * @param endRowKey   结束行键
     * @return 扫描结果
     */
    public List<String> scan(String startRowKey, String endRowKey) {
        return memStore.scan(startRowKey, endRowKey);
    }

    /**
     * 删除数据
     * 
     * @param rowKey          行键
     * @param columnQualifier 列限定符
     */
    public void delete(String rowKey, String columnQualifier) {
        memStore.delete(rowKey, columnQualifier);
    }

    /**
     * 刷新MemStore并生成新的HFile
     */
    public void flush() {
        if (memStore.isEmpty()) {
            return;
        }

        // 将MemStore中的数据刷新到新的HFile
        HFile hFile = new HFile(memStore.getData());
        hFiles.add(hFile);

        // 清空MemStore
        memStore.clear();
    }
}
