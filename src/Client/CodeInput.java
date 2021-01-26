import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.rmi.RemoteException;

public class CodeInput{
    private volatile boolean validInput = false;
    private backendInterface backEnd;
    private String user;
    private String key;

    private JFrame window = new JFrame("Enter Code");
    private JPanel mainPanel = new JPanel();
    private JPanel instructionPanel = new JPanel();
    private JPanel codeInputPanel = new JPanel();
    private JPanel submitPanel = new JPanel();
    private JPanel errorPanel = new JPanel();

    private JLabel instructionLabel = new JLabel("Enter the code from your email");
    private JFormattedTextField code = new JFormattedTextField();
    private JLabel codeLabel = new JLabel("Code: ");
    private JButton submit = new JButton("Submit");
    private JLabel errorLabel = new JLabel("    ");

    public CodeInput(backendInterface backEnd, String user){
        this.backEnd = backEnd;
        this.user = user;
    }

    private void init(){
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        codeInputPanel.setLayout(new BoxLayout(codeInputPanel, BoxLayout.X_AXIS));

        instructionPanel.add(instructionLabel);
        codeInputPanel.add(codeLabel);
        codeInputPanel.add(code);
        submitPanel.add(submit);
        errorPanel.add(errorLabel);
        errorLabel.setForeground(Color.RED);

        instructionPanel.setMaximumSize(new Dimension(250, 30));
        codeInputPanel.setMaximumSize(new Dimension(250, 30));
        submitPanel.setMaximumSize(new Dimension(250, 30));
        errorPanel.setMaximumSize(new Dimension(250, 30));

        mainPanel.add(instructionPanel);
        mainPanel.add(codeInputPanel);
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

    public String getSessionKey(){
        init();
        while(validInput == false){
        }
        window.dispose();
        return key;
    }

    /**
     * This method is called when someone clicks the 'submit' button.
     */
    private void submitClicked(){
        String inputtedCode = code.getText();

        String sessionKey = isValid(inputtedCode);
        if(sessionKey != null){

            System.out.println(sessionKey); // --------------------------------  For testing only  --------------------------------

            key = sessionKey;
            validInput = true;
        } else{
            errorLabel.setText("Wrong code!");
        }
    }

    /**
     * Checks if the password matches the username.
     * @return True if password matches. False otherwise.
     */
    private String isValid(String inputtedCode){
        int intCode;
        try {
            intCode = Integer.parseInt(inputtedCode);
            return backEnd.validateCode(user, intCode);
        } catch (RemoteException re){
            System.out.println(re);
        } catch (NumberFormatException nfe){
            return null;
        }
        return null;
    }

}