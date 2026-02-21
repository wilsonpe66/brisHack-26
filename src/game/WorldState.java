import java.util.ArrayList;
import java.util.List;

public class WorldState {
    public List<Updatable> updatables;
    public List<GameObject> objects;
    private Player player;
    private AsteroidGenerator generator;

    public WorldState() {
        player = new Player((double) Constants.WIDTH/2, (double) Constants.HEIGHT/2);
        objects = new ArrayList<GameObject>();
        objects.add(player);
        updatables = new ArrayList<Updatable>();
        updatables.add(player);
        generator = new AsteroidGenerator();
    }

    private void handleSpawning(double time) {
        if (time % Constants.FPS == 0) {
            generator.generate(this);
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
        objects.removeIf(obj -> !obj.isAlive);
    }

    public void updateState(double time) {
        handleSpawning(time);
        updateAll();
        handleCollisions();
        removeDeadObjects();
    }

}
