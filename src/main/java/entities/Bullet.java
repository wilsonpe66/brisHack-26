package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import utils.Constants;

public class Bullet extends GameObject {

    private final static Image sprite = getImage("missile.png").get();
    private final Player owner;

    // CONSTRUCTOR:
    public Bullet(double x, double y) {
        this(x, y, 10, 10, Math.PI / 4, null);
    }

    public Bullet(double x, double y, double velocityX, double velocityY, double rotationAngle) {
        this(x, y, velocityX, velocityY, rotationAngle, null);
    }

    public Bullet(double x, double y, double velocityX, double velocityY, double rotationAngle, Player owner) {
        this.owner = owner;
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setRotationAngle(rotationAngle);
        setRadius(5);
        setHealth(1);
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    @Override
    public void update() {
        // update position according to velocity:
        setPosition(getPositionX() + getVelocityX(), getPositionY() + getVelocityY());

        boolean outOfBounds = getPositionX() < 0
            || getPositionX() > Constants.WIDTH
            || getPositionY() < 0
            || getPositionY() > Constants.HEIGHT;

        if (outOfBounds) {
            dei();
        }
    }

    @Override
    public void collide(final GameObject gameObject) {
        switch (gameObject) {
            case Player _, Asteroid _, Bullet _, AlienBullet _ -> dei();
            case Alien _ -> {
                dei();
                if (owner != null) {
                    owner.setScore(owner.getScore() + Constants.ALIEN_KILL_SCORE);
                }
            }

            case null, default -> {
            }
        }
    }
}
