package game;

import entities.Asteroid;
import utils.Constants;

import java.util.Random;

public class AsteroidGenerator {
    private static final Random random = new Random();

    // @FunctionalInterface marks an interface with exactly one abstract method,
    // allowing it to be used as a lambda target.
    @FunctionalInterface
    interface SideSpawner {
        Asteroid spawn(double width, double height, double offset, double playerX, double playerY);
    }

    // Array of lambdas, one per screen edge (top, bottom, left, right).
    // Each spawns an asteroid just outside the visible area on that edge.
    private static final SideSpawner[] SIDES = {
            (width, height, offset, px, py) -> fromPosition(random.nextDouble() * width, -offset, px, py), // top
            (width, height, offset, px, py) -> fromPosition(random.nextDouble() * width, height + offset, px, py), // bottom
            (width, height, offset, px, py) -> fromPosition(-offset, random.nextDouble() * height, px, py), // left
            (width, height, offset, px, py) -> fromPosition(width + offset, random.nextDouble() * height, px, py)  // right
    };

    private final WorldState worldState;

    public AsteroidGenerator(WorldState worldState) {
        this.worldState = worldState;
    }

    private static Asteroid fromPosition(double x, double y, double playerX, double playerY) {
        // atan2(dy, dx) returns the angle in radians from spawn point to player position
        double angle = Math.atan2(playerY - y, playerX - x);
        // Random speed multiplier: 0.7 + [0.0, 0.6) = [0.7x, 1.3x) base speed for variety
        double speed = Constants.ASTEROID_SPEED * (0.7 + random.nextDouble() * 0.6);
        return new Asteroid(x, y, Math.cos(angle) * speed, Math.sin(angle) * speed);
    }

    // spawn an asteroid at a random side of the screen
    public void generate() {
        Asteroid asteroid = SIDES[random.nextInt(SIDES.length)].spawn(Constants.WIDTH, Constants.HEIGHT, Constants.ASTEROID_OFFSET, worldState.getPlayer().getPositionX(), worldState.getPlayer().getPositionY());
        worldState.objects.add(asteroid);
        worldState.updatables.add(asteroid);
    }
}
