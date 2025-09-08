package br.com.jumpman.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Representa um elevador que pode ser ativado com a chave correta
 */
public class Elevator implements AbstractEntity, Interactable {
    private int x;
    private boolean activated = false;
    
    /**
     * Cria um novo elevador
     * 
     * @param x Posi��o X do elevador
     * @param baseY Posi��o Y da base do elevador
     */
    public Elevator(int x, int baseY) {
        this.x = x;
    }
    
    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        // APENAS caixa de controle no ch�o - a plataforma real � a elevatorPlatform
        
        // Caixa de controle fixa no ch�o
        int controlX = x + 25;
        int controlY = 520; // Fixo no ch�o (y=520)
        
        // Desenhar caixa de controle
        if (activated) {
            g2d.setColor(Color.GREEN);
        } else {
            g2d.setColor(Color.DARK_GRAY);
        }
        g2d.fillRect(controlX, controlY, 15, 25);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(controlX, controlY, 15, 25);
        
        // Bot�es de controle
        if (activated) {
            // Bot�o de subir (tri�ngulo para cima)
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(controlX + 3, controlY + 3, 9, 9);
            g2d.setColor(Color.BLACK);
            int[] xPoints = {controlX + 7, controlX + 3, controlX + 12};
            int[] yPoints = {controlY + 4, controlY + 10, controlY + 10};
            g2d.fillPolygon(xPoints, yPoints, 3);
            
            // Bot�o de descer (tri�ngulo para baixo)
            g2d.setColor(Color.YELLOW);
            g2d.fillRect(controlX + 3, controlY + 13, 9, 9);
            g2d.setColor(Color.BLACK);
            int[] xPoints2 = {controlX + 7, controlX + 3, controlX + 12};
            int[] yPoints2 = {controlY + 21, controlY + 15, controlY + 15};
            g2d.fillPolygon(xPoints2, yPoints2, 3);
        } else {
            // Indicador vermelho quando desativado
            g2d.setColor(Color.RED);
            g2d.fillOval(controlX + 3, controlY + 3, 9, 9);
        }
        
        // Cabo/trilho do elevador (visual) - do ch�o at� bem alto
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawLine(controlX + 7, 50, controlX + 7, 550); // Trilho vertical
        
        // Texto de instru��o
        if (!activated) {
            g2d.setColor(Color.WHITE);
            g2d.drawString("Precisa chave azul", controlX - 30, controlY - 5);
        } else {
            g2d.setColor(Color.GREEN);
            g2d.drawString("Pressione E para subir/descer", controlX - 60, controlY - 5);
        }
    }
    
    /**
     * Ativa o elevador
     */
    public void activate() { 
        this.activated = true; 
    }
    
    /**
     * Verifica se o elevador est� ativado
     * @return true se ativado, false caso contr�rio
     */
    public boolean isActivated() { 
        return activated; 
    }
    
    /**
     * Retorna os limites retangulares da caixa de controle do elevador
     * @return Rectangle representando os limites da caixa de controle
     */
    public Rectangle getControlBoxBounds() {
        int controlX = x + 25;
        int controlY = 520;
        return new Rectangle(controlX, controlY, 15, 25);
    }
    
    @Override
    public Rectangle getBounds() {
        return getControlBoxBounds();
    }
    
    @Override
    public void update() {
        // Elevador n�o tem atualiza��o din�mica pr�pria
    }

    @Override
    public boolean interact(AbstractEntity player) {
        if (!activated) {
            // A l�gica de verifica��o da chave � feita externamente
            return false;
        }
        return true;
    }

    @Override
    public boolean canInteract(AbstractEntity player) {
        Rectangle playerBounds = player.getBounds();
        boolean playerProximo = getControlBoxBounds().intersects(
            new Rectangle(
                playerBounds.x - 10, 
                playerBounds.y - 10, 
                playerBounds.width + 20, 
                playerBounds.height + 20
            )
        );
        return playerProximo;
    }
}
