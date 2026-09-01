# Architecture

## Application structure

`Main` schedules construction of `Game` on Swing's Event Dispatch Thread. `Game` is the top-level `JFrame` and uses `CardLayout` to switch among the menu, gameplay, and game-over panels.

```text
Main
└── Game (JFrame)
    ├── MenuPanel
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
2. PLAY prompts the user to select or create a player name. A valid selection switches to `GamePanel`, changes music, and starts its Swing `Timer`.
3. Each timer event updates `WorldState`, checks for player death, and repaints the panel.
4. On death, `Game` records the reached level and score, refreshes the top-ten table, switches back to menu music, and shows `GameOverPanel`.
5. NEW GAME resets the existing world and returns through the player-selection flow.
6. If the window loses focus during an active, nonzero-score game, `Game` pauses the world. Closing the window routes through `quit()`, which records an unfinished nonzero score before exiting.

## Assets

`AssetManager` loads images and WAV clips with typed `ImageKey`, `SoundEffectKey`, and `SoundLoopKey` values. Maven packages resources from `src/resource/com/alienforce/assets` into the executable JAR.

## Persistence

`LeaderboardStore` uses Jackson to store the selected player, known player names, and the ten highest-ranked score records in:

```text
~/.alien-force/.alien-force-leaderboard.json
```

Each score record contains the player name, reached level, score, and creation time. Records are ranked by score descending and then timestamp descending. Names are trimmed, case-insensitively unique, and limited to 1–50 characters. Read/write failures are intentionally non-fatal so persistence cannot prevent the game from starting or displaying game over.
