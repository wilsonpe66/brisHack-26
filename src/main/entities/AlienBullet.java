package entities;

import utils.Constants;

import java.awt.*;

public class AlienBullet extends GameObject {
    private final static Image sprite = getImage("assets/images/missile.png");
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
    public void collideWith(Player player) {
        setHealth(0);
        player.setHealth(0);
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        setHealth(0);
    }

    @Override
    public void collideWith(Bullet bullet) {
        setHealth(0);
        bullet.setHealth(0);
    }

    @Override
    public void collideWith(Alien alien) {
        // friendly fire off - no effect
    }
}
