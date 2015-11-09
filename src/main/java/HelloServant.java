
import java.io.UnsupportedEncodingException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.security.spec.InvalidKeySpecException;


/**
 * Created by raz on 10/15/15.
 */
public class HelloServant extends UnicastRemoteObject implements HelloService {


    public HelloServant() throws RemoteException {
        super();
    }

    public String echo(String input) throws RemoteException {
        return "From Server" + input;
    }
    
    public String login(String user, String input) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String result = Database.login(user, input);
        return result;
    }


    public String print(String filename, String printer) throws RemoteException {
        return "print" + filename + printer;
    }

    public String queue() throws RemoteException {
        return "queue";
    }

    public String start() throws RemoteException {
        return "start";
    }

    public String stop() throws RemoteException {
        return "stop";
    }

    public String restart() throws RemoteException {
        return "restart";
    }

    public String status() throws RemoteException {
        return "status";
    }

    public String readConfig(String parameter) throws RemoteException {
        return "readConfig";
    }

    public String setConfig(String parameter, String value) throws RemoteException {
        return "setConfig";
    }

    public String hashit(String input) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance(input);
        byte[] hash = digest.digest(input.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String hashed = hexString.toString();
        return hashit(hashed);

    }


    public static String fuckayou() {
        String fuck = "fakayou";
        return fuck;
    }

}





