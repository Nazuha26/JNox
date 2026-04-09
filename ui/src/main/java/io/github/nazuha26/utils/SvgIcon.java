package io.github.nazuha26.utils;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox;
import com.github.weisj.jsvg.parser.SVGLoader;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * A custom Swing {@link Icon} implementation that renders an SVG document.
 * <p>
 * This class utilizes the {@code JSVG} library to parse and render vector graphics
 * directly onto a {@link Graphics2D} context. It ensures that the rendered output
 * scales properly to the requested dimensions and applies high-quality anti-aliasing.
 * </p>
 *
 * @author Nazuha26
 */
public class SvgIcon implements Icon {
    private final SVGDocument document;
    private final int width;
    private final int height;

    /**
     * Constructs a new {@code SvgIcon} from a given resource URL.
     *
     * @param path   the {@link URL} pointing to the SVG file resource.
     * @param width  the explicitly requested width for rendering the icon.
     * @param height the explicitly requested height for rendering the icon.
     * @throws RuntimeException if the SVG document is malformed or cannot be parsed
     * by the {@link SVGLoader}.
     */
    public SvgIcon(URL path, int width, int height) {
        this.width = width;
        this.height = height;
        SVGLoader loader = new SVGLoader();
        this.document = loader.load(path);

        if (this.document == null) {
            throw new RuntimeException("Could not parse SVG document from " + path);
        }
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (document != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            g2d.translate(x, y);

            document.render(c, g2d, new ViewBox(0, 0, width, height));

            g2d.dispose();
        }
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}