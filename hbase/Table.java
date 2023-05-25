import java.util.List;

public class Table {
    private String tableName;
    private List<String> columnFamilies;
    private HRegionServer regionServer;

    public Table(String tableName, List<String> columnFamilies, HRegionServer regionServer) {
        this.tableName = tableName;
        this.columnFamilies = columnFamilies;
        this.regionServer = regionServer;
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
        // 获取HRegion的引用
        HRegion region = regionServer.getRegion(tableName, rowKey);

        // 将数据写入HRegion
        region.put(rowKey, columnFamily, columnQualifier, value);
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
        // 获取HRegion的引用
        HRegion region = regionServer.getRegion(tableName, rowKey);

        // 从HRegion中读取数据
        return region.get(rowKey, columnFamily, columnQualifier);
    }

    /**
     * 扫描数据
     * 
     * @param startRowKey 起始行键
     * @param endRowKey   结束行键
     * @return 扫描结果
     */
    public List<String> scan(String startRowKey, String endRowKey) {
        // 获取HRegion的引用
        HRegion region = regionServer.getRegion(tableName, startRowKey);

        // 执行扫描操作
        return region.scan(startRowKey, endRowKey);
    }

    /**
     * 删除数据
     * 
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     */
    public void delete(String rowKey, String columnFamily, String columnQualifier) {
        // 获取HRegion的引用
        HRegion region = regionServer.getRegion(tableName, rowKey);

        // 删除数据
        region.delete(rowKey, columnFamily, columnQualifier);
    }
}
