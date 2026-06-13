package entities.amo;

import static assets.AssetManager.getImage;

import entities.GameObject;
import entities.SelfDefendable;
import entities.motion.Position;
import entities.motion.Velocity;
import java.awt.Image;
import lombok.Getter;

/// {@inheritDoc}
public class BulletLevel2 extends GameObject implements Bullet {
    private static final double PI2 = Math.PI * 2;

    private final static Image sprite = getImage("missile2.png").get();

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

    @Override
    public void collide(final GameObject gameObject) {
        Bullet.super.collide(gameObject);
    }
}
