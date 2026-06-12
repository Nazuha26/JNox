package io.github.nazuha26.components;

import java.awt.Dimension;

public record NoxFrameOptions(
        boolean resizable,
        boolean maximizable,
        boolean minimizable,
        boolean closable,
        Dimension minimumSize
) {
    public static NoxFrameOptions defaults() {
        return new NoxFrameOptions(
                true,
                true,
                true,
                true,
                new Dimension(256, 32)
        );
    }
}