package assets;

import utils.Settings;

import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SoundManager {

    /**
     * Looping clips by soundKey – can be stopped later with stop(soundKey).
     */
    private static final Map<SoundLoopKey, SuperClip> loopingClips = new HashMap<>();
    private static boolean isPreviousePausedState = false;

    /// Play a one-shot sound (e.g. shoot). Creates a new Clip each time so overlapping sounds (e.g. rapid shooting) play simultaneously.
    ///
    /// @param soundKey the soundKey of the resource to be looped.
    public static void play(final SoundKey soundKey) {
        if (Settings.muted) {
            return;
        }

        AssetManager.getClip(soundKey)
                .ifPresent(clip -> {
                    clip.setFramePosition(0);
                    clip.start();
                });
    }

    /// Start a looping sound under the given soundLoopKey. No-op if that soundLoopKey is already playing.
    ///
    /// @param soundLoopKey the soundLoopKey of the looping clip
    public static void play(final SoundLoopKey soundLoopKey) {
        // Always remember the path so we can resume after unmute
        if (Settings.muted) {
            return;
        }

        AssetManager.getClip(soundLoopKey)
                .filter(Predicate.not(SuperClip::isRunning))
                .ifPresent(clip -> {
                    loopingClips.put(soundLoopKey, clip);
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

    /// Stop a looping sound by soundLoopKey. Safe to call if not playing.
    public static void stop(final SoundLoopKey soundLoopKey) {
        final SuperClip clip = loopingClips.remove(soundLoopKey);
        if (clip != null) {
            clip.stop();
        }
    }

    public static void stop(final SoundEffectKey soundLoopKey) {
        AssetManager.getClip(soundLoopKey)
                .ifPresent(SuperClip::stop);
    }

    /// Stop all currently looping sounds.
    public static void stopAllLooping() {
        loopingClips.values().forEach(SuperClip::stop);
        loopingClips.clear();
    }

    /// Set the muted state. When muting, all currently looping sounds are stopped but remembered. When unmuting, remembered loops are restarted.
    public static void setMuted(final boolean muted) {
        Settings.muted = muted;
        if (muted) {
            stopAllLooping();
            return;
        }

        // Restart all loops that were registered while muted
        loopingClips.values()
                .stream()
                .map(SuperClip::soundKey)
                .map(SoundLoopKey.class::cast)
                .forEach(SoundManager::play);
    }

}