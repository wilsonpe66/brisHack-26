import java.awt.*;

public class Bullet extends GameObject {
    private final static Image sprite = Toolkit.getDefaultToolkit().getImage("assets/missile.png");

    // CONSTRUCTOR:
    public Bullet(double x, double y) {
        setPosition(x, y);
        setVelocity(10, 10);
        setRadius(5);
        setHealth(1);
        setAlive(true);
    }

    @Override
    public void update() {
        // update position according to velocity:
        setPosition(getPositionX() + getVelocityX(), getPositionY() + getVelocityY());

        boolean outOfBounds = getPositionX() < 0
                || getPositionX() > Constants.WIDTH
                || getPositionY() < 0
                || getPositionY() > Constants.HEIGHT;

        if (outOfBounds || getHealth() <= 0) {
            setAlive(false);
        }
    }

    @Override
    public void collide(GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(Player player) {
        throw new RuntimeException("BULLET HIT PLAYER?!?!?");
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        asteroid.setHealth(0); // make asteroids survive multiple bullets?
    }

    @Override
    public void collideWith(Bullet bullet) {
        throw new RuntimeException("BULLET HIT BULLET?!?!?");
    }
}
