package com.alienforce.entities;

import com.alienforce.assets.ImageKey;
import com.alienforce.assets.SoundEffectKey;
import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.entities.amo.Bullet;
import com.alienforce.entities.motion.Position;
import com.alienforce.entities.motion.Velocity;
import com.alienforce.game.InputHandler;
import com.alienforce.game.WorldState;
import lombok.Getter;
import lombok.Setter;
import com.alienforce.utils.Constants;
import com.alienforce.utils.GameLevel;

import java.awt.*;
import java.util.stream.Stream;

import static com.alienforce.assets.AssetManager.getImage;

public class Player extends GameObject implements Wrappable, SelfDefendable {

    private final static Image sprite = getImage(ImageKey.SPACE_SHIP).get();
    private final static Image sprite2 = getImage(ImageKey.SPACE_SHIP_2).get();

    private final WorldState worldState;

    @Getter
    private final InputHandler inputHandler;
    @Getter
    @Setter
    private int score;

    // CONSTRUCTOR:
    public Player(final WorldState worldState, final Position position, final InputHandler inputHandler) {
        this.worldState = worldState;
        this.inputHandler = inputHandler;
        setPosition(position);
        setVelocity(Velocity.ZERO);
        setRotationAngle(-Math.PI / 2); // straight up in radians
        setRadius(25);
        setHealth(100);
        setScale(0.5); // make player sprite smaller
        score = 0;
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
        final double angle = getRotationAngle(); // radians
        final GameLevel gameLevel = worldState.gameLevel();
        final int speed = gameLevel.PLAYER_BULLET_SPEED();
        return switch (gameLevel.LEVEL_NUMBER()) {
            case 0, 1 -> getSingleShoot(getPosition(), getRadius(), speed, angle);
            case 2 -> getSupperShoot(getPosition(), getRadius(), speed, angle);
            case 3, 4, 5, 6 -> getSupperDuperShoot(getPosition(), getRadius(), speed, angle);
            case 7, 8 -> getSupperDuper2Shoot(getPosition(), getRadius(), speed, angle);
            case 9 -> getSupperDuper3Shoot(getPosition(), getRadius(), speed, angle);
            default -> getSupperDuper4Shoot(getPosition(), getRadius(), speed, angle);
        };
    }

    @Override
    public Image getSprite() {
        if (worldState.gameLevel().LEVEL_NUMBER() > 4) {
            return sprite2;
        }
        return sprite;
    }

    @Override
    public void collide(final Colidable colidable) {
        final int health = getHealth();
        switch (colidable) {
            case Player _ -> throw new RuntimeException("PLAYER HIT PLAYER?!?!?");
            case Asteroid _ -> setHealth(Math.max(health - 10, 0));
            case Bullet bullet when (bullet.getOwner() instanceof Alien) -> setHealth(Math.max(health - 2, 0));
            case null -> {
            }
            default -> dei();
        }

        if (isDead()) {
            SoundManager.stop(SoundLoopKey.THRUSTER);
            SoundManager.play(SoundEffectKey.GAME_OVER);
        }
    }
}
