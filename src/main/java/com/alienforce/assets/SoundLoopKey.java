package com.alienforce.assets;

public enum SoundLoopKey implements SoundKey {
    BACK_GROUND("background.wav"),
    THRUSTER("thruster.wav"),
    MENU_MUSIC( "mixkit-fright-night-871.wav");

    final String resourceId;

    SoundLoopKey(final String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String resourceId() {
        return resourceId;
    }
}
