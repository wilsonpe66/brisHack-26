package game;

import utils.Settings;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static assets.AssetManager.getAudioInputStream;

public class SoundManager {

    /**
     * Looping clips by id – can be stopped later with stopLooping(id).
     */
    private static final Map<String, Clip> loopingClips = new HashMap<>();

    private static final Map<String, String> loopPaths = new HashMap<>();

    /**
     * Play a one-shot sound (e.g. shoot). Creates a new Clip each time so
     * overlapping sounds (e.g. rapid shooting) play simultaneously.
     */
    public static void playSound(String path) {
        if (Settings.muted) {
            return;
        }
        try {
            AudioInputStream audio = getAudioInputStream(path).get();
            // Clip is a pre-loaded audio buffer that can be started/stopped
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Start a looping sound under the given id. No-op if that id is already playing.
     */
    public static void playLooping(String id, String path) {
        // Always remember the path so we can resume after unmute
        loopPaths.put(id, path);

        if (Settings.muted) {
            return;
        }

        final AtomicReference<Clip> clip = new AtomicReference<>(loopingClips.get(id));
        if (clip.get() != null && clip.get().isRunning()) {
            return;
        }

        if (clip.get() != null) {
            clip.get().close();
            loopingClips.remove(id);
        }

        getAudioInputStream(path)
                .ifPresent(audio -> {
                    try {

                        clip.set(AudioSystem.getClip());
                        clip.get().open(audio);
                        // LOOP_CONTINUOUSLY repeats the clip until explicitly stopped
                        clip.get().loop(Clip.LOOP_CONTINUOUSLY);
                        loopingClips.put(id, clip.get());
                    } catch (Exception e) {
                        System.err.println("Error loading audio file: " + path);
                        System.err.println("Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Stop a looping sound by id. Safe to call if not playing.
     */
    public static void stopLooping(String id) {
        loopPaths.remove(id);
        Clip clip = loopingClips.remove(id);
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    /**
     * Stop all currently looping sounds.
     */
    public static void stopAllLooping() {
        for (Clip clip : loopingClips.values()) {
            clip.stop();
            clip.close();
        }
        loopingClips.clear();
    }

    /**
     * Set the muted state. When muting, all currently looping sounds are
     * stopped but remembered. When unmuting, remembered loops are restarted.
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