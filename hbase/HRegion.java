
// 区域：在HBase表中管理数据的逻辑单元。

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HRegion {
    private String tableName;
    private String regionId;
    private List<HFile> hFiles;
    private Map<String, Store> stores;

    public HRegion(String tableName, String regionId) {
        this.tableName = tableName;
        this.regionId = regionId;
        this.hFiles = new ArrayList<>();
        this.stores = new HashMap<>();
    }

    public String getRegionId() {
        return regionId;
    }

    public String getTableName() {
        return tableName;
    }

    public String getRegionName() {
        return regionId;
    }

    /**
     * 写入数据
     * 
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @param value           值
     */
    public void put(String rowKey, String columnFamily, String columnQualifier, String value) {
        // 获取或创建列族的Store对象
        Store store = getOrCreateStore(columnFamily);

        // 将数据写入Store的MemStore
        store.put(rowKey, columnQualifier, value);
    }

    /**
     * 读取数据
     * 
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @return 值
     */
    public String get(String rowKey, String columnFamily, String columnQualifier) {
        // 获取列族的Store对象
        Store store = getStore(columnFamily);

        if (store != null) {
            // 从Store中读取数据
            return store.get(rowKey, columnQualifier);
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

        for (Store store : stores.values()) {
            // 执行扫描操作
            List<String> storeResults = store.scan(startRowKey, endRowKey);
            results.addAll(storeResults);
        }

        return results;
    }

    /**
     * 删除数据
     * 
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     */
    public void delete(String rowKey, String columnFamily, String columnQualifier) {
        // 获取列族的Store对象
        Store store = getStore(columnFamily);

        if (store != null) {
            // 删除数据
            store.delete(rowKey, columnQualifier);
        }
    }

    /**
     * 获取或创建列族的Store对象
     * 
     * @param columnFamily 列族
     * @return Store对象
     */
    private Store getOrCreateStore(String columnFamily) {
        Store store = stores.get(columnFamily);

        if (store == null) {
            // 创建新的Store对象
            store = new Store(columnFamily);
            stores.put(columnFamily, store);
        }

        return store;
    }

    /**
     * 获取列族的Store对象
     * 
     * @param columnFamily 列族
     * @return Store对象
     */
    private Store getStore(String columnFamily) {
        return stores.get(columnFamily);
    }
}
