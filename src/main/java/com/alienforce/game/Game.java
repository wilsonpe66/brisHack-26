package com.alienforce.game;

import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.input.GamePadManager;
import com.alienforce.leaderboard.LeaderboardStore;
import com.alienforce.utils.Settings;
import java.awt.CardLayout;
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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import net.java.games.input.Event;

public class Game extends JFrame {

    // CardLayout stacks panels on top of each other — only one is visible at a time.
    // Calling cardLayout.show(container, "name") switches which panel is displayed.
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);
    private final GamePanel gamepanel;
    private final GameOverPanel gameOverPanel;
    private final LeaderboardStore leaderboardStore;
    private String playerName;
    private AtomicBoolean useMenuInputs;
    private final GamePadManager gamePadManager = new GamePadManager(this::gamePadEventHandler);

    public Game() {
        leaderboardStore = LeaderboardStore.load();
        playerName = leaderboardStore.playerName();
        final MenuPanel menupanel = new MenuPanel(this);
        gamepanel = new GamePanel(this, leaderboardStore.leaderBoard());
        gameOverPanel = new GameOverPanel(this);
        useMenuInputs = new AtomicBoolean(true);

        // Each panel is registered under a string key used by cardLayout.show()
        mainContainer.add(menupanel, "MENU");
        mainContainer.add(gamepanel, "GAME");
        mainContainer.add(gameOverPanel, "GAME OVER");
        add(mainContainer);

        setTitle("Alien Force");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent event) {
                quit();
            }

            @Override
            public void windowLostFocus(final WindowEvent event) {
                gamepanel.worldState
            }
        });
        setResizable(false); // Prevents layout glitches during gameplay

        // pack() sizes the JFrame to fit the preferred sizes of its child components
        pack();
        // setLocationRelativeTo(null) centres the window on screen
        setLocationRelativeTo(null);
        setVisible(true);

        registerGlobalKeyBindings();

        Thread.ofPlatform().start(() -> {
            while (true) {
                if (useMenuInputs.get()) {
                    gamePadManager.update();
                }
            }

        });
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

    public void showGameOver(int score) {
        useMenuInputs.set(true);
        leaderboardStore.record(playerName, score);

        gameOverPanel.setScore(score, gamepanel.worldState.getLeaderBoard());
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
        JOptionPane.showMessageDialog(this, "Thank you");
        dispose();
        System.exit(0);
    }

    public String getPlayerName() {
        return playerName;
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

    private static String platformUserName() {
        final String platformName = System.getProperty("user.name", "").trim();
        return platformName.length() <= 50 ? platformName : "";
    }

    private boolean selectPlayerName(final String name) {
        if (!leaderboardStore.selectPlayerName(name)) {
            return false;
        }
        playerName = leaderboardStore.playerName();
        return true;
    }
}
