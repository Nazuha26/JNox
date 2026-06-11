package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;
import io.github.nazuha26.utils.IconManager;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class CaptionButton extends JButton {

    public enum CaptionButtonType {
        MINIMIZE, MAXIMIZE, CLOSE
    }

    private static final int ICON_SIZE = 16;

    private boolean isHovered = false;
    @Getter private final CaptionButtonType buttonType;
    private Icon currentIcon;
    private Icon maximizeIcon;
    private Icon restoreIcon;

    private static final int caption_alpha = 160;
    private static final Color MINIMIZE_COLOR = new Color(NoxTheme.SUCCESS.getRed(), NoxTheme.SUCCESS.getGreen(), NoxTheme.SUCCESS.getBlue(), caption_alpha);
    private static final Color MAXIMIZE_COLOR = new Color(NoxTheme.WARNING.getRed(), NoxTheme.WARNING.getGreen(), NoxTheme.WARNING.getBlue(), caption_alpha);
    private static final Color CLOSE_COLOR = new Color(NoxTheme.ERROR.getRed(), NoxTheme.ERROR.getGreen(), NoxTheme.ERROR.getBlue(), caption_alpha);

    public CaptionButton(CaptionButtonType buttonType) {
        this.buttonType = buttonType;
        loadIcons();

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

    private void loadIcons() {
        switch (buttonType) {
            case MINIMIZE -> {
                currentIcon = IconManager.getSvgIcon("/icons/svg/caption/minimize.svg", ICON_SIZE, ICON_SIZE, null);
            }
            case MAXIMIZE -> {
                maximizeIcon = IconManager.getSvgIcon("/icons/svg/caption/maximize.svg", ICON_SIZE, ICON_SIZE, null);
                restoreIcon = IconManager.getSvgIcon("/icons/svg/caption/restore.svg", ICON_SIZE, ICON_SIZE, null);
                currentIcon = maximizeIcon;
            }
            case CLOSE -> {
                currentIcon = IconManager.getSvgIcon("/icons/svg/caption/close.svg", ICON_SIZE, ICON_SIZE, null);
            }
        }
    }

    public void setWindowMaximized(boolean maximized) {
        if (this.buttonType == CaptionButtonType.MAXIMIZE) {
            this.currentIcon = maximized ? restoreIcon : maximizeIcon;
            repaint();
        }
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
                case MINIMIZE -> g2.setColor(MINIMIZE_COLOR);
                case MAXIMIZE -> g2.setColor(MAXIMIZE_COLOR);
                case CLOSE -> g2.setColor(CLOSE_COLOR);
            }
        }
        int padding = 2;
        g2.fill(new RoundRectangle2D.Float(padding, padding, w - 2 * padding, h - 2 * padding, (float) w, (float) h));

        // SVG Icons
        if (currentIcon != null) {
            int iconX = (w - currentIcon.getIconWidth()) / 2;
            int iconY = (h - currentIcon.getIconHeight()) / 2;
            currentIcon.paintIcon(this, g2, iconX, iconY);
        }

        g2.dispose();

        super.paintComponent(g);
    }
}