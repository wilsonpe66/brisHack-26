package entities;

import entities.amo.Bullet;
import entities.motion.Position;
import entities.motion.Velocity;
import java.util.List;

public interface SelfDefendable {
    double ANGEL_OFFSET_THETA = .1;
    double ANGEL_OFFSET_PID2 = Math.PI / 2;
    double ANGEL_OFFSET_PID3 = Math.PI / 3;
    double ANGEL_OFFSET_PID4 = Math.PI / 4;
    double ANGEL_OFFSET_PID6 = Math.PI / 6;


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
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, radius)),
                Velocity.fromAngleAndSpeed(angle, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            )
        );
    }

    default List<Bullet> getSupperDuperShoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return List.of(
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, radius)),
                Velocity.fromAngleAndSpeed(angle, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, speed),
                angle, this
            )
        );
    }

    default List<Bullet> getSupperDuper2Shoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return List.of(
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID6, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID6, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, radius)),
                Velocity.fromAngleAndSpeed(angle, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID6, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID6, speed),
                angle, this
            )
        );
    }

    default List<Bullet> getSupperDuper3Shoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return List.of(
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID2, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID2, speed / 2),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID6, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID6, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, radius)),
                Velocity.fromAngleAndSpeed(angle, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, 1.5 * speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID6, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID6, speed),
                angle, this
            ),
            new Bullet(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID2, radius)),
                Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID2, speed / 2),
                angle, this
            )
        );
    }
}
