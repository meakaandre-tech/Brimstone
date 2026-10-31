package com.meakaandre.quicksilver.mesher;

import com.meakaandre.quicksilver.animation.Animation;
import java.util.Arrays;

/**
 * Which animated block-atlas sprites a section's quads show (package 8's "seen" test), looked up while the quads are
 * written instead of read back from the finished layer: each quad's UV centre, the same value package 8 reads from a
 * vanilla-format layer (the four corners' UVs added in order, times a quarter).
 */
final class SpriteScan {
    Object table;
    long[] found = new long[0];

    void begin() {
        table = Animation.meshTable();
        if (table != null) {
            int words = Animation.meshWords(table);
            if (found.length < words) found = new long[words];
            else Arrays.fill(found, 0L);
        }
    }

    /** As {@link #quad}, from the four corners' UVs already added up in order. */
    void quadSums(float uSum, float vSum) {
        Object t = table;
        if (t == null) return;
        Animation.meshFind(t, uSum, vSum, found);
    }

    void quad(float[] u, float[] v) {
        Object t = table;
        if (t == null) return;
        float us = 0, vs = 0;
        for (int c = 0; c < 4; c++) {
            us += u[c];
            vs += v[c];
        }
        Animation.meshFind(t, us, vs, found);
    }
}
