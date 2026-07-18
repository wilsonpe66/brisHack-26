package com.alienforce.entities.amo;

import com.alienforce.entities.Alien;
import com.alienforce.entities.Asteroid;
import com.alienforce.entities.BossAlien;
import com.alienforce.entities.Collidable;
import com.alienforce.entities.Player;
import com.alienforce.entities.SelfDefendable;
import com.alienforce.entities.Updatable;
import com.alienforce.entities.motion.Position;
import com.alienforce.entities.motion.Velocity;
import com.alienforce.utils.Constants;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public interface Bullet extends Updatable, Collidable {
    Position getPosition();

    void setPosition(final Position position);

    Velocity getVelocity();

    void die();

    SelfDefendable getOwner();

    @Override
    default void update() {
        // update position according to velocity:
        setPosition(getPosition().add(getVelocity()));

        if (getPosition() instanceof Position(var x, var y)) {
            if (x < 0 || x > Constants.WIDTH
                || y < 0 || y > Constants.HEIGHT) {
                die();
            }
        }
    }

    @Override
    default void collide(final Collidable collidable) {
        switch (collidable) {
            case SelfDefendable selfDefendable when (getOwner() == selfDefendable) -> {

            }
            case BossAlien _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(7);
                die();
            }
            case Alien _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(5);
                die();
            }
            case Asteroid _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(2);
                die();
            }

            case Bullet otherBullet when (getOwner() == otherBullet.getOwner()) -> {
            }

            case null -> {

            }
            default -> die();
        }
    }
}
