package com.alienforce.utils;

import lombok.Builder;

<<<<<<< Updated upstream
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
=======
import java.util.Objects;

/// Stores the game level constants.
///
/// @param levelNumber               The level number zero based.
/// @param alien                     The alien constants.
/// @param bossAlien                 the boss alien constants.
/// @author Peter Wilson
@Builder(toBuilder = true)
public record GameLevel(
        int levelNumber,
        ShootConstants playerShootConstants,
        AlienConstants alien,
        AlienConstants bossAlien
) {

    public GameLevel {
        bossAlien = Objects.requireNonNullElse(bossAlien, alien);
    }

>>>>>>> Stashed changes
}
