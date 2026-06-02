package entities;

public class AlienBullet extends Bullet {

    public AlienBullet(final Position position, final Velocity velocity, final double rotationAngle, final Alien alienOwner) {
        super(position, velocity, rotationAngle);
    }

    @Override
    public void collide(final GameObject gameObject) {
        switch (gameObject) {
            case Alien _, AlienBullet _ -> {
            }

            case null -> {
            }
            default -> dei();
        }
    }
}
