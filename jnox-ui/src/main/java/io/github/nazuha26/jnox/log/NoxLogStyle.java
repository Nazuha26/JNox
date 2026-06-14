package io.github.nazuha26.jnox.log;

import io.github.nazuha26.jnox.theme.NoxTheme;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Color;

public record NoxLogStyle(
        Color foreground,
        boolean bold,
        boolean italic,
        boolean underline
) {
    public NoxLogStyle {
        if (foreground == null) {
            foreground = NoxTheme.TEXT_PRIMARY;
        }
    }

    public static NoxLogStyle normal() {
        return new NoxLogStyle(NoxTheme.TEXT_PRIMARY, false, false, false);
    }

    public static NoxLogStyle accent() {
        return new NoxLogStyle(NoxTheme.ACCENT_PRIMARY, false, false, false);
    }

    public static NoxLogStyle success() {
        return new NoxLogStyle(NoxTheme.SUCCESS, false, false, false);
    }

    public static NoxLogStyle warning() {
        return new NoxLogStyle(NoxTheme.WARNING, false, false, false);
    }

    public static NoxLogStyle error() {
        return new NoxLogStyle(NoxTheme.ERROR, false, false, false);
    }

    public static NoxLogStyle muted() {
        return new NoxLogStyle(new Color(
                NoxTheme.TEXT_PRIMARY.getRed(),
                NoxTheme.TEXT_PRIMARY.getGreen(),
                NoxTheme.TEXT_PRIMARY.getBlue(),
                130
        ), false, false, false);
    }

    public NoxLogStyle color(Color color) {
        return new NoxLogStyle(color, bold, italic, underline);
    }

    public NoxLogStyle plainBold(boolean bold) {
        return new NoxLogStyle(foreground, bold, italic, underline);
    }

    public NoxLogStyle plainItalic(boolean italic) {
        return new NoxLogStyle(foreground, bold, italic, underline);
    }

    public NoxLogStyle plainUnderline(boolean underline) {
        return new NoxLogStyle(foreground, bold, italic, underline);
    }

    SimpleAttributeSet toAttributes() {
        SimpleAttributeSet attributes = new SimpleAttributeSet();

        StyleConstants.setForeground(attributes, foreground);
        StyleConstants.setBold(attributes, bold);
        StyleConstants.setItalic(attributes, italic);
        StyleConstants.setUnderline(attributes, underline);
        StyleConstants.setFontFamily(attributes, NoxTheme.FONT_NAME);
        StyleConstants.setFontSize(attributes, NoxTheme.FONT_PLAIN.getSize());

        return attributes;
    }
}