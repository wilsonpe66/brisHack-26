import java.awt.*;

public class Player extends GameObject{
    private final static Image sprite = Toolkit.getDefaultToolkit().getImage("assets/spaceship.png");

    // CONSTRUCTOR:
    public Player(double x, double y) {
        setPosition(x, y);
        setVelocity(0, 0);
        setRadius(20);
        setHealth(100);
        setAlive(true);
    }

    @Override
    public void update() {
        // update position according to velocity:
        setPosition(getPositionX() + getVelocityX(), getPositionY() + getVelocityY());

        // check if off the screen - put to other side:
        if (getPositionX() < 0) {
            setPositionX(Constants.WIDTH);
        } else if (getPositionX() > Constants.WIDTH) {
            setPositionX(0);
        }

        if (getPositionY() < 0) {
            setPositionY(Constants.HEIGHT);
        } else if (getPositionY() > Constants.HEIGHT) {
            setPositionY(0);
        }

        if (getHealth() <= 0) {
            setAlive(false);
        }
    }

    public void shoot() {
        Bullet bullet = new Bullet(this.getPositionX(),this.getPositionY()); // spawn a bullet on the player

    }

    @Override
    public void collide(GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(Player player) {
        throw new RuntimeException("PLAYER HIT PLAYER?!?!?");
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        this.setHealth(0);
    }

    @Override
    public void collideWith(Bullet bullet) {
        throw new RuntimeException("PLAYER HIT BULLET?!?!?");
    }
}
