package br.com.jumpman.timer;

/**
 * GameTimerTrigger dispara um callback apos um intervalo de tempo definido.
 * Funciona como uma thread leve, mas deve ser chamado dentro do clock do jogo.
 */
public class GameTimerTrigger {
    private long intervalMs;
    private long lastTriggerTime;
    private Runnable callback;
    private boolean active = false;

    public GameTimerTrigger(long intervalMs, Runnable callback) {
        this.intervalMs = intervalMs;
        this.callback = callback;
        this.lastTriggerTime = System.currentTimeMillis();
        this.active = true;
    }

    /**
     * Deve ser chamado a cada ciclo do clock do jogo.
     */
    public void update() {
        if (!active) return;
        long now = System.currentTimeMillis();
        if (now - lastTriggerTime >= intervalMs) {
            if (callback != null) callback.run();
            lastTriggerTime = now;
        }
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setInterval(long intervalMs) {
        this.intervalMs = intervalMs;
    }
}

