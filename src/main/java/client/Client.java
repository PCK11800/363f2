package client;

import client.components.AppColors;
import client.pages.DataEditor;
import client.pages.Login;
import client.pages.TaskSelection;
import server.backend.BackendInterface;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;

public class Client extends JFrame {

    private BackendInterface bI;
    private JPanel currentPage = null;

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

    public static void main(String[] args)
    {
        new Client();
    }
}
