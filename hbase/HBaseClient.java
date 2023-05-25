
// 客户端：与HBase集群交互，发送读取和写入请求。

import java.util.List;

public class HBaseClient {
    private Zookeeper zookeeper;
    private HMaster hMaster;

    public HBaseClient(Zookeeper zookeeper, HMaster hMaster) {
        this.zookeeper = zookeeper;
        this.hMaster = hMaster;
    }

    /**
     * 创建表
     * 
     * @param tableName      表名
     * @param columnFamilies 列族
     */
    public void createTable(String tableName, List<String> columnFamilies) {
        // 检查表是否已存在
        if (zookeeper.tableExists(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists.");
        }

        // 创建表
        hMaster.createTable(tableName, columnFamilies);
    }

    /**
     * 写入数据
     * 
     * @param tableName       表名
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @param value           值
     */
    public void put(String tableName, String rowKey, String columnFamily, String columnQualifier, String value) {
        // 获取表引用
        Table table = hMaster.getTable(tableName);

        // 将数据写入表
        table.put(rowKey, columnFamily, columnQualifier, value);
    }

    /**
     * 读取数据
     * 
     * @param tableName       表名
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @return 值
     */
    public String get(String tableName, String rowKey, String columnFamily, String columnQualifier) {
        // 获取表引用
        Table table = hMaster.getTable(tableName);

        // 从表中读取数据
        return table.get(rowKey, columnFamily, columnQualifier);
    }

    /**
     * 删除表
     * 
     * @param tableName 表名
     */
    public void deleteTable(String tableName) {
        // 检查表是否存在
        if (!zookeeper.tableExists(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " does not exist.");
        }

        // 删除表
        hMaster.deleteTable(tableName);
    }
}
