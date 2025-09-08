package br.com.jumpman.platforms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import br.com.jumpman.data.enums.StatusMovimento;
import br.com.jumpman.data.enums.TipoMovimento;

/**
 * Implementa��o avan�ada de plataforma com movimentos din�micos
 */
public class PlataformaDinamica extends PlataformaSimples {
    
    // Classe interna para representar pontos de movimento
    public static class Ponto {
        public int x, y;
        public int delaySegundos; // Tempo para aguardar neste ponto antes de ir ao proximo
        
        public Ponto(int x, int y, int delaySegundos) {
            this.x = x;
            this.y = y;
            this.delaySegundos = delaySegundos;
        }
        
        public Ponto(int x, int y) {
            this(x, y, 0); // Sem delay por padrao
        }
    }
    
    // Propriedades da plataforma dinamica
    private TipoMovimento tipo;
    private StatusMovimento status;
    private List<Ponto> pontos;
    private int pontoAtualIndex;
    private boolean looping;
    private float velocidade; // pixels por frame
    
    // Para DROPABLE
    private int tempoParaCairSegundos;
    private boolean visibleAfterFall;
    private long tempoInicioContato = 0; // Quando player pisou na plataforma
    private boolean playerSobre = false;
    
    // Para movimento suave
    private int xInicial, yInicial; // Posicao inicial da plataforma
    private int xDestino, yDestino; // Destino atual
    private long tempoInicioAguardo = 0;
    
    // Campos para controle da posicao atual
    private int xAtual;
    private int yAtual;
    
    // Campos para rastrear o �ltimo movimento da plataforma
    private int xAnterior;
    private int yAnterior;
    private int deltaX;
    private int deltaY;
    
    /**
     * Construtor para plataforma MOVABLE
     */
    public PlataformaDinamica(int x, int y, int w, int h, 
                             List<Ponto> pontos, boolean looping, float velocidade) {
        super(x, y, w, h);
        this.xAtual = x;
        this.yAtual = y;
        this.xAnterior = x;
        this.yAnterior = y;
        this.deltaX = 0;
        this.deltaY = 0;
        this.xInicial = x;
        this.yInicial = y;
        this.tipo = TipoMovimento.MOVABLE;
        this.status = StatusMovimento.PARADA;
        this.pontos = new ArrayList<>(pontos);
        this.pontoAtualIndex = 0;
        this.looping = looping;
        this.velocidade = velocidade;
        
        // Adicionar ponto inicial se lista estiver vazia
        if (this.pontos.isEmpty()) {
            this.pontos.add(new Ponto(x, y, 0));
        }
        
        iniciarMovimento();
    }
    
    /**
     * Construtor para plataforma DROPABLE
     */
    public PlataformaDinamica(int x, int y, int w, int h, 
                             int tempoParaCairSegundos, boolean visibleAfterFall) {
        super(x, y, w, h);
        this.xAtual = x;
        this.yAtual = y;
        this.xAnterior = x;
        this.yAnterior = y;
        this.deltaX = 0;
        this.deltaY = 0;
        this.xInicial = x;
        this.yInicial = y;
        this.tipo = TipoMovimento.DROPABLE;
        this.status = StatusMovimento.PARADA;
        this.tempoParaCairSegundos = tempoParaCairSegundos;
        this.visibleAfterFall = visibleAfterFall;
    }
    
    /**
     * Construtor para plataforma estatica (compatibilidade)
     */
    public PlataformaDinamica(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.xAtual = x;
        this.yAtual = y;
        this.xAnterior = x;
        this.yAnterior = y;
        this.deltaX = 0;
        this.deltaY = 0;
        this.tipo = TipoMovimento.ESTATICA;
        this.status = StatusMovimento.PARADA;
    }
    
    private void iniciarMovimento() {
        if (tipo == TipoMovimento.MOVABLE && !pontos.isEmpty()) {
            Ponto proximoPonto = pontos.get(pontoAtualIndex);
            xDestino = proximoPonto.x;
            yDestino = proximoPonto.y;
            
            // Se ja esta no ponto, ir para o proximo
            if (xAtual == xDestino && yAtual == yDestino) {
                proximoPonto();
            } else {
                status = StatusMovimento.MOVENDO;
            }
        }
    }
    
