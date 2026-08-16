# Constants and Tuning

Global values live in `com.alienforce.utils.Constants`. Difficulty-dependent values are represented by `GameLevel`, `AlienConstants`, and `ShootConstants` records in `Constants.GAME_LEVELS`.

## Global values

| Constant | Value | Meaning |
|---|---:|---|
| `WIDTH` | 1500 | Gameplay width in pixels |
| `HEIGHT` | 900 | Gameplay height in pixels |
| `BUTTON_WIDTH` | 200 | Menu and game-over button width |
| `BUTTON_HEIGHT` | 50 | Menu and game-over button height |
| `FPS` | 60 | Swing timer target rate |
| `FRAME_DELAY` | 16 ms | Integer result of `1000 / FPS` |
| `SPAWN_DELAY` | 1000 ms | Asteroid spawn interval |
| `ASTEROID_OFFSET` | 50 px | Distance beyond an edge used for spawning |
| `ASTEROID_SPEED` | 3 px/update | Base speed before a random 0.7–1.3 multiplier |
| `MAX_PLAYER_SPEED` | 5 px/update | Player speed cap |
| `PLAYER_ACCELERATION` | 0.2 px/update² | Forward acceleration while thrusting |
| `ROTATION_SPEED` | 0.06 rad/update | Player rotation step |
| `PLAYER_VELOCITY_DECAY` | 0.98 | Velocity multiplier while not thrusting |
| `MIDDLE_X`, `MIDDLE_Y` | 750, 450 | Player start/reset position |
| `ALIEN_KILL_SCORE` | 5 | Declared compatibility constant; bullet scoring currently uses literal values |

## Level configuration

There are 15 zero-based `GameLevel` entries, displayed to the player as levels 1–15. Each entry contains:

- player shot cooldown and bullet speed
- alien shot cooldown, post-spawn no-shoot period, bullet speed, movement speed, spawn interval, initial delay, and retarget interval
- optional boss-alien settings; when omitted, `GameLevel` defaults boss settings to the normal alien settings

As levels rise, the player's projectile pattern expands and generally fires faster, while enemies spawn more often and their movement/projectiles become faster. `Player` selects one of six ship and missile sprites based on level; `BossAlien` similarly selects one of five boss sprites.

## Score thresholds

`WorldState.levelUpdate()` changes the internal level after scores of 300, 800, 1,500, 2,500, 6,000, 10,000, 15,000, 21,000, 26,000, 31,000, 40,000, 50,000, 60,000, and 70,000. Comparisons use `>`, so a transition occurs only after exceeding the threshold.

Each level change plays the level-up effect and increases current health by 10%, clamped to 10–100. Above 80,000 points the game remains at the highest configured level and contains an additional periodic 20% healing check.

## Timing units

Despite the field name `shootCooldownFrames`, player firing compares the value with `System.currentTimeMillis()`, so the player cooldown values act as milliseconds. Alien cooldown and no-shoot values are decremented once per update and therefore act as frames. Spawn delays are also milliseconds.
