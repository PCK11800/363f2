package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskSelection extends JPanel {

    private Client client;
    private String username;

    public TaskSelection(String username, Client client)
    {
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
        data.setBounds(490, 260, 300,50);
        data.setText("View/Edit Patients");
        data.setFontSize(22);
        data.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.initDataEditor(username);
            }
        });
        add(data);

        accountManagement.setBounds(490, 320, 300, 50);
        accountManagement.setText("Account Management");
        accountManagement.setFontSize(22);
        add(accountManagement);

        assignPermissions.setBounds(490, 380, 300, 50);
        assignPermissions.setText("Assign Permissions");
        assignPermissions.setFontSize(22);
        add(assignPermissions);

        logout.setBounds(565, 460, 150, 25);
        logout.setText("Logout");
        logout.setFontSize(16);
        logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.initLogin();
            }
        });
        add(logout);
    }
}
