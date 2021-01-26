import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.net.MalformedURLException;

public class Main {
    private backendInterface backEnd;

    public Main(){
        try{
            backEnd = (backendInterface)
                    Naming.lookup("rmi://localhost/1099");
        } catch (RemoteException re) {
            System.out.println(re);
        } catch (NotBoundException nbe) {
            System.out.println("NotBoundException");
        } catch(MalformedURLException murle) {
            System.out.println("MalformedURLException");
        }

        LogIn loginPage = new LogIn(backEnd);
        String user = loginPage.getUser();
        CodeInput codeInput = new CodeInput(backEnd, user);
        String sessionKey = codeInput.getSessionKey();
        if(sessionKey != null){
            FileViewer fileViewer = new FileViewer(user, sessionKey, backEnd);
        }
    }

    public static void main(String args[]){
        Main main = new Main();
    }
}
