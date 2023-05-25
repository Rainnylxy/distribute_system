import java.util.ArrayList;
import java.util.List;

// 区域服务器：在HBase集群中管理一个或多个HRegion的数据。
public class HRegionServer {
    private String regionServerId;
    private HMaster hMaster;
    private List<HRegion> regions;
    private HLog hLog;

    public HRegionServer(String regionServerId, HMaster hMaster) {
        this.regionServerId = regionServerId;
        this.hMaster = hMaster;
        this.regions = new ArrayList<>();
        this.hLog = new HLog();
    }

    /**
     * 创建HRegion并添加到RegionServer
     * 
     * @param tableName 表名
     * @param regionId  区域标识符
     */
    public void createRegion(String tableName, String regionId) {
        HRegion region = new HRegion(tableName, regionId);
        regions.add(region);
    }

    /**
     * 获取HRegion的引用
     * 
     * @param tableName 表名
     * @param regionId  区域标识符
     * @return HRegion对象
     */
    public HRegion getRegion(String tableName, String regionId) {
        for (HRegion region : regions) {
            if (region.getTableName().equals(tableName) && region.getRegionId().equals(regionId)) {
                return region;
            }
        }

        return null;
    }

    /**
     * 处理写请求
     * 
     * @param tableName       表名
     * @param regionId        区域标识符
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @param value           值
     */
    public void processPut(String tableName, String regionId, String rowKey, String columnFamily,
            String columnQualifier, String value) {
        HRegion region = getRegion(tableName, regionId);
        if (region != null) {
            region.put(rowKey, columnFamily, columnQualifier, value);
            // 写入HLog
            hLog.write(tableName, regionId, rowKey, columnFamily, columnQualifier, value);
        }
    }

    /**
     * 处理读请求
     * 
     * @param tableName       表名
     * @param regionId        区域标识符
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @return 值
     */
    public String processGet(String tableName, String regionId, String rowKey, String columnFamily,
            String columnQualifier) {
        HRegion region = getRegion(tableName, regionId);
        if (region != null) {
            return region.get(rowKey, columnFamily, columnQualifier);
        }
        return null;
    }

    /**
     * 处理删除请求
     * 
     * @param tableName       表名
     * @param regionId        区域标识符
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     */
    public void processDelete(String tableName, String regionId, String rowKey, String columnFamily,
            String columnQualifier) {
        HRegion region = getRegion(tableName, regionId);
        if (region != null) {
            region.delete(rowKey, columnFamily, columnQualifier);
            // 写入HLog
            hLog.writeDelete(tableName, regionId, rowKey,
                    columnFamily, columnQualifier);
        }
    }

    /**
     * 从HLog中恢复数据
     */
    public void recoverFromHLog() {
        List<LogEntry> logEntries = hLog.readLogs();

        for (LogEntry logEntry : logEntries) {
            String tableName = logEntry.getTableName();
            String regionId = logEntry.getRegionId();
            String rowKey = logEntry.getRowKey();
            String columnFamily = logEntry.getColumnFamily();
            String columnQualifier = logEntry.getColumnQualifier();
            String value = logEntry.getValue();

            HRegion region = getRegion(tableName, regionId);
            if (region != null) {
                region.put(rowKey, columnFamily, columnQualifier, value);
            }
        }
    }

}