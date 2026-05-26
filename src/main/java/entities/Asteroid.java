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
    public Asteroid(final Position position, final Velocity velocity) {
        sprite = sprites[random.nextInt(sprites.length)];
        setPosition(position);
        setVelocity(velocity);
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
        setPosition(getPosition().add(getVelocity()));

        // remove asteroid if it goes too far off-screen (with buffer for spawning)
        // only setAlive(false) so we don't award score for flying off - score is only for shot asteroids
        double buffer = 100;

        if (getPosition() instanceof Position(double x, double y)) {
            if (x < -buffer || x > Constants.WIDTH + buffer) {
                dei();
                return;
            }
            if (y < -buffer || y > Constants.HEIGHT + buffer) {
                dei();
            }
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
