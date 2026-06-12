package io.github.nazuha26.components;

import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;

public class NoxDialogBuilder {
    NoxDialogBuilder() { }

    private Frame owner;
    private String title = "";
    private boolean modal;
    private boolean resizable = true;
    private Dimension size;
    private Dimension minimumSize;
    private Integer defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE;
    private Component locationRelativeTo;

    /**
     * Sets the dialog owner.
     *
     * @param owner parent frame
     * @return current builder instance
     */
    public NoxDialogBuilder owner(Frame owner) {
        this.owner = owner;
        return this;
    }

    /**
     * Sets the dialog title.
     *
     * @param title dialog title text
     * @return current builder instance
     */
    public NoxDialogBuilder title(String title) {
        this.title = title == null ? "" : title;
        return this;
    }

    /**
     * Enables or disables modal mode.
     * When enabled, the parent window is blocked.
     *
     * @param modal true to make dialog modal
     * @return current builder instance
     */
    public NoxDialogBuilder modal(boolean modal) {
        this.modal = modal;
        return this;
    }

    /**
     * Enables or disables dialog resizing.
     *
     * @param resizable true to allow resizing
     * @return current builder instance
     */
    public NoxDialogBuilder resizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    /**
     * Sets the initial dialog size.
     *
     * @param width dialog width in pixels
     * @param height dialog height in pixels
     * @return current builder instance
     */
    public NoxDialogBuilder size(int width, int height) {
        this.size = new Dimension(width, height);
        return this;
    }

    /**
     * Sets the minimum dialog size.
     *
     * @param width minimum width in pixels
     * @param height minimum height in pixels
     * @return current builder instance
     */
    public NoxDialogBuilder minimumSize(int width, int height) {
        this.minimumSize = new Dimension(width, height);
        return this;
    }

    /**
     * Sets the close operation for the dialog.
     *
     * @param defaultCloseOperation Swing close operation
     * @return current builder instance
     */
    public NoxDialogBuilder defaultCloseOperation(int defaultCloseOperation) {
        this.defaultCloseOperation = defaultCloseOperation;
        return this;
    }

    /**
     * Sets dialog location relative to another component.
     *
     * @param component component used for centering
     * @return current builder instance
     */
    public NoxDialogBuilder locationRelativeTo(Component component) {
        this.locationRelativeTo = component;
        return this;
    }

    /**
     * Builds and returns a configured component.
     *
     * @return configured component
     */
    public NoxNativeDialog build() {
        NoxNativeDialog dialog = new NoxNativeDialog(owner, title, modal);

        dialog.setResizable(resizable);

        if (defaultCloseOperation != null) {
            dialog.setDefaultCloseOperation(defaultCloseOperation);
        }

        if (minimumSize != null) {
            dialog.setMinimumSize(minimumSize);
        }

        if (size != null) {
            dialog.setSize(size);
        }

        if (locationRelativeTo != null) {
            dialog.setLocationRelativeTo(locationRelativeTo);
        }

        return dialog;
    }
}