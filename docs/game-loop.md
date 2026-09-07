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
6. **Removal:** each dead non-bullet object creates an expanding, color-changing `Explosion`; dead world objects and expired effects are then removed from their respective sets.
7. **Level update:** the score selects one of 15 difficulty configurations; transitions play a sound and restore some health.

## Spawning

`AsteroidSpawner`, `AlienSpawner`, and `BossAlienSpawner` choose a random screen edge and create an entity 50 pixels beyond it. Initial velocity points at the player's position at spawn time. Aliens then retarget periodically and wrap across the screen; asteroids keep their initial trajectory and eventually despawn.

`WorldState.Generate` uses wall-clock milliseconds for initial and recurring spawn delays. Each generator has an independent `lastSpawnTime`.

## Scoring and levels

Player-owned bullets award 2 points for hitting an asteroid, 5 for an alien, and 7 for a boss. The HUD displays the internal zero-based level as 1–15.

Level thresholds and all per-level projectile/enemy tuning are documented in [Constants and Tuning](constants.md).

## Reset

Starting a new round clears held input flags, stops the thruster loop, and constructs a new `WorldState` with a new centered player, fresh object/background/explosion sets, level 1, 100 health, zero score, and new spawn timing. `GamePanel`, its `InputHandler`, and the existing `LeaderboardStore` are retained.

From game over, **Retry** starts that fresh world with the currently selected player. **Switch User** creates the fresh world first and then opens the player-selection dialog; canceling the selection does not start the timer.

Normal death records the displayed level and final score before opening the game-over screen. Quitting also records the current level and unfinished score when the player is alive and has scored at least one point.
