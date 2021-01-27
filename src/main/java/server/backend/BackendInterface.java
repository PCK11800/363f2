package server.backend;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BackendInterface extends Remote {

    // Returns if login is valid.
    public boolean login(String username, String password) throws RemoteException;

    // Sends and returns an authentication code to user's email
    public int sendAuthenticationCode(String username) throws RemoteException;

    // Data Retrieval Methods
    public String[] getAllNames() throws RemoteException;
}
