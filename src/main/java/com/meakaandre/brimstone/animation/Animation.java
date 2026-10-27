package com.meakaandre.brimstone.animation;

import com.google.gson.JsonObject;
import com.meakaandre.brimstone.BrimstoneClient;
import com.meakaandre.brimstone.bench.GpuCounters;
import com.meakaandre.brimstone.terrain.CompactVertex;
import com.meakaandre.brimstone.terrain.TerrainGpuErrors;
import com.meakaandre.brimstone.terrain.TerrainMixins;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import com.mojang.renderpearl.frontend.FrontendCommandEncoder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
import net.minecraft.client.renderer.chunk.SectionMesh;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.system.MemoryUtil;

/**
 * Brimstone's animation module (package 8): animated block-atlas sprites are redrawn only when something on screen can
 * show them.
 *
 * <p>Vanilla redraws every animated sprite of an atlas into the atlas on each client tick that changes its frame (one
 * render pass per mip level, one draw per sprite and level), whether or not anything shows it. Brimstone keeps
 * vanilla's animation state ticking exactly as before and only leaves out the redraw of a sprite nothing can show; the
 * sprite is then <i>stale</i> and is redrawn from its current state, with vanilla's own {@code drawToAtlas}, before
 * anything can sample it:
 * <ul>
 * <li>Terrain: which animated sprites a section's mesh shows is read from the mesh itself (each quad's UV centre looked
 * up in the animated sprites' rectangles; {@link #meshLayer}), as each layer goes to the GPU, so it holds for vanilla,
 * FRAPI and modded models alike. Before a frame's terrain is drawn (vanilla's visible list is known, no render pass is
 * open; {@link #beforeTerrain}) every stale sprite in a visible section's mesh is redrawn.</li>
 * <li>Everything else (items, particles, entities, block entities, the screen overlays, Create's and Flywheel's caches):
 * a sprite whose UVs are read outside section meshing, through the sprite's UV getters or a baked quad's UVs
 * ({@link #spriteUsed}), counts as used outside terrain from then on, until the atlas reloads, and is never left out
 * again. A stale sprite is redrawn on the spot when that first happens (on the render thread, outside a render pass);
 * where that is impossible (inside a render pass, or on another thread) it is redrawn at the next safe point and counted
 * as late, which CI fails on.</li>
 * </ul>
 * So whatever samples the atlas finds exactly vanilla's frame there.
 *
 * <p>States: STARTING until its hooks, the vertex format it reads and the block atlas's table are proven, then ON; OFF
 * in CI's measure runs and for the rest of the session after anything unexpected (one ERROR line), when every stale
 * sprite is redrawn at once and vanilla redraws everything again.
 */
public final class Animation {
    public enum State { STARTING, ON, OFF }

    /** Debug (CI): one log line for each sprite's first use outside terrain, with where it came from. */
    static final boolean LOG_USES = Boolean.getBoolean("brimstone.debug.animationUses");
    private static final int USED_ELSEWHERE = 1, PENDING = 2;
    private static final int POISON = -1;
    private static final int GRID = 256;

    private static volatile State state = State.STARTING;
    private static volatile String reason = "waiting for the first frame";
    private static final AtomicInteger FALLBACKS = new AtomicInteger();
    private static volatile Table table;
    private static int nextGen = 1;
    private static volatile boolean catchUpAllPending;
    private static boolean drawing;
    private static int framesWaiting;
    /** Set once the section compile scope hook (a MixinExtras wrapper, applied after the plugin's check) has run. */
    public static volatile boolean compileHookSeen;

    /** Per thread: inside vanilla's section meshing (or another scope whose UV reads are not uses), how deep. */
    private static final ThreadLocal<int[]> NOT_A_USE = ThreadLocal.withInitial(() -> new int[1]);

    // window (bench) and since-start counters; render thread unless atomic
    private static long wTicks, wDirty, wDrawnAtTick, wSkipped, wCatchUps, wSpriteDraws, wFrames, wSeen, wFramesAll;
    /** Catch-ups while measuring, by where they happened: on a tick, before a frame's terrain, on a first use outside terrain. */
    private static long wAtTick, wBeforeTerrain, wFirstUse;
    private static long sSkipped, sCatchUps, sImmediate, sFramesAll;
    private static int unreadLogged;
    private static final AtomicLong LATE = new AtomicLong(), LATE_WINDOW = new AtomicLong();
    private static final AtomicLong MESH_LAYERS = new AtomicLong(), MESH_NANOS = new AtomicLong();

