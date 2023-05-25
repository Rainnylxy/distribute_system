
// 协调服务：管理HBase集群的元数据，负责协调和通信。

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zookeeper {
    private Map<String, List<String>> registeredTables;

    public Zookeeper() {
        this.registeredTables = new HashMap<>();
    }

    /**
     * 注册表信息到Zookeeper
     * 
     * @param tableName      表名
     * @param columnFamilies 列族列表
     */
    public void registerTable(String tableName, List<String> columnFamilies) {
        if (registeredTables.containsKey(tableName)) {
            System.out.println("Table " + tableName + " is already registered in Zookeeper.");
            return;
        }

        registeredTables.put(tableName, columnFamilies);
        System.out.println("Table " + tableName + " registered in Zookeeper.");
    }

    /**
     * 注销表信息从Zookeeper
     * 
     * @param tableName 表名
     */
    public void unregisterTable(String tableName) {
        if (!registeredTables.containsKey(tableName)) {
            System.out.println("Table " + tableName + " is not registered in Zookeeper.");
            return;
        }

        registeredTables.remove(tableName);
        System.out.println("Table " + tableName + " unregistered from Zookeeper.");
    }

    /**
     * 获取注册的表信息
     * 
     * @param tableName 表名
     * @return 列族列表
     */
    public List<String> getRegisteredTable(String tableName) {
        return registeredTables.get(tableName);
    }

    public void registerRegionServer(String regionServerId) {
        // 在Zookeeper中创建临时节点来表示RegionServer
        String nodePath = "/regionServers/" + regionServerId;
        // zookeeperClient.createNode(nodePath, true);

        System.out.println("RegionServer " + regionServerId + " registered in Zookeeper.");
    }

    public boolean tableExists(String tableName) {
        // 检查Zookeeper中是否存在表的节点
        String nodePath = "/tables/" + tableName;
        return registeredTables.containsKey(tableName);
    }

}
