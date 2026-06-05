package entities.motion;

public record Velocity(
    double dx,
    double dy
) {

    public static final Velocity ZERO = new Velocity(0, 0);

    public static Velocity fromAngleAndSpeed(final double angle, final double speed) {
        return new Velocity(Math.cos(angle) * speed, Math.sin(angle) * speed);
    }

    public Velocity scale(final double scale) {
        return new Velocity(dx * scale, dy * scale);
    }

    public Velocity add(final Velocity velocity) {
        return new Velocity(dx + velocity.dx, dy + velocity.dy);
    }

    public double getRotation() {
        return Math.atan2(dy, dx);
    }

    public double getSpeed() {
        return Math.sqrt(dx * dx + dy * dy);
    }
}
