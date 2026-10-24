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
import com.mojang.renderpearl.api.pipeline.UniformType;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.resources.Identifier;

/**
 * Brimstone's terrain pipelines, built through renderpearl like the game's own: Brimstone's vertex shader
 * ({@code brimstone:core/terrain}, reads the 16-byte vertex) with the game's own terrain fragment shader, so the
 * pixel stage is vanilla's. Same depth test, culling, blending (none) and alpha cut-off as the game's solid and
 * cutout terrain pipelines. One set per vertex format (compact, wide).
 *
 * <p>Two families: PLAIN (per-region uniform block, drawn with direct multi-draws) and INSTANCED (section data from an
 * instance stream, drawn with multi-draw indirect where vanilla itself uses it).
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
        for (RenderPipeline p : java.util.Arrays.stream(PIPELINES).flatMap(java.util.Arrays::stream).flatMap(java.util.Arrays::stream).toList()) {
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
