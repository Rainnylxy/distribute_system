import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author little rain
 * @date 2023/4/6 10:02
 */
public class Client {
    private static ZooKeeper zooKeeper;
    private static List<String> servicesAddress = new ArrayList<>();
    static {
        try {
            zooKeeper = new ZooKeeper("localhost:2181", 6000 * 10, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getSerAddress() throws InterruptedException, KeeperException {
        servicesAddress.clear();
        List<String> allServices = zooKeeper.getChildren("/registry",null);
        for(String service:allServices){
            servicesAddress.add(new String(zooKeeper.getData("/registry/"+service, null, null)));
        }
        System.out.println("ALL SERVICE:");
        System.out.println(servicesAddress);
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        while (true){
            getSerAddress();
            Thread.sleep(6000);
        }
    }
}
