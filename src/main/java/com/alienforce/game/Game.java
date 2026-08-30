package com.alienforce.game;

import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.entities.Player;
import com.alienforce.input.GamePadManager;
import com.alienforce.leaderboard.LeaderboardStore;
import com.alienforce.utils.Settings;
import java.awt.CardLayout;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import lombok.Getter;
import net.java.games.input.Event;

public class Game extends JFrame {

    static final String FULL_SCREEN_PROPERTY = "fullScreen";

    // CardLayout stacks panels on top of each other — only one is visible at a time.
    // Calling cardLayout.show(container, "name") switches which panel is displayed.
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);
    private final GamePanel gamepanel;
    private final GameOverPanel gameOverPanel;
    private final LeaderboardStore leaderboardStore;
    private final GamePadManager gamePadManager = new GamePadManager(this::gamePadEventHandler);
    private Rectangle windowedBounds;
    private GraphicsDevice fullScreenDevice;
    private boolean fullScreen;
    private boolean changingDisplayMode;
    @Getter
    private String playerName;
    private final AtomicBoolean useMenuInputs;

    public Game() {
        leaderboardStore = LeaderboardStore.load();
        playerName = leaderboardStore.playerName();
        final MenuPanel menupanel = new MenuPanel(this);
        gamepanel = new GamePanel(this, leaderboardStore);
        gameOverPanel = new GameOverPanel(this);
        useMenuInputs = new AtomicBoolean(true);

        // Each panel is registered under a string key used by cardLayout.show()
        mainContainer.add(menupanel, "MENU");
        mainContainer.add(gamepanel, "GAME");
        mainContainer.add(gameOverPanel, "GAME OVER");
        this.add(mainContainer);

        setTitle("Alien Force");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                quit();
            }

            public void windowDeactivated(final WindowEvent event) {
                if (changingDisplayMode) {
                    return;
                }
                final WorldState worldState = gamepanel.worldState;
                final Player player = worldState.getPlayer();
                if (player.isAlive() && player.getScore() > 0 && !worldState.isPaused()) {
                    worldState.pause();
                }
            }
        });
        setResizable(false); // Prevents layout glitches during gameplay

        // pack() sizes the JFrame to fit the preferred sizes of its child components
        this.pack();
        // setLocationRelativeTo(null) centres the window on screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        registerGlobalKeyBindings();

        Thread.ofPlatform().start(() -> {
            while (true) {
                if (useMenuInputs.get()) {
                    gamePadManager.update();
                }
            }

        });
    }

    /// Returns the action label for the current display mode.
    ///
    /// @param fullScreen whether fullscreen is currently active
    /// @return `Restore Screen` in fullscreen, otherwise `Full Screen`
    static String fullScreenButtonText(final boolean fullScreen) {
        return fullScreen ? "Restore Screen" : "Full Screen";
    }

    private static String platformUserName() {
        final String platformName = System.getProperty("user.name", "").trim();
        return platformName.length() <= 50 ? platformName : "";
    }

    private void gamePadEventHandler(final Event event) {
        switch (event.getComponent().getName()) {
            case "A", "X", "rz", "Right Thumb", "Start" -> {
                if (event.getValue() > 0) {
                    restartGame();
                }
            }
            default -> {
            }
        }
    }

    /**
     * Register key bindings on the root pane so they work regardless of which panel or component currently has focus.
     */
    private void registerGlobalKeyBindings() {
        final JRootPane rootPane1 = getRootPane();
        final InputMap inputMap = rootPane1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        final ActionMap actionMap = rootPane1.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("pressed M"), "toggleMute");
        actionMap.put("toggleMute", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SoundManager.setMuted(!Settings.muted);
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("pressed F11"), "toggleFullScreen");
        actionMap.put("toggleFullScreen", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                toggleFullScreen();
            }
        });

        inputMap.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "exitFullScreen");
        actionMap.put("exitFullScreen", new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent event) {
                if (fullScreen) {
                    toggleFullScreen();
                }
            }
        });
    }

    /// Toggles fullscreen without changing the monitor's display mode.
    ///
    /// Fullscreen uses the display containing the window. The game panel scales its fixed logical viewport to the available resolution, and leaving fullscreen
    /// restores the window's previous bounds.
    public void toggleFullScreen() {
        changingDisplayMode = true;
        try {
            final boolean previousFullScreen = fullScreen;
            if (fullScreen) {
                leaveFullScreen();
            } else {
                enterFullScreen();
            }
            fullScreen = !fullScreen;
            firePropertyChange(FULL_SCREEN_PROPERTY, previousFullScreen, fullScreen);
            mainContainer.revalidate();
            mainContainer.repaint();
            gamepanel.requestFocusInWindow();
        } finally {
            changingDisplayMode = false;
        }
    }

    /// Returns whether the game is currently displayed in fullscreen mode.
    ///
    /// @return `true` while fullscreen is active; otherwise `false`
    public boolean isFullScreen() {
        return fullScreen;
    }

    /// Enters fullscreen on the display containing the window.
    ///
    /// The current window bounds are saved before changing decoration state. When fullscreen windows are unsupported, this falls back to borderless maximized
    /// mode while preserving the display's current resolution.
    private void enterFullScreen() {
        windowedBounds = getBounds();
        fullScreenDevice = getGraphicsConfiguration().getDevice();
        dispose();
        setUndecorated(true);
        setResizable(false);
        if (fullScreenDevice.isFullScreenSupported()) {
            fullScreenDevice.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }
    }

    /// Leaves fullscreen and restores the previously saved window bounds.
    ///
    /// If no bounds were captured, the frame is packed to its preferred size and centered on the screen.
    private void leaveFullScreen() {
        if (fullScreenDevice != null && fullScreenDevice.getFullScreenWindow() == this) {
            fullScreenDevice.setFullScreenWindow(null);
        }
        dispose();
        setUndecorated(false);
        setResizable(false);
        setExtendedState(JFrame.NORMAL);
        if (windowedBounds == null) {
            pack();
            setLocationRelativeTo(null);
        } else {
            setBounds(windowedBounds);
        }
        setVisible(true);
        fullScreenDevice = null;
    }

    public void showGame() {
        if (!ensurePlayerName()) {
            return;
        }
        useMenuInputs.set(false);
        cardLayout.show(mainContainer, "GAME");
        SoundManager.stop(SoundLoopKey.MENU_MUSIC);
        SoundManager.play(SoundLoopKey.BACK_GROUND);
        gamepanel.startGame();
        // requestFocusInWindow() is required for KeyListener to receive keyboard events.
        // Without focus, key presses go to another component and the player can't move.
        // getComponent(1) retrieves the GamePanel (index 1 in the CardLayout).
        mainContainer.getComponent(1).requestFocusInWindow();
    }

    public void showGameOver(final int level, final int score) {
        useMenuInputs.set(true);
        leaderboardStore.record(playerName, level, score);

        gameOverPanel.setScore(score, level, gamepanel.worldState.getLeaderBoardStore().leaderBoard());
        SoundManager.stop(SoundLoopKey.BACK_GROUND);
        SoundManager.play(SoundLoopKey.MENU_MUSIC);
        cardLayout.show(mainContainer, "GAME OVER");

    }

    public void restartGame() {
        gamepanel.reset();
        showGame();
    }

    public void quit() {
        useMenuInputs.set(false);
        final Player player = gamepanel.worldState.getPlayer();
        if (player.isAlive() && player.getScore() > 0) {
            player.die();
            leaderboardStore.record(
                player.name(), gamepanel.worldState.gameLevel().levelNumber() + 1, player.getScore()
            );
        }
        dispose();
        System.exit(0);
    }

    private boolean ensurePlayerName() {
        final String addNewName = "Add new name...";
        final List<String> choices = new ArrayList<>(leaderboardStore.playerNames());

        if (choices.isEmpty()) {
            final String newName = requestNewName(platformUserName());
            return newName != null && selectPlayerName(newName);
        }

        choices.add(addNewName);
        while (true) {
            final Object selection = javax.swing.JOptionPane.showInputDialog(
                this,
                "Select your player name:",
                "Player name",
                javax.swing.JOptionPane.QUESTION_MESSAGE,
                null,
                choices.toArray(),
                playerName == null ? choices.get(0) : playerName
            );
            if (selection == null) {
                return false;
            }
            if (addNewName.equals(selection)) {
                final String newName = requestNewName("");
                if (newName != null) {
                    return selectPlayerName(newName);
                }
                continue;
            }
            return selectPlayerName(selection.toString());
        }
    }

    private String requestNewName(final String initialName) {
        while (true) {
            final JTextField nameField = new JTextField(initialName, 30);
            final JPanel prompt = new JPanel();
            prompt.add(new JLabel("Unique player name (1–50 characters):"));
            prompt.add(nameField);
            final int result = JOptionPane.showConfirmDialog(
                this,
                prompt,
                "Add player name",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (result != JOptionPane.OK_OPTION) {
                return null;
            }

            final String newName = nameField.getText().trim();
            if (newName.isBlank() || newName.length() > 50 || !leaderboardStore.addPlayerName(newName)) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Names must be unique and between 1 and 50 characters.",
                    "Invalid player name",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                continue;
            }
            return newName;
        }
    }

    private boolean selectPlayerName(final String name) {
        if (!leaderboardStore.selectPlayerName(name)) {
            return false;
        }
        playerName = leaderboardStore.playerName();
        return true;
    }
}
