package com.alienforce.entities;

import com.alienforce.utils.ColorTransition;
import lombok.Getter;

import java.awt.Color;
import java.awt.Image;
import java.util.List;


public class Explosion extends GameObject {

    private static final Color TRANSPARENT_BLACK = new Color(0, 0, 0, 0);
    private static final ColorTransition colorTransition = new ColorTransition(List.of(
            Color.BLUE, Color.YELLOW, Color.RED, TRANSPARENT_BLACK
    ));

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
        return colorTransition.getColor(scale);
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
        if (theta > 3) {
            die();
            return;
        }
        color = getColor(theta);
    }
}
