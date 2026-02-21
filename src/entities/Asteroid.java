public class Asteroid extends GameObject{

    // CONSTRUCTOR:
    public Asteroid(double x, double y, double velocityX, double velocityY) {
        setPosition(x, y);
        setVelocity(velocityX, velocityY);
        setHealth(1);
        setAlive(true);
    }


    @Override
    public void update() {
        // update position according to velocity:
        setPosition(getPositionX() + getVelocityX(), getPositionY() + getVelocityY());

        if (getPositionX() < 0) {
            this.setHealth(0);
        } else if (getPositionX() > Constants.WIDTH) {
            this.setHealth(0);
        }

        if (getPositionY() < 0) {
            setPositionY(Constants.HEIGHT);
        } else if (getPositionY() > Constants.HEIGHT) {
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
        player.collideWith(this);
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        this.setHealth(0);
        asteroid.setHealth(0);
    }

    @Override
    public void collideWith(Bullet bullet) {
        bullet.collideWith(this);
    }
}
