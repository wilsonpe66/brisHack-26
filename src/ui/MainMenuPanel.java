import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {

    public MainMenuPanel(ScreenManager manager) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);
        SoundManager.playSound("sounds/gameover_sound.wav");
        JLabel title = new JLabel("ASTEROIDS");
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton play = createButton("PLAY");
        JButton quit = createButton("QUIT");

        play.addActionListener(e -> manager.showScreen("game"));
        quit.addActionListener(e -> System.exit(0));

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0,40)));
        add(play);
        add(Box.createRigidArea(new Dimension(0,20)));
        add(quit);
        add(Box.createVerticalGlue());
    }

    private JButton createButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 32));
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        return b;
    }
}