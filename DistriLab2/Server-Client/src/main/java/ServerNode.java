import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author little rain
 * @date 2023/3/31 16:31
 */
public class ServerNode {
    private static ZooKeeper zooKeeper;
    private static Integer currChildsNum;
    private static List<String> aliveChilds = new ArrayList<>();
    private static List<String> allChilds;

    static {
        try {
            zooKeeper = new ZooKeeper("localhost:2181",60000*10,new WatchChild());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, KeeperException {
        if(zooKeeper.exists("/servernode",false)==null){
            zooKeeper.create("/servernode","server".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }else {
            System.out.println("servernode exists!");
        }

        allChilds = zooKeeper.getChildren("/servernode",true);
        System.out.println(allChilds);
        getAliveChilds();
        currChildsNum = aliveChilds.size();
        Thread.sleep(Long.MAX_VALUE);
    }

    public static void getAliveChilds() throws InterruptedException, KeeperException {
        aliveChilds.clear();
        for(int i=0;i<allChilds.size();i++){
            if(Arrays.equals(zooKeeper.getData("/servernode/" + allChilds.get(i), new WatchChildIsAlive(), null), "alive".getBytes())){
                aliveChilds.add(allChilds.get(i));
            }
        }
        System.out.println("alive clients: ");
        System.out.println(aliveChilds);
    }

    public static class WatchChildIsAlive implements Watcher{

        @Override
        public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getType().equals(Event.EventType.NodeDataChanged)){
                try {
                    System.out.println(watchedEvent.getPath()+" got changed!");
                    getAliveChilds();
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class WatchChild implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            String path = watchedEvent.getPath();
            if(path.equals("/servernode") && watchedEvent.getType()== Event.EventType.NodeChildrenChanged){
                try {
                    System.out.println(watchedEvent.getPath());
                    allChilds = zooKeeper.getChildren("/servernode", true);
                    getAliveChilds();
                    int modifyChildsNum = aliveChilds.size();
                    if(currChildsNum<modifyChildsNum){
                        System.out.println("add alive ClientNode");
                    }else {
                        System.out.println("delete offline ClientNode");
                    }
                    currChildsNum = modifyChildsNum;
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
