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
        UIManager.put("Label.foreground", NoxTheme.TEXT_PRIMARY);
        UIManager.put("Label.font", NoxTheme.FONT_PLAIN);
        UIManager.put("Button.font", NoxTheme.FONT_BOLD);
        UIManager.put("TextArea.font", NoxTheme.FONT_PLAIN);
        UIManager.put("TextField.font", NoxTheme.FONT_PLAIN);

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
}