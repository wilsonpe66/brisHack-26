import java.awt.*;

public class Bullet extends GameObject {
    private final static Image sprite = Toolkit.getDefaultToolkit().getImage("assets/images/missile.png");
    private final Player owner;

    // CONSTRUCTOR:
    public Bullet(double x, double y) {
        this(x, y, 10, 10, Math.PI / 4, null);
    }

    public Bullet(double x, double y, double velocityX, double velocityY, double rotationAngle) {
        this(x, y, velocityX, velocityY, rotationAngle, null);
    }

    public Bullet(double x, double y, double velocityX, double velocityY, double rotationAngle, Player owner) {
        this.owner = owner;
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setRotationAngle(rotationAngle);
        setRadius(5);
        setHealth(1);
        setAlive(true);
    }

    @Override
    public Image getSprite() {
        return sprite;
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
        setHealth(0);
    }

    @Override
    public void collideWith(Bullet bullet) {
        throw new RuntimeException("BULLET HIT BULLET?!?!?");
    }

    @Override
    public void collideWith(Alien alien) {
        setHealth(0);
        alien.setHealth(0);
        if (owner != null) {
            owner.setScore(owner.getScore() + Constants.ALIEN_KILL_SCORE);
        }
    }

    @Override
    public void collideWith(AlienBullet alienBullet) {
        setHealth(0);
        alienBullet.setHealth(0);
    }
}
