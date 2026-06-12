package io.github.nazuha26.components;

import java.awt.Component;
import java.awt.Dimension;

public class NoxScrollPaneBuilder {
    NoxScrollPaneBuilder() { }

    private Component view;
    private Dimension preferredSize;

    public NoxScrollPaneBuilder view(Component view) {
        this.view = view;
        return this;
    }

    public NoxScrollPaneBuilder preferredSize(int width, int height) {
        this.preferredSize = new Dimension(width, height);
        return this;
    }

    /**
     * Builds and returns a configured component.
     *
     * @return configured component
     */
    public NoxScrollPane build() {
        NoxScrollPane scrollPane = new NoxScrollPane(view);

        if (preferredSize != null) {
            scrollPane.setPreferredSize(preferredSize);
        }

        return scrollPane;
    }
}