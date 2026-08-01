package com.alienforce;

import com.alienforce.game.Game;

import javax.swing.SwingUtilities;

public class Main {

    static void main(final String[] args) {
        SwingUtilities.invokeLater(Game::new);
    }
}
