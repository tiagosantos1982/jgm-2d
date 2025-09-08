package br.com.jumpman.platforms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import br.com.jumpman.data.enums.StatusMovimento;
import br.com.jumpman.data.enums.TipoMovimento;
import br.com.jumpman.platforms.movement.DropablePlatformStrategy;
import br.com.jumpman.platforms.movement.MovementStrategy;
import br.com.jumpman.platforms.movement.MovingPlatformStrategy;

/**
 * Advanced implementation of platform with dynamic movements
 * English version of PlataformaDinamica
 * Uses Strategy pattern to manage different movement types
 */
public class DynamicPlatform extends SimplePlatform {
    
    // Classe interna para representar pontos de movimento
    public static class Point {
        private int x;
        private int y;
        private int delaySeconds; // Tempo para aguardar neste ponto antes de ir ao proximo
        
        public Point(int x, int y, int delaySeconds) {
            this.x = x;
            this.y = y;
            this.delaySeconds = delaySeconds;
        }
        
        public Point(int x, int y) {
            this(x, y, 0); // Sem delay por padrao
        }
        
        public int getX() { return x; }
        public int getY() { return y; }
        public int getDelaySeconds() { return delaySeconds; }
    }
    
    // Propriedades da plataforma dinamica
    private TipoMovimento type;
    private MovementStrategy movementStrategy; // Estrategia de movimento
    private float speed; // pixels por frame
    
    // Campos para rastrear o ultimo movimento da plataforma
    private int deltaX;
    private int deltaY;
    private int previousX;
    private int previousY;
    
    /**
     * Construtor para plataforma MOVABLE (que se move entre pontos)
     */
    public DynamicPlatform(int x, int y, int w, int h, 
                          List<Point> points, boolean looping, float speed) {
        super(x, y, w, h);
        this.type = TipoMovimento.MOVABLE;
        this.speed = speed;
        this.deltaX = 0;
        this.deltaY = 0;
        this.previousX = x;
        this.previousY = y;
        
        // Converter para pontos da PlataformaDinamica para compatibilidade
        List<PlataformaDinamica.Ponto> convertedPoints = new ArrayList<>();
        for (Point p : points) {
            convertedPoints.add(new PlataformaDinamica.Ponto(p.getX(), p.getY(), p.getDelaySeconds()));
        }
        
        // Criar a estrategia de movimento adequada
        this.movementStrategy = MovementStrategy.criarEstrategia(
            TipoMovimento.MOVABLE, this, convertedPoints, looping, speed, 0, false);
    }
    
    /**
     * Construtor para plataforma DROPABLE (que cai depois de um tempo)
     */
    public DynamicPlatform(int x, int y, int w, int h, 
                          int fallDelaySeconds, boolean visibleAfterFall) {
        super(x, y, w, h);
        this.type = TipoMovimento.DROPABLE;
        this.speed = 3.0f; // Velocidade de queda padrao
        this.deltaX = 0;
        this.deltaY = 0;
        this.previousX = x;
        this.previousY = y;
        
        // Criar a estrategia de movimento adequada
        this.movementStrategy = MovementStrategy.criarEstrategia(
            TipoMovimento.DROPABLE, this, null, false, speed,
            fallDelaySeconds, visibleAfterFall);
    }
    
    /**
     * Construtor para plataforma estatica (compatibilidade)
     */
    public DynamicPlatform(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.type = TipoMovimento.ESTATICA;
        this.deltaX = 0;
        this.deltaY = 0;
        this.previousX = x;
        this.previousY = y;
        
        // Criar a estrategia de movimento adequada (estatica)
        this.movementStrategy = MovementStrategy.criarEstrategia(
            TipoMovimento.ESTATICA, this, null, false, 0, 0, false);
    }
    
    @Override
    public void update() {
        // Salvar a posicao anterior para calcular o delta
        previousX = getX();
        previousY = getY();
        
        // Delegar a atualizacao para a estrategia de movimento
        movementStrategy.update();
        
        // Calcular o delta (mudanca) na posicao
        deltaX = getX() - previousX;
        deltaY = getY() - previousY;
    }
    
