package br.com.jumpman.platforms.movement;

import br.com.jumpman.data.enums.StatusMovimento;
import br.com.jumpman.platforms.AbstractPlatform;

/**
 * Estrategia para plataformas estaticas (sem movimento)
 */
public class StaticPlatformStrategy extends MovementStrategy {

    /**
     * Cria uma nova estrategia para plataforma estatica
     * 
     * @param platform Plataforma a qual esta estrategia pertence
     */
    public StaticPlatformStrategy(AbstractPlatform platform) {
        super(platform);
        this.status = StatusMovimento.PARADA;
    }

    @Override
    public boolean update() {
        // Plataformas est�ticas n�o se movem
        return false;
    }

    @Override
    public void iniciarMovimento() {
        // Plataformas est�ticas n�o t�m movimento para iniciar
    }
}