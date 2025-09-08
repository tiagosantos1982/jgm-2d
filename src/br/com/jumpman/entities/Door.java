package br.com.jumpman.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Representa uma porta que pode ser aberta com a chave correta
 */
public class Door implements AbstractEntity, Interactable {
    private int x;
    private int y;
    private boolean open = false;
    
    /**
     * Cria uma nova porta
     * 
     * @param x Posi��o X da porta
     * @param y Posi��o Y da porta
     */
    public Door(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Porta azul
        g2d.setColor(Color.BLUE);
        g2d.fillRect(x, y, 30, 50);
        g2d.setColor(Color.BLUE.darker());
        g2d.drawRect(x, y, 30, 50);
        
        if (!open) {
            // Ma�aneta amarela
            g2d.setColor(Color.YELLOW);
            g2d.fillOval(x + 22, y + 25, 6, 6);
            g2d.setColor(Color.ORANGE);
            g2d.drawOval(x + 22, y + 25, 6, 6);
        } else {
            // Porta aberta (desenhar de lado)
            g2d.setColor(Color.BLUE.brighter());
            g2d.fillRect(x + 25, y, 5, 50);
            g2d.setColor(Color.GREEN);
            g2d.drawString("ABERTA", x - 10, y - 5);
        }
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 30, 50);
    }
    
    /**
     * Abre a porta
     */
    public void open() { 
        this.open = true; 
    }
    
    /**
     * Verifica se a porta est� aberta
     * @return true se aberta, false caso contr�rio
     */
    public boolean isOpen() { 
        return open; 
    }
    
    @Override
    public void update() {
        // Portas n�o t�m atualiza��o din�mica
    }

    @Override
    public boolean interact(AbstractEntity player) {
        if (!open) {
            // A l�gica de verifica��o da chave � feita externamente
            return false;
        }
        return true;
    }

    @Override
    public boolean canInteract(AbstractEntity player) {
        return true; // Sempre pode ser interagida, mas precisa da chave para abrir
    }
}
