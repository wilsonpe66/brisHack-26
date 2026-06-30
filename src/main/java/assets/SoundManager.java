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
    private static final Map<String, SuperClip> loopingClips = new HashMap<>();
    private static boolean isPreviousePausedState = false;

    /// Play a one-shot sound (e.g. shoot). Creates a new Clip each time so overlapping sounds (e.g. rapid shooting) play simultaneously.
    ///
    /// @param path the path of the resource to be looped.
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

    /// Start a looping sound under the given id. No-op if that id is already playing.
    ///
    /// @param id   the id of the looping clip
    /// @param path the path of the resource to be looped.
    public static void playLooping(final String id, final String path) {
        // Always remember the path so we can resume after unmute
        if (Settings.muted) {
            return;
        }

        AssetManager.getClip(path)
            .filter(Predicate.not(SuperClip::isRunning))
            .ifPresent(clip -> {
                loopingClips.put(
                    id,
                    clip
                        .toBuilder()
                        .id(id)
                        .build()
                );
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.setFramePosition(0);
                clip.start();
            });
    }

    public static void togglePauseLooping(final boolean isPaused) {
        loopingClips.forEach((_, clip) -> {
            if (isPaused) {
                clip.stop();
                return;
            }

            clip.start();
        });

        isPreviousePausedState = isPaused;
    }

    /// Stop a looping sound by id. Safe to call if not playing.
    public static void stopLooping(final String id) {
        final SuperClip clip = loopingClips.remove(id);
        if (clip != null) {
            clip.stop();
        }
    }

    /// Stop all currently looping sounds.
    public static void stopAllLooping() {
        loopingClips.values().forEach(SuperClip::stop);
        loopingClips.clear();
    }

    /// Set the muted state. When muting, all currently looping sounds are stopped but remembered. When unmuting, remembered loops are restarted.
    public static void setMuted(boolean muted) {
        Settings.muted = muted;
        if (muted) {
            stopAllLooping();
            return;
        }

        // Restart all loops that were registered while muted
        loopingClips.values().forEach(clip -> SoundManager.playLooping(clip.id(), clip.path()));
    }

}