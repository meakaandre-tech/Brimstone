package com.meakaandre.brimstone.visibility;

import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.mixin.visibility.EntityRendererInvoker;
import com.meakaandre.brimstone.terrain.Terrain;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

/**
 * Brimstone's visibility module (package 2).
 *
 * <p>Sections: vanilla 26.3 already culls them with a frustum test over an octree and a cave graph walked from the
 * camera's section ({@code SectionOcclusionGraph}); Brimstone keeps exactly vanilla's visible list for its terrain
 * draws (a stricter list could only add pop-in), and checks in the bench that no section of that list goes undrawn.
 *
 * <p>Entities and block entities: vanilla draws every entity in the frustum whose section is meshed, and every block
 * entity of a visible section, even behind walls. Brimstone hides one only when its occlusion test proves that nothing
 * of it can be seen ({@link Occlusion}), and block entities also when their box is outside the same frustum vanilla
 * tests entities with. Only kinds Brimstone recognises are ever hidden: vanilla's own entity and block entity types
 * drawn by vanilla's own renderers, without name tags, outlines, leashes, beams or riders; everything else is left to
 * vanilla. The test is skipped for the rest of a frame once it has used {@link #BUDGET_NANOS} (what is left is drawn).
 *
 * <p>Crash safety: anything unexpected turns the module off for the session with one ERROR line, and vanilla decides
 * alone again.
 */
public final class Visibility {
    public enum State { STARTING, ON, OFF }

    /** Render-thread time the occlusion tests may use per frame; past it, the rest of the frame's objects are drawn. */
    static final long BUDGET_NANOS = 2_000_000L;
    /** Debug (CI): decide and count, but hide nothing. */
    static final boolean DRY_RUN = Boolean.getBoolean("brimstone.debug.visibilityDryRun");
    /** Debug (CI): check every hidden object with rays from the camera ({@link VisibilityCheck}). */
    static final boolean VERIFY = Boolean.getBoolean("brimstone.debug.verifyVisibility");

    private static volatile State state = State.STARTING;
    private static volatile String reason = "waiting for the first frame";
    private static final AtomicInteger FALLBACKS = new AtomicInteger();

    private static final Occlusion OCCLUSION = new Occlusion();
    private static int frame;
    private static boolean ready;
    private static Camera camera;
    private static Frustum frustum;
    private static long spent;
    static volatile boolean entityHookSeen, blockEntityHookSeen;

    /** Per entity or block entity class (and renderer class): 1 a kind Brimstone recognises, 2 not. */
    private static final Reference2ByteOpenHashMap<Class<?>> KNOWN = new Reference2ByteOpenHashMap<>();

    private Visibility() {
    }

    public static State state() {
        return state;
    }

    public static String reason() {
        return reason;
    }

    public static int fallbacks() {
        return FALLBACKS.get();
    }

