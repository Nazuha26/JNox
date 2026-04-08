package io.github.nazuha26.components;

import lombok.Getter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class NoxNativeDialog extends JDialog {

    private final NoxWindowDelegate delegate;
    @Getter private final CaptionButton closeButton = new CaptionButton(CaptionButton.CaptionButtonType.CLOSE);

    public NoxNativeDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        this.delegate = new NoxWindowDelegate(this);

        closeButton.addActionListener(e -> {
            if (delegate.isNativeInstalled()) delegate.getNativeLib().closeWindow(this);
            else dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        delegate.install(title, null, closeButton);
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
        if (delegate != null) delegate.updateCaptionButtonsWidth();
    }

    public JPanel getTitleBar() { return delegate.getTitleBar(); }
    public JPanel getBody() { return delegate.getBody(); }
    public JLabel getTitleLabel() { return delegate.getTitleLabel(); }
}