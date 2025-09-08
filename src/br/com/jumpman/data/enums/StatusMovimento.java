package br.com.jumpman.data.enums;

/**
 * Estados poss�veis para plataformas din�micas
 */
public enum StatusMovimento {
    PARADA,           // Parada no ponto atual
    MOVENDO,          // Se movimentando para proximo ponto
    AGUARDANDO,       // Esperando delay antes de continuar
    CAINDO,           // Caindo (para DROPABLE)
    CAIDA_INVISIVEL,  // Caiu e esta invisivel
    CAIDA_VISIVEL     // Caiu e esta visivel
}
