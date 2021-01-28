import java.security.PublicKey;

public interface backendInterface extends java.rmi.Remote {

    //Generate server keys
    public void generateServerKeys() throws java.rmi.RemoteException;

    //Generate server keys
    public PublicKey getServerPublic() throws java.rmi.RemoteException;

    //Sending the encrypted session key to the server
    public void sendSessionKey(byte[] encryptedKey) throws java.rmi.RemoteException;

    //Takes a users login details and returns if they are correct
    public boolean login(String userName, String password) throws java.rmi.RemoteException;

    //Returns true if account could be created, false otherwise
    public boolean newAccount(String userName, String password, String adminUserName, String adminPassword, int role)
        throws java.rmi.RemoteException;

    //Returns true if log out was successful, false otherwise
    public boolean logOut(String user, String sessionToken) throws java.rmi.RemoteException;

    //Returns a valid session key if the code is correct
    public String validateCode(String user, int code) throws java.rmi.RemoteException;

    //Returns true if password was chnaged, false otherwise
    public boolean changePassword(String userName, String password, String oldPassword) throws java.rmi.RemoteException;
    
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
}
