package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScreenSplash extends JPanel {
    private float alpha = 0f;
    private boolean fadingIn = true;
    private boolean fadingOut = false;
    private final Timer timer;
    private final transient Runnable onFinish;
    private int elapsed = 0;

    // Clamp method custom
    private static float clamp(float value, float min, float max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
    // ...existing code...

    public ScreenSplash(Runnable onFinish) {
        this.onFinish = onFinish;
        setBackground(Color.BLACK);
        timer = new Timer(40, new FadeHandler());
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
    float a = clamp(alpha, 0f, 1f);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 64));
        FontMetrics fm = g2.getFontMetrics();
        String text = "TSAN STUDIOS";
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 2 + fm.getAscent() / 2;
        g2.drawString(text, x, y);
    }

    private class FadeHandler implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (fadingIn) {
                alpha = clamp(alpha + 0.05f, 0f, 1f);
                if (alpha >= 1f - 0.0001f) {
                    fadingIn = false;
                }
            } else if (!fadingOut) {
                elapsed += 40;
                if (elapsed >= 3000) {
                    fadingOut = true;
                }
            } else {
                float step = (alpha > 0.6f) ? 0.05f : 0.015f;
                alpha = clamp(alpha - step, 0f, 1f);
                if (alpha <= 0f + 0.0001f) {
                    timer.stop();
                    if (onFinish != null) onFinish.run();
                }
            }
            repaint();
        }
    }
}

