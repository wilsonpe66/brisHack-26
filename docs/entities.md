# Entities

## Shared contracts

`GameObject` is the abstract base for rendered world objects. It stores `Position`, `Velocity`, rotation angle, collision radius, health, and sprite scale. An object is alive while health is greater than zero.

- `Updatable` provides `update()` for per-tick behavior.
- `Collidable` provides `collide(Collidable)`.
- `Wrappable` wraps positions across the playfield.
- `SelfDefendable` provides the shared projectile-pattern helpers used by the player and aliens.
- `Bullet` combines update/collision behavior with position, velocity, owner, and death accessors.

Collisions are detected as circle overlaps in `WorldState`; both objects then receive the other through `collide()`.

## Player

The player starts at (750, 450), facing upward, with radius 25, health 100, and scale 0.5. It accelerates forward, rotates, slows through velocity decay when not thrusting, and wraps at screen edges.

Firing behavior comes from the current `GameLevel`. Normal fire observes the configured cooldown; super fire skips that cooldown. Higher levels select wider projectile patterns, faster projectiles, and different ship/missile sprites.

Damage rules:

- asteroid collision: 10 health
- alien collision: 40 health
- alien-owned bullet: 2 health
- unexpected collision types: immediate death

## Asteroid

Asteroids spawn 50 pixels beyond a random edge and initially travel toward the player's current position at 0.7–1.3 times the base speed. They have radius 30, scale 0.3, continuously rotate, and use one of four sprites. Sprite index determines health from 1 to 4.

They lose one health when hit by a bullet, player, or alien; asteroid-to-asteroid collision kills them. They despawn beyond a 100-pixel off-screen buffer.

## Alien

Aliens spawn beyond a random edge using current-level speed and timing values. They have radius 20, health 100, scale 0.5, periodically retarget the player, wrap at edges, and fire level-configured projectiles after a grace period. An alien ignores its own bullets and dies on any other collision.

## Boss alien

`BossAlien` extends `Alien` with radius 60, health 200, scale 0.2, five level-selected sprites, and progressively wider projectile patterns. It captures the current `GameLevel` when spawned and continues using that level's boss shooting configuration for its lifetime. It takes:

- 20 damage from player bullets
- 2 damage from other alien bullets
- 50 damage from asteroids
- 10 damage from aliens

It ignores its own bullets and dies on other unhandled collisions.

## Bullets

Six `BulletLevel` classes use different missile sprites but share `Bullet` behavior. Bullets have radius 5, health 1, move in a straight line, and die when leaving the playfield. A bullet ignores its owner and bullets from the same owner.

Player-owned bullet collisions award points immediately: 2 for an asteroid, 5 for an alien, and 7 for a boss alien. The bullet then dies. Entity collision logic separately applies damage or death to the target.

## Explosions, background stars, and health bar

During dead-object removal, `WorldState` creates an `Explosion` at each removed object's position using its collision radius. The effect has no sprite or collision response: `GamePanel` draws it as a red oval whose radius expands while its color fades toward black. It lasts for roughly 100 updates before dying and being removed from the background and update sets.

`WorldState` creates three colored `BackgroundStar` objects, updates them separately, and `GamePanel` renders them as ovals. `HealthBar` mirrors player health, but the visible HUD health bar is drawn directly by `GamePanel` rather than using that entity.

> `Asteroid.killedByBullet` and the related removal-time score path remain in the code, but the flag is never set. Current asteroid points come from `Bullet.collide()`.
