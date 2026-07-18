package com.alienforce.entities;

import static com.alienforce.assets.AssetManager.getImage;

import com.alienforce.assets.ImageKey;
import com.alienforce.assets.SoundEffectKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.entities.amo.Bullet;
import com.alienforce.entities.motion.Position;
import com.alienforce.entities.motion.Velocity;
import com.alienforce.game.WorldState;
import java.awt.Image;
import java.util.stream.Stream;
import com.alienforce.utils.GameLevel;

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
        noShootTimer = worldState.gameLevel().ALIEN_SPAWN_NO_SHOOT_FRAMES();
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
        shootCooldown = gameLevel.ALIEN_SHOOT_COOLDOWN_FRAMES();

        // atan2(dy, dx) calculates the angle from this alien to the player
        final Position playerPosition = getPosition();
        final Velocity bulletVelocityInit = player.getPosition().minus(playerPosition);
        final double angle = bulletVelocityInit.getRotation();
        setRotationAngle(angle); // face the player when shooting

        return getSingleShoot(playerPosition, getRadius(), gameLevel.ALIEN_BULLET_SPEED(), angle);
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
            setVelocity(Velocity.fromAngleAndSpeed(angle, gameLevel.ALIEN_SPEED()));
            setRotationAngle(angle);
            targetUpdateTimer = gameLevel.ALIEN_TARGET_UPDATE_INTERVAL();
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