    private Animation() {
    }

    /** The animated sprites of one upload of the block atlas. */
    static final class Table {
        final int gen;
        final TextureAtlas atlas;
        final SpriteContents.AnimationState[] states;
        final TextureAtlasSprite[] sprites;
        final float[] u0, u1, v0, v1;
        final int[] grid = new int[GRID * GRID];
        /** Whether each animation blends between frames (redrawn by vanilla on every tick) or shows whole frames. */
        final boolean[] interpolated;
        final AtomicIntegerArray flags;
        final AtomicInteger usedElsewhere = new AtomicInteger();
        // render thread only
        final boolean[] stale;
        final long[] seen;
        boolean seenAll = true;
        boolean firstUpload = true;

        Table(int gen, TextureAtlas atlas, SpriteContents.AnimationState[] states, TextureAtlasSprite[] sprites, int width, int height) {
            this.gen = gen;
            this.atlas = atlas;
            this.states = states;
            this.sprites = sprites;
            int n = states.length;
            u0 = new float[n];
            u1 = new float[n];
            v0 = new float[n];
            v1 = new float[n];
            flags = new AtomicIntegerArray(n);
            interpolated = new boolean[n];
            for (int i = 0; i < n; i++) interpolated[i] = interpolates(states[i]);
            stale = new boolean[n];
            seen = new long[(n + 63) >> 6];
            Arrays.fill(grid, -1);
            // half a texel of margin on every side: a quad whose UVs sit on the sprite's edge still counts as showing it
            float mu = 0.5f / Math.max(1, width), mv = 0.5f / Math.max(1, height);
            for (int i = 0; i < n; i++) {
                SpriteSlot s = (SpriteSlot) sprites[i];
                u0[i] = s.brimstone$u0() - mu;
                u1[i] = s.brimstone$u1() + mu;
                v0[i] = s.brimstone$v0() - mv;
                v1[i] = s.brimstone$v1() + mv;
                int cx0 = cell(u0[i]), cx1 = cell(u1[i]), cy0 = cell(v0[i]), cy1 = cell(v1[i]);
                for (int cy = cy0; cy <= cy1; cy++) {
                    for (int cx = cx0; cx <= cx1; cx++) {
                        int c = cy * GRID + cx;
                        grid[c] = grid[c] == -1 ? i : -2;
                    }
                }
            }
        }

        /** Reads vanilla's animation's own "interpolate" flag (SpriteContents.AnimatedTexture is not public). */
        private static boolean interpolates(SpriteContents.AnimationState state) {
            try {
                java.lang.reflect.Field info = SpriteContents.AnimationState.class.getDeclaredField("animationInfo");
                info.setAccessible(true);
                Object anim = info.get(state);
                java.lang.reflect.Field f = anim.getClass().getDeclaredField("interpolateFrames");
                f.setAccessible(true);
                return f.getBoolean(anim);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("cannot read whether an animation interpolates", e);
            }
        }

        private static int cell(float t) {
            return Math.max(0, Math.min(GRID - 1, (int) Math.floor(t * GRID)));
        }

        /** Adds to {@code out} (a bit set) every animated sprite whose rectangle holds (u, v). */
        void find(float u, float v, long[] out) {
            if (!(u >= -0.01f && u <= 1.01f && v >= -0.01f && v <= 1.01f)) return;
            int c = grid[cell(v) * GRID + cell(u)];
            if (c == -1) return;
            if (c >= 0) {
                if (inside(c, u, v)) out[c >> 6] |= 1L << c;
                return;
            }
            for (int i = 0; i < states.length; i++) if (inside(i, u, v)) out[i >> 6] |= 1L << i;
        }

        private boolean inside(int i, float u, float v) {
            return u >= u0[i] && u <= u1[i] && v >= v0[i] && v <= v1[i];
        }

