package br.com.jumpman.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import br.com.jumpman.platforms.PlataformaDinamica;

/**
 * Representa o jogador controlado pelo usuario
 */
public class Player implements AbstractEntity {
    private int x;
    private int y;
    private int w;
    private int h;
    private double vx;
    private double vy;
    private boolean jumping;
    private Color color;
    
    // Campos para acompanhar a plataforma em que o jogador esta
    private PlataformaDinamica plataformaAtual;
    private boolean ancoraNaPlataforma;
    private int offsetXNaPlataforma; // Offset horizontal relativo a plataforma
    private long ultimaAtualizacao; // Timestamp da ultima atualizacao

    private static final double GRAVITY = 0.5; // Aproximacao simples da gravidade
    private static final double MOVE_SPEED = 4.0;
    private static final double JUMP_FORCE = 10.0;

    /**
     * Cria um novo jogador
     * 
     * @param x Posicao X inicial do jogador
     * @param y Posicao Y inicial do jogador
     * @param w Largura do jogador
     * @param h Altura do jogador
     */
    public Player(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.vx = 0;
        this.vy = 0;
        this.jumping = false;
        this.color = new Color(200, 100, 100);
        this.plataformaAtual = null;
        this.ancoraNaPlataforma = false;
        this.offsetXNaPlataforma = 0;
        this.ultimaAtualizacao = System.currentTimeMillis();
    }

    /**
     * Desenha o jogador na tela
     * 
     * @param g Contexto grafico para desenho
     * @param selected Se true, destaca o jogador como selecionado
     */
    public void draw(Graphics g, boolean selected) {
        Color originalColor = g.getColor();
        
        // Cor base do jogador
        g.setColor(selected ? Color.YELLOW : color);
        g.fillRect(x, y, w, h);
        
        // Contorno do jogador
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);
        
        // Indicacao visual de que est� ancorado
        if (ancoraNaPlataforma) {
            g.setColor(Color.GREEN);
            g.drawRect(x-1, y-1, w+2, h+2);
        }
        
