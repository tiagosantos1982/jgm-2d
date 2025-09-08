package br.com.jumpman.platforms.movement;

import br.com.jumpman.data.enums.StatusMovimento;
import br.com.jumpman.platforms.PlataformaDinamica;

/**
 * Estrategia para plataformas que caem apos contato com o jogador
 */
public class DropablePlatformStrategy extends MovementStrategy {
    
    private int tempoParaCairSegundos;
    private boolean visibleAfterFall;
    private long tempoInicioContato = 0; // Quando player pisou na plataforma
    private boolean playerSobre = false;
    private float velocidade; // pixels por frame
    
    private int xInicial, yInicial; // Posicao inicial para reset
    
    /**
     * Cria uma nova estrategia para plataforma que cai
     * 
     * @param plataforma Plataforma a qual esta estrategia pertence
     * @param velocidade Velocidade de queda em pixels por frame
     * @param tempoParaCairSegundos Tempo em segundos que leva para comecar a cair apos contato
     * @param visibleAfterFall Se a plataforma deve ficar visivel apos cair
     */
    public DropablePlatformStrategy(PlataformaDinamica plataforma, float velocidade,
                                   int tempoParaCairSegundos, boolean visibleAfterFall) {
        super(plataforma);
        this.tempoParaCairSegundos = tempoParaCairSegundos;
        this.visibleAfterFall = visibleAfterFall;
        this.velocidade = velocidade;
        this.xInicial = plataforma.getX();
        this.yInicial = plataforma.getY();
    }
    
    @Override
    public void iniciarMovimento() {
        // Nao faz nada, o movimento e ativado por contato
    }
    
    @Override
    public boolean update() {
        int xAnterior = xAtual;
        int yAnterior = yAtual;
        
        switch (status) {
            case PARADA:
                // Verificar se player esta sobre a plataforma
                if (playerSobre) {
                    if (tempoInicioContato == 0) {
                        tempoInicioContato = System.currentTimeMillis();
                    } else {
                        long tempoDecorrido = System.currentTimeMillis() - tempoInicioContato;
                        if (tempoDecorrido >= tempoParaCairSegundos * 1000) {
                            status = StatusMovimento.CAINDO;
                        }
                    }
                } else {
                    tempoInicioContato = 0; // Reset do timer se player sair
                }
                break;
                
            case CAINDO:
                // Mover para baixo
                setPosition(xAtual, yAtual + (int)velocidade);
                
                // Verificar se saiu da tela (assumindo altura da tela ~600)
                if (yAtual > 600) {
                    if (visibleAfterFall) {
                        status = StatusMovimento.CAIDA_VISIVEL;
                    } else {
                        status = StatusMovimento.CAIDA_INVISIVEL;
                    }
                }
                break;
                
            case CAIDA_INVISIVEL:
            case CAIDA_VISIVEL:
                // Nao faz nada depois de cair
                break;
                
            default:
                // Nao faz nada
                break;
        }
        
        // Retornar true se houve movimento
        return (xAtual != xAnterior || yAtual != yAnterior);
    }
    
    /**
     * Informa se o jogador esta sobre a plataforma
     * 
     * @param playerSobre true se o jogador esta sobre a plataforma, false caso contrario
     */
    public void setPlayerSobre(boolean playerSobre) {
        this.playerSobre = playerSobre;
    }
    
    /**
     * Verifica se o jogador esta sobre a plataforma
     * 
     * @return true se o jogador esta sobre a plataforma, false caso contrario
     */
    public boolean isPlayerSobre() {
        return playerSobre;
    }
    
    /**
     * Reinicia a plataforma para sua posicao original
     */
    public void resetar() {
        xAtual = xInicial;
        yAtual = yInicial;
        status = StatusMovimento.PARADA;
        tempoInicioContato = 0;
        playerSobre = false;
    }
}