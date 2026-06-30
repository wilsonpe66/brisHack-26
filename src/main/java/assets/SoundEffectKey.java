package assets;

public enum SoundEffectKey implements SoundKey {
    SHOOT("shoot.wav"),
    EXPLOSION("explosion.wav"),
    LEVEL_UP("win.wav"),
    THRUSTER("thruster.wav"),
    GAME_OVER("game-over.wav");

    final String resourceId;

    SoundEffectKey(final String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String resourceId() {
        return resourceId;
    }
}
