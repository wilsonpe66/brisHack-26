package entities;

import entities.motion.Velocity;

public record Position(
    double x,
    double y
) {

    public Velocity minus(final Position position) {
        return new Velocity(x - position.x, y - position.y);
    }

    public Position add(final Velocity velocity) {
        return new Position(x + velocity.dx(), y + velocity.dy());
    }
}
