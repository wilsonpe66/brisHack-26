package entities;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

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

    // Second dispatch - overloaded methods for each concrete type.
    // Defaults are no-ops; subclasses override only the pairs that need a response.
    public void collideWith(Player player) {
    }

    public void collideWith(Asteroid asteroid) {
    }

    public void collideWith(Bullet bullet) {
    }

    public void collideWith(Alien alien) {
    }

    public void collideWith(AlienBullet alienBullet) {
    }
}
