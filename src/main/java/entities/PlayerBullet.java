package entities;

import utils.Constants;

public class PlayerBullet extends Bullet {

    private final Player owner;

    // CONSTRUCTOR:
    public PlayerBullet(final Position position) {
        super(position);
        owner = null;
    }

    public PlayerBullet(final Position position, final Velocity velocity, double rotationAngle) {
        super(position, velocity, rotationAngle);
        owner = null;
    }

    public PlayerBullet(final Position position, final Velocity velocity, double rotationAngle, Player owner) {
        super(position, velocity, rotationAngle);
        this.owner = owner;
    }

    @Override
    public void collide(final GameObject gameObject) {
        switch (gameObject) {
            case Player _, Asteroid _, PlayerBullet _, AlienBullet _ -> dei();
            case Alien _ -> {
                dei();
                if (owner != null) {
                    owner.incrementScore(Constants.ALIEN_KILL_SCORE);
                }
            }

            case null, default -> {
            }
        }
    }
}
