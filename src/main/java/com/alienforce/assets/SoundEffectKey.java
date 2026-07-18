package com.alienforce.assets;

import java.util.Optional;

public enum SoundEffectKey implements SoundKey {
    SHOOT("shoot.wav"),
    EXPLOSION("explosion.wav", -8.0f),
    LEVEL_UP("win.wav"),
    GAME_OVER("game-over.wav");

    final String resourceId;
    final Float soundVolume;

    SoundEffectKey(final String resourceId, final Float soundVolume) {
        this.resourceId = resourceId;
        this.soundVolume = soundVolume;
    }

    SoundEffectKey(final String resourceId) {
        this(resourceId, null);
    }

    @Override
    public String resourceId() {
        return resourceId;
    }

    @Override
    public Optional<Float> soundVolume() {
        return Optional.ofNullable(soundVolume);
    }
}
