package com.alienforce.game.spawner;

import com.alienforce.entities.Alien;
import com.alienforce.entities.Player;
import com.alienforce.game.WorldState;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import com.alienforce.utils.Constants;

import java.util.List;
import java.util.Random;

public class AlienSpawner implements Spawner<Alien> {

    private static final Random random = new Random();
    protected final WorldState worldState;
    /// spawns an alien at an offset from one of the sides of the screen, with velocity towards the player position
    private final List<Spawner<Alien>> SIDES = List.of(
            player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, -Constants.ASTEROID_OFFSET), player), // top
            player -> fromPosition(new Position(random.nextDouble() * Constants.WIDTH, Constants.HEIGHT + Constants.ASTEROID_OFFSET), player), // bottom
            player -> fromPosition(new Position(-Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player), // left
            player -> fromPosition(new Position(Constants.WIDTH + Constants.ASTEROID_OFFSET, random.nextDouble() * Constants.HEIGHT), player)  // right
    );

    public AlienSpawner(WorldState worldState) {
        this.worldState = worldState;
    }

    protected Alien fromPosition(final Position position, final Player player) {
        final double angle = player.getPosition().minus(position).getRotation();
        final double alienSpeed = worldState.gameLevel().alien().speed();
        return new Alien(worldState, position, Velocity.fromAngleAndSpeed(angle, alienSpeed), player);
    }

    /// spawn an alien at a random side of the screen
    @Override
    public Alien spawn(final Player player) {
        return SIDES.get(random.nextInt(SIDES.size())).spawn(player);
    }

}
