package io.github.nazuha26.jnox;

import io.github.nazuha26.jnox.theme.NoxTheme;

import javax.swing.*;

public final class Nox {
    private static boolean installed;

    private Nox() { }

    public static void install() {
        if (installed) {
            return;
        }

        UIManager.put("Panel.background", NoxTheme.BG_PRIMARY);

        UIManager.put("Label.font", NoxTheme.FONT_PLAIN);
        UIManager.put("Label.foreground", NoxTheme.TEXT_PRIMARY);

        UIManager.put("Button.font", NoxTheme.FONT_BOLD);
        UIManager.put("Button.foreground", NoxTheme.TEXT_PRIMARY);

        UIManager.put("TextArea.font", NoxTheme.FONT_PLAIN);
        UIManager.put("TextArea.foreground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("TextArea.background", NoxTheme.BG_PRIMARY);
        UIManager.put("TextArea.caretForeground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("TextArea.selectionBackground", NoxTheme.ACCENT_PRIMARY);
        UIManager.put("TextArea.selectionForeground", NoxTheme.TEXT_PRIMARY);

        UIManager.put("TextField.font", NoxTheme.FONT_PLAIN);
        UIManager.put("TextField.foreground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("TextField.background", NoxTheme.BG_SURFACE);
        UIManager.put("TextField.caretForeground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("TextField.selectionBackground", NoxTheme.ACCENT_PRIMARY);
        UIManager.put("TextField.selectionForeground", NoxTheme.TEXT_PRIMARY);

        UIManager.put("PasswordField.font", NoxTheme.FONT_PLAIN);
        UIManager.put("PasswordField.foreground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("PasswordField.background", NoxTheme.BG_SURFACE);
        UIManager.put("PasswordField.caretForeground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("PasswordField.selectionBackground", NoxTheme.ACCENT_PRIMARY);
        UIManager.put("PasswordField.selectionForeground", NoxTheme.TEXT_PRIMARY);

        UIManager.put("TitledBorder.font", NoxTheme.FONT_BOLD.deriveFont(15f));
        UIManager.put("TitledBorder.titleColor", NoxTheme.TEXT_PRIMARY);

        UIManager.put("TextPane.font", NoxTheme.FONT_PLAIN);
        UIManager.put("TextPane.foreground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("TextPane.background", NoxTheme.BG_PRIMARY);
        UIManager.put("TextPane.caretForeground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("TextPane.selectionBackground", NoxTheme.ACCENT_PRIMARY);
        UIManager.put("TextPane.selectionForeground", NoxTheme.TEXT_PRIMARY);

        installed = true;
    }
}