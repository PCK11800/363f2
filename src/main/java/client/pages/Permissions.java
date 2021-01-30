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

public class Permissions extends JPanel {

    private Client client;
    private String username;
    private String currentSelectedUser = null;

    public Permissions(String username, Client client)
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

    private JCheckBox viewPersonalData, viewDoctorsNote, editPersonalData, editDoctorsNote
            , addPatient, deletePatient, createAccounts, deleteAccounts;
    private void initPermissionsFields()
    {
        viewPersonalData = new JCheckBox();
        viewPersonalData.setBounds(320, 150, 250, 30);
        viewPersonalData.setText("View Personal Data");
        setCheckBoxSettings(viewPersonalData);
        add(viewPersonalData);

        viewDoctorsNote = new JCheckBox();
        viewDoctorsNote.setBounds(320, 250, 250, 30);
        viewDoctorsNote.setText("View Doctors Note");
        setCheckBoxSettings(viewDoctorsNote);
        add(viewDoctorsNote);

        editPersonalData = new JCheckBox();
        editPersonalData.setBounds(320, 350, 250, 30);
        editPersonalData.setText("Edit Personal Data");
        setCheckBoxSettings(editPersonalData);
        add(editPersonalData);

        editDoctorsNote = new JCheckBox();
        editDoctorsNote.setBounds(320, 450, 250, 30);
        editDoctorsNote.setText("Edit Doctors Note");
        setCheckBoxSettings(editDoctorsNote);
        add(editDoctorsNote);

        addPatient = new JCheckBox();
        addPatient.setBounds(820, 150, 250, 30);
        addPatient.setText("Add Patient");
        setCheckBoxSettings(addPatient);
        add(addPatient);

        deletePatient = new JCheckBox();
        deletePatient.setBounds(820, 250, 250, 30);
        deletePatient.setText("Delete Patient");
        setCheckBoxSettings(deletePatient);
        add(deletePatient);

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
                // Save role changes here
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
