package com.alienforce.game;

import com.alienforce.assets.AssetManager;
import com.alienforce.assets.ImageKey;
import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.utils.Constants;
import com.alienforce.utils.CustomFonts;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import static com.alienforce.assets.AssetManager.getImage;

public class MenuPanel extends JPanel {

    private static final Image SPACE_BACKGROUND = AssetManager.getImage(ImageKey.SPACE_BACKGROUND).get();
    private static final Image BOSS_ALIEN = getImage(ImageKey.ALIEN_BOSS_5).get();

    public MenuPanel(final Game game) {
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

        // FlowLayout(CENTER, hgap, vgap) places buttons side-by-side, centred
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        // setOpaque(false) makes the panel transparent so the parent's background shows through
        buttonPanel.setOpaque(false);

        final JButton fullScreenButton = new RoundedButton(
                Game.fullScreenButtonText(game.isFullScreen()), game::toggleFullScreen
        );
        game.addPropertyChangeListener(
                Game.FULL_SCREEN_PROPERTY,
                event -> fullScreenButton.setText(Game.fullScreenButtonText((boolean) event.getNewValue()))
        );

        buttonPanel.add(new RoundedButton("PLAY GAME", game::showGameSwitchUser));
        buttonPanel.add(fullScreenButton);
        buttonPanel.add(new RoundedButton("GIVE UP",  game::quit));

        gbc.gridy = 2; // row 2 of the grid
        add(buttonPanel, gbc);

        // Start menu music immediately when the menu panel is constructed
        SoundManager.play(SoundLoopKey.MENU_MUSIC);
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        super.paintComponent(graphics);
        final int windowHeight = getHeight();
        final int windowWidth = getWidth();
        graphics.drawImage(SPACE_BACKGROUND, 0, 0, windowWidth, windowHeight, this);
        graphics.drawImage(BOSS_ALIEN, (windowWidth - windowHeight) / 2, 0, windowHeight, windowHeight, this);
    }
}
