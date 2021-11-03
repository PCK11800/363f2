package server.backend;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.LinkedList;

public interface BackendInterface extends Remote {

    //Send messages
    public void exchangeMessages(String encryptedMessage, String username) throws java.rmi.RemoteException;

    //Generate server keys
    public void generateServerKeys() throws java.rmi.RemoteException;

    //Generate server keys
    public PublicKey getServerPublic() throws java.rmi.RemoteException;

    //Sending the encrypted session key to the server
    public void sendSessionKey(byte[] encryptedKey, String username) throws java.rmi.RemoteException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException;

    public void logout(String username) throws RemoteException;

    // Returns if login is valid.
    public boolean login(String username, String password) throws RemoteException;

    // Sends and returns an authentication code to user's email
    public String sendAuthenticationCode(String username) throws RemoteException;

    // Data Retrieval Methods
    public String[] getAllNames(String username) throws RemoteException;

    public String[] getPerson(String username, String name) throws RemoteException;

    public void storePerson(String[] person, String username) throws RemoteException;

    public void deletePerson(String name, String username) throws RemoteException;
    
    public boolean changePassword(String userName, String password, String oldPassword)
        throws java.rmi.RemoteException;

    public boolean addPermission(String userName, String perm, String adminUserName, String adminPass)
        throws java.rmi.RemoteException;

    public boolean removePermission(String userName, String perm, String adminUserName, String adminPass)
        throws java.rmi.RemoteException;

    public boolean isPermitted(String userName, String perm, String currentUser)
        throws java.rmi.RemoteException;

    public void clearPermissions(String userName, String adminUserName, String adminPass)
        throws java.rmi.RemoteException;

    public boolean newAccount(String userName, String password, String role, String currentUsername) throws RemoteException;

    public boolean deleteUser(String userName, String username)
        throws java.rmi.RemoteException;
    
    public String getRole(String userName, String currentUserName)
        throws java.rmi.RemoteException;

    public boolean isPasswordValid(String username, String password) throws RemoteException;

    public boolean isPasswordStrong(String username, String password) throws RemoteException;

    public LinkedList<String> getAllUsers() throws java.rmi.RemoteException;

}
