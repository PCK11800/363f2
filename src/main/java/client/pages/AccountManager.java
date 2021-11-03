package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import client.components.extras.GhostText;
import client.components.font.Inconsolata;
import client.pages.components.NamesList;
import client.pages.components.UsersList;
import server.credentials.SessionToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;

public class AccountManager extends JPanel {

    private Client client;
    private String username;
    private int userRole = -1;
    private boolean isAdmin = false;
    private String currentSelectedUser = null;
    private SessionToken token;


    public AccountManager(String username, Client client, SessionToken sessionToken)
    {
        this.token = sessionToken;
        this.client = client;
        this.username = username;
        try{
            //userRole = client.bI().getRole(username);
            String encryptedUserRole = client.bI().getRole(client.encryptMessage(username, token.returnSessionTokenKey()), username);
            userRole = Integer.parseInt(client.decryptMessage(encryptedUserRole, token.returnSessionTokenKey()));
            if(userRole == 3)
            {
                isAdmin = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        if(!isAdmin)
        {
            initNonAdminFields();
        }
        else
        {
            initAdminFields();
        }

    }

    private JTextField oldPassword, newPassword;
    private AppButton setPassword = new AppButton();
    private AppButton goBack = new AppButton();
    private void initNonAdminFields()
    {
        oldPassword = new JTextField();
        oldPassword.setBounds(490, 200, 300, 50);

        newPassword = new JTextField();
        newPassword.setBounds(490, 260, 300, 50);

        setFieldSettings(oldPassword);
        setFieldSettings(newPassword);

        new GhostText(oldPassword, "Old password").changeStyle(Font.ITALIC);
        new GhostText(newPassword, "New password").changeStyle(Font.ITALIC);

        add(oldPassword);
        add(newPassword);

        setPassword.requestFocus();
        setPassword.setText("Set Password");
        setPassword.setFontSize(22);
        setPassword.setBounds(490, 320, 300, 50);

        setPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String oldPassword_str = oldPassword.getText();
                String newPassword_str = newPassword.getText();

                boolean isPasswordValid = false;
                boolean isPasswordStrong = false;

                try {
                    if(client.bI().isPasswordValid(username, client.encryptMessage(oldPassword_str, token.returnSessionTokenKey())))
                    {
                        isPasswordValid = true;
                    }
                    if(client.bI().isPasswordStrong(username, client.encryptMessage(newPassword_str, token.returnSessionTokenKey())))
                    {
                        isPasswordStrong = true;
                    }

                    if(!isPasswordStrong)
                    {
                        setPassword.setText("!Weak Password!");
                    }
                    if(!isPasswordValid)
                    {
                        setPassword.setText("!Wrong Password!");
                    }

                    if(isPasswordStrong && isPasswordValid)
                    {
                        client.bI().changePassword(username, client.encryptMessage(newPassword_str, token.returnSessionTokenKey()), client.encryptMessage(oldPassword_str, token.returnSessionTokenKey()));
                        setPassword.setText("Password Changed!");
                    }

                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        });

        add(setPassword);

        goBack.setText("Return");
        goBack.setBounds(490, 380, 300, 50);
        goBack.setFontSize(22);
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

    private AppButton goBack_admin;
    private void initAdminFields()
    {
        initDeleteUser();
        initCreateUser();

        goBack_admin = new AppButton();
        goBack_admin.setText("Return");
        goBack_admin.setFontSize(16);
        goBack_admin.setBounds(550, 580, 250, 50);
        goBack_admin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeAll();
                repaint();
                client.initTaskSelection(username, token);
            }
        });
        add(goBack_admin);
    }

    private UsersList usersList;
    private JLabel userLabel, roleLabel;
    private AppButton deleteUser;
    private void initDeleteUser()
    {
        usersList = new UsersList(this, 250, 460);
        usersList.setLocation(45, 150);
        add(usersList);

        userLabel = new JLabel();
        userLabel.setText("User: " + currentSelectedUser);
        userLabel.setBackground(AppColors.BACKGROUND);
        userLabel.setForeground(AppColors.BORDER);
        userLabel.setFont(new Inconsolata().getFont(16));
        userLabel.setBounds(320, 280, 300, 30);
        add(userLabel);

        roleLabel = new JLabel();
        roleLabel.setText("Role: null");
        roleLabel.setBackground(AppColors.BACKGROUND);
        roleLabel.setForeground(AppColors.BORDER);
        roleLabel.setFont(new Inconsolata().getFont(16));
        roleLabel.setBounds(320, 320, 300, 30);
        add(roleLabel);

        deleteUser = new AppButton();
        deleteUser.setText("Delete User");
        deleteUser.setFontSize(16);
        deleteUser.setBounds(320, 400, 250, 50);
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean success = false;
                try {
                    success = client.bI().deleteUser(client.encryptMessage(currentSelectedUser,token.returnSessionTokenKey()), username);
                    usersList.refresh();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }

                if(success) { deleteUser.setText("User deleted."); }
                else { deleteUser.setText("Delete Failed."); }
            }
        });
        deleteUser.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                deleteUser.setText("Delete User");
            }
        });
        add(deleteUser);
    }

    private JTextField user_username, user_password;
    private ButtonGroup buttonGroup;
    private JCheckBox patient, staff, regulator;
    private AppButton createUser;
    private void initCreateUser()
    {
        user_username = new JTextField();
        setFieldSettings(user_username);
        user_username.setBounds(750, 150, 350, 30);
        new GhostText(user_username, "Username");
        add(user_username);

        user_password = new JTextField();
        setFieldSettings(user_password);
        user_password.setBounds(750, 190, 350, 30);
        new GhostText(user_password, "Password");
        add(user_password);

        buttonGroup = new ButtonGroup();

        patient = new JCheckBox();
        patient.setBounds(850, 245, 200, 30);
        patient.setText("Patient");
        setCheckBoxSettings(patient);
        buttonGroup.add(patient);
        add(patient);

        staff = new JCheckBox();
        staff.setBounds(850, 295, 200, 30);
        staff.setText("Staff");
        setCheckBoxSettings(staff);
        buttonGroup.add(staff);
        add(staff);

        regulator = new JCheckBox();
        regulator.setBounds(850, 345, 200, 30);
        regulator.setText("Regulator");
        setCheckBoxSettings(regulator);
        buttonGroup.add(regulator);
        add(regulator);

        createUser = new AppButton();
        createUser.setText("Create User");
        createUser.setFontSize(16);
        createUser.setBounds(800, 400, 250, 50);
        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newUsername = user_username.getText();
                String newPassword = user_password.getText();
                int role = -1;
                if(patient.isSelected()) { role = 0; }
                if(staff.isSelected()) { role = 1; }
                if(regulator.isSelected()) { role = 2; }

                if(newUsername.isEmpty() || newPassword.isEmpty() || role == -1)
                {
                    createUser.setText("Missing field");
                }
                else
                {
                    try {
                        if(client.bI().newAccount(client.encryptMessage(newUsername,token.returnSessionTokenKey()), client.encryptMessage(newPassword,token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(role),token.returnSessionTokenKey()), username))
                        {
                            createUser.setText("User created!");
                            usersList.refresh();
                        }
                        else { createUser.setText("!Creation Failed!"); }
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                }
            }
        });

        createUser.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                createUser.setText("Create User");
            }
        });
        add(createUser);
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
                setPassword.setText("Set Password");
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
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

    public void populateFields(String person)
    {
        currentSelectedUser = person;
        userLabel.setText("User: " + currentSelectedUser);

        int role = -1;
        try {
            String encryptedRole = client.bI().getRole(client.encryptMessage(username, token.returnSessionTokenKey()), username);
            role = Integer.parseInt(client.decryptMessage(encryptedRole, token.returnSessionTokenKey()));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        switch(role)
        {
            case -1:
                roleLabel.setText("Role: null");
                break;
            case 0:
                roleLabel.setText("Role: Patient");
                break;
            case 1:
                roleLabel.setText("Role: Staff");
                break;
            case 2:
                roleLabel.setText("Role: Regulator");
                break;
            case 3:
                roleLabel.setText("Role: Admin");
                break;
        }
    }

    public SessionToken getSession() { return token; }

    public Client getClient() {
        return client;
    }

    public String getUsername()
    {
        return username;
    }
}
