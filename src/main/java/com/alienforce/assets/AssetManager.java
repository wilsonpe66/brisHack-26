package com.alienforce.assets;

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
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AssetManager {

    private static final Map<String, SuperClip> clips = new ConcurrentHashMap<>();

    public static Optional<URL> getResource(final String path) {
        return Optional.of(path).map(AssetManager.class::getResource);
    }

    public static Optional<Image> getImage(final ImageKey imageKey) {
        return getResource("images/" + imageKey.resourceId())
            .map(Toolkit.getDefaultToolkit()::getImage);
    }


    private static Optional<SuperClip> getClipNoCache(final SoundKey soundKey) {
        System.out.printf("Loading %s%n", soundKey);
        return getAudioInputStream(soundKey.resourceId())
            .map(audioInputStream -> {
                try {
                    // Clip is a pre-loaded audio buffer that can be started/stopped
                    final Clip aClip = AudioSystem.getClip();
                    aClip.open(audioInputStream);
                    soundKey.soundVolume().ifPresent(volume -> {
                        final FloatControl control = (FloatControl) aClip.getControl(Type.MASTER_GAIN);
                        control.setValue(volume);
                    });
                    return new SuperClip(soundKey, aClip);
                } catch (final Exception exception) {
                    System.err.println(exception);
                }
                return null;
            });
    }

    public static Optional<SuperClip> getClip(final SoundKey soundKey) {
        final var aClip = new AtomicReference<>(clips.get(soundKey.resourceId()));
        if (aClip.get() == null) {
            getClipNoCache(soundKey)
                .ifPresent(localClip -> {
                    clips.put(soundKey.resourceId(), localClip);
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
