package io.github.nazuha26.jnox.input;

import io.github.nazuha26.jnox.Nox;
import io.github.nazuha26.jnox.theme.NoxTheme;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class NoxPasswordField extends JPasswordField {
    private static final int ARC = 10;
    private static final Insets PADDING = new Insets(6, 10, 6, 10);

    @Getter private String placeholder = "";

    public NoxPasswordField() {
        this("");
    }

    public NoxPasswordField(String text) {
        super(text == null ? "" : text);

        Nox.install();

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(
                PADDING.top,
                PADDING.left,
                PADDING.bottom,
                PADDING.right
        ));

        setFont(NoxTheme.FONT_PLAIN);
        setForeground(NoxTheme.TEXT_PRIMARY);
        setCaretColor(NoxTheme.TEXT_PRIMARY);
        setSelectionColor(NoxTheme.ACCENT_PRIMARY);
        setSelectedTextColor(NoxTheme.TEXT_PRIMARY);
        setDisabledTextColor(NoxTheme.TEXT_PRIMARY.darker());
        setBackground(NoxTheme.BG_SURFACE);
        setEchoChar('*');

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

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder == null ? "" : placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(isEnabled() ? NoxTheme.BG_SURFACE : NoxTheme.BG_PRIMARY);
        g2.fill(new RoundRectangle2D.Float(1, 1, w - 2, h - 2, ARC, ARC));

        g2.dispose();

        super.paintComponent(g);

        if (getPassword().length == 0 && placeholder != null && !placeholder.isBlank()) {
            Graphics2D placeholderGraphics = (Graphics2D) g.create();
            placeholderGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            placeholderGraphics.setFont(getFont());
            placeholderGraphics.setColor(new Color(
                    NoxTheme.TEXT_PRIMARY.getRed(),
                    NoxTheme.TEXT_PRIMARY.getGreen(),
                    NoxTheme.TEXT_PRIMARY.getBlue(),
                    120
            ));

            FontMetrics fm = placeholderGraphics.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            placeholderGraphics.drawString(placeholder, PADDING.left, y);
            placeholderGraphics.dispose();
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        if (hasFocus()) {
            g2.setColor(NoxTheme.ACCENT_PRIMARY);
            g2.setStroke(new BasicStroke(2f));
            g2.draw(new RoundRectangle2D.Float(1, 1, w - 3, h - 3, ARC, ARC));
        } else {
            g2.setColor(NoxTheme.OUTLINE);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Float(1, 1, w - 3, h - 3, ARC, ARC));
        }

        g2.dispose();
    }
}