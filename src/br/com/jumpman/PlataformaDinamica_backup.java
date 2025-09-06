package br.com.jumpman;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlataformaDinamica extends PlataformaSimples {
    
    // Enums para tipos de comportamento
    public enum TipoMovimento {
        ESTATICA,    // Plataforma normal sem movimento
        MOVABLE,     // Se move
        DROPABLE     // Cai quando player fica sobre ela
    };
    
    public enum StatusMovimento {
        PARADA,           // Parada no ponto atual
        MOVENDO,          // Se movimentando para pr�ximo ponto
        AGUARDANDO,       // Esperando delay antes de continuar
        CAINDO,           // Caindo (para DROPABLE)
        CAIDA_INVISIVEL,  // Caiu e est� invis�vel
        CAIDA_VISIVEL     // Caiu e est� vis�vel
    }
    
    // Classe interna para representar pontos de movimento
    public static class Ponto {
        public int x, y;
        public int delaySegundos; // Tempo para aguardar neste ponto antes de ir ao pr�ximo
        
        public Ponto(int x, int y, int delaySegundos) {
            this.x = x;
            this.y = y;
            this.delaySegundos = delaySegundos;
        }
        
        public Ponto(int x, int y) {
            this(x, y, 0); // Sem delay por padr�o
        }
    }
    
    // Propriedades da plataforma din�mica
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
    private int xInicial, yInicial; // Posi��o inicial da plataforma
    private int xDestino, yDestino; // Destino atual
    private long tempoInicioAguardo = 0;
    
    // Construtor para plataforma MOVABLE
    public PlataformaDinamica(int x, int y, int w, int h, 
                             List<Ponto> pontos, boolean looping, float velocidade) {
        super(x, y, w, h);
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
    
    // Construtor para plataforma DROPABLE
    public PlataformaDinamica(int x, int y, int w, int h, 
                             int tempoParaCairSegundos, boolean visibleAfterFall) {
        super(x, y, w, h);
        this.tipo = TipoMovimento.DROPABLE;
        this.status = StatusMovimento.PARADA;
        this.tempoParaCairSegundos = tempoParaCairSegundos;
        this.visibleAfterFall = visibleAfterFall;
        this.xInicial = x;
        this.yInicial = y;
    }
    
    // Construtor para plataforma est�tica (compatibilidade)
    public PlataformaDinamica(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.tipo = TipoMovimento.ESTATICA;
        this.status = StatusMovimento.PARADA;
    }
    
    private void iniciarMovimento() {
        if (tipo == TipoMovimento.MOVABLE && !pontos.isEmpty()) {
            Ponto proximoPonto = pontos.get(pontoAtualIndex);
            xDestino = proximoPonto.x;
            yDestino = proximoPonto.y;
            
            // Se j� est� no ponto, ir para o pr�ximo
            if (getX() == xDestino && getY() == yDestino) {
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
        
        // Ir para pr�ximo ponto
        pontoAtualIndex++;
        
        // Verificar se chegou ao fim
        if (pontoAtualIndex >= pontos.size()) {
            if (looping) {
                pontoAtualIndex = 0; // Reiniciar do come�o
            } else {
                pontoAtualIndex = pontos.size() - 1; // Ficar no �ltimo ponto
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
    
    public void update() {
        switch (tipo) {
            case MOVABLE:
                updateMovable();
                break;
            case DROPABLE:
                updateDropable();
                break;
            case ESTATICA:
                // N�o faz nada
                break;
        }
    }
    
    private void updateMovable() {
        switch (status) {
            case MOVENDO:
                // Calcular dire��o
                int deltaX = xDestino - getX();
                int deltaY = yDestino - getY();
                
                // Calcular dist�ncia
                double distancia = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                
                if (distancia <= velocidade) {
                    // Chegou ao destino
                    setPosition(xDestino, yDestino);
                    proximoPonto();
                } else {
                    // Mover na dire��o do destino
                    double proporcao = velocidade / distancia;
                    int novoX = getX() + (int)(deltaX * proporcao);
                    int novoY = getY() + (int)(deltaY * proporcao);
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
                // N�o faz nada para estes estados em MOVABLE
                break;
        }
    }
    
    private void updateDropable() {
        switch (status) {
            case PARADA:
                // Verificar se player est� sobre a plataforma
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
                setPosition(getX(), getY() + (int)velocidade);
                
                // Verificar se saiu da tela (assumindo altura da tela ~600)
                if (getY() > 600) {
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
                // N�o faz nada, plataforma caiu ou estados n�o aplic�veis
                break;
        }
    }
    
    // M�todo para informar se player est� sobre a plataforma (para DROPABLE)
    public void setPlayerSobre(boolean playerSobre) {
        this.playerSobre = playerSobre;
    }
    
    // Getters para posi��o (necess�rios para movimento)
    public int getX() {
        return getBounds().x;
    }
    
    public int getY() {
        return getBounds().y;
    }
    
    // Setter para atualizar posi��o
    private void setPosition(int x, int y) {
        // Como PlataformaSimples n�o tem setters, precisamos sobrescrever getBounds
        this.xAtual = x;
        this.yAtual = y;
    }
    
    
    private int xAtual;
    private int yAtual;
    
    @Override
    public Rectangle getBounds() {
        if (tipo == TipoMovimento.ESTATICA) {
            return super.getBounds();
        } else {
            return new Rectangle(xAtual, yAtual, super.getBounds().width, super.getBounds().height);
        }
    }
    
    @Override
    public void draw(Graphics g) {
        // N�o desenhar se est� invis�vel ap�s queda
        if (status == StatusMovimento.CAIDA_INVISIVEL) {
            return;
        }
        
        // Desenhar diferente baseado no tipo
        Color corOriginal = g.getColor();
        
        switch (tipo) {
            case MOVABLE:
                if (status == StatusMovimento.MOVENDO) {
                    g.setColor(Color.BLUE); // Azul quando movendo
                } else if (status == StatusMovimento.AGUARDANDO) {
                    g.setColor(Color.CYAN); // Ciano quando aguardando
                } else {
                    g.setColor(Color.DARK_GRAY); // Cinza quando parada
                }
                break;
                
            case DROPABLE:
                if (status == StatusMovimento.CAINDO) {
                    g.setColor(Color.RED); // Vermelho quando caindo
                } else if (playerSobre && tempoInicioContato > 0) {
                    // Piscar amarelo quando player est� sobre e contando tempo
                    long tempoDecorrido = System.currentTimeMillis() - tempoInicioContato;
                    if ((tempoDecorrido / 200) % 2 == 0) { // Piscar a cada 200ms
                        g.setColor(Color.YELLOW);
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                } else {
                    g.setColor(Color.GRAY); // Cinza normal
                }
                break;
                
            case ESTATICA:
            default:
                g.setColor(Color.DARK_GRAY);
                break;
        }
        
        Rectangle bounds = getBounds();
        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(Color.BLACK);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        
        g.setColor(corOriginal);
    }
    
    // Getters para estado
    public TipoMovimento getTipo() { return tipo; }
    public StatusMovimento getStatus() { return status; }
    public boolean isVisivel() { return status != StatusMovimento.CAIDA_INVISIVEL; }
    
    // Metodo para ativar movimento (util para elevadores)
    public void ativar() {
        if (tipo == TipoMovimento.MOVABLE && status == StatusMovimento.PARADA) {
            iniciarMovimento();
        }
    }
    
    // Metodo para ir para ponto especifico (controle manual do elevador)
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
    
    // Metodo para resetar plataforma DROPABLE
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
