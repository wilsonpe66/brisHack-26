package entities.amo;

import static assets.AssetManager.getImage;

import entities.Alien;
import entities.Asteroid;
import entities.BossAlien;
import entities.GameObject;
import entities.Player;
import entities.SelfDefendable;
import entities.motion.Position;
import entities.motion.Velocity;
import java.awt.Image;
import lombok.Getter;
import utils.Constants;

public class Bullet extends GameObject {

    private final static Image sprite = getImage("missile.png").get();

    @Getter
    private final SelfDefendable owner;

    // CONSTRUCTOR:
    public Bullet(final Position position) {
        this(position, new Velocity(10, 10), Math.PI / 4, null);
    }

    public Bullet(final Position position, final Velocity velocity, final double rotationAngle, final SelfDefendable owner) {
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
    public void update() {
        // update position according to velocity:
        setPosition(getPosition().add(getVelocity()));

        if (getPosition() instanceof Position(var x, var y)) {
            if (x < 0 || x > Constants.WIDTH
                || y < 0 || y > Constants.HEIGHT) {
                dei();
            }
        }
    }

    @Override
    public void collide(final GameObject gameObject) {
        switch (gameObject) {
            case SelfDefendable selfDefendable when (getOwner() == selfDefendable) -> {

            }
            case BossAlien _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(7);
                dei();
            }
            case Alien _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(5);
                dei();
            }
            case Asteroid _ when (getOwner() instanceof Player player) -> {
                player.incrementScore(2);
                dei();
            }

            case Bullet otherBullet when (owner == otherBullet.owner) -> {
            }

            case null -> {

            }
            default -> {
                dei();
            }
        }
    }
}
