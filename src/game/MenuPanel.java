import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public MenuPanel(Game game){
        //for .pack() to get the preferred size of the window
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(Color.DARK_GRAY);
        setLayout(new GridBagLayout()); // Centers the button

        JButton playButton = new JButton("PLAY GAME");
        playButton.setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));

        // When clicked, tell the Game class to swap screens
        playButton.addActionListener(e -> game.showGame());

        add(playButton);
    }
}
