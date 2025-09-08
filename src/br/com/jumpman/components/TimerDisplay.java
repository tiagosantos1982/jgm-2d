package br.com.jumpman.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import br.com.jumpman.utils.ResourceLoader;

/**
 * Componente reutilizável para exibir um contador de tempo com ícone
 * Exibe um ícone e o tempo no formato mm:ss
 */
public class TimerDisplay {
    private BufferedImage icon;
    private int timeSeconds;
    private int posX;
    private int posY;
    private int iconSize;
    private Font font;
    private Color textColor;
    private Timer countdownTimer;

    /**
     * Cria um novo contador de tempo com ícone
     * 
     * @param iconPath Caminho para o arquivo da imagem do ícone
     * @param initialTimeSeconds Tempo inicial em segundos
     * @param posX Posição X do componente na tela
     * @param posY Posição Y do componente na tela
     * @param iconSize Tamanho do ícone (largura e altura iguais)
     */
    public TimerDisplay(String iconPath, int initialTimeSeconds, int posX, int posY, int iconSize) {
        this.timeSeconds = initialTimeSeconds;
        this.posX = posX;
        this.posY = posY;
        this.iconSize = iconSize;
        this.font = new Font("Arial", Font.BOLD, iconSize / 2);
        this.textColor = Color.WHITE;
        
        // Tenta carregar a imagem do ícone usando o ResourceLoader
        icon = ResourceLoader.loadImage(iconPath);
        if (icon == null) {
            // Se falhar, cria uma imagem padrão
            icon = createDefaultIcon();
            System.out.println("Utilizando ícone padrão para o timer");
        }
        
        // Inicia o contador regressivo
        countdownTimer = new Timer(1000, e -> {
            if (timeSeconds > 0) {
                timeSeconds--;
            } else {
                ((Timer)e.getSource()).stop();
            }
        });
        countdownTimer.start();
    }

    /**
     * Cria um ícone padrão caso o arquivo não possa ser carregado
     * 
     * @return Uma imagem de relógio simples como ícone padrão
     */
    private BufferedImage createDefaultIcon() {
        BufferedImage defaultIcon = new BufferedImage(iconSize, iconSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = defaultIcon.createGraphics();
        
        // Desenhar um relógio simples como ícone padrão
        g.setColor(Color.LIGHT_GRAY);
        g.fillOval(2, 2, iconSize - 4, iconSize - 4);
        g.setColor(Color.DARK_GRAY);
        g.drawOval(2, 2, iconSize - 4, iconSize - 4);
        
        // Centro do relógio
        int centerX = iconSize / 2;
        int centerY = iconSize / 2;
        
        // Ponteiros
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        g.drawLine(centerX, centerY, centerX, centerY - iconSize / 3); // Ponteiro das horas
        g.drawLine(centerX, centerY, centerX + iconSize / 4, centerY); // Ponteiro dos minutos
        
        g.dispose();
        return defaultIcon;
    }

    /**
     * Desenha o componente na tela
     * 
     * @param g O contexto gráfico onde desenhar
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Desenhar o ícone
        g2d.drawImage(icon, posX, posY, iconSize, iconSize, null);
        
        // Desenhar o tempo
        g2d.setFont(font);
        g2d.setColor(textColor);
        
        // Calcular minutos e segundos
        int minutes = timeSeconds / 60;
        int seconds = timeSeconds % 60;
        
        // Formatar o tempo (mm:ss)
        String timeText = String.format("%02d:%02d", minutes, seconds);
        
        // Posicionar o texto à direita do ícone com um pequeno espaçamento
        g2d.drawString(timeText, posX + iconSize + 10, posY + iconSize / 2 + font.getSize() / 3);
        
        // Se o tempo estiver acabando (menos de 10 segundos), piscar o texto
        if (timeSeconds < 10 && timeSeconds % 2 == 0) {
            g2d.setColor(Color.RED);
            g2d.drawString(timeText, posX + iconSize + 10, posY + iconSize / 2 + font.getSize() / 3);
        }
    }

    /**
     * Reinicia o temporizador com o tempo especificado
     * 
     * @param seconds Novo tempo em segundos
     */
    public void resetTimer(int seconds) {
        this.timeSeconds = seconds;
        if (!countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    /**
     * Pausa o temporizador
     */
    public void pauseTimer() {
        if (countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
    }

    /**
     * Retoma o temporizador
     */
    public void resumeTimer() {
        if (!countdownTimer.isRunning()) {
            countdownTimer.start();
        }
    }

    /**
     * Retorna o tempo atual em segundos
     * 
     * @return Tempo atual em segundos
     */
    public int getTimeSeconds() {
        return timeSeconds;
    }

    /**
     * Define a cor do texto
     * 
     * @param textColor Nova cor para o texto
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    /**
     * Define a fonte do texto
     * 
     * @param font Nova fonte para o texto
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Limpa recursos quando o componente não for mais necessário
     */
    public void dispose() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
    }
}