    /** Render thread, as vanilla starts picking this frame's entities (its visible sections are known by then). */
    public static void beginFrame(Camera cam, Frustum cullFrustum) {
        VisibilityStats.endFrame(spent);
        VisibilityCheck.newFrame();
        ready = false;
        spent = 0;
        if (state == State.OFF) return;
        try {
            if (state == State.STARTING && !start()) return;
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level == null || cam == null || cullFrustum == null) return;
            var visible = mc.levelRenderer.visibleSections();
            VisibilityStats.frame(visible.size());
            // the sections Brimstone's terrain holds must be drawn (not mid hand-back) for their faces to hide anything
            if (!Terrain.drawsAllItHolds()) return;
            var viewArea = mc.levelRenderer.viewArea();
            if (viewArea == null) return;
            frame++;
            long fadeMs = Util.toMillis(mc.options.chunkSectionFadeInTime().get());
            OCCLUSION.beginFrame(level, viewArea, visible, frame, fadeMs);
            camera = cam;
            frustum = cullFrustum;
            ready = true;
        } catch (Throwable t) {
            fail("starting a frame", t);
        }
    }

    private static boolean start() {
        String why = VisibilityMixins.loadTargetsAndCheck();
        if (why == null && !EntityRendererInvoker.class.isAssignableFrom(EntityRenderer.class)) why = "the EntityRenderer invoker did not apply";
        if (why == null && !RenderSectionStamp.class.isAssignableFrom(SectionRenderDispatcher.RenderSection.class)) {
            why = "the RenderSection field did not apply";
        }
        if (why != null) {
            state = State.OFF;
            reason = why;
            FALLBACKS.incrementAndGet();
            BrimstoneClient.LOG.error("Brimstone visibility: turned off before its first frame, vanilla decides what to draw (fallback: {})", why);
            return false;
        }
        state = State.ON;
        reason = "on";
        BrimstoneClient.LOG.info("Brimstone visibility: on (sections: vanilla's frustum and cave graph, unchanged; entities and block entities"
            + " of vanilla's own kinds hidden only when an opaque slab proves them unseen; block entities also outside the frustum;"
            + " budget {} ms per frame{}{})", BUDGET_NANOS / 1e6, DRY_RUN ? "; DRY RUN, nothing hidden" : "", VERIFY ? "; every hidden object checked with rays" : "");
        return true;
    }

    static void fail(String where, Throwable t) {
        synchronized (Visibility.class) {
            if (state == State.OFF) return;
            state = State.OFF;
            reason = where + ": " + t;
        }
        ready = false;
        FALLBACKS.incrementAndGet();
        BrimstoneClient.LOG.error("Brimstone visibility: turned off, vanilla decides what to draw again (fallback in {})", where, t);
    }

    /** Vanilla decided whether to draw an entity; Brimstone may still hide it. */
    public static boolean entityVisible(Entity entity, boolean vanilla, double camX, double camY, double camZ, float partialTick) {
        entityHookSeen = true;
        if (!vanilla) return false;
        VisibilityStats.entityConsidered();
        if (!ready) return true;
        try {
            Minecraft mc = Minecraft.getInstance();
            EntityRenderer<?, ?> renderer = mc.levelRenderer.entityRenderDispatcher().getRenderer(entity);
            if (renderer == null || !recognised(entity, renderer)) return true;
            if (spent >= BUDGET_NANOS) {
                VisibilityStats.overBudget();
                return true;
            }
            long t0 = System.nanoTime();
            EntityRendererInvoker inv = (EntityRendererInvoker) renderer;
            boolean hide = false;
            AABB box = null;
            if (inv.brimstone$affectedByCulling(entity)) {
                AABB cull = inv.brimstone$cullingBox(entity, partialTick);
                VisibilityStats.entityTested();
                if (cull != null) {
                    float size = Math.max(entity.getBbWidth(), entity.getBbHeight());
                    // model parts, held items and the shadow reach past the box vanilla's frustum test uses
                    box = cull.inflate(1.0 + 0.25 * size);
                    hide = OCCLUSION.hidden(camX, camY, camZ, box);
                }
            }
            spent += System.nanoTime() - t0;
            if (!hide) return true;
            VisibilityStats.entityCulled();
            if (VERIFY) VisibilityCheck.check("entity " + entity.getType() + " at " + entity.blockPosition(), box, camX, camY, camZ, frustum, mc.level);
            return DRY_RUN;
        } catch (Throwable t) {
            fail("entity occlusion", t);
            return true;
        }
    }

    /** False to hide a block entity vanilla is about to draw (from a visible section's list). */
    public static boolean blockEntityVisible(BlockEntityRenderDispatcher dispatcher, BlockEntity be) {
        blockEntityHookSeen = true;
        VisibilityStats.blockEntityConsidered();
        if (!ready) return true;
        try {
            BlockEntityRenderer<BlockEntity, ?> renderer = dispatcher.getRenderer(be);
            if (renderer == null || renderer.shouldRenderOffScreen() || !recognised(be, renderer)) return true;
            if (spent >= BUDGET_NANOS) {
                VisibilityStats.overBudget();
                return true;
            }
            long t0 = System.nanoTime();
            VisibilityStats.blockEntityTested();
            BlockPos p = be.getBlockPos();
            // beds, double chests, banners, bells and the things block entities hold reach about a block past their own
            AABB box = new AABB(p).inflate(1.0);
            var eye = camera.position();
            boolean outside = !frustum.isVisible(box);
            boolean hide = outside || OCCLUSION.hidden(eye.x, eye.y, eye.z, box);
            spent += System.nanoTime() - t0;
            if (!hide) return true;
            VisibilityStats.blockEntityCulled(outside);
            if (VERIFY) VisibilityCheck.check("block entity " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType()) + " at " + p, box,
                eye.x, eye.y, eye.z, frustum, Minecraft.getInstance().level);
            return DRY_RUN;
        } catch (Throwable t) {
            fail("block entity occlusion", t);
            return true;
        }
    }

    /**
     * Kinds Brimstone may hide: vanilla's own entity kinds (mobs other than the ender dragon, guardians and frogs, whose
     * beams or tongues reach far outside them; items, experience orbs, armour stands, minecarts, item frames and
     * paintings) that are not a player, carry no name, outline, leash or rider, are not too large, and are drawn by
     * vanilla's own renderer. Anything else is drawn as vanilla decides.
     */
    private static boolean recognised(Entity e, EntityRenderer<?, ?> renderer) {
        if (!knownClass(e.getClass(), renderer.getClass())) return false;
        if (e instanceof Avatar || e instanceof EnderDragon || e instanceof Guardian || e instanceof Frog) return false;
        if (!(e instanceof Mob || e instanceof ItemEntity || e instanceof ExperienceOrb || e instanceof ArmorStand
            || e instanceof AbstractMinecart || e instanceof HangingEntity)) return false;
        if (!"minecraft".equals(BuiltInRegistries.ENTITY_TYPE.getKey(e.getType()).getNamespace())) return false;
        if (e.isPassenger() || e.isVehicle() || e.hasCustomName() || e.isCurrentlyGlowing()) return false;
        if (e instanceof Leashable l && l.isLeashed()) return false;
        Minecraft mc = Minecraft.getInstance();
        if (mc.shouldEntityAppearGlowing(e) || (camera != null && camera.entity() == e)) return false;
        return Math.max(e.getBbWidth(), e.getBbHeight()) <= 6.0f;
    }

    private static boolean recognised(BlockEntity be, BlockEntityRenderer<?, ?> renderer) {
        if (!knownClass(be.getClass(), renderer.getClass())) return false;
        return "minecraft".equals(BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType()).getNamespace());
    }

    /** Both classes are vanilla's own. */
    private static boolean knownClass(Class<?> object, Class<?> renderer) {
        return vanilla(object) && vanilla(renderer);
    }

    private static boolean vanilla(Class<?> c) {
        byte v = KNOWN.getByte(c);
        if (v == 0) {
            v = c.getName().startsWith("net.minecraft.") ? (byte) 1 : (byte) 2;
            KNOWN.put(c, v);
        }
        return v == 1;
    }

    // change tracking (from the mixins; render thread)

    public static void sectionChanged(int x, int y, int z) {
        try {
            SectionChanges.section(x, y, z);
        } catch (Throwable t) {
            fail("tracking section changes", t);
        }
    }

    public static void blockChanged(BlockPos pos) {
        try {
            SectionChanges.block(pos.getX(), pos.getY(), pos.getZ());
        } catch (Throwable t) {
            fail("tracking block changes", t);
        }
    }

    public static void columnChanged(int cx, int cz) {
        try {
            SectionChanges.column(cx, cz);
        } catch (Throwable t) {
            fail("tracking chunk changes", t);
        }
    }

    public static void reset() {
        try {
            SectionChanges.reset();
            ready = false;
        } catch (Throwable t) {
            fail("reset", t);
        }
    }
}
