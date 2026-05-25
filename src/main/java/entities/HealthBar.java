package entities;

import lombok.Getter;

public class HealthBar implements Updatable {

    private final Player player; // the current player associated with the health

    @Getter
    private int displayedHealth; // the number shown on screen

    public HealthBar(Player player) {
        this.player = player;
        this.displayedHealth = player.getHealth();
    }

    @Override
    public void update() {
        displayedHealth = player.getHealth(); // add smooth health change transition?
    }
}