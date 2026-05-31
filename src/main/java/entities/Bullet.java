package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import lombok.Getter;
import utils.Constants;

public abstract class Bullet extends GameObject {

    private final static Image sprite = getImage("missile.png").get();

    // CONSTRUCTOR:
    public Bullet(final Position position) {
        this(position, new Velocity(10, 10), Math.PI / 4);
    }

    public Bullet(final Position position, final Velocity velocity, double rotationAngle) {
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
            case Player _, Asteroid _, Bullet _, Alien _ -> dei();

            case null, default -> {
            }
        }
    }
}
