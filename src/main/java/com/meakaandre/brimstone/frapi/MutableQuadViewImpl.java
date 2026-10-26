package com.meakaandre.brimstone.frapi;

import static com.meakaandre.brimstone.frapi.QuadData.*;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadTransform;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import org.joml.Vector3fc;

/**
 * One quad being built: Brimstone's {@link QuadEmitter}. {@link #emit()} runs the pushed transforms (last pushed
 * first), hands the quad to {@link #emitDirectly()} unless a transform dropped it, and resets it.
 *
 * <p>When the quad came from a vanilla {@link BakedQuad} ({@link #fromBakedQuad}) and nothing that vanilla's block
 * lighting reads has changed since, {@link #vanillaQuad()} returns that quad, so lighting runs on vanilla's own data;
 * while nothing a vanilla quad stores has changed, {@link #exactQuad()} returns it too (meshes keep it per quad), so a
 * vanilla quad drawn through a mesh needs no conversion back.
 */
abstract class MutableQuadViewImpl extends QuadViewImpl implements QuadEmitter {
    private final List<QuadTransform> transforms = new ArrayList<>();
    /** The vanilla quad this one was made from, while the positions and shade direction are still its own. */
    BakedQuad vanillaQuad;
    /** The vanilla quad this one is unchanged from, in everything a vanilla quad stores. */
    BakedQuad exactQuad;
    /** The sprite this quad's UVs come from (its vanilla quad's or a material bake's, UVs unchanged since), or null. */
    TextureAtlasSprite sprite;

    MutableQuadViewImpl() {
        super(new int[STRIDE], 0);
        clear();
    }

    protected abstract void emitDirectly();

    /** The vanilla quad this one still matches for block lighting, or null. */
    final BakedQuad vanillaQuad() {
        return vanillaQuad;
    }

    @Override
    final BakedQuad exactQuad() {
        return exactQuad;
    }

    @Override
    final TextureAtlasSprite knownSprite() {
        return sprite;
    }

    private void geometryChanged() {
        geometryDirty = true;
        vanillaQuad = null;
        exactQuad = null;
    }

    /** Drops every pushed transform (after a model threw between a push and its pop). */
    final void resetTransforms() {
        transforms.clear();
    }

    private void setBits(int shift, int mask, int value) {
        int b = data[base + BITS];
        data[base + BITS] = (b & ~(mask << shift)) | (value & mask) << shift;
    }

    private void flag(int flag, boolean on) {
        if (on) data[base + BITS] |= flag;
        else data[base + BITS] &= ~flag;
    }

    private static int dirValue(Direction d) {
        return d == null ? NO_DIR : d.get3DDataValue();
    }

    private void setF(int vertex, int field, float value) {
        data[vertex(base, vertex) + field] = Float.floatToRawIntBits(value);
    }

    @Override
    public QuadEmitter pos(int vertexIndex, float x, float y, float z) {
        setF(vertexIndex, X, x);
        setF(vertexIndex, Y, y);
        setF(vertexIndex, Z, z);
        geometryChanged();
        return this;
    }

    @Override
    public QuadEmitter color(int vertexIndex, int color) {
        data[vertex(base, vertexIndex) + COLOR] = color;
        return this;
    }

    @Override
    public QuadEmitter uv(int vertexIndex, float u, float v) {
        setF(vertexIndex, U, u);
        setF(vertexIndex, V, v);
        exactQuad = null;
        sprite = null;
        return this;
    }

    @Override
    public QuadEmitter lightmap(int vertexIndex, int lightmap) {
        int i = vertex(base, vertexIndex) + LIGHT;
        if (data[i] != lightmap) exactQuad = null; // a vanilla quad's light emission
        data[i] = lightmap;
        return this;
    }

    @Override
    public QuadEmitter normal(int vertexIndex, float x, float y, float z) {
        data[vertex(base, vertexIndex) + NORMAL] = packNormal(x, y, z);
        data[base + NORMALS] |= 1 << vertexIndex;
        exactQuad = null;
        return this;
    }

    @Override
    public QuadEmitter nominalFace(Direction face) {
        if (exactQuad != null && face != exactQuad.direction()) exactQuad = null;
        setBits(NOMINAL_SHIFT, DIR_MASK, dirValue(face));
        return this;
    }

