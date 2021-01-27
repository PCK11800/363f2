import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.rmi.RemoteException;

public class Permissions{

    private backendInterface backEnd;
    private String user;

    private JFrame window = new JFrame("Change Password");
    private JPanel mainPanel = new JPanel();
    private JPanel selectUserPanel = new JPanel();
    private JPanel errorPanel = new JPanel();
    private JPanel patientPanel = new JPanel();
    private JPanel staffPanel = new JPanel();
    private JPanel regulatorPanel = new JPanel();

    private JPanel pOnePanel = new JPanel();
    private JPanel pTwoPanel = new JPanel();
    private JPanel pThreePanel = new JPanel();
    private JPanel pFourPanel = new JPanel();
    private JPanel pFivePanel = new JPanel();
    private JPanel pSixPanel = new JPanel();
    private JPanel pSevenPanel = new JPanel();
    private JPanel pEightPanel = new JPanel();

    private JLabel selectUserLabel = new JLabel("Account name:");
    private JFormattedTextField usersName = new JFormattedTextField();
    private JButton search = new JButton("Search");
    private JButton save = new JButton("Save");
    private JLabel errorLabel = new JLabel("   ");

    private JCheckBox pOneCheck = new JCheckBox();
    private JLabel pOneLabel = new JLabel("View personal data: ");
    private JCheckBox pTwoCheck = new JCheckBox();
    private JLabel pTwoLabel = new JLabel("View all data: ");
    private JCheckBox pThreeCheck = new JCheckBox();
    private JLabel pThreeLabel = new JLabel("Edit personal data: ");
    private JCheckBox pFourCheck = new JCheckBox();
    private JLabel pFourLabel = new JLabel("Edit all data: ");
    private JCheckBox pFiveCheck = new JCheckBox();
    private JLabel pFiveLabel = new JLabel("Add data: ");
    private JCheckBox pSixCheck = new JCheckBox();
    private JLabel pSixLabel = new JLabel("Delete data: ");
    private JCheckBox pSevenCheck = new JCheckBox();
    private JLabel pSevenLabel = new JLabel("Create accounts: ");
    private JCheckBox pEightCheck = new JCheckBox();
    private JLabel pEightLabel = new JLabel("Delete accounts: ");


    public Permissions(backendInterface backEnd, String user){
        this.backEnd = backEnd;
        this.user = user;

        selectUserPanel.setMaximumSize(new Dimension(450, 30));
        errorPanel.setMaximumSize(new Dimension(450, 30));


        pOnePanel.setMaximumSize(new Dimension(200, 30));
        pTwoPanel.setMaximumSize(new Dimension(200, 30));
        pThreePanel.setMaximumSize(new Dimension(200, 30));
        pFourPanel.setMaximumSize(new Dimension(200, 30));
        pFivePanel.setMaximumSize(new Dimension(200, 30));
        pSixPanel.setMaximumSize(new Dimension(200, 30));
        pSevenPanel.setMaximumSize(new Dimension(200, 30));
        pEightPanel.setMaximumSize(new Dimension(200, 30));

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        selectUserPanel.setLayout(new BoxLayout(selectUserPanel, BoxLayout.X_AXIS));

        patientPanel.setLayout(new BoxLayout(patientPanel, BoxLayout.Y_AXIS));
        staffPanel.setLayout(new BoxLayout(staffPanel, BoxLayout.Y_AXIS));
        regulatorPanel.setLayout(new BoxLayout(regulatorPanel, BoxLayout.Y_AXIS));

        pOnePanel.setLayout(new BoxLayout(pOnePanel, BoxLayout.X_AXIS));
        pTwoPanel.setLayout(new BoxLayout(pTwoPanel, BoxLayout.X_AXIS));
        pThreePanel.setLayout(new BoxLayout(pThreePanel, BoxLayout.X_AXIS));
        pFourPanel.setLayout(new BoxLayout(pFourPanel, BoxLayout.X_AXIS));
        pFivePanel.setLayout(new BoxLayout(pFivePanel, BoxLayout.X_AXIS));
        pSixPanel.setLayout(new BoxLayout(pSixPanel, BoxLayout.X_AXIS));
        pSevenPanel.setLayout(new BoxLayout(pSevenPanel, BoxLayout.X_AXIS));
        pEightPanel.setLayout(new BoxLayout(pEightPanel, BoxLayout.X_AXIS));

        selectUserPanel.add(selectUserLabel);
        selectUserPanel.add(usersName);
        selectUserPanel.add(search);
        selectUserPanel.add(save);

        errorPanel.add(errorLabel);
        errorLabel.setForeground(Color.RED);

        mainPanel.add(selectUserPanel);
        mainPanel.add(errorPanel);

        pOnePanel.add(pOneLabel);
        pOnePanel.add(pOneCheck);
        pTwoPanel.add(pTwoLabel);
        pTwoPanel.add(pTwoCheck);
        pThreePanel.add(pThreeLabel);
        pThreePanel.add(pThreeCheck);
        pFourPanel.add(pFourLabel);
        pFourPanel.add(pFourCheck);
        pFivePanel.add(pFiveLabel);
        pFivePanel.add(pFiveCheck);
        pSixPanel.add(pSixLabel);
        pSixPanel.add(pSixCheck);
        pSevenPanel.add(pSevenLabel);
        pSevenPanel.add(pSevenCheck);
        pEightPanel.add(pEightLabel);
        pEightPanel.add(pEightCheck);

        if(true){  // ----------------------------- If user is staff -- needs to be implemented -----------------------------
            staffPanel.add(pOnePanel);
            staffPanel.add(pTwoPanel);
            staffPanel.add(pThreePanel);
            staffPanel.add(pFourPanel);
            staffPanel.add(pFivePanel);
            staffPanel.add(pSixPanel);

            mainPanel.add(staffPanel);
        } else if(true){ // ----------------------------- If user is regulator -- needs to be implemented -----------------------------
            staffPanel.add(pTwoPanel);
        } else if(true){ // ----------------------------- If user is patient -- needs to be implemented -----------------------------
            staffPanel.add(pOnePanel);
        }

        window.setContentPane(mainPanel);
        window.setSize(475, 350);
        window.setResizable(false);
        window.setVisible(true);

        search.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
            searchClicked();
        }});

        save.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e) {
            saveClicked();
        }});
    }

    private void searchClicked(){
        System.out.println("search clicked");
    }

    private void saveClicked(){
        System.out.println("save clicked");
    }

}