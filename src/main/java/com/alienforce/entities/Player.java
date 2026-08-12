package com.alienforce.entities;

import com.alienforce.assets.AssetManager;
import com.alienforce.assets.ImageKey;
import com.alienforce.assets.SoundEffectKey;
import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.assets.SuperClip;
import com.alienforce.entities.amo.Bullet;
import com.alienforce.game.WorldState;
import com.alienforce.input.InputHandler;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import com.alienforce.utils.Constants;
import com.alienforce.utils.GameLevel;
import com.alienforce.utils.PiConstants;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.alienforce.assets.AssetManager.getImage;

public class Player extends GameObject implements Wrappable, SelfDefendable {

    private final static List<Image> sprites = List.of(
            getImage(ImageKey.SPACE_SHIP_1).get(),
            getImage(ImageKey.SPACE_SHIP_2).get(),
            getImage(ImageKey.SPACE_SHIP_3).get(),
            getImage(ImageKey.SPACE_SHIP_4).get(),
            getImage(ImageKey.SPACE_SHIP_5).get(),
            getImage(ImageKey.SPACE_SHIP_6).get()
    );

    private final WorldState worldState;

    @Getter
    private final InputHandler inputHandler;
    long lastShotTime = 0;
    @Getter
    @Setter
    private int score;

    // CONSTRUCTOR:
    public Player(final WorldState worldState, final Position position, final InputHandler inputHandler) {
        this.worldState = worldState;
        this.inputHandler = inputHandler;
        setPosition(position);
        setVelocity(Velocity.ZERO);
        setRotationAngle(-PiConstants.PID2); // straight up in radians
        setRadius(25);
        setHealth(100);
        setScale(0.5); // make player sprite smaller
        score = 1000000;
    }

    public void incrementScore(final int offset) {
        score += offset;
    }

    @Override
    public void update() {
        // respond to input: thrust (W/Up) and rotation (A/D)
        final Velocity velocity = getVelocity();
        if (inputHandler.isUpPressed()) {
            SoundManager.play(SoundLoopKey.THRUSTER);
            Velocity v = velocity.add(Velocity.fromAngleAndSpeed(getRotationAngle(), Constants.PLAYER_ACCELERATION));
            final double speed = v.getSpeed();
            // Cap speed: scale the velocity vector down to MAX_PLAYER_SPEED
            // while preserving direction
            if (speed > Constants.MAX_PLAYER_SPEED) {
                v = v.scale(Constants.MAX_PLAYER_SPEED / speed);
            }
            setVelocity(v);
        } else {
            SoundManager.stop(SoundLoopKey.THRUSTER);
            // decay velocity when thrust is not pressed
            Velocity v = velocity.scale(Constants.PLAYER_VELOCITY_DECAY);
            if (v.getSpeed() < .01) {
                v = Velocity.ZERO;
            }
            setVelocity(v);
        }
        if (inputHandler.isLeftPressed()) {
            setRotationAngle(getRotationAngle() - Constants.ROTATION_SPEED);
        }
        if (inputHandler.isRightPressed()) {
            setRotationAngle(getRotationAngle() + Constants.ROTATION_SPEED);
        }

        // update position according to velocity:
        setPosition(getPosition().add(velocity));
        wrapPosition();

        // Normalise angle to [0, 2π) to prevent unbounded growth from continuous rotation
        double normalized = getRotationAngle() % (Math.PI * 2);
        if (normalized < 0) {
            normalized += Math.PI * 2;
        }
        setRotationAngle(normalized);
    }

    @Override
    public Stream<Bullet> shoot() {
        final long currentTime = System.currentTimeMillis();
        final GameLevel gameLevel = worldState.gameLevel();

        if (currentTime - lastShotTime < gameLevel.playerShootConstants().shootCooldownFrames()) {
            return Stream.empty();
        }

        lastShotTime = currentTime;
        return shootIgnoreCoolDown();
    }

    public Stream<Bullet> shootIgnoreCoolDown() {
        final double angle = getRotationAngle(); // radians
        final GameLevel gameLevel = worldState.gameLevel();

        AssetManager.getClip(SoundEffectKey.SHOOT)
                .filter(Predicate.not(SuperClip::isRunning))
                .ifPresent(_ -> SoundManager.play(SoundEffectKey.SHOOT));

        final int speed = gameLevel.playerShootConstants().bulletSpeed();
        return switch (gameLevel.levelNumber()) {
            case 0, 1 -> getSingleShoot(getPosition(), getRadius(), speed, angle);
            case 2 -> getSupperShoot(getPosition(), getRadius(), speed, angle);
            case 3, 4, 5, 6 -> getSupperDuperShoot(getPosition(), getRadius(), speed, angle);
            case 7, 8 -> getSupperDuper2Shoot(getPosition(), getRadius(), speed, angle);
            case 9, 10 -> getSupperDuper3Shoot(getPosition(), getRadius(), speed, angle);
            default -> getSupperDuper4Shoot(getPosition(), getRadius(), speed, angle);
        };
    }

    @Override
    public Image getSprite() {
        return sprites.get(Math.clamp((int)(worldState.gameLevel().levelNumber()/2.8), 0, 5));
    }

    @Override
    public void collide(final Collidable collidable) {
        final int health = getHealth();
        switch (collidable) {
            case Player _ -> throw new RuntimeException("PLAYER HIT PLAYER?!?!?");
            case Asteroid _ -> setHealth(Math.max(health - 10, 0));
            case Alien _ -> setHealth(Math.max(health - 40, 0));
            case Bullet bullet when (bullet.getOwner() instanceof Alien) -> setHealth(Math.max(health - 2, 0));
            case Bullet _ -> {
            }
            case null -> {
            }
            default -> die();
        }

        if (isDead()) {
            SoundManager.stop(SoundLoopKey.THRUSTER);
            SoundManager.play(SoundEffectKey.GAME_OVER);
        }
    }
}
