package com.alienforce.game.spawner;

import com.alienforce.entities.Alien;
import com.alienforce.entities.BossAlien;
import com.alienforce.entities.Player;
import com.alienforce.game.WorldState;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;

public class BossAlienSpawner extends AlienSpawner {

    public BossAlienSpawner(WorldState worldState) {
        super(worldState);
    }

    @Override
    protected Alien fromPosition(final Position position, final Player player) {
        final double angle = player.getPosition().minus(position).getRotation();
        final double alienSpeed = worldState.gameLevel().bossAlien().speed();
        return new BossAlien(worldState, position, Velocity.fromAngleAndSpeed(angle, alienSpeed), player);
    }
}
