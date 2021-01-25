import java.util.HashMap;

public class Backend extends java.rmi.server.UnicastRemoteObject implements backendInterface{
    private PasswordManager pM = new PasswordManager(); 
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
    
    public boolean changePassword(String userName, String password, String oldPassword)
    {
        return pM.changePassword(userName, password, oldPassword);
    }

    //Returns true if log out was successful, false otherwise
    public boolean logOut(String user) throws java.rmi.RemoteException
    {
        if(logout is successful)
        {
            //invalidate session key
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

    public boolean validateCode(String user, int inputtedCode) throws java.rmi.RemoteException
    {
        if(mfaMap.get(user) == inputtedCode)
        {
            return true;
        } else
        {
            return false;
        }
    }
}
