package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import client.components.extras.GhostText;
import client.components.font.Inconsolata;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;

public class Login extends JPanel {

    private Client client;
    private JTextField user_field, password_field;
    private AppButton login_button;

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

        initLoginFields();
        initLoginButton();
    }

    private void initLoginFields()
    {
        user_field = new JTextField();
        password_field = new JTextField();

        user_field.setBounds(490, 200, 300, 50);
        password_field.setBounds(490, 260, 300, 50);

        setFieldSettings(user_field);
        setFieldSettings(password_field);

        new GhostText(user_field, "Username").changeStyle(Font.ITALIC);
        new GhostText(password_field, "Password").changeStyle(Font.ITALIC);

        add(user_field);
        add(password_field);
    }

    private void initLoginButton()
    {
        login_button = new AppButton();
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
                        // multifactor authentication
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
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }
}
