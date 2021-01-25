public class Backend extends java.rmi.server.UnicastRemoteObject implements backendInterface{
    private PasswordManager pM = new PasswordManager(); 
    public Backend()
        throws java.rmi.RemoteException{
            super();
        }

    public boolean login(String userName, String password)
    {
        boolean correctPass = pM.checkIfPasswordIsCorrect(userName, password);
        if(correctPass)
        {
            //Send e-mail;
        }

        return correctPass;
    }

    public boolean newAccount(String userName, String password)
    {
        return pM.addNewUser(userName, password);
    }
}
