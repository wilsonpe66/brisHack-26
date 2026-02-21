import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;


public class GamePanel extends JPanel implements ActionListener {
    private final Timer gameTimer;

    private final InputHandler inputHandler;

    // deal with in world state
    private Player player;
    private List<Asteroid> asteroids;
    private List<Bullet> bullets;

    public GamePanel() {
        inputHandler = new InputHandler();

        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(inputHandler);

        // create world state
        initGame();

        gameTimer = new Timer(Constants.FRAME_DELAY, this);
        gameTimer.start();
    }

    private void initGame() {
        player = new Player((double) WIDTH / 2, (double) HEIGHT / 2);
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    // called every frame
    @Override
    public void actionPerformed(ActionEvent e) {
        WorldState.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // iterate using world state
        for (GameObject object : WorldState.Objects) {
            g.drawImage(object.sprite, object.getPositionX(), object.getPositionY(), null);
        }

        // draw hud
    }

    private void draw()
}
