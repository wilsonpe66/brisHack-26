# Sound

All audio is managed through the static `SoundManager` class.

## Sound Files

| File | Used for | Type |
|------|----------|------|
| `space_oddity.wav` | Menu background music | Looping |
| `background.wav` | Gameplay background music | Looping |
| `thruster.wav` | Ship thrust (while W/↑ held) | Looping |
| `shoot.wav` | Bullet fired | One-shot |
| `explosion.wav` | (Available but not currently used) | — |
| `win.wav` | Game Over screen | One-shot |

## `SoundManager` API

### `playSound(String path)`

Plays a **one-shot** sound effect. A new `Clip` is created each time, so overlapping sounds (e.g. rapid shooting) are supported.

### `playLooping(String id, String path)`

Starts a **looping** sound under a given `id`. If that `id` is already playing, the call is a no-op (prevents duplicate loops). The clip loops continuously until explicitly stopped.

### `stopLooping(String id)`

Stops and closes the looping clip registered under `id`. Safe to call even if nothing is playing.

## How Sound Fits Into the Game

| Event | Sound action |
|-------|--------------|
| Menu panel created | `playLooping("menu_music", "space_oddity.wav")` |
| PLAY button pressed | `stopLooping("menu_music")` → `playLooping("background", "background.wav")` |
| W / ↑ pressed (thrust) | `playLooping("thruster", "thruster.wav")` |
| W / ↑ released | `stopLooping("thruster")` |
| Space pressed (shoot) | `playSound("shoot.wav")` |
| Player dies | `stopLooping("background")` → `playSound("win.wav")` |
| Game reset | `stopLooping("thruster")` (cleanup) |
