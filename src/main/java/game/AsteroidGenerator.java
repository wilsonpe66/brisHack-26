package game;

import entities.Asteroid;
import entities.Player;
import entities.motion.Position;
import entities.motion.Velocity;
import java.util.Random;
import utils.Constants;

public class AsteroidGenerator {

    private static final Random random = new Random();
    // Array of lambdas, one per screen edge (top, bottom, left, right).
    // Each spawns an asteroid just outside the visible area on that edge.
    private static final SideSpawner[] SIDES = {
        player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, -Constants.ASTEROID_OFFSET), player), // top
        player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, Constants.HEIGHT + Constants.ASTEROID_OFFSET), player), // bottom
        player -> fromPosition(new Position(-Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player), // left
        player -> fromPosition(new Position(Constants.WIDTH + Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player)  // right
    };
    private final WorldState worldState;

    public AsteroidGenerator(final WorldState worldState) {
        this.worldState = worldState;
    }

    private static Asteroid fromPosition(final Position position, final Player player) {
        // atan2(dy, dx) returns the angle in radians from spawn point to player position
        final double angle = player.getPosition().minus(position).getRotation();
        // Random speed multiplier: 0.7 + [0.0, 0.6) = [0.7x, 1.3x) base speed for variety
        final double speed = Constants.ASTEROID_SPEED * (0.7 + random.nextDouble() * 0.6);
        return new Asteroid(position, Velocity.fromAngleAndSpeed(angle, speed));
    }

    // spawn an asteroid at a random side of the screen
    public void generate() {
        final Asteroid asteroid = SIDES[random.nextInt(SIDES.length)].spawn(worldState.getPlayer());
        worldState.objects.add(asteroid);
        worldState.updatableObjects.add(asteroid);
    }

    // @FunctionalInterface marks an interface with exactly one abstract method,
    // allowing it to be used as a lambda target.
    @FunctionalInterface
    interface SideSpawner {

        Asteroid spawn(final Player player);
    }
}
