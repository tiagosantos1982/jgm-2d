package br.com.jumpman.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Classe utilit�ria para carregar recursos (imagens, �udio, arquivos de texto, etc.)
 */
public class ResourceLoader {
    
    private static final String BASE_PATH = "resources/";
    
    /**
     * Carrega uma imagem a partir do caminho especificado
     * Primeiro tenta carregar do sistema de arquivos, depois dos recursos
     * 
     * @param path Caminho para o arquivo de imagem (pode ser relativo ou absoluto)
     * @return Objeto BufferedImage ou null se o carregamento falhar
     */
    public static BufferedImage loadImage(String path) {
        BufferedImage image = null;
        
        // Normalizar o caminho se ele n�o for absoluto
        String normalizedPath = normalizePath(path);
        
        // Primeiro tenta carregar do sistema de arquivos
        try {
            File file = new File(normalizedPath);
            if (file.exists()) {
                image = ImageIO.read(file);
                return image;
            }
        } catch (IOException e) {
            // Ignorar e tentar recursos
        }
        
        // Tentar carregar dos recursos
        try {
            // Tentar como est�
            InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                // Tentar remover "resources/" se j� estiver no caminho
                String resourcePath = path.startsWith("resources/") ? path : "resources/" + path;
                is = ResourceLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            }
            
            if (is != null) {
                image = ImageIO.read(is);
                return image;
            }
        } catch (IOException e) {
            System.err.println("Falha ao carregar imagem dos recursos: " + path);
        }
        
        // Se tudo falhar, procure na pasta de recursos usando caminhos alternativos
        try {
            String alternativePath = path;
            if (!path.startsWith(BASE_PATH) && !path.contains(":/") && !path.startsWith("/")) {
                alternativePath = BASE_PATH + path;
            }
            
            File file = new File(alternativePath);
            if (file.exists()) {
                image = ImageIO.read(file);
                return image;
            }
        } catch (IOException e) {
            System.err.println("Falha ao carregar imagem usando caminhos alternativos: " + path);
        }
        
        return null;
    }
    
    /**
     * Carrega um arquivo de texto e retorna seu conte�do como uma lista de strings
     * 
     * @param path Caminho para o arquivo de texto
     * @return Lista de strings, cada uma representando uma linha do arquivo, ou lista vazia se falhar
     */
    public static List<String> loadTextFile(String path) {
        List<String> lines = new ArrayList<>();
        
        // Normalizar o caminho se ele n�o for absoluto
        String normalizedPath = normalizePath(path);
        
        // Tentar ler do sistema de arquivos
        try {
            File file = new File(normalizedPath);
            if (file.exists()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        java.nio.file.Files.newInputStream(file.toPath()), "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
                return lines;
            }
        } catch (IOException e) {
            // Ignorar e tentar recursos
        }
        
        // Tentar ler dos recursos
        try {
            // Tentar como est�
            InputStream is = ResourceLoader.class.getClassLoader().getResourceAsStream(path);
            if (is == null) {
                // Tentar remover "resources/" se j� estiver no caminho
                String resourcePath = path.startsWith("resources/") ? path : "resources/" + path;
                is = ResourceLoader.class.getClassLoader().getResourceAsStream(resourcePath);
            }
            
            if (is != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Falha ao carregar arquivo de texto dos recursos: " + path);
        }
        
        return lines;
    }
    
    /**
     * Normaliza o caminho do arquivo, resolvendo caminhos relativos
     * 
     * @param path Caminho a ser normalizado
     * @return Caminho normalizado
     */
    private static String normalizePath(String path) {
        // Se for caminho absoluto, retornar como est�
        if (path.contains(":/") || path.startsWith("/")) {
            return path;
        }
        
        // Remover "resources/" se estiver duplicado
        if (path.startsWith("resources/") && BASE_PATH.equals("resources/")) {
            return path;
        }
        
        // Adicionar o caminho base se necess�rio
        return BASE_PATH + path.replace("resources/", "");
    }
}
