package br.com.jumpman;

import java.awt.*;


public class Player {
    private int x;
    private int y;
    private int w;
    private int h;
    private double vx;
    private double vy;
    private boolean jumping;
    private Color color;

    private static final double GRAVITY = 0.5; // Aproximacao simples da gravidade
    private static final double MOVE_SPEED = 4.0;
    private static final double JUMP_FORCE = 10.0;

    public Player(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.vx = 0;
        this.vy = 0;
        this.jumping = false;
        this.color = new Color(200, 100, 100);
    }

    public void draw(Graphics g, boolean selected) {
        g.setColor(selected ? Color.YELLOW : color);
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);
    }

    public void moveLeft() {
        vx = -MOVE_SPEED;
    }

    public void moveRight() {
        vx = MOVE_SPEED;
    }

    public void stop() {
        vx = 0;
    }

    public void jump() {
        if (!jumping) {
            vy = -JUMP_FORCE;
            jumping = true;
        }
    }

    public void update() {
        x += vx;
        y += vy;
        vy += GRAVITY;
        // Limites da tela
        if (x < 0) x = 0;
        if (x > 800 - w) x = 800 - w;
        if (y > 600 - h) {
            y = 600 - h;
            vy = 0;
            jumping = false;
        }
    }

    public void landOn(int platY) {
        y = platY - h;
        vy = 0;
        jumping = false;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public boolean isJumping() {
        return jumping;
    }

    public double getVy() {
        return vy;
    }
    
    // Metodos adicionais para interacao
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public void stopFalling() {
        this.vy = 0;
        this.jumping = false;
    }
}

