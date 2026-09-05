package com.alienforce.entities.amo;

import com.alienforce.assets.ImageKey;
import com.alienforce.entities.GameObject;
import com.alienforce.entities.SelfDefendable;
import com.alienforce.motion.Position;
import com.alienforce.motion.Velocity;
import lombok.Getter;

import java.awt.Image;

import static com.alienforce.assets.AssetManager.getImage;

/// {@inheritDoc}
public class BulletLevel1 extends GameObject implements Bullet {

    private final static Image sprite = getImage(ImageKey.BULLET_1).get();

    @Getter
    private final SelfDefendable owner;

    // CONSTRUCTOR:
    public BulletLevel1(final Position position, final Velocity velocity, final double rotationAngle, final SelfDefendable owner) {
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
