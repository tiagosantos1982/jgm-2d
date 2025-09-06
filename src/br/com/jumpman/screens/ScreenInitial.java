package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScreenInitial extends JPanel {
    private float alpha = 0f;
    private MainFrame frame;

    public ScreenInitial(MainFrame frame) {
        this.frame = frame;
        setLayout(null);
        setOpaque(false);
        JLabel title = new JLabel("Reino das Justas", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setBounds(0, 60, 1024, 80);
        add(title);
        JButton btnStart = new JButton("Iniciar");
        btnStart.setBounds(462, 200, 100, 40);
        btnStart.addActionListener(e -> frame.showScreen(new ScreenTeamSelect(frame)));
        add(btnStart);
        JButton btnConfig = new JButton("Configurações");
        btnConfig.setBounds(462, 250, 100, 40);
        btnConfig.addActionListener(e -> frame.showScreen(new ScreenConfig(frame)));
        add(btnConfig);
        JButton btnExit = new JButton("Sair");
        btnExit.setBounds(462, 300, 100, 40);
        btnExit.addActionListener(e -> System.exit(0));
        add(btnExit);

        showStoryThenCombat();
    }

    private void showStoryThenCombat() {
        frame.showScreen(new ScreenStory(frame));
        Timer timer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ScreenCombat removida - voltando para o menu inicial
                frame.showScreen(new ScreenInitial(frame));
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (alpha < 1f) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            super.paintComponent(g2);
            g2.dispose();
        } else {
            super.paintComponent(g);
        }
    }
}

