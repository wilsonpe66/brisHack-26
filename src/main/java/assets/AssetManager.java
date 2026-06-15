package assets;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AssetManager {

    private static final Map<String, Clip> clips = new ConcurrentHashMap<>();

    public static Optional<URL> getResource(final String path) {
        return Optional.of(path).map(AssetManager.class::getResource);
    }

    public static Optional<Image> getImage(final String path) {
        return getResource("images/" + path)
            .map(Toolkit.getDefaultToolkit()::getImage);
    }


    private static Optional<Clip> getClipNoCache(final String path) {
        System.out.printf("Loading %s%n", path);
        return getAudioInputStream(path)
            .map(audioInputStream -> {
                try {
                    // Clip is a pre-loaded audio buffer that can be started/stopped
                    final Clip aClip = AudioSystem.getClip();
                    aClip.open(audioInputStream);
                    return aClip;
                } catch (final Exception exception) {
                    System.err.println(exception);
                }
                return null;
            });
    }

    public static Optional<Clip> getClip(final String path) {
        final var aClip = new AtomicReference<>(clips.get(path));
        if (aClip.get() == null) {
            getClipNoCache(path)
                .ifPresent(localClip -> {
                    clips.put(path, localClip);
                    aClip.set(localClip);
                });
        }
        return Optional.ofNullable(aClip.get());
    }

    public static Optional<AudioInputStream> getAudioInputStream(final String path) {
        return getResource("sounds/" + path)
            .map(url -> {
                try {
                    return AudioSystem.getAudioInputStream(url);
                } catch (final UnsupportedAudioFileException | IOException exception) {
                    System.err.println("Error loading audio file: " + url);
                    System.err.println("Exception: " + exception.getMessage());
                    exception.printStackTrace();
                }
                return null;
            });
    }
}
