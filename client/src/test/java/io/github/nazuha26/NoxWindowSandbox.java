package io.github.nazuha26;

import io.github.nazuha26.components.NoxButton;
import io.github.nazuha26.components.NoxNativeDialog;
import io.github.nazuha26.components.NoxNativeFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class NoxWindowSandbox {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NoxNativeFrame window_1 = createDefaultWindow("Nox Frame");
            window_1.setVisible(true);
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

        JLabel title = new JLabel("JLabel Title");
        title.setFont(NoxTheme.FONT_BOLD);
        title.setForeground(NoxTheme.TEXT_PRIMARY);

        JLabel subtitle = new JLabel("Nam bibendum nisl sit amet lorem tristique lobortis. Praesent venenatis maximus sollicitudin. Mauris interdum nulla erat, eu blandit turpis semper nec.");
        subtitle.setFont(NoxTheme.FONT_PLAIN);
        subtitle.setForeground(NoxTheme.TEXT_PRIMARY);

        NoxButton defButton = new NoxButton("Default Nox Button");
        defButton.setPreferredSize(new Dimension(180, 40));
        frame.getRootPane().setDefaultButton(defButton);

        NoxButton dialogButton1 = new NoxButton("Show Nox Dialog 1");
        dialogButton1.setPreferredSize(new Dimension(180, 40));

        NoxButton dialogButton2 = new NoxButton("Show Nox Dialog 2");
        dialogButton2.setPreferredSize(new Dimension(180, 40));

        NoxNativeDialog dialog1 = createDefaultDialog(frame);
        dialog1.setResizable(true);
        dialog1.setModal(true);

        NoxNativeDialog dialog2 = createDefaultDialog(frame);
        dialog2.setResizable(false);
        dialog2.setModal(false);

        defButton.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Title", "Default JOptionPane", JOptionPane.ERROR_MESSAGE));
        dialogButton1.addActionListener(e -> dialog1.setVisible(true));
        dialogButton2.addActionListener(e -> dialog2.setVisible(true));



        gbc.gridy = 0;
        body.add(title, gbc);

        gbc.gridy = 1;
        body.add(subtitle, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(18, 0, 10, 0);
        body.add(defButton, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(dialogButton1, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        body.add(dialogButton2, gbc);

        return frame;
    }

    private static NoxNativeDialog createDefaultDialog(NoxNativeFrame owner) {
        NoxNativeDialog dialog = new NoxNativeDialog(owner, "Nox Dialog", false);
        dialog.setSize(340, 180);
        dialog.setLocationRelativeTo(owner);

        JPanel body = dialog.getBody();
        body.setLayout(new BorderLayout());
        body.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                        "Maecenas pretium sem nec mollis condimentum." +
                        "Morbi risus lacus, aliquet vel nunc in, scelerisque pretium risus",
                SwingConstants.CENTER);

        messageLabel.setFont(NoxTheme.FONT_PLAIN);
        messageLabel.setForeground(NoxTheme.TEXT_PRIMARY);
        body.add(messageLabel, BorderLayout.CENTER);

        NoxButton okButton = new NoxButton("OK");
        okButton.setPreferredSize(new Dimension(100, 32));
        okButton.addActionListener(e -> dialog.dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        buttonPanel.add(okButton);

        body.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }
}
