
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 主节点：在HBase集群中负责管理和协调RegionServer和其他组件的活动。
public class HMaster {
    private Zookeeper zookeeper;
    private Map<String, Table> tables;
    private Map<String, HRegionServer> regionServers;

    public HMaster(Zookeeper zookeeper) {
        this.zookeeper = zookeeper;
        this.tables = new HashMap<>();
        this.regionServers = new HashMap<>();
    }

    /**
     * 创建表
     * 
     * @param tableName      表名
     * @param columnFamilies 列族
     */
    public void createTable(String tableName, List<String> columnFamilies) {
        // 检查表是否已存在
        if (tables.containsKey(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " already exists.");
        }

        // 创建表
        Table table = new Table(tableName, columnFamilies, getRegionServer());

        // 向Zookeeper注册表
        zookeeper.registerTable(tableName, columnFamilies);

        // 通知RegionServer有新的表创建
        // regionServer.addTable(table);

        // 将表添加到表列表中
        tables.put(tableName, table);
    }

    public HRegionServer getRegionServer() {
        // return regionServers;
        return null;
    }

    /**
     * 获取表引用
     * 
     * @param tableName 表名
     * @return Table对象
     */
    public Table getTable(String tableName) {
        // 检查表是否存在
        if (!tables.containsKey(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " does not exist.");
        }

        return tables.get(tableName);
    }

    /**
     * 创建RegionServer
     * 
     * @param regionServerId RegionServer的唯一标识符
     */
    public void createRegionServer(String regionServerId) {
        // 检查RegionServer是否已存在
        if (regionServers.containsKey(regionServerId)) {
            throw new IllegalArgumentException("RegionServer " + regionServerId + " already exists.");
        }

        // 创建RegionServer
        regionServers.put(regionServerId, new HRegionServer(regionServerId, this));

        // 向Zookeeper注册RegionServer
        zookeeper.registerRegionServer(regionServerId);
    }

    /**
     * 获取RegionServer引用
     * 
     * @param regionServerId RegionServer的唯一标识符
     * @return HRegionServer对象
     */
    public HRegionServer getRegionServer(String regionServerId) {
        // 检查RegionServer是否存在
        if (!regionServers.containsKey(regionServerId)) {
            throw new IllegalArgumentException("RegionServer " + regionServerId + " does not exist.");
        }

        return regionServers.get(regionServerId);
    }

    /**
     * 删除表
     * 
     * @param tableName 表名
     */
    public void deleteTable(String tableName) {
        // 检查表是否存在
        if (!tables.containsKey(tableName)) {
            throw new IllegalArgumentException("Table " + tableName + " does not exist.");
        }

        // 删除表
        tables.remove(tableName);
    }
}
