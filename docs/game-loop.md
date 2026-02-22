# Game Loop & World State

## The Game Timer

`GamePanel` uses a Swing `Timer` that fires every `FRAME_DELAY` ms (1000 / 60 ≈ 16 ms → **60 FPS**).

Each tick calls `actionPerformed()`, which:

1. Calls `worldState.updateState()` — the core simulation step.
2. Checks if the player is dead; if so, stops the timer and transitions to the Game Over screen.
3. Calls `repaint()` to redraw the frame.

## `WorldState.updateState()` — One Frame

The update runs six phases **in order**:

```
handleShooting()  →  handleAlienShooting()  →  handleSpawning()  →  updateAll()  →  handleCollisions()  →  removeDeadObjects()
```

### 1. Handle Shooting
- If the shoot cooldown has expired **and** the space bar is held **and** the player is alive, a `Bullet` is created at the player's nose and added to the world.
- The cooldown resets to `SHOOT_COOLDOWN_FRAMES` (15 frames = 0.25 s).

### 2. Handle Alien Shooting
- Iterates over all living `Alien` objects and calls `alien.shoot()`.
- Each alien manages its own shoot cooldown (`ALIEN_SHOOT_COOLDOWN_FRAMES` = 150 frames ≈ 2.5 s) and spawn grace period (`ALIEN_SPAWN_NO_SHOOT_FRAMES` = 120 frames ≈ 2 s).
- If `shoot()` returns an `AlienBullet`, it is added to the world.

### 3. Handle Spawning
- **Asteroids**: uses `System.currentTimeMillis()` to check if `SPAWN_DELAY` (1000 ms) has passed since the last asteroid spawn. Delegates to `AsteroidGenerator.generate()`.
- **Aliens**: after an initial delay of `ALIEN_SPAWN_INITIAL_DELAY` (5 s) from the start of the game, a new alien spawns every `ALIEN_SPAWN_DELAY` (15 s). Delegates to `AlienGenerator.generate()`.

### 4. Update All
- Iterates over every `Updatable` and calls `update()`.
- Each entity moves itself according to its own velocity.
- The player also handles thrust, rotation, screen-wrapping, and death checks.
- Aliens periodically re-aim towards the player and also wrap around screen edges.
- Asteroids that fly too far off-screen mark themselves as dead.
- Bullets and alien bullets that leave the screen mark themselves as dead.

### 5. Handle Collisions
- A brute-force **O(n²)** pairwise check using **circle–circle distance**.
- Two objects collide when the distance between their centres is ≤ the sum of their radii.
- Collision response uses **double dispatch** (see [Entities](entities.md#double-dispatch-collisions)).

### 6. Remove Dead Objects
- Any asteroid with `health ≤ 0` is marked dead.
- The score is incremented only for asteroids that were **killed by a bullet** (not those that flew off-screen or were destroyed by another asteroid).
- All dead objects are removed from the `objects` and `updatables` lists.

## Asteroid Spawning Details

`AsteroidGenerator` picks one of four screen edges at random (top, bottom, left, right) and places the asteroid just outside the visible area (offset by `ASTEROID_OFFSET` = 50 px). The asteroid's velocity vector points **towards the player's current position**, with a base speed of `ASTEROID_SPEED` (3) and a random multiplier between **0.7×** and **1.3×** for variety.

## Alien Spawning Details

`AlienGenerator` works similarly to `AsteroidGenerator` — it picks a random screen edge and places the alien just outside the visible area. The alien's initial velocity points towards the player at `ALIEN_SPEED` (1.0 px/frame). Unlike asteroids, aliens continuously track the player and wrap around the screen.

## Scoring

- +1 point for every asteroid **shot by the player**.
- +5 points for every alien destroyed by a player bullet (`ALIEN_KILL_SCORE`).
- Asteroids that collide with each other or fly off-screen do **not** count.
- The high score is tracked across games within the same session (resets on app close).
