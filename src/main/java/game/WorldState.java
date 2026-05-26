package game;

import entities.Alien;
import entities.AlienBullet;
import entities.Asteroid;
import entities.Bullet;
import entities.GameObject;
import entities.Player;
import entities.Updatable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import utils.Constants;

public class WorldState {

    @Getter
    private final Player player;
    private final AsteroidGenerator asteroidGenerator;
    private final AlienGenerator alienGenerator;
    private final InputHandler inputHandler;
    public List<Updatable> updatables;
    public List<GameObject> objects;
    private int shootCooldown;
    private long lastSpawnTime = 0;
    private long lastAlienSpawnTime = 0;
    private long gameStartTime;

    public WorldState(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        player = new Player(Constants.MIDDLE_X, Constants.MIDDLE_Y, inputHandler);
        objects = new ArrayList<>();
        objects.add(player);
        updatables = new ArrayList<>();
        updatables.add(player);
        asteroidGenerator = new AsteroidGenerator(this);
        alienGenerator = new AlienGenerator(this);
        gameStartTime = System.currentTimeMillis();
        lastAlienSpawnTime = gameStartTime;
    }

    private void handleShooting() {
        if (shootCooldown > 0) {
            shootCooldown--;
            return;
        }
        if (inputHandler.isShootPressed() && player.isAlive()) {
            Bullet bullet = player.shoot();
            objects.add(bullet);
            updatables.add(bullet);
            SoundManager.playSound("shoot.wav");
            shootCooldown = Constants.SHOOT_COOLDOWN_FRAMES;
        }
    }

    private void handleAlienShooting() {
        // Collect new bullets into a separate list first to avoid ConcurrentModificationException
        // (we can't add to 'objects' while iterating over it)
        List<AlienBullet> newBullets = new ArrayList<>();
        for (GameObject obj : objects) {
            // 'instanceof Alien alien' is a Java 16+ pattern match that both checks the type
            // and creates a typed local variable 'alien' in one step
            if (obj instanceof Alien alien && alien.isAlive()) {
                AlienBullet alienBullet = alien.shoot();
                if (alienBullet != null) {
                    newBullets.add(alienBullet);
                }
            }
        }
        for (AlienBullet b : newBullets) {
            objects.add(b);
            updatables.add(b);
        }
    }

    private void handleSpawning() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime >= Constants.SPAWN_DELAY) {
            asteroidGenerator.generate();
            lastSpawnTime = currentTime;
        }
        long timeSinceStart = currentTime - gameStartTime;
        if (timeSinceStart >= Constants.ALIEN_SPAWN_INITIAL_DELAY
            && currentTime - lastAlienSpawnTime >= Constants.ALIEN_SPAWN_DELAY) {
            alienGenerator.generate();
            lastAlienSpawnTime = currentTime;
        }
    }

    private void updateAll() {
        for (Updatable obj : updatables) {
            obj.update();
        }
    }

    private boolean checkCollision(final GameObject a, final GameObject b) {
        if (a == b || a == null || b == null) {
            return false;
        }

        double xDistance = a.getPositionX() - b.getPositionX();
        double yDistance = a.getPositionY() - b.getPositionY();
        double collisionDistance = a.getRadius() + b.getRadius();
        return (xDistance * xDistance) + (yDistance * yDistance) <= collisionDistance * collisionDistance;
    }

    private void handleCollisions() {
        final List<GameObject> livingObjects = objects
            .stream()
            .filter(GameObject::isAlive)
            .toList();

        IntStream
            .range(0, livingObjects.size())
            .forEach(outerIndex -> {
                IntStream
                    .range(outerIndex + 1, livingObjects.size())
                    .forEach(innerIndex -> {
                        final GameObject a = livingObjects.get(outerIndex);
                        final GameObject b = livingObjects.get(innerIndex);
                        if (checkCollision(a, b)) {
                            a.collide(b);
                            b.collide(a);
                        }
                    });
            });
    }

    private void removeDeadObjects() {
        // Count asteroids killed by player bullets this frame using a stream filter.
        // Only asteroids with killedByBullet=true contribute to score —
        // those that flew off-screen or were destroyed by other asteroids don't count.
        int shotAsteroids = (int) objects.stream()
            .filter(GameObject::isDead)
            .filter(obj -> obj instanceof Asteroid && ((Asteroid) obj).wasKilledByBullet())
            .count();
        player.setScore(player.getScore() + shotAsteroids);

        // removeIf modifies the list in-place, removing all dead objects
        objects.removeIf(GameObject::isDead);
    }

    public void updateState() {
        handleShooting();
        handleAlienShooting();
        handleSpawning();
        updateAll();
        handleCollisions();
        removeDeadObjects();
    }

    /**
     * Reset player and clear all objects for a new game.
     */
    public void reset() {
        objects.clear();
        updatables.clear();
        player.setPosition(Constants.MIDDLE_X, Constants.MIDDLE_Y);
        player.setVelocity(0, 0);
        player.setHealth(100);
        player.setScore(0);
        player.setRotationAngle(-Math.PI / 2);
        objects.add(player);
        updatables.add(player);
        shootCooldown = 0;
        lastSpawnTime = 0;
        gameStartTime = System.currentTimeMillis();
        lastAlienSpawnTime = gameStartTime;
    }
}
