package server.backend;

import server.credentials.SessionToken;
import server.password.PasswordManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class Backend extends UnicastRemoteObject implements BackendInterface {

    //Assume it works for now
    //private PasswordManager passwordManager = new PasswordManager();

    public Backend() throws RemoteException {
        super();
    }

    public boolean login(String username, String password) throws RemoteException {
        //boolean loginValid = passwordManager.checkIfPasswordIsCorrect(username, password);
        //return loginValid;

        return true;
    }
}
