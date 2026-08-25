# Screens and UI

`Game` presents three panels in a `CardLayout`. Windowed mode uses a fixed 1500 × 900-pixel content area and is centered on screen. F11 or the **Full Screen** button enters fullscreen on the monitor containing the window without changing its display mode; unsupported window managers fall back to borderless maximized mode. In fullscreen, the button changes to **Restore Screen**. Escape or **Restore Screen** returns to the saved window bounds.

Gameplay always uses a 1500 × 900 logical viewport. In fullscreen, `GamePanel` scales that viewport uniformly and centers it with black letterboxing when the monitor aspect ratio differs. World coordinates, collisions, HUD placement, and sprite proportions therefore remain unchanged across resolutions.

## Menu

`MenuPanel` draws the space background and displays:

- the title **Alien Force**
- the subtitle **Press PLAY to start**
- PLAY GAME, Full Screen/Restore Screen, and QUIT buttons

Menu music starts when the panel is created. PLAY opens the player-name flow before starting the game. If no names exist, the platform user name is offered; otherwise the user can select an existing name or add a unique 1–50 character name.

## Gameplay

`GamePanel` renders, in order:

1. the stretched space-background image
2. three colored `BackgroundStar` objects
3. every live sprite using position, rotation, and scale transforms
4. the HUD and, when applicable, an animated PAUSED overlay

The HUD shows the score and player name on the left, level in the center, and a red/green health bar on the right.

If the window loses focus after the player has scored, the game automatically pauses. Returning to the window leaves the game paused until the player explicitly resumes it.

## Game over

When player health reaches zero, the game timer stops and the score is persisted. `GameOverPanel` displays:

- the last score
- a non-editable table of up to ten top scores with rank, player, score, and an `MM-dd` date
- NEW GAME, Full Screen/Restore Screen, and QUIT buttons

The ranking sorts by score descending, then timestamp descending. The ten highest-ranked score records are retained on disk.

When game over opens, gameplay music stops and menu music resumes.

Closing the application during an active game records the current score when the player is alive and has scored at least one point.
