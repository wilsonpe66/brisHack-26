package com.alienforce.entities.amo;

import static com.alienforce.assets.AssetManager.getImage;

import com.alienforce.assets.ImageKey;
import com.alienforce.entities.GameObject;
import com.alienforce.entities.SelfDefendable;
import com.alienforce.entities.motion.Position;
import com.alienforce.entities.motion.Velocity;
import java.awt.Image;
import lombok.Getter;

/// {@inheritDoc}
public class BulletLevel3 extends GameObject implements Bullet {

    private final static Image sprite = getImage(ImageKey.BULLET_3).get();

    @Getter
    private final SelfDefendable owner;

    // CONSTRUCTOR:
    public BulletLevel3(final Position position, final Velocity velocity, final double rotationAngle, final SelfDefendable owner) {
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
