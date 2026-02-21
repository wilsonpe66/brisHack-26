import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {
    private final Timer gameTimer;

    private final InputHandler inputHandler;

    WorldState worldState;

    public GamePanel() {
        inputHandler = new InputHandler();

        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(inputHandler);

        worldState = new WorldState();

        gameTimer = new Timer(Constants.FRAME_DELAY, this);
        gameTimer.start();
    }

    // called every frame
    @Override
    public void actionPerformed(ActionEvent e) {
        worldState.updateState();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // iterate using world state
        for (GameObject object : worldState.objects) {
            g.drawImage(object.sprite, object.getPositionX(), object.getPositionY(), null);
        }

        // draw hud
    }

    private void draw()
}
