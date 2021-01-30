package server.backend;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

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
    
    public boolean changePassword(String userName, String password, String oldPassword)
        throws java.rmi.RemoteException;

    public boolean addPermission(String userName, int perm, String adminUserName, String adminPass)
        throws java.rmi.RemoteException;

    public boolean removePermission(String userName, int perm, String adminUserName, String adminPass)
        throws java.rmi.RemoteException;

    public boolean isPermitted(String userName, int perm)
        throws java.rmi.RemoteException;

    public void clearPermissions(String userName, String adminUserName, String adminPass)
        throws java.rmi.RemoteException;

    public boolean deleteUser(String userName, String adminUserName, String adminPassword)
        throws java.rmi.RemoteException;
    
    public int getRole(String userName)
        throws java.rmi.RemoteException;

    public LinkedList<String> getAllUsers() throws java.rmi.RemoteException;

}
