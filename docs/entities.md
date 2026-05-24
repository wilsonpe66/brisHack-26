# Entities

All objects in the game world share a common base class and interface.

## Class Hierarchy

```
Updatable (interface)
    │
    └── GameObject (abstract class)
            ├── Player
            ├── Asteroid
            ├── Bullet
            ├── Alien
            └── AlienBullet

HealthBar (implements Updatable — not a GameObject)
```

---

## `Updatable` Interface

```java
public interface Updatable {
    void update(); // called every frame
}
```

Any object that needs per-frame logic implements `Updatable`. The game loop iterates over all `Updatable`s and calls `update()` once per frame.

---

## `GameObject` (Abstract)

The shared base class for everything that exists in the game world. It holds:

| Field                    | Type       | Purpose                                              |
|--------------------------|------------|------------------------------------------------------|
| `positionX`, `positionY` | `double`   | World position (pixels)                              |
| `velocityX`, `velocityY` | `double`   | Movement per frame (pixels/frame)                    |
| `rotationAngle`          | `double`   | Facing direction in **radians**                      |
| `radius`                 | `double`   | Collision circle radius                              |
| `health`                 | `int`      | Hit points (player/alien = 100, asteroid/bullet = 1) |
| `isAlive`                | `boolean`  | `false` → removed at end of frame                    |
| `scale`                  | `double`   | Sprite draw scale (default 1.0)                      |

### Key Methods

- `getSprite()` — abstract; each subclass returns its own image.
- `collide(GameObject other)` — abstract; entry point for double dispatch.
- `collideWith(Player)`, `collideWith(Asteroid)`, `collideWith(Bullet)`, `collideWith(Alien)`, `collideWith(AlienBullet)` — overridable collision response methods (default no-op).

---

## `Player`

The player-controlled spaceship.

| Property          | Value                                     |
|-------------------|-------------------------------------------|
| Sprite            | `assets/images/spaceship.png`             |
| Starting position | Centre of screen (`MIDDLE_X`, `MIDDLE_Y`) |
| Initial rotation  | −π/2 (pointing **up**)                    |
| Radius            | 25 px                                     |
| Health            | 100                                       |
| Scale             | 0.5                                       |

### Movement

- **Thrust** (W / ↑): accelerates in the direction the ship faces by `PLAYER_ACCELERATION` (0.2 px/frame²), capped at `MAX_PLAYER_SPEED` (5 px/frame).
- **Rotation** (A / D or ← / →): rotates by `ROTATION_SPEED` (0.06 rad/frame ≈ 3.4°/frame).
- **Velocity decay**: when not thrusting, velocity is multiplied by `PLAYER_VELOCITY_DECAY` (0.98) each frame, giving a gradual slow-down.
- **Screen wrapping**: if the player moves off one edge, they reappear on the opposite side.

### Shooting

`player.shoot()` creates a `Bullet` at the ship's nose, travelling in the ship's facing direction at speed 16 px/frame.

### Death

The player dies (health set to 0) upon colliding with any asteroid, alien, or alien bullet.

---

## `Asteroid`

A randomly-spawned obstacle that drifts across the screen.

| Property  | Value                                                   |
|-----------|---------------------------------------------------------|
| Sprites   | `asteroid1.png` – `asteroid4.png` (random per instance) |
| Rotation  | Random angle on spawn                                   |
| Radius    | 30 px                                                   |
| Health    | 1                                                       |
| Scale     | 0.3                                                     |

### Behaviour

- Moves in a straight line at its initial velocity (no acceleration, no screen wrapping).
- Despawns when it moves more than 100 px off-screen (no score awarded for this).
- Destroyed by a single bullet hit (+1 score).
- Two asteroids that collide destroy each other (no score).
- Alien bullets that hit an asteroid destroy the alien bullet but leave the asteroid unharmed.

### `killedByBullet` Flag

A boolean that is set to `true` when an asteroid takes damage from a bullet. This flag is checked during scoring so that only *shot* asteroids count towards the player's score.

---

## `Bullet`

A projectile fired by the player.

| Property | Value                       |
|----------|-----------------------------|
| Sprite   | `assets/images/missile.png` |
| Radius   | 5 px                        |
| Health   | 1                           |

### Behaviour

