package client.backuplog;

import java.io.*;
import java.rmi.Naming;

public class BackupClass {

    private static Interface myService;

    public static void main(String[] args) {

        try
        {
            myService = (Interface) Naming.lookup("rmi://localhost:1099/project");
        }
        catch (Exception exception)
        {
            System.out.println("Interface encountered a problem. Full trace: " + exception);
        }
    }

    public static void createBackup(){

        try
        {
            myService.createBackup(new File("src/dummyfile.txt")); // This create a backup of dummyfile.txt - The backup file is called backup.txt
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void getBackup()
    {
        try
        {
            File backup = (File) myService.getBackupFile(); // This is the backup file
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
