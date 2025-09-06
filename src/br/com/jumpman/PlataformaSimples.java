package br.com.jumpman;

import java.awt.*;

public class PlataformaSimples {
    private int x;
    private int y;
    private int w;
    private int h;
    private Color color;

    public PlataformaSimples(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.color = Color.DARK_GRAY;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);
    }
}
