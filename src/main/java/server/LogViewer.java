package server;

import server.log.Log;

public class LogViewer {

    public static void main(String[] args)
    {
        Log log = new Log(true);
        log.printLogs();
    }
}
