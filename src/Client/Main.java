import java.rmi.Naming;
import java.rmi.RemoteException;
import java.security.PublicKey;

import javax.crypto.SecretKey;

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

        //Get server's public key
        PublicKey serverPublic = backEnd.getServerPublic();
        String sessionKey = codeInput.getSessionKey();

        //encrypt session key with server's public key
        EncryptSessionKey encryptSessionKey = new EncryptSessionKey(serverPublic);

        //send encrypted key to server and server decrypts the session key with private key
        backEnd.sendSessionKey(encryptSessionKey.encryptSessionKey(sessionKey));

        if(sessionKey != null){
            FileViewer fileViewer = new FileViewer(user, sessionKey, backEnd);
        }
    }

    public static void main(String args[]){
        Main main = new Main();
    }
}
