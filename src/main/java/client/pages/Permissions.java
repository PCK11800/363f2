package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import client.components.font.Inconsolata;
import client.pages.components.UsersList;
import server.credentials.SessionToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.LinkedList;

public class Permissions extends JPanel {

    private Client client;
    private String username;
    private int userRole = -1;
    private String currentSelectedUser = null;
    private SessionToken token;

    public Permissions(String username, Client client, SessionToken sessionToken)
    {
        this.token = sessionToken;
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

        initUserLabel();
        initUsersList();
        initPermissionsFields();
        initSettings();
    }

    private JLabel userLabel;
    private void initUserLabel()
    {
        userLabel = new JLabel();
        userLabel.setText("User: " + currentSelectedUser);
        userLabel.setBackground(AppColors.BACKGROUND);
        userLabel.setForeground(AppColors.BORDER);
        userLabel.setFont(new Inconsolata().getFont(22));
        userLabel.setBounds(500, 50, 500, 30);
        add(userLabel);
    }

    private UsersList usersList;
    private void initUsersList()
    {
        usersList = new UsersList(this, 250, 460);
        usersList.setLocation(45, 150);
        add(usersList);
    }

    private JCheckBox viewPersonalData, editPersonalData,
            viewDoctorsNote, editDoctorsNote, selectOtherPeopleData;
    private void initPermissionsFields()
    {
        viewPersonalData = new JCheckBox();
        viewPersonalData.setBounds(560, 150, 250, 30);
        viewPersonalData.setText("View Personal Data");
        setCheckBoxSettings(viewPersonalData);


        selectOtherPeopleData = new JCheckBox();
        selectOtherPeopleData.setBounds(560, 430, 320, 30);
        selectOtherPeopleData.setText("Select Other Patient's Data");
        setCheckBoxSettings(selectOtherPeopleData);

        viewDoctorsNote = new JCheckBox();
        viewDoctorsNote.setBounds(560, 290, 250, 30);
        viewDoctorsNote.setText("View Doctors Note");
        setCheckBoxSettings(viewDoctorsNote);

        editPersonalData = new JCheckBox();
        editPersonalData.setBounds(560, 220, 250, 30);
        editPersonalData.setText("Edit Personal Data");
        setCheckBoxSettings(editPersonalData);

        editDoctorsNote = new JCheckBox();
        editDoctorsNote.setBounds(560, 360, 250, 30);
        editDoctorsNote.setText("Edit Doctors Notes");
        setCheckBoxSettings(editDoctorsNote);

    }

    private void setCheckBoxSettings(JCheckBox checkBox)
    {
        checkBox.setForeground(AppColors.BORDER);
        checkBox.setBackground(AppColors.BACKGROUND);
        checkBox.setFont(new Inconsolata().getFont(20));

        ImageIcon unticked = new ImageIcon("data/icons/checkbox_unticked.png");
        Image unticked_image = unticked.getImage();
        unticked_image = unticked_image.getScaledInstance(checkBox.getHeight(), checkBox.getHeight(), Image.SCALE_SMOOTH);
        unticked = new ImageIcon(unticked_image);

        ImageIcon ticked = new ImageIcon("data/icons/checkbox_ticked.png");
        Image ticked_image = ticked.getImage();
        ticked_image = ticked_image.getScaledInstance(checkBox.getHeight(), checkBox.getHeight(), Image.SCALE_SMOOTH);
        ticked = new ImageIcon(ticked_image);

        checkBox.setIcon(unticked);
        checkBox.setSelectedIcon(ticked);

        checkBox.setFocusPainted(false);
    }

    private AppButton saveSettings, goBack;
    private void initSettings()
    {
        saveSettings = new AppButton();
        saveSettings.setText("Save");
        saveSettings.setFontSize(20);
        saveSettings.setBounds(320, 550, 250, 60);
        saveSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LinkedList<Integer> added = new LinkedList();
                LinkedList<Integer> removed = new LinkedList();

                if(viewPersonalData.isSelected()) {
                    added.add(1);
                } else{
                    removed.add(1);
                }

                if(viewDoctorsNote.isSelected()) {
                    added.add(3);
                } else{
                    removed.add(3);
                }

                if(userRole == 1 || userRole == 2 || userRole == 3) {
                    if(selectOtherPeopleData.isSelected()) {
                        added.add(5);
                    } else{
                        removed.add(5);
                    }
                }

                if(userRole == 1 || userRole == 3) {
                    if (editPersonalData.isSelected()) {
                        added.add(2);
                    } else {
                        removed.add(2);
                    }

                    if (editDoctorsNote.isSelected()) {
                        added.add(4);
                    } else {
                        removed.add(4);
                    }
                }

                try {
                    for(int i: added){
                        client.bI().addPermission(client.encryptMessage(currentSelectedUser, token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(i), token.returnSessionTokenKey()), username, client.encryptMessage(client.getAdmin_password(), token.returnSessionTokenKey()));
                    }
                    for(int i: removed){
                        client.bI().removePermission(client.encryptMessage(currentSelectedUser, token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(i), token.returnSessionTokenKey()), username, client.encryptMessage(client.getAdmin_password(), token.returnSessionTokenKey()));
                    }
                } catch (RemoteException re) {
                    re.printStackTrace();
                }
            }
        });
        add(saveSettings);

        goBack = new AppButton();
        goBack.setText("Return");
        goBack.setFontSize(20);
        goBack.setBounds(820, 550, 250, 60);
        goBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
                repaint();
                client.initTaskSelection(username, token);
            }
        });
        add(goBack);
    }

    // This method is to get the role number from the selected user and
    // show it in the checkboxes.
    // Check UserList's nameButton actionListener
    public void populateFields(String username, int role)
    {
        userRole = role;

        remove(viewPersonalData);
        remove(selectOtherPeopleData);
        remove(viewDoctorsNote);
        remove(editDoctorsNote);
        remove(editPersonalData);

        add(viewPersonalData);
        add(viewDoctorsNote);
        if(userRole == 1 || userRole == 2 || userRole == 3) {
            add(selectOtherPeopleData);
        }
        if(userRole == 1 || userRole == 3) {
            add(editDoctorsNote);
            add(editPersonalData);
        }
        revalidate();
        repaint();

        viewPersonalData.setSelected(false);
        editPersonalData.setSelected(false);

        viewDoctorsNote.setSelected(false);
        editDoctorsNote.setSelected(false);

        selectOtherPeopleData.setSelected(false);

        currentSelectedUser = username;
        userLabel.setText("User: " + currentSelectedUser);
        for(int i = 1; i < 6; i++)
        {
            try {
                if(client.bI().isPermitted(client.encryptMessage(currentSelectedUser, token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(i), token.returnSessionTokenKey()), this.username))
                {
                    switch(i)
                    {
                        case 1:
                            viewPersonalData.setSelected(true);
                            break;
                        case 2:
                            editPersonalData.setSelected(true);
                            break;
                        case 3:
                            viewDoctorsNote.setSelected(true);
                            break;
                        case 4:
                            editDoctorsNote.setSelected(true);
                            break;
                        case 5:
                            selectOtherPeopleData.setSelected(true);
                            break;
                    }
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public SessionToken getSession() { return token; }

    public Client getClient()
    {
        return client;
    }

    public String getUsername()
    {
        return username;
    }

}
