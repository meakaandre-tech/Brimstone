package com.meakaandre.quicksilver.probe;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.device.DeviceInfo;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.sdl.SDLVideo;

/**
 * Reads the device once, on the render thread after the game has created it. Read-only queries: nothing is changed.
 */
public final class CapabilityProbe {
    private static final Pattern GL_VERSION = Pattern.compile("^\\s*(\\d+\\.\\d+)(?:\\.\\d+)?\\s*(.*)$");

    private CapabilityProbe() {
    }

    public static Capabilities run() {
        DeviceInfo info = RenderSystem.getDevice().getDeviceInfo();
        String backend = info.backendName();
        boolean gl = "OpenGL".equalsIgnoreCase(backend);

        String glVersion = "n/a";
        String driver = info.driverInfo();
        Set<String> extensions = Set.of();
        if (gl) {
            String version = GL11.glGetString(GL11.GL_VERSION);
            if (version != null) {
                Matcher m = GL_VERSION.matcher(version);
                if (m.matches()) {
                    glVersion = m.group(1);
                    driver = cleanDriver(m.group(2));
                } else {
                    driver = version;
                }
            }
            extensions = glExtensions();
        }

        return new Capabilities(
            info.vendorName(),
            info.name(),
            driver,
            glVersion,
            backend,
            System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"),
            windowSystem(),
            Runtime.getRuntime().availableProcessors(),
            extensions.contains("GL_ARB_multi_draw_indirect"),
            extensions.contains("GL_ARB_buffer_storage"),
            String.valueOf(info.type()),
            info.features().multiDrawIndirect(),
            info.features().persistentMapping(),
            info.hintsAndWorkarounds().multiDrawIndirectHasKnownIssues()
        );
    }

    /** Every extension the current context lists (core-profile query, one name at a time). */
    private static Set<String> glExtensions() {
        Set<String> out = new HashSet<>();
        int n = GL11.glGetInteger(GL30.GL_NUM_EXTENSIONS);
        for (int i = 0; i < n; i++) {
            String e = GL30.glGetStringi(GL11.GL_EXTENSIONS, i);
            if (e != null) out.add(e);
        }
        return out;
    }

    /** "(Core Profile) Mesa 25.2.8", "- Build 31.0.101.2125", "Core Profile Context 24.10.1" -> the driver part. */
    private static String cleanDriver(String rest) {
        String s = rest.replace("(Core Profile)", "").replace("(Compatibility Profile)", "")
            .replace("Core Profile Context", "").replace("Compatibility Profile Context", "").trim();
        if (s.startsWith("-")) s = s.substring(1).trim();
        return s.isEmpty() ? rest.trim() : s;
    }

    /** SDL's own answer for the video driver it opened the window with; read-only. */
    private static String windowSystem() {
        String driver;
        try {
            driver = SDLVideo.SDL_GetCurrentVideoDriver();
        } catch (Throwable t) {
            driver = null;
        }
        if (driver == null) return "unknown";
        return switch (driver.toLowerCase(Locale.ROOT)) {
            case "windows" -> "Windows";
            case "x11" -> "X11";
            case "wayland" -> "Wayland";
            case "cocoa" -> "macOS";
            default -> driver;
        };
    }
}