    @Override
    public QuadEmitter cullFace(Direction face) {
        setBits(CULL_SHIFT, DIR_MASK, dirValue(face));
        nominalFace(face);
        return this;
    }

    @Override
    public QuadEmitter atlas(QuadAtlas quadAtlas) {
        if (quadAtlas != atlas()) {
            exactQuad = null;
            sprite = null;
        }
        flag(ATLAS_ITEM, quadAtlas == QuadAtlas.ITEM);
        return this;
    }

    @Override
    public QuadEmitter chunkLayer(ChunkSectionLayer layer) {
        if (layer != chunkLayer()) exactQuad = null;
        setBits(LAYER_SHIFT, LAYER_MASK, layer.ordinal());
        return this;
    }

    @Override
    public QuadEmitter itemRenderType(RenderType renderType) {
        setRenderType(ITEM_RT, renderType);
        return this;
    }

    @Override
    public QuadEmitter itemGlintRenderType(RenderType renderType) {
        setRenderType(GLINT_RT, renderType);
        return this;
    }

    @Override
    public QuadEmitter itemGlintSpecialRenderType(RenderType renderType) {
        setRenderType(GLINT_SPECIAL_RT, renderType);
        return this;
    }

    private void setRenderType(int field, RenderType renderType) {
        int id = renderTypeId(renderType);
        if (data[base + field] != id) exactQuad = null;
        data[base + field] = id;
    }

    @Override
    public QuadEmitter emissive(boolean emissive) {
        flag(EMISSIVE, emissive);
        return this;
    }

    @Override
    public QuadEmitter shadeDirectionOverride(Direction shadeDirection) {
        setBits(SHADE_DIR_SHIFT, DIR_MASK, dirValue(shadeDirection));
        vanillaQuad = null;
        exactQuad = null;
        return this;
    }

    @Override
    public QuadEmitter ambientOcclusion(TriState ao) {
        setBits(AO_SHIFT, AO_MASK, ao == TriState.TRUE ? 1 : ao == TriState.FALSE ? 2 : 0);
        return this;
    }

    @Override
    public QuadEmitter foilType(ItemStackRenderState.FoilType foilType) {
        setBits(FOIL_SHIFT, FOIL_MASK, foilType == null ? 0 : foilType.ordinal() + 1);
        return this;
    }

    @Override
    public QuadEmitter shadeMode(ShadeMode mode) {
        flag(SHADE_VANILLA, mode == ShadeMode.VANILLA);
        return this;
    }

    @Override
    public QuadEmitter animated(boolean animated) {
        flag(ANIMATED, animated);
        return this;
    }

    @Override
    public QuadEmitter tintIndex(int tintIndex) {
        if (data[base + TINT] != tintIndex) exactQuad = null;
        data[base + TINT] = tintIndex;
        return this;
    }

    @Override
    public QuadEmitter tag(int tag) {
        data[base + TAG] = tag;
        return this;
    }

    @Override
    public QuadEmitter postMaterialBake(Material.Baked material) {
        QuadEmitter.super.postMaterialBake(material);
        sprite = material.sprite();
        return this;
    }

    @Override
    public QuadEmitter copyFrom(QuadView quad) {
        if (quad instanceof QuadViewImpl q) {
            System.arraycopy(q.data, q.base, data, base, STRIDE);
            geometryChanged();
            if (q instanceof MutableQuadViewImpl m) {
                vanillaQuad = m.vanillaQuad;
                exactQuad = m.exactQuad;
                sprite = m.sprite;
            } else {
                // a mesh quad: what the mesh kept of it (lighting goes on the decoded quad, as before meshes kept it)
                exactQuad = q.exactQuad();
                sprite = q.knownSprite();
            }
            // nominal face is transient: a decoded quad's is its light face
            if (!(q instanceof MutableQuadViewImpl)) nominalFace(lightFace());
            return this;
        }
        clear();
        for (int i = 0; i < 4; i++) {
            pos(i, quad.x(i), quad.y(i), quad.z(i));
            color(i, quad.color(i));
            uv(i, quad.u(i), quad.v(i));
            lightmap(i, quad.lightmap(i));
            if (quad.hasNormal(i)) normal(i, quad.normalX(i), quad.normalY(i), quad.normalZ(i));
        }
        cullFace(quad.cullFace());
        nominalFace(quad.nominalFace());
        atlas(quad.atlas());
        chunkLayer(quad.chunkLayer());
        itemRenderType(quad.itemRenderType());
        itemGlintRenderType(quad.itemGlintRenderType());
        itemGlintSpecialRenderType(quad.itemGlintSpecialRenderType());
        emissive(quad.emissive());
        shadeDirectionOverride(quad.shadeDirectionOverride());
        ambientOcclusion(quad.ambientOcclusion());
        foilType(quad.foilType());
        shadeMode(quad.shadeMode());
        animated(quad.animated());
        tintIndex(quad.tintIndex());
        tag(quad.tag());
        return this;
    }

