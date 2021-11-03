package server;

import server.backend.Backend;
import server.backend.BackendInterface;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Server {

    public Server()
    {
        try{
            //System.setProperty("java.rmi.server.hostname","10.63.2.241");
            LocateRegistry.createRegistry(1099);
            BackendInterface backendInterface = new Backend();
            Naming.rebind("rmi://localhost/1099", backendInterface);
            backendInterface.generateServerKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        new Server();
    }
}
