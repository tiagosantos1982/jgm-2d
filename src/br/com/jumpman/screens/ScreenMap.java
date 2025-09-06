package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;

public class ScreenMap extends JPanel {
    private static final int POINTS = 7;
    private int playerPos = 0;
    private boolean[] unlocked = {true, true, true, true, false, true, false}; // 5 e 7 bloqueados
    private int[] pointX = {100, 200, 300, 400, 500, 600, 700};
    private int[] pointY = {300, 250, 200, 250, 300, 350, 300};

    private JFrame parentFrame;

    public ScreenMap() {
        setFocusable(true);
        setBackground(new Color(60, 180, 255));
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                int key = e.getKeyCode();
                if (key == java.awt.event.KeyEvent.VK_RIGHT && playerPos < POINTS - 1 && unlocked[playerPos + 1]) {
                    playerPos++;
                    repaint();
                } else if (key == java.awt.event.KeyEvent.VK_LEFT && playerPos > 0 && unlocked[playerPos - 1]) {
                    playerPos--;
                    repaint();
                } else if (key == java.awt.event.KeyEvent.VK_ENTER && unlocked[playerPos] && parentFrame != null) {
                    // Abrir fase de teste
                    parentFrame.setContentPane(new ScreenTestStage());
                    parentFrame.revalidate();
                    parentFrame.repaint();
                }
            }
        });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
        // Tenta obter o JFrame pai
        Container c = getTopLevelAncestor();
        if (c instanceof JFrame jframe) {
            parentFrame = jframe;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw paths
        g2.setStroke(new BasicStroke(4));
        g2.setColor(Color.YELLOW);
        for (int i = 0; i < POINTS - 1; i++) {
            g2.drawLine(pointX[i], pointY[i], pointX[i + 1], pointY[i + 1]);
        }

        // Draw points
        for (int i = 0; i < POINTS; i++) {
            if (unlocked[i]) {
                g2.setColor(Color.GREEN);
            } else {
                g2.setColor(Color.RED);
            }
            g2.fillOval(pointX[i] - 20, pointY[i] - 20, 40, 40);
            g2.setColor(Color.BLACK);
            g2.drawString("" + (i + 1), pointX[i] - 5, pointY[i] + 5);
        }

        // Draw player
        g2.setColor(Color.BLUE);
        g2.fillRect(pointX[playerPos] - 10, pointY[playerPos] - 40, 20, 20);
        g2.setColor(Color.WHITE);
        g2.drawString("Player", pointX[playerPos] - 18, pointY[playerPos] - 45);
    }

    // Metodo para liberar ponto apos conclusao de fase
    public void unlockPoint(int idx) {
        if (idx >= 0 && idx < POINTS) {
            unlocked[idx] = true;
            repaint();
        }
    }

    // Metodo para obter posicao atual do player
    public int getPlayerPos() {
        return playerPos;
    }
}

