public abstract class GameObject {
    double velocityX;
    double velocityY;
    double rotationAngle;
    double positionX;
    double positionY;

    boolean isAlive;

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

    // NEEDS TO BE IMPLEMENTED BY EACH SUBCLASS:
    public abstract void update();
}
