package assets;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import javax.sound.sampled.Clip;
import utils.Settings;

public class SoundManager {

    /**
     * Looping clips by id – can be stopped later with stopLooping(id).
     */
    private static final Map<String, Clip> loopingClips = new HashMap<>();

    private static final Map<String, String> loopPaths = new HashMap<>();


    /**
     * Play a one-shot sound (e.g. shoot). Creates a new Clip each time so overlapping sounds (e.g. rapid shooting) play simultaneously.
     */
    public static void playSound(final String path) {
        if (Settings.muted) {
            return;
        }

        AssetManager.getClip(path)
            .ifPresent(clip -> {
                clip.setFramePosition(0);
                clip.start();
            });
    }

    /**
     * Start a looping sound under the given id. No-op if that id is already playing.
     */
    public static void playLooping(String id, String path) {
        // Always remember the path so we can resume after unmute
        if (Settings.muted) {
            return;
        }

        AssetManager.getClip(path)
            .filter(Predicate.not(Clip::isRunning))
            .ifPresent(clip -> {
                loopPaths.put(id, path);
                loopingClips.put(id, clip);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.setFramePosition(0);
                clip.start();
            });
    }

    /**
     * Stop a looping sound by id. Safe to call if not playing.
     */
    public static void stopLooping(final String id) {
        loopPaths.remove(id);
        final Clip clip = loopingClips.remove(id);
        if (clip != null) {
            clip.stop();
        }
    }

    /**
     * Stop all currently looping sounds.
     */
    public static void stopAllLooping() {
        for (final Clip clip : loopingClips.values()) {
            clip.stop();
            clip.close();
        }
        loopingClips.clear();
    }

    /**
     * Set the muted state. When muting, all currently looping sounds are stopped but remembered. When unmuting, remembered loops are restarted.
     */
    public static void setMuted(boolean muted) {
        Settings.muted = muted;
        if (muted) {
            stopAllLooping();
        } else {
            // Restart all loops that were registered while muted
            Map<String, String> toRestart = new HashMap<>(loopPaths);
            for (Map.Entry<String, String> entry : toRestart.entrySet()) {
                playLooping(entry.getKey(), entry.getValue());
            }
        }
    }
}