package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import client.components.font.Inconsolata;
import client.pages.components.UsersList;

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

    public Permissions(String username, Client client)
    {
        this.client = client;
        this.username = username;
        try{
            userRole = client.bI().getRole(username);
        } catch(Exception e){
            System.out.println(e);
        }


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
        userLabel.setBounds(620, 50, 250, 30);
        add(userLabel);
    }

    private UsersList usersList;
    private void initUsersList()
    {
        usersList = new UsersList(this, 200, 460);
        usersList.setLocation(45, 150);
        add(usersList);
    }

    private JCheckBox viewPersonalData, viewAllData, editPersonalData, editAllData
            , addDoctorsNote, deleteDoctorsNotes, createAccounts, deleteAccounts;
    private void initPermissionsFields()
    {

        viewPersonalData = new JCheckBox();
        viewPersonalData.setBounds(320, 150, 250, 30);
        viewPersonalData.setText("View Personal Data");
        setCheckBoxSettings(viewPersonalData);
        add(viewPersonalData);

        if(userRole == 1 || userRole == 2 || userRole == 3) {
            viewAllData = new JCheckBox();
            viewAllData.setBounds(320, 250, 250, 30);
            viewAllData.setText("View All Data");
            setCheckBoxSettings(viewAllData);
            add(viewAllData);
        }

        if(userRole == 1 || userRole == 3) {
            editPersonalData = new JCheckBox();
            editPersonalData.setBounds(320, 350, 250, 30);
            editPersonalData.setText("Edit Personal Data");
            setCheckBoxSettings(editPersonalData);
            add(editPersonalData);

            editAllData = new JCheckBox();
            editAllData.setBounds(320, 450, 250, 30);
            editAllData.setText("Edit All Data");
            setCheckBoxSettings(editAllData);
            add(editAllData);

            addDoctorsNote = new JCheckBox();
            addDoctorsNote.setBounds(820, 150, 250, 30);
            addDoctorsNote.setText("Add Doctors Note");
            setCheckBoxSettings(addDoctorsNote);
            add(addDoctorsNote);

            deleteDoctorsNotes = new JCheckBox();
            deleteDoctorsNotes.setBounds(820, 250, 250, 30);
            deleteDoctorsNotes.setText("Delete Doctors Notes");
            setCheckBoxSettings(deleteDoctorsNotes);
            add(deleteDoctorsNotes);
        }

        /*
        createAccounts = new JCheckBox();
        createAccounts.setBounds(820, 350, 250, 30);
        createAccounts.setText("Create Accounts");
        setCheckBoxSettings(createAccounts);
        add(createAccounts);

        deleteAccounts = new JCheckBox();
        deleteAccounts.setBounds(820, 450, 250, 30);
        deleteAccounts.setText("Delete Accounts");
        setCheckBoxSettings(deleteAccounts);
        add(deleteAccounts);
         */
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

                if(userRole == 1 || userRole == 2 || userRole == 3){
                    if(viewAllData.isSelected()) {
                        added.add(2);
                    } else{
                        removed.add(2);
                    }
                }

                if(userRole == 1 || userRole == 3){
                    if(editPersonalData.isSelected()) {
                        added.add(3);
                    } else{
                        removed.add(3);
                    }

                    if(editAllData.isSelected()) {
                        added.add(4);
                    } else{
                        removed.add(4);
                    }

                    if(addDoctorsNote.isSelected()) {
                        added.add(5);
                    } else{
                        removed.add(5);
                    }

                    if(deleteDoctorsNotes.isSelected()) {
                        added.add(6);
                    } else{
                        removed.add(6);
                    }
                }
                try {
                    for(int i: added){
                        client.bI().addPermission(currentSelectedUser, i, username, client.getPassword());
                    }
                    for(int i: removed){
                        client.bI().removePermission(currentSelectedUser, i, username, client.getPassword());
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
                client.initTaskSelection(username);
            }
        });
        add(goBack);
    }

    // This method is to get the role number from the selected user and
    // show it in the checkboxes.
    // Check UserList's nameButton actionListener
    public void populateFields(String username, int role)
    {
        currentSelectedUser = username;
        userLabel.setText("User: " + currentSelectedUser);
    }

    public Client getClient()
    {
        return client;
    }
}
