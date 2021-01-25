public interface Server extends java.rmi.Remote{

    //Takes a users login details and returns if they are correct
    public boolean logIn(String user, String password) throws java.rmi.RemoteException;

    //Returns a valid session key if the code is correct
    public String multiFactor(String user, String code) throws java.rmi.RemoteException;

    //Returns true if account could be created, false otherwise
    public boolean newAccount(String user, String password) throws java.rmi.RemoteException;

    //Returns true if log out was successful, false otherwise
    public boolean logOut(String user) throws java.rmi.RemoteException;
}