        boolean seen(int i) {
            return seenAll || (seen[i >> 6] & (1L << i)) != 0 || (flags.get(i) & USED_ELSEWHERE) != 0;
        }
    }

    public static State state() {
        return state;
    }

    public static String reason() {
        return reason;
    }

    // ---- scopes whose UV reads are not uses (section meshing; Fabric's sprite finder indexing every sprite)

    public static void notAUseEnter() {
        NOT_A_USE.get()[0]++;
    }

    public static void notAUseExit() {
        int[] d = NOT_A_USE.get();
        if (d[0] > 0) d[0]--;
    }

    // ---- the block atlas

    /** Render thread: an atlas finished uploading new contents. For the block atlas, builds the animation table. */
    public static void atlasUploaded(TextureAtlas atlas) {
        if (!TextureAtlas.LOCATION_BLOCKS.equals(atlas.location())) return;
        try {
            AtlasFrames a = (AtlasFrames) atlas;
            List<SpriteContents.AnimationState> list = a.brimstone$animationStates();
            int n = list.size();
            SpriteContents.AnimationState[] states = list.toArray(new SpriteContents.AnimationState[0]);
            TextureAtlasSprite[] sprites = new TextureAtlasSprite[n];
            for (int i = 0; i < n; i++) {
                sprites[i] = ((StateSprite) states[i]).brimstone$sprite();
                if (sprites[i] == null) throw new IllegalStateException("animated sprite " + i + " of " + n + " has no sprite");
            }
            Table t = new Table(nextGen++, atlas, states, sprites, a.brimstone$width(), a.brimstone$height());
            for (int i = 0; i < n; i++) ((SpriteSlot) sprites[i]).brimstone$setSlot(i + 1);
            table = t;
        } catch (Throwable t) {
            table = null;
            fail("reading the block atlas", t);
        }
    }

    /**
     * Render thread, a client tick's redraw of an atlas's changed animated sprites (vanilla's
     * {@code uploadAnimationFrames}). True if Brimstone did it (vanilla's is skipped): sprites that changed are redrawn
     * if seen in the last frame, the others marked stale; stale sprites seen again are redrawn too.
     */
    public static boolean uploadFrames(TextureAtlas atlas) {
        Table t = table;
        if (state != State.ON || t == null || t.atlas != atlas) return false;
        try {
            IntArrayList draw = new IntArrayList();
            boolean first = t.firstUpload;
            t.firstUpload = false;
            wTicks++;
            for (int i = 0; i < t.states.length; i++) {
                pending(t, i);
                boolean dirty = t.states[i].needsToDraw();
                boolean needed = first || t.seen(i);
                if (dirty) {
                    wDirty++;
                    if (needed) {
                        draw.add(i);
                        wDrawnAtTick++;
                    } else {
                        wSkipped++;
                        sSkipped++;
                    }
                    t.stale[i] = !needed;
                } else if (t.stale[i] && needed) {
                    draw.add(i);
                    t.stale[i] = false;
                    wCatchUps++;
                    sCatchUps++;
                    wAtTick++;
                }
            }
            drawSprites(t, draw);
            return true;
        } catch (Throwable e) {
            fail("redrawing animated sprites", e);
            // a safe point (render thread, no pass open): every stale sprite is redrawn before anything draws from it
            catchUpAll();
            return false;
        }
    }

    // ---- terrain

    /**
     * A worker thread (or the render thread) is about to put one layer of a freshly built section mesh to the GPU:
     * records which animated sprites its quads show (vertices null: only a new index list, nothing to read).
     */
    public static void meshLayer(CompiledSectionMesh mesh, ByteBuffer vertices) {
        if (vertices == null || mesh == null) return;
        MeshSprites m = (MeshSprites) mesh;
        Table t = table;
        try {
            long t0 = System.nanoTime();
            int[] ids = t == null ? null : scan(t, vertices);
            synchronized (mesh) {
                int g = m.brimstone$animGen();
                if (t == null || ids == null) {
                    m.brimstone$setAnim(POISON, null);
                } else if (g == 0) {
                    m.brimstone$setAnim(t.gen, ids.length == 0 ? null : ids);
                } else if (g == t.gen) {
                    m.brimstone$setAnim(g, merge(m.brimstone$animIds(), ids));
                } else {
                    m.brimstone$setAnim(POISON, null);
                }
            }
            MESH_LAYERS.incrementAndGet();
            MESH_NANOS.addAndGet(System.nanoTime() - t0);
        } catch (Throwable e) {
            m.brimstone$setAnim(POISON, null);
            fail("reading a section mesh", e);
        }
    }

