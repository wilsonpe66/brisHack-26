import javax.swing.*;
import java.awt.*;

public class ScreenManager extends JPanel {

    private CardLayout layout;

    public ScreenManager() {
        layout = new CardLayout();
        setLayout(layout);
    }

    public void addScreen(String name, JPanel panel) {
        add(panel, name);
    }

    public void showScreen(String name) {
        layout.show(this, name);
    }
}