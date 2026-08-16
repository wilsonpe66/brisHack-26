# Sound

Audio resources are WAV files loaded from the application classpath by `AssetManager`. `SoundManager` addresses them through typed keys.

## Sounds

| Key | File | Playback | Used for |
|---|---|---|---|
| `MENU_MUSIC` | `mixkit-fright-night-871.wav` | Loop | Menu and game-over music |
| `BACK_GROUND` | `background.wav` | Loop | Gameplay music |
| `THRUSTER` | `thruster.wav` | Loop | Active player thrust |
| `SHOOT` | `shoot.wav` | Effect | Player firing |
| `EXPLOSION` | `explosion.wav` | Effect | Alien or boss death; configured at -8 dB |
| `LEVEL_UP` | `level-up.wav` | Effect | Level transition |
| `GAME_OVER` | `game-over.wav` | Effect | Player death |

## Playback API

- `play(SoundKey)` restarts and plays a sound effect unless muted.
- `play(SoundLoopKey)` starts a continuous loop unless that clip is already running or sound is muted.
- `stop(SoundLoopKey)` stops and unregisters a loop.
- `stop(SoundEffectKey)` stops a loaded effect clip.
- `stopAllLooping()` stops and clears all registered loops.
- `togglePauseLooping(boolean)` stops or resumes registered looping clips with the world pause state.
- `setMuted(boolean)` updates `Settings.muted` and stops loops when muting.

The gameplay input path also toggles `Settings.muted` directly when `M` is pressed. Starting a new game resets the muted setting to `false`.
