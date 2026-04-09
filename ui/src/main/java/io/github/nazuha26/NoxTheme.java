package io.github.nazuha26;

import java.awt.*;

public class NoxTheme {

    // ===== FONT =====
    public static final String FONT_NAME = "Segoe UI";
    public static final Font FONT_PLAIN = new Font(FONT_NAME, Font.PLAIN, 14);
    public static final Font FONT_PLAIN_SMALL = new Font(FONT_NAME, Font.PLAIN, 12);
    public static final Font FONT_PLAIN_TINY = new Font(FONT_NAME, Font.PLAIN, 8);

    public static final Font FONT_BOLD = new Font(FONT_NAME, Font.BOLD, 14);


    // ===== COLORS =====
    /*public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static final Color ACCENT_PRIMARY = new Color(84, 137, 245);

    public static final Color BG_PRIMARY = new Color(43, 45, 48);

    public static final Color BG_SURFACE = new Color(48, 49, 51);

    public static final Color TEXT_PRIMARY = new Color(221, 221, 221);*/


    // ===== COLORS =====

    // Special
    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static final Color ACCENT_PRIMARY = new Color(38, 123, 114);
    public static final Color ACCENT_SECONDARY = ACCENT_PRIMARY.darker();

    public static final Color BG_PRIMARY = new Color(21, 32, 34);
    public static final Color BG_SURFACE = new Color(22, 44, 48);
    public static final Color OUTLINE = new Color(60, 71, 63);

    public static final Color TEXT_PRIMARY = new Color(189, 209, 191);

    public static final Color SCROLL_TRACK = TRANSPARENT;
    public static final Color SCROLL_TRACK_HOVER = new Color(ACCENT_PRIMARY.getRed(), ACCENT_PRIMARY.getGreen(), ACCENT_PRIMARY.getBlue(), 40);
    public static final Color SCROLL_THUMB = new Color(ACCENT_SECONDARY.getRed(), ACCENT_SECONDARY.getGreen(), ACCENT_SECONDARY.getBlue(), 120);
    public static final Color SCROLL_THUMB_HOVER = new Color(ACCENT_PRIMARY.getRed(), ACCENT_PRIMARY.getGreen(), ACCENT_PRIMARY.getBlue(), 120);

    public static final Color ERROR = new Color(255, 68, 69);
    public static final Color WARNING = new Color(245, 179, 94);
    public static final Color SUCCESS = new Color(141, 217, 93);
}
