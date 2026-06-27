package entities;

import entities.motion.Position;
import entities.motion.Velocity;
import java.awt.Color;
import java.awt.Image;
import lombok.Getter;
import utils.PiConstants;


public class BackgroundStar extends GameObject {

    @Getter
    final Color color;
    double theta = 0;

    public BackgroundStar(final Position position, final Velocity velocity, final Color color) {
        this.color = color;
        setPosition(position);
        setVelocity(velocity);
        setRotationAngle(0);
        setRadius(100 + 200 * Math.random());
        setHealth(1);
        setScale(0.3);
    }

    @Override
    public final double getRadius() {
        return super.getRadius() * (.75 + Math.abs(Math.sin(theta)) + .25);
    }

    @Override
    public Image getSprite() {
        return null;
    }

    @Override
    public void collide(final Colidable colidable) {

    }

    @Override
    public void update() {
        theta += .001;
        if (theta > PiConstants.TAU) {
            theta -= PiConstants.TAU;
        }
    }
}
