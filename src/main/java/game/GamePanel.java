package game;

import assets.ImageKey;
import assets.SoundEffectKey;
import assets.SoundLoopKey;
import assets.SoundManager;
import entities.BackgroundStar;
import entities.GameObject;
import entities.Player;
import entities.motion.Position;
import utils.Constants;
import utils.CustomFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Objects;

import static assets.AssetManager.getImage;

public class GamePanel extends JPanel implements ActionListener {

    private static final Image SPACE_BACKGROUND = getImage(ImageKey.SPACE_BACKGROUND).get();
    private static double pauseTime = 0;
    final WorldState worldState;
    private final Timer gameTimer;
    private final InputHandler inputHandler;
    private final Game game;

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

    /// Builds an AffineTransform to position, rotate, and scale a sprite. Transforms are applied in reverse order (last added = first applied):
    ///
    /// - translate(-w/2, -h/2) — shift so the sprite's centre is at the origin
    /// - scale — resize the sprite
    /// - rotate — rotate around the origin (the object's centre)
    ///
    /// +270° corrects for sprites that face right by default to face up at angle 0
    ///      * 4. translate(cx, cy) — move the origin to the object's world position
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

    private static void drawHealthBar(final Graphics graphics, final Player player) {
        graphics.setColor(Color.RED);
        final int X_RIGHT_MARGIN = 10;
        final int MAX_HEALTH = 100;
        final int startX = Constants.WIDTH - 2 * MAX_HEALTH - X_RIGHT_MARGIN;

        graphics.fillRect(startX, 20, 2 * MAX_HEALTH, 10);
        graphics.setColor(Color.GREEN);
        graphics.fillRect(startX, 20, 2 * player.getHealth(), 10);
    }

    private static double getColor(final double scale, double startColor, double endColor) {
        return (endColor - startColor) * scale + startColor;
    }

    private static Color getColor(final double scale, Color startColor, Color endColor) {
        return new Color(
                (int) getColor(scale, startColor.getRed(), endColor.getRed()),
                (int) getColor(scale, startColor.getGreen(), endColor.getGreen()),
                (int) getColor(scale, startColor.getBlue(), endColor.getBlue())
        );
    }

    private static Color getColorLoop(final double scale) {
        if (scale < 1) {
            return getColor(scale, Color.YELLOW, Color.CYAN);
        } else if (scale < 2) {
            return getColor(scale - 1, Color.CYAN, Color.RED);
        } else {
            return getColor(scale - 2, Color.RED, Color.YELLOW);
        }
    }

    private static void showPauseAction(final Graphics graphics) {
        graphics.setFont(CustomFonts.TITLE);
        pauseTime += .02;
        if (pauseTime > 3) {
            pauseTime -= 3;
        }
        graphics.setColor(Color.BLACK);
        graphics.drawString("PAUSED", Constants.WIDTH / 2 - 400 + 2, (Constants.HEIGHT + CustomFonts.TITLE.getSize()) / 2 + 2);

        graphics.setColor(getColorLoop(pauseTime));
        graphics.drawString("PAUSED", Constants.WIDTH / 2 - 400, (Constants.HEIGHT + CustomFonts.TITLE.getSize()) / 2);
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
        SoundManager.stop(SoundLoopKey.THRUSTER);
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

        drawBackground(game);
        drawObjects(game);
        drawHud(game);
    }

    private void drawBackground(Graphics game) {
        game.drawImage(SPACE_BACKGROUND, 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
    }

    private void drawHud(final Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(CustomFonts.PLAIN_24);
        final Player player = worldState.getPlayer();
        final int levelNumber = worldState.gameLevel().LEVEL_NUMBER() + 1;
        graphics.drawString("Score: %,d".formatted(player.getScore()), 20, 40);
        graphics.setColor(Color.YELLOW);
        graphics.setFont(CustomFonts.HUD_TITLE);
        final String levelAsLabel = "Level: %s".formatted(levelNumber);
        graphics.drawString(levelAsLabel, (Constants.WIDTH / 2) - levelAsLabel.length() * 5, 40);
        drawHealthBar(graphics, player);
    }

    private void drawObjects(final Graphics graphics) {
        final Graphics2D g2d = (Graphics2D) graphics;

        worldState.backgroundObjects.forEach(gameObject -> {
            switch (gameObject) {
                case BackgroundStar backgroundStar -> {
                    g2d.setColor(backgroundStar.getColor());
                    final Position position = gameObject.getPosition();
                    g2d.fillOval((int) position.x(), (int) position.y(), (int) gameObject.getRadius(),
                            (int) gameObject.getRadius());
                }
                default -> {
                }
            }
        });

        // iterate over worldState
        worldState.objects
                .stream()
                .filter(gameObject -> Objects.nonNull(gameObject.getSprite()))
                .forEach(object -> {
                    final Image sprite = object.getSprite();
                    final int w = sprite.getWidth(null);
                    final int h = sprite.getHeight(null);
                    if (w <= 0 || h <= 0) {
                        return;
                    }
                    final AffineTransform transform = getAffineTransform(object, w, h);

                    g2d.drawImage(sprite, transform, null);
                });

        if (worldState.isPaused()) {
            showPauseAction(graphics);
        }
    }

}
