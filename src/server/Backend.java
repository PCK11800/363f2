import java.util.HashMap;
import javax.mail.Session;

public class Backend extends java.rmi.server.UnicastRemoteObject implements backendInterface{
    private PasswordManager pM = new PasswordManager();
    private SessionToken sessionKeys = new SessionToken();

    public Backend()
        throws java.rmi.RemoteException{
            super();
        }

    public boolean login(String userName, String password) throws java.rmi.RemoteException
    {
        boolean correctPass = pM.checkIfPasswordIsCorrect(userName, password);
        if(correctPass)
        {
            sendCodeToUser(userName);
        }

        return correctPass;
    }

    public boolean newAccount(String userName, String password, String adminUserName, String adminPassword, int role)
    {
        return pM.addNewUser(userName, password, adminUserName, adminPassword, role);
    }

    
    public boolean changePassword(String userName, String password, String oldPassword) throws java.rmi.RemoteException
    {
        return pM.changePassword(userName, password, oldPassword);
    }

    //Returns true if log out was successful, false otherwise
    public boolean logOut(String user, String sessionKey) throws java.rmi.RemoteException
    {
        sessionKeys.removeSessionToken(sessionKey);
        if (!sessionKeys.tokenExists(sessionKey))
        {
            return true;
        }
        return false;
    }

    private MultifactorAuthenticator mfa = new MultifactorAuthenticator();
    private HashMap<String, Integer> mfaMap = new HashMap<>(); // Stores Username and Code in a HashMap

    public void sendCodeToUser(String user) throws java.rmi.RemoteException
    {
        int code = mfa.generateAuthenticationCode();
        mfa.sendAuthenticationCode("user.getEmail()", code);
        mfaMap.put(user, code);
    }

    public String validateCode(String user, int inputtedCode) throws java.rmi.RemoteException
    {
        if(mfaMap.get(user) == inputtedCode)
        {
            return sessionKeys.createSessionToken(user);
        }
        return null;
    }
    
    public boolean addPermission(String userName, int perm, String adminUserName, String adminPass)
    {
        return pM.addPermission(userName, perm, adminUserName, adminPass);
    }

    public boolean removePermission(String userName, int perm, String adminUserName, String adminPass)
    {
        return pM.removePermission(userName, perm, adminUserName, adminPass);
    }

    public boolean isPermitted(String userName, int perm)
    {
        return pM.isPermitted(userName, perm);
    }

    public void clearPermissions(String userName, String adminPass)
    {
        pM.clearPermissions(userName, adminPass);
    }

    public boolean deleteUser(String userName, String adminUserName, String adminPassword)
    {
        return pM.deleteUser(userName, adminUserName, adminPassword);
    }

}
