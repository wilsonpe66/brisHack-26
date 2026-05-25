package game;

import javax.swing.*;

public class Main {
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
