package client.backuplog;

import java.rmi.registry.LocateRegistry;

// Steps:
// 1. Run Main.java
// 2. Run Server.java
// 3. Run BackupClass.java

// The Jgroups .jar file needs to be used as well.

public class Main
{
    public static void main(String[] args) {

        try {
        LocateRegistry.createRegistry(1099).rebind("project", new ServerCaller());
        }
        catch (Exception exception) {
        System.out.println("registry problem... full trace: " + exception);
        }
    }
}
