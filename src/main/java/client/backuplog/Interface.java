import java.rmi.RemoteException;
import java.io.File;
import java.rmi.Remote;

public interface Interface extends Remote {

    public File getBackupFile() throws RemoteException, Exception;
    public void createBackup(File file) throws RemoteException, Exception;
}