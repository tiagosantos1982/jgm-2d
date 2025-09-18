package br.com.jumpman.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import br.com.jumpman.platforms.PlataformaDinamica.Ponto;
import br.com.jumpman.entities.Player;
import br.com.jumpman.components.TimerDisplay;
import br.com.jumpman.platforms.PlataformaDinamica;
import br.com.jumpman.platforms.PlataformaSimples;
import br.com.jumpman.utils.CollisionDetector;

public class ScreenFloodStage extends JPanel
{
    // Constantes
    private static final int GAME_WIDTH = 800;
    private static final int GAME_HEIGHT = 600;
    private static final int PLAYER_WIDTH = 32;
    private static final int PLAYER_HEIGHT = 32;

    private transient Player               player;
    private transient PlataformaSimples[]  plataformasFixas;
    private transient PlataformaDinamica[] plataformasMoveis;
    private transient PlataformaSimples    ground;
    private transient TimerDisplay         timerDisplay;
    private boolean                        left;
    private boolean                        right;
    
    // Para debug - mostra informacoes sobre ancoragem
    private boolean                        showDebugInfo = true;

    public ScreenFloodStage()
    {
        //-- screen configuration
       setFocusable(true);
       setBackground(new Color(0, 0, 0));   
       setPreferredSize(getScreenSize());

        //-- game configuration
        player = new Player(100, 400, PLAYER_WIDTH, PLAYER_HEIGHT);

        ground = new PlataformaSimples(0, 550, 800, 50, Color.LIGHT_GRAY, Color.GREEN);

        plataformasFixas = new PlataformaSimples[]{
            new PlataformaSimples(450, 170, 100, 20, Color.DARK_GRAY, Color.YELLOW),
            new PlataformaSimples(200, 340, 100, 20, Color.DARK_GRAY, Color.GREEN),
            new PlataformaSimples(300, 500, 100, 20, Color.DARK_GRAY, Color.GREEN)
        };

        // Criar plataformas moveis
        List<PlataformaDinamica.Ponto> pontosA = new ArrayList<>();
        pontosA.add(new Ponto(400, 250, 1)); // Espera 1 segundo
        pontosA.add(new Ponto(700, 250, 1)); // Espera 1 segundo

        List<PlataformaDinamica.Ponto> pontosB = new ArrayList<>();
        pontosB.add(new Ponto(100, 400, 1));
        pontosB.add(new Ponto(300, 300, 1));
        pontosB.add(new Ponto(100, 200, 1));
        pontosB.add(new Ponto(100, 400, 1));

        plataformasMoveis = new PlataformaDinamica[]{
            // Plataforma que cai quando o jogador fica sobre ela
            new PlataformaDinamica(100, 400, 200, 20, 2, false),
            
            // Plataforma que move horizontalmente
            new PlataformaDinamica(400, 250, 64, 16, pontosA, true, 3.0f),
            
            // Plataforma que se move em um padrao mais complexo
            new PlataformaDinamica(100, 400, 80, 16, pontosB, true, 2.0f)
        };

        timerDisplay = new TimerDisplay("d:/00-MyLab/java/jumpman/resources/images/timer_icon.txt", 60, 650, 30, 32);

        // Configurar controles de teclado
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e.getKeyCode());
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e.getKeyCode());
            }
        });
        
        // Iniciar o game loop
        Timer timer = new Timer(16, e -> {
            updateGame();
            repaint();
        });
        timer.start();
    } 

    private Dimension getScreenSize()
    {
        return new Dimension(GAME_WIDTH, GAME_HEIGHT);
    }

    private void handleKeyPress(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (keyCode == KeyEvent.VK_SPACE) {
            player.jump();
        }
        // Tecla para ativar/desativar debug
        if (keyCode == KeyEvent.VK_D) {
            showDebugInfo = !showDebugInfo;
        }
    }

    private void handleKeyRelease(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }
    
    private void updateGame() {
        // Primeiro atualizar as plataformas moveis
        for (PlataformaDinamica platformDin : plataformasMoveis) {
            platformDin.update();
        }
        
        // Aplicar controles ao player
        if (left) player.moveLeft();
        else if (right) player.moveRight();
        else player.stop();
        
        // Atualizar o player (que ira utilizar sua logica interna de ancoragem)
        player.update();
        
        // Detectar colisoes apos o movimento
        checkCollisions();
    }
    
    /**
     * Verifica colis�es do player com todas as plataformas
     */
    private void checkCollisions() {
        // Usar o m�todo do CollisionDetector para tratar colis�o com o ch�o
        if (CollisionDetector.checkAndResolveCollision(player, ground)) {
            // O ch�o n�o � uma plataforma din�mica, ent�o sempre desancoramos
            player.desancorarDaPlataforma();
        }
        
        // Verificar colis�es com plataformas fixas
        for (PlataformaSimples platform : plataformasFixas) {
            if (CollisionDetector.checkAndResolveCollision(player, platform)) {
                // Nas plataformas fixas, sempre desancoramos
                player.desancorarDaPlataforma();
            }
        }
        
        // Verificar colis�es com plataformas m�veis
        for (PlataformaDinamica platformDin : plataformasMoveis) {
            // S� processamos colis�es com plataformas vis�veis
            if (!platformDin.isVisivel()) {
                if (player.getPlataformaAtual() == platformDin) {
                    // Se a plataforma ficou invis�vel, desancorar o player
                    player.desancorarDaPlataforma();
                }
                platformDin.setPlayerSobre(false);
                continue;
            }
            
            // O m�todo checkAndResolveCollision j� trata todos os tipos de colis�o
            // e gerencia a ancoragem corretamente
            CollisionDetector.checkAndResolveCollision(player, platformDin);
            
            // Verificar se o player saiu da plataforma onde estava ancorado
            if (player.getPlataformaAtual() == platformDin && 
                !platformDin.getBounds().intersects(player.getBounds())) {
                player.desancorarDaPlataforma();
                platformDin.setPlayerSobre(false);
            }
        }
    }
    


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Desenhar o chao
        ground.draw(g2);
        
        // Desenhar plataformas fixas
        for (PlataformaSimples plat : plataformasFixas) 
        {
            plat.draw(g2);
        }
        
        // Desenhar plataformas moveis
        for (PlataformaDinamica plat : plataformasMoveis)
        {
            plat.draw(g2);
        }
        
        // Desenhar player
        player.draw(g2, true);
        
        // Desenhar o timer display no canto superior direito
        timerDisplay.draw(g2);
        
        // Instrucoes na tela
        g2.setColor(Color.WHITE);
        g2.drawString("Fase de Inundacao - Use setas para andar e espaco para pular", 20, 30);
        g2.drawString("Escape antes que a agua te alcance!", 20, 50);
        
        // Informacoes de debug
        if (showDebugInfo) {
            g2.setColor(Color.YELLOW);
            g2.drawString("DEBUG: Pressione 'D' para ocultar", 20, 70);
            g2.drawString("Player ancorado: " + player.estaAncorado(), 20, 90);
            g2.drawString("Player pulando: " + player.isJumping(), 20, 110);
            
            if (player.estaAncorado()) {
                PlataformaDinamica plat = player.getPlataformaAtual();
                g2.drawString("Plataforma atual: X=" + plat.getX() + ", Y=" + plat.getY(), 20, 130);
                g2.drawString("Tipo: " + plat.getTipo() + ", Status: " + plat.getStatus(), 20, 150);
                g2.drawString("DeltaX: " + plat.getDeltaX() + ", DeltaY: " + plat.getDeltaY(), 20, 170);
                
                // Desenhar linha conectando player a plataforma
                g2.setColor(Color.GREEN);
                g2.drawLine(
                    player.getX() + PLAYER_WIDTH/2,
                    player.getY() + PLAYER_HEIGHT,
                    plat.getX() + plat.getBounds().width/2,
                    plat.getY()
                );
            }
        }
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
}