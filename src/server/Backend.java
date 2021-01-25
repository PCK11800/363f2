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
            //Send e-mail;
        }

        return correctPass;
    }

    public boolean newAccount(String userName, String password) throws java.rmi.RemoteException
    {
        return pM.addNewUser(userName, password);
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

    //Returns a valid session key if the code is correct, return null otherwise
    public String multiFactor(String user, String code) throws java.rmi.RemoteException
    {
        if(code is correct)
        {
            return //New session key
        }
        return null;
    }
}
