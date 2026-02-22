# Architecture Overview

## Project Layout

```
brisHack-26/
├── src/
│   ├── entities/       # Game objects (Player, Asteroid, Bullet, …)
│   ├── game/           # Game loop, screens, input, audio
│   └── utils/          # Shared constants
├── assets/
│   ├── images/         # Sprites and backgrounds
│   └── sounds/         # WAV audio files
├── build.sh            # Compile + JAR packaging script
└── brisHack-26.jar     # Pre-built executable JAR
```

## Package Responsibilities

### `entities`
Contains every object that can exist in the game world. All concrete entities extend the abstract `GameObject` class and implement the `Updatable` interface so the game loop can call `update()` each frame.

### `game`
Owns the application lifecycle:

| Class | Role |
|-------|------|
| `Main` | Entry point — launches the Swing UI on the Event Dispatch Thread |
| `Game` | Top-level `JFrame`; manages screen navigation with a `CardLayout` |
| `GamePanel` | The gameplay screen; runs the game timer and renders sprites |
| `MenuPanel` | Main menu shown at startup |
| `GameOverPanel` | End-of-game screen with score / high-score display |
| `WorldState` | Core simulation — updates entities, detects collisions, manages scoring |
| `AsteroidGenerator` | Spawns asteroids from random screen edges aimed at the player |
| `InputHandler` | Translates keyboard events into boolean flags read by `Player` |
| `SoundManager` | Static utility for one-shot and looping audio playback |

### `utils`
A single `Constants` class holding every tunable number (window size, speeds, cooldowns, etc.).

## How It All Connects

```
Main  →  Game (JFrame)
              │
              ├── MenuPanel ────── "PLAY" ──→ Game.showGame()
              │
              ├── GamePanel ────── owns WorldState
              │       │                  │
              │       │                  ├── Player
              │       │                  ├── Asteroids (via AsteroidGenerator)
              │       │                  └── Bullets
              │       │
              │       └── game timer fires actionPerformed() @ 60 FPS
              │
              └── GameOverPanel ── "NEW GAME" ──→ Game.restartGame()
```

1. `Main` creates a `Game` (JFrame) on the Swing EDT.
2. `Game` holds three panels in a `CardLayout` — Menu, Game, and Game Over.
3. When the user clicks **PLAY**, `Game.showGame()` switches to `GamePanel` and starts the timer.
4. Every frame (~16 ms at 60 FPS), `GamePanel.actionPerformed()` calls `WorldState.updateState()`, then `repaint()`.
5. When the player dies, `GamePanel` stops the timer and tells `Game` to show the Game Over screen.
6. From Game Over the user can restart (resets `WorldState`) or quit.
