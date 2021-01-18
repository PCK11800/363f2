public class Main {

    public Main(){
        LogIn loginPage = new LogIn();
        String user = loginPage.getUser();
        FileViewer fileViewer = new FileViewer(user);
    }

    public static void main(String args[]){
        Main main = new Main();
    }
}
