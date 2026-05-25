package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import utils.Constants;

public class AlienBullet extends GameObject {

    private final static Image sprite = getImage("missile.png").get();
    private final Alien alienOwner;

    public AlienBullet(double x, double y, double velocityX, double velocityY, double rotationAngle, Alien alienOwner) {
        this.alienOwner = alienOwner;
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setRotationAngle(rotationAngle);
        setRadius(5);
        setHealth(1);
        setAlive(true);
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

        if (outOfBounds || getHealth() <= 0) {
            setAlive(false);
        }
    }

    @Override
    public void collide(GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(GameObject gameObject) {
        switch (gameObject) {
            case Player player -> {
                setHealth(0);
                player.setHealth(0);
            }
            case Asteroid _ -> setHealth(0);
            case Bullet bullet -> {
                setHealth(0);
                bullet.setHealth(0);
            }
            case null, default -> {
            }
        }
    }
}
