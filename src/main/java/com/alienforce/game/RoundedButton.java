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
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class RoundedButton extends JButton {

    private static final Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);

    private boolean isFocused = false;

    public RoundedButton(final String label, final Runnable runnable) {
        super(label);
        setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        addActionListener(_ -> runnable.run());
        setBorder(javax.swing.BorderFactory.createEmptyBorder());

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(final MouseEvent mouseEvent) {
                isFocused = true;
            }

            @Override
            public void mouseReleased(final MouseEvent mouseEvent) {
                isFocused = false;
            }

            @Override
            public void mouseEntered(final MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(final MouseEvent mouseEvent) {

            }
        });
    }

    @Override
    protected void paintComponent(final Graphics graphics) {
        final Graphics2D newGraphics = (Graphics2D) graphics.create();

        newGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        newGraphics.setColor(Color.YELLOW);
        final int width = getWidth();
        final int height = getHeight();
        newGraphics.fillRoundRect(0, 0, width, height, 40, 40);
        if (isFocused) {
            newGraphics.setColor(Color.RED);
            newGraphics.fillRoundRect(3, 3, width - 6, height - 6, 40, 40);
        }
        newGraphics.setFont(font);
        newGraphics.setColor(isFocused ? Color.DARK_GRAY : Color.BLACK);

        final FontMetrics fontMetrics = newGraphics.getFontMetrics(font);
        final String text = getText();
        final int textWidth = fontMetrics.stringWidth(text);
        final int x = (width - textWidth) / 2;
        final int y = (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

        newGraphics.drawString(text.toUpperCase(), x, y);
        newGraphics.dispose();
    }
}
