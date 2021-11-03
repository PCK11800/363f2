package client.pages.components;

import client.components.AppColors;
import client.components.font.Inconsolata;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class Editor extends JPanel {

    private JTextArea textArea;
    private JScrollPane scrollPane;

    public Editor(int x, int y, int width, int height)
    {
        setBounds(x, y, width, height);
        initUI();
        setVisible(true);
    }

    private void initUI()
    {
        initTextArea();
        initScrollPane();

        FlowLayout layout = new FlowLayout();
        layout.setVgap(0);
        setLayout(layout);
        setVisible(true);
        setBackground(AppColors.BACKGROUND);

        repaint();
    }

    private void initTextArea()
    {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setBounds(this.getBounds());
        textArea.setFont(new Inconsolata().getFont(16));
        textArea.setLineWrap(true);
        textArea.setForeground(AppColors.BORDER);
        textArea.setBackground(AppColors.BACKGROUND);
        textArea.setSelectedTextColor(AppColors.BACKGROUND);
        textArea.setSelectionColor(AppColors.BORDER);
        textArea.setCaretColor(AppColors.BORDER);
    }

    private void initScrollPane()
    {
        scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(this.getSize());
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
        scrollPane.setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 2));

        add(scrollPane);
    }

    public void disableEditor()
    {
        remove(scrollPane);
    }

    public void enableEditor()
    {
        add(scrollPane);
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public void setTextArea(JTextArea textArea) {
        this.textArea = textArea;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }
}
