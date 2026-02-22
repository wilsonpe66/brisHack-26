import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(Game game) {
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(new Color(30, 30, 40));
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);

        JLabel titleLabel = new JLabel("ASTROIDS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Press PLAY to start");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        subtitleLabel.setForeground(new Color(220, 220, 220));
        gbc.gridy = 1;
        add(subtitleLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton playButton = new JButton("PLAY GAME");
        playButton.setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        playButton.addActionListener(e -> game.showGame());

        JButton quitButton = new JButton("QUIT");
        quitButton.setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        quitButton.addActionListener(e -> game.quit());

        buttonPanel.add(playButton);
        buttonPanel.add(quitButton);

        gbc.gridy = 2;
        add(buttonPanel, gbc);
        SoundManager.playSound("assets/sounds/space_oddity.wav");
    }
}
