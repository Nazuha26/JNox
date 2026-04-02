package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;

public class NoxButton extends JButton {

    public NoxButton(String text) {
        super(text);

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);

        setForeground(NoxTheme.FG);
        setFont(NoxTheme.FONT_BOLD);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        float w = getWidth();
        float h = getHeight();

        float bgX = 4f, bgY = 4f;
        float bgW = w - 8f, bgH = h - 8f;

        // Background
        if (isDefaultButton()) {
            g2.setColor(NoxTheme.ACCENT);
        } else {
            g2.setColor(NoxTheme.TRANSPARENT);
        }
        int arc = 15;
        g2.fill(new RoundRectangle2D.Float(bgX, bgY, bgW, bgH, arc, arc));

        // Outline
        if (!isDefaultButton()) {
            g2.setColor(NoxTheme.BG_SECOND);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(bgX, bgY, bgW, bgH, arc, arc));
        }

        // Focus ring
        if (hasFocus()) {
            g2.setColor(NoxTheme.ACCENT);
            g2.setStroke(new BasicStroke(2f));

            if (isDefaultButton()) {
                g2.draw(new RoundRectangle2D.Float(2f, 2f, w - 4f, h - 4f, arc + 4f, arc + 4f));
            } else {
                g2.draw(new RoundRectangle2D.Float(bgX, bgY, bgW, bgH, arc, arc));
            }
        }

        g2.dispose();

        super.paintComponent(g);
    }
}