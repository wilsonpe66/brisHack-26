package utils;

import java.util.List;

public final class Constants {
    private Constants() {
    }

    public static final int WIDTH = 1500;
    public static final int HEIGHT = 900;
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
    public static final double MAX_PLAYER_SPEED = 5;
    public static final double PLAYER_ACCELERATION = 0.2;
    public static final double ROTATION_SPEED = 0.06;
    public static final double PLAYER_VELOCITY_DECAY = 0.98;
    public static final int SHOOT_COOLDOWN_FRAMES = 15;

    public static final List<GameLevel> GAME_LEVELS = List.of(
        GameLevel
            .builder()
            .LEVEL_NUMBER(0)
            .PLAYER_BULLET_SPEED(16)
            .ALIEN_SPEED(1.0)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(150)
            .ALIEN_SPAWN_DELAY(15000)
            .ALIEN_SPAWN_INITIAL_DELAY(5000)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(4)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(1)
            .PLAYER_BULLET_SPEED(16)
            .ALIEN_SPEED(1.0)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(150)
            .ALIEN_SPAWN_DELAY(7500)
            .ALIEN_SPAWN_INITIAL_DELAY(2500)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(4)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(2)
            .PLAYER_BULLET_SPEED(20)
            .ALIEN_SPEED(1.0)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(150)
            .ALIEN_SPAWN_DELAY(1000)
            .ALIEN_SPAWN_INITIAL_DELAY(1000)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(4)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build()
    );

    public static final int ALIEN_KILL_SCORE = 5;
}
