
package br.com.jumpman;

import javax.swing.*;
// Imports originais comentados temporariamente para teste
// import br.com.jumpman.screens.MainFrame;
// import br.com.jumpman.screens.ScreenInitial;
import br.com.jumpman.screens.ScreenTestStage; // Para m�todo comentado
import br.com.jumpman.screens.InteractTestStage;

public class App {
    public static void main(String[] args) {
        // TEMPORARIO: Teste do sistema de intera��o
        SwingUtilities.invokeLater(App::showInteractTest);
        
        // TEMPORARIO ANTERIOR: Teste do sistema de clima
        // SwingUtilities.invokeLater(App::showTestStage);
        
        // ORIGINAL: Descomentar estas linhas para voltar ao jogo normal
        // SwingUtilities.invokeLater(() -> {
        //     try {
        //         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //     }
        //     new MainFrame().setVisible(true);
        // });
    }

    // TEMPORARIO: Metodo para teste de intera��o
    private static void showInteractTest() {
        JFrame frame = new JFrame("Jumpman - Teste de Intera��o");
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        InteractTestStage testStage = new InteractTestStage();
        frame.setContentPane(testStage);
        frame.setVisible(true);
    }

    // TEMPORARIO: Metodo para teste do clima
    private static void showTestStage() {
        JFrame frame = new JFrame("Jumpman - Teste de Clima");
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        ScreenTestStage testStage = new ScreenTestStage();
        frame.setContentPane(testStage);
        frame.setVisible(true);
    }
}

