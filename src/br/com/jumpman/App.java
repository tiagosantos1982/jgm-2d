/**
 * Jumpman - Jogo de plataforma 2D com efeitos climaticos e interacoes
 * 
 * IMPORTANTE: Consulte o arquivo INSTRUCTIONS.md na raiz do projeto
 * para obter instrucoes detalhadas sobre compilacao, execucao e desenvolvimento.
 * 
 * Versao: 1.2.0
 * Ultima atualizacao: Setembro 2025
 */

package br.com.jumpman;

import br.com.jumpman.core.GameLauncher;
import br.com.jumpman.data.enums.GameLauncherMode;

/**
 * Classe principal que inicia a aplicacao
 */
public class App {
    
    /**
     * Metodo principal do jogo
     * @param args Argumentos da linha de comando
     */
    public static void main(String[] args) {
        // Exibir mensagem sobre instrucoes ao iniciar
        System.out.println("==============================================");
        System.out.println("  JUMPMAN - Jogo de Plataforma 2D - v1.2.0   ");
        System.out.println("==============================================");
        System.out.println("IMPORTANTE: Consulte o arquivo INSTRUCTIONS.md");
        System.out.println("para instrucoes de execucao e desenvolvimento.");
        System.out.println("==============================================\n");
        
        // TEMPORARIO: Teste de cores em plataformas
        GameLauncher.launch(GameLauncherMode.WALTER_FLOODFILL_TEST);
        
        // TEMPORARIO ANTERIOR: Teste do sistema de interacao
        // GameLauncher.launch(LaunchMode.INTERACTION_TEST);
        
        // TEMPORARIO ANTERIOR: Teste do sistema de clima
        // GameLauncher.launch(LaunchMode.WEATHER_TEST);
        
        // ORIGINAL: Descomentar esta linha para voltar ao jogo normal
        // GameLauncher.launch(LaunchMode.NORMAL);
    }
}