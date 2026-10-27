package com.meakaandre.brimstone.terrain;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.device.GpuDevice;
import java.util.List;
import java.util.Locale;

/**
 * GPU errors, as the device's own debug output reports them (renderpearl's {@code GpuDevice.getLastDebugMessages},
 * which the game fills from OpenGL's debug output when its GL debug verbosity is on, the default). Brimstone takes a
 * mark before it draws or uploads and asks afterwards whether an error message arrived meanwhile.
 */
public final class TerrainGpuErrors {
    public record Mark(List<String> messages) {
    }

    private static final Mark NONE = new Mark(List.of());

    private TerrainGpuErrors() {
    }

    static String describe() {
        try {
            GpuDevice d = RenderSystem.getDevice();
            if (!d.isDebuggingEnabled()) return "none (the device's debug output is off)";
            if (!"OpenGL".equalsIgnoreCase(d.getDeviceInfo().backendName())) {
                return "none (this backend reports no debug messages to the game; its errors still reach the log)";
            }
            return "the device's debug output";
        } catch (Throwable t) {
            return "none";
        }
    }

    public static Mark mark() {
        GpuDevice d = RenderSystem.getDevice();
        if (!d.isDebuggingEnabled()) return NONE;
        return new Mark(List.copyOf(d.getLastDebugMessages()));
    }

    /** The first error message that arrived since the mark, or null. */
    public static String since(Mark mark) {
        GpuDevice d = RenderSystem.getDevice();
        if (!d.isDebuggingEnabled()) return null;
        List<String> now = d.getLastDebugMessages();
        if (now.equals(mark.messages())) return null;
        for (String m : now) {
            if (mark.messages().contains(m)) continue;
            String l = m.toLowerCase(Locale.ROOT);
            if (l.contains("type=error") || l.contains("validation error")) return m;
        }
        return null;
    }
}
