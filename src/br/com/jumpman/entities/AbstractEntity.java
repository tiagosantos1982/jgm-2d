package br.com.jumpman.entities;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Interface base para todas as entidades do jogo
 * Define os metodos essenciais que todas as entidades devem implementar
 */
public interface AbstractEntity {
    
    /**
     * Atualiza o estado da entidade (movimento, fisica, etc)
     * Este metodo e chamado a cada frame do jogo
     */
    void update();
    
    /**
     * Desenha a entidade na tela
     * 
     * @param g O contexto grafico para desenho
     */
    void draw(Graphics g);
    
    /**
     * Retorna os limites retangulares da entidade para deteccao de colisao
     * 
     * @return Rectangle representando os limites da entidade
     */
    Rectangle getBounds();
}
