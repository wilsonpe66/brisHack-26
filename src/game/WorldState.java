import java.util.List;

public class WorldState {
    private List<Updatable> updatables;
    private List<GameObject> objects;
    private Player player;
    private AsteroidGenerator generator;
    private void handleSpawning() {}

    private void updateAll(double timeUnit) {
        for (Updatable obj : updatables) {
            obj.update(timeUnit);
        }
    }

    private boolean checkCollision(GameObject a, GameObject b) {
        double xDistance = a.getPositionX() - b.getPositionX();
        double yDistance = a.getPositionY() - b.getPositionY();
        double collisionDistance = a.getRadius() + b.getRadius();
        return (xDistance * xDistance) + (yDistance * yDistance) <= collisionDistance * collisionDistance;
    }

    private void handleCollisions() {
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i+1; j < objects.size(); j++) {
                GameObject a = objects.get(i);
                GameObject b = objects.get(j);
                if (checkCollision(a, b)) {
                    a.collide(b);
                    b.collide(a);
                }
            }
        }
    }

    private void removeDeadObjects() {
        objects.removeIf(obj -> !obj.isAlive);
    }

    public void updateState(double timeUnit) {
        handleSpawning();
        updateAll(timeUnit);
        handleCollisions();
        removeDeadObjects();
    }

}
