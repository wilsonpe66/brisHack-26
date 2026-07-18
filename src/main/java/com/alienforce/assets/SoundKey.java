package com.alienforce.assets;

import java.util.Optional;

public interface SoundKey extends AssetKey {
    default Optional<Float> soundVolume() {
        return Optional.empty();
    }
}