- Travels in a straight line, despawning when it leaves the screen.
- Destroyed on contact with an asteroid.
- Hitting an alien destroys both the bullet and the alien, awarding `ALIEN_KILL_SCORE` (5) points to the player.
- Player bullets and alien bullets destroy each other on contact.
- Has an `owner` reference (the `Player` who fired it), used for awarding alien kill score.

---

## `Alien`

An enemy spaceship that chases and shoots at the player.

| Property | Value                                |
|----------|--------------------------------------|
| Sprite   | `assets/images/shipGreen_manned.png` |
| Radius   | 20 px                                |
| Health   | 100                                  |
| Scale    | 0.5                                  |

### Spawning

- Spawned by `AlienGenerator` from a random screen edge, similar to asteroids.
- First alien appears after `ALIEN_SPAWN_INITIAL_DELAY` (5 s), then another every `ALIEN_SPAWN_DELAY` (15 s).

### Behaviour

- **Tracking**: periodically re-aims its velocity towards the player's position every `ALIEN_TARGET_UPDATE_INTERVAL` (45) frames.
- Moves at `ALIEN_SPEED` (1.0 px/frame) — slower than asteroids but actively pursues the player.
- **Screen wrapping**: like the player, aliens wrap around screen edges.
- **Shooting**: fires `AlienBullet`s aimed at the player. The shoot cooldown is `ALIEN_SHOOT_COOLDOWN_FRAMES` (150 frames ≈ 2.5 s). After spawning, the alien waits `ALIEN_SPAWN_NO_SHOOT_FRAMES` (120 frames ≈ 2 s) before firing its first shot.

### Collision Response

- Destroyed by a single player bullet (+5 score).
- Destroyed on contact with the player (player also destroys the alien on collision, but the player survives).
- Destroyed on contact with an asteroid.
- Immune to its own alien bullets (friendly fire is off).

---

## `AlienBullet`

A projectile fired by an `Alien`, aimed at the player.

| Property | Value                                                |
|----------|------------------------------------------------------|
| Sprite   |  `assets/images/missile.png` (same as player bullet) |
| Radius   | 5 px                                                 |
| Health   | 1                                                    |
| Speed    | `ALIEN_BULLET_SPEED` (4 px/frame)                    |

### Behaviour

- Travels in a straight line, despawning when it leaves the screen.
- **Kills the player** on contact (player health set to 0).
- Destroyed on contact with a player bullet (both destroyed).
- Destroyed on contact with an asteroid (asteroid unharmed).
- No effect on other aliens (friendly fire off).
- Has an `alienOwner` reference to the `Alien` that fired it.

---

## `HealthBar`

A simple `Updatable` (not a `GameObject`) that mirrors the player's current health value to a `displayedHealth` field. Currently not rendered on-screen — it's a stub for a future visual health bar.

---

## Double-Dispatch Collisions

Collision response uses the **Visitor / double-dispatch** pattern to let each pair of entity types define their own behaviour without `instanceof` checks:

1. `WorldState.handleCollisions()` detects overlap between objects `a` and `b`.
2. It calls `a.collide(b)` and `b.collide(a)`.
3. Each `collide()` implementation calls `other.collideWith(this)`, passing its concrete type.
4. The receiving object's `collideWith(Player)` / `collideWith(Asteroid)` / `collideWith(Bullet)` / `collideWith(Alien)` / `collideWith(AlienBullet)` override determines the outcome.

### Collision Matrix

| A ↓ \ B →       | Player                        | Asteroid              | Bullet                  | Alien                     | AlienBullet                |
|-----------------|-------------------------------|-----------------------|-------------------------|---------------------------|----------------------------|
| **Player**      |  Error (impossible)           | Player dies           | Error (impossible)      | Alien destroyed           | Player dies                |
| **Asteroid**    | (no-op)                       | Both destroyed        | Asteroid takes 1 damage | Alien destroyed           | AlienBullet destroyed      |
| **Bullet**      | Error                         | Bullet destroyed      | Error                   | Both destroyed (+5 score) | Both destroyed             |
| **Alien**       | Alien destroyed               | Alien destroyed       | Alien destroyed         | (no-op)                   | (no-op, friendly fire off) |
| **AlienBullet** | Player dies, bullet destroyed | AlienBullet destroyed | Both destroyed          | (no-op)                   | (no-op)                    |

> "Error" cases throw a `RuntimeException` — they should never happen with the current game rules.
