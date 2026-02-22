import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {
    private final JLabel scoreLabel;
    private final Game game;

    public GameOverPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(new Color(30, 30, 40));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("GAME OVER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        scoreLabel.setForeground(new Color(220, 220, 220));
        gbc.gridy = 1;
        add(scoreLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton newGameButton = new JButton("NEW GAME");
        newGameButton.setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        newGameButton.addActionListener(e -> game.restartGame());

        JButton quitButton = new JButton("QUIT");
        quitButton.setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        quitButton.addActionListener(e -> game.quit());

        buttonPanel.add(newGameButton);
        buttonPanel.add(quitButton);

        gbc.gridy = 2;
        add(buttonPanel, gbc);
    }

    public void setScore(int score) {
        scoreLabel.setText("Score: " + score);
    }
}
