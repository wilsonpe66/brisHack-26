package entities;

import entities.amo.Bullet;
import entities.amo.BulletLevel1;
import entities.amo.BulletLevel2;
import entities.motion.Position;
import entities.motion.Velocity;
import java.util.stream.Stream;

public interface SelfDefendable {

    double ANGEL_OFFSET_THETA = .1;
    double ANGEL_OFFSET_PID2 = Math.PI / 2;
    double ANGEL_OFFSET_PID3 = Math.PI / 3;
    double ANGEL_OFFSET_PID4 = Math.PI / 4;
    double ANGEL_OFFSET_PID6 = Math.PI / 6;


    Stream<? extends Bullet> shoot();

    default Stream<Bullet> getSingleShoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.of(
            new BulletLevel1(
                playerPosition.add(Velocity.fromAngleAndSpeed(angle, radius)),
                Velocity.fromAngleAndSpeed(angle, speed),
                angle, this
            )
        );
    }

    default Stream<Bullet> getSupperShoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSingleShoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel1(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, radius)),
                    Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_THETA, 1.5 * speed),
                    angle, this
                ),
                new BulletLevel1(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, radius)),
                    Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, 1.5 * speed),
                    angle, this
                )
            )
        );
    }

    default Stream<Bullet> getSupperDuperShoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSupperShoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel1(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, radius)),
                    Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID4, speed),
                    angle, this
                ),
                new BulletLevel1(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, radius)),
                    Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID4, speed),
                    angle, this
                )
            )
        );
    }

    default Stream<Bullet> getSupperDuper2Shoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSupperDuperShoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel2(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID6, radius)),
                    Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID6, speed),
                    angle, this
                ),
                new BulletLevel2(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID6, radius)),
                    Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID6, speed),
                    angle, this
                )
            )
        );
    }

    default Stream<Bullet> getSupperDuper3Shoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSupperDuper2Shoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel2(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID2, radius)),
                    Velocity.fromAngleAndSpeed(angle - ANGEL_OFFSET_PID2, speed / 2),
                    angle, this
                ),
                new BulletLevel2(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID2, radius)),
                    Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_PID2, speed / 2),
                    angle, this
                )
            )
        );
    }
}
