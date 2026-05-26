package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import java.util.Random;
import utils.Constants;

public class Asteroid extends GameObject {

    private static final Random random = new Random();
    private static final Image[] sprites = {
        getImage("asteroid1.png").get(),
        getImage("asteroid2.png").get(),
        getImage("asteroid3.png").get(),
        getImage("asteroid4.png").get()
    };
    private final Image sprite;
    /**
     * True only when this asteroid was destroyed by a bullet (used for scoring).
     */
    private boolean killedByBullet;

    // CONSTRUCTOR:
    public Asteroid(double x, double y, double velocityX, double velocityY) {
        sprite = sprites[random.nextInt(sprites.length)];
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setRotationAngle(Math.random() * Math.PI * 2);
        setRadius(30);
        setHealth(1);
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
            dei();
            return;
        }
        if (getPositionY() < -buffer || getPositionY() > Constants.HEIGHT + buffer) {
            dei();
        }
    }

    @Override
    public void collide(final GameObject gameObject) {
        switch (gameObject) {
            case Asteroid _, AlienBullet _, Player _ -> dei();
            case Bullet _ -> {
                dei();
                killedByBullet = true;
            }
            case null, default -> {
            }
        }
    }

    public boolean wasKilledByBullet() {
        return killedByBullet;
    }
}
