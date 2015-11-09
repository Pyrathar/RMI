/**
 * Created by raz on 10/15/15.
 */

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public interface HelloService extends Remote {

    String echo(String input) throws RemoteException;
    String login(String user, String input) throws NoSuchAlgorithmException, InvalidKeySpecException;
    String print(String filename, String printer) throws RemoteException;
    String queue() throws RemoteException;
    String start() throws RemoteException;
    String stop() throws RemoteException;
    String restart() throws RemoteException;
    String status() throws RemoteException;
    String readConfig(String parameter) throws RemoteException;
    String setConfig(String parameter, String value) throws RemoteException;


}

