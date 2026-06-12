package io.github.nazuha26.components;

import io.github.nazuha26.NoxTheme;

import javax.swing.*;
import java.awt.*;

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

    public static NoxFrameBuilder frame() {
        install();
        return new NoxFrameBuilder();
    }

    public static NoxDialogBuilder dialog() {
        install();
        return new NoxDialogBuilder();
    }

    public static NoxDialogBuilder dialog(Frame owner) {
        install();
        return new NoxDialogBuilder().owner(owner);
    }

    public static NoxDialogBuilder dialog(Frame owner, String title) {
        install();
        return new NoxDialogBuilder()
                .owner(owner)
                .title(title);
    }

    public static NoxButtonBuilder button() {
        install();
        return new NoxButtonBuilder();
    }

    public static NoxButtonBuilder button(String text) {
        install();
        return new NoxButtonBuilder().text(text);
    }

    public static NoxScrollPaneBuilder scrollPane() {
        install();
        return new NoxScrollPaneBuilder();
    }

    public static NoxTextFieldBuilder textField() {
        install();
        return new NoxTextFieldBuilder();
    }

    public static NoxTextFieldBuilder textField(String text) {
        install();
        return new NoxTextFieldBuilder().text(text);
    }

    public static NoxPasswordFieldBuilder passwordField() {
        install();
        return new NoxPasswordFieldBuilder();
    }

    public static NoxPasswordFieldBuilder passwordField(String text) {
        install();
        return new NoxPasswordFieldBuilder().text(text);
    }

    public static NoxLogPaneBuilder logPane() {
        install();
        return new NoxLogPaneBuilder();
    }
}