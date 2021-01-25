package server;

public interface backendInterface extends java.rmi.Remote{

    //Takes a users login details and returns if they are correct
    public boolean login(String userName, String password) throws java.rmi.RemoteException;

    //Returns true if account could be created, false otherwise
    public boolean newAccount(String userName, String password) throws java.rmi.RemoteException;

    //Returns true if log out was successful, false otherwise
    public boolean logOut(String user) throws java.rmi.RemoteException;

    //Sends a random generated code to user's email account
    public void sendCodeToUser(String user) throws java.rmi.RemoteException;

    //Returns true if user inputted code is correct, false otherwise
    public boolean validateCode(String user, int inputtedCode) throws java.rmi.RemoteException;

    //Returns true if password was chnaged, false otherwise
    public boolean changePassword(String userName, String password) throws java.rmi.RemoteException;
}
