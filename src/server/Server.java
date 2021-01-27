import java.rmi.Naming;
public class Server {
    public Server(){
        try {
            Backend bI = new Backend();
            Naming.rebind("rmi://localhost/1099", bI);
            bI.generateServerKeys();
        } catch (Exception e) {
            System.out.println("Server Error");
        }
    }
    public static void main(String[] args){
        Server s = new Server();
    }
}
