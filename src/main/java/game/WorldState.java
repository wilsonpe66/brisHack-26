package game;

import entities.Alien;
import entities.AlienBullet;
import entities.Asteroid;
import entities.Bullet;
import entities.PlayerBullet;
import entities.GameObject;
import entities.Player;
import entities.Position;
import entities.Updatable;
import entities.Velocity;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import leaderboard.LeaderBoard;
import lombok.Getter;
import utils.Constants;

public class WorldState {

    @Getter
    private final Player player;
    private final AsteroidGenerator asteroidGenerator;
    private final AlienGenerator alienGenerator;
    private final InputHandler inputHandler;
    @Getter
    private final LeaderBoard leaderBoard = LeaderBoard.builder().build();
    public Set<Updatable> updatableObjects;
    public Set<GameObject> objects;
    private int shootCooldown;
    private long lastSpawnTime = 0;
    private long lastAlienSpawnTime = 0;
    private long gameStartTime;

    public WorldState(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        player = new Player(new Position(Constants.MIDDLE_X, Constants.MIDDLE_Y), inputHandler);
        objects = new HashSet<>();
        objects.add(player);
        updatableObjects = new HashSet<>();
        updatableObjects.add(player);
        asteroidGenerator = new AsteroidGenerator(this);
        alienGenerator = new AlienGenerator(this);
        gameStartTime = System.currentTimeMillis();
        lastAlienSpawnTime = gameStartTime;
    }

    private static String aaa(final GameObject a) {
        return "%s@%s: %d".formatted(a.getClass().getCanonicalName(), System.identityHashCode(a), a.getHealth());
    }

    private void handleShooting() {
        if (shootCooldown > 0) {
            shootCooldown--;
            return;
        }
        if (inputHandler.isShootPressed() && player.isAlive()) {
            final List<PlayerBullet> bullet = player.shoot();
            objects.addAll(bullet);
            updatableObjects.addAll(bullet);
            SoundManager.playSound("shoot.wav");
            shootCooldown = Constants.SHOOT_COOLDOWN_FRAMES;
        }
    }

    private void handleAlienShooting() {
        // Collect new bullets into a separate list first to avoid ConcurrentModificationException
        // (we can't add to 'objects' while iterating over it)
        final List<? extends Bullet> newBullets = objects
            .stream()
            .filter(GameObject::isAlive)
            .filter(Alien.class::isInstance)
            .map(Alien.class::cast)
            .map(Alien::shoot)
            .flatMap(List::stream)
            .toList();

        objects.addAll(newBullets);
        updatableObjects.addAll(newBullets);
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
        for (Updatable obj : updatableObjects) {
            obj.update();
        }
    }

    private boolean checkCollision(final GameObject a, final GameObject b) {
        if (a == b || a == null || b == null) {
            return false;
        }

        final double collisionDistance = a.getRadius() + b.getRadius();
        return a.getPosition().minus(b.getPosition()).getSpeed() <= collisionDistance;
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
                            //System.out.printf("a=%s, b=%s%n", aaa(a), aaa(b));
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
        player.incrementScore(shotAsteroids);

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
        updatableObjects.clear();
        player.setPosition(new Position(Constants.MIDDLE_X, Constants.MIDDLE_Y));
        player.setVelocity(Velocity.ZERO);
        player.setHealth(100);
        player.setScore(0);
        player.setRotationAngle(-Math.PI / 2);
        objects.add(player);
        updatableObjects.add(player);
        shootCooldown = 0;
        lastSpawnTime = 0;
        gameStartTime = System.currentTimeMillis();
        lastAlienSpawnTime = gameStartTime;
    }
}
