public class Player extends GameObject{

    // CONSTRUCTOR:
    public Player(double x, double y) {
        setPosition(x, y);
        setVelocity(0, 0);
        setHealth(health);
        setAlive(true);
    }


    @Override
    public void update(double timeUnit) {

    }


    @Override
    public void collide(GameObject other) {
        other.collideWith(this);
    }

    @Override
    public void collideWith(Player player) {
        // collision endpoint
    }

    @Override
    public void collideWith(Asteroid asteroid) {
        // collision endpoint
    }

    @Override
    public void collideWith(Bullet bullet) {
        // collision endpoint
    }

}
