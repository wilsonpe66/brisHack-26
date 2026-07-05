package com.alienforce.game;

import javax.swing.SwingUtilities;

public class Main {

    static void main(final String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
