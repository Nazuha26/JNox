package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;

public class NoxScrollBarUI extends BasicScrollBarUI {

    private boolean isTrackHover = false;
    private boolean isTrackPressed = false;

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        c.setOpaque(false);
    }

    @Override
    protected TrackListener createTrackListener() {
        return new NoxTrackListener();
    }

    /**
     * Custom track listener responsible for handling mouse interactions
     * over the scrollbar's track area. It tracks both hover state and
     * left-click press state to ensure the track remains visually highlighted
     * even if the user drags the mouse outside the component bounds.
     */
    protected class NoxTrackListener extends TrackListener {

        @Override
        public void mouseEntered(MouseEvent e) {
            super.mouseEntered(e);
            isTrackHover = true;
            scrollbar.repaint();
        }

        @Override
        public void mouseExited(MouseEvent e) {
            super.mouseExited(e);
            isTrackHover = false;
            scrollbar.repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            if (SwingUtilities.isLeftMouseButton(e)) {
                isTrackPressed = true;
                scrollbar.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            if (SwingUtilities.isLeftMouseButton(e)) {
                isTrackPressed = false;
                scrollbar.repaint();
            }
        }
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        Graphics2D g2 = (Graphics2D) g.create();

        if (isTrackHover || isTrackPressed) {
            g2.setColor(NoxTheme.SCROLL_TRACK_HOVER);
        } else {
            g2.setColor(NoxTheme.SCROLL_TRACK);
        }

        g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        g2.dispose();
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (isThumbRollover()) {
            g2.setColor(NoxTheme.SCROLL_THUMB_HOVER);
        } else {
            g2.setColor(NoxTheme.SCROLL_THUMB);
        }

        g2.fillRect(thumbBounds.x + 1, thumbBounds.y + 1, thumbBounds.width - 2, thumbBounds.height - 2);
        g2.dispose();
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }
}