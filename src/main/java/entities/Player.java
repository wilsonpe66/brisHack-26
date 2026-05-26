package entities;

import static assets.AssetManager.getImage;

import game.InputHandler;
import game.SoundManager;
import java.awt.Image;
import lombok.Getter;
import lombok.Setter;
import utils.Constants;

public class Player extends GameObject implements Wrappable {

    private final static Image sprite = getImage("spaceship.png").get();
    @Getter
    private final InputHandler inputHandler;
    @Getter
    @Setter
    private int score;

    // CONSTRUCTOR:
    public Player(final Position position, final InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        setPosition(position);
        setVelocity(Velocity.ZERO);
        setRotationAngle(-Math.PI / 2); // straight up in radians
        setRadius(25);
        setHealth(100);
        setScale(0.5); // make player sprite smaller
        score = 0;
    }

    @Override
    public void update() {
        // respond to input: thrust (W/Up) and rotation (A/D)
        final Velocity velocity = getVelocity();
        if (inputHandler.isUpPressed()) {
            SoundManager.playLooping("thruster", "thruster.wav");
            Velocity v = velocity.add(Velocity.fromAngleAndSpeed(getRotationAngle(), Constants.PLAYER_ACCELERATION));
            final double speed = v.getSpeed();
            // Cap speed: scale the velocity vector down to MAX_PLAYER_SPEED
            // while preserving direction
            if (speed > Constants.MAX_PLAYER_SPEED) {
                v = v.scale(Constants.MAX_PLAYER_SPEED / speed);
            }
            setVelocity(v);
        } else {
            SoundManager.stopLooping("thruster");
            // decay velocity when thrust is not pressed
            Velocity v = velocity.scale(Constants.PLAYER_VELOCITY_DECAY);
            if (v.getSpeed() < .01) {
                v = Velocity.ZERO;
            }
            setVelocity(v);
        }
        if (inputHandler.isLeftPressed()) {
            setRotationAngle(getRotationAngle() - Constants.ROTATION_SPEED);
        }
        if (inputHandler.isRightPressed()) {
            setRotationAngle(getRotationAngle() + Constants.ROTATION_SPEED);
        }

        // update position according to velocity:
        setPosition(getPosition().add(velocity));
        wrapPosition();

        // Normalise angle to [0, 2π) to prevent unbounded growth from continuous rotation
        double normalized = getRotationAngle() % (Math.PI * 2);
        if (normalized < 0) {
            normalized += Math.PI * 2;
        }
        setRotationAngle(normalized);
    }

    public Bullet shoot() {
        final double angle = getRotationAngle(); // radians
        return new Bullet(
            getPosition().add(Velocity.fromAngleAndSpeed(angle, getRadius())),
            Velocity.fromAngleAndSpeed(angle, 16),
            angle, this
        );
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    public void rotateBy(double deltaAngle) {
        setRotationAngle(getRotationAngle() + deltaAngle);
    }

    @Override
    public void collide(final GameObject gameObject) {
        final int health = getHealth();
        switch (gameObject) {
            case Player _ -> throw new RuntimeException("PLAYER HIT PLAYER?!?!?");
            case Asteroid _ -> setHealth(Math.max(health - 10, 0));
            case Bullet _ -> throw new RuntimeException("PLAYER HIT BULLET?!?!?");
            case AlienBullet _ -> setHealth(Math.max(health - 2, 0));
            case null -> {
            }
            default -> dei();
        }
    }
}
