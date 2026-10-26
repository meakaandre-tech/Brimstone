package com.meakaandre.brimstone.frapi;

import static com.meakaandre.brimstone.frapi.QuadData.*;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadAtlas;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.ShadeMode;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/** A read view of one encoded quad ({@link QuadData}) in an {@code int[]} at {@link #base}. */
class QuadViewImpl implements QuadView {
    private static final ChunkSectionLayer[] LAYERS = ChunkSectionLayer.values();
    private static final TriState[] AO = {TriState.DEFAULT, TriState.TRUE, TriState.FALSE};
    private static final ItemStackRenderState.FoilType[] FOILS = ItemStackRenderState.FoilType.values();

    int[] data;
    int base;
    /** Face normal and light face, computed from the positions when first asked for after a change. */
    private final Vector3f faceNormal = new Vector3f();
    private Direction lightFace;
    protected boolean geometryDirty = true;

    QuadViewImpl(int[] data, int base) {
        this.data = data;
        this.base = base;
    }

    final int bits() {
        return data[base + BITS];
    }

    static Direction dir(int value) {
        return value == NO_DIR ? null : Direction.from3DDataValue(value);
    }

    private float f(int vertex, int field) {
        return Float.intBitsToFloat(data[vertex(base, vertex) + field]);
    }

    @Override
    public float x(int vertexIndex) {
        return f(vertexIndex, X);
    }

    @Override
    public float y(int vertexIndex) {
        return f(vertexIndex, Y);
    }

    @Override
    public float z(int vertexIndex) {
        return f(vertexIndex, Z);
    }

    @Override
    public float posByIndex(int vertexIndex, int coordinateIndex) {
        return f(vertexIndex, X + coordinateIndex);
    }

    @Override
    public Vector3f copyPos(int vertexIndex, Vector3f target) {
        if (target == null) target = new Vector3f();
        return target.set(x(vertexIndex), y(vertexIndex), z(vertexIndex));
    }

    @Override
    public int color(int vertexIndex) {
        return data[vertex(base, vertexIndex) + COLOR];
    }

    @Override
    public float u(int vertexIndex) {
        return f(vertexIndex, U);
    }

    @Override
    public float v(int vertexIndex) {
        return f(vertexIndex, V);
    }

    @Override
    public Vector2f copyUv(int vertexIndex, Vector2f target) {
        if (target == null) target = new Vector2f();
        return target.set(u(vertexIndex), v(vertexIndex));
    }

    @Override
    public int lightmap(int vertexIndex) {
        return data[vertex(base, vertexIndex) + LIGHT];
    }

    @Override
    public boolean hasNormal(int vertexIndex) {
        return (data[base + NORMALS] & (1 << vertexIndex)) != 0;
    }

    private float normal(int vertexIndex, int component) {
        return hasNormal(vertexIndex) ? unpackNormal(data[vertex(base, vertexIndex) + NORMAL], component) : Float.NaN;
    }

    @Override
    public float normalX(int vertexIndex) {
        return normal(vertexIndex, 0);
    }

    @Override
    public float normalY(int vertexIndex) {
        return normal(vertexIndex, 1);
    }

    @Override
    public float normalZ(int vertexIndex) {
        return normal(vertexIndex, 2);
    }

    @Override
    public Vector3f copyNormal(int vertexIndex, Vector3f target) {
        if (!hasNormal(vertexIndex)) return null;
        if (target == null) target = new Vector3f();
        return target.set(normalX(vertexIndex), normalY(vertexIndex), normalZ(vertexIndex));
    }

    private void computeGeometry() {
        if (!geometryDirty) return;
        // normal of the plane through the two diagonals
        float ax = x(2) - x(0), ay = y(2) - y(0), az = z(2) - z(0);
        float bx = x(3) - x(1), by = y(3) - y(1), bz = z(3) - z(1);
        float nx = ay * bz - az * by, ny = az * bx - ax * bz, nz = ax * by - ay * bx;
        float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (len != 0) faceNormal.set(nx / len, ny / len, nz / len);
        else faceNormal.set(0, 1, 0);
        lightFace = lightFace(faceNormal.x, faceNormal.y, faceNormal.z);
        geometryDirty = false;
    }

