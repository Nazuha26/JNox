package io.github.nazuha26.jnox.platform.windows;

import io.github.nazuha26.jnox.platform.OsUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class WinNativeLib {
    private static final String RESOURCE_PATH = "native/windows/x64/WinNativeLib.dll";

    static {
        if (OsUtils.isWindows()) {
            try {
                Path dllPath = extractNativeLibrary();
                System.load(dllPath.toAbsolutePath().toString());
            } catch (Exception e) {
                throw new UnsatisfiedLinkError("Failed to load WinNativeLib.dll: " + e.getMessage());
            }
        }
    }

    private static Path extractNativeLibrary() throws IOException {
        Path dir = Path.of(
                System.getProperty("user.home"),
                ".jnox",
                "native"
        );

        Files.createDirectories(dir);

        Path dll = dir.resolve("WinNativeLib.dll");

        try (InputStream input = WinNativeLib.class
                .getClassLoader()
                .getResourceAsStream(RESOURCE_PATH)) {

            if (input == null) {
                throw new IOException("Native library not found in resources: " + RESOURCE_PATH);
            }

            Files.copy(input, dll, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        return dll;
    }

    public native void hookWindow(Component window);
    public native void unhookWindow(Component window);
    public native void configureWindow(Component window, int titleBarHeight, int captionButtonsWidth, boolean isResizeable, boolean isMaximizable);
    public native void setBackgroundColor(Component component, int r, int g, int b);
    public native void setBorderColor(Component component, int r, int g, int b);
    public native void setMinSize(Component window, int minWidth, int minHeight);
    public native double[] getDPIScale(Component window);
    public native void minimizeWindow(Component window);
    public native void maximizeWindow(Component window);
    public native void restoreWindow(Component window);
    public native void closeWindow(Component window);
}