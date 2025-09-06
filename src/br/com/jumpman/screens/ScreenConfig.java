package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenConfig extends JPanel {
    public ScreenConfig(MainFrame frame) {
        setLayout(null);
        JLabel lbl = new JLabel("Configurações", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 36));
        lbl.setBounds(0, 60, 1024, 60);
        add(lbl);
        // Placeholder para configurações
        JButton btnBack = new JButton("Voltar");
        btnBack.setBounds(462, 400, 140, 40);
        btnBack.addActionListener(e -> frame.showScreen(new ScreenInitial(frame)));
        add(btnBack);
    }
}

