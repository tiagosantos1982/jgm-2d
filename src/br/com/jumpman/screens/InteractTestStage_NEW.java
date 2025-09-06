package br.com.jumpman.screens;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import br.com.jumpman.Player;
import br.com.jumpman.PlataformaSimples;
import br.com.jumpman.PlataformaDinamica;

public class InteractTestStage extends JPanel {
    private Player player;
    private boolean left = false, right = false;
    private PlataformaSimples[] platforms;
    private PlataformaSimples ground;
    private Key[] keys; // Array de chaves coloridas
    private Door door; // Porta azul
    private PlataformaDinamica elevatorPlatform; // Plataforma din�mica do elevador
    private Elevator elevator; // Elevador
    private Inventory inventory;
    
    public InteractTestStage() {
        setBackground(Color.BLACK);
        setFocusable(true);
        
        // Inicializar player
        player = new Player(100, 450, 20, 30);
        
        // Ch�o completo
        ground = new PlataformaSimples(0, 550, 800, 50);
        
        // 5 plataformas (removida a plataforma do elevador)
        platforms = new PlataformaSimples[] {
            new PlataformaSimples(270, 450, 80, 20),  // Alcan��vel pulando
            new PlataformaSimples(400, 350, 120, 20),  // Serve de apoio
            new PlataformaSimples(600, 250, 100, 20),  // Plataforma antiga
            new PlataformaSimples(350, 100, 150, 20)   // Plataforma da porta (porta ficar� ACIMA)
        };
        
        // 3 chaves coloridas espalhadas
        keys = new Key[] {
            new Key(335, 430, "AZUL"),     // Chave azul na plataforma 1
            new Key(630, 230, "VERDE"),    // Chave verde na plataforma 3
            new Key(430, 330, "AMARELA")   // Chave amarela na plataforma 2
        };
        
        // Porta azul ACIMA da plataforma (n�o penetrando)
        door = new Door(380, 50);
        
        // Elevador din�mico (plataforma m�vel) - movimento suave
        ArrayList<PlataformaDinamica.Ponto> pontosElevador = new ArrayList<>();
        pontosElevador.add(new PlataformaDinamica.Ponto(150, 450, 1)); // Ponto inicial (embaixo) com delay de 1s
        pontosElevador.add(new PlataformaDinamica.Ponto(150, 85, 2));  // Ponto final (em cima) com delay de 2s
        
        elevatorPlatform = new PlataformaDinamica(150, 450, 80, 15, pontosElevador, false, 3.0f);
        elevator = new Elevator(15, 300); //base do elevador, ponto de chegada.
        
        // Invent�rio com 3 slots
        inventory = new Inventory();
        
        // Usar KeyBindings ao inv�s de KeyListener para garantir funcionamento
        setupKeyBindings();
        
        Timer timer = new Timer(16, e -> {
            updatePlayer();
            elevatorPlatform.update(); // Atualizar movimento da plataforma din�mica
            repaint();
        });
        timer.start();
    }
    
