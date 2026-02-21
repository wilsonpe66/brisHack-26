public abstract class GameObject implements Updatable {
    protected double velocityX;
    protected double velocityY;
    protected double rotationAngle;
    protected double positionX;
    protected double positionY;

    protected int health;
    protected boolean isAlive;

    // GETTERS:
    public double getVelocityX() {
        return this.velocityX;
    }

    public double getVelocityY() {
        return this.velocityY;
    }

    public double getRotationAngle() {
        return this.rotationAngle;
    }

    public double getPositionX() {
        return this.positionX;
    }

    public double getPositionY() {
        return this.positionY;
    }

    public int getHealth() {
        return this.health;
    }

    public boolean getIsAlive() {
        return this.isAlive;
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

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAlive(boolean alive) {
        this.isAlive = alive;
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
}