    /** The animated sprites a layer's quads show (vanilla's BLOCK vertices: UV at byte 16 of 28), sorted; null if unreadable. */
    private static int[] scan(Table t, ByteBuffer vertices) {
        int n = t.states.length;
        int bytes = vertices.remaining();
        if (bytes % (4 * CompactVertex.VANILLA_BYTES) != 0) return null;
        if (n == 0 || bytes == 0) return new int[0];
        long base = MemoryUtil.memAddress(vertices);
        long[] found = new long[(n + 63) >> 6];
        int quads = bytes / (4 * CompactVertex.VANILLA_BYTES);
        for (int q = 0; q < quads; q++) {
            long a = base + (long) q * 4 * CompactVertex.VANILLA_BYTES + 16;
            float u = 0, v = 0;
            for (int k = 0; k < 4; k++) {
                u += MemoryUtil.memGetFloat(a + (long) k * CompactVertex.VANILLA_BYTES);
                v += MemoryUtil.memGetFloat(a + (long) k * CompactVertex.VANILLA_BYTES + 4);
            }
            t.find(u * 0.25f, v * 0.25f, found);
        }
        IntArrayList out = new IntArrayList();
        for (int i = 0; i < n; i++) if ((found[i >> 6] & (1L << i)) != 0) out.add(i);
        return out.toIntArray();
    }

