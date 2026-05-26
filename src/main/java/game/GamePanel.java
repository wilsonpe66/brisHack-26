package game;

import entities.GameObject;
import entities.Player;
import utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

import static assets.AssetManager.getImage;

public class GamePanel extends JPanel implements ActionListener {
    private static final Image SPACE_BACKGROUND = getImage("spacebackground.png").get();

    private final Timer gameTimer;
    private final InputHandler inputHandler;
    private final Game game;
    final WorldState worldState;

    public GamePanel(Game game) {
        this.game = game;
        inputHandler = new InputHandler();

        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        setBackground(Color.BLACK);
        // setFocusable(true) allows this panel to receive keyboard events via KeyListener
        setFocusable(true);
        addKeyListener(inputHandler);

        worldState = new WorldState(inputHandler);

        // Swing Timer fires actionPerformed() every FRAME_DELAY ms (~16 ms = 60 FPS).
        // This is the game loop driver — each tick updates state and repaints.
        gameTimer = new Timer(Constants.FRAME_DELAY, this);
        // Timer started in startGame() when user presses Play


    }

    /**
     * Builds an AffineTransform to position, rotate, and scale a sprite.
     * Transforms are applied in reverse order (last added = first applied):
     * 1. translate(-w/2, -h/2) — shift so the sprite's centre is at the origin
     * 2. scale — resize the sprite
     * 3. rotate — rotate around the origin (the object's centre)
     * +270° corrects for sprites that face right by default to face up at angle 0
     * 4. translate(cx, cy) — move the origin to the object's world position
     */
    private static AffineTransform getAffineTransform(final GameObject object, int w, int h) {
        final double cx = object.getPosition().x();
        final double cy = object.getPosition().y();

        final AffineTransform transform = new AffineTransform();
        transform.translate(cx, cy);
        transform.rotate(object.getRotationAngle() + Math.toRadians(270));
        transform.scale(object.getScale(), object.getScale());
        transform.translate(-w / 2.0, -h / 2.0);
        return transform;
    }

    /**
     * Start the game loop and asteroid spawning. Called when switching to game from menu.
     */
    public void startGame() {
        if (!gameTimer.isRunning()) {
            gameTimer.start();
        }
    }

    /**
     * Stop the game loop. Called when player dies.
     */
    public void stopGame() {
        gameTimer.stop();
    }

    /**
     * Reset world state for a new game.
     */
    public void reset() {
        inputHandler.clearAllKeys(); // keys held during game over never got keyReleased (panel lost focus)
        SoundManager.stopLooping("thruster");
        worldState.reset();
    }

    // called every frame
    @Override
    public void actionPerformed(final ActionEvent event) {
        worldState.updateState();
        final Player player = worldState.getPlayer();
        if (player.isDead()) {
            stopGame();
            game.showGameOver(player.getScore());
        }
        repaint();
    }

    // paintComponent is called by Swing whenever the panel needs to be redrawn (e.game. after repaint()).
    // Always call super.paintComponent(game) first to clear the previous frame.
    @Override
    protected void paintComponent(final Graphics game) {
        super.paintComponent(game);

        game.drawImage(SPACE_BACKGROUND, 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
        drawObjects(game);
        drawHud(game);
    }

    private void drawHud(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 24));
        graphics.drawString("Score: " + worldState.getPlayer().getScore(), 20, 40);
        graphics.drawString("Health: " + worldState.getPlayer().getHealth(), 20, 80);
    }

    private void drawObjects(final Graphics graphics) {
        final Graphics2D g2d = (Graphics2D) graphics;

        // iterate over worldState
        for (final GameObject object : worldState.objects) {
            final Image sprite = object.getSprite();
            if (sprite == null) continue;
            final int w = sprite.getWidth(null);
            final int h = sprite.getHeight(null);
            if (w <= 0 || h <= 0) continue; // image not yet loaded
            AffineTransform transform = getAffineTransform(object, w, h);

            g2d.drawImage(sprite, transform, null);
        }
    }

}
