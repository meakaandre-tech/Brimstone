package com.meakaandre.quicksilver.cipinstate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CI only (package 8): the two areas of the core suite whose effects the pins make the same in every run, vanilla and
 * Quicksilver alike, and what the pins do there, worked out once per client tick from where the player is.
 * <ul>
 * <li>The particle scene's area and the animation scene's area: particles made there take their random numbers from
 * where (and what) they are, are not ticked while the player is in the same area (and are drawn at partial tick 0), and
 * are removed once the player is elsewhere.</li>
 * <li>The animation scene's area and the animated items scene's area (package 8 follow-up): on entering (and on leaving) either, every animated texture goes back to its first frame;
 * inside it, animation runs for exactly {@link #ANIMATION_TICKS} texture ticks and is then held again, so vanilla and
 * Quicksilver reach the same frame; while the marker block under the platform is a redstone block (set by the scene's
 * bench step) it runs freely, for measuring.</li>
 * </ul>
 */
public final class Pins {
    private static final Logger LOG = LoggerFactory.getLogger("quicksilver_ci_pin");
    public static final int NONE = 0, PARTICLES = 1, ANIMATION = 2, ITEMS = 3;
    public static final int ANIMATION_TICKS = 70;
    private static final BlockPos MARKER = new BlockPos(2630, 194, -2000);
    private static final BlockPos ITEMS_MARKER = new BlockPos(2730, 194, -2000);

    /** Which area the player is in (client thread). */
    public static volatile int playerArea = NONE;
    /** Bumped whenever every animated texture must go back to its first frame. */
    public static volatile int animationResets;
    /** Whether animated textures advance on this texture tick. */
    public static volatile boolean animationRuns;
    private static int ticksRun, clientTicksWhileRunning;
    private static int textureArea = NONE;
    private static boolean wasFree;

    private Pins() {
    }

    /** The pin area holding a point of the overworld, or NONE. */
    public static int area(Level level, double x, double y, double z) {
        if (level == null || level.dimension() != Level.OVERWORLD || y < 190 || y > 240 || z < -2040 || z > -1940) return NONE;
        if (x >= 2800 && x <= 2880) return PARTICLES;
        if (x >= 2580 && x <= 2680) return ANIMATION;
        if (x >= 2700 && x <= 2780) return ITEMS;
        return NONE;
    }

    /** Once per client tick, before anything ticks: the player's area, for the particle pins. */
    public static void clientTick(Minecraft mc) {
        LocalPlayer p = mc.player;
        int now = p == null ? NONE : area(mc.level, p.getX(), p.getY(), p.getZ());
        int before = playerArea;
        if (now != before) {
            playerArea = now;
            LOG.info("Quicksilver CI pin: player now in pin area {} (was {})", name(now), name(before));
        }
        if (animationRuns) clientTicksWhileRunning++;
    }

    /**
     * At the start of each texture tick, before any animated texture ticks: whether animation advances on it. The game
     * ticks its textures once per frame that runs at least one client tick, not once per client tick (a slow frame that
     * catches up two client ticks ticks the textures once), so the fixed run is counted in texture ticks: counting client
     * ticks let a slow run reach one interpolation step less (main's run 40487515684, measure runs only).
     */
    public static void textureTick(Minecraft mc) {
        LocalPlayer p = mc.player;
        ClientLevel level = mc.level;
        int now = p == null ? NONE : area(level, p.getX(), p.getY(), p.getZ());
        int before = textureArea;
        if (now != before) {
            textureArea = now;
            if (animated(now) || animated(before)) {
                animationResets++;
                ticksRun = 0;
                clientTicksWhileRunning = 0;
                LOG.info("Quicksilver CI pin: every animated texture back to its first frame{}", animated(now)
                    ? ", then " + ANIMATION_TICKS + " texture ticks of animation" : ", held");
            }
        }
        boolean free = animated(now) && level != null && level.getBlockState(now == ITEMS ? ITEMS_MARKER : MARKER).is(Blocks.REDSTONE_BLOCK);
        if (free != wasFree) {
            wasFree = free;
            LOG.info("Quicksilver CI pin: animation {}", free ? "runs freely (marker set, measuring)" : "pinned again");
        }
        if (animated(now) && (free || ticksRun < ANIMATION_TICKS)) {
            animationRuns = true;
            if (!free && ++ticksRun == ANIMATION_TICKS) {
                LOG.info("Quicksilver CI pin: {} texture ticks of animation done ({} client ticks), held from the next one",
                    ANIMATION_TICKS, clientTicksWhileRunning);
            }
        } else {
            animationRuns = false;
        }
    }

    /** Areas where animation runs its fixed number of ticks from frame 0 (the animation and animated items scenes). */
    private static boolean animated(int a) {
        return a == ANIMATION || a == ITEMS;
    }

    private static String name(int a) {
        return a == PARTICLES ? "particles" : a == ANIMATION ? "animation" : a == ITEMS ? "animated items" : "none";
    }

    /** A seed from where (and what) something is, the same in every run. */
    public static long seed(String what, double x, double y, double z, double dx, double dy, double dz) {
        long h = what.hashCode();
        for (double d : new double[] {x, y, z, dx, dy, dz}) h = h * 0x9E3779B97F4A7C15L + Double.doubleToLongBits(d);
        return h ^ (h >>> 31);
    }
}
