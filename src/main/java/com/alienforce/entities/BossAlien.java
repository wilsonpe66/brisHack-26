package com.alienforce.entities;

import static com.alienforce.assets.AssetManager.getImage;

import com.alienforce.assets.ImageKey;
import com.alienforce.assets.SoundEffectKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.entities.amo.Bullet;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import com.alienforce.game.WorldState;
import java.awt.Image;
import java.util.List;
import java.util.stream.Stream;
import com.alienforce.utils.GameLevel;
import com.alienforce.utils.ShootConstants;

public class BossAlien extends Alien {

    private final static List<Image> sprites = List.of(
            getImage(ImageKey.ALIEN_BOSS_1).get(),
            getImage(ImageKey.ALIEN_BOSS_2).get(),
            getImage(ImageKey.ALIEN_BOSS_3).get(),
            getImage(ImageKey.ALIEN_BOSS_4).get(),
            getImage(ImageKey.ALIEN_BOSS_5).get()
    );

    final GameLevel gameLevel;

    /**
     * Spawn from side of screen with given position and initial velocity.
     */
    public BossAlien(final WorldState worldState, final Position position, final Velocity velocity, final Player player) {
        super(worldState, position, velocity, player);
        setRadius(60);
        setHealth(200);
        setScale(.2);
        gameLevel = worldState.gameLevel();
    }

    @Override
    public Image getSprite() {
        return sprites.get(Math.clamp((int)(worldState.gameLevel().levelNumber()/3.4), 0, 4));
    }

    /**
     * Returns an AlienBullet aimed at the player, or null if on cooldown.
     */
    public Stream<? extends Bullet> shoot() {
        if (shootCooldown > 0 || noShootTimer > 0 || player.isDead()) {
            return Stream.of();
        }

        final ShootConstants shootConstants = gameLevel.bossAlien().shootConstants();
        shootCooldown = shootConstants.shootCooldownFrames();

        // atan2(dy, dx) calculates the angle from this alien to the player
        final Position playerPosition = getPosition();
        final Velocity bulletVelocityInit = player.getPosition().minus(playerPosition);
        final double angle = bulletVelocityInit.getRotation();
        setRotationAngle(angle); // face the player when shooting
        final double bulletSpeed = shootConstants.bulletSpeed();
        return switch (gameLevel.levelNumber()) {
            case 0,1,2,3,4,5 -> getSupperShoot(playerPosition, getRadius(), bulletSpeed, angle);
            case 6,7,8 -> getSupperDuperShoot(playerPosition, getRadius(), bulletSpeed, angle);
            case 9,10,11 ->getSupperDuper2Shoot(playerPosition, getRadius(), bulletSpeed, angle);
            case 12, 13, 14 -> getSupperDuper3Shoot(playerPosition, getRadius(), bulletSpeed, angle);
            default -> getSupperDuper4Shoot(playerPosition, getRadius(), bulletSpeed, angle);
        };
    }

    @Override
    public void collide(final Collidable collidable) {
        final int health = getHealth();
        switch (collidable) {
            case Bullet bullet when (bullet.getOwner() == this) -> {
            }
            case Bullet bullet when (bullet.getOwner() instanceof Player) -> setHealth(health - 20);
            case Bullet bullet when (bullet.getOwner() instanceof Alien) -> setHealth(health - 2);
            case Asteroid _ -> setHealth(health - 50);
            case Alien _ -> setHealth(health - 10);
            case null -> {
            }
            default -> die();
        }

        if (isDead()) {
            SoundManager.play(SoundEffectKey.EXPLOSION);
        }
    }
}
