package com.alienforce.game;

import com.alienforce.assets.SoundEffectKey;
import com.alienforce.assets.SoundLoopKey;
import com.alienforce.assets.SoundManager;
import com.alienforce.entities.Alien;
import com.alienforce.entities.Asteroid;
import com.alienforce.entities.BackgroundStar;
import com.alienforce.entities.GameObject;
import com.alienforce.entities.Player;
import com.alienforce.entities.SelfDefendable;
import com.alienforce.entities.Updatable;
import com.alienforce.game.spawner.AlienSpawner;
import com.alienforce.game.spawner.AsteroidSpawner;
import com.alienforce.game.spawner.BossAlienSpawner;
import com.alienforce.game.spawner.Spawner;
import com.alienforce.input.InputHandler;
import com.alienforce.leaderboard.LeaderboardStore;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import com.alienforce.utils.AlienConstants;
import com.alienforce.utils.Constants;
import com.alienforce.utils.GameLevel;
import com.alienforce.utils.PiConstants;
import com.alienforce.utils.Settings;
import java.awt.Color;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Getter;

public class WorldState {

    @Getter
    private final Player player;
    private final Generate<Asteroid> asteroidGenerator;
    private final Generate<Alien> alienGenerator;
    private final Generate<Alien> bossAlienGenerator;
    private final InputHandler inputHandler;
    @Getter
    private final LeaderboardStore leaderBoardStore;
    public Set<Updatable> backgroundUpdatableObjects;
    public Set<GameObject> backgroundObjects;
    public Set<Updatable> updatableObjects;
    public Set<GameObject> objects;
    private boolean lastIsPressedState = false;
    @Getter
    private boolean isPaused = false;
    private long gameStartTime;
    private int level;
    private int lastLevel = 0;
    private boolean isMuteTogglePressedState = false;

    public WorldState(InputHandler inputHandler, final LeaderboardStore leaderboardStore) {
        this.inputHandler = inputHandler;
        player = new Player(this, new Position(Constants.MIDDLE_X, Constants.MIDDLE_Y), inputHandler);
        objects = new HashSet<>();
        objects.add(player);

        creatBackGroundStars();

        updatableObjects = new HashSet<>();
        updatableObjects.add(player);
        asteroidGenerator = new Generate<>(new AsteroidSpawner());
        alienGenerator = new Generate<>(new AlienSpawner(this));
        bossAlienGenerator = new Generate<>(new BossAlienSpawner(this));
        gameStartTime = System.currentTimeMillis();
        this.leaderBoardStore = leaderboardStore;
    }

    private static String aaa(final GameObject a) {
        return "%s@%s: %d".formatted(a.getClass().getCanonicalName(), System.identityHashCode(a), a.getHealth());
    }

    private void creatBackGroundStars() {
        backgroundObjects = new HashSet<>();
        backgroundUpdatableObjects = new HashSet<>();

        Stream
            .of(Color.CYAN, Color.RED, Color.GREEN)
            .forEach(color -> {
                final BackgroundStar backgroundStar = new BackgroundStar(
                    new Position(Math.random() * Constants.WIDTH, Math.random() * Constants.HEIGHT),
                    Velocity.ZERO,
                    color
                );
                backgroundObjects.add(backgroundStar);
                backgroundUpdatableObjects.add(backgroundStar);
            });
    }

    public GameLevel gameLevel() {
        return Constants.GAME_LEVELS.get(Math.clamp(level, 0, Constants.GAME_LEVELS.size()));
    }

    private void handleShooting() {
        if (player.isDead()) {
            return;
        }

        final boolean superShootPressed = inputHandler.isSuperShootPressed();
        final boolean shootPressed = inputHandler.isShootPressed();
        if (!superShootPressed && !shootPressed) {
            return;
        }

        (superShootPressed ? player.shootIgnoreCoolDown() : player.shoot())
            .filter(GameObject.class::isInstance)
            .forEach(bullet -> {
                objects.add((GameObject) bullet);
                updatableObjects.add(bullet);
            });
    }

    private void handleAlienShooting() {
        // Collect new bullets into a separate list first to avoid ConcurrentModificationException
        // (we can't add to 'objects' while iterating over it)
        objects
            .stream()
            .filter(Objects::nonNull)
            .filter(GameObject::isAlive)
            .filter(Alien.class::isInstance)
            .map(SelfDefendable.class::cast)
            .flatMap(SelfDefendable::shoot)
            .filter(GameObject.class::isInstance)
            .collect(Collectors.toSet())
            .forEach(bullet -> {
                objects.add((GameObject) bullet);
                updatableObjects.add(bullet);
            });
    }
    //pausedPressed

