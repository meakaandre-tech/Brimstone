package com.meakaandre.brimstone.gui;

import com.google.gson.JsonObject;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.state.gui.GuiRenderState;

/**
 * What the game's GUI pass (26.3's {@code GuiRenderer}) draws per frame, counted while a bench measures: the draw calls
 * of its draw pass and of its preparation (item icons drawn into the item atlas, picture-in-picture renders), the
 * elements it meshes and, as the room batching could still win, two lower counts for the same elements:
 * <ul>
 *   <li>{@code statesPerFrame}: distinct (pipeline, textures, scissor) states per part of the pass (before and after
 *   the menu blur), the fewest draws any reordering could reach;</li>
 *   <li>{@code reorderedDrawsPerFrame}: the draws left if each element joins the latest earlier draw of its state that
 *   no element drawn in between overlaps on screen (so the picture cannot change), a batcher's realistic best; text
 *   glyphs carry no bounds and count as overlapping everything ({@code reorderedIgnoringTextPerFrame}: as overlapping
 *   nothing, more than any batcher could safely win).</li>
 * </ul>
 * The elements are read once per frame at the start of the draw pass, and only while a bench measures; outside it the
 * hooks only set the phase. Observes only; nothing here changes what is drawn. Every count is a plain field: the game
 * draws on the render thread only (CI: the counted GUI draws equal the game's listed draws in every bench).
 */
public final class GuiStats {
    private enum Phase { NONE, RENDER, DRAW }

    private static Phase phase = Phase.NONE;
    private static boolean window;
    private static long frames, guiFrames, drawCalls, prepareDrawCalls, listedDraws, elements, states, reordered, reorderedText;
    private static long pipelines, textures, scissors;

    /** One batch of the estimate: a state and the union of its elements' bounds. */
    private static final class Batch {
        final RenderPipeline pipeline;
        final TextureSetup textures;
        final ScreenRectangle scissor;
        int x0 = Integer.MAX_VALUE, y0 = Integer.MAX_VALUE, x1 = Integer.MIN_VALUE, y1 = Integer.MIN_VALUE;
        boolean unbounded;
        int count;

        Batch(RenderPipeline pipeline, TextureSetup textures, ScreenRectangle scissor) {
            this.pipeline = pipeline;
            this.textures = textures;
            this.scissor = scissor;
        }

        boolean same(RenderPipeline p, TextureSetup t, ScreenRectangle s) {
            return pipeline == p && Objects.equals(scissor, s) && Objects.equals(textures, t);
        }

        /** Whether the element's bounds meet this batch's; elements without bounds (text glyphs) meet everything. */
        boolean overlaps(ScreenRectangle r) {
            if (unbounded || r == null) return true;
            return count > 0 && r.left() < x1 && x0 < r.right() && r.top() < y1 && y0 < r.bottom();
        }

        /** The same, but elements without bounds meet nothing: the most any reordering could win. */
        boolean overlapsIgnoringUnbounded(ScreenRectangle r) {
            if (r == null || count == 0 || x1 < x0) return false;
            return r.left() < x1 && x0 < r.right() && r.top() < y1 && y0 < r.bottom();
        }

        void add(ScreenRectangle r) {
            count++;
            if (r == null) {
                unbounded = true;
                return;
            }
            x0 = Math.min(x0, r.left());
            y0 = Math.min(y0, r.top());
            x1 = Math.max(x1, r.right());
            y1 = Math.max(y1, r.bottom());
        }
    }

    /** Batches of the estimate in the current part of the pass, and the distinct states in it. */
    private static final List<Batch> BATCHES = new ArrayList<>();
    private static final List<Batch> STATES = new ArrayList<>();
    private static final List<Batch> BATCHES_TEXT = new ArrayList<>();
    /** Distinct pipelines, texture setups and scissor areas in the current part of the pass. */
    private static final List<Object> PIPELINES = new ArrayList<>(), TEXTURES = new ArrayList<>(), SCISSORS = new ArrayList<>();

    private GuiStats() {
    }

    /** Starts the bench's measuring window. */
    public static void startWindow() {
        frames = guiFrames = drawCalls = prepareDrawCalls = listedDraws = elements = states = reordered = reorderedText = 0;
        pipelines = textures = scissors = 0;
        window = true;
    }

    public static void stopWindow() {
        window = false;
    }

    /** {@code GuiRenderer.render} starts: item atlas and picture-in-picture draws until the draw pass. */
    public static void renderStart() {
        phase = Phase.RENDER;
    }

    public static void renderEnd() {
        phase = Phase.NONE;
        if (window) guiFrames++;
    }

    /**
     * The draw pass starts with the game's list of draws (one per run of elements with the same state) and its sorted
     * elements; while a bench measures, the elements are read here in the game's order, part by part.
     */
    public static void drawStart(int draws, GuiRenderState state) {
        phase = Phase.DRAW;
        if (!window) return;
        listedDraws += draws;
        part(state, GuiRenderState.TraverseRange.BEFORE_BLUR);
        part(state, GuiRenderState.TraverseRange.AFTER_BLUR);
    }

