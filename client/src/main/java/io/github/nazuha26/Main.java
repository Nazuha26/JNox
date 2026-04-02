package io.github.nazuha26;

import io.github.nazuha26.components.NoxButton;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Nox Client");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);

            // BG
            frame.getContentPane().setBackground(NoxTheme.BG);
            frame.setLayout(new GridBagLayout()); // Чтобы выровнять кнопку по центру

            // Создаем нашу кастомную кнопку из модуля ui!
            NoxButton loginButton = new NoxButton("Log In");
            frame.getRootPane().setDefaultButton(loginButton);
            loginButton.setPreferredSize(new Dimension(150, 40));

            NoxButton logoutButton = new NoxButton("Log Out");
            logoutButton.setPreferredSize(new Dimension(120, 40));

            // Добавляем действие
            loginButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(frame, "Кнопка из модуля UI работает!");
            });

            frame.add(loginButton);
            frame.add(logoutButton);
            frame.setLocationRelativeTo(null); // По центру экрана
            frame.setVisible(true);
        });
    }
}