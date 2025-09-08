package br.com.jumpman.fx;

import java.awt.*;
import java.util.ArrayList;

import br.com.jumpman.timer.PeriodTimer;

public class GameWeatherControl {
    private int lightningTotalBlinks = 0;
    private long lightningBlinkStart = 0;
    private long lightningBlinkDuration = 0;
    private boolean lightningRayDrawn = false;

    private boolean rainActive = false; // Comeca sem chuva
    private float rainIntensityLevel = 0f; // Controla a intensidade da chuva (0-1)
    private long rainStartTime = 0;
    private static final long RAIN_BUILDUP_TIME = 3000; // 3 segundos para ceu escurecer completamente
    private static final java.util.Random rand = new java.util.Random();
    
    // Cores do ceu normal (melhoradas)
    private static final Color DAY_BG_TOP = new Color(87, 160, 235);        // Azul ceu mais vibrante
    private static final Color DAY_BG_BOTTOM = new Color(135, 206, 250);   // Azul mais claro embaixo
    private static final Color AFTERNOON_BG_TOP = new Color(255, 200, 120);
    private static final Color AFTERNOON_BG_BOTTOM = new Color(255, 183, 77);
    private static final Color NIGHT_BG_TOP = new Color(30, 30, 60);
    private static final Color NIGHT_BG_BOTTOM = new Color(60, 60, 100);
    
    // Cores do ceu durante chuva (azul petroleo para cinza chumbo)
    private static final Color RAIN_BG_TOP = new Color(47, 79, 79);        // Azul petroleo escuro (DarkSlateGray)
    private static final Color RAIN_BG_BOTTOM = new Color(105, 105, 105);  // Cinza chumbo (DimGray)
    private final ArrayList<RainParticle> rainParticles;
    private static final int RAIN_DENSITY = 1; // particulas por pixel
    private int groundY;
    private int panelWidth;
    private int panelHeight;

    // Lightning
    private boolean lightningActive = false;
    // Campos de agendamento de relÃ¢mpago removidos (nÃ£o utilizados)

    private PeriodTimer.Period currentPeriod = PeriodTimer.Period.DIA;
    private PeriodTimer.Period lastPeriod = PeriodTimer.Period.DIA; // usado na transiÃ§Ã£o de gradiente
    private long periodTransitionStart = 0;
    private static final int PERIOD_TRANSITION_MS = 2000;
    // Intensidade da chuva
    private int rainIntensity = 2; // 1=leve, 2=moderada, 3=forte
    public void setRainIntensity(int intensity) {
        rainIntensity = clamp(intensity, 1, 3);
    }
    public int getRainIntensity() { return rainIntensity; }
    public boolean isRainActive() { return rainActive; }
    
    // Controles de configuracao do clima
    public void adjustGroundY(int delta) {
        groundY += delta;
        groundY = Math.clamp(groundY, panelHeight - 100, panelHeight); // Limitar range
    }
    
    public int getGroundY() { return groundY; }
    
    public void adjustRainDensity(int delta) {
        int newCount = rainParticles.size() + delta;
        newCount = Math.clamp(newCount, 20, 200); // Limitar entre 20-200
        
        if (newCount > rainParticles.size()) {
            // Adicionar particulas
            for (int i = rainParticles.size(); i < newCount; i++) {
                rainParticles.add(new RainParticle(panelWidth, panelHeight));
            }
        } else if (newCount < rainParticles.size()) {
            // Remover particulas
            while (rainParticles.size() > newCount) {
                rainParticles.remove(rainParticles.size() - 1);
            }
        }
    }
    
    public int getRainDensity() { return rainParticles.size(); }
    // Particulas de cenario
    private final ArrayList<Point> dustParticles = new ArrayList<>();
    private final ArrayList<Point> fireflies = new ArrayList<>();
    private static final int DUST_COUNT = 20;
    private static final int FIREFLY_COUNT = 15;

    public GameWeatherControl(int groundY, int panelWidth, int panelHeight) {
        this.groundY = groundY;
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.rainParticles = new ArrayList<>();
        int rainCount = Math.max(80, panelWidth * RAIN_DENSITY / 10); // ajusta densidade
        for (int i = 0; i < rainCount; i++) {
            rainParticles.add(new RainParticle(panelWidth, panelHeight));
        }
        for (int i = 0; i < DUST_COUNT; i++) {
            dustParticles.add(new Point(rand.nextInt(panelWidth), groundY + rand.nextInt(40)));
        }
        for (int i = 0; i < FIREFLY_COUNT; i++) {
            fireflies.add(new Point(rand.nextInt(panelWidth), groundY - 40 - rand.nextInt(60)));
        }
    }

