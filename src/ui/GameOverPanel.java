import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    public GameOverPanel(ui.ScreenManager manager) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);

        JLabel title = new JLabel("GAME OVER");
        title.setFont(new Font("Arial", Font.BOLD, 72));
        title.setForeground(Color.RED);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton menu = new JButton("MAIN MENU");
        menu.setFont(new Font("Arial", Font.BOLD, 28));
        menu.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.addActionListener(e -> manager.showScreen("menu"));

        add(Box.createVerticalGlue());
        add(title);
        add(Box.createRigidArea(new Dimension(0,40)));
        add(menu);
        add(Box.createVerticalGlue());
    }
}
