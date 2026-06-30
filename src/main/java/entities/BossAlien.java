package entities;

import static assets.AssetManager.getImage;

import assets.ImageKey;
import assets.SoundEffectKey;
import assets.SoundManager;
import entities.amo.Bullet;
import entities.motion.Position;
import entities.motion.Velocity;
import game.WorldState;
import java.awt.Image;
import java.util.stream.Stream;
import utils.GameLevel;

public class BossAlien extends Alien {

    private final static Image sprite = getImage(ImageKey.ALIEN_BOSS).get();

    /**
     * Spawn from side of screen with given position and initial velocity.
     */
    public BossAlien(final WorldState worldState, final Position position, final Velocity velocity, final Player player) {
        super(worldState, position, velocity, player);
        setRadius(60);
        setHealth(200);
        setScale(.2);
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    /**
     * Returns an AlienBullet aimed at the player, or null if on cooldown.
     */
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
        return getSupperShoot(playerPosition, getRadius(), gameLevel.ALIEN_BULLET_SPEED(), angle);
    }

    @Override
    public void collide(final Colidable colidable) {
        final int health = getHealth();
        switch (colidable) {
            case Bullet bullet when (bullet.getOwner() == this) -> {
            }
            case Bullet bullet when (bullet.getOwner() instanceof Player) -> setHealth(health - 20);
            case Bullet bullet when (bullet.getOwner() instanceof Alien) -> setHealth(health - 2);
            case Asteroid _ -> setHealth(health - 50);
            case Alien _ -> setHealth(health - 10);
            case null -> {
            }
            default -> dei();
        }

        if (isDead()) {
            SoundManager.play(SoundEffectKey.EXPLOSION);
        }
    }
}
