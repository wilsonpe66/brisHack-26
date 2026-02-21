public class Asteroid extends GameObject{

    // CONSTRUCTOR:
    public Asteroid(double x, double y) {
        setPosition(x, y);
        setVelocity(0, 0);
        setHealth(health);
        setAlive(true);
    }


    @Override
    public void update(double timeUnit) {
        // NEED TO IMPLEMENT
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
