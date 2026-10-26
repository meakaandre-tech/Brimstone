package com.meakaandre.brimstone.frapi;

import static com.meakaandre.brimstone.frapi.QuadData.STRIDE;

import java.util.Arrays;
import java.util.function.Consumer;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableMesh;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadView;

/** Brimstone's meshes: encoded quads ({@link QuadData}) back to back in one {@code int[]}. */
final class Meshes {
    private Meshes() {
    }

    /** A view that walks a mesh's quads. */
    private static final class Cursor extends QuadViewImpl {
        Cursor(int[] data) {
            super(data, 0);
        }

        void at(int[] data, int base) {
            this.data = data;
            this.base = base;
            geometryDirty = true;
        }
    }

    /** A view that walks a mutable mesh's quads and can change them in place. */
    private static final class MutableCursor extends MutableQuadViewImpl {
        void at(int[] data, int base) {
            this.data = data;
            this.base = base;
            geometryDirty = true;
        }

        @Override
        protected void emitDirectly() {
            // in-place edits are not emitted
        }
    }

    private static void forEach(int[] data, int size, Consumer<? super QuadView> action) {
        Cursor c = new Cursor(data);
        for (int i = 0; i < size; i++) {
            c.at(data, i * STRIDE);
            action.accept(c);
        }
    }

    private static void outputTo(int[] data, int size, QuadEmitter emitter) {
        Cursor c = new Cursor(data);
        for (int i = 0; i < size; i++) {
            c.at(data, i * STRIDE);
            emitter.copyFrom(c);
            emitter.emit();
        }
    }

    static final class Immutable implements Mesh {
        static final Immutable EMPTY = new Immutable(new int[0], 0);
        private final int[] data;
        private final int size;

        Immutable(int[] data, int size) {
            this.data = data;
            this.size = size;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void forEach(Consumer<? super QuadView> action) {
            Meshes.forEach(data, size, action);
        }

        @Override
        public void outputTo(QuadEmitter emitter) {
            Meshes.outputTo(data, size, emitter);
        }
    }

    static final class Mutable implements MutableMesh {
        private int[] data = new int[STRIDE * 8];
        private int size;
        private final MutableQuadViewImpl emitter = new MutableQuadViewImpl() {
            @Override
            protected void emitDirectly() {
                if ((size + 1) * STRIDE > Mutable.this.data.length) Mutable.this.data = Arrays.copyOf(Mutable.this.data, Mutable.this.data.length * 2);
                System.arraycopy(this.data, this.base, Mutable.this.data, size * STRIDE, STRIDE);
                size++;
            }
        };

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
            Meshes.forEach(data, size, action);
        }

        @Override
        public void forEachMutable(Consumer<? super MutableQuadView> action) {
            MutableCursor c = new MutableCursor();
            for (int i = 0; i < size; i++) {
                c.at(data, i * STRIDE);
                action.accept(c);
            }
        }

        @Override
        public void outputTo(QuadEmitter emitter) {
            Meshes.outputTo(data, size, emitter);
        }

        @Override
        public Mesh immutableCopy() {
            return size == 0 ? Immutable.EMPTY : new Immutable(Arrays.copyOf(data, size * STRIDE), size);
        }

        @Override
        public void clear() {
            size = 0;
            emitter.clear();
        }
    }
}
