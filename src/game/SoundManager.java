import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    /** Looping clips by id – can be stopped later with stopLooping(id). */
    private static final Map<String, Clip> loopingClips = new HashMap<>();

    /** Play a one-shot sound (e.g. shoot). */
    public static void playSound(String path) {
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
        Clip clip = loopingClips.remove(id);
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
