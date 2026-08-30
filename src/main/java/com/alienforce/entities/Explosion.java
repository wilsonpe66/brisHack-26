package com.alienforce.entities;

import java.awt.Color;
import java.awt.Image;
import lombok.Getter;


public class Explosion extends GameObject {

    private static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 0);

    @Getter
    Color color;
    double theta = 0;

    public Explosion(final GameObject gameObject) {
        this.color = getColor(0);
        setPosition(gameObject.getPosition());
        setRotationAngle(0);
        setRadius(gameObject.getRadius());
        setHealth(1);
        setScale(1);
    }

    private static Color getColor(final double scale) {
        final Color startColor = Color.RED;
        return new Color(
            (int) getColor(scale, startColor.getRed(), TRANSPARENT_BLACK.getRed()),
            (int) getColor(scale, startColor.getGreen(), TRANSPARENT_BLACK.getGreen()),
            (int) getColor(scale, startColor.getBlue(), TRANSPARENT_BLACK.getBlue())
        );
    }

    private static double getColor(final double scale, double startColor, double endColor) {
        return (endColor - startColor) * scale + startColor;
    }

    @Override
    public final double getRadius() {
        return super.getRadius() * (1 + theta);
    }

    @Override
    public Image getSprite() {
        return null;
    }

    @Override
    public void collide(final Collidable collidable) {

    }

    @Override
    public void update() {
        theta += .01;
        if (theta > 1) {
            die();
            return;
        }
        color = getColor(theta);
    }
}
