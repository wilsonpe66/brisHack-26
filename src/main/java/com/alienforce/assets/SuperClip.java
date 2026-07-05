package com.alienforce.assets;

import java.util.Objects;
import javax.sound.sampled.Clip;
import lombok.Builder;

@Builder(toBuilder = true)
public record SuperClip(SoundKey soundKey, Clip clip) {

    public SuperClip {
        Objects.requireNonNull(soundKey);
        Objects.requireNonNull(clip);
    }

    public boolean isRunning() {
        return clip.isRunning();
    }

    public void setFramePosition(final int frames) {
        clip.setFramePosition(frames);
    }

    public boolean isLoopable() {
        return soundKey instanceof SoundLoopKey;
    }

    public void loop(final int count) {
        clip.loop(count);
    }

    public void stop() {
        if (clip.isRunning()) {
            clip.stop();
        }
    }

    public void start() {
        if (clip.isRunning()) {
            return;
        }
        clip.start();
    }
}
