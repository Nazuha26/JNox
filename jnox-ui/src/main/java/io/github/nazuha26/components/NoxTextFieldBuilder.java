package io.github.nazuha26.components;

import java.awt.Dimension;
import java.awt.event.ActionListener;

public class NoxTextFieldBuilder {
    NoxTextFieldBuilder() { }

    private String text = "";
    private String placeholder = "";
    private Integer columns;
    private Dimension preferredSize;
    private boolean editable = true;
    private boolean enabled = true;
    private ActionListener actionListener;

    public NoxTextFieldBuilder text(String text) {
        this.text = text == null ? "" : text;
        return this;
    }

    public NoxTextFieldBuilder placeholder(String placeholder) {
        this.placeholder = placeholder == null ? "" : placeholder;
        return this;
    }

    public NoxTextFieldBuilder columns(int columns) {
        this.columns = columns;
        return this;
    }

    public NoxTextFieldBuilder preferredSize(int width, int height) {
        this.preferredSize = new Dimension(width, height);
        return this;
    }

    public NoxTextFieldBuilder editable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public NoxTextFieldBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public NoxTextFieldBuilder onAction(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    /**
     * Builds and returns a configured component.
     *
     * @return configured component
     */
    public NoxTextField build() {
        NoxTextField field = new NoxTextField(text);

        field.setPlaceholder(placeholder);
        field.setEditable(editable);
        field.setEnabled(enabled);

        if (columns != null) {
            field.setColumns(columns);
        }

        if (preferredSize != null) {
            field.setPreferredSize(preferredSize);
        }

        if (actionListener != null) {
            field.addActionListener(actionListener);
        }

        return field;
    }
}