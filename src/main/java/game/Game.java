package game;

import assets.SoundManager;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import leaderboard.PlayerScore;
import utils.Settings;

public class Game extends JFrame {

    // CardLayout stacks panels on top of each other — only one is visible at a time.
    // Calling cardLayout.show(container, "name") switches which panel is displayed.
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);
    private final GamePanel gamepanel;
    private final GameOverPanel gameOverPanel;

    public Game() {
        final MenuPanel menupanel = new MenuPanel(this);
        gamepanel = new GamePanel(this);
        gameOverPanel = new GameOverPanel(this);

        // Each panel is registered under a string key used by cardLayout.show()
        mainContainer.add(menupanel, "MENU");
        mainContainer.add(gamepanel, "GAME");
        mainContainer.add(gameOverPanel, "GAME OVER");
        this.add(mainContainer);

        this.setTitle("Asteroids");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); // Prevents layout glitches during gameplay

        // pack() sizes the JFrame to fit the preferred sizes of its child components
        this.pack();
        // setLocationRelativeTo(null) centres the window on screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        registerGlobalKeyBindings();
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
        cardLayout.show(mainContainer, "GAME");
        SoundManager.stopLooping("menu_music");
        SoundManager.playLooping("background", "background.wav");
        gamepanel.startGame();
        // requestFocusInWindow() is required for KeyListener to receive keyboard events.
        // Without focus, key presses go to another component and the player can't move.
        // getComponent(1) retrieves the GamePanel (index 1 in the CardLayout).
        mainContainer.getComponent(1).requestFocusInWindow();
    }

    public void showGameOver(int score) {
        gamepanel.worldState.getLeaderBoard().scores().add(
            PlayerScore
                .builder()
                .name("Pete")
                .score(score)
                .build()
        );

        gameOverPanel.setScore(score, gamepanel.worldState.getLeaderBoard());
        SoundManager.stopLooping("background");
        SoundManager.playLooping("menu_music", "mixkit-fright-night-871.wav");
        cardLayout.show(mainContainer, "GAME OVER");

    }

    public void restartGame() {
        gamepanel.reset();
        showGame();
    }

    public void quit() {
        dispose();
        System.exit(0);
    }
}