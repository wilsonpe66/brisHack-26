import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    /** Looping clips by id – can be stopped later with stopLooping(id). */
    private static final Map<String, Clip> loopingClips = new HashMap<>();

    /** File paths for each active loop id, so loops can be restarted after unmute. */
    private static final Map<String, String> loopPaths = new HashMap<>();

    /** Play a one-shot sound (e.g. shoot). */
    public static void playSound(String path) {
        if (Settings.muted) {
            return;
        }
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Start a looping sound under the given id. No-op if that id is already playing. */
    public static void playLooping(String id, String path) {
        // Always remember the path so we can resume after unmute
        loopPaths.put(id, path);

        if (Settings.muted) {
            return;
        }

        Clip clip = loopingClips.get(id);
        if (clip != null && clip.isRunning()) {
            return;
        }
        try {
            if (clip != null) {
                clip.close();
                loopingClips.remove(id);
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            loopingClips.put(id, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Stop a looping sound by id. Safe to call if not playing. */
    public static void stopLooping(String id) {
        loopPaths.remove(id);
        Clip clip = loopingClips.remove(id);
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }

    /** Stop all currently looping sounds. */
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