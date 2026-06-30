package assets;

public enum ImageType implements AssetKey {
    ;

    final String resourceId;

    ImageType(final String resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public String resourceId() {
        return resourceId;
    }
}