    private void setupKeyBindings() {
        // Mapear teclas de movimento
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("SPACE"), "jump");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("E"), "interact");
        
        // Mapear a��es de movimento
        getActionMap().put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                left = true;
            }
        });
        
        getActionMap().put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                right = true;
            }
        });
        
        getActionMap().put("jump", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.jump();
            }
        });
        
        getActionMap().put("interact", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tryInteract();
            }
        });
        
        // Mapear libera��o de teclas
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released LEFT"), "stopLeft");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("released RIGHT"), "stopRight");
        
        getActionMap().put("stopLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                left = false;
            }
        });
        
        getActionMap().put("stopRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                right = false;
            }
        });
    }
    
    private void updatePlayer() {
        player.update();
        
        // Movimento horizontal
        if (left) player.moveLeft();
        if (right) player.moveRight();
        if (!left && !right) player.stop();
        
        Rectangle playerBounds = player.getBounds();
        
        // Colis�o com o ch�o
        Rectangle groundBounds = ground.getBounds();
        if (playerBounds.intersects(groundBounds)) {
            if (playerBounds.y < groundBounds.y) {
                // Player caindo no ch�o
                player.setY(groundBounds.y - playerBounds.height);
                player.stopFalling();
            }
        }
        
        // Colis�o com plataformas fixas
        for (PlataformaSimples platform : platforms) {
            Rectangle platformBounds = platform.getBounds();
            if (playerBounds.intersects(platformBounds) && player.getVy() > 0) {
                if (playerBounds.y < platformBounds.y) {
                    player.landOn(platformBounds.y);
                }
            }
        }
        
        // Colis�o com plataforma m�vel do elevador        
        Rectangle elevatorPlatformBounds = elevatorPlatform.getBounds();
        if (playerBounds.intersects(elevatorPlatformBounds) && player.getVy() > 0) {
            if (playerBounds.y < elevatorPlatformBounds.y) {
                player.landOn(elevatorPlatformBounds.y);
            }
        }
    }
    
    private void tryInteract() {
        Rectangle playerBounds = player.getBounds();
        Rectangle interactionArea = new Rectangle(
            playerBounds.x - 10, playerBounds.y - 10, 
            playerBounds.width + 20, playerBounds.height + 20
        );
        
        // Verificar intera��o com as chaves
        for (Key key : keys) {
            if (key.isVisible() && interactionArea.intersects(key.getBounds())) {
                if (inventory.addItem(key.getColor())) {
                    key.collect();
                    System.out.println("Chave " + key.getColor() + " coletada!");
                } else {
                    System.out.println("Invent�rio cheio!");
                }
                return;
            }
        }
        
        // Verificar intera��o com elevador (caixa de controle)
        if (interactionArea.intersects(elevator.getControlBoxBounds())) {
            if (!elevator.isActivated()) {
                // Elevador ainda n�o foi ativado - precisa da chave azul
                if (inventory.hasItem("AZUL")) {
                    elevator.activate();
                    elevatorPlatform.ativar(); // Ativar movimento da plataforma din�mica
                    inventory.removeItem("AZUL");
                    System.out.println("Elevador ativado com chave azul!");
                } else {
                    System.out.println("Precisa da chave azul para ativar o elevador!");
                }
            } else {
                // Elevador j� ativado - informar que pode usar a plataforma
                System.out.println("Elevador ativo - use a plataforma m�vel!");
            }
            return;
        }
        
        // Verificar intera��o com porta
        if (interactionArea.intersects(door.getBounds())) {
            if (!door.isOpen()) {
                if (inventory.hasItem("AMARELA")) {
                    door.open();
                    inventory.removeItem("AMARELA");
                    System.out.println("Porta aberta com chave amarela!");
                } else {
                    System.out.println("Precisa da chave amarela para abrir a porta!");
                }
            } else {
                System.out.println("Porta j� est� aberta!");
            }
            return;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        // Desenhar ch�o
        ground.draw(g2);
        
        // Desenhar plataformas
        for (PlataformaSimples platform : platforms) {
            platform.draw(g2);
        }
        
        // Desenhar chaves
        for (Key key : keys) {
            if (key.isVisible()) {
                key.draw(g2);
            }
        }
        
        // Desenhar elevador (plataforma din�mica + caixa de controle)
        elevatorPlatform.draw(g2);
        elevator.draw(g2);
        
        // Desenhar porta
        door.draw(g2);
        
        // Desenhar player
        player.draw(g2, true);
        
        // Desenhar UI
        g2.setColor(Color.WHITE);
        g2.drawString("Teste de Intera��o - Use setas para mover, ESPA�O para pular", 20, 30);
        g2.drawString("Pressione E para interagir com objetos", 20, 50);
        g2.drawString("Player: (" + player.getX() + ", " + player.getY() + ")", 20, 70);
        
        // Status das chaves
        StringBuilder keysStatus = new StringBuilder("Chaves: ");
        for (Key key : keys) {
            keysStatus.append(key.getColor()).append("(").append(key.isVisible() ? "?" : "X").append(") ");
        }
        g2.drawString(keysStatus.toString(), 20, 90);
        
        // Status dos objetos
        String elevatorStatus = "Elevador: ";
        if (!elevator.isActivated()) {
            elevatorStatus += "INATIVO - precisa chave azul";
        } else {
            elevatorStatus += "ATIVO - Status: " + elevatorPlatform.getStatus();
        }
        g2.drawString(elevatorStatus, 20, 110);
        g2.drawString("Porta: " + (door.isOpen() ? "ABERTA" : "fechada"), 20, 130);
        
        // Desenhar invent�rio
        inventory.draw(g2, 20, 150);
    }
    
    // Classe interna para as chaves
    private static class Key {
        private int x, y;
        private String color;
        private boolean visible = true;
        
        public Key(int x, int y, String color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
        
        public void draw(Graphics2D g) {
            Color keyColor;
            switch (color) {
                case "AZUL": keyColor = Color.BLUE; break;
                case "VERDE": keyColor = Color.GREEN; break;
                case "AMARELA": keyColor = Color.YELLOW; break;
                default: keyColor = Color.WHITE; break;
            }
            
            g.setColor(keyColor);
            g.fillOval(x, y, 15, 15);
            g.setColor(Color.BLACK);
            g.drawOval(x, y, 15, 15);
            
            // Desenhar texto da cor
            g.setColor(Color.WHITE);
            g.drawString(color.substring(0, 1), x + 4, y + 25);
        }
        
        public Rectangle getBounds() {
            return new Rectangle(x, y, 15, 15);
        }
        
        public void collect() { this.visible = false; }
        public boolean isVisible() { return visible; }
        public String getColor() { return color; }
    }
    
    // Classe interna para a porta
    private static class Door {
        private int x, y;
        private boolean open = false;
        
        public Door(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public void draw(Graphics2D g) {
            // Porta azul
            g.setColor(Color.BLUE);
            g.fillRect(x, y, 30, 50);
            g.setColor(Color.BLUE.darker());
            g.drawRect(x, y, 30, 50);
            
            if (!open) {
                // Ma�aneta amarela
                g.setColor(Color.YELLOW);
                g.fillOval(x + 22, y + 25, 6, 6);
                g.setColor(Color.ORANGE);
                g.drawOval(x + 22, y + 25, 6, 6);
            } else {
                // Porta aberta (desenhar de lado)
                g.setColor(Color.BLUE.brighter());
                g.fillRect(x + 25, y, 5, 50);
                g.setColor(Color.GREEN);
                g.drawString("ABERTA", x - 10, y - 5);
            }
        }
        
        public Rectangle getBounds() {
            return new Rectangle(x, y, 30, 50);
        }
        
        public void open() { this.open = true; }
        public boolean isOpen() { return open; }
    }
    
    // Classe interna para o elevador - APENAS CAIXA DE CONTROLE
    private static class Elevator {
        private int x;
        private boolean activated = false;
        
        public Elevator(int x, int baseY) {
            this.x = x;
        }
        
        public void draw(Graphics2D g) {
            // APENAS caixa de controle no ch�o - a plataforma real � a elevatorPlatform
            
            // Caixa de controle fixa no ch�o
            int controlX = x + 25;
            int controlY = 520; // Fixo no ch�o (y=520)
            
            // Desenhar caixa de controle
            if (activated) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.DARK_GRAY);
            }
            g.fillRect(controlX, controlY, 15, 25);
            g.setColor(Color.BLACK);
            g.drawRect(controlX, controlY, 15, 25);
            
            // Indicador da caixa de controle
            g.setColor(activated ? Color.YELLOW : Color.RED);
            g.fillOval(controlX + 3, controlY + 3, 9, 9);
            
            // Cabo/trilho do elevador (visual) - do ch�o at� bem alto
            g.setColor(Color.DARK_GRAY);
            g.drawLine(controlX + 7, 50, controlX + 7, 550); // Trilho vertical
            
            // Texto de instru��o
            if (!activated) {
                g.setColor(Color.WHITE);
                g.drawString("Precisa chave azul", controlX - 30, controlY - 5);
            } else {
                g.setColor(Color.GREEN);
                g.drawString("Elevador ATIVO", controlX - 25, controlY - 5);
            }
        }
        
        public void activate() { 
            this.activated = true; 
        }
        
        public boolean isActivated() { 
            return activated; 
        }
        
        public Rectangle getControlBoxBounds() {
            int controlX = x + 25;
            int controlY = 520;
            return new Rectangle(controlX, controlY, 15, 25);
        }
    }
    
    // Classe interna para o invent�rio
    private static class Inventory {
        private ArrayList<String> items;
        private final int MAX_SLOTS = 3;
        
        public Inventory() {
            items = new ArrayList<>();
        }
        
        public boolean addItem(String item) {
            if (items.size() < MAX_SLOTS) {
                items.add(item);
                return true;
            }
            return false;
        }
        
        public boolean removeItem(String item) {
            return items.remove(item);
        }
        
        public boolean hasItem(String item) {
            return items.contains(item);
        }
        
        public void draw(Graphics2D g, int x, int y) {
            g.setColor(Color.WHITE);
            g.drawString("Invent�rio (" + items.size() + "/" + MAX_SLOTS + "):", x, y);
            
            for (int i = 0; i < MAX_SLOTS; i++) {
                int slotX = x + i * 30;
                int slotY = y + 10;
                
                // Desenhar slot
                g.setColor(Color.GRAY);
                g.fillRect(slotX, slotY, 25, 25);
                g.setColor(Color.BLACK);
                g.drawRect(slotX, slotY, 25, 25);
                
                // Desenhar item se existir
                if (i < items.size()) {
                    String item = items.get(i);
                    Color itemColor;
                    switch (item) {
                        case "AZUL": itemColor = Color.BLUE; break;
                        case "VERDE": itemColor = Color.GREEN; break;
                        case "AMARELA": itemColor = Color.YELLOW; break;
                        default: itemColor = Color.WHITE; break;
                    }
                    
                    g.setColor(itemColor);
                    g.fillOval(slotX + 5, slotY + 5, 15, 15);
                    g.setColor(Color.BLACK);
                    g.drawOval(slotX + 5, slotY + 5, 15, 15);
                }
            }
        }
    }
}
