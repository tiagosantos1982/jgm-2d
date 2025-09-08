package br.com.jumpman.platforms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Implementa��o b�sica de uma plataforma est�tica
 * Esta classe � a vers�o em ingl�s de PlataformaSimples
 */
public class SimplePlatform extends AbstractPlatform {
    
    /**
     * Cria uma nova plataforma simples
     * 
     * @param x Posi��o X da plataforma
     * @param y Posi��o Y da plataforma
     * @param w Largura da plataforma
     * @param h Altura da plataforma
     */
    public SimplePlatform(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    /**
     * Cria uma nova plataforma simples com cores personalizadas
     * 
     * @param x Posi��o X da plataforma
     * @param y Posi��o Y da plataforma
     * @param w Largura da plataforma
     * @param h Altura da plataforma
     * @param color Cor de preenchimento da plataforma
     * @param borderColor Cor da borda da plataforma
     */
    public SimplePlatform(int x, int y, int w, int h, Color color, Color borderColor) {
        super(x, y, w, h);
        setColor(color);
        setBorderColor(borderColor);
    }

    @Override
    public void update() {
        // Plataformas simples n�o t�m atualiza��o din�mica
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
        
        // Restaurar a cor original do contexto gr�fico
        g.setColor(corOriginal);
    }
}