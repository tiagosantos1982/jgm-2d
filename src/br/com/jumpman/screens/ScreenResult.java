package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenResult extends JPanel {
    public ScreenResult(MainFrame frame) {
        setLayout(null);
        JLabel lbl = new JLabel("Resultado da Rodada", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 36));
        lbl.setBounds(0, 60, 1024, 60);
        add(lbl);
        // Placeholder para resultado
        JButton btnMap = new JButton("Voltar ao Mapa");
        btnMap.setBounds(462, 400, 140, 40);
        btnMap.addActionListener(e -> frame.showScreen(new ScreenMap()));
        add(btnMap);
    }
}

