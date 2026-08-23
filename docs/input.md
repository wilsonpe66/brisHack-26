# Input and Controls

## Keyboard

| Action | Keys | Notes |
|---|---|---|
| Thrust | `W` or `Up` | Accelerates forward; releasing thrust applies velocity decay |
| Rotate left | `A` or `Left` | Rotates counter-clockwise |
| Rotate right | `D` or `Right` | Rotates clockwise |
| Fire | `Space` or `Z` | Uses the current level's shot cooldown and pattern |
| Super fire | `X` | Uses the current shot pattern without the cooldown check |
| Pause/resume | `Enter` | Toggles simulation and looping audio pause |
| Mute | `M` | Toggles the global mute setting |
| Toggle fullscreen | `F11` | Switches between the saved window bounds and borderless fullscreen |
| Exit fullscreen | `Escape` | Returns to windowed mode; has no effect when already windowed |

`S` and `Down` are tracked as downward input but are not consumed by `Player`, so they currently have no gameplay effect.

`InputHandler` implements `KeyListener` and stores pressed/released state in boolean flags. This supports sustained actions while a key is held. `clearAllKeys()` resets every flag when a new game starts.

## Gamepad

JInput selects the first controller whose type is `GAMEPAD`. During gameplay it is polled once per world update.

| Action | Reported component |
|---|---|
| Move/rotate | `pov` directional values |
| Fire | `A` or `Right Thumb` |
| Super fire | `X` or `rz` |
| Pause/resume | `Start` |

On the menu and game-over screens, pressing `A`, `X`, `rz`, `Right Thumb`, or `Start` calls `restartGame()`. Component names are supplied by the controller driver and may vary between devices.

Keyboard `M`, `F11`, and `Escape` are registered as window-level Swing key bindings so they remain available when another component has focus.