    private void proximoPonto() {
        if (tipo != TipoMovimento.MOVABLE) return;
        
        Ponto pontoAtual = pontos.get(pontoAtualIndex);
        
        // Se tem delay, aguardar
        if (pontoAtual.delaySegundos > 0) {
            status = StatusMovimento.AGUARDANDO;
            tempoInicioAguardo = System.currentTimeMillis();
            return;
        }
        
        // Ir para proximo ponto
        pontoAtualIndex++;
        
        // Verificar se chegou ao fim
        if (pontoAtualIndex >= pontos.size()) {
            if (looping) {
                pontoAtualIndex = 0; // Reiniciar do comeco
            } else {
                pontoAtualIndex = pontos.size() - 1; // Ficar no ultimo ponto
                status = StatusMovimento.PARADA;
                return;
            }
        }
        
        // Definir novo destino
        Ponto proximoPonto = pontos.get(pontoAtualIndex);
        xDestino = proximoPonto.x;
        yDestino = proximoPonto.y;
        status = StatusMovimento.MOVENDO;
    }
    
    @Override
    public void update() {
        // Salvar a posi��o anterior para calcular o delta
        xAnterior = xAtual;
        yAnterior = yAtual;
        
        switch (tipo) {
            case MOVABLE:
                updateMovable();
                break;
            case DROPABLE:
                updateDropable();
                break;
            case ESTATICA:
                // Nao faz nada
                break;
        }
        
        // Calcular o delta (mudan�a) na posi��o
        deltaX = xAtual - xAnterior;
        deltaY = yAtual - yAnterior;
    }
    
    private void updateMovable() {
        switch (status) {
            case MOVENDO:
                // Calcular direcao
                int dirX = xDestino - xAtual;
                int dirY = yDestino - yAtual;
                
                // Calcular distancia
                double distancia = Math.sqrt((double)dirX * dirX + (double)dirY * dirY);
                
                if (distancia <= velocidade) {
                    // Chegou ao destino
                    setPosition(xDestino, yDestino);
                    proximoPonto();
                } else {
                    // Mover na direcao do destino
                    double proporcao = velocidade / distancia;
                    int novoX = xAtual + (int)(dirX * proporcao);
                    int novoY = yAtual + (int)(dirY * proporcao);
                    setPosition(novoX, novoY);
                }
                break;
                
            case AGUARDANDO:
                // Verificar se o tempo de aguardo passou
                long tempoDecorrido = System.currentTimeMillis() - tempoInicioAguardo;
                Ponto pontoAtual = pontos.get(pontoAtualIndex);
                
                if (tempoDecorrido >= pontoAtual.delaySegundos * 1000) {
                    proximoPonto();
                }
                break;
                
            case PARADA:
            case CAINDO:
            case CAIDA_INVISIVEL:
            case CAIDA_VISIVEL:
                // Para garantir que deltaX e deltaY sejam 0 quando n�o houver movimento
                this.deltaX = 0;
                this.deltaY = 0;
                break;
        }
    }
    
