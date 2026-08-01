package com.alienforce.game.spawner;

import com.alienforce.entities.GameObject;
import com.alienforce.entities.Player;

/// Marks an interface with exactly one abstract method, allowing it to be used as a lambda target.
@FunctionalInterface
public interface Spawner<SpawnType extends GameObject> {
    SpawnType spawn(final Player player);
}
