import java.awt.*;

public abstract class GameObject implements Updatable {
    private double velocityX;
    private double velocityY;
    private double rotationAngle; // radians
    private double positionX;
    private double positionY;
    private double radius;

    private int health;
    private boolean isAlive;
    private double scale = 1.0;

    private static Image sprite;

    // GETTERS:
    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getRadius() { return this.radius; }

    public int getHealth() {
        return health;
    }

    public boolean getIsAlive() {
        return isAlive;
    }

    public abstract Image getSprite();

    public double getScale() {
        return scale;
    }

    // SETTERS:
    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    public void setRadius(double radius) { this.radius = radius; } // TODO: fix arbitrary magic numbers for radius in all gameobjects

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    // SET X and Y TOGETHER:
    public void setPosition(double x, double y) {
        this.positionX = x;
        this.positionY = y;
    }

    public void setVelocity(double vx, double vy) {
        this.velocityX = vx;
        this.velocityY = vy;
    }

    // COLLISIONS - double dispatch:

    // entry point - first dispatch
    public abstract void collide(GameObject other);

    // overloading (default responses) - not abstract as some combinations don't need to exist
    public void collideWith(Player player) {}
    public void collideWith(Asteroid asteroid) {}
    public void collideWith(Bullet bullet) {}
    public void collideWith(Alien alien) {}
    public void collideWith(AlienBullet alienBullet) {}
}
