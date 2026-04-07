package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;
import io.github.nazuha26.OsUtils;
import io.github.nazuha26.WinNativeLib;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class NoxWindowDelegate {

    public static final int TITLE_BAR_HEIGHT = 32;
    public static final int TITLE_BAR_LEFT_INSET = 12;
    public static final int CAPTION_BUTTON_WIDTH = 32;
    public static final int CAPTION_BUTTON_HEIGHT = 32;

    private final Window window;
    @Getter private final WinNativeLib nativeLib = new WinNativeLib();
    @Getter private boolean nativeInstalled;

    @Getter private final JPanel titleBar = new JPanel(new BorderLayout());
    @Getter private final JPanel body = new JPanel();
    @Getter private final JLabel titleLabel = new JLabel();
    @Getter private final JPanel captionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

    private Point fallbackDragStartScreen;
    private Point fallbackWindowStart;

    public NoxWindowDelegate(Window window) {
        this.window = window;
    }

    public void install(String title, Runnable onDoubleClickTitleBar, CaptionButton... buttons) {
        if (OsUtils.isWindows()) {
            if (window instanceof Frame) ((Frame) window).setUndecorated(true);
            else if (window instanceof Dialog) ((Dialog) window).setUndecorated(true);
        }

        Container contentPane = window instanceof RootPaneContainer
                ? ((RootPaneContainer) window).getContentPane() : window;

        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(NoxTheme.BG_PRIMARY);

        configureTitleBar(title, buttons);
        configureBody();
        installFallbackDrag(onDoubleClickTitleBar);

        contentPane.add(titleBar, BorderLayout.NORTH);
        contentPane.add(body, BorderLayout.CENTER);
    }

    private void configureTitleBar(String title, CaptionButton... buttons) {
        titleBar.setOpaque(true);
        titleBar.setBackground(NoxTheme.BG_PRIMARY);
        titleBar.setPreferredSize(new Dimension(0, TITLE_BAR_HEIGHT));
        titleBar.setBorder(BorderFactory.createEmptyBorder(0, TITLE_BAR_LEFT_INSET, 0, 0));

        titleLabel.setText(title);
        titleLabel.setFont(NoxTheme.FONT_BOLD);
        titleLabel.setForeground(NoxTheme.TEXT_PRIMARY);

        captionButtons.setOpaque(false);
        for (CaptionButton btn : buttons) {
            btn.setPreferredSize(new Dimension(CAPTION_BUTTON_WIDTH, CAPTION_BUTTON_HEIGHT));
            captionButtons.add(btn);
        }
        updateCaptionButtonsWidth();

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(captionButtons, BorderLayout.EAST);
    }

    private void configureBody() {
        body.setOpaque(true);
        body.setBackground(NoxTheme.BG_PRIMARY);
    }

    public void updateCaptionButtonsWidth() {
        int visibleCount = 0;
        for (Component c : captionButtons.getComponents()) {
            if (c.isVisible()) visibleCount++;
        }
        int totalWidth = visibleCount * CAPTION_BUTTON_WIDTH;
        captionButtons.setPreferredSize(new Dimension(totalWidth, TITLE_BAR_HEIGHT));
        captionButtons.revalidate();
        captionButtons.repaint();

        if (OsUtils.isWindows() && nativeInstalled) {
            nativeLib.configureWindow(window, TITLE_BAR_HEIGHT, totalWidth, isWindowResizable());
        }
    }

    private void installFallbackDrag(Runnable onDoubleClick) {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e) || OsUtils.isWindows()) return;
                fallbackDragStartScreen = e.getLocationOnScreen();
                fallbackWindowStart = window.getLocation();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (OsUtils.isWindows() || fallbackDragStartScreen == null || fallbackWindowStart == null) return;
                Point screen = e.getLocationOnScreen();
                int dx = screen.x - fallbackDragStartScreen.x;
                int dy = screen.y - fallbackDragStartScreen.y;
                window.setLocation(fallbackWindowStart.x + dx, fallbackWindowStart.y + dy);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                fallbackDragStartScreen = null;
                fallbackWindowStart = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && !OsUtils.isWindows()) {
                    if (onDoubleClick != null) onDoubleClick.run();
                }
            }
        };

        titleBar.addMouseListener(adapter);
        titleBar.addMouseMotionListener(adapter);
        titleLabel.addMouseListener(adapter);
        titleLabel.addMouseMotionListener(adapter);
    }

    public void onAddNotify() {
        if (OsUtils.isWindows() && !nativeInstalled) {
            installNativeWindow();
        }
    }

    public void onRemoveNotify() {
        if (OsUtils.isWindows() && nativeInstalled) {
            nativeLib.unhookWindow(window);
            nativeInstalled = false;
        }
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    private void installNativeWindow() {
        int captionWidth = captionButtons.getPreferredSize().width;
        nativeLib.hookWindow(window);
        nativeLib.configureWindow(window, TITLE_BAR_HEIGHT, captionWidth, isWindowResizable());
        nativeLib.setBackgroundColor(window, NoxTheme.BG_PRIMARY.getRed(), NoxTheme.BG_PRIMARY.getGreen(), NoxTheme.BG_PRIMARY.getBlue());
        nativeLib.setBorderColor(window, NoxTheme.ACCENT_PRIMARY.getRed(), NoxTheme.ACCENT_PRIMARY.getGreen(), NoxTheme.ACCENT_PRIMARY.getBlue());
        nativeInstalled = true;
    }

    private boolean isWindowResizable() {
        if (window instanceof Frame) return ((Frame) window).isResizable();
        if (window instanceof Dialog) return ((Dialog) window).isResizable();
        return false;
    }
}