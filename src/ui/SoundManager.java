import javax.sound.sampled.*;
import java.io.File;

public class SoundManager {

    public static void playSound(String path) {
        try {
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(new File(path));

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Clip playLoop(String path) {
        try {
            AudioInputStream audio =
                    AudioSystem.getAudioInputStream(new File(path));

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            return clip;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}