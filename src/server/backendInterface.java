public interface backendInterface extends java.rmi.Remote{
    public boolean login(String userName, String password)
        throws java.rmi.RemoteException;

    public boolean newAccount(String userName, String password)
        throws java.rmi.RemoteException;
}
