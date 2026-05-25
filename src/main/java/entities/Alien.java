package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import utils.Constants;

public class Alien extends GameObject {

    private final static Image sprite = getImage("shipGreen_manned.png").get();
    private final Player player;
    /**
     * Frames until the alien can shoot again (slower than player).
     */
    private int shootCooldown;
    /**
     * Grace period after spawning before the alien starts shooting. Gives the player a brief window to react to a new alien.
     */
    private int noShootTimer;
    /**
     * Frames until we next recalculate velocity direction towards the player. This makes alien movement "steppy" rather than perfectly smooth tracking.
     */
    private int targetUpdateTimer;

    /**
     * Spawn from side of screen with given position and initial velocity.
     */
    public Alien(double x, double y, double velocityX, double velocityY, Player player) {
        this.player = player;
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setRotationAngle(Math.atan2(velocityY, velocityX));
        setRadius(20);
        setHealth(100);
        setAlive(true);
        setScale(0.5);
        shootCooldown = 0;
        noShootTimer = Constants.ALIEN_SPAWN_NO_SHOOT_FRAMES;
        targetUpdateTimer = 0;
    }

    /**
     * Returns an AlienBullet aimed at the player, or null if on cooldown.
     */
    public AlienBullet shoot() {
        if (shootCooldown > 0 || noShootTimer > 0 || !player.getIsAlive()) {
            return null;
        }

        // atan2(dy, dx) calculates the angle from this alien to the player
        double angle = Math.atan2(
            player.getPositionY() - getPositionY(),
            player.getPositionX() - getPositionX()
        );
        setRotationAngle(angle); // face the player when shooting
        double bulletVelocityX = Math.cos(angle) * Constants.ALIEN_BULLET_SPEED;
        double bulletVelocityY = Math.sin(angle) * Constants.ALIEN_BULLET_SPEED;

        double spawnX = getPositionX() + Math.cos(angle) * getRadius();
        double spawnY = getPositionY() + Math.sin(angle) * getRadius();

        shootCooldown = Constants.ALIEN_SHOOT_COOLDOWN_FRAMES;
        return new AlienBullet(spawnX, spawnY, bulletVelocityX, bulletVelocityY, angle, this);
    }

    @Override
    public void update() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (noShootTimer > 0) {
            noShootTimer--;
        }
        targetUpdateTimer--;
        if (targetUpdateTimer <= 0 && player.getIsAlive()) {
            double angle = Math.atan2(
                player.getPositionY() - getPositionY(),
                player.getPositionX() - getPositionX()
            );
            setVelocity(Math.cos(angle) * Constants.ALIEN_SPEED, Math.sin(angle) * Constants.ALIEN_SPEED);
            setRotationAngle(angle);
            targetUpdateTimer = Constants.ALIEN_TARGET_UPDATE_INTERVAL;
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
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    @Override
    public void collide(final GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(GameObject gameObject) {
        switch (gameObject) {
            case AlienBullet _ -> {
            }
            case null -> {
            }
            default -> setHealth(0);
        }
    }
}
