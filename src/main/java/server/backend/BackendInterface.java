package server.backend;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BackendInterface extends Remote {

    // Returns if login is valid.
    public boolean login(String username, String password) throws RemoteException;

    // Sends and returns an authentication code to user's email
    public String sendAuthenticationCode(String username) throws RemoteException;

    // Data Retrieval Methods
    public String[] getAllNames() throws RemoteException;

    public String[] getPerson(String name) throws RemoteException;

    public void storePerson(String[] person) throws RemoteException;

    public void deletePerson(String name) throws RemoteException;
}
