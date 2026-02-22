import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class InputHandler implements KeyListener {
    private boolean leftPressed, rightPressed, upPressed, downPressed, shootPressed;

    // Double-brace initializer: creates an anonymous HashMap subclass and immediately
    // populates it. Each entry maps a key code to a lambda that sets the corresponding flag.
    // Consumer<Boolean> accepts true (pressed) or false (released).
    private final Map<Integer, Consumer<Boolean>> keyMap = new HashMap<>() {{
        put(KeyEvent.VK_W, v -> upPressed = v);
        put(KeyEvent.VK_UP,    v -> upPressed = v);
        put(KeyEvent.VK_S,     v -> downPressed = v);
        put(KeyEvent.VK_DOWN,  v -> downPressed = v);
        put(KeyEvent.VK_A,     v -> leftPressed = v);
        put(KeyEvent.VK_LEFT,  v -> leftPressed = v);
        put(KeyEvent.VK_D,     v -> rightPressed = v);
        put(KeyEvent.VK_RIGHT, v -> rightPressed = v);
        put(KeyEvent.VK_SPACE, v -> shootPressed = v);
    }};

    // keyTyped is for character input (e.g. typing text). Not used here — we only care about
    // key press/release for continuous movement, which is handled by keyPressed/keyReleased.
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        Consumer<Boolean> action = keyMap.get(e.getKeyCode());
        if (action != null) action.accept(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Consumer<Boolean> action = keyMap.get(e.getKeyCode());
        if (action != null) action.accept(false);
    }

    public boolean isUpPressed() { return upPressed; }
    public boolean isDownPressed() { return downPressed; }
    public boolean isLeftPressed() { return leftPressed; }
    public boolean isRightPressed() { return rightPressed; }
    public boolean isShootPressed() { return shootPressed; }

    /** Clear all key states. Call when restarting so keys held during game over don't carry over. */
    public void clearAllKeys() {
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        shootPressed = false;
    }
}
