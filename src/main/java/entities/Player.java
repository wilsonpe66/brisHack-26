package entities;

import static assets.AssetManager.getImage;

import entities.amo.Bullet;
import entities.motion.Position;
import entities.motion.Velocity;
import game.InputHandler;
import game.SoundManager;
import game.WorldState;
import java.awt.Image;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import utils.Constants;
import utils.GameLevel;

public class Player extends GameObject implements Wrappable, SelfDefendable {

    private final static Image sprite = getImage("spaceship.png").get();

    private final WorldState worldState;

    @Getter
    private final InputHandler inputHandler;
    @Getter
    @Setter
    private int score;

    // CONSTRUCTOR:
    public Player(final WorldState worldState, final Position position, final InputHandler inputHandler) {
        this.worldState = worldState;
        this.inputHandler = inputHandler;
        setPosition(position);
        setVelocity(Velocity.ZERO);
        setRotationAngle(-Math.PI / 2); // straight up in radians
        setRadius(25);
        setHealth(100);
        setScale(0.5); // make player sprite smaller
        score = 0;
    }

    public void incrementScore(final int offset) {
        score += offset;
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

    @Override
    public List<Bullet> shoot() {
        final double angle = getRotationAngle(); // radians
        final GameLevel gameLevel = worldState.gameLevel();
        final int speed = gameLevel.PLAYER_BULLET_SPEED();
        return switch (gameLevel.LEVEL_NUMBER()) {
            case 0, 1 -> getSingleShoot(getPosition(), getRadius(), speed, angle);
            case 2 -> getSupperShoot(getPosition(), getRadius(), speed, angle);
            case 3, 4, 5, 6 -> getSupperDuperShoot(getPosition(), getRadius(), speed, angle);
            case 7, 8 -> getSupperDuper2Shoot(getPosition(), getRadius(), speed, angle);
            default -> getSupperDuper3Shoot(getPosition(), getRadius(), speed, angle);
        };
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    @Override
    public void collide(final GameObject gameObject) {
        final int health = getHealth();
        switch (gameObject) {
            case Player _ -> throw new RuntimeException("PLAYER HIT PLAYER?!?!?");
            case Asteroid _ -> setHealth(Math.max(health - 10, 0));
            case Bullet bullet when (bullet.getOwner() instanceof Alien) -> setHealth(Math.max(health - 2, 0));
            case null -> {
            }
            default -> dei();
        }

        if (isDead()) {
            SoundManager.stopLooping("thruster");
            SoundManager.playSound("game-over.wav");
        }
    }
}
