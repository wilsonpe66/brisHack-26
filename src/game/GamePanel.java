import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

public class GamePanel extends JPanel implements KeyListener, ActionListener {
    Map<Integer, Consumer<Boolean>> keyMap = new HashMap<>() {{
        put(KeyEvent.VK_W,     v -> upPressed = v);
        put(KeyEvent.VK_UP,    v -> upPressed = v);
        put(KeyEvent.VK_S,     v -> downPressed = v);
        put(KeyEvent.VK_DOWN,  v -> downPressed = v);
        put(KeyEvent.VK_A,     v -> leftPressed = v);
        put(KeyEvent.VK_LEFT,  v -> leftPressed = v);
        put(KeyEvent.VK_D,     v -> rightPressed = v);
        put(KeyEvent.VK_RIGHT, v -> rightPressed = v);
        put(KeyEvent.VK_SPACE, v -> shootPressed = v);
    }};

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
        player = new Player((double) WIDTH / 2, (double) HEIGHT / 2);
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

    @Override
    public void keyPressed(KeyEvent e) {
        Consumer<Boolean> action = keyMap.get(e.getKeyCode());
        if (action != null) action.accept(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Consumer<Boolean> action = keyMap.get(e.getKeyCode());
        if (action != null) action.accept(false);
    }
}
