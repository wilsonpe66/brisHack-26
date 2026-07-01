package com.alienforce.entities.amo;

import com.alienforce.entities.Alien;
import com.alienforce.entities.Asteroid;
import com.alienforce.entities.BossAlien;
import com.alienforce.entities.Colidable;
import com.alienforce.entities.Player;
import com.alienforce.entities.SelfDefendable;
import com.alienforce.entities.Updatable;
import com.alienforce.entities.motion.Position;
import com.alienforce.entities.motion.Velocity;
import com.alienforce.utils.Constants;

public interface Bullet extends Updatable, Colidable {

    Position getPosition();

    void setPosition(final Position position);

    Velocity getVelocity();

    void dei();

    SelfDefendable getOwner();

    @Override
    default void update() {
        // update position according to velocity:
        setPosition(getPosition().add(getVelocity()));

        if (getPosition() instanceof Position(var x, var y)) {
            if (x < 0 || x > Constants.WIDTH
                || y < 0 || y > Constants.HEIGHT) {
                dei();
            }
        }
    }

    @Override
    default void collide(final Colidable colidable) {
        switch (colidable) {
            case SelfDefendable selfDefendable when (getOwner() == selfDefendable) -> {

            }
            case BossAlien _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(7);
                dei();
            }
            case Alien _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(5);
                dei();
            }
            case Asteroid _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(2);
                dei();
            }

            case Bullet otherBullet when (getOwner() == otherBullet.getOwner()) -> {
            }

            case null -> {

            }
            default -> dei();
        }
    }
}
