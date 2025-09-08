package br.com.jumpman.platforms.movement;

import java.awt.Rectangle;
import java.util.List;
import br.com.jumpman.data.enums.StatusMovimento;
import br.com.jumpman.data.enums.TipoMovimento;
import br.com.jumpman.platforms.PlataformaDinamica;
import br.com.jumpman.platforms.AbstractPlatform;

/**
 * Movement manager for dynamic platforms
 * This class implements the Strategy pattern to encapsulate different
 * types of movement a platform can have.
 */
public abstract class MovementStrategy {
    
    protected AbstractPlatform platform; // Changed to AbstractPlatform for compatibility
    protected int xAtual;
    protected int yAtual;
    protected StatusMovimento status;
    
    /**
     * Creates a new movement strategy
     * 
     * @param platform Platform to which this strategy belongs
     */
    protected MovementStrategy(AbstractPlatform platform) {
        this.platform = platform;
        this.xAtual = platform.getX();
        this.yAtual = platform.getY();
        this.status = StatusMovimento.PARADA;
    }
    
    /**
     * Atualiza a posi��o da plataforma conforme a estrat�gia de movimento
     * 
     * @return true se a posi��o foi alterada, false caso contr�rio
     */
    public abstract boolean update();
    
    /**
     * Reinicia o movimento da plataforma
     */
    public abstract void iniciarMovimento();
    
    /**
     * Retorna a posi��o X atual da plataforma
     * @return posi��o X
     */
    public int getX() {
        return xAtual;
    }
    
    /**
     * Retorna a posi��o Y atual da plataforma
     * @return posi��o Y
     */
    public int getY() {
        return yAtual;
    }
    
    /**
     * Define a posi��o atual da plataforma
     * @param x Nova posi��o X
     * @param y Nova posi��o Y
     */
    protected void setPosition(int x, int y) {
        this.xAtual = x;
        this.yAtual = y;
    }
    
    /**
     * Retorna o status atual do movimento
     * @return Status do movimento
     */
    public StatusMovimento getStatus() {
        return status;
    }
    
    /**
     * Define o status atual do movimento
     * @param status Novo status
     */
    protected void setStatus(StatusMovimento status) {
        this.status = status;
    }
    
    /**
     * Creates a movement strategy appropriate for the platform type
     * 
     * @param tipo Type of movement
     * @param platform Platform to which this strategy belongs
     * @param pontos List of points for movement (only for MOVABLE)
     * @param looping Whether movement is continuous (only for MOVABLE)
     * @param velocidade Movement speed in pixels per frame
     * @param tempoParaCairSegundos Time to fall after contact (only for DROPABLE)
     * @param visibleAfterFall Whether platform remains visible after falling (only for DROPABLE)
     * @return An appropriate movement strategy
     */
    public static MovementStrategy criarEstrategia(
            TipoMovimento tipo, AbstractPlatform platform, 
            List<PlataformaDinamica.Ponto> pontos, boolean looping, float velocidade,
            int tempoParaCairSegundos, boolean visibleAfterFall) {
        
        switch(tipo) {
            case MOVABLE:
                return new MovingPlatformStrategy((PlataformaDinamica)platform, pontos, looping, velocidade);
            case DROPABLE:
                return new DropablePlatformStrategy((PlataformaDinamica)platform, velocidade, tempoParaCairSegundos, visibleAfterFall);
            case ESTATICA:
            default:
                return new StaticPlatformStrategy(platform);
        }
    }
}