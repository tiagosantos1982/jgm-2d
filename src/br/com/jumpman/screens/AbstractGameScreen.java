package br.com.jumpman.screens;

import java.awt.Graphics;

/**
 * Interface base para todas as telas do jogo
 */
public interface AbstractGameScreen {
    
    /**
     * Inicializa a tela, carregando recursos necess�rios
     */
    void initialize();
    
    /**
     * Atualiza o estado da tela
     */
    void update();
    
    /**
     * Renderiza a tela
     * @param g Contexto gr�fico para desenho
     */
    void render(Graphics g);
    
    /**
     * M�todo chamado quando a tela recebe foco
     */
    void onEnter();
    
    /**
     * M�todo chamado quando a tela perde foco
     */
    void onExit();
}
