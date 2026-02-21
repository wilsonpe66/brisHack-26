import java.util.ArrayList;
import java.util.List;

public class WorldState {
    public List<Updatable> updatables;
    public List<GameObject> objects;
    private final Player player;
    private final AsteroidGenerator generator;
    private final InputHandler inputHandler;
    private int shootCooldown;
    private long lastSpawnTime = 0;

    private int framesUntilNextSpawn = Constants.SPAWN_DELAY;

    public WorldState(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        player = new Player((double) Constants.MIDDLEX, (double) Constants.MIDDLEY, inputHandler);
        objects = new ArrayList<>();
        objects.add(player);
        updatables = new ArrayList<>();
        updatables.add(player);
        generator = new AsteroidGenerator(this);
    }

    private void handleShooting() {
        if (shootCooldown > 0) {
            shootCooldown--;
            return;
        }
        if (inputHandler.isShootPressed() && player.getIsAlive()) {
            Bullet bullet = player.shoot();
            objects.add(bullet);
            updatables.add(bullet);
            shootCooldown = Constants.SHOOT_COOLDOWN_FRAMES;
        }
    }

    private void handleSpawning() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime >= Constants.SPAWN_DELAY) {
            generator.generate();
            lastSpawnTime = currentTime;
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

                if (!a.getIsAlive() || !b.getIsAlive()) continue;

                if (checkCollision(a, b)) {
                    a.collide(b);
                }
            }
        }
    }

    private static boolean isAlive(Updatable u) {
        return !(u instanceof GameObject) || ((GameObject) u).getIsAlive();
    }

    private void removeDeadObjects() {
        objects.removeIf(obj -> !obj.getIsAlive());
        updatables.removeIf(u -> !isAlive(u));
    }

    public void updateState() {
        handleShooting();
        handleSpawning();
        updateAll();
        handleCollisions();
        removeDeadObjects();
    }

    public Player getPlayer() {
        return player;
    }
}
