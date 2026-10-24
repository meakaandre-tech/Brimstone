package com.meakaandre.brimstone.terrain;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompiledRenderPipeline;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PolygonMode;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.ShaderType;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.vertex.VertexFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.oit.OitStage;
import net.minecraft.resources.Identifier;

/**
 * Brimstone's terrain pipelines, built through renderpearl like the game's own: Brimstone's vertex shader
 * ({@code brimstone:core/terrain}, reads the 16-byte vertex) with the game's own terrain fragment shader, so the
 * pixel stage is vanilla's. Same depth test, culling, blending (none) and alpha cut-off as the game's solid and
 * cutout terrain pipelines. One set per vertex format (compact, wide).
 *
 * <p>Two families: PLAIN (per-region uniform block, drawn with direct multi-draws) and INSTANCED (section data from an
 * instance stream, drawn with multi-draw indirect where vanilla itself uses it).
 *
 * <p>Translucent (package 3): each of Brimstone's translucent pipelines is made from the game's own pipeline for the
 * same draw (classic translucent terrain, and each of the three Improved Transparency stages): the game's fragment
 * shader, defines, blending, colour targets, depth state, culling and uniform blocks are taken from it as they are;
 * only the vertex shader (Brimstone's), its vertex layout and, on the plain path, the region uniform block differ. So
 * whatever the game draws translucent terrain with, Brimstone draws its part with the same states.
 */
final class TerrainPipelines {
    static final BindGroupLayout REGION = BindGroupLayout.builder().withUniform("BrimRegion", UniformType.UNIFORM_BUFFER).build();
    /** std140: ivec4 origin, then 64 vec4 of fade-in values (256 sections). */
    static final int REGION_UBO_BYTES = 16 + 64 * 16;

    /** [format][plain ? 1 : 0][cutout ? 1 : 0]; format 0 compact, 1 wide. */
    private static final RenderPipeline[][][] PIPELINES = new RenderPipeline[2][2][2];
    /** [format][plain ? 1 : 0]. */
    private static final RenderPipeline[][] WIREFRAME = new RenderPipeline[2][2];

    static {
        for (int f = 0; f < 2; f++) {
            for (int p = 0; p < 2; p++) {
                for (int c = 0; c < 2; c++) PIPELINES[f][p][c] = build(f == 1, p == 1, c == 1, false);
                WIREFRAME[f][p] = build(f == 1, p == 1, false, true);
            }
        }
    }

    static RenderPipeline pipeline(int format, boolean plain, boolean cutout) {
        return PIPELINES[format][plain ? 1 : 0][cutout ? 1 : 0];
    }

    /** Translucent pipelines: [mode][format][plain ? 1 : 0]; mode 0 classic, 1 + stage ordinal for Improved Transparency. */
    private static RenderPipeline[][][] translucent;
    private static String translucentProblem;

    /** Mode of a translucent draw: {@link #CLASSIC}, or {@code 1 + OitStage.ordinal()}. */
    static final int CLASSIC = 0;

    private static synchronized String buildTranslucent() {
        if (translucent != null || translucentProblem != null) return translucentProblem;
        try {
            OitStage[] stages = OitStage.values();
            RenderPipeline[][][] t = new RenderPipeline[1 + stages.length][2][2];
            for (int f = 0; f < 2; f++) {
                for (int p = 0; p < 2; p++) {
                    t[CLASSIC][f][p] = derive(ChunkSectionLayer.TRANSLUCENT.pipeline(true), "translucent", f == 1, p == 1);
                    for (OitStage st : stages) {
                        t[1 + st.ordinal()][f][p] = derive(RenderPipelines.OIT_TERRAIN_MULTIDRAW.getPipeline(st),
                            "oit_" + st.name().toLowerCase(java.util.Locale.ROOT), f == 1, p == 1);
                    }
                }
            }
            translucent = t;
            return null;
        } catch (Throwable e) {
            translucentProblem = "the game's translucent terrain pipelines are not what Brimstone builds on: " + e;
            return translucentProblem;
        }
    }

