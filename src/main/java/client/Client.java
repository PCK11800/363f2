package client;

import client.components.AppColors;
import client.pages.*;
import server.backend.BackendInterface;
import server.credentials.SessionToken;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.rmi.Naming;
import java.util.Base64;

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
    public void initTaskSelection(String username, SessionToken session)
    {
        resetPages();

        workpage = new TaskSelection(username, this, session);
        add(workpage);
        currentPage = workpage;

        pack();
        repaint();
    }

    DataEditor dataEditor;
    public void initDataEditor(String username, SessionToken session)
    {
        resetPages();

        dataEditor = new DataEditor(username, this, session);
        add(dataEditor);
        currentPage = dataEditor;

        pack();
        repaint();
    }

    Permissions permissions;
    public void initPermissions(String username, SessionToken session)
    {
        resetPages();

        permissions = new Permissions(username, this, session);
        add(permissions);
        currentPage = permissions;

        pack();
        repaint();
    }

    AccountManager accountManager;
    public void initAccountManager(String username, SessionToken session)
    {
        resetPages();

        accountManager = new AccountManager(username, this, session);
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

    public String encryptMessage(String message, SecretKey sessionKey)
    {
        String encrypted = "";
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
            final byte[] encValue = cipher.doFinal(message.getBytes("UTF-8"));
            encrypted = Base64.getEncoder().encodeToString(encValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    public String decryptMessage(String encryptedMessage, SecretKey sessionKey)
    {
        String decrypted = "";
        try {
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            c.init(Cipher.DECRYPT_MODE, sessionKey);
            byte[] decryptedBytes = c.doFinal(Base64.getDecoder().decode(encryptedMessage));
            decrypted = new String(decryptedBytes);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return decrypted;
    }

    public String[] encryptArrayMessage(String[] messages, SecretKey sessionKey)
    {
        String[] encryptedMessages = new String[messages.length];
        String encrypted = "";

        for (int i = 0; i < messages.length; i++)
        {
            if (sessionKey != null) {
                try {
                    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                    cipher.init(Cipher.ENCRYPT_MODE, sessionKey);
                    final byte[] encValue = cipher.doFinal(messages[i].getBytes("UTF-8"));
                    encrypted = Base64.getEncoder().encodeToString(encValue);
                    encryptedMessages[i] = encrypted;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                encryptedMessages[i] = "";
            }
        }
        return encryptedMessages;
    }

    public String[] decryptArrayMessage(String[] encryptedMessages, SecretKey sessionKey)
    {
        String[] decryptedMessages = new String[encryptedMessages.length];
        String decrypted = "";

        for (int i = 0; i < encryptedMessages.length; i++)
        {
            if (sessionKey != null) {
                try {
                    Cipher c = Cipher.getInstance("AES/ECB/PKCS5PADDING");
                    c.init(Cipher.DECRYPT_MODE, sessionKey);
                    byte[] decryptedBytes = c.doFinal(Base64.getDecoder().decode(encryptedMessages[i]));
                    decrypted = new String(decryptedBytes);
                    decryptedMessages[i] = decrypted;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
            {
                decryptedMessages[i] = "";
            }
        }
        return decryptedMessages;
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