    /** One part of the pass (before the blur, after it): nothing is merged across parts. */
    private static void part(GuiRenderState state, GuiRenderState.TraverseRange range) {
        BATCHES.clear();
        BATCHES_TEXT.clear();
        STATES.clear();
        PIPELINES.clear();
        TEXTURES.clear();
        SCISSORS.clear();
        state.forEachElement(GuiStats::element, range);
        reordered += BATCHES.size();
        reorderedText += BATCHES_TEXT.size();
        states += STATES.size();
        pipelines += PIPELINES.size();
        textures += TEXTURES.size();
        scissors += SCISSORS.size();
    }

    public static void drawEnd() {
        phase = Phase.RENDER;
    }

    /** One element, in the game's order. */
    private static void element(GuiElementRenderState e) {
        elements++;
        RenderPipeline p = e.pipeline();
        TextureSetup t = e.textureSetup();
        ScreenRectangle s = e.scissorArea();
        ScreenRectangle r = e.bounds();
        boolean known = false;
        for (Batch b : STATES) {
            if (b.same(p, t, s)) {
                known = true;
                break;
            }
        }
        if (!known) STATES.add(new Batch(p, t, s));
        distinct(PIPELINES, p);
        distinct(TEXTURES, t);
        distinct(SCISSORS, s);
        join(BATCHES, p, t, s, r, false);
        join(BATCHES_TEXT, p, t, s, r, true);
    }

    private static void distinct(List<Object> seen, Object o) {
        for (Object x : seen) if (Objects.equals(x, o)) return;
        seen.add(o);
    }

    /** Puts an element into the latest batch of its state that no later batch it overlaps comes after, or a new one. */
    private static void join(List<Batch> batches, RenderPipeline p, TextureSetup t, ScreenRectangle s, ScreenRectangle r, boolean ignoreUnbounded) {
        // the latest batch this element overlaps: it must be drawn after that one
        int after = -1;
        for (int i = batches.size() - 1; i >= 0; i--) {
            Batch b = batches.get(i);
            if (ignoreUnbounded ? b.overlapsIgnoringUnbounded(r) : b.overlaps(r)) {
                after = i;
                break;
            }
        }
        for (int i = batches.size() - 1; i >= Math.max(0, after); i--) {
            Batch b = batches.get(i);
            if (b.same(p, t, s)) {
                b.add(r);
                return;
            }
        }
        Batch b = new Batch(p, t, s);
        b.add(r);
        batches.add(b);
    }

    /** A draw call reached the backend while the bench measures. */
    public static void onDraw() {
        if (phase == Phase.DRAW) drawCalls++;
        else if (phase == Phase.RENDER) prepareDrawCalls++;
    }

    /** A frame was presented. */
    public static void frameEnd() {
        phase = Phase.NONE;
        if (window) frames++;
    }

    public static JsonObject json() {
        JsonObject o = new JsonObject();
        o.addProperty("frames", frames);
        o.addProperty("framesWithGui", guiFrames);
        o.addProperty("drawCallsPerFrame", per(drawCalls));
        o.addProperty("drawsListedPerFrame", per(listedDraws));
        o.addProperty("prepareDrawCallsPerFrame", per(prepareDrawCalls));
        o.addProperty("elementsPerFrame", per(elements));
        o.addProperty("statesPerFrame", per(states));
        o.addProperty("reorderedDrawsPerFrame", per(reordered));
        o.addProperty("reorderedIgnoringTextPerFrame", per(reorderedText));
        o.addProperty("pipelinesPerFrame", per(pipelines));
        o.addProperty("textureSetupsPerFrame", per(textures));
        o.addProperty("scissorAreasPerFrame", per(scissors));
        o.addProperty("note", "the game's GUI pass (GuiRenderer): draw calls of its draw pass and of its preparation (item atlas, picture-in-picture); states = distinct pipeline/texture/scissor per part of the pass (fewest possible draws); reordered = draws if each element joined the latest earlier draw of its state with nothing overlapping it in between (text glyphs, which carry no bounds, counted as overlapping everything; IgnoringText: as overlapping nothing, an upper bound on what reordering could win); pipelines, texture setups, scissor areas = distinct ones per part of the pass (what makes the states differ); draws also count the menu blur's own draws, which the game runs inside its GUI draw pass (not listed)");
        return o;
    }

    public static String line(JsonObject o) {
        return "draws/frame " + o.get("drawCallsPerFrame") + " (listed " + o.get("drawsListedPerFrame") + ", preparation " + o.get("prepareDrawCallsPerFrame")
            + "), elements " + o.get("elementsPerFrame") + ", states " + o.get("statesPerFrame") + ", reordered " + o.get("reorderedDrawsPerFrame")
            + " (ignoring text " + o.get("reorderedIgnoringTextPerFrame") + "; pipelines " + o.get("pipelinesPerFrame") + ", texture setups "
            + o.get("textureSetupsPerFrame") + ", scissor areas " + o.get("scissorAreasPerFrame") + ")"
            + " (" + o.get("framesWithGui") + " of " + o.get("frames") + " frames)";
    }

    private static double per(long v) {
        return frames == 0 ? 0 : Math.round(100.0 * v / frames) / 100.0;
    }
}
