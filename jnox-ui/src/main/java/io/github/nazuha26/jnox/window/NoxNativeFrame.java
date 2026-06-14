package io.github.nazuha26.jnox.window;

import io.github.nazuha26.jnox.Nox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

@Slf4j
public class NoxNativeFrame extends JFrame {

    private final NoxWindowDelegate delegate;

    private final CaptionButton minimizeButton = new CaptionButton(CaptionButton.CaptionButtonType.MINIMIZE);
    private final CaptionButton maximizeButton = new CaptionButton(CaptionButton.CaptionButtonType.MAXIMIZE);
    private final CaptionButton closeButton = new CaptionButton(CaptionButton.CaptionButtonType.CLOSE);

    @Getter private boolean maximizable = true;
    @Getter private boolean minimizable = true;
    @Getter private boolean closable = true;

    public NoxNativeFrame() {
        this("");
    }

    public NoxNativeFrame(String title) {
        super(title == null ? "" : title);

        Nox.install();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(true);
        setMinimumSize(new Dimension(256, NoxWindowDelegate.TITLE_BAR_HEIGHT));

        this.delegate = new NoxWindowDelegate(this);

        minimizeButton.addActionListener(e -> {
            if (!minimizable) {
                return;
            }

            if (delegate.isNativeInstalled()) {
                delegate.getNativeLib().minimizeWindow(this);
            } else {
                setState(Frame.ICONIFIED);
            }
        });

        maximizeButton.addActionListener(e -> {
            if (maximizable) {
                toggleMaximizeRestore();
            }
        });

        closeButton.addActionListener(e -> {
            if (!closable) {
                return;
            }

            if (delegate.isNativeInstalled()) {
                delegate.getNativeLib().closeWindow(this);
            } else {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });

        delegate.install(getTitle(), this::toggleMaximizeRestore, minimizeButton, maximizeButton, closeButton);

        setMinimizable(minimizable);
        setMaximizable(maximizable);
        setClosable(closable);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        delegate.onAddNotify();
    }

    @Override
    public void removeNotify() {
        delegate.onRemoveNotify();
        super.removeNotify();
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title == null ? "" : title);
        if (delegate != null) {
            delegate.setTitle(getTitle());
        }
    }

    @Override
    public void setMinimumSize(Dimension minimumSize) {
        super.setMinimumSize(minimumSize);
        if (delegate != null) {
            delegate.updateNativeMinimumSize();
        }
    }

    @Override
    public void setResizable(boolean resizable) {
        super.setResizable(resizable);
        if (delegate != null) {
            delegate.updateCaptionButtonsWidth();
        }
    }

    public void setMaximizable(boolean maximizable) {
        this.maximizable = maximizable;
        if (maximizeButton != null) {
            maximizeButton.setVisible(maximizable);
        }
        if (delegate != null) {
            delegate.updateCaptionButtonsWidth();
        }
    }

    public void setMinimizable(boolean minimizable) {
        this.minimizable = minimizable;
        if (minimizeButton != null) {
            minimizeButton.setVisible(minimizable);
        }
        if (delegate != null) {
            delegate.updateCaptionButtonsWidth();
        }
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
        if (closeButton != null) {
            closeButton.setVisible(closable);
        }
        if (delegate != null) {
            delegate.updateCaptionButtonsWidth();
        }
    }

    private boolean isMaximized() {
        return (getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    }

    private void toggleMaximizeRestore() {
        if (!maximizable) {
            return;
        }

        if (delegate.isNativeInstalled()) {
            if (isMaximized()) {
                delegate.getNativeLib().restoreWindow(this);
            } else {
                delegate.getNativeLib().maximizeWindow(this);
            }
        } else {
            setExtendedState(isMaximized() ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
        }
    }

    public JPanel getTitleBar() {
        return delegate.getTitleBar();
    }

    public JPanel getBody() {
        return delegate.getBody();
    }

    public JLabel getTitleLabel() {
        return delegate.getTitleLabel();
    }
}