package com.alienforce.entities;

import static com.alienforce.game.GamePanel.getAffineTransform;

import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.awt.Image;

@Setter
@Getter
@ToString
public abstract class GameObject implements Updatable, Collidable {

    private static Image sprite;
    private Velocity velocity;
    private double rotationAngle; // radians
    private Position position;
    private double radius;
    private int health;
    private double scale = 1.0;

    public final boolean isAlive() {
        return health > 0;
    }

    public final boolean isDead() {
        return !isAlive();
    }

    public void die() {
        health = 0;
    }

    public abstract Image getSprite();

    public Color getColor() {
        return Color.WHITE;
    }

    public void repaint(final Graphics graphics) {
        final Graphics2D g2d = (Graphics2D) graphics;
        Optional
            .ofNullable(getSprite())
            .ifPresentOrElse(sprite -> {
                    final int w = sprite.getWidth(null);
                    final int h = sprite.getHeight(null);
                    if (w <= 0 || h <= 0) {
                        return;
                    }
                    final AffineTransform transform = getAffineTransform(this, w, h);

                    g2d.drawImage(sprite, transform, null);
                },
                () -> {
                    g2d.setColor(getColor());
                    final Position position = getPosition();
                    g2d.fillOval((int) position.x(), (int) position.y(), (int) getRadius(),
                        (int) getRadius());
                }
            );
    }

}