    private void updateDropable() {
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
            case MOVENDO:
            case AGUARDANDO:
                // Garantir que n�o haja delta quando estiver em estados que n�o se movem
                if (status != StatusMovimento.MOVENDO) {
                    this.deltaX = 0;
                    this.deltaY = 0;
                }
                break;
        }
    }
    
    /**
     * M�todo para informar se player est� sobre a plataforma (para DROPABLE)
     */
    public void setPlayerSobre(boolean playerSobre) {
        this.playerSobre = playerSobre;
    }
    
    /**
     * Retorna a posi��o X atual da plataforma
     */
    @Override
    public int getX() {
        return xAtual;
    }
    
    /**
     * Retorna a posi��o Y atual da plataforma
     */
    @Override
    public int getY() {
        return yAtual;
    }
    
    /**
     * Define a posi��o da plataforma
     */
    private void setPosition(int x, int y) {
        this.xAtual = x;
        this.yAtual = y;
    }
    
    @Override
    public Rectangle getBounds() {
        if (tipo == TipoMovimento.ESTATICA) {
            return super.getBounds();
        } else {
            return new Rectangle(xAtual, yAtual, width, height);
        }
    }
    
    @Override
    public void draw(Graphics g) {
        // Nao desenhar se esta invisivel apos queda
        if (status == StatusMovimento.CAIDA_INVISIVEL) {
            return;
        }
        
        // Desenhar diferente baseado no tipo
        Color corOriginal = g.getColor();
        Color corPreenchimento = getColor(); // Usar a cor definida na plataforma
        Color corBorda = getBorderColor();   // Usar a cor de borda definida
        
        // Alterar cores com base no estado da plataforma
        switch (tipo) {
            case MOVABLE:
                if (status == StatusMovimento.MOVENDO) {
                    corPreenchimento = Color.BLUE; // Azul quando movendo
                } else if (status == StatusMovimento.AGUARDANDO) {
                    corPreenchimento = Color.CYAN; // Ciano quando aguardando
                }
                break;
                
            case DROPABLE:
                if (status == StatusMovimento.CAINDO) {
                    corPreenchimento = Color.RED; // Vermelho quando caindo
                } else if (playerSobre && tempoInicioContato > 0) {
                    // Piscar amarelo quando player esta sobre e contando tempo
                    long tempoDecorrido = System.currentTimeMillis() - tempoInicioContato;
                    if ((tempoDecorrido / 200) % 2 == 0) { // Piscar a cada 200ms
                        corPreenchimento = Color.YELLOW;
                    } else {
                        corPreenchimento = Color.ORANGE;
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
        g.setColor(corPreenchimento);
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(corBorda);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        // Restaurar a cor original
        g.setColor(corOriginal);
    }
    
    /**
     * Retorna o tipo de movimento da plataforma
     */
    public TipoMovimento getTipo() { 
        return tipo; 
    }
    
    /**
     * Retorna o status atual da plataforma
     */
    public StatusMovimento getStatus() { 
        return status; 
    }
    
    /**
     * Verifica se a plataforma est� vis�vel
     */
    public boolean isVisivel() { 
        return status != StatusMovimento.CAIDA_INVISIVEL; 
    }
    
    /**
     * Verifica se a plataforma est� se movendo
     */
    public boolean estaMovendo() {
        return status == StatusMovimento.MOVENDO;
    }
    
    /**
     * Retorna a varia��o de posi��o X desde a �ltima atualiza��o
     */
    public int getDeltaX() {
        return deltaX;
    }
    
    /**
     * Retorna a varia��o de posi��o Y desde a �ltima atualiza��o
     */
    public int getDeltaY() {
        return deltaY;
    }
    
    /**
     * Ativa o movimento da plataforma (�til para elevadores)
     */
    public void ativar() {
        if (tipo == TipoMovimento.MOVABLE && status == StatusMovimento.PARADA) {
            iniciarMovimento();
        }
    }
    
    /**
     * Move a plataforma para um ponto espec�fico (controle manual do elevador)
     */
    public void irParaPonto(int indexPonto) {
        if (tipo == TipoMovimento.MOVABLE && pontos.size() > indexPonto && indexPonto >= 0) {
            pontoAtualIndex = indexPonto;
            Ponto destino = pontos.get(indexPonto);
            xDestino = destino.x;
            yDestino = destino.y;
            
            if (status == StatusMovimento.PARADA || status == StatusMovimento.AGUARDANDO) {
                status = StatusMovimento.MOVENDO;
            }
        }
    }
    
    /**
     * Reseta uma plataforma DROPABLE para sua posi��o inicial
     */
    public void resetar() {
        if (tipo == TipoMovimento.DROPABLE) {
            xAtual = xInicial;
            yAtual = yInicial;
            status = StatusMovimento.PARADA;
            tempoInicioContato = 0;
            playerSobre = false;
        }
    }
}
