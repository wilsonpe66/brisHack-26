import java.util.List;
import java.util.Random;

public class AsteroidGenerator {
    private static final Random random = new Random();

    @FunctionalInterface
    interface SideSpawner {
        Asteroid spawn(double width, double height, double offset);
    }

    private static final SideSpawner[] SIDES = {
            (w, h, o) -> new Asteroid(random.nextDouble() * w, -o), // top
            (w, h, o) -> new Asteroid(random.nextDouble() * w, h + o), // bottom
            (w, h, o) -> new Asteroid(-o, random.nextDouble() * h), // left
            (w, h, o) -> new Asteroid(w + o, random.nextDouble() * h)  // right
    };

    private WorldState worldState;

    public AsteroidGenerator(WorldState worldState) {
        this.worldState = worldState;
    }

    public void generate(List<GameObject> objects) {
        Asteroid asteroid = SIDES[random.nextInt(SIDES.length)].spawn(Constants.WIDTH, Constants.HEIGHT, Constants.ASTEROID_OFFSET);
        worldState.objects.add(asteroid);
        worldState.updatables.add(asteroid);
    }
}
