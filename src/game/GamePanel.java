import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel implements ActionListener {
    private static final Image SPACE_BACKGROUND = Toolkit.getDefaultToolkit().getImage("assets/images/spacebackground.png");
    private final Timer gameTimer;
    private final InputHandler inputHandler;
    private final Game game;
    WorldState worldState;

    public GamePanel(Game game) {
        this.game = game;
        inputHandler = new InputHandler();

        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(inputHandler);

        worldState = new WorldState(inputHandler);

        gameTimer = new Timer(Constants.FRAME_DELAY, this);
        // Timer started in startGame() when user presses Play


    }

    /** Start the game loop and asteroid spawning. Called when switching to game from menu. */
    public void startGame() {
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }

    /** Stop the game loop. Called when player dies. */
    public void stopGame() {
        gameTimer.stop();
    }

    /** Reset world state for a new game. */
    public void reset() {
        inputHandler.clearAllKeys(); // keys held during game over never got keyReleased (panel lost focus)
        worldState.reset();
    }

    // called every frame
    @Override
    public void actionPerformed(ActionEvent e) {
        worldState.updateState();
        if (!worldState.getPlayer().getIsAlive()) {
            stopGame();
            game.showGameOver(worldState.getPlayer().getScore());
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (SPACE_BACKGROUND != null) {
            g.drawImage(SPACE_BACKGROUND, 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
        }
        drawObjects(g);
        drawHud(g);
    }

    private void drawHud(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + worldState.getPlayer().getScore(), 20, 40);
    }

    private void drawObjects(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // iterate over worldState
        for (GameObject object : worldState.objects) {
            Image sprite = object.getSprite();
            if (sprite == null) continue;
            int w = sprite.getWidth(null);
            int h = sprite.getHeight(null);
            if (w <= 0 || h <= 0) continue; // image not yet loaded
            AffineTransform transform = getAffineTransform(object, w, h);

            g2d.drawImage(sprite, transform, null);
        }
    }

    private static AffineTransform getAffineTransform(GameObject object, int w, int h) {
        double cx = object.getPositionX();
        double cy = object.getPositionY();

        AffineTransform transform = new AffineTransform();
        transform.translate(cx, cy);                 // move origin to object center
        transform.rotate(object.getRotationAngle() + Math.toRadians(270)); // rotate around center
        transform.scale(object.getScale(), object.getScale()); // apply scale
        transform.translate(-w / 2.0, -h / 2.0);    // offset so sprite draws centered
        return transform;
    }

}
