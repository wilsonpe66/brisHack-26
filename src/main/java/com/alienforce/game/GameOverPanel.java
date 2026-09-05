package com.alienforce.game;

import com.alienforce.leaderboard.LeaderBoard;
import com.alienforce.utils.Constants;
import com.alienforce.utils.CustomFonts;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

public class GameOverPanel extends JPanel {

    private final JLabel scoreLabel;
    private final DefaultTableModel leaderboardModel;
    private final Game game;

    public GameOverPanel(final Game game) {
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

        leaderboardModel = new DefaultTableModel(new Object[]{"Player", "Score", "Level", "Date"}, 0) {
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

        final JButton newGameButtonSameUser = new RoundedButton("Retry");
        newGameButtonSameUser.addActionListener(_ -> game.restartGameSameUser());

        final JButton newGameButtonSwitchUser = new RoundedButton("Switch User");
        newGameButtonSwitchUser.addActionListener(_ -> game.restartGame());

        final JButton quitButton = new RoundedButton("GIVE UP");
        quitButton.addActionListener(_ -> game.quit());

        final JButton fullScreenButton = new RoundedButton(Game.fullScreenButtonText(game.isFullScreen()));
        fullScreenButton.addActionListener(_ -> game.toggleFullScreen());
        game.addPropertyChangeListener(
                Game.FULL_SCREEN_PROPERTY,
                event -> fullScreenButton.setText(Game.fullScreenButtonText((boolean) event.getNewValue()))
        );

        buttonPanel.add(newGameButtonSameUser);
        buttonPanel.add(newGameButtonSwitchUser);
        buttonPanel.add(fullScreenButton);
        buttonPanel.add(quitButton);

        gbc.gridy = 4;
        add(buttonPanel, gbc);
    }

    public void setScore(final int score, final int level, final LeaderBoard leaderBoard) {
        scoreLabel.setText("Score: %,d, Level: %d".formatted(score, level));

        leaderboardModel.setRowCount(0);
        final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd");
        final AtomicInteger rank = new AtomicInteger(1);
        leaderBoard.getTopScorers(10)
                .forEach(playerScore -> leaderboardModel.addRow(new Object[]{
                        playerScore.name(),
                        "%,d".formatted(playerScore.score()),
                        playerScore.level(),
                        playerScore.createTime().format(dateFormatter)
                }));
    }
}
