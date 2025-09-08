package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;
import br.com.jumpman.platforms.PlataformaSimples;

/**
 * Tela de teste para demonstrar cores em plataformas
 */
public class PlatformColorTestScreen extends JPanel {

    private transient PlataformaSimples[] platforms;
    
    public PlatformColorTestScreen() {
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(800, 600));
        
        // Criar plataformas com cores diferentes
        platforms = new PlataformaSimples[] {
            // Plataformas usando o construtor sem cor
            new PlataformaSimples(50, 100, 100, 20),
            
            // Plataformas usando o construtor com cor
            new PlataformaSimples(50, 150, 100, 20, Color.RED, Color.WHITE),
            new PlataformaSimples(50, 200, 100, 20, Color.GREEN, Color.BLACK),
            new PlataformaSimples(50, 250, 100, 20, Color.BLUE, Color.YELLOW),
            new PlataformaSimples(50, 300, 100, 20, Color.ORANGE, Color.MAGENTA)
        };
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Desenhar plataformas
        for (int i = 0; i < platforms.length; i++) {
            platforms[i].draw(g2);
            
            // Mostrar informacoes sobre a plataforma
            g2.setColor(Color.WHITE);
            if (i == 0) {
                g2.drawString("Plataforma padrao (sem cor especificada)", 170, 110);
            } else {
                Color fill = platforms[i].getColor();
                Color border = platforms[i].getBorderColor();
                g2.drawString(String.format("Plataforma com cores - Preenchimento: %s, Borda: %s", 
                             getColorName(fill), getColorName(border)), 170, 110 + i * 50);
            }
        }
        
        // Titulo e instrucoes
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("Teste de Cores em Plataformas", 300, 50);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("Verificando se as cores estao sendo aplicadas corretamente", 300, 70);
    }
    
    // Metodo para obter o nome da cor para visualizacao
    private String getColorName(Color color) {
        if (color == Color.RED) return "RED";
        if (color == Color.GREEN) return "GREEN";
        if (color == Color.BLUE) return "BLUE";
        if (color == Color.WHITE) return "WHITE";
        if (color == Color.BLACK) return "BLACK";
        if (color == Color.YELLOW) return "YELLOW";
        if (color == Color.MAGENTA) return "MAGENTA";
        if (color == Color.ORANGE) return "ORANGE";
        if (color == Color.DARK_GRAY) return "DARK_GRAY";
        return "Unknown";
    }
}