    /**
     * Brimstone's version of one of the game's multi-draw terrain pipelines: everything as the game has it, but
     * Brimstone's vertex shader and vertex layout, and on the plain path no instance stream but the region block.
     */
    private static RenderPipeline derive(RenderPipeline v, String name, boolean wide, boolean plain) {
        if (v == null) throw new IllegalStateException("no pipeline for " + name);
        if (!v.getShaderDefines().flags().contains("MULTIDRAW_TERRAIN")) throw new IllegalStateException(v.getLocation() + " is not a multi-draw terrain pipeline");
        if (!DefaultVertexFormat.BLOCK.equals(v.getVertexFormatBinding(0)) || !DefaultVertexFormat.CHUNK_DATA_INSTANCED.equals(v.getVertexFormatBinding(1))) {
            throw new IllegalStateException(v.getLocation() + " does not read the BLOCK vertex with instanced section data");
        }
        Identifier fragment = v.getShaders().get(ShaderType.FRAGMENT);
        if (fragment == null) throw new IllegalStateException(v.getLocation() + " has no fragment shader");
        // the game's defines as they are (values and flags), through a snippet that carries nothing else
        RenderPipeline.Snippet defines = new RenderPipeline.Snippet(Map.of(), Optional.of(v.getShaderDefines()), Optional.empty(),
            new ColorTargetState[0], 0, Optional.empty(), Optional.empty(), Optional.empty(), new VertexFormat[0], Optional.empty(), 0);
        RenderPipeline.Builder b = RenderPipeline.builder(defines)
            .withLocation(Identifier.fromNamespaceAndPath("brimstone", "pipeline/terrain_" + name + (wide ? "_wide" : "_compact") + (plain ? "_plain" : "_instanced")))
            .withVertexShader(Identifier.fromNamespaceAndPath("brimstone", "core/terrain"))
            .withFragmentShader(fragment)
            .withVertexBinding(0, wide ? CompactVertex.WIDE_FORMAT : CompactVertex.FORMAT)
            .withPrimitiveTopology(v.getPrimitiveTopology())
            .withDepthStencilState(Optional.ofNullable(v.getDepthStencilState()))
            .withPolygonMode(v.getPolygonMode())
            .withCull(v.isCull())
            .withPushConstantSize(v.pushConstantSize());
        for (var layout : v.getBindGroupLayouts()) b.withBindGroupLayout(layout);
        List<ColorTargetState> targets = new ArrayList<>(v.getColorTargetStates());
        for (int i = 0; i < targets.size(); i++) {
            if (targets.get(i) == null) b.withUnusedColorTargetState(i);
            else b.withColorTargetState(i, targets.get(i));
        }
        if (plain) b.withShaderDefine("BRIMSTONE_PLAIN").withBindGroupLayout(REGION);
        else b.withVertexBinding(1, DefaultVertexFormat.CHUNK_DATA_INSTANCED);
        if (wide) b.withShaderDefine("BRIM_WIDE");
        return b.build();
    }

    /** The translucent pipeline for a draw mode; null if they could not be made (then the module is off). */
    static RenderPipeline translucent(int mode, int format, boolean plain) {
        if (buildTranslucent() != null) return null;
        return translucent[mode][format][plain ? 1 : 0];
    }

    /**
     * The mode of the translucent draw vanilla just made, from the pipeline overrides it passed: CLASSIC with none, the
     * Improved Transparency stage whose terrain pipeline it is, -1 for vanilla's wireframe view, -2 for anything else.
     */
    static int translucentMode(RenderPipeline override, RenderPipeline overrideMultidraw) {
        if (override == null && overrideMultidraw == null) return CLASSIC;
        if (override == RenderPipelines.WIREFRAME || overrideMultidraw == RenderPipelines.WIREFRAME_MULTIDRAW) return -1;
        for (OitStage st : OitStage.values()) {
            if (override == RenderPipelines.OIT_TERRAIN.getPipeline(st) || overrideMultidraw == RenderPipelines.OIT_TERRAIN_MULTIDRAW.getPipeline(st)) {
                return 1 + st.ordinal();
            }
        }
        return -2;
    }

