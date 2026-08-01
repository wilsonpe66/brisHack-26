package com.alienforce.game.spawner;

import com.alienforce.entities.Asteroid;
import com.alienforce.entities.Player;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import com.alienforce.utils.Constants;

import java.util.List;
import java.util.Random;

public class AsteroidSpawner implements Spawner<Asteroid> {

    private static final Random random = new Random();
    // Array of lambdas, one per screen edge (top, bottom, left, right).
    // Each spawns an asteroid just outside the visible area on that edge.
    private static final List<Spawner<Asteroid>> SIDES = List.of(
            player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, -Constants.ASTEROID_OFFSET), player), // top
            player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, Constants.HEIGHT + Constants.ASTEROID_OFFSET), player), // bottom
            player -> fromPosition(new Position(-Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player), // left
            player -> fromPosition(new Position(Constants.WIDTH + Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player)  // right
    );

    private static Asteroid fromPosition(final Position position, final Player player) {
        // atan2(dy, dx) returns the angle in radians from spawn point to player position
        final double angle = player.getPosition().minus(position).getRotation();
        // Random speed multiplier: 0.7 + [0.0, 0.6) = [0.7x, 1.3x) base speed for variety
        final double speed = Constants.ASTEROID_SPEED * (0.7 + random.nextDouble() * 0.6);
        return new Asteroid(position, Velocity.fromAngleAndSpeed(angle, speed));
    }

    // spawn an asteroid at a random side of the screen
    @Override
    public Asteroid spawn(final Player player) {
        return SIDES.get(random.nextInt(SIDES.size())).spawn(player);
    }
}
