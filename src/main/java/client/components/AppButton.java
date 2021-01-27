package client.components;

import client.components.font.Inconsolata;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class AppButton extends JButton {

    private boolean noBorder = false;

    public AppButton()
    {
        initUI();
    }

    private void initUI()
    {
        setBackground(AppColors.BACKGROUND);
        setForeground(AppColors.BORDER);
        setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 1));
        setFont(new Inconsolata().getFont(40));
        setFocusPainted(false);
        setChangeColor();
    }

    @Override
    public void setSize(int width, int height)
    {
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
    }

    private void setChangeColor()
    {
        setContentAreaFilled(false);
        setOpaque(true);
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (getModel().isPressed()) {
                    setBackground(AppColors.BORDER);
                    setForeground(AppColors.BACKGROUND);
                    setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 1));
                    if(noBorder) {
                        setBorder(BorderFactory.createEmptyBorder());
                    }
                } else if (getModel().isRollover()) {
                    setBackground(AppColors.BACKGROUND);
                    setForeground(AppColors.BORDER);
                    setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 1));
                    if(noBorder) {
                        setBorder(BorderFactory.createEmptyBorder());
                    }
                } else {
                    setBackground(AppColors.BACKGROUND);
                    setForeground(AppColors.BORDER);
                    setBorder(BorderFactory.createLineBorder(AppColors.BORDER, 1));
                    if(noBorder) {
                        setBorder(BorderFactory.createEmptyBorder());
                    }
                }
            }
        });
    }

    public void setFontSize(int size)
    {
        setFont(new Inconsolata().getFont(size));
    }

    public void removeBorder()
    {
        noBorder = true;
        setBorder(BorderFactory.createEmptyBorder());
    }
}