    // Metodos publicos para integracao com GamePanel
    public void setRainActive(boolean active) {
        if (active && !this.rainActive) {
            // Chuva comecando - iniciar cronometro para escurecer ceu
            rainStartTime = System.currentTimeMillis();
            rainIntensityLevel = 0f;
        } else if (!active && this.rainActive) {
            // Chuva parando - resetar
            rainIntensityLevel = 0f;
            rainStartTime = 0;
        }
        this.rainActive = active;
    }

    public void updateRainIntensity() {
        if (rainActive && rainStartTime > 0) {
            long elapsed = System.currentTimeMillis() - rainStartTime;
            rainIntensityLevel = Math.min(1.0f, elapsed / (float)RAIN_BUILDUP_TIME);
        } else if (!rainActive) {
            // Transicao suave de volta ao normal quando para de chover
            rainIntensityLevel = Math.max(0f, rainIntensityLevel - 0.02f);
            if (rainIntensityLevel <= 0f) {
                rainStartTime = 0;
            }
        }
    }

    public void setPeriod(PeriodTimer.Period period) {
        this.currentPeriod = period;
    }

    public PeriodTimer.Period getPeriod() {
        return currentPeriod;
    }

    public void updateWeather(java.util.List<Rectangle> obstacles, Rectangle playerBounds) {
        // Atualizar intensidade da chuva e transicoes do ceu
        updateRainIntensity();
        
        // Atualiza particulas de chuva com colisao (incluindo player)
        for (RainParticle rain : rainParticles) {
            rain.update(groundY, panelHeight, obstacles, playerBounds);
        }
        // Pode adicionar outras atualizacoes de efeitos aqui
    }

    public void triggerLightning() {
    double durationMs = 500 + Math.random() * 500; // 500 a 1000 ms
    lightningBlinkDuration = (long)durationMs;
    lightningTotalBlinks = durationMs < 750 ? 2 : 3;
    lightningBlinkStart = System.currentTimeMillis();
    lightningActive = true;
    lightningRayDrawn = false;
    }

    public void drawWeather(Graphics g) {
        drawBackgroundGradient(g);
        drawDust(g);
        drawFireflies(g);
        drawRain(g);
        drawLightning(g);
    }

    private void drawBackgroundGradient(Graphics g) {
        Color topFrom;
        Color bottomFrom;
        Color topTo;
        Color bottomTo;
        switch (lastPeriod) {
            case DIA:
                topFrom = DAY_BG_TOP; bottomFrom = DAY_BG_BOTTOM; break;
            case TARDE:
                topFrom = AFTERNOON_BG_TOP; bottomFrom = AFTERNOON_BG_BOTTOM; break;
            case NOITE:
                topFrom = NIGHT_BG_TOP; bottomFrom = NIGHT_BG_BOTTOM; break;
            default:
                topFrom = DAY_BG_TOP; bottomFrom = DAY_BG_BOTTOM;
        }
        switch (currentPeriod) {
            case DIA:
                topTo = DAY_BG_TOP; bottomTo = DAY_BG_BOTTOM; break;
            case TARDE:
                topTo = AFTERNOON_BG_TOP; bottomTo = AFTERNOON_BG_BOTTOM; break;
            case NOITE:
                topTo = NIGHT_BG_TOP; bottomTo = NIGHT_BG_BOTTOM; break;
            default:
                topTo = DAY_BG_TOP; bottomTo = DAY_BG_BOTTOM;
        }
        float transitionRatio = 1f;
        if (periodTransitionStart > 0) {
            long elapsed = System.currentTimeMillis() - periodTransitionStart;
            if (elapsed < PERIOD_TRANSITION_MS) {
                transitionRatio = elapsed / (float)PERIOD_TRANSITION_MS;
            } else {
                periodTransitionStart = 0;
                lastPeriod = currentPeriod;
            }
        }
        Color topBg = blendColors(topFrom, topTo, transitionRatio);
        Color bottomBg = blendColors(bottomFrom, bottomTo, transitionRatio);
        
        // Aplicar degrade de chuva (azul petroleo para cinza chumbo)
        if (rainIntensityLevel > 0f) {
            // Interpolar entre as cores normais e as cores de chuva
            topBg = blendColors(topBg, RAIN_BG_TOP, rainIntensityLevel);
            bottomBg = blendColors(bottomBg, RAIN_BG_BOTTOM, rainIntensityLevel);
        }
        
        // Desenhar degrade linha por linha para melhor qualidade visual
        for (int y = 0; y < panelHeight; y++) {
            float ratio = y / (float)panelHeight;
            Color rowColor = blendColors(topBg, bottomBg, ratio);
            g.setColor(rowColor);
            g.drawLine(0, y, panelWidth, y);
        }
    }

