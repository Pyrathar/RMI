import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;

/**
 * Created by raz on 10/15/15.
 */
public class AplicationServer {
    public static void main(String[] args) throws RemoteException {

        Registry registry = LocateRegistry.createRegistry(5099);
        registry.rebind("hello",new HelloServant());
    }

    }


