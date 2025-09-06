package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenEvolution extends JPanel {
    public ScreenEvolution(MainFrame frame) {
        setLayout(null);
        JLabel lbl = new JLabel("Tela de Evolu��o", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 36));
        lbl.setBounds(0, 60, 1024, 60);
        add(lbl);
        // Placeholder para evolucao
        JButton btnBack = new JButton("Voltar ao Mapa");
        btnBack.setBounds(462, 400, 140, 40);
        btnBack.addActionListener(e -> frame.showScreen(new ScreenMap()));
        add(btnBack);
    }
}

