import java.awt.*;

public class Player extends GameObject{
    private final static Image sprite = Toolkit.getDefaultToolkit().getImage("assets/images/spaceship.png");

    // CONSTRUCTOR:
    public Player(double x, double y) {
        setPosition(x, y);
        setVelocity(0, 0);
        setRotationAngle(-Math.PI / 2); // straight up in radians
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

        // keep angle in [0, 2π) to avoid unbounded growth
        double normalized = getRotationAngle() % (Math.PI * 2);
        if (normalized < 0) {
            normalized += Math.PI * 2;
        }
        setRotationAngle(normalized);
    }

    public Bullet shoot() {
        double angle = getRotationAngle(); // radians
        double bulletVelocityX = Math.cos(angle) * 16;
        double bulletVelocityY = Math.sin(angle) * 16;

        // spawn at the ship nose, along the current facing angle
        double spawnX = getPositionX() + Math.cos(angle) * getRadius();
        double spawnY = getPositionY() + Math.sin(angle) * getRadius();

        return new Bullet(spawnX, spawnY, bulletVelocityX, bulletVelocityY, angle);
    }

    public void rotateBy(double deltaAngle) {
        setRotationAngle(getRotationAngle() + deltaAngle);
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
