package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import client.components.extras.GhostText;
import client.components.extras.JTextFieldLimit;
import client.components.font.Inconsolata;
import server.credentials.EncryptSessionKey;
import server.credentials.SessionToken;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

public class Login extends JPanel {

    private Client client;
    private SessionToken token = new SessionToken();
    private String sessionKey = "";

    public Login(Client client)
    {
        this.client = client;
        initUI();
        setVisible(true);
    }

    private void initUI()
    {
        setLayout(null);
        setLocation(0, 0);
        setSize(client.getPreferredSize());
        setBackground(AppColors.BACKGROUND);

        initUsernamePasswordLogin();
    }

    private void initUsernamePasswordLogin()
    {
        initLoginFields();
        initLoginButton();
    }

    private JTextField user_field = new JTextField();
    private JTextField password_field = new JTextField();
    private void initLoginFields()
    {
        user_field.setBounds(490, 200, 300, 50);
        password_field.setBounds(490, 260, 300, 50);

        setFieldSettings(user_field);
        setFieldSettings(password_field);

        new GhostText(user_field, "Username").changeStyle(Font.ITALIC);
        new GhostText(password_field, "Password").changeStyle(Font.ITALIC);

        add(user_field);
        add(password_field);
    }

    private AppButton login_button = new AppButton();
    private void initLoginButton()
    {
        login_button.requestFocus();
        login_button.setText("Login");
        login_button.setFontSize(22);
        login_button.setBounds(490, 320, 300, 50);

        login_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = user_field.getText();
                String password = password_field.getText();

                try {
                    boolean login_valid = client.bI().login(username, password);

                    if(login_valid)
                    {
                        credentialNegotiation(username);
                        sendMessageToServer("Client Connected", username);
                        //client.setPassword(password);
                        client.setAdmin_password(password);
                        initMultifactorAuthentication(username);
                    }
                    else
                    {
                        login_button.setText("!Invalid!");
                    }

                } catch (RemoteException re) {
                    re.printStackTrace();
                }
            }
        });

        add(login_button);
    }

    private void sendMessageToServer(String message, String username)
    {
        byte[] messageBytes = message.getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, token.stringToKey(sessionKey));
            final byte[] encValue = cipher.doFinal(message.getBytes());
            String encrypted = Base64.getEncoder().encodeToString(encValue);
            client.bI().exchangeMessages(encrypted, username);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void credentialNegotiation(String username)
    {
        try {
            PublicKey serverPublic = client.bI().getServerPublic();
            this.sessionKey = token.createSessionTokenString();

            //encrypt session key with server's public key
            EncryptSessionKey encryptSessionKey = new EncryptSessionKey(serverPublic);

            //send encrypted key to server and server decrypts the session key with private key
            client.bI().sendSessionKey(encryptSessionKey.encryptSessionKey(sessionKey), username);

        } catch (RemoteException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    private void setFieldSettings(JTextField textField)
    {
        textField.setForeground(AppColors.BORDER);
        textField.setBackground(AppColors.BACKGROUND);
        textField.setSelectedTextColor(AppColors.BACKGROUND);
        textField.setSelectionColor(AppColors.BORDER);
        textField.setCaretColor(AppColors.BORDER);
        textField.setFont(new Inconsolata().getFont(22));

        textField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                login_button.setText("Login");
                confirmation_button.setText("Confirm");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    JLabel authenticationLabel = new JLabel("Authentication Code");
    JTextField codeInput = new JTextField();
    AppButton confirmation_button = new AppButton();
    AppButton cancel_button = new AppButton();
    private void initMultifactorAuthentication(String username)
    {
        String authenticationCode = "null"; //Default invalid - can't have 7 digit codes
        try
        {
            authenticationCode = client.decryptMessage(client.bI().sendAuthenticationCode(username), token.stringToKey(sessionKey));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Remove Username/Password Login
        removeAll();
        repaint();

        authenticationLabel.setBounds(490, 160, 300, 50);
        authenticationLabel.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        authenticationLabel.setFont(new Inconsolata().getFont(22));
        authenticationLabel.setBackground(AppColors.BACKGROUND);
        authenticationLabel.setForeground(AppColors.BORDER);
        add(authenticationLabel);

        codeInput = new JTextField();
        codeInput.setBounds(490, 200, 300, 50);
        codeInput.setHorizontalAlignment((int) CENTER_ALIGNMENT);
        codeInput.setDocument(new JTextFieldLimit(6));
        setFieldSettings(codeInput);
        add(codeInput);

        confirmation_button = new AppButton();
        confirmation_button.setText("Confirm");
        confirmation_button.setBounds(490, 260, 300, 50);
        confirmation_button.setFontSize(22);
        add(confirmation_button);

        cancel_button = new AppButton();
        cancel_button.setText("Cancel");
        cancel_button.setBounds(490, 320, 300, 50);
        cancel_button.setFontSize(22);
        add(cancel_button);

        String finalAuthenticationCode = authenticationCode;
        confirmation_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputtedCode = codeInput.getText();
                if (inputtedCode.equals(finalAuthenticationCode))
                {
                    System.out.println("Login successful");
                    removeAll();
                    repaint();
                    client.initTaskSelection(username, token);
                }
                else
                {
                    confirmation_button.setText("!Invalid!");
                }
            }
        });

        cancel_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Remove MFA
                removeAll();
                repaint();
                initUsernamePasswordLogin();
            }
        });
    }
}
