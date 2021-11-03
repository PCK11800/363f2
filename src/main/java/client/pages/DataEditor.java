package client.pages;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import client.components.font.Inconsolata;
import client.pages.components.Editor;
import client.pages.components.NamesList;
import server.credentials.SessionToken;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;

public class DataEditor extends JPanel {

    private Client client;
    private String username;
    private String[] currentSelectedPerson = null;
    private SessionToken token;

    public DataEditor(String username, Client client, SessionToken sessionToken)
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

        initNamesList();
        initPersonalFields();
        initMedicalField();
        initSettings();

        enableFunctionalitiesAccordingToPermission();
    }

    private NamesList namesList;
    private void initNamesList()
    {
        namesList = new NamesList(this, 200, 630);
        namesList.setLocation(10, 20);
        namesList.enableList();
        add(namesList);
    }

    private JTextField name, gender, birthday, age, email, phoneNumber;
    private JTextArea address;
    private JLabel nameLabel, genderLabel, birthdayLabel, ageLabel, emailLabel, addressLabel, phoneNumberLabel;
    private void initPersonalFields()
    {
        name = new JTextField();
        gender = new JTextField();
        birthday = new JTextField();
        age = new JTextField();
        address = new JTextArea();
        email = new JTextField();
        phoneNumber = new JTextField();

        nameLabel = new JLabel("Name");
        genderLabel = new JLabel("Gender");
        birthdayLabel = new JLabel("Birthday");
        ageLabel = new JLabel("Age");
        emailLabel = new JLabel("Email* (Must include)");
        addressLabel = new JLabel("Address");
        phoneNumberLabel = new JLabel("Phone Number");

        setFieldSettings(name);
        setFieldSettings(gender);
        setFieldSettings(birthday);
        setFieldSettings(age);
        setFieldSettings(email);
        setFieldSettings(phoneNumber);

        setLabelSettings(nameLabel);
        setLabelSettings(genderLabel);
        setLabelSettings(birthdayLabel);
        setLabelSettings(ageLabel);
        setLabelSettings(emailLabel);
        setLabelSettings(addressLabel);
        setLabelSettings(phoneNumberLabel);

        name.setBounds(220, 30, 250, 40);
        nameLabel.setBounds(220, 0, 250, 40);
        add(name);
        add(nameLabel);

        gender.setBounds(220, 90, 250, 40);
        genderLabel.setBounds(220, 60, 250, 40);
        add(gender);
        add(genderLabel);

        birthday.setBounds(220, 150, 250, 40);
        birthdayLabel.setBounds(220, 120, 250, 40);
        add(birthday);
        add(birthdayLabel);

        age.setBounds(220, 210, 250, 40);
        ageLabel.setBounds(220, 180, 250, 40);
        add(age);
        add(ageLabel);

        email.setBounds(220, 270, 250, 40);
        email.setFont(new Inconsolata().getFont(18));
        emailLabel.setBounds(220, 240, 250, 40);
        add(email);
        add(emailLabel);

        phoneNumber.setBounds(220, 330, 250, 40);
        phoneNumberLabel.setBounds(220, 300, 250, 40);
        add(phoneNumber);
        add(phoneNumberLabel);

        address.setBounds(220, 390, 250, 200);
        addressLabel.setBounds(220, 360, 250, 40);
        add(addressLabel);
        address.setFont(new Inconsolata().getFont(18));
        address.setForeground(AppColors.BORDER);
        address.setBackground(AppColors.BACKGROUND);
        address.setSelectedTextColor(AppColors.BACKGROUND);
        address.setSelectionColor(AppColors.BORDER);
        address.setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 2));
        address.setLineWrap(true);
        add(address);

        name.setEditable(false);
        gender.setEditable(false);
        birthday.setEditable(false);
        age.setEditable(false);
        address.setEditable(false);
        email.setEditable(false);
        phoneNumber.setEditable(false);
        address.setEnabled(false);
    }

    private void setFieldSettings(JTextField textField)
    {
        textField.setForeground(AppColors.BORDER);
        textField.setBackground(AppColors.BACKGROUND);
        textField.setSelectedTextColor(AppColors.BACKGROUND);
        textField.setSelectionColor(AppColors.BORDER);
        textField.setCaretColor(AppColors.BORDER);
        textField.setFont(new Inconsolata().getFont(22));
    }

    private void setLabelSettings(JLabel label)
    {
        label.setForeground(AppColors.BORDER);
        label.setBackground(AppColors.BACKGROUND);
        label.setFont(new Inconsolata().getFont(16));
    }

    private Editor editor;
    private JLabel editorLabel;
    private void initMedicalField()
    {
        editor = new Editor(490, 30, 750, 560);
        editor.disableEditor();
        add(editor);

        editorLabel = new JLabel("Doctor's Note");
        setLabelSettings(editorLabel);
        editorLabel.setBounds(490, 0, 250, 40);
        add(editorLabel);
    }

    private AppButton addNewPerson, savePerson, deletePerson, goBack;
    private void initSettings()
    {
        addNewPerson = new AppButton();
        addNewPerson.setText("Create New");
        addNewPerson.setFontSize(16);
        addNewPerson.setBounds(220, 610, 230, 40);
        addNewPerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewPerson();
                namesList.refresh();
            }
        });
        addNewPerson.setEnabled(false);
        add(addNewPerson);

        savePerson = new AppButton();
        savePerson.setText("Save");
        savePerson.setFontSize(16);
        savePerson.setBounds(480, 610, 230, 40);
        savePerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(email.getText().isEmpty())
                {
                    savePerson.setText("!Missing Email!");
                }
                else {
                    savePerson();
                    namesList.refresh();
                }
            }
        });
        savePerson.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }
            @Override
            public void focusLost(FocusEvent e) {
                savePerson.setText("Save");
            }
        });
        savePerson.setEnabled(false);
        add(savePerson);

        deletePerson = new AppButton();
        deletePerson.setText("Delete");
        deletePerson.setFontSize(16);
        deletePerson.setBounds(745, 610, 230, 40);
        deletePerson.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button Pressed");
                deletePerson(currentSelectedPerson[0]);
                namesList.refresh();
            }
        });
        deletePerson.setEnabled(false);
        add(deletePerson);

        goBack = new AppButton();
        goBack.setText("Return");
        goBack.setFontSize(16);
        goBack.setBounds(1010, 610, 230, 40);
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

    private void createNewPerson()
    {
        name.setText("");
        gender.setText("");
        birthday.setText("");
        age.setText("");
        address.setText("");
        email.setText("");
        phoneNumber.setText("");
        editor.getTextArea().setText("");
    }

    private void savePerson()
    {
        String[] newPerson = new String[8];
        newPerson[0] = name.getText();
        newPerson[1] = gender.getText();
        newPerson[2] = birthday.getText();
        newPerson[3] = age.getText();
        newPerson[4] = address.getText();
        newPerson[5] = email.getText();
        newPerson[6] = phoneNumber.getText();
        newPerson[7] = editor.getTextArea().getText();
        String[] encryptedNewPerson = client.encryptArrayMessage(newPerson, token.returnSessionTokenKey());
        try {
            client.bI().storePerson(encryptedNewPerson, username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void populateFields(String[] person)
    {
        currentSelectedPerson = person;
        name.setText(person[0]);
        gender.setText(person[1]);
        birthday.setText(person[2]);
        age.setText(person[3]);
        address.setText(person[4]);
        email.setText(person[5]);
        phoneNumber.setText(person[6]);
        editor.getTextArea().setText(person[7]);
    }

    private void deletePerson(String name)
    {
        currentSelectedPerson = null;
        try {
            System.out.println("Delete command - Client");
            client.bI().deletePerson(client.encryptMessage(name, token.returnSessionTokenKey()), username);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void enableFunctionalitiesAccordingToPermission()
    {
        try {
            //View Personal data
            if(client.bI().isPermitted(client.encryptMessage(username, token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(1), token.returnSessionTokenKey()), username))
            {
                name.setEnabled(true);
            }
            //Edit Personal Data
            if(client.bI().isPermitted(client.encryptMessage(username, token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(2), token.returnSessionTokenKey()), username))
            {
                name.setEditable(true);
                gender.setEditable(true);
                birthday.setEditable(true);
                age.setEditable(true);
                address.setEditable(true);
                email.setEditable(true);
                phoneNumber.setEditable(true);
                address.setEnabled(true);

                savePerson.setEnabled(true);
            }
            //View Medical Data
            if(client.bI().isPermitted(client.encryptMessage(username, token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(3), token.returnSessionTokenKey()), username))
            {
                editor.enableEditor();
            }
            //Edit Medical Data
            if(client.bI().isPermitted(client.encryptMessage(username, token.returnSessionTokenKey()), client.encryptMessage(Integer.toString(4), token.returnSessionTokenKey()), username))
            {
                editor.enableEditor();
                editor.getTextArea().setEditable(true);
            }
            String encryptedRole = client.bI().getRole(client.encryptMessage(username, token.returnSessionTokenKey()), username);
            int role = Integer.parseInt(client.decryptMessage(encryptedRole, token.returnSessionTokenKey()));
            if(role == 3)
            {
                addNewPerson.setEnabled(true);
                deletePerson.setEnabled(true);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Client getClient()
    {
        return client;
    }

    public String getUsername()
    {
        return username;
    }

    public SessionToken getSession() {return token; }
}
