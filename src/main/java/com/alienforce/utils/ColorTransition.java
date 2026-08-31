package com.alienforce.utils;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

public record ColorTransition(
    List<Color> colors
) {

    public ColorTransition {
        if (colors == null || colors.size() < 2) {
            throw new IllegalArgumentException("color must not be at lease size 2.");
        }
        colors = Collections.unmodifiableList(colors);
    }

    private static double getColor(final double scale, double startColor, double endColor) {
        return (endColor - startColor) * scale + startColor;
    }

    private static Color getColor(final double scale, final Color startColor, final Color endColor) {
        return new Color(
            (int) getColor(scale, startColor.getRed(), endColor.getRed()),
            (int) getColor(scale, startColor.getGreen(), endColor.getGreen()),
            (int) getColor(scale, startColor.getBlue(), endColor.getBlue())
        );
    }

    public Color getColor(final double scale) {
        final int index = Math.clamp((int) scale, 0, colors().size() - 2);
        return getColor(scale - index, colors.get(index), colors.get(index + 1));
    }
}
