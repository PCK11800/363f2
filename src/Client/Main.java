import client.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

public class Main {

    public Main(){
        try{
            backendInterface backEnd = (backendInterface)
                    Naming.lookup("rmi://localhost/1099");
        } catch (RemoteException re) {
            System.out.println(re);
        } catch (NotBoundException nbe) {
            System.out.println("NotBoundException");
        } catch(MalformedURLException murle) {
            System.out.println("MalformedURLException");
        }

        LogIn loginPage = new LogIn();
        String user = loginPage.getUser();
        FileViewer fileViewer = new FileViewer(user);
    }

    public static void main(String args[]){
        Main main = new Main();
    }
}
