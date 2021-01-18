import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class will display the appropriate data to the user.
 */
public class FileViewer {
    private JFrame window = new JFrame("Hospital Data");
    private JPanel mainPanel = new JPanel();
    private JPanel menuBarPanel = new JPanel();
    private JPanel dataPanel = new JPanel();

    private JButton changePassword = new JButton("Change Password");
    private JButton createAccount; //Only initiated if user is admin

    private JLabel dataPlaceHolderLabel = new JLabel("\n\nDATA WILL GO HERE");

    public FileViewer(String user){

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        menuBarPanel.setLayout(new BoxLayout(menuBarPanel, BoxLayout.X_AXIS));
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        menuBarPanel.add(changePassword);
        changePassword.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
            changePasswordClicked();
        }});
        if(isAdmin(user)){
            createAccount = new JButton("Create an account");
            menuBarPanel.add(createAccount);
            createAccount.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
                createAccountClicked();
            }});
        }

        dataPanel.add(dataPlaceHolderLabel);  //-----------------------------------------   place holder only   -----------------------------------------

        mainPanel.add(menuBarPanel);
        mainPanel.add(dataPanel);

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setContentPane(mainPanel);
        window.setSize(900, 800);
        window.setResizable(true);
        window.setVisible(true);
    }

    /**
     * Checks if a user is an admin or not.
     * @return True if user is admin, False otherwise.
     */
    private boolean isAdmin(String user){

        //-----------------------------------------   Needs to be implemented   -----------------------------------------
        return true;
    }

    /**
     * Used by users to change their passwords.
     */
    private void changePasswordClicked(){
        ChangePassword changePassword = new ChangePassword();
    }

    /**
     * Used by an admin to create a new account.
     */
    private void createAccountClicked(){
        CreateAccount createAccount = new CreateAccount();
    }
}
