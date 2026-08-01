package com.alienforce.utils;

import lombok.Builder;

/// Stores the alien constants.
///
/// @param speed                Alien movement speed (px/frame)
/// @param spawnDelay           Time between alien spawns (after initial delay)
/// @param spawnInitialDelay    Wait time after game start before first alien appears
/// @param targetUpdateInterval Frames between alien velocity re-aims towards player
/// @author Peter Wilson
@Builder(toBuilder = true)
public record AlienConstants(
        ShootConstants shootConstants,
        double speed,
        int spawnDelay,
        int spawnInitialDelay,
        int targetUpdateInterval
) {
}
