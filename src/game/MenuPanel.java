import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(Game game){
        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.DARK_GRAY);
        setLayout(new GridBagLayout()); // Centers the button

        JButton playButton = new JButton("PLAY GAME");
        playButton.setPreferredSize(new Dimension(200, 50));

        // When clicked, tell the Game class to swap screens
        playButton.addActionListener(e -> game.showGame());

        add(playButton);
    }
}
