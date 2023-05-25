import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author little rain
 * @date 2023/4/6 13:58
 * serviceName: web-service
 */
public class Service {
    private static ZooKeeper zooKeeper;
    private static String ipAddress="";
    private static String port="";

    static {
        try {
            zooKeeper = new ZooKeeper("localhost:2181",6000*10,new WatchService());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static class WatchService implements Watcher {

        @Override
        public void process(WatchedEvent watchedEvent) {

        }
    }



    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
        ipAddress = args[0];
        port = args[1];
        //注册服务
        String path = zooKeeper.create("/registry/webservice-",(ipAddress+":"+port).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        zooKeeper.exists(path,true);
        //开启http服务
        InetSocketAddress inetSocket = new InetSocketAddress(Integer.parseInt(port));
        HttpServer httpServer = HttpServer.create(inetSocket,10);
        httpServer.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                String response = "succeed connecting to "+ipAddress+":"+port;
                httpExchange.sendResponseHeaders(200,response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        });

        httpServer.setExecutor(null);
        httpServer.start();
        System.out.println("Web Service Start!");
    }
}
