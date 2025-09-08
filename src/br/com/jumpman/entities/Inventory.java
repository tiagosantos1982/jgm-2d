package br.com.jumpman.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 * Representa o invent�rio do jogador que guarda itens coletados
 */
public class Inventory {
    private ArrayList<String> items;
    private final int MAX_SLOTS = 3;
    
    /**
     * Cria um novo invent�rio vazio
     */
    public Inventory() {
        items = new ArrayList<>();
    }
    
    /**
     * Adiciona um item ao invent�rio
     * 
     * @param item O item a ser adicionado
     * @return true se o item foi adicionado, false se o invent�rio estiver cheio
     */
    public boolean addItem(String item) {
        if (items.size() < MAX_SLOTS) {
            items.add(item);
            return true;
        }
        return false;
    }
    
    /**
     * Remove um item do invent�rio
     * 
     * @param item O item a ser removido
     * @return true se o item foi removido, false se o item n�o estava no invent�rio
     */
    public boolean removeItem(String item) {
        return items.remove(item);
    }
    
    /**
     * Verifica se o invent�rio cont�m um determinado item
     * 
     * @param item O item a ser verificado
     * @return true se o item estiver no invent�rio, false caso contr�rio
     */
    public boolean hasItem(String item) {
        return items.contains(item);
    }
    
    /**
     * Desenha o invent�rio na tela
     * 
     * @param g Contexto gr�fico para desenho
     * @param x Posi��o X para desenhar o invent�rio
     * @param y Posi��o Y para desenhar o invent�rio
     */
    public void draw(Graphics2D g, int x, int y) {
        g.setColor(Color.WHITE);
        g.drawString("Invent�rio (" + items.size() + "/" + MAX_SLOTS + "):", x, y);
        
        for (int i = 0; i < MAX_SLOTS; i++) {
            int slotX = x + i * 30;
            int slotY = y + 10;
            
            // Desenhar slot
            g.setColor(Color.GRAY);
            g.fillRect(slotX, slotY, 25, 25);
            g.setColor(Color.BLACK);
            g.drawRect(slotX, slotY, 25, 25);
            
            // Desenhar item se existir
            if (i < items.size()) {
                String item = items.get(i);
                Color itemColor;
                switch (item) {
                    case "AZUL": itemColor = Color.BLUE; break;
                    case "VERDE": itemColor = Color.GREEN; break;
                    case "AMARELA": itemColor = Color.YELLOW; break;
                    default: itemColor = Color.WHITE; break;
                }
                
                g.setColor(itemColor);
                g.fillOval(slotX + 5, slotY + 5, 15, 15);
                g.setColor(Color.BLACK);
                g.drawOval(slotX + 5, slotY + 5, 15, 15);
            }
        }
    }
    
    /**
     * Retorna a quantidade de itens no invent�rio
     * @return O n�mero de itens
     */
    public int getItemCount() {
        return items.size();
    }
    
    /**
     * Retorna a capacidade m�xima do invent�rio
     * @return A capacidade m�xima
     */
    public int getMaxSlots() {
        return MAX_SLOTS;
    }
}
