package io.github.nazuha26.utils;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.net.URL;

/**
 * A utility class responsible for loading and managing icons for the UI.
 * <p>
 * This manager provides handler for SVG icon. It includes
 * a multi-tier fallback mechanism: if the requested SVG cannot be found or parsed,
 * it attempts to load a default placeholder PNG. If the placeholder is also missing,
 * it falls back to a user-provided default {@link Icon}.
 * </p>
 *
 * @author Nazuha26
 */
@Slf4j
public class IconManager {

    private static final String PLACEHOLDER_PATH = "/icons/png/placeholder.png";

    /**
     * Loads an SVG icon from the specified classpath resource path.
     * <p>
     * <strong>Note:</strong> The path must be absolute relative to the classpath
     * (e.g., {@code "/icons/svg/info.svg"}).
     * </p>
     *
     * @param path     the absolute classpath to the SVG resource.
     * @param width    the desired target width of the loaded icon.
     * @param height   the desired target height of the loaded icon.
     * @param fallback the default {@link Icon} to return if both the SVG and
     * the internal placeholder PNG fail to load.
     * @return an {@link Icon} instance representing the SVG, a placeholder, or the fallback.
     */
    public static Icon getSvgIcon(String path, int width, int height, Icon fallback) {
        URL imgURL = IconManager.class.getResource(path);

        if (imgURL == null) {
            log.warn("SVG Icon not found: {}. Using fallback.", path);
            return fallback;
        }

        try {
            return new SvgIcon(imgURL, width, height);
        } catch (Exception e) {
            log.error("Failed to parse SVG document from: {}", path, e);
            return getPlaceholderIcon(fallback);
        }
    }

    /**
     * Attempts to load a standard PNG placeholder icon if SVG parsing fails.
     *
     * @param fallback the ultimate fallback {@link Icon} to use if the placeholder
     * image is also missing from the resources.
     * @return the placeholder {@link ImageIcon}, or the provided fallback.
     */
    private static Icon getPlaceholderIcon(Icon fallback) {
        URL placeholderURL = IconManager.class.getResource(PLACEHOLDER_PATH);
        if (placeholderURL != null) {
            return new ImageIcon(placeholderURL);
        } else {
            log.error("Placeholder PNG icon not found. Falling back to default.");
            return fallback;
        }
    }
}