public class Player extends GameObject{

    // CONSTRUCTOR:
    public Player(double x, double y) {
        setPosition(x, y);
        setVelocity(0, 0);
        setHealth(health);
        setAlive(true);
    }

    
    @Override
    public void update() {
        // NEED TO IMPLEMENT
    }
}
