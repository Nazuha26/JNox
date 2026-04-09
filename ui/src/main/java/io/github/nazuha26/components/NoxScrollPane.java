package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;

import javax.swing.*;
import java.awt.*;

public class NoxScrollPane extends JScrollPane {

    public NoxScrollPane(Component view) {
        super(view);

        setOpaque(false);
        getViewport().setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder());
        setViewportBorder(BorderFactory.createEmptyBorder());

        getVerticalScrollBar().setUI(new NoxScrollBarUI());
        getHorizontalScrollBar().setUI(new NoxScrollBarUI());

        getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

        // Bottom Left Corner
        JPanel corner = new JPanel();
        corner.setBackground(NoxTheme.TRANSPARENT);
        setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);

        // enhanced mouse wheel scrolling
        getVerticalScrollBar().setUnitIncrement(16);
    }
}