package io.github.nazuha26.components;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

@Slf4j
public class NoxNativeFrame extends JFrame {

    private final NoxWindowDelegate delegate;

    @Getter private final CaptionButton minimizeButton = new CaptionButton(CaptionButtonType.MINIMIZE);
    @Getter private final CaptionButton maximizeButton = new CaptionButton(CaptionButtonType.MAXIMIZE);
    @Getter private final CaptionButton closeButton = new CaptionButton(CaptionButtonType.CLOSE);

    public NoxNativeFrame(String title) {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.delegate = new NoxWindowDelegate(this);

        minimizeButton.addActionListener(e -> {
            if (delegate.isNativeInstalled()) delegate.getNativeLib().minimizeWindow(this);
            else setState(Frame.ICONIFIED);
        });

        maximizeButton.addActionListener(e -> toggleMaximizeRestore());

        closeButton.addActionListener(e -> {
            if (delegate.isNativeInstalled()) delegate.getNativeLib().closeWindow(this);
            else dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        delegate.install(title, this::toggleMaximizeRestore, minimizeButton, maximizeButton, closeButton);
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
        super.setTitle(title);
        if (delegate != null) delegate.setTitle(title);
    }

    @Override
    public void setResizable(boolean resizable) {
        super.setResizable(resizable);
        if (maximizeButton != null) maximizeButton.setVisible(resizable);
        if (delegate != null) delegate.updateCaptionButtonsWidth();
    }

    private boolean isMaximized() {
        return (getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    }

    private void toggleMaximizeRestore() {
        if (delegate.isNativeInstalled()) {
            if (isMaximized()) delegate.getNativeLib().restoreWindow(this);
            else delegate.getNativeLib().maximizeWindow(this);
        } else {
            setExtendedState(isMaximized() ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
        }
    }

    public JPanel getTitleBar() { return delegate.getTitleBar(); }
    public JPanel getBody() { return delegate.getBody(); }
    public JLabel getTitleLabel() { return delegate.getTitleLabel(); }
}