package br.com.jumpman.utils;

import java.awt.Rectangle;
import java.util.List;

import br.com.jumpman.entities.AbstractEntity;
import br.com.jumpman.entities.Player;
import br.com.jumpman.platforms.AbstractPlatform;

/**
 * Classe utilit�ria para detec��o de colis�es entre entidades e plataformas
 */
public class CollisionDetector {
    
    /**
     * Verifica colis�es entre um jogador e plataformas, realizando a a��o de pousar quando necess�rio
     * 
     * @param player O jogador a verificar colis�es
     * @param platforms Lista de plataformas para verificar colis�es
     * @return true se houve alguma colis�o, false caso contr�rio
     */
    public static boolean checkPlayerPlatformCollisions(Player player, List<AbstractPlatform> platforms) {
        Rectangle playerBounds = player.getBounds();
        boolean colisionDetected = false;
        
        for (AbstractPlatform platform : platforms) {
            Rectangle platformBounds = platform.getBounds();
            
            // S� verificamos colis�o se o jogador estiver caindo (velocidade vertical positiva)
            if (playerBounds.intersects(platformBounds) && player.getVy() > 0) {
                // Verificar se o jogador est� acima da plataforma
                if (playerBounds.y < platformBounds.y) {
                    player.landOn(platformBounds.y);
                    colisionDetected = true;
                    break;
                }
            }
        }
        
        return colisionDetected;
    }
    
    /**
     * Verifica se h� colis�o entre duas entidades
     * 
     * @param entity1 Primeira entidade
     * @param entity2 Segunda entidade
     * @return true se h� colis�o, false caso contr�rio
     */
    public static boolean checkEntityCollision(AbstractEntity entity1, AbstractEntity entity2) {
        return entity1.getBounds().intersects(entity2.getBounds());
    }
    
    /**
     * Verifica se uma entidade est� em uma �rea de intera��o pr�xima de outra entidade
     * 
     * @param entity1 Entidade principal
     * @param entity2 Entidade para verificar proximidade
     * @param interactionRadius Raio de intera��o em pixels
     * @return true se estiver pr�ximo para intera��o, false caso contr�rio
     */
    public static boolean isNearForInteraction(AbstractEntity entity1, AbstractEntity entity2, int interactionRadius) {
        Rectangle bounds1 = entity1.getBounds();
        Rectangle interactionArea = new Rectangle(
            bounds1.x - interactionRadius, 
            bounds1.y - interactionRadius, 
            bounds1.width + (2 * interactionRadius), 
            bounds1.height + (2 * interactionRadius)
        );
        
        return interactionArea.intersects(entity2.getBounds());
    }
}