    /**
     * Metodo para informar se player esta sobre a plataforma (para DROPABLE)
     */
    public void setPlayerOn(boolean playerOn) {
        if (movementStrategy instanceof DropablePlatformStrategy dropablePlatform) {
            dropablePlatform.setPlayerSobre(playerOn);
        }
    }
    
    /**
     * Retorna a posicao X atual da plataforma
     */
    @Override
    public int getX() {
        return movementStrategy.getX();
    }
    
    /**
     * Retorna a posicao Y atual da plataforma
     */
    @Override
    public int getY() {
        return movementStrategy.getY();
    }
    
    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), width, height);
    }
    
    @Override
    public void draw(Graphics g) {
        // Nao desenhar se esta invisivel apos queda
        if (getStatus() == StatusMovimento.CAIDA_INVISIVEL) {
            return;
        }
        
        // Desenhar diferente baseado no tipo
        Color originalColor = g.getColor();
        Color fillColor = getColor(); // Usar a cor definida na plataforma
        Color borderColor = getBorderColor();   // Usar a cor de borda definida
        
        // Alterar cores com base no estado da plataforma
        switch (type) {
            case MOVABLE:
                if (getStatus() == StatusMovimento.MOVENDO) {
                    fillColor = Color.BLUE; // Azul quando movendo
                } else if (getStatus() == StatusMovimento.AGUARDANDO) {
                    fillColor = Color.CYAN; // Ciano quando aguardando
                }
                break;
                
            case DROPABLE:
                if (getStatus() == StatusMovimento.CAINDO) {
                    fillColor = Color.RED; // Vermelho quando caindo
                } else if (movementStrategy instanceof DropablePlatformStrategy dropablePlatform && 
                          dropablePlatform.isPlayerSobre()) {
                    // Piscar amarelo quando player esta sobre e contando tempo
                    long currentTime = System.currentTimeMillis();
                    if ((currentTime / 200) % 2 == 0) { // Piscar a cada 200ms
                        fillColor = Color.YELLOW;
                    } else {
                        fillColor = Color.ORANGE;
                    }
                }
                break;
                
            case ESTATICA:
            default:
                // Manter a cor definida na plataforma
                break;
        }
        
        // Desenhar a plataforma
        Rectangle bounds = getBounds();
        g.setColor(fillColor);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(borderColor);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Restaurar a cor original
        g.setColor(originalColor);
    }
    
    /**
     * Retorna o tipo de movimento da plataforma
     */
    public TipoMovimento getType() { 
        return type; 
    }
    
    /**
     * Retorna o status atual da plataforma
     */
    public StatusMovimento getStatus() { 
        return movementStrategy.getStatus(); 
    }
    
    /**
     * Verifica se a plataforma esta visivel
     */
    public boolean isVisible() { 
        return getStatus() != StatusMovimento.CAIDA_INVISIVEL; 
    }
    
    /**
     * Verifica se a plataforma esta se movendo
     */
    public boolean isMoving() {
        return getStatus() == StatusMovimento.MOVENDO;
    }
    
    /**
     * Retorna a variacao de posicao X desde a ultima atualizacao
     */
    public int getDeltaX() {
        return deltaX;
    }
    
    /**
     * Retorna a variacao de posicao Y desde a ultima atualizacao
     */
    public int getDeltaY() {
        return deltaY;
    }
    
    /**
     * Ativa o movimento da plataforma (util para elevadores)
     */
    public void activate() {
        if (type == TipoMovimento.MOVABLE) {
            movementStrategy.iniciarMovimento();
        }
    }
    
    /**
     * Move a plataforma para um ponto especifico (controle manual do elevador)
     */
    public void moveToPoint(int pointIndex) {
        if (type == TipoMovimento.MOVABLE && movementStrategy instanceof MovingPlatformStrategy movingPlatform) {
            movingPlatform.irParaPonto(pointIndex);
        }
    }
    
    /**
     * Reseta uma plataforma DROPABLE para sua posicao inicial
     */
    public void reset() {
        if (type == TipoMovimento.DROPABLE && movementStrategy instanceof DropablePlatformStrategy dropablePlatform) {
            dropablePlatform.resetar();
        }
    }
}