import javax.swing.*;

public class Game extends JFrame {
    public Game() {
        GamePanel panel = new GamePanel();
        //adding panel to the center of this JFrame
        this.add(panel);

        this.setTitle("Astroids");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); // Prevents layout glitches during gameplay

        this.pack(); // Sizes the window to fit the preferred size of its components
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        panel.startGame();
    }
}
