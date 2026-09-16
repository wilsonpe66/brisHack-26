package com.alienforce.entities;

import static com.alienforce.assets.AssetManager.getImage;
import static com.alienforce.game.GamePanel.getAffineTransform;

import com.alienforce.assets.ImageKey;
import com.alienforce.motion.Position;
import com.alienforce.utils.ColorTransition;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;


public class Explosion extends GameObject {

    private static final double THETA_FACTOR = 2 / 3.0;
    private static final double SCALE_THETA_FACTOR = 1.8 * THETA_FACTOR;

    private final static Map<Class<?>, Image> sprites = Map.of(
        Asteroid.class, getImage(ImageKey.EXPLOSION_1).get(),
        Alien.class, getImage(ImageKey.EXPLOSION_2).get(),
        BossAlien.class, getImage(ImageKey.EXPLOSION_3).get()
    );

    private static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 0);
    private static final Map<Class<?>, ColorTransition> colorTransitions = Map.of(
        Asteroid.class,
        new ColorTransition(List.of(Color.ORANGE, Color.YELLOW, Color.RED, TRANSPARENT_BLACK)),
        Alien.class,
        new ColorTransition(List.of(Color.GREEN, Color.YELLOW, Color.RED, TRANSPARENT_BLACK)),
        BossAlien.class,
        new ColorTransition(List.of(Color.BLUE, Color.LIGHT_GRAY, Color.DARK_GRAY, TRANSPARENT_BLACK))
    );
    private final GameObject deadObject;
    private final double angularVelocity;
    @Getter
    private Color color;
    private double theta = 0;

    public Explosion(final GameObject gameObject) {
        deadObject = Objects.requireNonNull(gameObject);
        color = getColor(0);
        setPosition(deadObject.getPosition());
        setVelocity(deadObject.getVelocity().scale(.5));
        setRotationAngle(0);
        setRadius(deadObject.getRadius());
        setHealth(1);
        setScale(.5);
        if (deadObject instanceof Asteroid asteroid) {
            angularVelocity = asteroid.getAngularVelocity();
        } else {
            angularVelocity = 0;
        }
    }

    private Color getColor(final double scale) {
        return colorTransitions.get(deadObject.getClass()).getColor(scale);
    }

    @Override
    public double getScale() {
        return super.getScale() * (SCALE_THETA_FACTOR * theta);
    }

    @Override
    public double getRadius() {
        return super.getRadius() * (1 + theta * THETA_FACTOR);
    }

    @Override
    public Image getSprite() {
        return sprites.get(deadObject.getClass());
    }

    @Override
    public void collide(final Collidable collidable) {

    }

    @Override
    public void update() {
        // update position according to velocity:
        setPosition(getPosition().add(getVelocity()));

        theta += .02;
        if (theta > 3) {
            die();
            return;
        }
        color = getColor(theta);

        setRotationAngle(getRotationAngle() + angularVelocity);
    }

    @Override
    public void repaint(final Graphics graphics) {
        final Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(getColor());
        final Position position = getPosition();
        final double radius = getRadius();
        final int offset = (int) (radius / 2);
        g2d.fillOval((int) (position.x() - offset), (int) (position.y() - offset), (int) radius, (int) radius);

        final Image sprite = getSprite();
        final int w = sprite.getWidth(null);
        final int h = sprite.getHeight(null);
        if (w <= 0 || h <= 0) {
            return;
        }
        final AffineTransform transform = getAffineTransform(this, w, h);

        g2d.drawImage(sprite, transform, null);
    }
}
