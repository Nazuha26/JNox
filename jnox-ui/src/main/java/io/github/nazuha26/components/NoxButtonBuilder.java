package io.github.nazuha26.components;

import javax.swing.JRootPane;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class NoxButtonBuilder {
    NoxButtonBuilder() { }

    private String text = "";
    private Dimension preferredSize;
    private ActionListener actionListener;
    private boolean defaultButton;
    private JRootPane rootPane;

    public NoxButtonBuilder text(String text) {
        this.text = text == null ? "" : text;
        return this;
    }

    public NoxButtonBuilder preferredSize(int width, int height) {
        this.preferredSize = new Dimension(width, height);
        return this;
    }

    public NoxButtonBuilder onClick(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public NoxButtonBuilder defaultButton(JRootPane rootPane) {
        this.defaultButton = true;
        this.rootPane = rootPane;
        return this;
    }

    /**
     * Builds and returns a configured component.
     *
     * @return configured component
     */
    public NoxButton build() {
        NoxButton button = new NoxButton(text);

        if (preferredSize != null) {
            button.setPreferredSize(preferredSize);
        }

        if (actionListener != null) {
            button.addActionListener(actionListener);
        }

        if (defaultButton && rootPane != null) {
            rootPane.setDefaultButton(button);
        }

        return button;
    }
}