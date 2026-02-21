import java.util.ArrayList;
import java.util.List;

public class WorldState {
    public List<Updatable> updatables;
    public List<GameObject> objects;
    private final Player player;
    private final AsteroidGenerator generator;

    public WorldState(InputHandler inputHandler) {
        player = new Player((double) Constants.MIDDLEX, (double) Constants.MIDDLEY, inputHandler);
        objects = new ArrayList<>();
        objects.add(player);
        updatables = new ArrayList<>();
        updatables.add(player);
        generator = new AsteroidGenerator(this);
    }

    private void handleSpawning() {
        if (System.currentTimeMillis() % Constants.SPAWN_DELAY == 0) {
            generator.generate();
        }
    }

    private void updateAll() {
        for (Updatable obj : updatables) {
            obj.update();
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
        objects.removeIf(obj -> !obj.getIsAlive());
    }

    public void updateState() {
        handleSpawning();
        updateAll();
        handleCollisions();
        removeDeadObjects();
    }

    public Player getPlayer() {
        return player;
    }
}
