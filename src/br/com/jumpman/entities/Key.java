package br.com.jumpman.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Representa uma chave que pode ser coletada pelo jogador
 */
public class Key implements AbstractEntity, Interactable {
    private int x, y;
    private String color;
    private boolean visible = true;
    
    /**
     * Cria uma nova chave
     * 
     * @param x Posi��o X da chave
     * @param y Posi��o Y da chave
     * @param color Cor da chave (AZUL, VERDE, AMARELA, etc)
     */
    public Key(int x, int y, String color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color keyColor;
        switch (color) {
            case "AZUL": keyColor = Color.BLUE; break;
            case "VERDE": keyColor = Color.GREEN; break;
            case "AMARELA": keyColor = Color.YELLOW; break;
            default: keyColor = Color.WHITE; break;
        }
        
        g2d.setColor(keyColor);
        g2d.fillOval(x, y, 15, 15);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(x, y, 15, 15);
        
        // Desenhar texto da cor
        g2d.setColor(Color.WHITE);
        g2d.drawString(color.substring(0, 1), x + 4, y + 25);
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 15, 15);
    }
    
    /**
     * Coleta a chave, tornando-a invis�vel
     */
    public void collect() { 
        this.visible = false; 
    }
    
    @Override
    public void update() {
        // Chaves n�o t�m atualiza��o din�mica
    }
    
    /**
     * Verifica se a chave est� vis�vel/dispon�vel
     * @return true se vis�vel, false caso contr�rio
     */
    public boolean isVisible() { 
        return visible; 
    }
    
    /**
     * Retorna a cor da chave
     * @return String representando a cor
     */
    public String getColor() { 
        return color; 
    }

    @Override
    public boolean interact(AbstractEntity player) {
        if (visible) {
            collect();
            return true;
        }
        return false;
    }

    @Override
    public boolean canInteract(AbstractEntity player) {
        return visible;
    }
}
