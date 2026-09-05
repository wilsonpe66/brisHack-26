package com.alienforce.utils;

import lombok.Builder;

/// Stores the shoot constants.
///
/// @param shootCooldownFrames Frames between the entity shots (≈ 2.5 s at 60 FPS)
/// @param spawnNoShootFrames  Grace period after spawn before the entity fires (≈ 2 s)
/// @param bulletSpeed         Speed of bullets fired by the entity (px/frame)
/// @author Peter Wilson
@Builder(toBuilder = true)
public record ShootConstants(
        int shootCooldownFrames,
        int spawnNoShootFrames,
        int bulletSpeed
) {
}
