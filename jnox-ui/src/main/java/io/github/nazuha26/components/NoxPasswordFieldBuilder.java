package io.github.nazuha26.components;

import java.awt.Dimension;
import java.awt.event.ActionListener;

public class NoxPasswordFieldBuilder {
    NoxPasswordFieldBuilder() { }

    private String text = "";
    private String placeholder = "";
    private Integer columns;
    private Dimension preferredSize;
    private boolean editable = true;
    private boolean enabled = true;
    private Character echoChar;
    private ActionListener actionListener;

    public NoxPasswordFieldBuilder text(String text) {
        this.text = text == null ? "" : text;
        return this;
    }

    public NoxPasswordFieldBuilder placeholder(String placeholder) {
        this.placeholder = placeholder == null ? "" : placeholder;
        return this;
    }

    public NoxPasswordFieldBuilder columns(int columns) {
        this.columns = columns;
        return this;
    }

    public NoxPasswordFieldBuilder preferredSize(int width, int height) {
        this.preferredSize = new Dimension(width, height);
        return this;
    }

    public NoxPasswordFieldBuilder editable(boolean editable) {
        this.editable = editable;
        return this;
    }

    public NoxPasswordFieldBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public NoxPasswordFieldBuilder echoChar(char echoChar) {
        this.echoChar = echoChar;
        return this;
    }

    public NoxPasswordFieldBuilder onAction(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    /**
     * Builds and returns a configured component.
     *
     * @return configured component
     */
    public NoxPasswordField build() {
        NoxPasswordField field = new NoxPasswordField();

        field.setText(text);
        field.setPlaceholder(placeholder);
        field.setEditable(editable);
        field.setEnabled(enabled);

        if (columns != null) {
            field.setColumns(columns);
        }

        if (preferredSize != null) {
            field.setPreferredSize(preferredSize);
        }

        if (echoChar != null) {
            field.setEchoChar(echoChar);
        }

        if (actionListener != null) {
            field.addActionListener(actionListener);
        }

        return field;
    }
}