public class Main {

    public Main(){
        LogIn loginPage = new LogIn();
        String user = loginPage.getUser();
        System.out.println(user);
    }

    public static void main(String args[]){
        Main main = new Main();
    }
}
