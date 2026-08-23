package com.alienforce.game;

import com.alienforce.leaderboard.LeaderBoard;
import com.alienforce.utils.Constants;
import com.alienforce.utils.CustomFonts;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class GameOverPanel extends JPanel {

    private final JLabel scoreLabel;
    private final DefaultTableModel leaderboardModel;
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

        leaderboardModel = new DefaultTableModel(new Object[]{"#", "Player", "Score", "Date"}, 0) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };
        final JTable leaderboardTable = new JTable(leaderboardModel);
        leaderboardTable.setFont(CustomFonts.PLAIN_22);
        leaderboardTable.setRowHeight(28);
        leaderboardTable.getTableHeader().setFont(CustomFonts.PLAIN_22);
        leaderboardTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leaderboardTable.setFillsViewportHeight(true);
        final JScrollPane tableScrollPane = new JScrollPane(leaderboardTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 310));
        gbc.gridy = 3;
        add(tableScrollPane, gbc);

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

        final JButton fullScreenButton = new JButton("FULL SCREEN");
        fullScreenButton.setPreferredSize(preferredSize);
        fullScreenButton.addActionListener(_ -> game.toggleFullScreen());

        buttonPanel.add(newGameButton);
        buttonPanel.add(fullScreenButton);
        buttonPanel.add(quitButton);

        gbc.gridy = 4;
        add(buttonPanel, gbc);
    }

    public void setScore(int score, final LeaderBoard leaderBoard) {
        scoreLabel.setText("Score: %,d".formatted(score));

        leaderboardModel.setRowCount(0);
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd");
        final AtomicInteger rank = new AtomicInteger(1);
        leaderBoard.getTopScorers(10)
            .forEach(playerScore -> leaderboardModel.addRow(new Object[]{
                rank.getAndIncrement(),
                playerScore.name(),
                "%,d".formatted(playerScore.score()),
                playerScore.createTime().format(dateFormatter)
            }));
    }
}
