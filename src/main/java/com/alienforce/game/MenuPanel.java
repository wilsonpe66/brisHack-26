package com.alienforce.game;

import com.alienforce.assets.AssetManager;
import com.alienforce.assets.ImageKey;
import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.utils.Constants;
import com.alienforce.utils.CustomFonts;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {

    private static final Image SPACE_BACKGROUND = AssetManager.getImage(ImageKey.SPACE_BACKGROUND).get();

    public MenuPanel(Game game) {
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(new Color(30, 30, 40));
        // GridBagLayout centres components and allows flexible grid positioning
        setLayout(new GridBagLayout());

        // GridBagConstraints controls where each component sits in the grid
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // all components in column 0 (single column layout)
        // Insets(top, left, bottom, right) — adds spacing around each component
        gbc.insets = new Insets(10, 0, 10, 0);

        final JLabel titleLabel = new JLabel("Alien Force");
        titleLabel.setFont(CustomFonts.TITLE);
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 0; // row 0 of the grid
        add(titleLabel, gbc);

        final JLabel subtitleLabel = new JLabel("Press PLAY to start");
        subtitleLabel.setFont(CustomFonts.PLAIN_28);
        subtitleLabel.setForeground(new Color(220, 220, 220));
        gbc.gridy = 1; // row 1 of the grid
        add(subtitleLabel, gbc);

        // FlowLayout(CENTER, hgap, vgap) places buttons side-by-side, centred
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        // setOpaque(false) makes the panel transparent so the parent's background shows through
        buttonPanel.setOpaque(false);

        final Dimension preferredSize = new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);

        final JButton playButton = new JButton("PLAY GAME");
        playButton.setPreferredSize(preferredSize);
        playButton.addActionListener(_ -> game.showGame());

        final JButton quitButton = new JButton("QUIT");
        quitButton.setPreferredSize(preferredSize);
        quitButton.addActionListener(_ -> game.quit());

        buttonPanel.add(playButton);
        buttonPanel.add(quitButton);

        gbc.gridy = 2; // row 2 of the grid
        add(buttonPanel, gbc);

        // Start menu music immediately when the menu panel is constructed
        SoundManager.play(SoundLoopKey.MENU_MUSIC);
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(SPACE_BACKGROUND, 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
    }
}
