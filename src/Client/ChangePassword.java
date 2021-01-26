import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.rmi.RemoteException;

/**
 * This class lets a user change their password.
 */
public class ChangePassword {
    private backendInterface backEnd;
    private String user;
    Evaluation evaluation = new Evaluation();

    private JFrame window = new JFrame("Change Password");
    private JPanel mainPanel = new JPanel();
    private JPanel currentPasswordPanel = new JPanel();
    private JPanel newPasswordPanel = new JPanel();
    private JPanel confirmNewPasswordPanel = new JPanel();
    private JPanel submitPanel = new JPanel();
    private JPanel errorPanel = new JPanel();
    private JPanel errorPanelTwo = new JPanel();

    private JLabel currentPasswordLabel = new JLabel("Current Password:");
    private JFormattedTextField currentPassword = new JFormattedTextField();

    private JLabel newPasswordLabel = new JLabel("New Password:");
    private JFormattedTextField newPassword = new JFormattedTextField();

    private JLabel confirmNewPasswordLabel = new JLabel("Confirm New Password:");
    private JFormattedTextField confirmNewPassword = new JFormattedTextField();

    private JButton submit = new JButton("Submit");

    private JLabel errorLabel = new JLabel("    ");
    private JLabel errorLabelTwo = new JLabel("    ");
    private JLabel errorLabelThree = new JLabel("    ");
    private JLabel errorLabelFour = new JLabel("    ");
    private JLabel errorLabelFive = new JLabel("    ");

    public ChangePassword(backendInterface backEnd, String user){
        this.backEnd = backEnd;
        this.user = user;

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        currentPasswordPanel.setLayout(new BoxLayout(currentPasswordPanel, BoxLayout.X_AXIS));
        newPasswordPanel.setLayout(new BoxLayout(newPasswordPanel, BoxLayout.X_AXIS));
        confirmNewPasswordPanel.setLayout(new BoxLayout(confirmNewPasswordPanel, BoxLayout.X_AXIS));
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
        errorPanelTwo.setLayout(new BoxLayout(errorPanelTwo, BoxLayout.X_AXIS));

        currentPasswordPanel.add(currentPasswordLabel);
        currentPasswordPanel.add(currentPassword);

        newPasswordPanel.add(newPasswordLabel);
        newPasswordPanel.add(newPassword);

        confirmNewPasswordPanel.add(confirmNewPasswordLabel);
        confirmNewPasswordPanel.add(confirmNewPassword);

        submitPanel.add(submit);

        errorLabel.setForeground(Color.RED);
        errorLabelTwo.setForeground(Color.RED);
        errorLabelThree.setForeground(Color.RED);
        errorLabelFour.setForeground(Color.RED);
        errorLabelFive.setForeground(Color.RED);

        errorPanel.add(errorLabel);
        errorPanel.add(errorLabelTwo);
        errorPanel.add(errorLabelThree);
        errorPanel.add(errorLabelFour);
        errorPanel.add(errorLabelFive);

        errorPanelTwo.add(errorPanel);

        mainPanel.add(currentPasswordPanel);
        mainPanel.add(newPasswordPanel);
        mainPanel.add(confirmNewPasswordPanel);
        mainPanel.add(submitPanel);
        mainPanel.add(errorPanelTwo);

        currentPasswordPanel.setMaximumSize(new Dimension(250, 30));
        newPasswordPanel.setMaximumSize(new Dimension(250, 30));
        confirmNewPasswordPanel.setMaximumSize(new Dimension(250, 30));
        submitPanel.setMaximumSize(new Dimension(250, 30));
        errorPanelTwo.setMaximumSize(new Dimension(250, 150));

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

        if(!evaluation.checkPasswordStrength(newPassword.getText())){
            errorLabel.setText("Password must include at least one:");
            errorLabelTwo.setText("Lower case character");
            errorLabelThree.setText("Upper case character");
            errorLabelFour.setText("Special character");
            errorLabelFive.setText("Number");
            return;
        }

        if(!newPasswordInput.equals(confirmNewPasswordInput)){
            errorLabel.setText("Passwords do not match!");
            errorLabelTwo.setText("    ");
            errorLabelThree.setText("    ");
            errorLabelFour.setText("    ");
            errorLabelFive.setText("    ");
            return;
        }

        try {
            if (backEnd.changePassword(user, newPasswordInput, currentPassword.getText())) {
                errorLabel.setText("Wrong password!");
                return;
            }
        } catch (RemoteException re) {
            System.out.println(re);
            errorLabel.setText("System error!");
            return;
        }
        window.dispose();
    }
}
