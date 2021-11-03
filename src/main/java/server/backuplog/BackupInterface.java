package server.backuplog;

import java.io.File;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BackupInterface extends Remote {

    public File getBackupFile() throws RemoteException, Exception;
    public void createBackup(File file) throws RemoteException, Exception;
}