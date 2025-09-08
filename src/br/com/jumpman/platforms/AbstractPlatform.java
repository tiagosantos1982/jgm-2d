package br.com.jumpman.platforms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import br.com.jumpman.entities.AbstractEntity;

/**
 * Classe base para todas as plataformas do jogo
 */
public abstract class AbstractPlatform implements AbstractEntity {
    
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Color color;
    protected Color borderColor;
    
    /**
     * Construtor padr�o para plataformas
     * 
     * @param x Posi��o X da plataforma
     * @param y Posi��o Y da plataforma
     * @param width Largura da plataforma
     * @param height Altura da plataforma
     */
    protected AbstractPlatform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = Color.DARK_GRAY;
        this.borderColor = Color.GREEN;
    }
    
    /**
     * Define a cor da plataforma
     * 
     * @param color Nova cor para a plataforma
     */
    public void setColor(Color color) {
        this.color = color;
    }

    public void setBorderColor(Color color)
    {
        this.borderColor =  color;
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    /**
     * Retorna a posi��o X da plataforma
     */
    public int getX() {
        return x;
    }
    
    /**
     * Retorna a posi��o Y da plataforma
     */
    public int getY() {
        return y;
    }
    
    /**
     * Retorna a largura da plataforma
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Retorna a altura da plataforma
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Retorna a cor de preenchimento da plataforma
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Retorna a cor da borda da plataforma
     */
    public Color getBorderColor() {
        return borderColor;
    }
}
