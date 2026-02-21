import java.awt.*;

public class Player extends GameObject{
    private final static Image sprite = Toolkit.getDefaultToolkit().getImage("assets/images/spaceship.png");
    private final InputHandler inputHandler;

    // CONSTRUCTOR:
    public Player(double x, double y, InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        setPosition(x, y);
        setVelocity(0, 0);
        setRotationAngle(-Math.PI / 2); // straight up in radians
        setRadius(20);
        setHealth(100);
        setAlive(true);
        setScale(0.5); // make player sprite smaller
    }

    @Override
    public void update() {
        // respond to input: thrust (W/Up) and rotation (A/D)
        if (inputHandler.isUpPressed()) {
            double ax = Math.cos(getRotationAngle()) * Constants.PLAYER_ACCELERATION;
            double ay = Math.sin(getRotationAngle()) * Constants.PLAYER_ACCELERATION;
            double vx = getVelocityX() + ax;
            double vy = getVelocityY() + ay;
            double speed = Math.sqrt(vx * vx + vy * vy);
            if (speed > Constants.MAX_PLAYER_SPEED) {
                double scale = Constants.MAX_PLAYER_SPEED / speed;
                vx *= scale;
                vy *= scale;
            }
            setVelocity(vx, vy);
        } else {
            // decay velocity when thrust is not pressed
            double vx = getVelocityX() * Constants.PLAYER_VELOCITY_DECAY;
            double vy = getVelocityY() * Constants.PLAYER_VELOCITY_DECAY;
            if (Math.abs(vx) < 0.01 && Math.abs(vy) < 0.01) {
                vx = 0;
                vy = 0;
            }
            setVelocity(vx, vy);
        }
        if (inputHandler.isLeftPressed()) {
            setRotationAngle(getRotationAngle() - Constants.ROTATION_SPEED);
        }
        if (inputHandler.isRightPressed()) {
            setRotationAngle(getRotationAngle() + Constants.ROTATION_SPEED);
        }

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

    @Override
    public Image getSprite() {
        return sprite;
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

    @Override
    public Image getSprite() {
        return sprite;
    }
}
