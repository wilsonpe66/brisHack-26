import java.awt.*;

public class Asteroid extends GameObject{
    private final static Image sprite = Toolkit.getDefaultToolkit().getImage("assets/images/asteroid1.png");

    // CONSTRUCTOR:
    public Asteroid(double x, double y, double velocityX, double velocityY) {
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setRotationAngle(Math.random() * Math.PI * 2);
        setRadius(30);
        setHealth(1);
        setAlive(true);
        setScale(0.3);
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    @Override
    public void update() {
        // update position according to velocity:
        setPosition(getPositionX() + getVelocityX(), getPositionY() + getVelocityY());

        // kill asteroid if it goes too far off screen (with buffer for spawning)
        double buffer = 100;
        if (getPositionX() < -buffer || getPositionX() > Constants.WIDTH + buffer) {
            this.setHealth(0);
        }

        if (getPositionY() < -buffer || getPositionY() > Constants.HEIGHT + buffer) {
            this.setHealth(0);
        }

        if (getHealth() <= 0) {
            setAlive(false);
        }
    }

    @Override
    public void collide(GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(Player player) {
        setHealth(0);
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        setHealth(0);
        asteroid.setHealth(0);
    }

    @Override
    public void collideWith(Bullet bullet) {
        setHealth(getHealth() - 1 );
    }
}
