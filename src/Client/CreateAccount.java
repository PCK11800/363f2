import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.rmi.RemoteException;

/**
 * This class lets an admin create a new account.
 */
public class CreateAccount {
    private backendInterface backEnd;
    private String user;

    private JFrame window = new JFrame("Create a new account");
    private JPanel mainPanel = new JPanel();
    private JPanel accountNamePanel = new JPanel();
    private JPanel accountPasswordPanel = new JPanel();
    private JPanel radioButtonPanel = new JPanel();
    private JPanel userTypePanel = new JPanel();
    private JPanel submitPanel = new JPanel();
    private JPanel errorPanel = new JPanel();

    private JRadioButton patientButton = new JRadioButton("Patient");
    private JRadioButton staffButton = new JRadioButton("Staff");
    private JRadioButton regulatorButton = new JRadioButton("Regulator");
    private ButtonGroup userTypeButtons = new ButtonGroup();

    private JLabel accountNameLabel = new JLabel("Account name:");
    private JFormattedTextField accountName = new JFormattedTextField();

    private JLabel passwordLabel = new JLabel("Password:");
    private JFormattedTextField password = new JFormattedTextField();

    private JButton submit = new JButton("Submit");

    private JLabel errorLabel = new JLabel("    ");

    public CreateAccount(backendInterface backEnd, String user){
        this.backEnd = backEnd;
        this.user = user;

        // --------------------------- implement check here to make sure user is admin???? ---------------------------

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        accountNamePanel.setLayout(new BoxLayout(accountNamePanel, BoxLayout.X_AXIS));
        accountPasswordPanel.setLayout(new BoxLayout(accountPasswordPanel, BoxLayout.X_AXIS));
        radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
        userTypePanel.setLayout(new BoxLayout(userTypePanel, BoxLayout.X_AXIS));

        accountNamePanel.add(accountNameLabel);
        accountNamePanel.add(accountName);

        accountPasswordPanel.add(passwordLabel);
        accountPasswordPanel.add(password);

        userTypeButtons.add(patientButton);
        userTypeButtons.add(staffButton);
        userTypeButtons.add(regulatorButton);

        radioButtonPanel.add(patientButton);
        radioButtonPanel.add(staffButton);
        radioButtonPanel.add(regulatorButton);
        userTypePanel.add(radioButtonPanel);

        submitPanel.add(submit);

        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);


        accountNamePanel.setMaximumSize(new Dimension(250, 30));
        accountPasswordPanel.setMaximumSize(new Dimension(250, 30));
        userTypePanel.setMaximumSize(new Dimension(250, 70));
        submitPanel.setMaximumSize(new Dimension(250, 30));
        errorPanel.setMaximumSize(new Dimension(250, 30));

        mainPanel.add(accountNamePanel);
        mainPanel.add(accountPasswordPanel);
        mainPanel.add(userTypePanel);
        mainPanel.add(submitPanel);
        mainPanel.add(errorPanel);

        window.setContentPane(mainPanel);
        window.setSize(300, 245);
        window.setResizable(false);
        window.setVisible(true);

        submit.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
            submitClicked();
        }});
    }

    private void submitClicked(){
        String uName = accountName.getText();
        String pWord = password.getText();
        int type = -1;

        /*if(!passwordIsValid(pWord)){
            return;
        }*/

        if(patientButton.isSelected()){
            type = 0;
        } else if(staffButton.isSelected()){
            type = 1;
        } else if(regulatorButton.isSelected()){
            type = 2;
        } else{
            errorLabel.setText("Please select the account type");
            return;
        }

        try {
            if (!backEnd.newAccount(uName, pWord)){
                errorLabel.setText("Account unavailable!");
                return;
            }
        } catch (RemoteException re) {
            System.out.println(re);
            errorLabel.setText("System error!");
            return;
        }
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
