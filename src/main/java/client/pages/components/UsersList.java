package client.pages.components;

import client.Client;
import client.components.AppButton;
import client.components.AppColors;
import client.components.font.Inconsolata;
import client.pages.AccountManager;
import client.pages.Permissions;
import server.credentials.SessionToken;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

public class UsersList extends JPanel {

    private Permissions permissions;
    private AccountManager accountManager;
    private Client client;
    private int width, height;
    private int mode; //0 = dataEditor, 1 = accountManager
    private SessionToken token;
    private String username;

    public UsersList(Permissions permissions, int width, int height)
    {
        this.permissions = permissions;
        this.token = permissions.getSession();
        this.mode = 0;
        this.client = permissions.getClient();
        this.username = permissions.getUsername();
        this.width = width;
        this.height = height;
        initUI();
        setVisible(true);
    }

    public UsersList(AccountManager accountManager, int width, int height)
    {
        this.accountManager = accountManager;
        this.token = accountManager.getSession();
        this.mode = 1;
        this.client = accountManager.getClient();
        this.username = accountManager.getUsername();
        this.width = width;
        this.height = height;
        initUI();
        setVisible(true);
    }

    private void initUI()
    {
        setLayout(null);
        setSize(width, height);
        setLocation(0, 0);
        setBackground(AppColors.BACKGROUND);

        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        setLayout(layout);

        //setBorder(BorderFactory.createLineBorder(Color.RED));

        initPersonList();
        initScrollPane();
        initFilter();
    }

    JPanel listOfNamesPanel = new JPanel();
    String[] listOfNames = {};
    private void initPersonList() {

        try{
            LinkedList<String> listOfNames_list = client.bI().getAllUsers();
            listOfNames = listOfNames_list.toArray(new String[listOfNames_list.size()]);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        listOfNamesPanel.setBounds(0, 0, width, height - 20);
        listOfNamesPanel.setLayout(new BoxLayout(listOfNamesPanel, BoxLayout.PAGE_AXIS));
        listOfNamesPanel.setBackground(AppColors.BACKGROUND);

        repopulateList((String[]) listOfNames);
    }

    JScrollPane scrollPane;
    private void initScrollPane()
    {
        scrollPane = new JScrollPane(listOfNamesPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(listOfNamesPanel.getWidth(), height - 20));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5,1));
        scrollPane.getVerticalScrollBar().setBackground(AppColors.BACKGROUND);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI(){
            @Override
            protected void configureScrollBarColors(){
                this.thumbColor = AppColors.BORDER;
            }
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        });
        scrollPane.setBackground(AppColors.BACKGROUND);
        scrollPane.setBorder(BorderFactory.createLineBorder(AppColors.BACKGROUND, 1));

        add(scrollPane);
    }

    private JTextField filter = new JTextField();
    private void initFilter()
    {
        filter.setForeground(AppColors.BORDER);
        filter.setBackground(AppColors.BACKGROUND);
        filter.setSelectedTextColor(AppColors.BACKGROUND);
        filter.setSelectionColor(AppColors.BORDER);
        filter.setCaretColor(AppColors.BORDER);
        filter.setFont(new Inconsolata().getFont(16));
        filter.setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 1));

        filter.setPreferredSize(new Dimension(200, 20));
        add(filter);

        filter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filter(filter.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                filter(filter.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                filter(filter.getText());
            }
        });
    }

    private void filter(String input)
    {
        ArrayList<String> filteredNames = new ArrayList<>();
        for(String name : listOfNames)
        {
            if(name.contains(input))
            {
                filteredNames.add(name);
            }
        }
        repopulateList(filteredNames.toArray(String[]::new));
    }

    private void repopulateList(String[] list)
    {
        listOfNamesPanel.removeAll();
        listOfNamesPanel.revalidate();
        listOfNamesPanel.repaint();

        for(int i = 0; i < list.length; i++)
        {
            AppButton nameButton = new AppButton();
            nameButton.setText(list[i]);
            nameButton.setFontSize(16);
            nameButton.setMaximumSize(new Dimension(listOfNamesPanel.getWidth(), 20));
            nameButton.setPreferredSize(new Dimension(listOfNamesPanel.getWidth(), 20));
            nameButton.setMinimumSize(new Dimension(listOfNamesPanel.getWidth(), 20));
            nameButton.removeBorder();

            nameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(mode == 0)
                    {
                        int person = 0;
                        try {
                            String encryptedPerson = client.bI().getRole(client.encryptMessage(nameButton.getText(), token.returnSessionTokenKey()), username);
                            person = Integer.parseInt(client.decryptMessage(encryptedPerson, token.returnSessionTokenKey()));
                        } catch (RemoteException remoteException) {
                            remoteException.printStackTrace();
                        }
                        permissions.populateFields(nameButton.getText(), person);
                    }
                    if(mode == 1)
                    {
                        String person = null;
                        person = nameButton.getText();
                        accountManager.populateFields(person);
                    }
                }
            });
            listOfNamesPanel.add(nameButton);
        }
    }

    public void refresh()
    {
        initPersonList();
        filter(filter.getText());
    }
}
