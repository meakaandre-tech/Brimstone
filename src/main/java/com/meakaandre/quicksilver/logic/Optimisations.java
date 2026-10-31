package com.meakaandre.quicksilver.logic;

import java.util.Map;

/**
 * The Logic module's optimisations: their ids and descriptions (the status line and the config file), which mixins
 * belong to each ({@link LogicMixins}), and the hooks each must have landed before it changes anything.
 */
final class Optimisations {
    private Optimisations() {
    }

    /** Each optimisation, in the order the status line lists them. */
    static void register(Map<String, String> m) {
    }

    /** Each mixin class (simple name) and the optimisation it belongs to. */
    static String owner(String mixinSimpleName) {
        return null;
    }

    /** At server start: an optimisation that is on but whose hooks didn't land falls back (CI fails on it). */
    static void checkHooks() {
    }
}
