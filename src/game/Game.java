import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);
    private final GamePanel gamepanel;
    private final GameOverPanel gameOverPanel;

    public Game() {
        MenuPanel menupanel = new MenuPanel(this);
        gamepanel = new GamePanel(this);
        gameOverPanel = new GameOverPanel(this);

        mainContainer.add(menupanel, "MENU");
        mainContainer.add(gamepanel, "GAME");
        mainContainer.add(gameOverPanel, "GAME OVER");
        //adding panel to the center of this JFrame
        this.add(mainContainer);

        this.setTitle("Asteroids");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); // Prevents layout glitches during gameplay

        this.pack(); // Sizes the window to fit the preferred size of its components
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }
    public void showGame() {
        cardLayout.show(mainContainer, "GAME");
        SoundManager.playSound("assets/sounds/background.wav");
        gamepanel.startGame(); // start timer and asteroid spawning only when playing
        // Focus is important for KeyListeners to work
        mainContainer.getComponent(1).requestFocusInWindow();
    }

    public void showGameOver(int score) {
        gameOverPanel.setScore(score);
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
