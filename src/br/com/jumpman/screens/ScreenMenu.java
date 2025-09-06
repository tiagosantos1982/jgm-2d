package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenMenu extends JPanel {
    private JButton btnStart;
    private JButton btnContinue;
    private JButton btnOption;
    // ...existing code...

    public ScreenMenu(MenuListener listener) {
    // ...existing code...
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        btnStart = new JButton("Game Start");
        btnContinue = new JButton("Continue");
        btnOption = new JButton("Option");

        btnStart.addActionListener(e -> listener.onStart());
        btnContinue.addActionListener(e -> listener.onContinue());
        btnOption.addActionListener(e -> listener.onOption());

        gbc.gridy = 0;
        add(btnStart, gbc);
        gbc.gridy = 1;
        add(btnContinue, gbc);
        gbc.gridy = 2;
        add(btnOption, gbc);
    }

    public interface MenuListener {
        void onStart();
        void onContinue();
        void onOption();
    }
}
