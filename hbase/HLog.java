// 日志：记录HBase中数据的更改操作

import java.util.ArrayList;
import java.util.List;

public class HLog {
    private List<LogEntry> logEntries;

    public HLog() {
        this.logEntries = new ArrayList<>();
    }

    /**
     * 写入日志
     * 
     * @param tableName       表名
     * @param regionId        区域标识符
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     * @param value           值
     */
    public void write(String tableName, String regionId, String rowKey, String columnFamily, String columnQualifier,
            String value) {
        LogEntry logEntry = new LogEntry(tableName, regionId, rowKey, columnFamily, columnQualifier, value);
        logEntries.add(logEntry);
    }

    /**
     * 写入删除日志
     * 
     * @param tableName       表名
     * @param regionId        区域标识符
     * @param rowKey          行键
     * @param columnFamily    列族
     * @param columnQualifier 列限定符
     */
    public void writeDelete(String tableName, String regionId, String rowKey, String columnFamily,
            String columnQualifier) {
        LogEntry logEntry = new LogEntry(tableName, regionId, rowKey, columnFamily, columnQualifier, null, true);
        logEntries.add(logEntry);
    }

    /**
     * 读取日志
     * 
     * @return 日志条目列表
     */
    public List<LogEntry> readLogs() {
        return logEntries;
    }
}
