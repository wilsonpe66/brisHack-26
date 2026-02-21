public class Player extends GameObject{

    // CONSTRUCTOR:
    public Player(double x, double y) {
        setPosition(x, y);
        setVelocity(0, 0);
        setHealth(100);
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
