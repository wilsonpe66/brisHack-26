package com.alienforce.utils;

import lombok.Builder;

import java.util.Objects;

/// Stores the game level constants.
///
/// @param levelNumber The level number zero based.
/// @param alien       The alien constants.
/// @param bossAlien   the boss alien constants.
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
}
