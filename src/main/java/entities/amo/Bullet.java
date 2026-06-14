package entities.amo;

import entities.Alien;
import entities.Asteroid;
import entities.BossAlien;
import entities.GameObject;
import entities.Player;
import entities.SelfDefendable;
import entities.Updatable;
import entities.motion.Position;
import entities.motion.Velocity;
import utils.Constants;

public interface Bullet  extends Updatable {

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

    default void collide(final GameObject gameObject) {
        switch (gameObject) {
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
