# Constants & Tuning

All tunable values live in `src/utils/Constants.java`. This makes it easy to tweak gameplay without hunting through multiple files.

## Window

| Constant        | Value  | Description                  |
|-----------------|--------|------------------------------|
| `WIDTH`         | 1000   | Game window width in pixels  |
| `HEIGHT`        | 1000   | Game window height in pixels |
| `BUTTON_WIDTH`  | 200    | Menu/Game Over button width  |
| `BUTTON_HEIGHT` | 50     | Menu/Game Over button height |

## Timing

| Constant                | Value   | Description                                      |
|-------------------------|---------|--------------------------------------------------|
| `FPS`                   | 60      | Target frames per second                         |
| `FRAME_DELAY`           | 16 ms   | Milliseconds between frames (1000 / FPS)         |
| `SPAWN_DELAY`           | 1000 ms | Time between asteroid spawns                     |
| `SHOOT_COOLDOWN_FRAMES` | 15      | Frames between player shots (≈ 0.25 s at 60 FPS) |

## Player

| Constant                | Value | Description                                      |
|-------------------------|-------|--------------------------------------------------|
| `MAX_PLAYER_SPEED`      | 5.0   | Maximum velocity magnitude (px/frame)            |
| `PLAYER_ACCELERATION`   | 0.2   | Thrust acceleration (px/frame²)                  |
| `ROTATION_SPEED`        | 0.06  | Rotation rate (radians/frame ≈ 3.4°)             |
| `PLAYER_VELOCITY_DECAY` | 0.98  | Velocity multiplier per frame when not thrusting |

## Asteroids

| Constant           | Value | Description                                      |
|--------------------|-------|--------------------------------------------------|
| `ASTEROID_SPEED`   | 3.0   | Base asteroid speed (px/frame)                   |
| `ASTEROID_OFFSET`  | 50.0  | How far outside the screen edges asteroids spawn |

> Actual asteroid speed varies between **0.7×** and **1.3×** the base speed thanks to a random multiplier in `AsteroidGenerator`.

## Aliens

| Constant                       | Value     | Description                                           |
|--------------------------------|-----------|-------------------------------------------------------|
| `ALIEN_SPEED`                  | 1.0       |  Alien movement speed (px/frame)                      |
| `ALIEN_BULLET_SPEED`           | 4.0       | Speed of bullets fired by aliens (px/frame)           |
| `ALIEN_SHOOT_COOLDOWN_FRAMES`  | 150       | Frames between alien shots (≈ 2.5 s at 60 FPS)        |
| `ALIEN_SPAWN_DELAY`            | 15 000 ms | Time between alien spawns (after initial delay)       |
| `ALIEN_SPAWN_INITIAL_DELAY`    | 5 000 ms  | Wait time after game start before first alien appears |
| `ALIEN_SPAWN_NO_SHOOT_FRAMES`  | 120       | Grace period after spawn before alien fires (≈ 2 s)   |
| `ALIEN_KILL_SCORE`             | 5         | Points awarded for destroying an alien with a bullet  |
| `ALIEN_TARGET_UPDATE_INTERVAL` | 45        | Frames between alien velocity re-aims towards player  |

## Derived Values

| Name        | Value   | How                           |
|-------------|---------|-------------------------------|
| `MIDDLE_X`  | 500.0   | `WIDTH / 2` — player spawn X  |
| `MIDDLE_Y`  | 500.0   | `HEIGHT / 2` — player spawn Y |
