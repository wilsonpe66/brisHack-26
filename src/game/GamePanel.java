import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;

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

        worldState = new WorldState(inputHandler);

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

        drawObjects(g);

        // draw hud
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
            int x = (int) Math.round(object.getPositionX());
            int y = (int) Math.round(object.getPositionY());

            AffineTransform transform = new AffineTransform();
            transform.translate(x + w / 2.0, y + h / 2.0); // move origin to image center
            transform.rotate(object.getRotationAngle()); // rotate around center
            transform.translate(-w / 2.0, -h / 2.0); // shift back so image draws correctly

            g2d.drawImage(sprite, transform, null);
        }
    }

    private void drawHud() {}
}
