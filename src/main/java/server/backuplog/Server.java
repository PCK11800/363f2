package server.backuplog;

import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.blocks.RpcDispatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.rmi.RemoteException;

public class Server extends ReceiverAdapter {

    public static void main(String[] args) throws Exception
    {
        new Server();
    }

    public Server() throws Exception {
        super();

        JChannel myChannel = new JChannel();

        myChannel.connect("Server");

        myChannel.setReceiver(this);
        new RpcDispatcher(myChannel, this);
    }

    public synchronized void createBackup(File file) throws IOException {

        File newBackup = new File("data/backups/backup.txt");
        Files.copy(file.toPath(), newBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public synchronized File getBackupFile() throws RemoteException{

        File savedBackup = new File("data/backups/src/backup.txt");
        return savedBackup;
    }
}