package entities;

import java.awt.Image;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
    private boolean isAlive;
    private double scale = 1.0;

    public boolean getIsAlive() {
        return isAlive;
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

    // COLLISIONS - double dispatch pattern:
    // When two objects collide, we call a.collide(b) which internally calls b.collideWith(a).
    // This resolves both runtime types without instanceof checks, because each subclass
    // passes 'this' (its concrete type) to the other object's overloaded collideWith().

    // entry point - first dispatch: each subclass calls other.collideWith(this)
    public abstract void collide(GameObject other);

    public abstract void collideWith(GameObject gameObject);
}
