package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import utils.Constants;

public class AlienBullet extends GameObject {

    private final static Image sprite = getImage("missile.png").get();
    private final Alien alienOwner;

    public AlienBullet(final double x, final double y, final double velocityX, final double velocityY, final double rotationAngle, final Alien alienOwner) {
        this.alienOwner = alienOwner;
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
            case Player _, Asteroid _, Bullet _ -> dei();
            case null, default -> {
            }
        }
    }
}
