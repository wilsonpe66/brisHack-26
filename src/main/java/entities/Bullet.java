package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import utils.Constants;

public class Bullet extends GameObject {

    private final static Image sprite = getImage("missile.png").get();
    private final Player owner;

    // CONSTRUCTOR:
    public Bullet(final Position position) {
        this(position, new Velocity(10, 10), Math.PI / 4, null);
    }

    public Bullet(final Position position, final Velocity velocity, double rotationAngle) {
        this(position, velocity, rotationAngle, null);
    }

    public Bullet(final Position position, final Velocity velocity, double rotationAngle, Player owner) {
        this.owner = owner;
        setPosition(position);
        setVelocity(velocity);
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
        setPosition(getPosition().add(getVelocity()));

        final Position position = getPosition();
        final double x = position.x();
        final double y = position.y();
        boolean outOfBounds = x < 0
            ||  x > Constants.WIDTH
            ||  y < 0
            ||  y > Constants.HEIGHT;

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
