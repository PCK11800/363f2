
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;

import java.rmi.RemoteException;

/**
 * This class enables a user to log in to the system.
 */
public class LogIn{
    private volatile boolean validInput = false;
    private String user = "";
    private backendInterface backEnd;

    private JFrame window = new JFrame("Log In");
    private JPanel mainPanel = new JPanel();
    private JPanel logInPanel = new JPanel();
    private JPanel userNamePanel = new JPanel();
    private JPanel passwordPanel = new JPanel();
    private JPanel submitPanel = new JPanel();
    private JPanel errorPanel = new JPanel();

    private JLabel logInLabel = new JLabel("Log In");

    private JLabel userNameLabel = new JLabel("User name:");
    private JFormattedTextField userName = new JFormattedTextField();

    private JLabel passwordLabel = new JLabel("Password:");
    private JFormattedTextField password = new JFormattedTextField();

    private JButton submit = new JButton("Submit");

    private JLabel errorLabel = new JLabel("    ");


    public LogIn(backendInterface backEnd){
        this.backEnd = backEnd;
    }

    private void init(){
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        userNamePanel.setLayout(new BoxLayout(userNamePanel, BoxLayout.X_AXIS));
        passwordPanel.setLayout(new BoxLayout(passwordPanel, BoxLayout.X_AXIS));

        logInPanel.add(logInLabel);

        userNamePanel.add(userNameLabel);
        userNamePanel.add(userName);

        passwordPanel.add(passwordLabel);
        passwordPanel.add(password);

        submitPanel.add(submit);

        errorLabel.setForeground(Color.RED);
        errorPanel.add(errorLabel);

        logInPanel.setMaximumSize(new Dimension(250, 30));
        userNamePanel.setMaximumSize(new Dimension(250, 30));
        passwordPanel.setMaximumSize(new Dimension(250, 30));
        submitPanel.setMaximumSize(new Dimension(250, 30));
        errorPanel.setMaximumSize(new Dimension(250, 30));

        mainPanel.add(logInPanel);
        mainPanel.add(userNamePanel);
        mainPanel.add(passwordPanel);
        mainPanel.add(submitPanel);
        mainPanel.add(errorPanel);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setContentPane(mainPanel);
        window.setSize(300, 245);
        window.setResizable(false);
        window.setVisible(true);

        submit.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
            submitClicked();
        }});
    }

    public String getUser(){
        init();
        while(validInput == false){
        }
        window.dispose();
        return user;
    }

    /**
     * This method is called when someone clicks the 'submit' button.
     */
    private void submitClicked(){
        String retrievedUserName = userName.getText();
        String retrievedPassword = password.getText();

        System.out.println("\nsubmit button has been clicked!");
        System.out.println("UserName: " + retrievedUserName);
        System.out.println("Password: " + retrievedPassword);
        System.out.println(" ");

        if(isValid(retrievedUserName, retrievedPassword)){
            user = retrievedUserName;
            validInput = true;
        }
    }

    /**
     * Checks if the password matches the username.
     * @return True if password matches. False otherwise.
     */
    private boolean isValid(String uName, String pWord){
        try {
            if (backEnd.login(uName, pWord)) {
                return true;
            } else {
                errorLabel.setText("Not a valid username/password.");
            }
        } catch (RemoteException re) {
            System.out.println(re);
        }
        return false;
    }
}
