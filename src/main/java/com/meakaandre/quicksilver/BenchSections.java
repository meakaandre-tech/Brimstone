package com.meakaandre.quicksilver;

import com.google.gson.JsonObject;
import com.meakaandre.quicksilver.animation.Animation;
import com.meakaandre.quicksilver.bench.BenchModules;
import com.meakaandre.quicksilver.frapi.RendererStats;
import com.meakaandre.quicksilver.terrain.TerrainStats;
import com.meakaandre.quicksilver.terrain.TerrainStore;
import com.meakaandre.quicksilver.terrain.TranslucencyStats;
import com.meakaandre.quicksilver.visibility.VisibilityStats;

/**
 * The full jar's bench sections: what each of Quicksilver's modules reports (package 4b moved them out of the bench, so
 * the bench-only jar can carry the bench without the modules). Same JSON keys and log lines as before.
 */
final class BenchSections {
    private BenchSections() {
    }

    static void register() {
        BenchModules.jar = "quicksilver";
        BenchModules.arenas = () -> {
            TerrainStore.Snapshot s = TerrainStore.snapshot();
            return new long[] {s.arenaBytes(), s.usedBytes()};
        };
        BenchModules.register(BenchModules.section("meshing", TerrainStats::startWindow, f -> TerrainStats.meshingJson(), mesh ->
            mesh.get("mesher").getAsString() + " mesher; " + mesh.get("sections") + " sections, ms per section avg " + mesh.get("msPerSection") + " (mesher avg "
                + mesh.get("compileMsAvg") + ", p95 " + mesh.get("compileMsP95") + "; transcode or copy per layer " + mesh.get("transcodeMsPerLayer") + "); whole bench "
                + mesh.get("benchSections") + " sections, ms per section " + mesh.get("benchMsPerSection")));
        // package 4c: Quicksilver's own mesher (and the bench-wide meshing counts in "meshing")
        BenchModules.register(new BenchModules.Section() {
            @Override
            public String key() {
                return "mesher";
            }

            @Override
            public void startBench() {
                TerrainStats.startBench();
                com.meakaandre.quicksilver.mesher.MesherStats.startBench();
            }

            @Override
            public com.google.gson.JsonElement json(int frames) {
                return com.meakaandre.quicksilver.mesher.MesherStats.json();
            }

            @Override
            public String line(JsonObject o) {
                return com.meakaandre.quicksilver.mesher.MesherStats.line(o);
            }
        });
        BenchModules.register(BenchModules.section("terrain", null, TerrainStats::terrainJson, ter ->
            ter.get("state").getAsString() + " (" + ter.get("reason").getAsString() + "), fallbacks " + ter.get("fallbacks")
                + ", " + ter.get("vertexBytes") + "-byte vertices (vanilla " + ter.get("vanillaVertexBytes")
                + "; " + ter.get("positionRoundedPct") + "% with a position rounded to the grid), path " + ter.get("lastDrawPath").getAsString()
                + ", draws/frame " + ter.get("drawCallsPerFrame") + " for " + ter.get("sectionLayersPerFrame") + " section layers, " + ter.get("regions")
                + " regions in " + ter.get("arenas") + " arenas (" + ter.get("usedMB") + " of " + ter.get("arenaMB") + " MB used; vanilla's format would need "
                + ter.get("vanillaEquivalentMB") + " MB), layers taken " + ter.get("layersTaken") + ", kept by vanilla " + ter.get("layersKeptByVanilla")));
        BenchModules.register(BenchModules.section("backFaces", null, TerrainStats::backFacesJson, BenchSections::backFacesLine));
        BenchModules.register(BenchModules.section("uploadBudget", null, f -> TerrainStats.uploadBudgetJson(), BenchSections::uploadLine));
        BenchModules.register(BenchModules.section("visibility", VisibilityStats::startWindow, f -> VisibilityStats.json(), VisibilityStats::line));
        BenchModules.register(BenchModules.section("translucency", TranslucencyStats::startWindow, f -> TranslucencyStats.json(), TranslucencyStats::line));
        BenchModules.register(BenchModules.section("renderer", null, f -> RendererStats.json(), RendererStats::line));
        BenchModules.register(BenchModules.section("animation", Animation::startWindow, f -> Animation.json(), Animation::line));
    }

    private static String backFacesLine(JsonObject o) {
        return o.get("state").getAsString() + (o.get("state").getAsString().equals("on") ? "" : " (" + o.get("reason").getAsString() + ")")
            + ", triangles/frame " + o.get("trianglesPerFrame") + " of " + o.get("trianglesAllFacesPerFrame") + " with every face ("
            + o.get("trianglesLeftOutPct") + "% left out), index ranges per section layer " + o.get("rangesPerSectionLayer");
    }

    private static String uploadLine(JsonObject o) {
        return o.get("state").getAsString() + ", bytes/frame avg " + o.get("bytesPerFrame") + ", max in one frame " + o.get("maxBytesInOneFrame")
            + " (budget avg " + o.get("budgetBytesPerFrame") + "), sections waiting avg " + o.get("sectionsWaitingAvg") + ", max "
            + o.get("sectionsWaitingMax") + " (" + o.get("framesWithSectionsWaiting") + " frames), upload ms avg " + o.get("uploadMsAvg")
            + ", max " + o.get("uploadMsMax");
    }
}
