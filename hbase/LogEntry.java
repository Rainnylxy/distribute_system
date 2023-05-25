public class LogEntry {
    private String tableName;
    private String regionId;
    private String rowKey;
    private String columnFamily;
    private String columnQualifier;
    private String value;
    private boolean isDelete;

    public LogEntry(String tableName, String regionId, String rowKey, String columnFamily, String columnQualifier,
            String value) {
        this(tableName, regionId, rowKey, columnFamily, columnQualifier, value, false);
    }

    public LogEntry(String tableName, String regionId, String rowKey, String columnFamily, String columnQualifier,
            boolean isDelete) {
        this(tableName, regionId, rowKey, columnFamily, columnQualifier, null, isDelete);
    }

    public LogEntry(String tableName, String regionId, String rowKey, String columnFamily, String columnQualifier,
            String value, boolean isDelete) {
        this.tableName = tableName;
        this.regionId = regionId;
        this.rowKey = rowKey;
        this.columnFamily = columnFamily;
        this.columnQualifier = columnQualifier;
        this.value = value;
        this.isDelete = isDelete;
    }

    public String getTableName() {
        return tableName;
    }

    public String getRegionId() {
        return regionId;
    }

    public String getRowKey() {
        return rowKey;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public String getColumnQualifier() {
        return columnQualifier;
    }

    public String getValue() {
        return value;
    }

    public boolean isDelete() {
        return isDelete;
    }
}
