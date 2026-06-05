package game;

import entities.Alien;
import entities.BossAlien;
import entities.Player;
import entities.motion.Position;
import entities.motion.Velocity;
import java.util.Random;
import utils.Constants;

public class AlienGenerator {

    private static final Random random = new Random();
    // spawns an alien at an offset from one of the sides of the screen, with velocity towards the player position
    private static final SideSpawner[] SIDES = {
        player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, -Constants.ASTEROID_OFFSET), player), // top
        player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, Constants.HEIGHT + Constants.ASTEROID_OFFSET), player), // bottom
        player -> fromPosition(new Position(-Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player), // left
        player -> fromPosition(new Position(Constants.WIDTH + Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player)  // right
    };
    private final WorldState worldState;

    public AlienGenerator(WorldState worldState) {
        this.worldState = worldState;
    }

    private static Alien fromPosition(final Position position, final Player player) {
        final double angle = player.getPosition().minus(position).getRotation();
        final double alienSpeed = Constants.GAME_LEVELS.get(0).ALIEN_SPEED();
        if (random.nextBoolean()) {
            return new Alien(position, Velocity.fromAngleAndSpeed(angle, alienSpeed), player);
        }
        return new BossAlien(position, Velocity.fromAngleAndSpeed(angle, alienSpeed), player);
    }

    // spawn an alien at a random side of the screen
    public void generate() {
        final Player player = worldState.getPlayer();
        final Alien alien = SIDES[random.nextInt(SIDES.length)].spawn(player);
        worldState.objects.add(alien);
        worldState.updatableObjects.add(alien);
    }

    @FunctionalInterface
    interface SideSpawner {

        Alien spawn(final Player player);
    }
}
