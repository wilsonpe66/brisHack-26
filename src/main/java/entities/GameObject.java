package entities;

import java.awt.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import utils.Constants;

@Setter
@Getter
@ToString
public abstract class GameObject implements Updatable {

    private static Image sprite;
    private Velocity velocity;
    private double rotationAngle; // radians
    private Position position;
    // TODO: fix arbitrary magic numbers for radius in all gameobjects
    private double radius;
    private int health;
    private double scale = 1.0;

    public final boolean isAlive() {
        return health > 0;
    }

    public final boolean isDead() {
        return !isAlive();
    }

    public void dei() {
        health = 0;
    }

    public abstract Image getSprite();

    public abstract void collide(final GameObject gameObject);
}
