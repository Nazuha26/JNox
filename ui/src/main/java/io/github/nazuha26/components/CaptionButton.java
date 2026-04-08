package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class CaptionButton extends JButton {

    public enum CaptionButtonType {
        MINIMIZE, MAXIMIZE, CLOSE
    }

    private boolean isHovered = false;
    private final CaptionButtonType buttonType;

    public CaptionButton(CaptionButtonType buttonType) {
        this.buttonType = buttonType;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setFocusable(false);
        setDefaultCapable(false);
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        int w = getWidth();
        int h = getHeight();

        // Background
        if (!isHovered) {
            g2.setColor(NoxTheme.BG_SURFACE);
        } else {
            switch (buttonType) {
                case MINIMIZE -> g2.setColor(NoxTheme.SUCCESS);
                case MAXIMIZE -> g2.setColor(NoxTheme.WARNING);
                case CLOSE -> g2.setColor(NoxTheme.ERROR);
            }
        }
        int padding = 2;
        g2.fill(new RoundRectangle2D.Float(padding, padding, w - 2 * padding, h - 2 * padding, (float) w, (float) h));

        // --- Icons Fallback Painting ---
        int p4w = w / 3;
        int p4h = h / 3;

        if (!isHovered) {
            g2.setColor(NoxTheme.TEXT_PRIMARY);
            switch (buttonType) {
                case MINIMIZE -> g2.drawLine(p4w, h - p4h, w - p4w, h - p4h);
                case MAXIMIZE -> g2.drawRect(p4w, p4h, w - 2 * p4w, h - 2 * p4h);
                case CLOSE -> {
                    g2.drawLine(p4w, p4h, w - p4w, h - p4h);
                    g2.drawLine(w - p4w, p4h, p4w, h - p4h);
                }
            }
        }

        g2.dispose();

        super.paintComponent(g);
    }
}