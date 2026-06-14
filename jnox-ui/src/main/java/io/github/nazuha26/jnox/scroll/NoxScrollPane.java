package io.github.nazuha26.jnox.scroll;

import io.github.nazuha26.jnox.Nox;
import io.github.nazuha26.jnox.theme.NoxTheme;

import javax.swing.*;
import java.awt.*;

public class NoxScrollPane extends JScrollPane {

    public NoxScrollPane() {
        this(null);
    }

    public NoxScrollPane(Component view) {
        super(view);

        Nox.install();

        setOpaque(false);
        getViewport().setOpaque(false);

        setBorder(BorderFactory.createEmptyBorder());
        setViewportBorder(BorderFactory.createEmptyBorder());

        getVerticalScrollBar().setUI(new NoxScrollBarUI());
        getHorizontalScrollBar().setUI(new NoxScrollBarUI());

        getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));

        JPanel corner = new JPanel();
        corner.setBackground(NoxTheme.TRANSPARENT);
        setCorner(JScrollPane.LOWER_RIGHT_CORNER, corner);

        getVerticalScrollBar().setUnitIncrement(16);
    }
}