    @Override
    public QuadEmitter fromBakedQuad(BakedQuad quad) {
        BakedQuad.MaterialInfo info = quad.materialInfo();
        int light = LightCoordsUtil.pack(info.lightEmission(), info.lightEmission());
        for (int i = 0; i < 4; i++) {
            Vector3fc p = quad.position(i);
            setF(i, X, p.x());
            setF(i, Y, p.y());
            setF(i, Z, p.z());
            long uv = quad.packedUV(i);
            uv(i, UVPair.unpackU(uv), UVPair.unpackV(uv));
            color(i, -1);
            lightmap(i, light);
        }
        data[base + NORMALS] = 0;
        geometryDirty = true;
        nominalFace(quad.direction());
        TextureAtlasSprite s = info.sprite();
        QuadAtlas atlas = s != null ? QuadAtlas.ofLocation(s.atlasLocation()) : null;
        if (atlas != null) atlas(atlas);
        animated(s != null && s.contents().isAnimated());
        chunkLayer(info.layer());
        itemRenderType(info.itemRenderType());
        itemGlintRenderType(info.itemGlintRenderType());
        itemGlintSpecialRenderType(info.itemGlintSpecialRenderType());
        tintIndex(info.tintIndex());
        setBits(SHADE_DIR_SHIFT, DIR_MASK, dirValue(info.shadeDirectionOverride()));
        vanillaQuad = quad;
        exactQuad = quad;
        sprite = s;
        return this;
    }

    @Override
    public QuadEmitter clear() {
        java.util.Arrays.fill(data, base, base + STRIDE, 0);
        for (int i = 0; i < 4; i++) data[vertex(base, i) + COLOR] = -1;
        data[base + BITS] = NO_DIR << NOMINAL_SHIFT | NO_DIR << CULL_SHIFT | NO_DIR << SHADE_DIR_SHIFT
            | ChunkSectionLayer.CUTOUT.ordinal() << LAYER_SHIFT;
        data[base + TINT] = -1;
        int[] d = defaultRenderTypes();
        data[base + ITEM_RT] = d[0];
        data[base + GLINT_RT] = d[1];
        data[base + GLINT_SPECIAL_RT] = d[2];
        geometryChanged();
        sprite = null;
        return this;
    }

    private static volatile int[] defaultRenderTypes;

    /** The API's default item render types (cutout block item sheets), as ids. */
    private static int[] defaultRenderTypes() {
        int[] d = defaultRenderTypes;
        if (d == null) {
            d = new int[] {renderTypeId(Sheets.cutoutBlockItemSheet()), renderTypeId(Sheets.cutoutBlockItemGlintSheet()),
                renderTypeId(Sheets.cutoutBlockItemGlintSpecialSheet())};
            defaultRenderTypes = d;
        }
        return d;
    }

    @Override
    public void pushTransform(QuadTransform transform) {
        if (transform == null) throw new NullPointerException("quad transform");
        transforms.add(transform);
    }

    @Override
    public void popTransform() {
        transforms.removeLast();
    }

    @Override
    public QuadEmitter emit() {
        boolean keep = true;
        for (int i = transforms.size() - 1; keep && i >= 0; i--) keep = transforms.get(i).transform(this);
        if (keep) emitDirectly();
        clear();
        return this;
    }
}
