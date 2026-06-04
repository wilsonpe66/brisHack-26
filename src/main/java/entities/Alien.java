package entities;

import static assets.AssetManager.getImage;

import game.SoundManager;
import java.awt.Image;
import java.util.List;
import utils.Constants;

public class Alien extends GameObject implements Wrappable, SelfDefendable {

    private final static Image sprite = getImage("shipGreen_manned.png").get();
    protected final Player player;
    /**
     * Frames until the alien can shoot again (slower than player).
     */
    protected int shootCooldown;
    /**
     * Grace period after spawning before the alien starts shooting. Gives the player a brief window to react to a new alien.
     */
    protected int noShootTimer;
    /**
     * Frames until we next recalculate velocity direction towards the player. This makes alien movement "steppy" rather than perfectly smooth tracking.
     */
    protected int targetUpdateTimer;

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
    @Override
    public List<? extends Bullet> shoot() {
        if (shootCooldown > 0 || noShootTimer > 0 || player.isDead()) {
            return List.of();
        }

        shootCooldown = Constants.ALIEN_SHOOT_COOLDOWN_FRAMES;

        // atan2(dy, dx) calculates the angle from this alien to the player
        final Position playerPosition = getPosition();
        final Velocity bulletVelocityInit = player.getPosition().minus(playerPosition);
        final double angle = bulletVelocityInit.getRotation();
        setRotationAngle(angle); // face the player when shooting

        return List.of(
            new AlienBullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, getRadius())),
                Velocity.fromAngleAndSpeed(angle, Constants.ALIEN_BULLET_SPEED),
                angle, this
            )
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
            case AlienBullet _ -> setHealth(getHealth() - 2);
            case null -> {
            }
            default -> {
                dei();
            }
        }

        if (isDead()) {
            SoundManager.playSound("explosion.wav");
        }
    }
}
