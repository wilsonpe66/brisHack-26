package assets;

import game.GamePanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public class AssetManager {
    public static Optional<URL> getResource(final String path) {
        return Optional.of(path).map(AssetManager.class::getResource);
    }

    public static Optional<Image> getImage(final String path) {
        return getResource("images/" + path)
                .map(Toolkit.getDefaultToolkit()::getImage);
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
