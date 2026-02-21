public final class Constants {
    private Constants() {}

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 50;


    public static final int FPS = 60;
    public static final int FRAME_DELAY = 1000 / FPS;
    public static final double ASTEROID_OFFSET = 50;

    // how far outside the screen asteroids spawn
    public static final double MIDDLEX = (double) WIDTH / 2;
    public static final double MIDDLEY = (double) HEIGHT / 2;
    public static final int SPAWN_DELAY = 60;
    public static final double ASTEROID_SPEED = 3;
    public static final double MAX_PLAYER_SPEED = 5;
}
