package lancher;

import api.DataNode;
import api.DataNodeHelper;
import api.NameNode;
import api.NameNodeHelper;
import impl.DataNodeImpl;
import impl.NameNodeImpl;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import java.util.Scanner;

public class DataNodeLancher{
    public static void main(String[] args) {
        try{
            // create and initialize the ORB
            ORB orb = ORB.init(args, null);

            // get reference to rootpoa & activate the POAManager
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            DataNodeImpl dataNodeImpl = new DataNodeImpl(args[1]);

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(dataNodeImpl);
            DataNode href = DataNodeHelper.narrow(ref);

            // get the root naming context
            // NameService invokes the name service
            org.omg.CORBA.Object objRef =
                    orb.resolve_initial_references("NameService");
            // Use NamingContextExt which is part of the Interoperable
            // Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            String name = "DataNodeLancher";
            NameComponent[] path = ncRef.to_name( name );
            ncRef.rebind(path, href);

            System.out.println("server.DataNodeLancher ready and waiting ...");

            // wait for invocations from clients
            orb.run();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("server.DataNodeLancher Exiting ...");
    }
}
