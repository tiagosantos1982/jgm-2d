package br.com.jumpman.fx;

import java.awt.*;
import java.util.Random;

public class RainParticle {
    private int x;
    private int y;
    private int length;
    private int speed;
    private boolean active;
    private int splashFrame;
    private int panelWidth;  // Armazenar dimensoes para reset correto
    private int panelHeight;
    private static final int SPLASH_FRAMES = 8; // Mais frames para splash mais visivel
    private static final Random rand = new Random();

    public RainParticle(int width, int height) {
        this.panelWidth = width;
        this.panelHeight = height;
        reset(width, height);
    }

    public void reset(int width, int height) {
        x = rand.nextInt(width);
        y = rand.nextInt(height / 2) - height;
        length = 8 + rand.nextInt(4);
        speed = 6 + rand.nextInt(4);
        active = true;
        splashFrame = 0;
    }

    public void update(int groundY, int panelHeight, java.util.List<Rectangle> obstacles, Rectangle playerBounds) {
        if (active) {
            y += speed;
            Rectangle dropRect = new Rectangle(x-1, y+length-1, 3, 3);
            boolean hit = false;
            int hitY = groundY;
            
            // Verificar colisao com o chao
            if (y + length >= groundY) {
                hit = true;
            }
            
            // Verificar colisao com o player (prioridade alta)
            if (!hit && playerBounds != null && dropRect.intersects(playerBounds)) {
                hit = true;
                // Splash no topo do player para parecer que a chuva bate na cabeca/ombros
                hitY = playerBounds.y; 
            }
            
            // Verificar colisao com obstaculos (plataformas)
            if (!hit && obstacles != null) {
                for (Rectangle r : obstacles) {
                    if (dropRect.intersects(r)) {
                        hit = true;
                        hitY = r.y; // Splash na superficie da plataforma
                        break;
                    }
                }
            }
            
            // Verificar se saiu da tela sem colidir (estender range)
            if (!hit && y > panelHeight + 50) { // +50 pixels de margem
                reset(panelWidth, panelHeight);
                return;
            }
            
            if (hit) {
                active = false;
                splashFrame = 1;
                y = hitY - length; // Posicionar splash na superficie
            }
        } else if (splashFrame > 0) {
            splashFrame++;
            if (splashFrame > SPLASH_FRAMES) {
                reset(panelWidth, panelHeight);
            }
        }
    }

    public void draw(Graphics g) {
        if (active) {
            // Chuva mais visivel - cor azul clara mais forte
            g.setColor(new Color(140, 160, 255));
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2f)); // Linha mais grossa
            g2.drawLine(x, y, x, y + length);
            g2.setStroke(new BasicStroke(1f)); // Resetar stroke
        } else if (splashFrame > 0) {
            // Splash mais visivel e duradouro
            Graphics2D g2 = (Graphics2D) g;
            float alpha = 1.0f - (splashFrame / (float)SPLASH_FRAMES); // Fade out gradual
            g2.setColor(new Color(140, 180, 255, (int)(alpha * 255)));
            g2.setStroke(new BasicStroke(1.5f));
            
            int splashSize = splashFrame * 3; // Splash maior
            for (int i = 0; i < 8; i++) { // Mais linhas para splash mais denso
                double angle = Math.PI * i / 4.0;
                int dx = (int)(Math.cos(angle) * splashSize);
                int dy = (int)(Math.sin(angle) * splashSize/3);
                g2.drawLine(x, y + length, x + dx, y + length + dy);
            }
            
            // Adicionar algumas goticulas extras
            for (int i = 0; i < 4; i++) {
                int dropX = x + rand.nextInt(splashSize * 2) - splashSize;
                int dropY = y + length + rand.nextInt(splashSize/2);
                g2.fillOval(dropX, dropY, 2, 2);
            }
            
            g2.setStroke(new BasicStroke(1f)); // Resetar stroke
        }
    }
}

