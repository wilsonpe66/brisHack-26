package com.alienforce.entities;

import com.alienforce.entities.amo.Bullet;
import com.alienforce.entities.amo.BulletLevel1;
import com.alienforce.entities.amo.BulletLevel2;
import com.alienforce.entities.amo.BulletLevel3;
import com.alienforce.entities.motion.Position;
import com.alienforce.entities.motion.Velocity;
import java.util.stream.Stream;
import com.alienforce.utils.PiConstants;

public interface SelfDefendable {

    double ANGEL_OFFSET_THETA = .1;

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
                    angle - ANGEL_OFFSET_THETA, this
                ),
                new BulletLevel1(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, radius)),
                    Velocity.fromAngleAndSpeed(angle + ANGEL_OFFSET_THETA, 1.5 * speed),
                    angle + ANGEL_OFFSET_THETA, this
                )
            )
        );
    }

    default Stream<Bullet> getSupperDuperShoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSupperShoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel2(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - PiConstants.PID4, radius)),
                    Velocity.fromAngleAndSpeed(angle - PiConstants.PID4, speed),
                    angle - PiConstants.PID4, this
                ),
                new BulletLevel2(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + PiConstants.PID4, radius)),
                    Velocity.fromAngleAndSpeed(angle + PiConstants.PID4, speed),
                    angle + PiConstants.PID4, this
                )
            )
        );
    }

    default Stream<Bullet> getSupperDuper2Shoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSupperDuperShoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel3(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - PiConstants.PID6, radius)),
                    Velocity.fromAngleAndSpeed(angle - PiConstants.PID6, speed),
                    angle - PiConstants.PID6, this
                ),
                new BulletLevel3(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + PiConstants.PID6, radius)),
                    Velocity.fromAngleAndSpeed(angle + PiConstants.PID6, speed),
                    angle + PiConstants.PID6, this
                )
            )
        );
    }

    default Stream<Bullet> getSupperDuper3Shoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSupperDuper2Shoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel3(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - PiConstants.PID2, radius)),
                    Velocity.fromAngleAndSpeed(angle - PiConstants.PID2, speed / 2),
                    angle - PiConstants.PID2, this
                ),
                new BulletLevel3(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle + PiConstants.PID2, radius)),
                    Velocity.fromAngleAndSpeed(angle + PiConstants.PID2, speed / 2),
                    angle + PiConstants.PID2, this
                )
            )
        );
    }

    default Stream<Bullet> getSupperDuper4Shoot(final Position playerPosition, final double radius, final double speed, final double angle) {
        return Stream.concat(
            getSupperDuper3Shoot(playerPosition, radius, speed, angle),
            Stream.of(
                new BulletLevel3(
                    playerPosition.add(Velocity.fromAngleAndSpeed(angle - Math.PI, radius)),
                    Velocity.fromAngleAndSpeed(angle -  - Math.PI, speed / 2),
                    angle -  Math.PI, this
                )
            )
        );
    }

}