    private void drawDust(Graphics g) {
        if (currentPeriod == PeriodTimer.Period.DIA && !rainActive) {
            for (Point p : dustParticles) {
                g.setColor(new Color(200, 180, 120, 80));
                g.fillOval(p.x, p.y, 6, 3);
            }
        }
    }

    private void drawFireflies(Graphics g) {
        if (currentPeriod == PeriodTimer.Period.NOITE && !rainActive) {
            long time = System.currentTimeMillis();
            for (int i = 0; i < fireflies.size(); i++) {
                Point f = fireflies.get(i);
                // Animacao de pisca-pisca - cada firefly pisca em tempo diferente
                long offset = (long)i * 200; // Cada firefly tem offset diferente
                double cycle = (time + offset) / 1000.0; // Ciclo de 1 segundo
                double brightness = (Math.sin(cycle * Math.PI * 2) + 1) / 2; // 0 a 1
                
                // So desenha se estiver "aceso" (brightness > 0.3)
                if (brightness > 0.3) {
                    int alpha = (int)(brightness * 255);
                    g.setColor(new Color(255, 255, 180, alpha));
                    
                    // Desenhar o corpo do firefly
                    g.fillOval(f.x, f.y, 6, 6);
                    
                    // Adicionar um halo ao redor quando muito brilhante
                    if (brightness > 0.7) {
                        g.setColor(new Color(255, 255, 180, alpha / 3));
                        g.fillOval(f.x - 2, f.y - 2, 10, 10);
                    }
                }
            }
        }
    }

    private void drawRain(Graphics g) {
        // Mostrar chuva imediatamente quando ativada, mesmo durante transicao do ceu
        if (rainActive) {
            int rainDrawCount = rainParticles.size();
            for (int i = 0; i < rainDrawCount; i++) {
                rainParticles.get(i).draw(g);
            }
        }
    }

    private void drawLightning(Graphics g) {
        if (lightningActive) {
            long now = System.currentTimeMillis();
            long elapsed = now - lightningBlinkStart;
            long blinkPeriod = lightningBlinkDuration / Math.max(1, lightningTotalBlinks);
            int blinkIndex = (int)(elapsed / blinkPeriod);
            boolean showFlash = ((elapsed % blinkPeriod) < (blinkPeriod / 2)) && blinkIndex < lightningTotalBlinks;
            if (showFlash) {
                Color flash = new Color(255, 255, 255, 180);
                g.setColor(flash);
                g.fillRect(0, 0, panelWidth, panelHeight);
            }
            // Apos o ultimo flash, desenha o raio
            if (blinkIndex >= lightningTotalBlinks && !lightningRayDrawn) {
                int x = panelWidth / 2 + rand.nextInt(100) - 50;
                int y = 60;
                g.setColor(new Color(255,255,255));
                drawBranchedLightning(g, x, y, x, groundY, 7);
                lightningRayDrawn = true;
            }
            // Finaliza efeito apos tempo
            if (elapsed > lightningBlinkDuration + 400) {
                lightningActive = false;
                lightningRayDrawn = false;
            }
        }
    }

    // Desenha raio ramificado
    private void drawBranchedLightning(Graphics g, int x1, int y1, int x2, int y2, int depth) {
        if (depth == 0) return;
        int midX = (x1 + x2) / 2 + rand.nextInt(30) - 15;
        int midY = (y1 + y2) / 2 + rand.nextInt(30) - 15;
        g.drawLine(x1, y1, midX, midY);
        g.drawLine(midX, midY, x2, y2);
        if (depth > 2 && rand.nextFloat() < 0.5f) {
            // ramificacao lateral
            int branchX = midX + rand.nextInt(40) - 20;
            int branchY = midY + rand.nextInt(40) - 20;
            g.drawLine(midX, midY, branchX, branchY);
        }
        drawBranchedLightning(g, x1, y1, midX, midY, depth - 1);
        drawBranchedLightning(g, midX, midY, x2, y2, depth - 1);
    }

    // Utilitario clamp
    private int clamp(int value, int min, int max) {
        return Math.clamp(value, min, max);
    }

    private Color blendColors(Color c1, Color c2, float ratio) {
        float ir = 1f - ratio;
        int r = (int)(c1.getRed() * ir + c2.getRed() * ratio);
        int g = (int)(c1.getGreen() * ir + c2.getGreen() * ratio);
        int b = (int)(c1.getBlue() * ir + c2.getBlue() * ratio);
        return new Color(r, g, b);
    }
}
