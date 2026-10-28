package com.meakaandre.quicksilver.visibility;

import com.google.gson.JsonObject;
import com.meakaandre.quicksilver.bench.GpuCounters;

/**
 * What the visibility module did while a bench measured ({@link GpuCounters#enabled}): per frame, the sections vanilla
 * found visible, the entities and block entities vanilla would draw, how many Quicksilver tested and hid, the time it
 * took; and the pop-in checks: section layers of vanilla's visible list that nobody drew, and hidden objects that a ray
 * from the camera still reaches ({@link VisibilityCheck}, CI only). Render thread only.
 */
public final class VisibilityStats {
    private static long frames, sectionsVisible;
    private static long entities, entitiesTested, entitiesCulled;
    private static long blockEntities, blockEntitiesTested, blockEntitiesCulled;
    private static long nanos, maxNanos, overBudgetFrames;
    private static boolean overBudgetThisFrame;
    private static long terrainFrames, sectionLayersChecked, sectionLayersMissing;
    static long verifiedObjects, verifiedRays, violations, unverifiedObjects;

    private VisibilityStats() {
    }

    /** A bench starts measuring. */
    public static void startWindow() {
        frames = sectionsVisible = 0;
        entities = entitiesTested = entitiesCulled = 0;
        blockEntities = blockEntitiesTested = blockEntitiesCulled = 0;
        nanos = maxNanos = overBudgetFrames = 0;
        overBudgetThisFrame = false;
        terrainFrames = sectionLayersChecked = sectionLayersMissing = 0;
        verifiedObjects = verifiedRays = violations = unverifiedObjects = 0;
    }

    static void endFrame(long spentNanos) {
        if (!GpuCounters.enabled) {
            overBudgetThisFrame = false;
            return;
        }
        nanos += spentNanos;
        maxNanos = Math.max(maxNanos, spentNanos);
        if (overBudgetThisFrame) overBudgetFrames++;
        overBudgetThisFrame = false;
    }

    static void frame(int visibleSections) {
        if (!GpuCounters.enabled) return;
        frames++;
        sectionsVisible += visibleSections;
    }

    static void entityConsidered() {
        if (GpuCounters.enabled) entities++;
    }

    static void entityTested() {
        if (GpuCounters.enabled) entitiesTested++;
    }

    static void entityCulled() {
        if (GpuCounters.enabled) entitiesCulled++;
    }

    static void blockEntityConsidered() {
        if (GpuCounters.enabled) blockEntities++;
    }

    static void blockEntityTested() {
        if (GpuCounters.enabled) blockEntitiesTested++;
    }

    static void blockEntityCulled() {
        if (GpuCounters.enabled) blockEntitiesCulled++;
    }

    static void overBudget() {
        overBudgetThisFrame = true;
    }

    /**
     * From Quicksilver's terrain each frame while measuring: of the solid and cutout section layers in vanilla's visible
     * list that have geometry, how many neither vanilla's buffers nor Quicksilver's draw list held (pop-in).
     */
    public static void terrainFrame(int checked, int missing) {
        if (!GpuCounters.enabled) return;
        terrainFrames++;
        sectionLayersChecked += checked;
        sectionLayersMissing += missing;
    }

    public static JsonObject json() {
        JsonObject o = new JsonObject();
        o.addProperty("state", Visibility.state().name().toLowerCase(java.util.Locale.ROOT));
        o.addProperty("reason", Visibility.reason());
        o.addProperty("fallbacks", Visibility.fallbacks());
        o.addProperty("dryRun", Visibility.DRY_RUN);
        o.addProperty("sections", "vanilla's frustum and cave graph (SectionOcclusionGraph), unchanged");
        o.addProperty("entityTypesAllowed", Visibility.ENTITY_MARGIN.size());
        o.addProperty("blockEntityTypesAllowed", Visibility.BLOCK_ENTITY_TYPES.size());
        o.addProperty("frames", frames);
        o.addProperty("sectionsVisiblePerFrame", per(sectionsVisible, frames));
        o.addProperty("entitiesPerFrame", per(entities, frames));
        o.addProperty("entitiesTestedPerFrame", per(entitiesTested, frames));
        o.addProperty("entitiesCulledPerFrame", per(entitiesCulled, frames));
        o.addProperty("entitiesCulled", entitiesCulled);
        o.addProperty("blockEntitiesPerFrame", per(blockEntities, frames));
        o.addProperty("blockEntitiesTestedPerFrame", per(blockEntitiesTested, frames));
        o.addProperty("blockEntitiesCulledPerFrame", per(blockEntitiesCulled, frames));
        o.addProperty("blockEntitiesCulled", blockEntitiesCulled);
        o.addProperty("cullMsPerFrame", frames == 0 ? 0 : round(nanos / 1e6 / frames));
        o.addProperty("cullMsMax", round(maxNanos / 1e6));
        o.addProperty("framesOverBudget", overBudgetFrames);
        o.addProperty("budgetMs", Visibility.BUDGET_NANOS / 1e6);
        o.addProperty("terrainFrames", terrainFrames);
        o.addProperty("sectionLayersCheckedPerFrame", per(sectionLayersChecked, terrainFrames));
        o.addProperty("sectionLayersMissing", sectionLayersMissing);
        o.addProperty("verify", Visibility.VERIFY);
        o.addProperty("verifiedObjects", verifiedObjects);
        o.addProperty("verifiedRays", verifiedRays);
        o.addProperty("unverifiedObjects", unverifiedObjects);
        o.addProperty("violations", violations);
        o.addProperty("violationsSinceStart", VisibilityCheck.totalViolations);
        o.addProperty("unverifiedSinceStart", VisibilityCheck.totalUnverified);
        o.addProperty("trackedSectionChanges", SectionChanges.size());
        o.addProperty("entityHookSeen", Visibility.entityHookSeen);
        o.addProperty("blockEntityHookSeen", Visibility.blockEntityHookSeen);
        return o;
    }

    public static String line(JsonObject v) {
        return v.get("state").getAsString() + " (" + v.get("reason").getAsString() + "), fallbacks " + v.get("fallbacks")
            + (v.get("dryRun").getAsBoolean() ? ", DRY RUN" : "") + ", sections visible/frame " + v.get("sectionsVisiblePerFrame")
            + ", entities/frame " + v.get("entitiesPerFrame") + " (tested " + v.get("entitiesTestedPerFrame") + ", hidden "
            + v.get("entitiesCulledPerFrame") + "), block entities/frame " + v.get("blockEntitiesPerFrame") + " (tested "
            + v.get("blockEntitiesTestedPerFrame") + ", hidden " + v.get("blockEntitiesCulledPerFrame") + "), ms/frame " + v.get("cullMsPerFrame") + " (max " + v.get("cullMsMax")
            + ", frames over budget " + v.get("framesOverBudget") + "), section layers missing " + v.get("sectionLayersMissing")
            + (v.get("verify").getAsBoolean() ? ", ray check: " + v.get("verifiedObjects") + " objects, " + v.get("verifiedRays") + " rays, "
            + v.get("violations") + " violations, " + v.get("unverifiedObjects") + " unchecked" : "");
    }

    private static double per(long v, long n) {
        return n == 0 ? 0 : round((double) v / n);
    }

    private static double round(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
