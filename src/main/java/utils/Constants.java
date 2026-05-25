package utils;

public final class Constants {
    private Constants() {
    }

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 50;


    public static final int FPS = 60;
    public static final int FRAME_DELAY = 1000 / FPS;
    public static final double ASTEROID_OFFSET = 50;

    // how far outside the screen asteroids spawn
    public static final double MIDDLE_X = (double) WIDTH / 2;
    public static final double MIDDLE_Y = (double) HEIGHT / 2;
    public static final int SPAWN_DELAY = 1000;
    public static final double ASTEROID_SPEED = 3;
    public static final double ALIEN_SPEED = 1.0;
    public static final double MAX_PLAYER_SPEED = 5;
    public static final double PLAYER_ACCELERATION = 0.2;
    public static final double ROTATION_SPEED = 0.06;
    public static final double PLAYER_VELOCITY_DECAY = 0.98;
    public static final int SHOOT_COOLDOWN_FRAMES = 15;
    public static final int ALIEN_SHOOT_COOLDOWN_FRAMES = 150;
    public static final int ALIEN_SPAWN_DELAY = 15000;
    public static final int ALIEN_SPAWN_INITIAL_DELAY = 5000;
    public static final int ALIEN_SPAWN_NO_SHOOT_FRAMES = 120;
    public static final double ALIEN_BULLET_SPEED = 4;
    public static final int ALIEN_KILL_SCORE = 5;
    public static final int ALIEN_TARGET_UPDATE_INTERVAL = 45;
}
