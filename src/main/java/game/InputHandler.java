package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;

public class InputHandler implements KeyListener {

    @Getter
    private boolean leftPressed;
    @Getter
    private boolean rightPressed;
    @Getter
    private boolean upPressed;
    @Getter
    private boolean downPressed;
    @Getter
    private boolean shootPressed;
    @Getter
    private boolean superShootPressed;
    @Getter
    private boolean pausedPressed;

    // Double-brace initializer: creates an anonymous HashMap subclass and immediately
    // populates it. Each entry maps a key code to a lambda that sets the corresponding flag.
    // Consumer<Boolean> accepts true (pressed) or false (released).
    private final Map<Integer, Consumer<Boolean>> keyMap = new HashMap<>() {{
        put(KeyEvent.VK_W, v -> upPressed = v);
        put(KeyEvent.VK_UP, v -> upPressed = v);
        put(KeyEvent.VK_S, v -> downPressed = v);
        put(KeyEvent.VK_DOWN, v -> downPressed = v);
        put(KeyEvent.VK_A, v -> leftPressed = v);
        put(KeyEvent.VK_LEFT, v -> leftPressed = v);
        put(KeyEvent.VK_D, v -> rightPressed = v);
        put(KeyEvent.VK_RIGHT, v -> rightPressed = v);
        put(KeyEvent.VK_SPACE, v -> shootPressed = v);
        put(KeyEvent.VK_ENTER, v -> pausedPressed = v);
        put(KeyEvent.VK_Z, v -> shootPressed = v);
        put(KeyEvent.VK_X, v -> superShootPressed = v);
    }};

    // keyTyped is for character input (e.g. typing text). Not used here — we only care about
    // key press/release for continuous movement, which is handled by keyPressed/keyReleased.
    @Override
    public void keyTyped(final KeyEvent event) {
    }

    @Override
    public void keyPressed(final KeyEvent event) {
        final Consumer<Boolean> action = keyMap.get(event.getKeyCode());
        if (action != null) {
            action.accept(true);
        }
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        final Consumer<Boolean> action = keyMap.get(event.getKeyCode());
        if (action != null) {
            action.accept(false);
        }
    }

    /**
     * Clear all key states. Call when restarting so keys held during game over don't carry over.
     */
    public void clearAllKeys() {
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        shootPressed = false;
        superShootPressed = false;
        pausedPressed = false;
    }
}
