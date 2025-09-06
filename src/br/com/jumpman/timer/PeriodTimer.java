package br.com.jumpman.timer;

/**
 * Controla a transicao entre periodos do dia: manha, tarde, noite.
 */
public class PeriodTimer {
    public enum Period { DIA, TARDE, NOITE }
    private Period current = Period.DIA;
    private long lastChange = System.currentTimeMillis();
    private long intervalMs;

    public PeriodTimer(long intervalMs) {
        this.intervalMs = intervalMs;
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastChange >= intervalMs) {
            nextPeriod();
            lastChange = now;
        }
    }

    private void nextPeriod() {
        switch (current) {
            case DIA: current = Period.TARDE; break;
            case TARDE: current = Period.NOITE; break;
            case NOITE: current = Period.DIA; break;
        }
    }

    public Period getCurrentPeriod() {
        return current;
    }

    public void setPeriod(Period p) {
        current = p;
        lastChange = System.currentTimeMillis();
    }
}

