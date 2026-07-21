package com.alienforce.utils;

import lombok.Builder;

@Builder(toBuilder = true)
public record GameLevel(
    int LEVEL_NUMBER,
    int PLAYER_BULLET_SPEED,
    int PLAYER_SHOOT_COOLDOWN_FRAMES,
    double ALIEN_SPEED,
    int ALIEN_SHOOT_COOLDOWN_FRAMES,
    int ALIEN_SPAWN_DELAY,
    int ALIEN_SPAWN_INITIAL_DELAY,
    int ALIEN_SPAWN_NO_SHOOT_FRAMES,
    int ALIEN_BULLET_SPEED,
    int ALIEN_TARGET_UPDATE_INTERVAL
) {
}
