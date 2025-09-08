package br.com.jumpman.core;

import javax.swing.*;

import br.com.jumpman.data.enums.GameLauncherMode;
import br.com.jumpman.screens.MainFrame;
import br.com.jumpman.screens.ScreenFloodStage;
import br.com.jumpman.screens.ScreenTestStage;

/**
 * Classe responsavel pelo lancamento do jogo nas diferentes modalidades.
 * Esta classe segue o padrao Singleton e fornece metodos para iniciar o jogo
 * em diferentes modos: normal, teste de interacao ou teste de clima.
 * 
 * <p>Exemplo de uso:
 * <pre>
 *     GameLauncher.launch(LaunchMode.NORMAL);
 * </pre>
 * 
 * @author Tiago Santos
 * @version 1.2.0
 * @since Setembro 2025
 */
public class GameLauncher {
    

    /**
     * Construtor privado para evitar instanciacao
     */
    private GameLauncher() 
    {
        // Classe utilitaria, nao deve ser instanciada
    }
    
    /**
     * Inicia o jogo no modo especificado
     * 
     * @param mode Modo de inicializacao do jogo
     */
    public static void launch(GameLauncherMode mode) 
    {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            switch (mode) {
                case NORMAL:
                    launchNormalGame();
                    break;
                case INTERACTION_TEST:
                    launchInteractionTest();
                    break;
                case WEATHER_TEST:
                    launchWeatherTest();
                    break;
                case PLATFORM_COLOR_TEST:
                    launchPlatformColorTest();
                    break;
                case WALTER_FLOODFILL_TEST:
                    launchFloodFillTestGame();
                    break;
            }
        });
    }
    
    /**
     * Inicia o jogo no modo normal
     */
    private static void launchNormalGame() {
        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
    
    /**
     * Inicia o teste do sistema de interacao
     */
    private static void launchInteractionTest() {
        // Exibir instrucoes rapidas em uma janela
        if (GameConfig.getBooleanProperty("game.show_instructions", true)) {
            JOptionPane.showMessageDialog(null, """
                INSTRUCOES RAPIDAS:

                • Use as setas ↝ → para mover o personagem
                • Use a tecla ESPACO para pular
                • Use a tecla E para interagir com objetos

                Objetivos:
                1. Colete a chave azul para ativar o elevador
                2. Use o elevador para acessar plataformas mais altas
                3. Colete a chave amarela para abrir a porta

                Para instrucoes completas, consulte INSTRUCTIONS.md""",
                "Jumpman - Instrucoes", JOptionPane.INFORMATION_MESSAGE);
        }
        
        // Iniciar o jogo
        JFrame frame = new JFrame("Jumpman - Teste de Interacao");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        ScreenFloodStage testStage = new ScreenFloodStage();
        frame.setContentPane(testStage);
        frame.setVisible(true);
    }
    
    /**
     * Inicia o teste do sistema de clima
     */
    private static void launchWeatherTest() {
        JFrame frame = new JFrame("Jumpman - Teste de Clima");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        ScreenTestStage testStage = new ScreenTestStage();
        frame.setContentPane(testStage);
        frame.setVisible(true);
    }
    
    /**
     * Inicia o teste de cores em plataformas
     */
    private static void launchPlatformColorTest() {
        JFrame frame = new JFrame("Jumpman - Teste de Cores em Plataformas");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new br.com.jumpman.screens.PlatformColorTestScreen());
        frame.setVisible(true);
    }

    private static void launchFloodFillTestGame() {
        JFrame frame = new JFrame("Jumpman - Teste de Inundacao por tempo.");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        ScreenFloodStage testStage = new ScreenFloodStage();
        frame.setContentPane(testStage);
        frame.setVisible(true);
    }
}