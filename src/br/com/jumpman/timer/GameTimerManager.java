package br.com.jumpman.timer;

import java.util.ArrayList;
import java.util.List;

/**
 * Gerencia multiplos GameTimerTrigger para facilitar o controle de eventos temporizados.
 */
public class GameTimerManager {
    private final List<GameTimerTrigger> triggers = new ArrayList<>();

    public void addTrigger(GameTimerTrigger trigger) {
        triggers.add(trigger);
    }

    public void removeTrigger(GameTimerTrigger trigger) {
        triggers.remove(trigger);
    }

    /**
     * Deve ser chamado a cada ciclo do clock do jogo para atualizar todos os triggers.
     */
    public void updateAll() {
        for (GameTimerTrigger trigger : triggers) {
            trigger.update();
        }
    }
}

