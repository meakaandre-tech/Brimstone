package com.meakaandre.brimstone.frapi;

import static com.meakaandre.brimstone.frapi.QuadData.STRIDE;

import java.util.Arrays;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;

/**
 * Brimstone's meshes: encoded quads ({@link QuadData}) back to back in one {@code int[]}, plus, per quad, the vanilla
 * quad it is unchanged from and the sprite its UVs come from where known (arrays made only once a quad has one), so a
 * vanilla quad drawn through a mesh needs no conversion or sprite lookup.
 */
final class Meshes {
    private Meshes() {
    }

    /** A view that walks a mesh's quads. */
    private static final class Cursor extends QuadViewImpl {
        private BakedQuad exact;
        private TextureAtlasSprite sprite;

        Cursor(int[] data) {
            super(data, 0);
        }

        void at(int base, BakedQuad exact, TextureAtlasSprite sprite) {
            this.base = base;
            this.exact = exact;
            this.sprite = sprite;
            loadGeometry();
        }

        @Override
        BakedQuad exactQuad() {
            return exact;
        }

        @Override
        TextureAtlasSprite knownSprite() {
            return sprite;
        }

        /** The API: a decoded quad's nominal face is its light face. */
        @Override
        public Direction nominalFace() {
            return lightFace();
        }
    }

    /**
     * An emitter over one quad slot of an {@code int[]} (a mutable mesh's quad, edited in place, or a block's pending
     * quad); {@link #emit()} does nothing (an in-place edit is not emitted, and must not clear the quad).
     */
    static final class SlotQuad extends MutableQuadViewImpl {
        /** {@code stored}: the slot's geometry is stored with it (a mesh quad), else worked out again when asked for. */
        void at(int[] data, int base, BakedQuad exact, TextureAtlasSprite sprite, boolean stored) {
            this.data = data;
            this.base = base;
            if (stored) loadGeometry();
            else geometryDirty = true;
            exactQuad = exact;
            this.sprite = sprite;
        }

        @Override
        protected void emitDirectly() {
        }

        @Override
        public QuadEmitter emit() {
            return this;
        }
    }

    private static BakedQuad at(BakedQuad[] a, int i) {
        return a == null ? null : a[i];
    }

    private static TextureAtlasSprite at(TextureAtlasSprite[] a, int i) {
        return a == null ? null : a[i];
    }

    private static void forEach(int[] data, BakedQuad[] exact, TextureAtlasSprite[] sprites, int size, Consumer<? super QuadView> action) {
        Cursor c = new Cursor(data);
        for (int i = 0; i < size; i++) {
            c.at(i * STRIDE, at(exact, i), at(sprites, i));
            action.accept(c);
        }
    }

    private static void outputTo(int[] data, BakedQuad[] exact, TextureAtlasSprite[] sprites, int size, QuadEmitter emitter) {
        Cursor c = new Cursor(data);
        for (int i = 0; i < size; i++) {
            c.at(i * STRIDE, at(exact, i), at(sprites, i));
            emitter.copyFrom(c);
            emitter.emit();
        }
    }

    static final class Immutable implements Mesh {
        static final Immutable EMPTY = new Immutable(new int[0], null, null, 0);
        private final int[] data;
        private final BakedQuad[] exact;
        private final TextureAtlasSprite[] sprites;
        private final int size;

        Immutable(int[] data, BakedQuad[] exact, TextureAtlasSprite[] sprites, int size) {
            this.data = data;
            this.exact = exact;
            this.sprites = sprites;
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void forEach(Consumer<? super QuadView> action) {
            Meshes.forEach(data, exact, sprites, size, action);
        }

        @Override
        public void outputTo(QuadEmitter emitter) {
            Meshes.outputTo(data, exact, sprites, size, emitter);
        }
    }

    static final class Mutable implements MutableMesh {
        private int[] data = new int[STRIDE * 8];
        private BakedQuad[] exact;
        private TextureAtlasSprite[] sprites;
        private int size;
        private final MutableQuadViewImpl emitter = new MutableQuadViewImpl() {
            @Override
            protected void emitDirectly() {
                add(this);
            }
        };

        private void add(MutableQuadViewImpl q) {
            q.computeGeometry(); // kept with the quad, as Indigo's meshes keep it
            if ((size + 1) * STRIDE > data.length) data = Arrays.copyOf(data, data.length * 2);
            System.arraycopy(q.data, q.base, data, size * STRIDE, STRIDE);
            int capacity = data.length / STRIDE;
            if (q.exactQuad != null && exact == null) exact = new BakedQuad[capacity];
            if (q.sprite != null && sprites == null) sprites = new TextureAtlasSprite[capacity];
            if (exact != null) {
                if (exact.length < capacity) exact = Arrays.copyOf(exact, capacity);
                exact[size] = q.exactQuad;
            }
            if (sprites != null) {
                if (sprites.length < capacity) sprites = Arrays.copyOf(sprites, capacity);
                sprites[size] = q.sprite;
            }
            size++;
        }

        @Override
        public QuadEmitter emitter() {
            emitter.clear();
            return emitter;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void forEach(Consumer<? super QuadView> action) {
            Meshes.forEach(data, exact, sprites, size, action);
        }

        @Override
        public void forEachMutable(Consumer<? super MutableQuadView> action) {
            SlotQuad c = new SlotQuad();
            for (int i = 0; i < size; i++) {
                c.at(data, i * STRIDE, at(exact, i), at(sprites, i), true);
                action.accept(c);
                // what an in-place edit left of the vanilla quad and sprite
                if (c.exactQuad != null && exact == null) exact = new BakedQuad[data.length / STRIDE];
                if (c.sprite != null && sprites == null) sprites = new TextureAtlasSprite[data.length / STRIDE];
                if (exact != null) exact[i] = c.exactQuad;
                if (sprites != null) sprites[i] = c.sprite;
            }
        }

        @Override
        public void outputTo(QuadEmitter emitter) {
            Meshes.outputTo(data, exact, sprites, size, emitter);
        }

        @Override
        public Mesh immutableCopy() {
            if (size == 0) return Immutable.EMPTY;
            return new Immutable(Arrays.copyOf(data, size * STRIDE), exact == null ? null : Arrays.copyOf(exact, size),
                sprites == null ? null : Arrays.copyOf(sprites, size), size);
        }

        @Override
        public void clear() {
            if (exact != null) Arrays.fill(exact, 0, size, null);
            if (sprites != null) Arrays.fill(sprites, 0, size, null);
            size = 0;
            emitter.clear();
        }
    }
}
