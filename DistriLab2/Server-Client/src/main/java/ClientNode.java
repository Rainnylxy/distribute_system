import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @author little rain
 * @date 2023/3/31 16:48
 */
public class ClientNode {
    private static  ZooKeeper zooKeeper ;

    static {
        try {
            zooKeeper = new ZooKeeper("localhost:2181",6000,new MyWatcher());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int clientId;

    public static void main(String[] args) throws InterruptedException, KeeperException {
        clientId = Integer.parseInt(args[0]);
        if(zooKeeper.exists("/servernode/client-"+clientId,false)==null){
            zooKeeper.create("/servernode/client-"+clientId,"alive".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }
        zooKeeper.setData("/servernode/client-"+clientId,"alive".getBytes(),-1);
        zooKeeper.exists("/servernode/client-"+clientId,true);
        Thread.sleep(10000L *clientId);
        close();
    }

    public static void close(){
        try {
            zooKeeper.setData("/servernode/client-"+clientId,"offline".getBytes(),-1);
            zooKeeper.close();
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
    }

    public static class MyWatcher implements org.apache.zookeeper.Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            System.out.println(watchedEvent.getState());
            if(watchedEvent.getState().equals(Event.KeeperState.Closed)){
                System.out.println("client-"+clientId+" is offline");
            }
        }
    }
}
