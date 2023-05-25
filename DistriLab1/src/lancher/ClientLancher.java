package lancher;

import api.Client;
import api.NameNode;
import api.NameNodeHelper;
import impl.ClientImpl;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import static java.util.logging.Level.parse;

public class ClientLancher {

    static final String CMD_OPEN = "open ";
    static final String CMD_APPEND = "append ";
    static final String CMD_READ = "read ";
    static final String CMD_CLOSE = "close ";
    static final String CMD_EXIT = "exit";
    static final String CMD_HELP = "help";

    static ClientImpl clientImpl;

    public static class Input implements Runnable {

        @Override
        public void run() {
            Scanner in = new Scanner(System.in);
            help();
            while (true) {
                try {
                    String s = in.nextLine();
                    parse(s);
                }catch (Exception e){
                   help();
                }
            }
        }

        void parse(String str) throws InterruptedException {
            if (str.startsWith(CMD_OPEN)) {
                str = str.substring(CMD_OPEN.length());
                String filepath = str.substring(0, str.indexOf(' '));
                String openmode = str.substring(str.indexOf(' ')+1);
                int mode;
                switch (openmode){
                    case "r":
                        mode=0;
                        break;
                    case "w":
                        mode=1;
                        break;
                    case "rw":
                        mode=2;
                        break;
                    default: mode=0;
                }
                long fd = clientImpl.open(filepath, mode);
                if(fd == 0){
                    System.out.println("INFO: "+filepath+" fd: NULL");
                }else {
                    System.out.println("INFO: "+filepath+" fd:  "+fd);
                }
            } else if (str.startsWith(CMD_APPEND)) {
                String infor = str.substring(CMD_APPEND.length());
                int fp = Integer.parseInt(infor.substring(0,infor.indexOf(' ')));
                byte[] bytes = infor.substring(infor.indexOf(' ') + 1).getBytes();
                System.out.println("run append");
                if(clientImpl.append(fp,bytes)){
                    System.out.println("INFO: write done");
                }else {
                    System.out.println("INFO: write error");
                };
            } else if (str.startsWith(CMD_READ)) {
                int fp = Integer.parseInt(str.substring(CMD_READ.length()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] data=null;
                byte[] data1=clientImpl.read(fp);
                try {
                    baos.write(data1,0,data1.length);			//写出字节数组
                    baos.flush();				//强制刷新输出流
                    data = baos.toByteArray();	//获取数据
                    System.out.println(new String(data,0,data.length));		//打印结果
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (str.startsWith(CMD_CLOSE)) {
                int fp = Integer.parseInt(str.substring(CMD_CLOSE.length()));
                clientImpl.close(fp);
                System.out.println("INFO: fd "+ String.valueOf((int)fp) +"closed");
            } else if (str.startsWith(CMD_HELP)) {
                help();
            } else if (str.startsWith(CMD_EXIT)) {
                System.out.println("INFO: bye");
                System.exit(0);
            } else {
                System.out.println("please input right commands");
                help();
            }
        }

        void help() {
            String str = "Commands:\n" +
                    CMD_OPEN + "\n" +
                    CMD_APPEND + "\n" +
                    CMD_READ + "\n" +
                    CMD_CLOSE + "\n" +
                    CMD_HELP + "\n" +
                    CMD_EXIT;
            System.out.println(str);
        }
    }

    public static void main(String[] args) {
        try {
            args = new String[6];
            args[0]="-port";
            args[2]="-ORBInitialPort";
            args[4]="-ORBInitialHost";
            args[1]="1049";
            args[3]="1050";
            args[5]="localhost";
            // ORB initial
            ORB orb = ORB.init(args, null);

            // Get the context
            org.omg.CORBA.Object obRef = orb
                    .resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(obRef);

            NameComponent nc = new NameComponent("NameNodeLancher", "");
            NameComponent[] path = { nc };

            NameNode nameNode = NameNodeHelper.narrow(ncRef.resolve(path));
            clientImpl = new ClientImpl(nameNode);
            new Thread(new Input()).start();
        } catch (Exception e) {
            System.out.println("Calling CORBA fails." + e);
        }
    }
}
