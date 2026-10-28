package com.meakaandre.quicksilver.probe;

/**
 * What the machine and driver offer, read once at start-up. Later modules pick a fast path or its fallback from this
 * and nothing else, so every choice is made from a detected capability and shows up in the log.
 *
 * @param gpuVendor            the driver's vendor string
 * @param renderer             the device name the driver reports
 * @param driverVersion        the driver part of the version string (after the GL version on OpenGL)
 * @param glVersion            the context's OpenGL version ("4.5"), or "n/a" on another backend
 * @param backend              26.3's backend name ("OpenGL", "Vulkan")
 * @param os                   operating system name, version and architecture
 * @param windowSystem         Windows, X11, Wayland, macOS, or the raw SDL video driver name
 * @param cpuCores             processors available to the JVM
 * @param glMultiDrawIndirect  GL_ARB_multi_draw_indirect is in the driver's extension list (false off OpenGL)
 * @param glBufferStorage      GL_ARB_buffer_storage is in the driver's extension list (false off OpenGL)
 * @param deviceType           26.3's guess at the device type (INTEGRATED, DISCRETE, CPU, ...)
 * @param vanillaMultiDrawIndirect 26.3 itself enabled multi-draw indirect on this device
 * @param vanillaPersistentMapping 26.3 itself enabled persistent buffer mapping on this device
 * @param vanillaMdiKnownIssues    26.3's own workaround flag: multi-draw indirect has known issues here (Intel
 *                                 integrated); later modules must then use the plain-draw path
 */
public record Capabilities(
    String gpuVendor,
    String renderer,
    String driverVersion,
    String glVersion,
    String backend,
    String os,
    String windowSystem,
    int cpuCores,
    boolean glMultiDrawIndirect,
    boolean glBufferStorage,
    String deviceType,
    boolean vanillaMultiDrawIndirect,
    boolean vanillaPersistentMapping,
    boolean vanillaMdiKnownIssues
) {
    public boolean isOpenGl() {
        return "OpenGL".equalsIgnoreCase(backend);
    }

    /** The single log line, also stored in every bench report. */
    public String describe() {
        String mdi = isOpenGl() ? yesNo(glMultiDrawIndirect) : "n/a";
        String bs = isOpenGl() ? yesNo(glBufferStorage) : "n/a";
        return "gpuVendor=\"" + gpuVendor + "\" renderer=\"" + renderer + "\" driver=\"" + driverVersion + "\" gl=" + glVersion
            + " backend=" + backend + " os=\"" + os + "\" windowSystem=" + windowSystem + " cpuCores=" + cpuCores
            + " GL_ARB_multi_draw_indirect=" + mdi + " GL_ARB_buffer_storage=" + bs
            + " deviceType=" + deviceType + " vanilla.multiDrawIndirect=" + yesNo(vanillaMultiDrawIndirect)
            + " vanilla.persistentMapping=" + yesNo(vanillaPersistentMapping)
            + " vanilla.mdiKnownIssues=" + yesNo(vanillaMdiKnownIssues);
    }

    private static String yesNo(boolean b) {
        return b ? "yes" : "no";
    }
}