    private static int[] merge(int[] a, int[] b) {
        if (a == null || a.length == 0) return b.length == 0 ? null : b;
        if (b.length == 0) return a;
        int[] all = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, all, a.length, b.length);
        return Arrays.stream(all).sorted().distinct().toArray();
    }

    /**
     * Render thread, as vanilla prepares a frame's terrain draws (its visible sections are known, no render pass is
     * open, nothing of the frame is drawn yet): works out which animated sprites the frame's terrain shows and redraws
     * every stale one it shows or that is now used outside terrain.
     */
    public static void beforeTerrain(List<SectionRenderDispatcher.RenderSection> visible) {
        NOT_A_USE.get()[0] = 0;
        if (catchUpAllPending) catchUpAll();
        if (state == State.STARTING) start();
        Table t = table;
        if (state != State.ON || t == null) return;
        try {
            Arrays.fill(t.seen, 0L);
            boolean all = false;
            for (SectionRenderDispatcher.RenderSection s : visible) {
                SectionMesh mesh = s.getSectionMesh();
                if (!(mesh instanceof CompiledSectionMesh cm)) continue;
                MeshSprites m = (MeshSprites) cm;
                int g = m.brimstone$animGen();
                if (g == t.gen) {
                    int[] ids = m.brimstone$animIds();
                    if (ids != null) for (int id : ids) t.seen[id >> 6] |= 1L << id;
                } else if (cm.hasRenderableLayers()) {
                    all = true;
                    if (LOG_USES && unreadLogged < 20) {
                        unreadLogged++;
                        BrimstoneClient.LOG.info("Brimstone animation: visible section {} has a mesh whose sprites were not read (generation {}, table {})",
                            net.minecraft.core.SectionPos.of(s.getSectionNode()), g, t.gen);
                    }
                }
            }
            t.seenAll = all;
            wFrames++;
            if (all) {
                wFramesAll++;
                sFramesAll++;
            }
            IntArrayList draw = new IntArrayList();
            int seenCount = 0;
            for (int i = 0; i < t.states.length; i++) {
                if (t.seen(i)) seenCount++;
                pending(t, i);
                if (t.stale[i] && t.seen(i)) {
                    draw.add(i);
                    t.stale[i] = false;
                    wCatchUps++;
                    sCatchUps++;
                    wBeforeTerrain++;
                }
            }
            wSeen += seenCount;
            drawSprites(t, draw);
        } catch (Throwable e) {
            fail("redrawing stale sprites before terrain", e);
            // a safe point (render thread, no pass open): every stale sprite is redrawn before anything draws from it
            catchUpAll();
        }
    }

    // ---- uses outside terrain

    /** A block-atlas sprite's UVs are being read (any thread); {@code slot} is its slot, never 0 here. */
    public static void spriteUsed(TextureAtlasSprite sprite, int slot) {
        Table t = table;
        if (t == null) return;
        int id = slot - 1;
        if (id >= t.sprites.length || t.sprites[id] != sprite) return;
        if ((t.flags.get(id) & USED_ELSEWHERE) != 0) return;
        if (NOT_A_USE.get()[0] > 0) return;
        int old;
        do {
            old = t.flags.get(id);
            if ((old & USED_ELSEWHERE) != 0) return;
        } while (!t.flags.compareAndSet(id, old, old | USED_ELSEWHERE));
        t.usedElsewhere.incrementAndGet();
        if (LOG_USES) logUse(sprite, id < t.stale.length && t.stale[id]);
        if (state != State.ON) {
            // the module failed on another thread: the redraw of every stale sprite happens here if it is a safe point
            if (catchUpAllPending && RenderSystem.isOnRenderThread() && !drawing
                && RenderSystem.getDevice().createCommandEncoder() instanceof FrontendCommandEncoder f && !f.isInRenderPass()) catchUpAll();
            return;
        }
        if (!RenderSystem.isOnRenderThread()) {
            // redrawn at the next safe point on the render thread, and counted as late there if it was stale
            t.flags.getAndUpdate(id, x -> x | PENDING);
            return;
        }
        if (!t.stale[id] || drawing) return;
        CommandEncoder enc = RenderSystem.getDevice().createCommandEncoder();
        if (!(enc instanceof FrontendCommandEncoder f) || f.isInRenderPass()) {
            // inside a render pass: redrawn at the next safe point, after whatever is drawing now may have used it
            late();
            return;
        }
        try {
            IntArrayList one = new IntArrayList(1);
            one.add(id);
            t.stale[id] = false;
            sImmediate++;
            wFirstUse++;
            wCatchUps++;
            sCatchUps++;
            drawSprites(t, one);
        } catch (Throwable e) {
            fail("redrawing a sprite on its first use outside terrain", e);
            // a safe point (render thread, no pass open): every stale sprite is redrawn before anything draws from it
            catchUpAll();
        }
    }

    /**
     * Render thread: a first use outside terrain from another thread is handled here (the sprite is redrawn by the
     * caller since it now counts as seen); if the sprite was stale it may already have been drawn from, so it is late.
     */
    private static void pending(Table t, int i) {
        if ((t.flags.get(i) & PENDING) == 0) return;
        t.flags.getAndUpdate(i, x -> x & ~PENDING);
        if (t.stale[i]) late();
    }

    private static void late() {
        LATE.incrementAndGet();
        LATE_WINDOW.incrementAndGet();
    }

    private static void logUse(TextureAtlasSprite sprite, boolean stale) {
        String from = StackWalker.getInstance().walk(s -> s.skip(3).limit(6)
            .map(f -> f.getClassName().substring(f.getClassName().lastIndexOf('.') + 1) + "." + f.getMethodName())
            .reduce((x, y) -> x + " < " + y).orElse("?"));
        BrimstoneClient.LOG.info("Brimstone animation: {} is used outside terrain ({}, thread {}, {})", sprite.contents().name(), from,
            Thread.currentThread().getName(), stale ? "stale" : "up to date");
    }

    // ---- drawing

    /** Redraws these sprites from their current state, exactly as vanilla's redraw does (render thread, no pass open). */
    private static void drawSprites(Table t, IntArrayList ids) {
        if (ids.isEmpty()) return;
        GpuCounters.Phase previous = GpuCounters.beginPhase(GpuCounters.Phase.ANIMATION);
        drawing = true;
        boolean done = false;
        try {
            TerrainGpuErrors.Mark mark = TerrainGpuErrors.mark();
            AtlasFrames a = (AtlasFrames) t.atlas;
            GpuTextureView[] views = a.brimstone$mipViews();
            int maxMip = a.brimstone$maxMipLevel();
            CommandEncoder enc = RenderSystem.getDevice().createCommandEncoder();
            for (int mip = 0; mip <= maxMip; mip++) {
                try (RenderPass pass = enc.createRenderPass(() -> "Brimstone animated sprites", views[mip], Optional.empty())) {
                    RenderSystem.bindDefaultUniforms(pass);
                    for (int k = 0; k < ids.size(); k++) {
                        int id = ids.getInt(k);
                        SpriteContents.AnimationState s = t.states[id];
                        // drawn as vanilla's redraw on the tick the current frame started draws it: with the state's
                        // redraw flag set and, for a whole-frame animation, at tick 0 of its frame
                        StateSprite st = (StateSprite) s;
                        int sub = st.brimstone$subFrame();
                        boolean dirty = st.brimstone$dirty();
                        if (!t.interpolated[id]) st.brimstone$setSubFrame(0);
                        if (!dirty) st.brimstone$setDirty(true);
                        try {
                            s.drawToAtlas(pass, s.getDrawUbo(mip));
                        } catch (RuntimeException e) {
                            throw new IllegalStateException("drawing " + t.sprites[id].contents().name() + " (interpolated " + t.interpolated[id]
                                + ", tick in frame " + sub + ", redraw flag " + dirty + ", " + k + " of " + ids.size() + " in the pass)", e);
                        } finally {
                            st.brimstone$setSubFrame(sub);
                            if (!dirty) st.brimstone$setDirty(false);
                        }
                    }
                }
            }
            wSpriteDraws += ids.size();
            String err = TerrainGpuErrors.since(mark);
            if (err != null) throw new IllegalStateException("GPU error: " + err);
            done = true;
        } finally {
            // a batch that failed counts as stale again, so the redraw of every stale sprite after the failure includes it
            if (!done) for (int k = 0; k < ids.size(); k++) t.stale[ids.getInt(k)] = true;
            drawing = false;
            GpuCounters.endPhase(previous);
        }
    }

    /** Render thread, no pass open: every stale sprite redrawn (the module is going off). */
    private static void catchUpAll() {
        catchUpAllPending = false;
        Table t = table;
        if (t == null) return;
        try {
            IntArrayList draw = new IntArrayList();
            for (int i = 0; i < t.states.length; i++) {
                if (t.stale[i]) {
                    draw.add(i);
                    t.stale[i] = false;
                }
            }
            drawSprites(t, draw);
            if (!draw.isEmpty()) BrimstoneClient.LOG.info("Brimstone animation: {} stale sprites redrawn, vanilla redraws every animated texture from here on", draw.size());
        } catch (Throwable e) {
            BrimstoneClient.LOG.error("Brimstone animation: could not redraw the stale sprites", e);
        }
    }

    // ---- states

    private static void start() {
        if (Boolean.getBoolean("brimstone.ci.measureOnly")) {
            state = State.OFF;
            reason = "measure only (CI)";
            BrimstoneClient.LOG.warn("Brimstone animation: off, brimstone.ci.measureOnly is set (vanilla redraws every animated texture)");
            return;
        }
        if (!compileHookSeen && ++framesWaiting < 600) return;
        String why = compileHookSeen ? AnimationMixins.loadTargetsAndCheck()
            : "hooks that did not apply: SectionCompiler#brimstone$compileScope (did not run in 600 frames)";
        if (why == null && !TerrainMixins.applied("net.minecraft.client.renderer.chunk.SectionRenderDispatcher$RenderSection", "brimstone$take")) {
            why = "hooks that did not apply: RenderSection#brimstone$take (where meshes are read)";
        }
        if (why == null) why = CompactVertex.checkVanillaFormat();
        if (why == null && !(RenderSystem.getDevice().createCommandEncoder() instanceof FrontendCommandEncoder)) {
            why = "the command encoder is not renderpearl's frontend";
        }
        if (why == null && table == null) why = "no animation table for the block atlas";
        if (why != null) {
            state = State.OFF;
            reason = why;
            FALLBACKS.incrementAndGet();
            BrimstoneClient.LOG.error("Brimstone animation: turned off before its first frame, vanilla redraws every animated texture (fallback: {})", why);
            return;
        }
        state = State.ON;
        reason = "on";
        BrimstoneClient.LOG.info("Brimstone animation: on ({} animated block-atlas sprites; each redrawn only when seen: in a visible section's mesh,"
            + " or used outside terrain)", table.states.length);
    }

    /** Something went wrong: off for good, with one log line; every stale sprite is redrawn at the next safe point. */
    static void fail(String where, Throwable t) {
        synchronized (Animation.class) {
            if (state == State.OFF) return;
            state = State.OFF;
            reason = where + ": " + t;
        }
        FALLBACKS.incrementAndGet();
        catchUpAllPending = true;
        BrimstoneClient.LOG.error("Brimstone animation: turned off, vanilla redraws every animated texture again (fallback in {})", where, t);
    }

    // ---- bench

    public static void startWindow() {
        wTicks = wDirty = wDrawnAtTick = wSkipped = wCatchUps = wSpriteDraws = wFrames = wSeen = wFramesAll = 0;
        wAtTick = wBeforeTerrain = wFirstUse = 0;
        LATE_WINDOW.set(0);
    }

    public static JsonObject json() {
        Table t = table;
        JsonObject o = new JsonObject();
        o.addProperty("state", state.name().toLowerCase(java.util.Locale.ROOT));
        o.addProperty("reason", reason);
        o.addProperty("fallbacks", FALLBACKS.get());
        o.addProperty("sprites", t == null ? 0 : t.states.length);
        o.addProperty("usedOutsideTerrain", t == null ? 0 : t.usedElsewhere.get());
        o.addProperty("ticks", wTicks);
        o.addProperty("changedPerTick", wTicks == 0 ? 0 : round((double) wDirty / wTicks));
        o.addProperty("redrawnPerTick", wTicks == 0 ? 0 : round((double) wDrawnAtTick / wTicks));
        o.addProperty("skippedPerTick", wTicks == 0 ? 0 : round((double) wSkipped / wTicks));
        o.addProperty("catchUps", wCatchUps);
        o.addProperty("skipped", wSkipped);
        o.addProperty("catchUpsAtTick", wAtTick);
        o.addProperty("catchUpsBeforeTerrain", wBeforeTerrain);
        o.addProperty("catchUpsOnFirstUse", wFirstUse);
        o.addProperty("spriteRedraws", wSpriteDraws);
        o.addProperty("seenPerFrame", wFrames == 0 ? 0 : round((double) wSeen / wFrames));
        o.addProperty("framesWithUnreadMeshes", wFramesAll);
        o.addProperty("late", LATE_WINDOW.get());
        o.addProperty("skippedSinceStart", sSkipped);
        o.addProperty("catchUpsSinceStart", sCatchUps);
        o.addProperty("redrawnOnFirstUseSinceStart", sImmediate);
        o.addProperty("lateSinceStart", LATE.get());
        o.addProperty("framesWithUnreadMeshesSinceStart", sFramesAll);
        long layers = MESH_LAYERS.get();
        o.addProperty("meshLayersRead", layers);
        o.addProperty("meshReadMsPerLayer", layers == 0 ? 0 : round(MESH_NANOS.get() / 1e6 / layers));
        return o;
    }

    public static String line(JsonObject o) {
        return o.get("state").getAsString() + " (" + o.get("reason").getAsString() + "), fallbacks " + o.get("fallbacks") + ", "
            + o.get("sprites") + " animated sprites (" + o.get("usedOutsideTerrain") + " used outside terrain), per tick changed "
            + o.get("changedPerTick") + " / redrawn " + o.get("redrawnPerTick") + " / left out " + o.get("skippedPerTick") + ", caught up "
            + o.get("catchUps") + ", seen per frame " + o.get("seenPerFrame") + ", frames with unread meshes " + o.get("framesWithUnreadMeshes")
            + ", late " + o.get("late") + " (since start: left out " + o.get("skippedSinceStart") + ", caught up " + o.get("catchUpsSinceStart")
            + ", on first use " + o.get("redrawnOnFirstUseSinceStart") + ", late " + o.get("lateSinceStart") + "), mesh read "
            + o.get("meshReadMsPerLayer") + " ms/layer";
    }

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
