import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(Game game) {
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(new Color(30, 30, 40));
        // GridBagLayout centres components and allows flexible grid positioning
        setLayout(new GridBagLayout());

        // GridBagConstraints controls where each component sits in the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // all components in column 0 (single column layout)
        // Insets(top, left, bottom, right) — adds spacing around each component
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("(git) Push To Orbit");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0; // row 0 of the grid
        add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Press PLAY to start");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        gbc.gridy = 1; // row 1 of the grid
        add(subtitleLabel, gbc);

        // FlowLayout(CENTER, hgap, vgap) places buttons side-by-side, centred
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        // setOpaque(false) makes the panel transparent so the parent's background shows through
        buttonPanel.setOpaque(false);

        JButton playButton = new JButton("PLAY GAME");
        playButton.setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        playButton.addActionListener(e -> game.showGame());

        JButton quitButton = new JButton("QUIT");
        quitButton.setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        quitButton.addActionListener(e -> game.quit());

        buttonPanel.add(playButton);
        buttonPanel.add(quitButton);

        gbc.gridy = 2; // row 2 of the grid
        add(buttonPanel, gbc);

        // Start menu music immediately when the menu panel is constructed
        SoundManager.playLooping("menu_music", "assets/sounds/space_oddity.wav");
    }
}
