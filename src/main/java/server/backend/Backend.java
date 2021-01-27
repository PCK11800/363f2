package server.backend;

import server.credentials.SessionToken;
import server.data.DataRetriever;
import server.multifactor.MultifactorAuthenticator;
import server.password.PasswordManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Backend extends UnicastRemoteObject implements BackendInterface {

    //Assume it works for now
    //private PasswordManager passwordManager = new PasswordManager();
    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private DataRetriever dataRetriever = new DataRetriever();

    public Backend() throws RemoteException {
        super();
    }

    public boolean login(String username, String password) throws RemoteException {
        //boolean loginValid = passwordManager.checkIfPasswordIsCorrect(username, password);
        //return loginValid;

        return true;
    }

    public int sendAuthenticationCode(String username) throws RemoteException {
        String email = "chinkiu.pak@gmail.com"; //Retrieve user email
        int code = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode(email, code);
        return code;
    }

    public String[] getAllNames() throws RemoteException {
        return dataRetriever.getAllNames();
    }
}
