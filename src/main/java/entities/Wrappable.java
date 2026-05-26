package entities;

import utils.Constants;

public interface Wrappable {

    Position getPosition();

    void setPosition(Position position);

    default void wrapPosition() {
        // check if off the screen - put to other side:
        if (!(getPosition() instanceof Position(var x, var y))) {
            return;
        }

        if (x < 0) {
            x = Constants.WIDTH;
        } else if (x > Constants.WIDTH) {
            x = 0;
        }

        if (y < 0) {
            y = Constants.HEIGHT;
        } else if (y > Constants.HEIGHT) {
            y = 0;
        }
        setPosition(new Position(x, y));
    }
}
