package assets;

import java.util.Objects;
import javax.sound.sampled.Clip;
import lombok.Builder;

@Builder(toBuilder = true)
public record SuperClip(String id, String path, Clip clip) {

    public SuperClip {
        Objects.requireNonNull(id);
        Objects.requireNonNull(path);
        Objects.requireNonNull(clip);
    }

    public SuperClip(final String path, final Clip clip) {
        this(path, path, clip);
    }

    public boolean isRunning() {
        return clip.isRunning();
    }

    public void setFramePosition(final int frames) {
        clip.setFramePosition(frames);
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
