package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenStory extends JPanel {
    public ScreenStory(MainFrame frame) {
        setLayout(null);
        JLabel lbl = new JLabel("Narrativa / Hist�ria", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 36));
        lbl.setBounds(0, 60, 1024, 60);
        add(lbl);
        // Placeholder para narrativa
        JButton btnNext = new JButton("Avan�ar");
        btnNext.setBounds(462, 400, 140, 40);
        btnNext.addActionListener(e -> frame.showScreen(new ScreenMap()));
        add(btnNext);
    }
}

