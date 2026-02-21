import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private final Timer gameTimer;
    private static final int FPS = 60;
    private static final int FRAME_DELAY = 1000 / FPS;

    private boolean leftPressed, rightPressed, upPressed, downPressed, shootPressed;

    private Player player;
    private List<Asteroid> asteroids;
    private List<Bullet> bullets;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initGame();

        gameTimer = new Timer(FRAME_DELAY, this);
        gameTimer.start();
    }

    private void initGame() {
        player = new Player();
        asteroids = new ArrayList<>();
        bullets = new ArrayList<>();
    }

    // called every frame
    @Override
    public void actionPerformed(ActionEvent e) {
        update();
    }

    private void update() {

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // refactor
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;
            case KeyEvent.VK_SPACE:
                shootPressed = true;
                break;
        }
    }

    // refactor
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;
            case KeyEvent.VK_SPACE:
                shootPressed = false;
                break;
        }
    }
}
