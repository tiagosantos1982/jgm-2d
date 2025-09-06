package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenTeamSelect extends JPanel {
    public ScreenTeamSelect(MainFrame frame) {
        setLayout(null);
        JLabel lbl = new JLabel("Seleção de Time", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 36));
        lbl.setBounds(0, 60, 1024, 60);
        add(lbl);
        // Placeholder para seleção de cavaleiros
        JButton btnNext = new JButton("Avançar");
        btnNext.setBounds(462, 400, 100, 40);
        btnNext.addActionListener(e -> frame.showScreen(new ScreenMap()));
        add(btnNext);
    }
}

