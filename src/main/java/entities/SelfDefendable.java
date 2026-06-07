package entities;

import entities.amo.Bullet;
import entities.motion.Position;
import entities.motion.Velocity;
import java.util.List;

public interface SelfDefendable {

    List<? extends Bullet> shoot();

    default List<Bullet> getSingleShoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return List.of(
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, radius)),
                Velocity.fromAngleAndSpeed(angle, speed),
                angle, this
            )
        );
    }

    default List<Bullet> getSupperShoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return List.of(
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - .1, radius)),
                Velocity.fromAngleAndSpeed(angle - .1, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, radius)),
                Velocity.fromAngleAndSpeed(angle, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + .1, radius)),
                Velocity.fromAngleAndSpeed(angle + .1, 1.5 * speed),
                angle, this
            )
        );
    }
}
