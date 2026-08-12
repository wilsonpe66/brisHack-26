package com.alienforce.assets;

public enum ImageKey implements AssetKey {
    ALIEN("shipGreen_manned.png"),
    ALIEN_BOSS_1("boss-alien/1.png"),
    ALIEN_BOSS_2("boss-alien/2.png"),
    ALIEN_BOSS_3("boss-alien/3.png"),
    ALIEN_BOSS_4("boss-alien/4.png"),
    ALIEN_BOSS_5("boss-alien/5.png"),
    ASTEROID_1("asteroid/1.png"),
    ASTEROID_2("asteroid/2.png"),
    ASTEROID_3("asteroid/3.png"),
    ASTEROID_4("asteroid/4.png"),
    SPACE_SHIP_1("spaceship/1.png"),
    SPACE_SHIP_2("spaceship/2.png"),
    SPACE_SHIP_3("spaceship/3.png"),
    SPACE_SHIP_4("spaceship/4.png"),
    SPACE_SHIP_5("spaceship/5.png"),
    SPACE_SHIP_6("spaceship/6.png"),
    BULLET_1("missile/1.png"),
    BULLET_2("missile/2.png"),
    BULLET_3("missile/3.png"),
    BULLET_4("missile/4.png"),
    BULLET_5("missile/5.png"),
    BULLET_6("missile/6.png"),
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
