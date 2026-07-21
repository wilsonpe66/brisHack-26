package com.alienforce.utils;

import java.util.List;

public final class Constants {

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
    public static final List<GameLevel> GAME_LEVELS = List.of(
        GameLevel
            .builder()
            .LEVEL_NUMBER(0)
            .PLAYER_BULLET_SPEED(16)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(15)
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
            .PLAYER_SHOOT_COOLDOWN_FRAMES(12)
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
            .PLAYER_SHOOT_COOLDOWN_FRAMES(10)
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
            .LEVEL_NUMBER(3)
            .PLAYER_BULLET_SPEED(20)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(9)
            .ALIEN_SPEED(1.0)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(150)
            .ALIEN_SPAWN_DELAY(1000)
            .ALIEN_SPAWN_INITIAL_DELAY(1000)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(4)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(4)
            .PLAYER_BULLET_SPEED(25)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(8)
            .ALIEN_SPEED(1.0)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(150)
            .ALIEN_SPAWN_DELAY(500)
            .ALIEN_SPAWN_INITIAL_DELAY(500)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(5)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(5)
            .PLAYER_BULLET_SPEED(30)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(7)
            .ALIEN_SPEED(1.1)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(100)
            .ALIEN_SPAWN_DELAY(500)
            .ALIEN_SPAWN_INITIAL_DELAY(500)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(5)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(6)
            .PLAYER_BULLET_SPEED(35)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(6)
            .ALIEN_SPEED(1.6)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(70)
            .ALIEN_SPAWN_DELAY(400)
            .ALIEN_SPAWN_INITIAL_DELAY(400)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(5)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(7)
            .PLAYER_BULLET_SPEED(40)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(5)
            .ALIEN_SPEED(1.7)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(70)
            .ALIEN_SPAWN_DELAY(400)
            .ALIEN_SPAWN_INITIAL_DELAY(400)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(6)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(8)
            .PLAYER_BULLET_SPEED(45)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(5)
            .ALIEN_SPEED(1.8)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(70)
            .ALIEN_SPAWN_DELAY(350)
            .ALIEN_SPAWN_INITIAL_DELAY(350)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(6)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(9)
            .PLAYER_BULLET_SPEED(50)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(4)
            .ALIEN_SPEED(2.0)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(70)
            .ALIEN_SPAWN_DELAY(325)
            .ALIEN_SPAWN_INITIAL_DELAY(325)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(7)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(10)
            .PLAYER_BULLET_SPEED(80)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(4)
            .ALIEN_SPEED(2.2)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(65)
            .ALIEN_SPAWN_DELAY(325)
            .ALIEN_SPAWN_INITIAL_DELAY(325)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(7)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(11)
            .PLAYER_BULLET_SPEED(100)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(4)
            .ALIEN_SPEED(2.3)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(65)
            .ALIEN_SPAWN_DELAY(325)
            .ALIEN_SPAWN_INITIAL_DELAY(325)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(7)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(12)
            .PLAYER_BULLET_SPEED(120)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(4)
            .ALIEN_SPEED(2.4)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(50)
            .ALIEN_SPAWN_DELAY(325)
            .ALIEN_SPAWN_INITIAL_DELAY(325)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(45)
            .ALIEN_BULLET_SPEED(7)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build(),
        GameLevel
            .builder()
            .LEVEL_NUMBER(13)
            .PLAYER_BULLET_SPEED(140)
            .PLAYER_SHOOT_COOLDOWN_FRAMES(3)
            .ALIEN_SPEED(2.5)
            .ALIEN_SHOOT_COOLDOWN_FRAMES(40)
            .ALIEN_SPAWN_DELAY(325)
            .ALIEN_SPAWN_INITIAL_DELAY(325)
            .ALIEN_SPAWN_NO_SHOOT_FRAMES(40)
            .ALIEN_BULLET_SPEED(7)
            .ALIEN_TARGET_UPDATE_INTERVAL(45)
            .build()
    );
    public static final int ALIEN_KILL_SCORE = 5;

    private Constants() {
    }
}
