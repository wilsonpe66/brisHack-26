package entities;

import entities.amo.Bullet;
import entities.motion.Position;
import entities.motion.Velocity;
import game.SoundManager;
import java.util.List;
import utils.Constants;
import utils.GameLevel;

public class BossAlien extends Alien {

    /**
     * Spawn from side of screen with given position and initial velocity.
     */
    public BossAlien(final Position position, final Velocity velocity, final Player player) {
        super(position, velocity, player);
        setRadius(40);
        setHealth(200);
        setScale(1);
    }

    /**
     * Returns an AlienBullet aimed at the player, or null if on cooldown.
     */
    public List<? extends Bullet> shoot() {
        if (shootCooldown > 0 || noShootTimer > 0 || player.isDead()) {
            return List.of();
        }

        final GameLevel gameLevel = Constants.GAME_LEVELS.get(0);
        shootCooldown = gameLevel.ALIEN_SHOOT_COOLDOWN_FRAMES();

        // atan2(dy, dx) calculates the angle from this alien to the player
        final Position playerPosition = getPosition();
        final Velocity bulletVelocityInit = player.getPosition().minus(playerPosition);
        final double angle = bulletVelocityInit.getRotation();
        setRotationAngle(angle); // face the player when shooting
        return getSupperShoot(playerPosition, getRadius(), gameLevel.ALIEN_BULLET_SPEED(), angle);
    }

    @Override
    public void collide(final GameObject gameObject) {
        final int health = getHealth();
        switch (gameObject) {
            case Bullet bullet when(bullet.getOwner() == this) -> {
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
            SoundManager.playSound("explosion.wav");
        }
    }
}
