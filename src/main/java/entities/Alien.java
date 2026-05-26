package entities;

import static assets.AssetManager.getImage;

import java.awt.Image;
import utils.Constants;

public class Alien extends GameObject implements Wrappable {

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
    public Alien(final Position position, final Velocity velocity, final Player player) {
        super();
        this.player = player;
        setPosition(position);
        setVelocity(velocity);
        setRotationAngle(velocity.getRotation());
        setRadius(20);
        setHealth(100);
        setScale(0.5);
        shootCooldown = 0;
        noShootTimer = Constants.ALIEN_SPAWN_NO_SHOOT_FRAMES;
        targetUpdateTimer = 0;
    }

    /**
     * Returns an AlienBullet aimed at the player, or null if on cooldown.
     */
    public AlienBullet shoot() {
        if (shootCooldown > 0 || noShootTimer > 0 || player.isDead()) {
            return null;
        }

        shootCooldown = Constants.ALIEN_SHOOT_COOLDOWN_FRAMES;

        // atan2(dy, dx) calculates the angle from this alien to the player
        final Velocity bulletVelocityInit = player.getPosition().minus(getPosition());
        final double angle = bulletVelocityInit.getRotation();
        setRotationAngle(angle); // face the player when shooting

        return new AlienBullet(
            getPosition().add(Velocity.fromAngleAndSpeed(angle, getRadius())),
            Velocity.fromAngleAndSpeed(angle, Constants.ALIEN_BULLET_SPEED),
            angle, this
        );
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
        if (targetUpdateTimer <= 0 && player.isAlive()) {
            final double angle = player.getPosition().minus(getPosition()).getRotation();
            setVelocity(Velocity.fromAngleAndSpeed(angle, Constants.ALIEN_SPEED));
            setRotationAngle(angle);
            targetUpdateTimer = Constants.ALIEN_TARGET_UPDATE_INTERVAL;
        }
        // update position according to velocity:
        setPosition(getPosition().add(getVelocity()));
        wrapPosition();
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    @Override
    public void collide(final GameObject gameObject) {
        switch (gameObject) {
            case AlienBullet _ -> {
            }
            case null -> {
            }
            default -> dei();
        }
    }
}
