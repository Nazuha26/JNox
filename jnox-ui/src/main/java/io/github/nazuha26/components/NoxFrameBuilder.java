package io.github.nazuha26.components;

import java.awt.Dimension;

public class NoxFrameBuilder {
    NoxFrameBuilder() { }

    private String title = "";
    private Dimension size;
    private Dimension minimumSize = NoxFrameOptions.defaults().minimumSize();

    private boolean resizable = NoxFrameOptions.defaults().resizable();
    private boolean maximizable = NoxFrameOptions.defaults().maximizable();
    private boolean minimizable = NoxFrameOptions.defaults().minimizable();
    private boolean closable = NoxFrameOptions.defaults().closable();

    /**
     * Sets the window title.
     *
     * @param title window title text
     * @return current builder instance
     */
    public NoxFrameBuilder title(String title) {
        this.title = title == null ? "" : title;
        return this;
    }

    /**
     * Sets the initial window size.
     *
     * @param width window width in pixels
     * @param height window height in pixels
     * @return current builder instance
     */
    public NoxFrameBuilder size(int width, int height) {
        this.size = new Dimension(width, height);
        return this;
    }

    /**
     * Sets the minimum window size.
     *
     * @param width minimum width in pixels
     * @param height minimum height in pixels
     * @return current builder instance
     */
    public NoxFrameBuilder minimumSize(int width, int height) {
        this.minimumSize = new Dimension(width, height);
        return this;
    }

    /**
     * Enables or disables window resizing.
     *
     * @param resizable true to allow resizing
     * @return current builder instance
     */
    public NoxFrameBuilder resizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    /**
     * Enables or disables window maximizing.
     * When disabled, the maximize button is hidden and native maximize actions
     * like Win + Up also do not work.
     *
     * @param maximizable true to allow window maximizing
     * @return current builder instance
     */
    public NoxFrameBuilder maximizable(boolean maximizable) {
        this.maximizable = maximizable;
        return this;
    }

    /**
     * Shows or hides the minimize button.
     * This does not block native/system minimize actions.
     *
     * @param minimizable true to show the minimize button
     * @return current builder instance
     */
    public NoxFrameBuilder minimizable(boolean minimizable) {
        this.minimizable = minimizable;
        return this;
    }

    /**
     * Shows or hides the close button.
     * This does not block native/system close actions.
     *
     * @param closable true to show the close button
     * @return current builder instance
     */
    public NoxFrameBuilder closable(boolean closable) {
        this.closable = closable;
        return this;
    }

    /**
     * Builds and returns a configured component.
     *
     * @return configured component
     */
    public NoxNativeFrame build() {
        NoxFrameOptions options = new NoxFrameOptions(
                resizable,
                maximizable,
                minimizable,
                closable,
                minimumSize
        );

        NoxNativeFrame frame = new NoxNativeFrame(title, options);

        if (size != null) {
            frame.setSize(size);
        }

        return frame;
    }
}