package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;

public final class NoxBorders {
    private static final int CONTENT_TOP = 4;
    private static final int CONTENT_LEFT = 8;
    private static final int CONTENT_BOTTOM = 8;
    private static final int CONTENT_RIGHT = 8;

    private NoxBorders() { }

    public static Border titled(String title) {
        return titled(title, NoxTheme.OUTLINE);
    }

    public static Border titledAccent(String title) {
        return titled(title, NoxTheme.ACCENT_PRIMARY);
    }

    public static Border surface() {
        return BorderFactory.createLineBorder(NoxTheme.OUTLINE);
    }

    public static Border titled(String title, Color lineColor) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(lineColor),
                title == null ? "" : title,
                TitledBorder.LEADING,
                TitledBorder.TOP,
                NoxTheme.FONT_BOLD,
                NoxTheme.TEXT_PRIMARY
        );

        return BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(
                        CONTENT_TOP,
                        CONTENT_LEFT,
                        CONTENT_BOTTOM,
                        CONTENT_RIGHT
                )
        );
    }
}