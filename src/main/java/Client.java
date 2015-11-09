
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Created by raz on 10/15/15.
 */
public class Client {


    public static void main(String[]args) throws RemoteException, NotBoundException, MalformedURLException, InvalidKeySpecException, NoSuchAlgorithmException {

        HelloService service = (HelloService)Naming.lookup("rmi://localhost:5099/hello");


        System.out.println("---"+service.echo("Hey Server"));

        System.out.println("---"+service.print("filename", "printer"));

        System.out.println("---"+service.queue());

        System.out.println("---"+service.start());

        System.out.println("---"+service.stop());

        System.out.println("---"+service.restart());

        System.out.println("---"+service.status());

        System.out.println("---"+service.readConfig("parameter"));

        System.out.println("---"+service.setConfig("parameter","value"));

        System.out.println("---"+service.login("seph","1q2w3e4r"));

    }
}
