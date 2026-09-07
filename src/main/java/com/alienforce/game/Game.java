package com.alienforce.game;

import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.entities.Player;
import com.alienforce.input.GamePadManager;
import com.alienforce.leaderboard.LeaderboardStore;
import com.alienforce.utils.Settings;
import lombok.Getter;
import net.java.games.input.Event;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.IntStream;

public class Game extends JFrame {

    static final String FULL_SCREEN_PROPERTY = "fullScreen";
    private static final Collector<String, DefaultListModel<String>, DefaultListModel<String>> stringDefaultListModelCollector = Collector.of(
            DefaultListModel::new,
            (a, b) -> a.add(a.size(), b),
            (a, b) -> {
                IntStream.range(0, a.size()).forEach(i -> a.add(i, b.get(i)));
                return a;
            }
    );
    private static final Font ARIAL_FONT = new Font("Arial", Font.PLAIN, 20);
    private static final Font ARIA_BOLD = ARIAL_FONT.deriveFont(Font.BOLD);
    // CardLayout stacks panels on top of each other — only one is visible at a time.
    // Calling cardLayout.show(container, "name") switches which panel is displayed.
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);
    private final GamePanel gamepanel;
    private final GameOverPanel gameOverPanel;
    private final LeaderboardStore leaderboardStore;
    private final GamePadManager gamePadManager = new GamePadManager(this::gamePadEventHandler);
    private final AtomicBoolean useMenuInputs;
    private Rectangle windowedBounds;
    private GraphicsDevice fullScreenDevice;
    /**
     * -- GETTER --
     * Returns whether the game is currently displayed in fullscreen mode.
     * ///
     * ///
     *
     * @return `true` while fullscreen is active; otherwise `false`
     */
    @Getter
    private boolean fullScreen;
    private boolean changingDisplayMode;
    @Getter
    private String playerName;

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

    private static JPanel getBlackPanel() {
        final JPanel blackPanel = new JPanel(new FlowLayout());
        blackPanel.setBackground(Color.BLACK);
        return blackPanel;
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

    public void showGameSameUser() {
        if (playerName == null || playerName.isBlank()) {
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

    public void showGameSwitchUser() {
        if (!ensurePlayerName()) {
            return;
        }
        showGameSameUser();
    }

    public void showGameOver(final int level, final int score) {
        useMenuInputs.set(true);
        leaderboardStore.record(playerName, level, score);

        gameOverPanel.setScore(score, level, gamepanel.worldState.getLeaderBoardStore().leaderBoard());
        SoundManager.stop(SoundLoopKey.BACK_GROUND);
        SoundManager.play(SoundLoopKey.MENU_MUSIC);
        cardLayout.show(mainContainer, "GAME OVER");

    }

    public void restartGameSameUser() {
        gamepanel.reset();
        showGameSameUser();
    }

    public void restartGame() {
        gamepanel.reset();
        showGameSwitchUser();
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
        final List<String> choices = new ArrayList<>(leaderboardStore.playerNames());

        if (choices.isEmpty()) {
            return requestNewName(this, platformUserName())
                    .map(this::selectPlayerName)
                    .orElse(false);
        }

        while (true) {
            final JDialog modalDialog = new JDialog(this, "Select Player", true);
            modalDialog.setMinimumSize(new Dimension(700, 440));
            modalDialog.setLocationRelativeTo(this);

            final JPanel prompt = (JPanel) modalDialog.add(getBlackPanel());
            prompt.setLayout(new BoxLayout(prompt, BoxLayout.Y_AXIS));

            final JLabel message = (JLabel) prompt.add(new JLabel("A player must be selected to play!"));
            message.setFont(ARIA_BOLD);
            message.setForeground(Color.RED);
            message.setVisible(false);

            final JLabel label = (JLabel) prompt.add(new JLabel("Player name"));
            final JPanel subPanel = (JPanel) prompt.add(getBlackPanel());
            label.setFont(ARIA_BOLD);
            label.setForeground(Color.YELLOW);

            final JList<String> nameField = new JList<>(
                    choices
                            .stream()
                            .sorted(String::compareToIgnoreCase)
                            .collect(stringDefaultListModelCollector)
            );

            final JScrollPane nameScrollPane = new JScrollPane(nameField);
            subPanel.add(nameScrollPane);

            nameField.setFont(ARIA_BOLD);
            nameField.setBackground(Color.GRAY);
            nameField.setForeground(Color.YELLOW);
            nameField.scrollRectToVisible(new Rectangle(0, 0, 600, 6));
            nameField.setPreferredSize(new Dimension(680, 300));
            nameField.setSelectedValue(playerName, true);

            final var optNewName = new AtomicReference<Optional<String>>(Optional.empty());

            final JPanel buttonPanel = (JPanel) prompt.add(getBlackPanel());
            buttonPanel.add(new RoundedButton("Play", () -> {
                final Optional<String> selectedValue = Optional.ofNullable(nameField.getSelectedValue());
                if (selectedValue.isPresent()) {
                    optNewName.set(selectedValue);
                    modalDialog.dispose();
                    return;
                }
                message.setVisible(true);
            }));
            buttonPanel.add(new RoundedButton("Add New Player", () -> {
                requestNewName(this, platformUserName())
                        .ifPresent(newName -> {
                            optNewName.set(Optional.of(newName));
                            modalDialog.dispose();
                        });
            }));
            buttonPanel.add(new RoundedButton("Cancel", () -> {
                optNewName.set(Optional.empty());
                modalDialog.dispose();
            }));

            // This line blocks user interaction with the main frame until closed
            modalDialog.setVisible(true);
            nameField.requestFocus();
            modalDialog.repaint();

            final var selection = optNewName.get();

            if (selection.isEmpty()) {
                playerName = null;
                return false;
            }

            return selectPlayerName(selection.get());
        }
    }

    private Optional<String> requestNewName(final Frame parent, final String initialName) {
        final JDialog modalDialog = new JDialog(parent, "Add player name", true);
        modalDialog.setMinimumSize(new Dimension(700, 150));
        modalDialog.setLocationRelativeTo(this);

        final JPanel prompt = (JPanel) modalDialog.add(getBlackPanel());

        final JLabel message = (JLabel) prompt.add(new JLabel("Player name must unique and (1–50 characters)"));
        message.setFont(ARIA_BOLD);
        message.setForeground(Color.RED);
        message.setVisible(false);

        final JPanel subPanel = (JPanel) prompt.add(getBlackPanel());

        final JLabel label = (JLabel) subPanel.add(new JLabel("Player name"));
        label.setFont(ARIA_BOLD);
        label.setForeground(Color.YELLOW);

        final JTextField nameField = (JTextField) subPanel.add(new JTextField(initialName, 25));
        nameField.setFont(ARIAL_FONT);
        nameField.setBackground(Color.GRAY);
        nameField.setForeground(Color.YELLOW);
        nameField.setPreferredSize(new Dimension(200, 30));

        final var optNewName = new AtomicReference<Optional<String>>();

        final JPanel buttonPanel = (JPanel) prompt.add(getBlackPanel());
        buttonPanel.add(new RoundedButton("Confirm", () -> {
            final Optional<String> newValue = Optional.ofNullable(nameField.getText())
                    .map(String::trim)
                    .filter(value -> value.length() <= 50)
                    .filter(Predicate.not(String::isEmpty));
            if (newValue.isPresent() && leaderboardStore.addPlayerName(newValue.get())) {
                modalDialog.dispose();
                optNewName.set(newValue);
                return;
            }
            nameField.requestFocus();
            message.setVisible(true);
        }));
        buttonPanel.add(new RoundedButton("Cancel", modalDialog::dispose));

        // This line blocks user interaction with the main frame until closed
        modalDialog.setVisible(true);
        nameField.requestFocus();
        return optNewName.get();
    }

    private boolean selectPlayerName(final String name) {
        if (!leaderboardStore.selectPlayerName(name)) {
            return false;
        }
        playerName = leaderboardStore.playerName();
        return true;
    }
}
