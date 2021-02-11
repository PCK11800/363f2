package server.backuplog;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

// Steps:
// 1. Run Main.java
// 2. Run Server.java
// 3. Run BackupClass.java


public class BackupLog
{
    public BackupLog()
    {
        try {
            LocateRegistry.createRegistry(1100);
            BackupInterface bI = new ServerCaller();
            Naming.rebind("rmi://localhost/1100", bI);
        }
        catch (Exception exception) {
            System.out.println("registry problem... full trace: " + exception);
        }
    }

    public static void main(String[] args) {
        new BackupLog();
    }
}