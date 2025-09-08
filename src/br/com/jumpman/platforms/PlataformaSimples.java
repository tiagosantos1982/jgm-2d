package br.com.jumpman.platforms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Implementacao basica de uma plataforma estatica
 */
public class PlataformaSimples extends AbstractPlatform {
    
    /**
     * Cria uma nova plataforma simples
     * 
     * @param x Posicao X da plataforma
     * @param y Posicao Y da plataforma
     * @param w Largura da plataforma
     * @param h Altura da plataforma
     */
    public PlataformaSimples(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public PlataformaSimples(int x, int y, int w, int h, Color color, Color borderColor) {
        super(x, y, w, h);
        setColor(color);
        setBorderColor(borderColor);
    }

    @Override
    public void update() {
        // Plataformas simples nao tem atualizacao dinamica
    }

    @Override
    public void draw(Graphics g) {
        Color corOriginal = g.getColor();
        
        // Usar a cor definida na plataforma para preenchimento
        g.setColor(color);
        g.fillRect(x, y, width, height);
        
        // Usar a cor de borda definida na plataforma
        g.setColor(borderColor);
        g.drawRect(x, y, width, height);
        
        // Restaurar a cor original do contexto grafico
        g.setColor(corOriginal);
    }
}