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

    public boolean newAccount(String userName, String password) throws java.rmi.RemoteException
    {
        return pM.addNewUser(userName, password);
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
    
    public boolean addPermission(String userName, int perm , String adminPass)
    {
        return pM.addPermission(userName, perm, adminPass);
    }

    public boolean removePermission(String userName, int perm, String adminPass)
    {
        return pM.removePermission(userName, perm, adminPass);
    }

    public boolean isPermitted(String userName, int perm)
    {
        return pM.isPermitted(userName, perm);
    }

    public void clearPermissions(String userName, String adminPass)
    {
        pM.clearPermissions(userName, adminPass);
    }

}
