package client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;

/**
 * This class lets a user change their password.
 */
public class ChangePassword {
    private JFrame window = new JFrame("Change Password");
    private JPanel mainPanel = new JPanel();
    private JPanel currentPasswordPanel = new JPanel();
    private JPanel newPasswordPanel = new JPanel();
    private JPanel confirmNewPasswordPanel = new JPanel();
    private JPanel submitPanel = new JPanel();
    private JPanel errorPanel = new JPanel();

    private JLabel currentPasswordLabel = new JLabel("Current Password:");
    private JFormattedTextField currentPassword = new JFormattedTextField();

    private JLabel newPasswordLabel = new JLabel("New Password:");
    private JFormattedTextField newPassword = new JFormattedTextField();

    private JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password:");
    private JFormattedTextField confirmNewPassword = new JFormattedTextField();

    private JButton submit = new JButton("Submit");

    private JLabel errorLabel = new JLabel("    ");

    public ChangePassword(){
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        currentPasswordPanel.setLayout(new BoxLayout(currentPasswordPanel, BoxLayout.X_AXIS));
        newPasswordPanel.setLayout(new BoxLayout(newPasswordPanel, BoxLayout.X_AXIS));
        confirmNewPasswordPanel.setLayout(new BoxLayout(confirmNewPasswordPanel, BoxLayout.X_AXIS));

        currentPasswordPanel.add(currentPasswordLabel);
        currentPasswordPanel.add(currentPassword);

        newPasswordPanel.add(newPasswordLabel);
        newPasswordPanel.add(newPassword);

        confirmNewPasswordPanel.add(confirmNewPasswordLabel);
        confirmNewPasswordPanel.add(confirmNewPassword);

        submitPanel.add(submit);

        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);

        mainPanel.add(currentPasswordPanel);
        mainPanel.add(newPasswordPanel);
        mainPanel.add(confirmNewPasswordPanel);
        mainPanel.add(submitPanel);
        mainPanel.add(errorPanel);

        currentPasswordPanel.setMaximumSize(new Dimension(250, 30));
        newPasswordPanel.setMaximumSize(new Dimension(250, 30));
        confirmNewPasswordPanel.setMaximumSize(new Dimension(250, 30));
        submitPanel.setMaximumSize(new Dimension(250, 30));
        errorPanel.setMaximumSize(new Dimension(250, 30));

        window.setContentPane(mainPanel);
        window.setSize(300, 245);
        window.setResizable(false);
        window.setVisible(true);

        submit.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
            submitClicked();
        }});
    }

    private void submitClicked(){
        String newPasswordInput = newPassword.getText();
        String confirmNewPasswordInput = confirmNewPassword.getText();

        //-----------------------------------------   Needs to be implemented   -----------------------------------------
        /*if(!checkPassword(currentPassword.getText())){
            errorLabel.setText("Wrong password!");
            return;
        }*/

        if(!passwordIsValid(newPasswordInput)){
            return;
        }

        if(!newPasswordInput.equals(confirmNewPasswordInput)){
            errorLabel.setText("Passwords do not match!");
            return;
        }
        //-----------------------------------------   Needs to be implemented   -----------------------------------------
        //changePassword(user, newPasswordInput);
        window.dispose();
    }

    /**
     * Checks a password is valid and meets any requirements.
     * @param pWord The password being evaluated.
     * @return True if the requirements are met. False otherwise.
     */
    private boolean passwordIsValid(String pWord){
        //-----------------------------------------   Needs to be implemented   -----------------------------------------
        return true;
    }
}
