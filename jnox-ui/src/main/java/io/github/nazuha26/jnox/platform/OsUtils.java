package io.github.nazuha26.jnox.platform;

public final class OsUtils {

    private OsUtils() {}

    public static boolean isWindows() {
        return System.getProperty("os.name", "").toLowerCase().contains("win");
    }
}