# Game Loop and World State

`GamePanel` owns a Swing `Timer` configured with a 16 ms delay. Each timer event calls `WorldState.updateState()`, transitions to game over if the player is dead, and repaints the panel.

## Update order

Each world update first polls the gameplay gamepad, edge-detects pause and mute inputs, and pauses or resumes looping audio. A paused world returns before simulation work. `Game` can also call `WorldState.pause()` when the application window loses focus during an active, nonzero-score game.

An active update runs these phases:

```text
shooting → alien shooting → spawning → entity updates → collisions → removal → level update
```

1. **Shooting:** normal or cooldown-free player projectile patterns are added to both the render/collision and update sets.
2. **Alien shooting:** every live `Alien`, including `BossAlien`, may return projectiles. New bullets are collected before insertion to avoid modifying the set during iteration.
3. **Spawning:** asteroids use the global one-second interval. Normal and boss aliens each use the current level's initial delay and recurring interval.
4. **Updates:** background objects and active world objects run `update()`.
5. **Collisions:** living objects are checked pairwise in O(n²) time using circle radii; on overlap, each object receives the other object's collision response.
6. **Removal:** dead objects are removed from the world and update sets.
7. **Level update:** the score selects one of 15 difficulty configurations; transitions play a sound and restore some health.

## Spawning

`AsteroidSpawner`, `AlienSpawner`, and `BossAlienSpawner` choose a random screen edge and create an entity 50 pixels beyond it. Initial velocity points at the player's position at spawn time. Aliens then retarget periodically and wrap across the screen; asteroids keep their initial trajectory and eventually despawn.

`WorldState.Generate` uses wall-clock milliseconds for initial and recurring spawn delays. Each generator has an independent `lastSpawnTime`.

## Scoring and levels

Player-owned bullets award 2 points for hitting an asteroid, 5 for an alien, and 7 for a boss. The HUD displays the internal zero-based level as 1–15.

Level thresholds and all per-level projectile/enemy tuning are documented in [Constants and Tuning](constants.md).

## Reset

Starting a new round clears world objects, restores the player to the center with 100 health and zero score, resets level/pause state and input flags, and restarts timing. The same `Player`, `WorldState`, and `LeaderboardStore` instances are reused.

Normal death records the final score before opening the game-over screen. Quitting also records an unfinished score when the player is alive and has scored at least one point.
