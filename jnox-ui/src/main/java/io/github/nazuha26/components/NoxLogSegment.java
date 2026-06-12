package io.github.nazuha26.components;

import java.awt.*;

public record NoxLogSegment(
        String text,
        NoxLogStyle style
) {
    public NoxLogSegment {
        if (text == null) {
            text = "";
        }

        if (style == null) {
            style = NoxLogStyle.normal();
        }
    }

    public static NoxLogSegment of(String text) {
        return new NoxLogSegment(text, NoxLogStyle.normal());
    }

    public static NoxLogSegment of(String text, NoxLogStyle style) {
        return new NoxLogSegment(text, style);
    }

    public static NoxLogSegment accent(String text) {
        return new NoxLogSegment(text, NoxLogStyle.accent());
    }

    public static NoxLogSegment success(String text) {
        return new NoxLogSegment(text, NoxLogStyle.success());
    }

    public static NoxLogSegment warning(String text) {
        return new NoxLogSegment(text, NoxLogStyle.warning());
    }

    public static NoxLogSegment error(String text) {
        return new NoxLogSegment(text, NoxLogStyle.error());
    }

    public static NoxLogSegment muted(String text) {
        return new NoxLogSegment(text, NoxLogStyle.muted());
    }

    public NoxLogSegment color(Color color) {
        return new NoxLogSegment(text, style.color(color));
    }

    public NoxLogSegment bold() {
        return new NoxLogSegment(text, style.bold());
    }

    public NoxLogSegment italic() {
        return new NoxLogSegment(text, style.italic());
    }

    public NoxLogSegment underline() {
        return new NoxLogSegment(text, style.underline());
    }

    public NoxLogSegment withStyle(NoxLogStyle style) {
        return new NoxLogSegment(text, style);
    }
}