# Entities

All objects in the game world share a common base class and interface.

## Class Hierarchy

```
Updatable (interface)
    │
    └── GameObject (abstract class)
            ├── Player
            ├── Asteroid
            └── Bullet

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

| Field | Type | Purpose |
|-------|------|---------|
| `positionX`, `positionY` | `double` | World position (pixels) |
| `velocityX`, `velocityY` | `double` | Movement per frame (pixels/frame) |
| `rotationAngle` | `double` | Facing direction in **radians** |
| `radius` | `double` | Collision circle radius |
| `health` | `int` | Hit points (player = 100, asteroid/bullet = 1) |
| `isAlive` | `boolean` | `false` → removed at end of frame |
| `scale` | `double` | Sprite draw scale (default 1.0) |

### Key Methods

- `getSprite()` — abstract; each subclass returns its own image.
- `collide(GameObject other)` — abstract; entry point for double dispatch.
- `collideWith(Player)`, `collideWith(Asteroid)`, `collideWith(Bullet)` — overridable collision response methods (default no-op).

---

## `Player`

The player-controlled spaceship.

| Property | Value |
|----------|-------|
| Sprite | `assets/images/spaceship.png` |
| Starting position | Centre of screen (`MIDDLE_X`, `MIDDLE_Y`) |
| Initial rotation | −π/2 (pointing **up**) |
| Radius | 20 px |
| Health | 100 |
| Scale | 0.5 |

### Movement

- **Thrust** (W / ↑): accelerates in the direction the ship faces by `PLAYER_ACCELERATION` (0.2 px/frame²), capped at `MAX_PLAYER_SPEED` (5 px/frame).
- **Rotation** (A / D or ← / →): rotates by `ROTATION_SPEED` (0.06 rad/frame ≈ 3.4°/frame).
- **Velocity decay**: when not thrusting, velocity is multiplied by `PLAYER_VELOCITY_DECAY` (0.98) each frame, giving a gradual slow-down.
- **Screen wrapping**: if the player moves off one edge, they reappear on the opposite side.

### Shooting

`player.shoot()` creates a `Bullet` at the ship's nose, travelling in the ship's facing direction at speed 16 px/frame.

### Death

The player dies (health set to 0) instantly upon colliding with any asteroid.

---

## `Asteroid`

A randomly-spawned obstacle that drifts across the screen.

| Property | Value |
|----------|-------|
| Sprites | `asteroid1.png` – `asteroid4.png` (random per instance) |
| Rotation | Random angle on spawn |
| Radius | 30 px |
| Health | 1 |
| Scale | 0.3 |

### Behaviour

- Moves in a straight line at its initial velocity (no acceleration, no screen wrapping).
- Despawns when it moves more than 100 px off-screen (no score awarded for this).
- Destroyed by a single bullet hit (+1 score).
- Two asteroids that collide destroy each other (no score).

### `killedByBullet` Flag

A boolean that is set to `true` when an asteroid takes damage from a bullet. This flag is checked during scoring so that only *shot* asteroids count towards the player's score.

---

## `Bullet`

A projectile fired by the player.

| Property | Value |
|----------|-------|
| Sprite | `assets/images/missile.png` |
| Radius | 5 px |
| Health | 1 |

### Behaviour

- Travels in a straight line, despawning when it leaves the screen.
- Destroyed on contact with an asteroid.
- Has an `owner` reference (the `Player` who fired it), currently unused beyond construction.

---

## `HealthBar`

A simple `Updatable` (not a `GameObject`) that mirrors the player's current health value to a `displayedHealth` field. Currently not rendered on-screen — it's a stub for a future visual health bar.

---

## Double-Dispatch Collisions

Collision response uses the **Visitor / double-dispatch** pattern to let each pair of entity types define their own behaviour without `instanceof` checks:

1. `WorldState.handleCollisions()` detects overlap between objects `a` and `b`.
2. It calls `a.collide(b)` and `b.collide(a)`.
3. Each `collide()` implementation calls `other.collideWith(this)`, passing its concrete type.
4. The receiving object's `collideWith(Player)` / `collideWith(Asteroid)` / `collideWith(Bullet)` override determines the outcome.

### Collision Matrix

| A ↓ \ B → | Player | Asteroid | Bullet |
|------------|--------|----------|--------|
| **Player** | Error (impossible) | Player dies | Error (impossible) |
| **Asteroid** | (no-op on asteroid side) | Both destroyed | Asteroid takes 1 damage |
| **Bullet** | Error | Bullet destroyed | Error |

> "Error" cases throw a `RuntimeException` — they should never happen with the current game rules.
