package br.com.jumpman.screens;

import br.com.jumpman.entities.Player;
import br.com.jumpman.platforms.PlataformaSimples;
import br.com.jumpman.components.TimerDisplay;
import br.com.jumpman.fx.GameWeatherControl;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ScreenTestStage extends JPanel {
    private transient Player player;
    private transient PlataformaSimples[] platforms;
    private transient GameWeatherControl weatherControl;
    private transient TimerDisplay timerDisplay;
    private boolean left;
    private boolean right;

    public ScreenTestStage() {
        setFocusable(true);
        setBackground(new Color(100, 100, 180));
        setPreferredSize(new Dimension(800, 600));
        player = new Player(100, 400, 32, 32);
        platforms = new PlataformaSimples[] {
            new PlataformaSimples(50, 500, 200, 20),
            new PlataformaSimples(300, 400, 150, 20),
            new PlataformaSimples(550, 350, 120, 20),
            new PlataformaSimples(200, 250, 100, 20)
        };
        // Inicializar controle de clima (groundY mais baixo para nao colidir com plataformas)
        weatherControl = new GameWeatherControl(580, 800, 600); // groundY = 580 (era 500)
        
        // Inicializar o timer display - 1 minuto (60 segundos), posicionado no lado direito
        timerDisplay = new TimerDisplay("d:/00-MyLab/java/jumpman/resources/images/timer_icon.txt", 60, 650, 30, 32);
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) left = true;
                if (key == KeyEvent.VK_RIGHT) right = true;
                if (key == KeyEvent.VK_SPACE) {
                    player.jump();
                }
                // Controles de teste do clima
                if (key == KeyEvent.VK_R) {
                    // Alternar chuva com R
                    weatherControl.setRainActive(!weatherControl.isRainActive());
                }
                if (key == KeyEvent.VK_L) {
                    // Disparar raio com L
                    weatherControl.triggerLightning();
                }
                // Controles de periodo do dia
                if (key == KeyEvent.VK_N) {
                    // Ativar periodo NOITE com N (para ver fireflies)
                    weatherControl.setPeriod(br.com.jumpman.timer.PeriodTimer.Period.NOITE);
                }
                if (key == KeyEvent.VK_D) {
                    // Ativar periodo DIA com D
                    weatherControl.setPeriod(br.com.jumpman.timer.PeriodTimer.Period.DIA);
                }
                if (key == KeyEvent.VK_T) {
                    // Ativar periodo TARDE com T
                    weatherControl.setPeriod(br.com.jumpman.timer.PeriodTimer.Period.TARDE);
                }
                
                // Controles de configuracao do clima
                if (key == KeyEvent.VK_1) {
                    // Diminuir intensidade da chuva
                    weatherControl.setRainIntensity(weatherControl.getRainIntensity() - 1);
                }
                if (key == KeyEvent.VK_2) {
                    // Aumentar intensidade da chuva
                    weatherControl.setRainIntensity(weatherControl.getRainIntensity() + 1);
                }
                if (key == KeyEvent.VK_3) {
                    // Diminuir densidade (menos particulas)
                    weatherControl.adjustRainDensity(-10);
                }
                if (key == KeyEvent.VK_4) {
                    // Aumentar densidade (mais particulas)
                    weatherControl.adjustRainDensity(+10);
                }
                if (key == KeyEvent.VK_5) {
                    // Subir chao invisivel (groundY)
                    weatherControl.adjustGroundY(-10);
                }
                if (key == KeyEvent.VK_6) {
                    // Descer chao invisivel (groundY)
                    weatherControl.adjustGroundY(+10);
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_LEFT) left = false;
                if (key == KeyEvent.VK_RIGHT) right = false;
            }
        });
        Timer timer = new Timer(16, e -> {
            updatePlayer();
            updateWeather();
            repaint();
        });
        timer.start();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }
    
    @Override
    public void removeNotify() {
        // Limpar recursos quando o painel for removido
        if (timerDisplay != null) {
            timerDisplay.dispose();
        }
        super.removeNotify();
    }

    private void updateWeather() {
        // Criar lista de obstaculos para colisao da chuva
        java.util.List<Rectangle> obstacles = new java.util.ArrayList<>();
        for (PlataformaSimples plat : platforms) {
            obstacles.add(plat.getBounds());
        }
        // Atualizar clima com colisao melhorada (incluindo player)
        weatherControl.updateWeather(obstacles, player.getBounds());
    }

    private void updatePlayer() {
        if (left) {
            player.moveLeft();
        } else if (right) {
            player.moveRight();
        } else {
            player.stop();
        }
        player.update();
        Rectangle pb = player.getBounds();
        for (PlataformaSimples plat : platforms) {
            Rectangle platRect = plat.getBounds();
            if (pb.intersects(platRect) && pb.y + pb.height - player.getVy() <= platRect.y) {
                // Player esta caindo sobre a plataforma
                player.landOn(platRect.y);
            }
        }
    }

    // Metodos removidos, logica agora esta no Player

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Desenhar clima (fundo com gradiente e chuva)
        weatherControl.drawWeather(g2);
        
        // Plataformas
        for (PlataformaSimples plat : platforms) {
            plat.draw(g2);
        }
        // Player
        player.draw(g2, true);
        // Texto
        g2.setColor(Color.WHITE);
        g2.drawString("Fase de Teste - Use setas para andar e espaco para pular", 20, 30);
        g2.drawString("Pressione R para alternar chuva | L para raio", 20, 50);
        g2.drawString("N = Noite (fireflies) | D = Dia | T = Tarde", 20, 70);
        g2.drawString("1/2 = Intensidade | 3/4 = Densidade | 5/6 = Chao", 20, 90);
        g2.drawString("Left: " + left + " | Right: " + right, 20, 110);
        Rectangle pb = player.getBounds();
        g2.drawString("Player pos: (" + pb.x + ", " + pb.y + ") size: " + pb.width + "x" + pb.height, 20, 130);
        g2.drawString("Chuva ativa: " + weatherControl.isRainActive(), 20, 150);
        g2.drawString("Periodo atual: " + weatherControl.getPeriod(), 20, 170);
        g2.drawString("Intensidade: " + weatherControl.getRainIntensity() + " | Densidade: " + weatherControl.getRainDensity(), 20, 190);
        g2.drawString("GroundY (chao): " + weatherControl.getGroundY(), 20, 210);
        g2.drawString("Splash na chuva: Nas plataformas e no player!", 20, 230);
        
        // Desenhar o timer display no canto superior direito
        timerDisplay.draw(g2);
    }
}
