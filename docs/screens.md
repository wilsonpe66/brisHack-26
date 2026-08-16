# Screens and UI

`Game` presents three panels in a `CardLayout`. The fixed-size window is 1500 × 900 pixels, non-resizable, and centered on screen.

## Menu

`MenuPanel` draws the space background and displays:

- the title **Alien Force**
- the subtitle **Press PLAY to start**
- PLAY GAME and QUIT buttons

Menu music starts when the panel is created. PLAY opens the player-name flow before starting the game. If no names exist, the platform user name is offered; otherwise the user can select an existing name or add a unique 1–50 character name.

## Gameplay

`GamePanel` renders, in order:

1. the stretched space-background image
2. three colored `BackgroundStar` objects
3. every live sprite using position, rotation, and scale transforms
4. the HUD and, when applicable, an animated PAUSED overlay

The HUD shows the score and player name on the left, level in the center, and a red/green health bar on the right.

## Game over

When player health reaches zero, the game timer stops and the score is persisted. `GameOverPanel` displays:

- the last score
- the highest score and player name
- a non-editable table of up to ten top scores with rank, player, score, and timestamp
- NEW GAME and QUIT buttons

The ranking sorts by score descending, then timestamp descending. Only the ten most recent score records are retained on disk, so the displayed top ten is ranked from that retained set.

When game over opens, gameplay music stops and menu music resumes.
