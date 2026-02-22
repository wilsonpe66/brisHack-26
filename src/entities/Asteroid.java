import java.awt.*;
import java.util.Random;

public class Asteroid extends GameObject{
    private static final Random random = new Random();
    private static final Image[] sprites = {
        Toolkit.getDefaultToolkit().getImage("assets/images/asteroid1.png"),
        Toolkit.getDefaultToolkit().getImage("assets/images/asteroid2.png"),
        Toolkit.getDefaultToolkit().getImage("assets/images/asteroid3.png"),
        Toolkit.getDefaultToolkit().getImage("assets/images/asteroid4.png")
    };
    private final Image sprite;
    /** True only when this asteroid was destroyed by a bullet (used for scoring). */
    private boolean killedByBullet;

    // CONSTRUCTOR:
    public Asteroid(double x, double y, double velocityX, double velocityY) {
        sprite = sprites[random.nextInt(sprites.length)];
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setRotationAngle(Math.random() * Math.PI * 2);
        setRadius(30);
        setHealth(1);
        setAlive(true);
        setScale(0.3);
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    @Override
    public void update() {
        // update position according to velocity:
        setPosition(getPositionX() + getVelocityX(), getPositionY() + getVelocityY());

        // remove asteroid if it goes too far off screen (with buffer for spawning)
        // only setAlive(false) so we don't award score for flying off - score is only for shot asteroids
        double buffer = 100;
        if (getPositionX() < -buffer || getPositionX() > Constants.WIDTH + buffer) {
            setAlive(false);
            return;
        }
        if (getPositionY() < -buffer || getPositionY() > Constants.HEIGHT + buffer) {
            setAlive(false);
            return;
        }

        if (getHealth() <= 0) {
            setAlive(false);
        }
    }

    @Override
    public void collide(GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(Player player) {
        // Don't kill this asteroid - only the player dies. Avoids awarding +1 score when player dies.
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        setHealth(0);
        asteroid.setHealth(0);
    }

    @Override
    public void collideWith(Bullet bullet) {
        setHealth(getHealth() - 1);
        killedByBullet = true;
    }

    public boolean wasKilledByBullet() {
        return killedByBullet;
    }
}
