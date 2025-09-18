package br.com.jumpman.utils;

import java.awt.Rectangle;
import java.util.List;

import br.com.jumpman.entities.AbstractEntity;
import br.com.jumpman.entities.Player;
import br.com.jumpman.platforms.AbstractPlatform;
import br.com.jumpman.platforms.PlataformaDinamica;
import br.com.jumpman.platforms.PlataformaDinamica;

/**
 * Classe utilit�ria para detec��o de colis�es entre entidades e plataformas
 */
public class CollisionDetector {
    
    /**
     * Construtor privado para classe utilit�ria
     */
    private CollisionDetector() {
        // Esta classe n�o deve ser instanciada
    }
    
    // Constante para determinar o limiar de sobreposi��o para detec��o de colis�o por cima
    private static final int TOP_COLLISION_THRESHOLD = 10;
    
    /**
     * Verifica colis�es entre um jogador e plataformas, realizando a a��o de pousar quando necess�rio
     * 
     * @param player O jogador a verificar colis�es
     * @param platforms Lista de plataformas para verificar colis�es
     * @return true se houve alguma colis�o, false caso contr�rio
     */
    public static boolean checkPlayerPlatformCollisions(Player player, List<AbstractPlatform> platforms) {
        boolean colisionDetected = false;
        
        for (AbstractPlatform platform : platforms) {
            if (checkAndResolveCollision(player, platform)) {
                colisionDetected = true;
                // N�o paramos o loop aqui para verificar todas as poss�veis colis�es
            }
        }
        
        return colisionDetected;
    }
    
    /**
     * Detecta colis�es entre o player e uma plataforma
     * 
     * @param player Player a verificar
     * @param platform Plataforma para verificar colis�o
     * @return true se ocorreu colis�o e foi resolvida, false caso contr�rio
     */
    /**
     * Verifica se o player est� ancorado na plataforma especificada
     * 
     * @param player O player para verificar
     * @param platform A plataforma para verificar se o player est� ancorado
     * @return true se o player est� ancorado na plataforma, false caso contr�rio
     */
    private static boolean isPlayerOnAnchoredPlatform(Player player, AbstractPlatform platform) {
        if (!player.estaAncorado()) {
            return false;
        }
        
        // Verifica se o player est� ancorado nesta plataforma espec�fica
        // Converte para PlataformaDinamica se for poss�vel
        if (platform instanceof PlataformaDinamica) {
            return player.getPlataformaAtual() == platform;
        }
        
        return false;
    }
    
