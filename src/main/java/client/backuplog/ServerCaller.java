import java.io.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import org.jgroups.*;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;

public class ServerCaller extends UnicastRemoteObject implements Interface {

    RpcDispatcher disp;
    RequestOptions rqstOpts;

    public ServerCaller() throws Exception {
        super();

        JChannel myChannel = new JChannel();
        myChannel.connect("Server");

        disp = new RpcDispatcher(myChannel, null);

        rqstOpts = new RequestOptions(ResponseMode.GET_ALL, 5000);
    }

    public synchronized File getBackupFile() throws Exception
    {
        RspList resp = disp.callRemoteMethods(null, "getBackupFile",
                new Object[]{},
                new Class[]{}, rqstOpts);

        List results = resp.getResults();

        return (File) results.get(0); // this returns the backup file
    }

    public synchronized void createBackup(File file) throws Exception
    {
        disp.callRemoteMethods(null, "createBackup",
                new Object[]{file},
                new Class[]{File.class}, rqstOpts);
        // create backup file
    }
}