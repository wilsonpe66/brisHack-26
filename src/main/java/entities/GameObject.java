package entities;

import java.awt.Image;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public abstract class GameObject implements Updatable {

    private static Image sprite;
    private double velocityX;
    private double velocityY;
    private double rotationAngle; // radians
    private double positionX;
    private double positionY;
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

    // SET X and Y TOGETHER:
    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    public abstract void collide(final GameObject gameObject);
}
