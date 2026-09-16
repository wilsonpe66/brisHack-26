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

Every call to `Player.collide()` starts the `TAKING_ON_DAMAGE` sound before applying the collision-specific rule. Player death stops the thruster loop and plays the game-over effect.

## Asteroid

Asteroids spawn 50 pixels beyond a random edge and initially travel toward the player's current position at 0.7–1.3 times the base speed. A random size multiplier from 0.75 up to 1.5 gives them radii from 22.5 up to 45 and sprite scales from 0.225 up to 0.45. Each also receives a clockwise or counter-clockwise angular velocity with magnitude below 0.05 radians per update. They use one of four sprites, whose index determines health from 1 to 4.

They lose one health when hit by a bullet, player, or alien; asteroid-to-asteroid collision kills them. They despawn beyond a 100-pixel off-screen buffer.

## Alien

Aliens spawn beyond a random edge using current-level speed and timing values. They have radius 20, health 100, scale 0.5, recompute velocity toward the player on every update, blend their facing angle toward that target, wrap at edges, and fire level-configured projectiles after a grace period. An alien ignores its own bullets and dies on any other collision.

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

## Explosions

During dead-object removal, `WorldState` constructs an `Explosion` for every dead non-bullet object. `Explosion` currently defines sprite and color mappings for the exact `Asteroid`, `Alien`, and `BossAlien` classes: orange fire for asteroids, green plasma for standard aliens, and blue energy for bosses. Each effect combines its expanding sprite with a class-specific colored oval, moves at half the destroyed object's velocity, and has no collision response. Asteroid explosions also inherit the asteroid's angular velocity; alien explosions do not rotate.

Explosion progress increases by 0.02 per update and expires after passing 3, which is about 150 updates. Over that lifetime its collision radius grows from the source radius to roughly three times that value, while its rendered sprite scale grows from zero toward 1.8. Explosions are stored separately from both background planets and active collidable objects and are removed from the explosion and update sets after death.

## Background planets

`WorldState` creates three `Planet` objects at random positions and updates them separately from active gameplay objects. Each instance randomly selects the ocean, rocky, or volcanic planet sprite, starts at a random rotation, and uses a random scale from 0.25 up to 1.0. Its base radius is randomly chosen from 100 up to 300 pixels, while its update cycle slowly pulses the effective radius. Its velocity is zero, it has no collision response, and `GamePanel` renders the selected planet image behind explosions and active sprites. The stored color remains available for an oval fallback if a background sprite is absent.

## Health bar and color transitions

`HealthBar` mirrors player health, but the visible HUD health bar is drawn directly by `GamePanel` rather than using that entity.

`ColorTransition` accepts at least two `Color` values and linearly interpolates between adjacent entries according to the integer and fractional parts of a supplied scale. Explosions use it for their class-specific underlays, and `GamePanel` uses it to cycle the paused title through yellow, cyan, red, and back to yellow.

Implementation note: `Asteroid.killedByBullet` and the related removal-time score path remain in the code, but the flag is never set. Current asteroid points come from `Bullet.collide()`.
