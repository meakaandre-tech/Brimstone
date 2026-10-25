package com.meakaandre.brimstone.visibility;

import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.mixin.visibility.EntityRendererInvoker;
import com.meakaandre.brimstone.terrain.Terrain;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import java.util.Map;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
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
 * of it can be seen ({@link Occlusion}), and only for the kinds on its allow-lists ({@link #ENTITY_MARGIN},
 * {@link #BLOCK_ENTITY_TYPES}): vanilla types whose drawn extent was checked against the box Brimstone tests (the
 * reasoning per type is in docs/visibility-allowlist.md). Everything else is left to vanilla. The tests are skipped for
 * the rest of a frame once they have used {@link #BUDGET_NANOS} (what is left is drawn). No frustum test of Brimstone's
 * own: the frame is drawn with view bobbing and the damage tilt on top of the frustum vanilla culls with.
 *
 * <p>Crash safety: anything unexpected turns the module off for the session with one ERROR line, and vanilla decides
 * alone again. A resource pack that supplies a block texture, model or blockstate in the {@code minecraft} namespace
 * defers it (nothing hidden; {@link VisibilityResources}).
 */
public final class Visibility {
    public enum State { STARTING, ON, DEFERRED, OFF }

    /** Render-thread time the visibility work may use per frame; past it, the rest of the frame's objects are drawn. */
    static final long BUDGET_NANOS = 2_000_000L;
    /** Debug (CI noise collection only): decide and count, but hide nothing. CI fails any run that has it set. */
    static final boolean DRY_RUN = Boolean.getBoolean("brimstone.debug.visibilityDryRun");
    /** Debug (CI): check every hidden object with rays from the camera ({@link VisibilityCheck}). */
    static final boolean VERIFY = Boolean.getBoolean("brimstone.debug.verifyVisibility");

    /**
     * Entity allow-list: type -> blocks added on every side of vanilla's own culling box (times the entity's scale).
     * Each type's drawn extent (model and animations, held items, fire, shadow) was checked to fit; see
     * docs/visibility-allowlist.md. Name tags, outlines, leashes, riders, sleeping and dying poses are excluded below.
     */
    static final Map<EntityType<?>, Double> ENTITY_MARGIN = Map.ofEntries(
        Map.entry(EntityTypes.COW, 2.0), Map.entry(EntityTypes.MOOSHROOM, 2.0), Map.entry(EntityTypes.PIG, 2.0),
        Map.entry(EntityTypes.SHEEP, 2.0), Map.entry(EntityTypes.CHICKEN, 2.0), Map.entry(EntityTypes.VILLAGER, 2.0),
        Map.entry(EntityTypes.IRON_GOLEM, 2.0), Map.entry(EntityTypes.SNOW_GOLEM, 2.0), Map.entry(EntityTypes.ZOMBIE, 2.0),
        Map.entry(EntityTypes.SKELETON, 2.0), Map.entry(EntityTypes.CREEPER, 2.0), Map.entry(EntityTypes.SPIDER, 2.0),
        Map.entry(EntityTypes.ITEM, 2.0));

    /**
     * Block entity allow-list: each type's renderer draws inside its block grown by one block on every side (lids,
     * double chests, standing and wall banners, hanging signs, bell, books, held items); see
     * docs/visibility-allowlist.md. Not end gateways (beams), beacons, spawners, conduits or anything else.
     */
    static final java.util.Set<BlockEntityType<?>> BLOCK_ENTITY_TYPES = java.util.Set.of(
        BlockEntityTypes.CHEST, BlockEntityTypes.TRAPPED_CHEST, BlockEntityTypes.ENDER_CHEST, BlockEntityTypes.SIGN,
        BlockEntityTypes.HANGING_SIGN, BlockEntityTypes.BANNER, BlockEntityTypes.BELL, BlockEntityTypes.SHULKER_BOX,
        BlockEntityTypes.DECORATED_POT, BlockEntityTypes.CAMPFIRE, BlockEntityTypes.LECTERN, BlockEntityTypes.ENCHANTING_TABLE);
    static final double BLOCK_ENTITY_MARGIN = 1.0;

    private static volatile State state = State.STARTING;
    private static volatile String reason = "waiting for the first frame";
    private static final AtomicInteger FALLBACKS = new AtomicInteger();
    private static volatile boolean resourcesChanged = true;

    private static final Occlusion OCCLUSION = new Occlusion();
    private static int frame;
    private static boolean ready;
    private static Camera camera;
    private static long spent;
    static volatile boolean entityHookSeen, blockEntityHookSeen;

    /** Per renderer class: 1 vanilla's own, 2 not. */
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
            if (resourcesChanged) checkResources();
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (level == null || cam == null) return;
            var visible = mc.levelRenderer.visibleSections();
            VisibilityStats.frame(visible.size());
            if (state != State.ON) return;
            // the sections Brimstone's terrain holds must be drawn (not mid hand-back) for their faces to hide anything
            if (!Terrain.drawsAllItHolds()) return;
            var viewArea = mc.levelRenderer.viewArea();
            if (viewArea == null) return;
            frame++;
            long fadeMs = Util.toMillis(mc.options.chunkSectionFadeInTime().get());
            OCCLUSION.beginFrame(level, viewArea, visible, frame, fadeMs);
            camera = cam;
            ready = true;
        } catch (Throwable t) {
            fail("starting a frame", t);
        }
    }

    private static boolean start() {
        if (Boolean.getBoolean("brimstone.ci.measureOnly")) {
            // CI's measure runs (package 3): vanilla decides what to draw, the bench measures vanilla's own counts
            state = State.OFF;
            reason = "measure only (CI)";
            BrimstoneClient.LOG.warn("Brimstone visibility: off, brimstone.ci.measureOnly is set");
            return false;
        }
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
        BrimstoneClient.LOG.info("Brimstone visibility: on (sections: vanilla's frustum and cave graph, unchanged; allow-listed vanilla entities"
            + " ({} types) and block entities ({} types) hidden only when an opaque slab proves them unseen; budget {} ms per frame{}{})",
            ENTITY_MARGIN.size(), BLOCK_ENTITY_TYPES.size(), BUDGET_NANOS / 1e6, DRY_RUN ? "; DRY RUN (noise collection), nothing hidden" : "",
            VERIFY ? "; every hidden object checked with rays" : "");
        return true;
    }

    /** Resource packs changed (or first frame): defer while one supplies minecraft block textures, models or blockstates. */
    private static void checkResources() {
        resourcesChanged = false;
        Occlusion.clearCache();
        String pack = VisibilityResources.override();
        if (pack != null && state == State.ON) {
            state = State.DEFERRED;
            reason = pack;
            BrimstoneClient.LOG.warn("Brimstone visibility: deferring to vanilla, nothing is hidden while {}", pack);
        } else if (pack == null && state == State.DEFERRED) {
            state = State.ON;
            reason = "on";
            BrimstoneClient.LOG.info("Brimstone visibility: resource packs no longer replace vanilla block resources, hiding again");
        }
    }

    /** Called by the resource reload (render thread). */
    public static void resourcesReloaded() {
        resourcesChanged = true;
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
        if (spent >= BUDGET_NANOS) {
            VisibilityStats.overBudget();
            return true;
        }
        long t0 = System.nanoTime();
        try {
            Double margin = ENTITY_MARGIN.get(entity.getType());
            if (margin == null || !recognised(entity)) return true;
            Minecraft mc = Minecraft.getInstance();
            EntityRenderer<?, ?> renderer = mc.levelRenderer.entityRenderDispatcher().getRenderer(entity);
            if (renderer == null || !vanilla(renderer.getClass())) return true;
            EntityRendererInvoker inv = (EntityRendererInvoker) renderer;
            if (!inv.brimstone$affectedByCulling(entity)) return true;
            AABB cull = inv.brimstone$cullingBox(entity, partialTick);
            if (cull == null) return true;
            VisibilityStats.entityTested();
            double scale = entity instanceof LivingEntity l ? Math.max(1.0, l.getScale()) : 1.0;
            AABB box = cull.inflate(margin * scale);
            if (!OCCLUSION.hidden(camX, camY, camZ, box)) return true;
            VisibilityStats.entityCulled();
            if (VERIFY) {
                spent += System.nanoTime() - t0;
                VisibilityCheck.check("entity " + entity.getType() + " at " + entity.blockPosition(), box, camX, camY, camZ, mc.level);
                t0 = System.nanoTime(); // the CI check's own time is not the module's cost
            }
            return DRY_RUN;
        } catch (Throwable t) {
            fail("entity occlusion", t);
            return true;
        } finally {
            spent += System.nanoTime() - t0;
        }
    }

    /** False to hide a block entity vanilla is about to draw (from a visible section's list). */
    public static boolean blockEntityVisible(BlockEntityRenderDispatcher dispatcher, BlockEntity be) {
        blockEntityHookSeen = true;
        VisibilityStats.blockEntityConsidered();
        if (!ready) return true;
        if (spent >= BUDGET_NANOS) {
            VisibilityStats.overBudget();
            return true;
        }
        long t0 = System.nanoTime();
        try {
            if (!BLOCK_ENTITY_TYPES.contains(be.getType()) || !vanilla(be.getClass())) return true;
            BlockEntityRenderer<BlockEntity, ?> renderer = dispatcher.getRenderer(be);
            if (renderer == null || renderer.shouldRenderOffScreen() || !vanilla(renderer.getClass())) return true;
            VisibilityStats.blockEntityTested();
            BlockPos p = be.getBlockPos();
            AABB box = new AABB(p).inflate(BLOCK_ENTITY_MARGIN);
            var eye = camera.position();
            if (!OCCLUSION.hidden(eye.x, eye.y, eye.z, box)) return true;
            VisibilityStats.blockEntityCulled();
            if (VERIFY) {
                spent += System.nanoTime() - t0;
                VisibilityCheck.check("block entity " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType()) + " at " + p, box,
                    eye.x, eye.y, eye.z, Minecraft.getInstance().level);
                t0 = System.nanoTime();
            }
            return DRY_RUN;
        } catch (Throwable t) {
            fail("block entity occlusion", t);
            return true;
        } finally {
            spent += System.nanoTime() - t0;
        }
    }

    /**
     * Per-entity exclusions on top of the allow-list: anything drawn outside the entity's own model (name tags, which
     * draw through walls; outlines; leashes; riders and what they ride), the camera entity, and poses whose extent the
     * allow-list margins were not checked for (sleeping, dying).
     */
    private static boolean recognised(Entity e) {
        if (!vanilla(e.getClass())) return false;
        if (e.isPassenger() || e.isVehicle() || e.hasCustomName() || e.isCurrentlyGlowing()) return false;
        if (e instanceof Leashable l && l.isLeashed()) return false;
        if (e instanceof LivingEntity l && (l.isSleeping() || l.isDeadOrDying())) return false;
        Minecraft mc = Minecraft.getInstance();
        return !mc.shouldEntityAppearGlowing(e) && (camera == null || camera.entity() != e);
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
