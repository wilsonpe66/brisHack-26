import java.util.Random;

public class AlienGenerator {
    private static final Random random = new Random();

    @FunctionalInterface
    interface SideSpawner {
        Alien spawn(double width, double height, double offset, double playerX, double playerY, Player player);
    }

    // spawns an alien at an offset from one of the sides of the screen, with velocity towards the player position
    private static final SideSpawner[] SIDES = {
            (width, height, offset, px, py, player) -> fromPosition(random.nextDouble() * width, -offset, px, py, player), // top
            (width, height, offset, px, py, player) -> fromPosition(random.nextDouble() * width, height + offset, px, py, player), // bottom
            (width, height, offset, px, py, player) -> fromPosition(-offset, random.nextDouble() * height, px, py, player), // left
            (width, height, offset, px, py, player) -> fromPosition(width + offset, random.nextDouble() * height, px, py, player)  // right
    };

    private final WorldState worldState;

    public AlienGenerator(WorldState worldState) {
        this.worldState = worldState;
    }

    private static Alien fromPosition(double x, double y, double playerX, double playerY, Player player) {
        double angle = Math.atan2(playerY - y, playerX - x);
        return new Alien(x, y, Math.cos(angle) * Constants.ALIEN_SPEED, Math.sin(angle) * Constants.ALIEN_SPEED, player);
    }

    // spawn an alien at a random side of the screen
    public void generate() {
        Player player = worldState.getPlayer();
        Alien alien = SIDES[random.nextInt(SIDES.length)].spawn(Constants.WIDTH, Constants.HEIGHT, Constants.ASTEROID_OFFSET, player.getPositionX(), player.getPositionY(), player);
        worldState.objects.add(alien);
        worldState.updatables.add(alien);
    }
}