    /**
     * The face a normal points to most: its largest component, x before y before z when they are equal (within float
     * noise), as Indigo (the reference) picks it for a quad at 45 degrees, such as a cross model's.
     */
    private static Direction lightFace(float nx, float ny, float nz) {
        final float tie = 1.0e-5f;
        float ax = Math.abs(nx), ay = Math.abs(ny), az = Math.abs(nz);
        if (ax >= ay - tie && ax >= az - tie) return nx > 0 ? Direction.EAST : Direction.WEST;
        if (ay >= az - tie) return ny > 0 ? Direction.UP : Direction.DOWN;
        return nz > 0 ? Direction.SOUTH : Direction.NORTH;
    }

    @Override
    public Vector3fc faceNormal() {
        computeGeometry();
        return faceNormal;
    }

    @Override
    public Direction lightFace() {
        computeGeometry();
        return lightFace;
    }

    @Override
    public Direction nominalFace() {
        return dir(bits() >> NOMINAL_SHIFT & DIR_MASK);
    }

    @Override
    public Direction cullFace() {
        return dir(bits() >> CULL_SHIFT & DIR_MASK);
    }

    @Override
    public QuadAtlas atlas() {
        return (bits() & ATLAS_ITEM) != 0 ? QuadAtlas.ITEM : QuadAtlas.BLOCK;
    }

    @Override
    public ChunkSectionLayer chunkLayer() {
        return LAYERS[bits() >> LAYER_SHIFT & LAYER_MASK];
    }

    @Override
    public RenderType itemRenderType() {
        return renderType(data[base + ITEM_RT]);
    }

    @Override
    public RenderType itemGlintRenderType() {
        return renderType(data[base + GLINT_RT]);
    }

    @Override
    public RenderType itemGlintSpecialRenderType() {
        return renderType(data[base + GLINT_SPECIAL_RT]);
    }

    @Override
    public boolean emissive() {
        return (bits() & EMISSIVE) != 0;
    }

    @Override
    public Direction shadeDirectionOverride() {
        return dir(bits() >> SHADE_DIR_SHIFT & DIR_MASK);
    }

    @Override
    public TriState ambientOcclusion() {
        return AO[bits() >> AO_SHIFT & AO_MASK];
    }

    @Override
    public ItemStackRenderState.FoilType foilType() {
        int f = bits() >> FOIL_SHIFT & FOIL_MASK;
        return f == 0 ? null : FOILS[f - 1];
    }

    @Override
    public ShadeMode shadeMode() {
        return (bits() & SHADE_VANILLA) != 0 ? ShadeMode.VANILLA : ShadeMode.ENHANCED;
    }

    @Override
    public boolean animated() {
        return (bits() & ANIMATED) != 0;
    }

    @Override
    public int tintIndex() {
        return data[base + TINT];
    }

    @Override
    public int tag() {
        return data[base + TAG];
    }

    /** The normal written for vertices without their own: the light face's axis, as vanilla writes block quads. */
    protected Vector3fc bufferNormal() {
        return lightFace().getUnitVec3f();
    }

    /** As vanilla's {@code VertexConsumer.putBlockBakedQuad}: positions as they are, the face's axis as normal. */
    @Override
    public void buffer(int overlayCoords, VertexConsumer consumer) {
        Vector3fc face = bufferNormal();
        for (int i = 0; i < 4; i++) {
            float nx = face.x(), ny = face.y(), nz = face.z();
            if (hasNormal(i)) {
                nx = normalX(i);
                ny = normalY(i);
                nz = normalZ(i);
            }
            consumer.addVertex(x(i), y(i), z(i), color(i), u(i), v(i), overlayCoords, lightmap(i), nx, ny, nz);
        }
    }

    /**
     * As vanilla's {@code VertexConsumer.putBakedQuad}: positions through the pose, normals through its normal matrix;
     * vertices without their own normal get the quad's geometric normal (as Indigo writes them, which differs from
     * vanilla's face axis only for quads not facing along an axis, such as a cross model's).
     */
    @Override
    public void buffer(int overlayCoords, PoseStack.Pose pose, VertexConsumer consumer) {
        Matrix4f m = pose.pose();
        Vector3f faceNormal = pose.transformNormal(faceNormal(), new Vector3f());
        Vector3f p = new Vector3f(), n = new Vector3f();
        for (int i = 0; i < 4; i++) {
            m.transformPosition(copyPos(i, p), p);
            Vector3f nv = faceNormal;
            if (hasNormal(i)) nv = pose.transformNormal(normalX(i), normalY(i), normalZ(i), n);
            consumer.addVertex(p.x(), p.y(), p.z(), color(i), u(i), v(i), overlayCoords, lightmap(i), nv.x(), nv.y(), nv.z());
        }
    }
}
