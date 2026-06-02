package entities;

import java.util.List;
import utils.Constants;

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

        shootCooldown = Constants.ALIEN_SHOOT_COOLDOWN_FRAMES;

        // atan2(dy, dx) calculates the angle from this alien to the player
        final Position playerPosition = getPosition();
        final Velocity bulletVelocityInit = player.getPosition().minus(playerPosition);
        final double angle = bulletVelocityInit.getRotation();
        setRotationAngle(angle); // face the player when shooting

        return List.of(
            new AlienBullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - .1, getRadius())),
                Velocity.fromAngleAndSpeed(angle - .1, Constants.ALIEN_BULLET_SPEED),
                angle, this
            ),
            new AlienBullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, getRadius())),
                Velocity.fromAngleAndSpeed(angle, Constants.ALIEN_BULLET_SPEED),
                angle, this
            ),
            new AlienBullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + .1, getRadius())),
                Velocity.fromAngleAndSpeed(angle + .1, Constants.ALIEN_BULLET_SPEED),
                angle, this
            )
        );
    }

    @Override
    public void collide(final GameObject gameObject) {
         final int health = getHealth();
        switch (gameObject) {
            case AlienBullet _ -> setHealth(health - 2);
            case Asteroid _ -> setHealth(health - 50);
            case PlayerBullet _ -> setHealth(health - 20);
            case Alien _ -> setHealth(health - 10);
            case null -> {
            }
            default -> dei();
        }
    }
}
