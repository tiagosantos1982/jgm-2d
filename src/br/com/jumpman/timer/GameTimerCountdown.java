package br.com.jumpman.timer;

/**
 * Controla a contagem regressiva do tempo de jogo.
 */
public class GameTimerCountdown {
    private int secondsLeft;
    private final GameTimerTrigger trigger;
    private boolean gameOver = false;

    public GameTimerCountdown(int startSeconds, Runnable onGameOver) {
        this.secondsLeft = startSeconds;
        this.trigger = new GameTimerTrigger(1000, () -> {
            if (secondsLeft > 0) {
                secondsLeft--;
                if (secondsLeft == 0) {
                    gameOver = true;
                    if (onGameOver != null) onGameOver.run();
                }
            }
        });
    }

    public void update() {
        if (!gameOver) trigger.update();
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}

