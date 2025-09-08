package br.com.jumpman.platforms.movement;

import java.util.ArrayList;
import java.util.List;
import br.com.jumpman.data.enums.StatusMovimento;
import br.com.jumpman.platforms.PlataformaDinamica;
import br.com.jumpman.platforms.AbstractPlatform;

/**
 * Estrategia para plataformas moveis (que seguem pontos)
 */
public class MovingPlatformStrategy extends MovementStrategy {
    
    private List<PlataformaDinamica.Ponto> pontos;
    private int pontoAtualIndex;
    private boolean looping;
    private float velocidade; // pixels por frame
    
    // Para movimento suave
    private int xDestino, yDestino; // Destino atual
    private long tempoInicioAguardo = 0;
    
    /**
     * Cria uma nova estrategia para plataforma movel
     * 
     * @param plataforma Plataforma a qual esta estrategia pertence
     * @param pontos Lista de pontos por onde a plataforma deve passar
     * @param looping Se verdadeiro, a plataforma continua o movimento em loop
     * @param velocidade Velocidade de movimento em pixels por frame
     */
    public MovingPlatformStrategy(PlataformaDinamica plataforma, 
                                 List<PlataformaDinamica.Ponto> pontos,
                                 boolean looping, float velocidade) {
        super(plataforma);
        this.pontos = new ArrayList<>(pontos);
        this.pontoAtualIndex = 0;
        this.looping = looping;
        this.velocidade = velocidade;
        
        // Adicionar ponto inicial se lista estiver vazia
        if (this.pontos.isEmpty()) {
            this.pontos.add(new PlataformaDinamica.Ponto(xAtual, yAtual, 0));
        }
        
        iniciarMovimento();
    }
    
    @Override
    public void iniciarMovimento() {
        if (!pontos.isEmpty()) {
            PlataformaDinamica.Ponto proximoPonto = pontos.get(pontoAtualIndex);
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
        PlataformaDinamica.Ponto pontoAtual = pontos.get(pontoAtualIndex);
        
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
        PlataformaDinamica.Ponto proximoPonto = pontos.get(pontoAtualIndex);
        xDestino = proximoPonto.x;
        yDestino = proximoPonto.y;
        status = StatusMovimento.MOVENDO;
    }
    
    @Override
    public boolean update() {
        int xAnterior = xAtual;
        int yAnterior = yAtual;
        
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
                PlataformaDinamica.Ponto pontoAtual = pontos.get(pontoAtualIndex);
                
                if (tempoDecorrido >= pontoAtual.delaySegundos * 1000) {
                    proximoPonto();
                }
                break;
                
            case PARADA:
            default:
                // Nao faz nada
                return false;
        }
        
        // Retornar true se houve movimento
        return (xAtual != xAnterior || yAtual != yAnterior);
    }
    
    /**
     * Move a plataforma para um ponto especifico da lista
     * 
     * @param indexPonto Indice do ponto na lista
     * @return true se o indice for valido, false caso contrario
     */
    public boolean irParaPonto(int indexPonto) {
        if (pontos.size() > indexPonto && indexPonto >= 0) {
            pontoAtualIndex = indexPonto;
            PlataformaDinamica.Ponto destino = pontos.get(indexPonto);
            xDestino = destino.x;
            yDestino = destino.y;
            
            if (status == StatusMovimento.PARADA || status == StatusMovimento.AGUARDANDO) {
                status = StatusMovimento.MOVENDO;
            }
            return true;
        }
        return false;
    }
}