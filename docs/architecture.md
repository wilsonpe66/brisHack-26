# Architecture

## Application structure

`Main` schedules construction of `Game` on Swing's Event Dispatch Thread. `Game` is the top-level `JFrame` and uses `CardLayout` to switch among the menu, gameplay, and game-over panels.

```text
Main
└── Game (JFrame)
    ├── MenuPanel
    ├── RoundedButton (shared panel/dialog controls)
    ├── GamePanel
    │   ├── InputHandler
    │   └── WorldState
    │       ├── Player and other GameObjects
    │       ├── AsteroidSpawner
    │       ├── AlienSpawner
    │       └── BossAlienSpawner
    ├── GameOverPanel
    ├── GamePadManager (menu/game-over actions)
    └── LeaderboardStore
```

## Packages

| Package | Responsibility |
|---|---|
| `com.alienforce` | Application entry point |
| `assets` | Typed image and sound keys, classpath loading, and audio playback |
| `entities` | `GameObject` implementations and collision/update contracts |
| `entities.amo` | Six projectile implementations selected by game level |
| `game` | Window, panels, timer-driven simulation, and spawning orchestration |
| `game.spawner` | Edge-based asteroid, alien, and boss-alien construction |
| `input` | Keyboard state and first-detected JInput gamepad |
| `leaderboard` | Score records, rankings, player names, and JSON persistence |
| `motion` | Immutable `Position` and `Velocity` value types |
| `utils` | Global constants, per-level tuning records, color interpolation, fonts, and mute state |

## Runtime flow

1. `Game` loads `LeaderboardStore`, creates the three panels, starts menu music, and begins polling a gamepad for menu actions.
2. PLAY opens a modal player-selection flow. A valid existing or newly created name switches to `GamePanel`, changes music, and starts its Swing `Timer`.
3. Each timer event updates `WorldState`, checks for player death, and repaints the panel.
4. On death, `Game` records the reached level and score, refreshes the top-ten table, switches back to menu music, and shows `GameOverPanel`.
5. RETRY replaces the gameplay world and starts with the current player. SWITCH USER replaces the world and returns through the player-selection flow.
6. If the window loses focus during an active, nonzero-score game, `Game` pauses the world. Closing the window routes through `quit()`, which records an unfinished nonzero score before exiting.

## Assets

`AssetManager` loads images and WAV clips with typed `ImageKey`, `SoundEffectKey`, and `SoundLoopKey` values. Maven packages resources from `src/resource/com/alienforce/assets` into the executable JAR.

The active `ImageKey` entries cover the standard alien, five boss aliens, four asteroids, three planets, six player ships, six missiles, and the space background. The 500 × 500 RGBA planet images are stored in `images/planet/`:

| Key | File | Visual |
|---|---|---|
| `PLANET_1` | `ocean.png` | Blue ocean planet with islands, clouds, and storms |
| `PLANET_2` | `rocky.png` | Rust-colored cratered desert planet |
| `PLANET_3` | `volcanic.png` | Dark volcanic planet with glowing lava |

All three planet files preserve transparent pixels outside their circular atmospheric rims. `Planet` loads the three keyed images, and each background object randomly selects one when constructed. `GamePanel` renders the selected image with the standard position, rotation, and scale transform before drawing explosions and active gameplay objects.

## Persistence

`LeaderboardStore` uses Jackson to store the selected player, known player names, and the ten highest-ranked score records in:

```text
~/.alien-force/.alien-force-leaderboard.json
```

Each score record contains the player name, reached level, score, and creation time. Records are ranked by score descending and then timestamp descending. Names are trimmed, case-insensitively unique, and limited to 1–50 characters. Read/write failures are intentionally non-fatal so persistence cannot prevent the game from starting or displaying game over.
