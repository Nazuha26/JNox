package io.github.nazuha26;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

@Slf4j
public class WinNativeLib {

    static {
        try {
            File tempDll = File.createTempFile("WinNativeLib", ".dll");
            tempDll.deleteOnExit();

            try (InputStream is = WinNativeLib.class.getResourceAsStream("/WinNativeLib.dll");
                 FileOutputStream os = new FileOutputStream(tempDll)) {

                if (is == null) {
                    throw new FileNotFoundException("WinNativeLib.dll not found (src/main/resources/)");
                }

                byte[] buffer = new byte[8192];
                int readBytes;
                while ((readBytes = is.read(buffer)) != -1) {
                    os.write(buffer, 0, readBytes);
                }
            }

            System.load(tempDll.getAbsolutePath());
            log.info("WinNativeLib.dll successfully loaded.");

        } catch (Exception e) {
            log.warn("Failed to unpack WinNativeLib.dll", e);
            System.loadLibrary("WinNativeLib");     // Fallback
        }
    }

    public native void hookWindow(Component window);
    public native void unhookWindow(Component window);
    public native void configureWindow(Component window, int titleBarHeight, int captionButtonsWidth, boolean isResizeable);
    public native void setBackgroundColor(Component component, int r, int g, int b);
    public native void setBorderColor(Component component, int r, int g, int b);
    public native double[] getDPIScale(Component window);
    public native void minimizeWindow(Component window);
    public native void maximizeWindow(Component window);
    public native void restoreWindow(Component window);
    public native void closeWindow(Component window);
}