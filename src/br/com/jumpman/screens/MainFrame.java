package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel currentScreen;

    public MainFrame() {
        setTitle("Reino das Justas");
        setSize(1024, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        showScreen(new ScreenSplash(this::onSplashFinished));
    }

    public void showScreen(JPanel screen) {
        if (currentScreen != null) remove(currentScreen);
        currentScreen = screen;
        add(currentScreen, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    public void onSplashFinished() {
        showScreen(new ScreenInitial(this));
    }
}

