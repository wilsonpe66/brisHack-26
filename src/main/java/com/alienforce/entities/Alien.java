package com.alienforce.entities;

import com.alienforce.assets.ImageKey;
import com.alienforce.assets.SoundEffectKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.entities.amo.Bullet;
import com.alienforce.game.WorldState;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import com.alienforce.utils.AlienConstants;
import com.alienforce.utils.GameLevel;
import com.alienforce.utils.ShootConstants;

import java.awt.Image;
import java.util.stream.Stream;

import static com.alienforce.assets.AssetManager.getImage;

public class Alien extends GameObject implements Wrappable, SelfDefendable {

    private final static Image sprite = getImage(ImageKey.ALIEN).get();

    protected final WorldState worldState;

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
    public Alien(final WorldState worldState, final Position position, final Velocity velocity, final Player player) {
        super();
        this.worldState = worldState;
        this.player = player;
        setPosition(position);
        setVelocity(velocity);
        setRotationAngle(velocity.getRotation());
        setRadius(20);
        setHealth(100);
        setScale(0.5);
        shootCooldown = 0;
        noShootTimer = worldState.gameLevel().alien().shootConstants().spawnNoShootFrames();
        targetUpdateTimer = 0;
    }

    /**
     * Returns an AlienBullet aimed at the player, or null if on cooldown.
     */
    @Override
    public Stream<? extends Bullet> shoot() {
        if (shootCooldown > 0 || noShootTimer > 0 || player.isDead()) {
            return Stream.of();
        }

        final GameLevel gameLevel = worldState.gameLevel();
        final ShootConstants shootConstants = gameLevel.alien().shootConstants();
        shootCooldown = shootConstants.shootCooldownFrames();

        // atan2(dy, dx) calculates the angle from this alien to the player
        final Position playerPosition = getPosition();
        final Velocity bulletVelocityInit = player.getPosition().minus(playerPosition);
        final double angle = bulletVelocityInit.getRotation();
        setRotationAngle(angle); // face the player when shooting

        return getSingleShoot(playerPosition, getRadius(), shootConstants.bulletSpeed(), angle);
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
            final GameLevel gameLevel = worldState.gameLevel();
            final AlienConstants alienConstants = gameLevel.alien();
            setVelocity(Velocity.fromAngleAndSpeed(angle, alienConstants.speed()));
            setRotationAngle(angle);
            targetUpdateTimer = alienConstants.targetUpdateInterval();
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
    public void collide(final Collidable collidable) {
        switch (collidable) {
            case Bullet bullet when (bullet.getOwner() == this) -> {
            }
            case null -> {
            }
            default -> die();
        }

        if (isDead()) {
            SoundManager.play(SoundEffectKey.EXPLOSION);
        }
    }
}