    private void handleSpawning() {
        final GameLevel gameLevel = Constants.GAME_LEVELS.get(level);

        asteroidGenerator.generate(Constants.SPAWN_DELAY);

        final AlienConstants alienConstants = gameLevel.alien();
        alienGenerator.generate(alienConstants.spawnInitialDelay(), alienConstants.spawnDelay());

        final AlienConstants bossAlienConstants = gameLevel.bossAlien();
        bossAlienGenerator.generate(bossAlienConstants.spawnInitialDelay(), bossAlienConstants.spawnDelay());
    }

    private void updateAll() {
        backgroundUpdatableObjects.forEach(Updatable::update);
        updatableObjects.forEach(Updatable::update);
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
            .filter(obj -> obj instanceof Asteroid asteroid && asteroid.wasKilledByBullet())
            .count();
        player.incrementScore(shotAsteroids);

        // removeIf modifies the list in-place, removing all dead objects
        objects.removeIf(GameObject::isDead);
        updatableObjects.removeIf(obj -> obj instanceof GameObject go && go.isDead());
    }

    public void updateState() {
        inputHandler.updateGamePad();
        final boolean pausedPressed = inputHandler.isPausedPressed();
        if (pausedPressed != lastIsPressedState) {
            lastIsPressedState = pausedPressed;
            if (pausedPressed) {
                isPaused = !isPaused;
            }
        }

        if (inputHandler.isMuteTogglePressed()) {
            if (!isMuteTogglePressedState) {
                Settings.muted = !Settings.muted;
                isMuteTogglePressedState = true;
            }
        } else {
            isMuteTogglePressedState = false;
        }
        SoundManager.togglePauseLooping(isPaused);

        if (isPaused) {
            return;
        }

        handleShooting();
        handleAlienShooting();
        handleSpawning();
        updateAll();
        handleCollisions();
        removeDeadObjects();
        levelUpdate();
    }

    private void levelUpdate() {
        if (player.isDead()) {
            return;
        }

        final int score = player.getScore();
        if (score > 80_000) {
            if ((score - 80_000) % 5000 < 10) {
                SoundManager.play(SoundLoopKey.BACK_GROUND);
                player.setHealth(Math.clamp((int) (player.getHealth() * 1.2), 10, 100));
            }
            level = 14;
        } else if (score > 70_000) {
            level = 14;
        } else if (score > 60_000) {
            level = 13;
        } else if (score > 50_000) {
            level = 12;
        } else if (score > 40_000) {
            level = 11;
        } else if (score > 31_000) {
            level = 10;
        } else if (score > 26_000) {
            level = 9;
        } else if (score > 21_000) {
            level = 8;
        } else if (score > 15_000) {
            level = 7;
        } else if (score > 10_000) {
            level = 6;
        } else if (score > 6_000) {
            level = 5;
        } else if (score > 2_500) {
            level = 4;
        } else if (score > 1500) {
            level = 3;
        } else if (score > 800) {
            level = 2;
        } else if (score > 300) {
            level = 1;
        }

        if (lastLevel != level) {
            lastLevel = level;
            SoundManager.play(SoundEffectKey.LEVEL_UP);
            SoundManager.play(SoundLoopKey.BACK_GROUND);
            player.setHealth(Math.clamp((int) (player.getHealth() * 1.1), 10, 100));
        }
    }

    /**
     * Reset player and clear all objects for a new game.
     */
    public void reset() {
        objects.clear();
        level = 0;
        lastLevel = 0;
        updatableObjects.clear();
        player.setPosition(new Position(Constants.MIDDLE_X, Constants.MIDDLE_Y));
        player.setVelocity(Velocity.ZERO);
        player.setHealth(100);
        player.setScore(0);
        player.setRotationAngle(-PiConstants.PID2);
        objects.add(player);
        updatableObjects.add(player);
        gameStartTime = System.currentTimeMillis();

        lastIsPressedState = false;
        isPaused = false;
        Settings.muted = false;
    }

    public void pause() {
        isPaused = true;
    }

    private class Generate<EntityType extends GameObject> {
        private final Spawner<EntityType> spawner;
        long lastSpawnTime = 0;

        private Generate(final Spawner<EntityType> spawner) {
            this.spawner = spawner;
        }

        final void generate(final long spawnDelay) {
            generate(0, spawnDelay);
        }

        final void generate(final long spawnInitialDelay, final long spawnDelay) {
            final long currentTime = System.currentTimeMillis();
            final long timeSinceStart = currentTime - gameStartTime;

            if (timeSinceStart < spawnInitialDelay || currentTime - lastSpawnTime < spawnDelay) {
                return;
            }

            final var entity = spawner.spawn(player);
            objects.add(entity);
            updatableObjects.add(entity);
            lastSpawnTime = currentTime;
        }
    }
}
