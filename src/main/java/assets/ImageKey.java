package assets;

public enum ImageKey implements AssetKey {
    ALIEN("shipGreen_manned.png"),
    ALIEN_BOSS("boss-alien.png"),
    ASTEROID_1("asteroid1.png"),
    ASTEROID_2("asteroid2.png"),
    ASTEROID_3("asteroid3.png"),
    ASTEROID_4("asteroid4.png"),
    SPACE_SHIP("spaceship.png"),
    SPACE_SHIP_2("spaceship2-1.png"),
    BULLET_1("missile.png"),
    BULLET_2("missile2.png"),
    BULLET_3("missile3.png"),
    SPACE_BACKGROUND("spacebackground.png"),;

    final String resourceId;

    ImageKey(final String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String resourceId() {
        return resourceId;
    }
}
