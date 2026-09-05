package com.alienforce.game;

import com.alienforce.utils.Constants;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class RoundedButton extends JButton {

    private static final Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);

    public RoundedButton(final String label) {
        super(label);
        setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        final Graphics2D newGraphics = (Graphics2D) graphics.create();

        newGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        newGraphics.setColor(Color.YELLOW);
        final int width = getWidth();
        final int height = getHeight();
        newGraphics.fillRoundRect(0, 0, width, height, 40, 40);
        newGraphics.setFont(font);
        newGraphics.setColor(Color.BLACK);

        final FontMetrics fontMetrics = newGraphics.getFontMetrics(font);
        final String text = getText();
        final int textWidth = fontMetrics.stringWidth(text);
        final int x = (width - textWidth) / 2;
        final int y = (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

        newGraphics.drawString(text.toUpperCase(), x, y);
        newGraphics.dispose();
    }
}
