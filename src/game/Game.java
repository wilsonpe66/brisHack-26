import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);
    private final GamePanel gamepanel;

    public Game() {
        MenuPanel menupanel = new MenuPanel(this);
        gamepanel = new GamePanel();

        mainContainer.add(menupanel, "MENU");
        mainContainer.add(gamepanel, "GAME");
        //adding panel to the center of this JFrame
        this.add(mainContainer);

        this.setTitle("Astroids");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false); // Prevents layout glitches during gameplay

        this.pack(); // Sizes the window to fit the preferred size of its components
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }
    public void showGame(){
        cardLayout.show(mainContainer, "GAME");
        gamepanel.startGame(); // start timer and asteroid spawning only when playing
        // Focus is important for KeyListeners to work
        mainContainer.getComponent(1).requestFocusInWindow();
    }
}
