import java.util.Random;

public class AsteroidGenerator {
    private static final Random random = new Random();

    @FunctionalInterface
    interface SideSpawner {
        Asteroid spawn(double width, double height, double offset, double playerX, double playerY);
    }

    // spawns an asteroid at an offset from one of the sides of the screen, with velocity towards the player position
    private static final SideSpawner[] SIDES = {
            (width, height, offset, px, py) -> fromPosition(random.nextDouble() * width, -offset, px, py), // top
            (width, height, offset, px, py) -> fromPosition(random.nextDouble() * width,   height + offset,         px, py), // bottom
            (width, height, offset, px, py) -> fromPosition(-offset, random.nextDouble() * height, px, py), // left
            (width, height, offset, px, py) -> fromPosition(width + offset, random.nextDouble() * height, px, py)  // right
    };

    private final WorldState worldState;

    public AsteroidGenerator(WorldState worldState) {
        this.worldState = worldState;
    }

    private static Asteroid fromPosition(double x, double y, double playerX, double playerY) {
        double angle = Math.atan2(playerY - y, playerX - x);
        return new Asteroid(x, y, Math.cos(angle) * Constants.ASTEROID_SPEED, Math.sin(angle) * Constants.ASTEROID_SPEED);
    }

    // spawn an asteroid at a random side of the screen
    public void generate() {
        Asteroid asteroid = SIDES[random.nextInt(SIDES.length)].spawn(Constants.WIDTH, Constants.HEIGHT, Constants.ASTEROID_OFFSET, worldState.getPlayer().getPositionX(), worldState.getPlayer().getPositionY());
        worldState.objects.add(asteroid);
        worldState.updatables.add(asteroid);
    }
}
