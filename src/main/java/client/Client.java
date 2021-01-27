package client;

import client.components.AppColors;
import client.pages.Login;
import server.backend.BackendInterface;

import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;

public class Client extends JFrame {

    private BackendInterface bI;

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

        initComponents();

        pack();
        repaint();
    }

    private void initComponents()
    {
        Login login = new Login(this);
        add(login);
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
