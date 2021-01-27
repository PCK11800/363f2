package client.pages.components;

import client.components.AppColors;
import client.components.font.Inconsolata;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ButtonWindow extends JPanel {

    public ButtonWindow(int xPos, int yPos, int panelWidth, int panelHeight) {
        setBounds(xPos, yPos, panelWidth, panelHeight);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        initTextArea();
        initScrollPane();

        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        setLayout(layout);
        setVisible(true);
    }

    private JPanel buttonArea = new JPanel();
    private void initTextArea() {
        buttonArea.setLayout(new GridLayout());
        buttonArea.setBounds(0, 0, this.getWidth(), this.getHeight());
        add(buttonArea);
    }

    private JScrollPane scrollPane;
    private void initScrollPane() {
        scrollPane = new JScrollPane(buttonArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(5, 1));
        scrollPane.getVerticalScrollBar().setBackground(AppColors.BACKGROUND);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
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

        add(scrollPane);
    }

    public JPanel getButtonArea() {
        return buttonArea;
    }

    public void setButtonArea(JPanel buttonArea) {
        this.buttonArea = buttonArea;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
