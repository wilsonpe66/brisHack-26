# Input & Controls

## Control Scheme

| Action | Keys |
|--------|------|
| Thrust forward | `W` or `↑` |
| Brake / reverse (unused) | `S` or `↓` (tracked but the player doesn't use it) |
| Rotate left | `A` or `←` |
| Rotate right | `D` or `→` |
| Shoot | `Space` |

Both WASD and arrow keys are supported for movement.

> **Note:** `S` / `↓` is captured by `InputHandler` but the `Player.update()` method does not read `downPressed`, so pressing it currently has no effect.

---

## How `InputHandler` Works

`InputHandler` implements Java's `KeyListener` interface and is attached to `GamePanel`.

### Key Map

A `HashMap<Integer, Consumer<Boolean>>` maps each key code to a lambda that sets the corresponding boolean flag:

```
VK_W / VK_UP    → upPressed
VK_S / VK_DOWN  → downPressed
VK_A / VK_LEFT  → leftPressed
VK_D / VK_RIGHT → rightPressed
VK_SPACE        → shootPressed
```

- On `keyPressed` → the flag is set to `true`.
- On `keyReleased` → the flag is set to `false`.

This means the game uses **sustained** input: holding a key produces continuous action, not a single event.

### `clearAllKeys()`

When the game resets, `clearAllKeys()` sets all flags to `false`. This prevents stuck keys — if a key was held down when the game ended and focus was lost, the `keyReleased` event would never fire, leaving the flag `true` for the next game.
