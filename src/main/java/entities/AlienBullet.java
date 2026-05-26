package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import utils.Constants;

public class AlienBullet extends GameObject {

    private final static Image sprite = getImage("missile.png").get();
    private final Alien alienOwner;

    public AlienBullet(final Position position, final Velocity velocity, final double rotationAngle, final Alien alienOwner) {
        this.alienOwner = alienOwner;
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
        setPosition(getPosition().add(getVelocity()));

        if (getPosition() instanceof Position(var x, var y)) {
            boolean outOfBounds = x < 0 || x > Constants.WIDTH
                || y < 0 || y > Constants.HEIGHT;

            if (outOfBounds) {
                dei();
            }
        }
    }

    @Override
    public void collide(final GameObject gameObject) {
        switch (gameObject) {
            case Player _, Asteroid _, Bullet _-> dei();
            case null, default -> {
            }
        }
    }
}
