public class Bullet extends GameObject {

    // CONSTRUCTOR:
    public Bullet(double x, double y) {
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
