package client;

import client.components.AppColors;
import client.pages.*;
import server.backend.BackendInterface;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;

public class Client extends JFrame {

    private BackendInterface bI;
    private JPanel currentPage = null;
    private String admin_password = "tY,?S5b&7Xn{)NR@";

    int APP_WIDTH = 1280;
    int APP_HEIGHT = 720;
    static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public Client()
    {
        initClientServerConnection();
        initUI();
        setVisible(true);
    }

    private void initClientServerConnection()
    {
        try {
            bI = (BackendInterface) Naming.lookup("rmi://localHost/1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI()
    {
        setTitle("Client");
        setPreferredSize(new Dimension(APP_WIDTH, APP_HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(
                (int) (SCREEN_WIDTH / 2) - (APP_WIDTH / 2),
                (int) (SCREEN_HEIGHT / 2) - (APP_HEIGHT / 2)
        );
        setResizable(false);
        getContentPane().setBackground(AppColors.BACKGROUND);
        getContentPane().setLayout(null);

        initLogin();
        //initPermissions("f2.scc363@gmail.com");
        //initDataEditor("patient@sharklasers.com");
        //initAccountManager("regulator@sharklasers.com");
        //initAccountManager("f2.scc363@gmail.com");
    }

    Login login = new Login(this);
    public void initLogin()
    {
        resetPages();

        login = new Login(this);
        add(login);
        currentPage = login;

        pack();
        repaint();
    }

    TaskSelection workpage;
    public void initTaskSelection(String username)
    {
        resetPages();

        workpage = new TaskSelection(username, this);
        add(workpage);
        currentPage = workpage;

        pack();
        repaint();
    }

    DataEditor dataEditor;
    public void initDataEditor(String username)
    {
        resetPages();

        dataEditor = new DataEditor(username, this);
        add(dataEditor);
        currentPage = dataEditor;

        pack();
        repaint();
    }

    Permissions permissions;
    public void initPermissions(String username)
    {
        resetPages();

        permissions = new Permissions(username, this);
        add(permissions);
        currentPage = permissions;

        pack();
        repaint();
    }

    AccountManager accountManager;
    public void initAccountManager(String username)
    {
        resetPages();

        accountManager = new AccountManager(username, this);
        add(accountManager);
        currentPage = accountManager;

        pack();
        repaint();
    }

    private void resetPages()
    {
        if(currentPage != null)
        {
            remove(currentPage);
            repaint();
        }
    }

    public BackendInterface bI()
    {
        return bI;
    }

    public void setAdmin_password(String admin_password){
        this.admin_password = admin_password;
    }

    public String getAdmin_password(){
        return admin_password;
    }

    public static void main(String[] args)
    {
        new Client();
    }
}