        g.setColor(originalColor);
    }
    
    @Override
    public void draw(Graphics g) {
        draw(g, false);
    }

    /**
     * Move o jogador para a esquerda
     */
    public void moveLeft() {
        vx = -MOVE_SPEED;
    }

    /**
     * Move o jogador para a direita
     */
    public void moveRight() {
        vx = MOVE_SPEED;
    }

    /**
     * Interrompe o movimento horizontal do jogador
     */
    public void stop() {
        vx = 0;
    }

    /**
     * Faz o jogador pular, se nao estiver no ar
     */
    public void jump() {
        if (!jumping) {
            vy = -JUMP_FORCE;
            jumping = true;
            desancorarDaPlataforma(); // Desancora quando pular
        }
    }

    @Override
    public void update() {
        long tempoAtual = System.currentTimeMillis();
        long deltaTemp = tempoAtual - ultimaAtualizacao;
        ultimaAtualizacao = tempoAtual;
        
        // Se ancorado em uma plataforma movel
        if (ancoraNaPlataforma && plataformaAtual != null && !jumping) {
            // Acompanhar a plataforma utilizando os deltas de posicao
            if (plataformaAtual.estaMovendo()) {
                // Usar os deltas da plataforma para ajustar a posicao do jogador
                int deltaX = plataformaAtual.getDeltaX();
                int deltaY = plataformaAtual.getDeltaY();
                
                // Ajustar posicao do jogador junto com a plataforma
                if (deltaX != 0 || deltaY != 0) {
                    x += deltaX;
                    y += deltaY;
                }
            }
            
            // Garantir que o jogador esteja no topo da plataforma
            y = plataformaAtual.getY() - h;
            
            // Aplicar movimento horizontal do jogador
            if (vx != 0) {
                x += vx;
                // Recalcular offset
                offsetXNaPlataforma = x - plataformaAtual.getX();
                
                // Limitar offset para nao sair da plataforma
                int plataformaWidth = plataformaAtual.getBounds().width;
                if (offsetXNaPlataforma < 0) {
                    offsetXNaPlataforma = 0;
                    x = plataformaAtual.getX();
                } else if (offsetXNaPlataforma > plataformaWidth - w) {
                    offsetXNaPlataforma = plataformaWidth - w;
                    x = plataformaAtual.getX() + offsetXNaPlataforma;
                }
                
                // Verificar se saiu da plataforma
                if (x + w <= plataformaAtual.getX() || x >= plataformaAtual.getX() + plataformaWidth) {
                    desancorarDaPlataforma();
                }
            }
        } else {
            // Movimento normal quando nao esta ancorado
            x += vx;
            y += vy;
            vy += GRAVITY;
        }
        
        // Limites da tela
        if (x < 0) x = 0;
        if (x > 800 - w) x = 800 - w;
        if (y > 600 - h) {
            y = 600 - h;
            vy = 0;
            jumping = false;
        }
    }

    /**
     * Faz o jogador pousar em uma plataforma
     * 
     * @param platY Posi��o Y da plataforma
     */
    public void landOn(int platY) {
        // Garantir que o player n�o fique "preso" dentro da plataforma
        if (y + h > platY) {
            y = platY - h;
        }
        vy = 0;
        jumping = false;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    /**
     * Verifica se o jogador est� pulando
     * 
     * @return true se estiver pulando, false caso contr�rio
     */
    public boolean isJumping() {
        return jumping;
    }

    /**
     * Retorna a velocidade vertical do jogador
     * 
     * @return Velocidade vertical atual
     */
    public double getVy() {
        return vy;
    }
    
    /**
     * Retorna a posi��o X do jogador
     * 
     * @return Posi��o X atual
     */
    public int getX() {
        return x;
    }
    
    /**
     * Retorna a posi��o Y do jogador
     * 
     * @return Posi��o Y atual
     */
    public int getY() {
        return y;
    }
    
    /**
     * Define a posi��o Y do jogador
     * 
     * @param y Nova posi��o Y
     */
    public void setY(int y) {
        this.y = y;
    }
    
    /**
     * Define a posi��o X do jogador
     * 
     * @param x Nova posi��o X
     */
    public void setX(int x) {
        this.x = x;
    }
    
    /**
     * Interrompe a queda do jogador
     */
    public void stopFalling() {
        this.vy = 0;
        this.jumping = false;
    }
    
    /**
     * M�todo para interromper o movimento ascendente ao colidir com uma plataforma por baixo
     */
    public void stopRising() {
        // Se estiver subindo, fazer parar de subir
        if (this.vy < 0) {
            this.vy = 1.0; // Pequena velocidade para baixo para iniciar queda
        }
    }
    
    /**
     * Ancora o jogador a uma plataforma movel
     * @param plataforma A plataforma em que o jogador esta
     */
    public void ancorarNaPlataforma(PlataformaDinamica plataforma) {
        if (plataforma != null && !jumping) {
            plataformaAtual = plataforma;
            ancoraNaPlataforma = true;
            
            // Posiciona o player corretamente no topo da plataforma
            y = plataforma.getY() - h;
            
            // Calcula o offset horizontal do jogador em relacao a plataforma
            offsetXNaPlataforma = x - plataforma.getX();
            
            // Limitar offset dentro dos limites da plataforma
            int plataformaWidth = plataforma.getBounds().width;
            if (offsetXNaPlataforma < 0) {
                offsetXNaPlataforma = 0;
                x = plataforma.getX();
            } else if (offsetXNaPlataforma > plataformaWidth - w) {
                offsetXNaPlataforma = plataformaWidth - w;
                x = plataforma.getX() + offsetXNaPlataforma;
            }
        }
    }

    /**
     * Remove a ancora do jogador a plataforma
     */
    public void desancorarDaPlataforma() {
        plataformaAtual = null;
        ancoraNaPlataforma = false;
    }

    /**
     * Atualiza a posicao do jogador com base no movimento da plataforma
     * Este metodo e chamado a partir da tela, nao e mais necessario
     * com o novo sistema de ancora, mas mantido para compatibilidade
     */
    public void atualizarComPlataforma(int deltaX, int deltaY) {
        if (ancoraNaPlataforma && plataformaAtual != null && !jumping) {
            // Aplicar movimento da plataforma ao player
            x += deltaX;
            y += deltaY;
        }
    }

    /**
     * Verifica se o jogador esta ancorado em uma plataforma
     * @return true se estiver ancorado, false caso contrario
     */
    public boolean estaAncorado() {
        return ancoraNaPlataforma;
    }

    /**
     * Obtem a plataforma atual em que o jogador esta
     * @return A plataforma atual ou null
     */
    public PlataformaDinamica getPlataformaAtual() {
        return plataformaAtual;
    }
}
