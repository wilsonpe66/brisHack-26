# Screens and UI

`Game` presents three panels in a `CardLayout`. Windowed mode uses a fixed 1500 × 900-pixel content area and is centered on screen. F11 or the **Full Screen** button enters fullscreen on the monitor containing the window without changing its display mode; unsupported window managers fall back to borderless maximized mode. In fullscreen, the button changes to **Restore Screen**. Escape or **Restore Screen** returns to the saved window bounds.

The application window supplies 16 × 16, 32 × 32, and 64 × 64 icons derived from the fifth boss-alien sprite, allowing the operating system to select the best size for title bars, task switchers, and window lists.

Gameplay always uses a 1500 × 900 logical viewport. In fullscreen, `GamePanel` scales that viewport uniformly and centers it with black letterboxing when the monitor aspect ratio differs. World coordinates, collisions, HUD placement, and sprite proportions therefore remain unchanged across resolutions.

## Menu

`MenuPanel` stretches the space background to the panel, then draws the fifth boss-alien sprite as a centered square whose width and height equal the window height. The controls are painted over those images. The menu displays:

- the title **Alien Force**
- PLAY GAME, Full Screen/Restore Screen, and GIVE UP buttons

Menu music starts when the panel is created. PLAY opens the player-name flow before starting the game.

## Buttons and player dialogs

Menu, game-over, and player-dialog actions use `RoundedButton`. It paints an antialiased yellow pill with centered uppercase monospaced text; while pressed, it adds a red inset and changes the text to dark gray.

If no player names exist, PLAY opens the modal **Add player name** form directly, prefilled with the platform user name when it is no longer than 50 characters. The form accepts a trimmed, case-insensitively unique name of 1–50 characters through Confirm, or can be closed with Cancel.

When names already exist, PLAY opens a modal **Select Player** form. It shows the saved names in case-insensitive alphabetical order and preselects the current player when possible. Play requires a selected name, Add New Player opens the name form, and Cancel returns without starting gameplay.

## Gameplay

`GamePanel` renders, in order:

1. the stretched space-background image
2. three `Planet` objects rendered with randomly selected ocean, rocky, or volcanic planet sprites
3. expanding explosion effects from the dedicated explosion set
4. every live sprite using position, rotation, and scale transforms
5. the HUD and, when applicable, an animated PAUSED overlay

The HUD shows the score and player name on the left, level in the center, and a red/green health bar on the right.

The PAUSED title cycles from yellow to cyan to red and back to yellow using the shared `ColorTransition` interpolation utility.

If the window loses focus after the player has scored, the game automatically pauses. Returning to the window leaves the game paused until the player explicitly resumes it.

## Game over

When player health reaches zero, the game timer stops and the score is persisted. `GameOverPanel` displays:

- the last score and reached level
- a non-editable table of up to ten top records with player, score, level, and an `MM-dd` date
- RETRY, SWITCH USER, Full Screen/Restore Screen, and GIVE UP buttons

RETRY constructs a fresh game world and starts immediately with the same player. SWITCH USER constructs a fresh world and opens the player-selection form before starting.

The ranking sorts by score descending, then timestamp descending. Each stored record includes player name, score, reached level, and creation time; the ten highest-ranked records are retained on disk.

When game over opens, gameplay music stops and menu music resumes.

Closing the application during an active game records the current score when the player is alive and has scored at least one point.
