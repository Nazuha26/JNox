package io.github.nazuha26;

import io.github.nazuha26.components.NoxButton;
import io.github.nazuha26.components.NoxNativeFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NoxNativeFrame firstFrame = createDefaultWindow("Resizable");
            NoxNativeFrame secondFrame = createDefaultWindow("Not Resizable");

            firstFrame.setResizable(true);
            secondFrame.setResizable(false);

            firstFrame.setVisible(true);
            secondFrame.setVisible(true);
        });
    }

    private static NoxNativeFrame createDefaultWindow(String windowTitle) {
        NoxNativeFrame frame = new NoxNativeFrame(windowTitle);
        frame.setSize(520, 360);
        frame.setMinimumSize(new Dimension(420, 280));
        frame.setLocationRelativeTo(null);

        JPanel body = frame.getBody();
        body.setLayout(new GridBagLayout());
        body.setBorder(new EmptyBorder(24, 24, 24, 24));
        body.setBackground(NoxTheme.BG_PRIMARY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 12, 0);
        gbc.weightx = 1.0;

        JLabel title = new JLabel("Sign in");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(NoxTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Decorated JFrame with a custom Win32 title bar, native drag, snap and resize.");
        subtitle.setFont(NoxTheme.FONT_PLAIN);
        subtitle.setForeground(NoxTheme.TEXT_PRIMARY);

        NoxButton loginButton = new NoxButton("Log In");
        loginButton.setPreferredSize(new Dimension(180, 40));

        NoxButton secondaryButton = new NoxButton("Show Dialog");
        secondaryButton.setPreferredSize(new Dimension(180, 40));

        frame.getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Кнопка из модуля UI"));
        secondaryButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Нативный каркас установлен безопасно"));

        gbc.gridy = 0;
        body.add(title, gbc);

        gbc.gridy = 1;
        body.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(18, 0, 10, 0);
        body.add(loginButton, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(secondaryButton, gbc);

        return frame;
    }
}
