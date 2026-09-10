package com.alienforce.game;

import com.alienforce.utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.Consumer;
import javax.swing.JButton;

public class RoundedButton extends JButton {

    private static final Color DARK_YELLOW = new Color(Color.YELLOW.getRed() * 3 / 4, Color.YELLOW.getGreen() * 3 / 4, Color.YELLOW.getBlue() * 3 / 4);
    private static final Font font = new Font(Font.MONOSPACED, Font.BOLD, 20);
    private ButtonState buttonState = ButtonState.Default;

    public RoundedButton(final String label, final Runnable runnable) {
        super(label);
        setPreferredSize(new Dimension(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT));
        addActionListener(_ -> {
            buttonState = ButtonState.Default;
            runnable.run();
        });
        setBorder(javax.swing.BorderFactory.createEmptyBorder());

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(final MouseEvent mouseEvent) {
            }

            @Override
            public void mousePressed(final MouseEvent mouseEvent) {
                buttonState = ButtonState.MousePressed;
            }

            @Override
            public void mouseReleased(final MouseEvent mouseEvent) {
                buttonState = ButtonState.Default;
            }

            @Override
            public void mouseEntered(final MouseEvent mouseEvent) {
                buttonState = ButtonState.MousedOver;
            }

            @Override
            public void mouseExited(final MouseEvent mouseEvent) {
                buttonState = ButtonState.Default;
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

        final Consumer<Integer> drawRoundedRect = (borderSize) -> newGraphics.fillRoundRect(
            borderSize, borderSize, width - 2 * borderSize, height - 2 * borderSize, 30, 30
        );

        drawRoundedRect.accept(0);

        switch (buttonState) {
            case MousedOver -> {
                newGraphics.setColor(DARK_YELLOW);
                drawRoundedRect.accept(1);
                newGraphics.setColor(Color.BLACK);
            }
            case MousePressed -> {
                newGraphics.setColor(Color.RED);
                drawRoundedRect.accept(1);
                newGraphics.setColor(Color.DARK_GRAY);
            }
            default -> newGraphics.setColor(Color.BLACK);
        }

        newGraphics.setFont(font);
        final FontMetrics fontMetrics = newGraphics.getFontMetrics(font);
        final String text = getText();
        final int textWidth = fontMetrics.stringWidth(text);
        final int x = (width - textWidth) / 2;
        final int y = (height - fontMetrics.getHeight()) / 2 + fontMetrics.getAscent();

        newGraphics.drawString(text.toUpperCase(), x, y);
        newGraphics.dispose();
    }

    private enum ButtonState {
        Default,
        MousedOver,
        MousePressed
    }
}
