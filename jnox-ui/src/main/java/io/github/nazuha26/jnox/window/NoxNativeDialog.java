package io.github.nazuha26.jnox.window;

import io.github.nazuha26.jnox.Nox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class NoxNativeDialog extends JDialog {

    private final NoxWindowDelegate delegate;

    public NoxNativeDialog(Frame owner) {
        this(owner, "", false);
    }

    public NoxNativeDialog(Frame owner, String title) {
        this(owner, title, false);
    }

    public NoxNativeDialog(Frame owner, String title, boolean modal) {
        super(owner, title == null ? "" : title, modal);

        Nox.install();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(256, NoxWindowDelegate.TITLE_BAR_HEIGHT));

        this.delegate = new NoxWindowDelegate(this);

        CaptionButton closeButton = new CaptionButton(CaptionButton.CaptionButtonType.CLOSE);
        closeButton.addActionListener(e -> {
            if (delegate.isNativeInstalled()) {
                delegate.getNativeLib().closeWindow(this);
            } else {
                dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }
        });

        delegate.install(getTitle(), null, closeButton);
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