    /**
     * Detecta colis�es futuras baseadas na velocidade do player
     * 
     * @param player Player para verificar
     * @param platform Plataforma para verificar colis�o
     * @return true se haver� colis�o no pr�ximo frame
     */
    private static boolean detectFutureCollision(Player player, AbstractPlatform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBounds();
        double vy = player.getVy();
        double absVy = Math.abs(vy);
        
        // N�o h� necessidade de verificar colis�o futura se a velocidade for muito baixa
        if (absVy < 0.5) {
            return false;
        }
        
        // Criar pontos de verifica��o baseados na velocidade
        int checkPoints;
        if (absVy > 20) {
            checkPoints = Math.min(10, (int)(absVy / 4)); // No m�ximo 10 pontos
        } else if (absVy > 10) {
            checkPoints = 3;
        } else {
            checkPoints = 1;
        }
        
        // Verificar cada ponto intermedi�rio
        for (int i = 1; i <= checkPoints; i++) {
            double fraction = i / (double)checkPoints;
            Rectangle futureBound = new Rectangle(
                playerBounds.x,
                playerBounds.y + (int)(Math.ceil(vy * fraction)),
                playerBounds.width,
                playerBounds.height
            );
            
            if (futureBound.intersects(platformBounds)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Trata colis�o por cima do player com a plataforma
     * 
     * @param player O player para tratar colis�o
     * @param platform A plataforma colidida
     * @param playerTop O topo do player
     * @param playerBottom O fundo do player
     * @param platformTop O topo da plataforma
     * @param vy A velocidade vertical do player
     * @param nearFutureBounds O ret�ngulo que representa a posi��o futura do player
     * @param platformBounds O ret�ngulo da plataforma
     * @return true se houve colis�o por cima
     */
    private static boolean handleTopCollision(Player player, AbstractPlatform platform, 
                                             int playerTop, int playerBottom, int platformTop, 
                                             double vy, Rectangle nearFutureBounds, Rectangle platformBounds) {
        // Verificar colis�o por cima (player caindo em cima da plataforma)
        // A condi��o foi expandida para melhor detec��o em velocidades altas
        boolean isTopCollision = vy >= 0 && // O player est� caindo ou parado
                                (playerBottom - vy <= platformTop + TOP_COLLISION_THRESHOLD || // O player estava acima da plataforma antes da colis�o
                                 (nearFutureBounds.intersects(platformBounds) && playerTop < platformTop)); // Vai colidir no pr�ximo frame
        
        if (!isTopCollision) {
            return false;
        }
        
        player.landOn(platformTop);
        
        // Verificar se � uma PlataformaDinamica
        if (platform instanceof PlataformaDinamica platDinamica) {
            // Marcar que h� um player sobre a plataforma (importante para plataformas que caem)
            platDinamica.setPlayerSobre(true);
            
            // Para plataformas m�veis, ancorar o player � plataforma
            try {
                boolean isMoving = platDinamica.getStatus().equals(br.com.jumpman.data.enums.StatusMovimento.MOVENDO);
                if (isMoving) {
                    player.ancorarNaPlataforma(platDinamica);
                }
            } catch (Exception e) {
                // Se n�o conseguir verificar o status, ignora
            }
        }
        
        return true;
    }
    
    /**
     * Trata colis�o por baixo do player com a plataforma
     * 
     * @param player O player para tratar colis�o
     * @param playerTop O topo do player
     * @param playerBottom O fundo do player
     * @param platformBottom O fundo da plataforma
     * @return true se houve colis�o por baixo
     */
    private static boolean handleBottomCollision(Player player, int playerTop, int playerBottom, int platformBottom) {
        if (player.getVy() >= 0) {
            return false; // Player n�o est� subindo
        }
        
        boolean isBottomCollision = playerTop <= platformBottom && // O topo do player est� abaixo ou na plataforma
                                   playerBottom >= platformBottom - TOP_COLLISION_THRESHOLD; // O fundo do player est� perto ou acima da base da plataforma
        
        if (!isBottomCollision) {
            return false;
        }
        
        // Se o player est� indo para cima, fazer ele parar a subida
        player.setY(platformBottom);
        player.stopRising();
        return true;
    }
    
    /**
     * Trata colis�o lateral do player com a plataforma
     * 
     * @param player O player para tratar colis�o
     * @param intersection A intersec��o entre player e plataforma
     * @param playerBounds O ret�ngulo do player
     * @param platformBounds O ret�ngulo da plataforma
     * @return true se houve colis�o lateral
     */
    private static boolean handleSideCollision(Player player, Rectangle intersection, Rectangle playerBounds, Rectangle platformBounds) {
        // Se a interse��o � mais alta que larga, � uma colis�o lateral
        if (intersection.height <= intersection.width) {
            return false;
        }
        
        if (playerBounds.x < platformBounds.x) {
            // Colis�o pela direita do player
            player.setX(platformBounds.x - playerBounds.width);
        } else {
            // Colis�o pela esquerda do player
            player.setX(platformBounds.x + platformBounds.width);
        }
        return true;
    }
    
    /**
     * Detecta e resolve colis�o entre player e plataforma
     */
    public static boolean checkAndResolveCollision(Player player, AbstractPlatform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBounds();
        double vy = player.getVy(); // Usar a velocidade do player para os c�lculos
        
        // Se o player est� ancorado em uma plataforma din�mica mas est� colidindo com outra plataforma
        // devemos desancorar ele para permitir que a colis�o seja tratada corretamente
        if (player.estaAncorado() && !isPlayerOnAnchoredPlatform(player, platform) && playerBounds.intersects(platformBounds)) {
            player.desancorarDaPlataforma();
        }
        
        // Detectar colis�o atual e futura para evitar atravessar em alta velocidade
        boolean currentCollision = playerBounds.intersects(platformBounds);
        boolean futureCollision = detectFutureCollision(player, platform);
        
        if (!currentCollision && !futureCollision) {
            return false; // N�o h� colis�o atual nem futura
        }
        
        // Calcular a sobreposi��o
        Rectangle intersection = playerBounds.intersection(platformBounds);
        
        // Obter a posi��o do player
        int playerBottom = playerBounds.y + playerBounds.height;
        int playerTop = playerBounds.y;
        int platformTop = platformBounds.y;
        int platformBottom = platformBounds.y + platformBounds.height;
        
        // Verificar se o player est� subindo e pode atravessar a plataforma de baixo para cima
        if (canPassThroughFromBelow(player, platform)) {
            // Permitir que o player atravesse a plataforma
            return false;
        }
        
        // Criar um ret�ngulo para o futuro pr�ximo do player
        Rectangle nearFutureBounds = new Rectangle(
            playerBounds.x,
            playerBounds.y + (int)(Math.ceil(vy)),
            playerBounds.width,
            playerBounds.height
        );
        
        // Verificar diferentes tipos de colis�o em ordem de prioridade
        if (handleTopCollision(player, platform, playerTop, playerBottom, platformTop, vy, nearFutureBounds, platformBounds)) {
            return true;
        }
        
        if (handleBottomCollision(player, playerTop, playerBottom, platformBottom)) {
            return true;
        }
        
        if (handleSideCollision(player, intersection, playerBounds, platformBounds)) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Verifica se o player pode atravessar uma plataforma de baixo para cima
     * 
     * @param player Player a verificar
     * @param platform Plataforma a verificar
     * @return true se o player pode atravessar, false caso contr�rio
     */
    public static boolean canPassThroughFromBelow(Player player, AbstractPlatform platform) {
        Rectangle playerBounds = player.getBounds();
        Rectangle platformBounds = platform.getBounds();
        
        // Se o player est� subindo (velocidade vertical negativa)
        if (player.getVy() < 0) {
            int playerTop = playerBounds.y;
            int playerBottom = playerBounds.y + playerBounds.height;
            int platformBottom = platformBounds.y + platformBounds.height;
            
            // Condi��o 1: O jogador come�ou a subir de uma posi��o abaixo da plataforma
            boolean startedBelow = playerTop > platformBottom - 10;
            
            // Condi��o 2: A cabe�a do player acabou de entrar na plataforma por baixo
            boolean justEntered = playerTop <= platformBottom && playerTop > platformBottom - 15;
            
            // Condi��o 3: O player j� estava subindo e agora est� no meio do pulo
            boolean midJump = player.isJumping() && playerBottom < platformBottom;
            
            // Condi��o 4: O jogador est� no come�o do pulo, querendo subir atrav�s da plataforma
            boolean earlyJump = player.getVy() < -5 && playerTop >= platformBottom - 20;
            
            // Se qualquer uma das condi��es for verdadeira, permitir a passagem
            if (startedBelow || justEntered || midJump || earlyJump) {
                return true;
            }
        }
        
        return false;
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
