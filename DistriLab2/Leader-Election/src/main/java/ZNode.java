import com.sun.org.apache.bcel.internal.generic.FADD;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author little rain
 * @date 2023/3/30 15:45
 */
public class ZNode{
    public static ZooKeeper zk;
    public static String ELECTION = "/election";
    public static String NAMEPATH = "/election/node-";
    public static String currentZnodePath;
    public static String watchZnodePath;


    /*public ZNode(int id) throws IOException, InterruptedException {
        this.id = id;
        this.zk = new ZooKeeper("localhost:2181",60000*10, this);
    }*/


    public static void main(String [] args) throws IOException, InterruptedException, KeeperException {
        zk = new ZooKeeper("localhost:2181",60000*100,new ProcessNodeWatcher());
        if(zk.exists(ELECTION,false)==null){
            zk.create(ELECTION,"".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
        }else {
            System.out.println("namespace exists!");
        }
        try {
            currentZnodePath = zk.create(NAMEPATH,"childNode".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }
        System.out.println("succeed creating znode with name "+currentZnodePath);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            leaderElection();
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }

    }


    public static void leaderElection() throws InterruptedException, KeeperException {
        //while (true){
            List<String> children = zk.getChildren(ELECTION, false);
            Collections.sort(children);
            if(children.get(0).equals(currentZnodePath.replace("/election/",""))){
                System.out.println(currentZnodePath+": I am the leader");
                zk.setData(ELECTION,currentZnodePath.getBytes(),-1);
                zk.exists(currentZnodePath,true);
                //break;
            }else{
                System.out.println(currentZnodePath+": I am follower\nfollowing "+children.get(0));
                zk.setData(ELECTION,(ELECTION+"/"+children.get(0)).getBytes(),-1);
                System.out.println("Setting watch on: "+ ELECTION+"/"+children.get(0));
                watchZnodePath=ELECTION+"/"+children.get(0);
                zk.exists(ELECTION+"/"+children.get(0),true);
            }
            System.out.println("election content: " + new String(zk.getData(ELECTION,true,null)));
            Thread.sleep(Long.MAX_VALUE);
        //}
    }



    public static class ProcessNodeWatcher implements Watcher{

        @Override
        public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted){
                if(watchedEvent.getPath().equals(watchZnodePath)){
                    try {
                        System.out.println(watchedEvent.getPath()+" was deleted, Triggering leader election!");
                        leaderElection();
                    } catch (InterruptedException | KeeperException e) {
                        e.printStackTrace();
                    }
                }else if(watchedEvent.getPath().equals(currentZnodePath)){
                    System.out.println("I was deleted, System exit!");
                    System.exit(0);
                }
            }
        }
    }

}
