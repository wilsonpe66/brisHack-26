package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import leaderboard.LeaderBoard;
import utils.Constants;
import utils.CustomFonts;

public class GameOverPanel extends JPanel {

    private final JLabel scoreLabel;
    private final JTextArea highScoreLabel;
    private final Game game;

    public GameOverPanel(Game game) {
        this.game = game;
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(new Color(30, 30, 40));
        // GridBagLayout centres components and allows flexible grid positioning
        setLayout(new GridBagLayout());
        // GridBagConstraints controls where each component sits in the grid
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // all components in column 0 (single column layout)
        // Insets(top, left, bottom, right) — adds spacing around each component
        gbc.insets = new Insets(10, 0, 10, 0);

        final JLabel titleLabel = new JLabel("GAME OVER");
        titleLabel.setFont(CustomFonts.TITLE);
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0; // row 0
        add(titleLabel, gbc);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(CustomFonts.PLAIN_28);
        scoreLabel.setForeground(new Color(220, 220, 220));
        gbc.gridy = 1; // row 1
        add(scoreLabel, gbc);

        highScoreLabel = new JTextArea("High Score: 0", 21, 80);
        highScoreLabel.setPreferredSize(new Dimension(400, 400));
        highScoreLabel.setFont(CustomFonts.PLAIN_22);
        highScoreLabel.setForeground(new Color(255, 215, 0)); // gold colour
//        gbc.gridy = 2; // row 2
//        add(highScoreLabel, gbc);

        // FlowLayout(CENTER, hgap, vgap) places buttons side-by-side, centred
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        // setOpaque(false) makes the panel transparent so the parent's background shows through
        buttonPanel.setOpaque(false);

        final Dimension preferredSize = new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);

        final JButton newGameButton = new JButton("NEW GAME");
        newGameButton.setPreferredSize(preferredSize);
        newGameButton.addActionListener(_ -> game.restartGame());

        final JButton quitButton = new JButton("QUIT");
        quitButton.setPreferredSize(preferredSize);
        quitButton.addActionListener(_ -> game.quit());

        buttonPanel.add(newGameButton);
        buttonPanel.add(quitButton);

        gbc.gridy = 2; // row 3
        add(buttonPanel, gbc);
    }

    public void setScore(int score, final LeaderBoard leaderBoard) {
        scoreLabel.setText("Score: " + score);
//        highScoreLabel.setText("High Score: " + leaderBoard);
    }
}
