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

        // kill asteroid if it goes too far off screen (with buffer for spawning)
        double buffer = 100;
        if (getPositionX() < -buffer || getPositionX() > Constants.WIDTH + buffer) {
            this.setHealth(0);
        }

        if (getPositionY() < -buffer || getPositionY() > Constants.HEIGHT + buffer) {
            this.setHealth(0);
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
        setHealth(0);
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        setHealth(0);
        asteroid.setHealth(0);
    }

    @Override
    public void collideWith(Bullet bullet) {
        setHealth(getHealth() - 1 );
    }
}
