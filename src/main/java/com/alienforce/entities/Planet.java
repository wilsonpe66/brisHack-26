package com.alienforce.entities;

import static com.alienforce.assets.AssetManager.getImage;

import com.alienforce.assets.ImageKey;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import com.alienforce.utils.PiConstants;
import java.awt.Color;
import java.awt.Image;
import java.util.List;
import java.util.Random;
import lombok.Getter;


public class Planet extends GameObject {

    private static final Random random = new Random();

    private final static List<Image> sprites = List.of(
        getImage(ImageKey.PLANET_1).get(),
        getImage(ImageKey.PLANET_2).get(),
        getImage(ImageKey.PLANET_3).get(),
        getImage(ImageKey.PLANET_1).get(),
        getImage(ImageKey.PLANET_2).get(),
        getImage(ImageKey.PLANET_3).get()
    );
    @Getter
    final Color color;
    private final Image sprite;
    double theta = 0;

    public Planet(final Position position, final Velocity velocity, final Color color) {
        this.color = color;
        setPosition(position);
        setVelocity(velocity);
        setRotationAngle(Math.random()*PiConstants.TAU);
        setRadius(100 + 200 * Math.random());
        setHealth(1);
        setScale(1 - .75 * Math.random());

        final int size = sprites.size();

        sprite = sprites.get(random.nextInt(size));
    }

    @Override
    public final double getRadius() {
        return super.getRadius() * (.75 + Math.abs(Math.sin(theta)) + .25);
    }

    @Override
    public Image getSprite() {
        return sprite;
    }

    @Override
    public void collide(final Collidable collidable) {

    }

    @Override
    public void update() {
        theta += .001;
        if (theta > PiConstants.TAU) {
            theta -= PiConstants.TAU;
        }
    }
}
