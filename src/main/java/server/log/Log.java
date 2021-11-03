package server.log;

import server.backuplog.BackupLog;
import server.backuplog.Server;
import server.backuplog.ServerCaller;

import java.util.LinkedList;
import java.security.MessageDigest;
import java.util.*;
import java.io.*;

public class Log {
    private LinkedList<LogItem> logItems = loadLog();
    private BackupLog backupLog;
    private Server backupLogServer;
    private ServerCaller serverCaller;

    public Log(){
        try {
            backupLog = new BackupLog();
            backupLogServer = new Server();
            serverCaller = new ServerCaller();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Log(boolean viewerMode)
    {

    }

    public boolean addMsg(int type, String desc, String user)
    {
        if(type < 0 || type > 3)
        {
            return false;
        }
        try {
            byte[] hash;
            LogItem lI = new LogItem(type, desc, null, user);
            logItems.add(lI);
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(logItems.toString().getBytes());
            logItems.removeLast();
            lI = new LogItem(type, desc, hash, user);
            logItems.add(lI);
            saveLog();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String toString()
    {
        String str = "";
        for (Iterator i = logItems.iterator(); i.hasNext();) {
            str = str + i.next().toString();
        }

        return str;
    }

    public String getMsgsOfType(int type)
    {
        String str = "";
        for (Iterator i = logItems.iterator(); i.hasNext();) {
            LogItem lI = (LogItem) i.next();
            if(lI.getType() == type){
                str = str + lI.toString();
            }
        }
        return str;
    }


    private boolean saveLog()
    {
        try {
            System.out.println("Save Log Method Called");
            FileOutputStream FOS = new FileOutputStream(new File("data/log/Log.txt"));
            ObjectOutputStream OOS = new ObjectOutputStream(FOS);
            OOS.writeObject(logItems);
            FOS.close();
            OOS.close();

            //Backup
            serverCaller.createBackup(new File("data/log/Log.txt"));

            return true;
        } catch (Exception e) {
            System.out.println("Log save failed: " + e);
            return false;
        }
    }

    private LinkedList<LogItem> loadLog()
    {
        try {
            FileInputStream FIS = new FileInputStream(new File("data/log/Log.txt"));
            ObjectInputStream OIS = new ObjectInputStream(FIS);
            return (LinkedList<LogItem>) OIS.readObject();
        } catch (Exception e) {
            System.out.println("Password Load Failed: " + e);
            return new LinkedList<LogItem>();
        }
    }

    public void printLogs()
    {
        loadLog();
        System.out.println(toString());
    }

    public static void main(String[] args)
    {
        new Log().printLogs();
    }
}
