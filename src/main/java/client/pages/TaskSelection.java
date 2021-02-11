package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import server.credentials.SessionToken;

import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class TaskSelection extends JPanel {

    private Client client;
    private String username;
    private SessionToken token;
    private String session;

    public TaskSelection(String username, Client client, SessionToken sessionToken)
    {
        token = sessionToken;
        session = token.returnSessionTokenString();
        this.client = client;
        this.username = username;
        initUI();
        setVisible(true);
    }

    private void initUI()
    {
        setLayout(null);
        setLocation(0, 0);
        setSize(client.getPreferredSize());
        setBackground(AppColors.BACKGROUND);

        initTaskButtons();
    }

    private AppButton data = new AppButton();
    private AppButton accountManagement = new AppButton();
    private AppButton assignPermissions = new AppButton();
    private AppButton logout = new AppButton();

    private void initTaskButtons()
    {
        data.setBounds(490, 200, 300,50);
        data.setText("View/Edit Patients");
        data.setFontSize(22);
        data.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (token.isTokenValid())
                {
                    client.initDataEditor(username, token);
                }
                else
                {
                    System.out.println("Session token expired");
                    try {
                        client.bI().logout(username);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                    client.initLogin();
                }
            }
        });
        add(data);

        accountManagement.setBounds(490, 260, 300, 50);
        accountManagement.setText("Account Management");
        accountManagement.setFontSize(22);
        accountManagement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (token.isTokenValid())
                {
                    client.initAccountManager(username, token);
                }
                else
                {
                    System.out.println("Session token expired");
                    try {
                        client.bI().logout(username);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                    client.initLogin();
                }
            }
        });
        add(accountManagement);

        assignPermissions.setBounds(490, 320, 300, 50);
        assignPermissions.setText("Assign Permissions");
        assignPermissions.setFontSize(22);
        assignPermissions.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (token.isTokenValid())
                {
                    client.initPermissions(username, token);
                }
                else
                {
                    System.out.println("Session token expired");
                    try {
                        client.bI().logout(username);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                    client.initLogin();
                }
            }
        });
        add(assignPermissions);

        logout.setBounds(565, 380, 150, 25);
        logout.setText("Logout");
        logout.setFontSize(16);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    client.bI().logout(username);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                client.initLogin();
            }
        });
        add(logout);

        try {
            String encryptedRole = client.bI().getRole(client.encryptMessage(username, token.returnSessionTokenKey()), username);
            int role = Integer.parseInt(client.decryptMessage(encryptedRole, token.returnSessionTokenKey()));
            if(role != 3)
            {
                assignPermissions.setEnabled(false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
