package entities;

public class HealthBar implements Updatable{

    private Player player; // the current player associated with the health
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