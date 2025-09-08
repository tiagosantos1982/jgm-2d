package br.com.jumpman.stages;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * Interface base para est�gios do jogo
 */
public abstract class AbstractStage extends JPanel {
    
    /**
     * Inicializa o est�gio, carregando recursos e configurando elementos
     */
    public abstract void initialize();
    
    /**
     * Configura os controles de input para o est�gio
     */
    protected abstract void setupControls();
    
    /**
     * Atualiza o estado do est�gio, incluindo f�sica, movimentos, etc.
     */
    public abstract void update();
    
    /**
     * M�todo chamado quando o est�gio � finalizado
     * @return true se o est�gio foi conclu�do com sucesso, false caso contr�rio
     */
    public abstract boolean isCompleted();
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }
    
    /**
     * Renderiza o est�gio na tela
     * @param g Contexto gr�fico para desenho
     */
    protected abstract void render(Graphics g);
}
