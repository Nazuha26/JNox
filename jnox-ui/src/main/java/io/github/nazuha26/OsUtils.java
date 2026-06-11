package io.github.nazuha26;

public final class OsUtils {

    private OsUtils() {}

    public static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }
}