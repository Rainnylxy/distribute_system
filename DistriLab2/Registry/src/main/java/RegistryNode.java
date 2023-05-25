import org.apache.zookeeper.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author little rain
 * @date 2023/4/6 10:00
 * 监听服务状态，如果连接不上则删除，开启多个服务，手动关闭
 */
public class RegistryNode {
    private static ZooKeeper zooKeeper;
    private static List<String> services = new ArrayList<>();

    static {
        try {
            zooKeeper = new ZooKeeper("localhost:2181",6000*10,new WatchServices());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
        if(zooKeeper.exists("/registry",false)==null){
            zooKeeper.create("/registry","server".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }else {
            System.out.println("Registry exists!");
        }
        services = zooKeeper.getChildren("/registry",true);
        System.out.println(services);

        while (true){
            for(String service:services){
                //servicesAddress.add(Arrays.toString(zooKeeper.getData(service, null, null)));
                String address = new String(zooKeeper.getData("/registry/" + service, false, null));
                try {
                    URL url = new URL("http://"+address);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(10000);
                    conn.connect();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sBuffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine())!=null){
                        sBuffer.append(line).append("\r\n");
                    }
                    reader.close();
                }catch (Exception e){
                    zooKeeper.delete("/registry/"+service,-1);
                    Thread.sleep(2000);
                    break;
                }
            }
        }
    }

    public static class WatchServices implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {
            String path = watchedEvent.getPath();
            if("/registry".equals(path) && watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                System.out.println("services have changed!");
                try {
                    services = zooKeeper.getChildren("/registry",true);
                } catch (KeeperException | InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(services);
            }
        }
    }
}
