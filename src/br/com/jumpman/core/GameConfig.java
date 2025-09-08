package br.com.jumpman.core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Classe para gerenciar as configura��es do jogo
 */
public class GameConfig {
    private static final String CONFIG_FILE = "config.properties";
    private static Properties properties;
    
    static {
        properties = new Properties();
        loadConfig();
    }
    
    /**
     * Construtor privado para evitar instancia��o
     */
    private GameConfig() {
        // Classe utilit�ria, n�o deve ser instanciada
    }
    
    /**
     * Carrega as configura��es do arquivo
     */
    private static void loadConfig() {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            properties.load(fis);
        } catch (IOException e) {
            // Arquivo n�o encontrado ou erro de leitura
            // Criar configura��es padr�o
            setDefaultConfig();
        }
    }
    
    /**
     * Define configura��es padr�o
     */
    private static void setDefaultConfig() {
        properties.setProperty("game.show_instructions", "true");
        properties.setProperty("game.screen.width", "800");
        properties.setProperty("game.screen.height", "600");
        properties.setProperty("game.debug_mode", "false");
        
        saveConfig();
    }
    
    /**
     * Salva as configura��es atuais no arquivo
     */
    public static void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "Jumpman Game Configuration");
        } catch (IOException e) {
            System.err.println("Erro ao salvar configura��es: " + e.getMessage());
        }
    }
    
    /**
     * Obt�m uma propriedade de configura��o
     * 
     * @param key Chave da propriedade
     * @param defaultValue Valor padr�o caso a propriedade n�o exista
     * @return Valor da propriedade
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    /**
     * Define uma propriedade de configura��o
     * 
     * @param key Chave da propriedade
     * @param value Valor da propriedade
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Obt�m uma propriedade de configura��o como booleano
     * 
     * @param key Chave da propriedade
     * @param defaultValue Valor padr�o caso a propriedade n�o exista
     * @return Valor da propriedade como booleano
     */
    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Obt�m uma propriedade de configura��o como inteiro
     * 
     * @param key Chave da propriedade
     * @param defaultValue Valor padr�o caso a propriedade n�o exista
     * @return Valor da propriedade como inteiro
     */
    public static int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
