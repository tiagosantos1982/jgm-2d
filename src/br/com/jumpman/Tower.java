// ...existing code...
package br.com.jumpman;

import java.awt.*;

public class Tower
{
    private int x;
    private int y;
    private int w;
    private int h;
    private int topSize;
    private boolean falling;
    private int fallSpeed;
    private int life = 2;
    public void hit()
    {
        if (life > 0)
        {
            life--;
            if (life == 0)
            {
                fall();
            }
        }
    }

    public Tower(int x, int y, int w, int h, int topSize)
    {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.topSize = topSize;
        this.falling = false;
        this.fallSpeed = 0;
    }

    public void draw(Graphics g)
    {
        g.setColor(new Color(100, 100, 200));
        g.fillRect(x, y, w, h);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, w, h);
        g.setColor(new Color(200, 200, 100));
        g.fillRect(x - (topSize - w) / 2, y - topSize, topSize, topSize);
        g.setColor(Color.BLACK);
        g.drawRect(x - (topSize - w) / 2, y - topSize, topSize, topSize);

        // Barra de vida: quadradinhos acima da torre
        int barWidth = 12;
        int barHeight = 12;
        int spacing = 4;
        for (int i = 0; i < life; i++)
        {
            int bx = x + (w / 2) - ((life * barWidth + (life - 1) * spacing) / 2) + i * (barWidth + spacing);
            int by = y - topSize - 18;
            g.setColor(Color.GREEN);
            g.fillRect(bx, by, barWidth, barHeight);
            g.setColor(Color.BLACK);
            g.drawRect(bx, by, barWidth, barHeight);
        }
    }

    public boolean checkCollision(Player p)
    {
        return getBounds().intersects(p.getBounds());
    }

    public Rectangle getBounds()
    {
        return new Rectangle(x, y, w, h);
    }

    public void fall()
    {
        falling = true;
        fallSpeed = 10;
    }

    public void update()
    {
        if (falling)
        {
            y += fallSpeed;
            if (y > 600)
            {
                falling = false;
            }
        }
    }
}