    private static boolean wireframeFailed;

    private TerrainPipelines() {
    }

    private static RenderPipeline build(boolean wide, boolean plain, boolean cutout, boolean wireframe) {
        String name = (wide ? "wide" : "compact") + (plain ? "_plain" : "_instanced") + (wireframe ? "_wireframe" : cutout ? "_cutout" : "_solid");
        RenderPipeline.Builder b = RenderPipeline.builder()
            .withLocation(Identifier.fromNamespaceAndPath("brimstone", "pipeline/terrain_" + name))
            .withVertexShader(Identifier.fromNamespaceAndPath("brimstone", "core/terrain"))
            .withFragmentShader(Identifier.withDefaultNamespace("core/terrain"))
            // the game's fragment shader then reads section data from the vertex stage, as on its multi-draw path
            .withShaderDefine("MULTIDRAW_TERRAIN")
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.FOG)
            .withBindGroupLayout(BindGroupLayouts.TERRAIN_INFO)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withBindGroupLayout(BindGroupLayouts.SAMPLER2)
            .withVertexBinding(0, wide ? CompactVertex.WIDE_FORMAT : CompactVertex.FORMAT)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .withColorTargetState(ColorTargetState.DEFAULT);
        if (plain) {
            b.withShaderDefine("BRIMSTONE_PLAIN").withBindGroupLayout(REGION);
        } else {
            b.withVertexBinding(1, DefaultVertexFormat.CHUNK_DATA_INSTANCED);
        }
        if (wide) b.withShaderDefine("BRIM_WIDE");
        if (cutout) b.withShaderDefine("ALPHA_CUTOUT", 0.5f);
        if (wireframe) b.withPolygonMode(PolygonMode.WIREFRAME);
        return b.build();
    }

    /** Compiles the pipelines Brimstone always needs. Null if all compiled, else what failed. */
    static String compileAll() {
        String problem = buildTranslucent();
        if (problem != null) return problem;
        List<RenderPipeline> all = new ArrayList<>(java.util.Arrays.stream(PIPELINES).flatMap(java.util.Arrays::stream).flatMap(java.util.Arrays::stream).toList());
        all.addAll(java.util.Arrays.stream(translucent).flatMap(java.util.Arrays::stream).flatMap(java.util.Arrays::stream).toList());
        for (RenderPipeline p : all) {
            try {
                if (RenderSystem.getCompiledPipelineNullable(p) == null) return "pipeline " + p.getLocation() + " did not compile";
            } catch (Throwable t) {
                return "pipeline " + p.getLocation() + " did not compile: " + t;
            }
        }
        return null;
    }

    /** The compiled pipeline; throws if it no longer compiles (a resource reload broke it), which turns the module off. */
    static CompiledRenderPipeline compiled(RenderPipeline p) {
        CompiledRenderPipeline c = RenderSystem.getCompiledPipelineNullable(p);
        if (c == null) throw new IllegalStateException("pipeline " + p.getLocation() + " did not compile");
        return c;
    }

    /** Wireframe (a debug view) is optional: if it doesn't compile Brimstone's terrain is just left out of it. */
    static CompiledRenderPipeline wireframe(int format, boolean plain) {
        if (wireframeFailed || !RenderSystem.isWireframeAvailable()) return null;
        try {
            CompiledRenderPipeline c = RenderSystem.getCompiledPipelineNullable(WIREFRAME[format][plain ? 1 : 0]);
            if (c == null) wireframeFailed = true;
            return c;
        } catch (Throwable t) {
            wireframeFailed = true;
            com.meakaandre.brimstone.BrimstoneClient.LOG.warn("Brimstone terrain: wireframe pipeline unavailable, its terrain is left out of wireframe view", t);
            return null;
        }
    }
}
