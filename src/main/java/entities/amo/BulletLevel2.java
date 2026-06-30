package entities.amo;

import static assets.AssetManager.getImage;

import assets.ImageKey;
import entities.GameObject;
import entities.SelfDefendable;
import entities.motion.Position;
import entities.motion.Velocity;
import java.awt.Image;
import lombok.Getter;

/// {@inheritDoc}
public class BulletLevel2 extends GameObject implements Bullet {

    private final static Image sprite = getImage(ImageKey.BULLET_2).get();

    @Getter
    private final SelfDefendable owner;

    // CONSTRUCTOR:
    public BulletLevel2(final Position position, final Velocity velocity, final double rotationAngle, final SelfDefendable owner) {
        setPosition(position);
        setVelocity(velocity);
        setRotationAngle(rotationAngle);
        setRadius(5);
        setHealth(1);
        this.owner = owner;
    }

    @Override
    public Image getSprite() {
        return sprite;
    }
}
