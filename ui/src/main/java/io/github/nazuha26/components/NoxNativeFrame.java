package io.github.nazuha26.components;

import io.github.nazuha26.OsUtils;
import io.github.nazuha26.NoxTheme;
import io.github.nazuha26.WinNativeLib;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

@Slf4j
public class NoxNativeFrame extends JFrame {

    private static final int TITLE_BAR_HEIGHT = 32;
    private static final int TITLE_BAR_LEFT_INSET = 12;

    private static final int CAPTION_BUTTON_WIDTH = 32;
    private static final int CAPTION_BUTTON_HEIGHT = 32;

    @Getter private final JPanel titleBar = new JPanel(new BorderLayout());
    @Getter private final JPanel body = new JPanel();
    @Getter private final JLabel titleLabel = new JLabel();

    private final JPanel captionButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    @Getter private final CaptionButton minimizeButton = new CaptionButton(CaptionButtonType.MINIMIZE);
    @Getter private final CaptionButton maximizeButton = new CaptionButton(CaptionButtonType.MAXIMIZE);
    @Getter private final CaptionButton closeButton = new CaptionButton(CaptionButtonType.CLOSE);

    private final WinNativeLib nativeLib = new WinNativeLib();

    private boolean nativeInstalled;

    private Point fallbackDragStartScreen;
    private Point fallbackWindowStart;

    public NoxNativeFrame(String title) {
        super(title);

        if (OsUtils.isWindows()) {
            setUndecorated(true);
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(NoxTheme.BG_PRIMARY);

        configureTitleBar(title);
        configureBody();
        configureButtons();
        installFallbackDrag();

        getContentPane().add(titleBar, BorderLayout.NORTH);
        getContentPane().add(body, BorderLayout.CENTER);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        if (OsUtils.isWindows() && !nativeInstalled) {
            installNativeWindow();
        }
    }

    @Override
    public void removeNotify() {
        if (OsUtils.isWindows() && nativeInstalled) {
            nativeLib.unhookWindow(this);
            nativeInstalled = false;
        }
        super.removeNotify();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        if (titleLabel != null) {
            titleLabel.setText(title);
        }
    }

    @Override
    public void setResizable(boolean resizable) {
        super.setResizable(resizable);

        if (maximizeButton != null) {
            maximizeButton.setVisible(resizable);
        }

        if (captionButtons != null) {
            captionButtons.setPreferredSize(new Dimension(getCaptionButtonsWidth(), TITLE_BAR_HEIGHT));
            captionButtons.revalidate();
            captionButtons.repaint();
        }

        if (OsUtils.isWindows() && nativeInstalled) {
            nativeLib.configureWindow(this, TITLE_BAR_HEIGHT, getCaptionButtonsWidth(), resizable);
        }
    }

    private void configureTitleBar(String title) {
        titleBar.setOpaque(true);
        titleBar.setBackground(NoxTheme.BG_PRIMARY);
        titleBar.setPreferredSize(new Dimension(0, TITLE_BAR_HEIGHT));
        titleBar.setBorder(BorderFactory.createEmptyBorder(0, TITLE_BAR_LEFT_INSET, 0, 0));

        titleLabel.setText(title);
        titleLabel.setFont(NoxTheme.FONT_BOLD);
        titleLabel.setForeground(NoxTheme.TEXT_PRIMARY);

        captionButtons.setOpaque(false);
        captionButtons.setPreferredSize(new Dimension(getCaptionButtonsWidth(), TITLE_BAR_HEIGHT));

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(captionButtons, BorderLayout.EAST);
    }

    private void configureBody() {
        body.setOpaque(true);
        body.setBackground(NoxTheme.BG_PRIMARY);
    }

    private void configureButtons() {
        Consumer<CaptionButton> configureCaptionButton = (button) -> {
            button.setPreferredSize(new Dimension(CAPTION_BUTTON_WIDTH, CAPTION_BUTTON_HEIGHT));
        };

        configureCaptionButton.accept(minimizeButton);
        minimizeButton.addActionListener(e -> {
            if (nativeInstalled) {
                nativeLib.minimizeWindow(this);
            } else {
                setState(Frame.ICONIFIED);
            }
        });

        configureCaptionButton.accept(maximizeButton);
        maximizeButton.addActionListener(e -> toggleMaximizeRestore());

        configureCaptionButton.accept(closeButton);
        closeButton.addActionListener(e -> {
            if (nativeInstalled) {
                nativeLib.closeWindow(this);
            } else {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });

        captionButtons.add(minimizeButton);
        captionButtons.add(maximizeButton);
        captionButtons.add(closeButton);
    }

    private void installFallbackDrag() {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!SwingUtilities.isLeftMouseButton(e) || OsUtils.isWindows()) return;
                fallbackDragStartScreen = e.getLocationOnScreen();
                fallbackWindowStart = getLocation();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (OsUtils.isWindows() || fallbackDragStartScreen == null || fallbackWindowStart == null) return;
                Point screen = e.getLocationOnScreen();
                int dx = screen.x - fallbackDragStartScreen.x;
                int dy = screen.y - fallbackDragStartScreen.y;
                setLocation(fallbackWindowStart.x + dx, fallbackWindowStart.y + dy);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                fallbackDragStartScreen = null;
                fallbackWindowStart = null;
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e) && !OsUtils.isWindows()) {
                    toggleMaximizeRestore();
                }
            }
        };

        titleBar.addMouseListener(adapter);
        titleBar.addMouseMotionListener(adapter);
        titleLabel.addMouseListener(adapter);
        titleLabel.addMouseMotionListener(adapter);
    }

    public void installNativeWindow() {
        nativeLib.hookWindow(this);
        nativeLib.configureWindow(this, TITLE_BAR_HEIGHT, getCaptionButtonsWidth(), isResizable());
        nativeLib.setBackgroundColor(this, NoxTheme.BG_PRIMARY.getRed(), NoxTheme.BG_PRIMARY.getGreen(), NoxTheme.BG_PRIMARY.getBlue());
        nativeLib.setBorderColor(this, NoxTheme.ACCENT_PRIMARY.getRed(), NoxTheme.ACCENT_PRIMARY.getGreen(), NoxTheme.ACCENT_PRIMARY.getBlue());
        nativeInstalled = true;
    }

    private boolean isMaximized() {
        return (getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    }

    private void toggleMaximizeRestore() {
        if (nativeInstalled) {
            if (isMaximized()) {
                nativeLib.restoreWindow(this);
            } else {
                nativeLib.maximizeWindow(this);
            }
        } else {
            setExtendedState(isMaximized() ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
        }
    }


    private int getCaptionButtonsWidth() {
        int visibleCount = maximizeButton.isVisible() ? 3 : 2;
        return visibleCount * CAPTION_BUTTON_WIDTH;
    }
}