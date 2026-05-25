package entities;

import static assets.AssetManager.getImage;

import game.InputHandler;
import game.SoundManager;
import java.awt.Image;
import lombok.Getter;
import lombok.Setter;
import utils.Constants;

public class Player extends GameObject {

    private final static Image sprite = getImage("spaceship.png").get();
    @Getter
    private final InputHandler inputHandler;
    @Getter
    @Setter
    private int score;

    // CONSTRUCTOR:
    public Player(double x, double y, InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        setPosition(x, y);
        setVelocity(0, 0);
        setRotationAngle(-Math.PI / 2); // straight up in radians
        setRadius(25);
        setHealth(100);
        setAlive(true);
        setScale(0.5); // make player sprite smaller
        score = 0;
    }

    @Override
    public void update() {
        // respond to input: thrust (W/Up) and rotation (A/D)
        if (inputHandler.isUpPressed()) {
            SoundManager.playLooping("thruster", "thruster.wav");
            double ax = Math.cos(getRotationAngle()) * Constants.PLAYER_ACCELERATION;
            double ay = Math.sin(getRotationAngle()) * Constants.PLAYER_ACCELERATION;
            double vx = getVelocityX() + ax;
            double vy = getVelocityY() + ay;
            double speed = Math.sqrt(vx * vx + vy * vy);
            // Cap speed: scale the velocity vector down to MAX_PLAYER_SPEED
            // while preserving direction
            if (speed > Constants.MAX_PLAYER_SPEED) {
                double scale = Constants.MAX_PLAYER_SPEED / speed;
                vx *= scale;
                vy *= scale;
            }
            setVelocity(vx, vy);
        } else {
            SoundManager.stopLooping("thruster");
            // decay velocity when thrust is not pressed
            double vx = getVelocityX() * Constants.PLAYER_VELOCITY_DECAY;
            double vy = getVelocityY() * Constants.PLAYER_VELOCITY_DECAY;
            if (Math.abs(vx) < 0.01 && Math.abs(vy) < 0.01) {
                vx = 0;
                vy = 0;
            }
            setVelocity(vx, vy);
        }
        if (inputHandler.isLeftPressed()) {
            setRotationAngle(getRotationAngle() - Constants.ROTATION_SPEED);
        }
        if (inputHandler.isRightPressed()) {
            setRotationAngle(getRotationAngle() + Constants.ROTATION_SPEED);
        }

        // update position according to velocity:
        setPosition(getPositionX() + getVelocityX(), getPositionY() + getVelocityY());

        // check if off the screen - put to other side:
        if (getPositionX() < 0) {
            setPositionX(Constants.WIDTH);
        } else if (getPositionX() > Constants.WIDTH) {
            setPositionX(0);
        }

        if (getPositionY() < 0) {
            setPositionY(Constants.HEIGHT);
        } else if (getPositionY() > Constants.HEIGHT) {
            setPositionY(0);
        }

        if (getHealth() <= 0) {
            setAlive(false);
        }

        // Normalise angle to [0, 2π) to prevent unbounded growth from continuous rotation
        double normalized = getRotationAngle() % (Math.PI * 2);
        if (normalized < 0) {
            normalized += Math.PI * 2;
        }
        setRotationAngle(normalized);
    }

    public Bullet shoot() {
        double angle = getRotationAngle(); // radians
        double bulletVelocityX = Math.cos(angle) * 16;
        double bulletVelocityY = Math.sin(angle) * 16;

        // spawn at the ship nose, along the current facing angle
        double spawnX = getPositionX() + Math.cos(angle) * getRadius();
        double spawnY = getPositionY() + Math.sin(angle) * getRadius();

        return new Bullet(spawnX, spawnY, bulletVelocityX, bulletVelocityY, angle, this);
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    public void rotateBy(double deltaAngle) {
        setRotationAngle(getRotationAngle() + deltaAngle);
    }

    @Override
    public void collide(GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(GameObject gameObject) {
        switch (gameObject) {
            case Player _ -> throw new RuntimeException("PLAYER HIT PLAYER?!?!?");
            case Alien alien -> alien.setHealth(0);
            case Asteroid asteroid -> {
                setHealth(Math.max(getHealth() - 10, 0));
                asteroid.setHealth(0);
                System.out.printf("Player is with asteroid %d!%n", getHealth());
            }
            case Bullet _ -> throw new RuntimeException("PLAYER HIT BULLET?!?!?");
            case AlienBullet alienBullet -> {
                setHealth(Math.max(getHealth() - 2, 0));
                alienBullet.setHealth(0);
                System.out.printf("Player is with asteroid %d!%n", getHealth());
            }
            case null -> {
            }
            default -> setHealth(0);
        }
    }
}
