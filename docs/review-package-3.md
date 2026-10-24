# Review: Brimstone package 3 (translucency), branch `package-3`

Reviewer: independent. I did not see the work being written. Sources: `git diff main...package-3` (9 commits,
6f491b9..13f56fb), docs/progress.md (Package 3), docs/ci-limits.md, docs/terrain-research.md section 6a, the game
bytecode dump, ci-logs for run 40130610610 (13f56fb, docs-only, same code as 6d48fc7), and the agent's saved
ci-logs extracts of runs 40119107821 through 40130610610 (`scratchpad/p3/cl2..cl7`, each checked against its
`commit.txt`). I read the screenshots and computed the pixel diffs myself. I did not modify the repo.

## Summary

The code is sound. Translucent layers keep vanilla's sorted index bytes. Vanilla's own worker-thread resorts are
taken and rewritten in place. The draw order is vanilla's visible list, reversed when vanilla sorts, and only
consecutive sections are batched. The OIT stages use pipelines built from the game's own. Every error path goes
through `Terrain.fail`. The per-frame check found 0 misses in every scene, profile and suite. The translucent shots
match vanilla under both gates. No existing limit, baseline or check was changed. The trailers are present on every
commit, and I found no raw GL or Vulkan calls, no vendor-specific code and no copied code.

Open items:
- The draw-call gate decision for the hologram scene (see "Decision for Andre").
- A count regression against vanilla on full and Vulkan. The design doc says counts must go down.
- A hologram test that cannot see the one place where the hologram meets Brimstone's translucent water.
- A few weak spots in the new checks.

## Done-when check

"Water, glass, ice and Build Gun holograms sorted correctly when the camera moves; sorting off the main thread."

| item | verdict | evidence |
|---|---|---|
| Quad order inside a section equals vanilla's, ties included | met | `Terrain.takeTranslucent` copies vanilla's index list byte for byte (`memCopy`) and asserts `indexCount == quads*6` and `bytes == count*type.bytes`. The layer's vertices go through `CompactVertex.split`, which returns one format for the whole layer in the original vertex order, so the indices stay valid with `baseVertex` set to the part's start. |
| Order between sections | met | `buildTranslucentRuns` walks vanilla's visible list, reversed when `sortTranslucent` is set (vanilla's own argument, taken in both `prepareChunkRenders*` hooks). A run only grows while the next section shares format, arena, index type and (plain path) region. Indirect: one MDI per run, which runs its commands in order. Plain: `multiDrawIndexed` with per-section offsets, in order. |
| Correct while moving | met | translucent_move: 180 vanilla resorts per bench, 180 lists uploaded, 2,233-2,828 section layers checked, 0 misses (all profiles). The six fixed-point shots plus the main shot are at most 7 strict px against limits of 10/20, in line with vanilla a/b noise (report.md, run 40130610610). I viewed `translucent_move.png` and `.low.png`: the overlapping stained glass, water and ice look the same as vanilla's. |
| Sorting off the main thread | met | Vanilla's `ResortTransparencyTask` still sorts on vanilla's workers. Brimstone only copies the result there and writes it at vanilla's end-of-frame upload. `sortsOnRenderThread` is 0 in every scene and profile. |
| Build Gun holograms | evidenced but weakly gated | It stays on Create's outliner path, which Brimstone does not touch. Pixel equality holds in four runs: full against vanilla-a 28-36 px at 8/255 when both have the same outliner brightness state, and ~2,400 px when they differ, as vanilla a/b also do (2,204-2,213 px in runs 6 and 7). But see M1: the gates cannot see an error confined to the outline over the water, the camera does not move, and Vulkan is not covered. |

## Findings

### B1 (BLOCKER until Andre accepts it as a deviation): translucent draw calls rise above vanilla's on full and Vulkan

**Evidence.**
- On the indirect path vanilla draws all translucent terrain with about one MDI per pass. Brimstone makes one per
  run, and runs break whenever the format changes (compact/wide), and also on an arena or index-type change.
  Report for run 40130610610, Translucency tables:

  | scene | vanilla draws | Brimstone draws (full) | Brimstone draws (Vulkan) |
  |---|---|---|---|
  | translucent_move | ~1 | 4 | 4 |
  | translucent_move_oit | ~3 | 12 | 12 |
  | fly-through | ~1 | 30.3 | 29.1 |

- Totals against the vanilla-equivalent baselines (dry run: translucent left with vanilla):
  - translucent_move: 129.0 vs 122.86 (full), 129.0 vs 126.0 (Vulkan).
  - translucent_move_oit: 140.0 vs 131.0 (both).
  - fly-through on full: 157.18 vs 123.08.
- None of these is gated. The scenes are memory-gated only (`ciGatedCounts`; the fly-through since package 0), so
  CI cannot see this.
- The doc's "draw calls must go down against vanilla" was relaxed in package 1 to "don't go down on full/Vulkan"
  (progress.md, package 1 deviations). Package 3 makes them go up there.
- The plain path (the real path) improves a lot: spawn 150→14, fly-through 186→42 translucent draws on old-GPU.

**Fix (Andre's choice).**
1. Accept the deviation explicitly. The author lists it under "Choices Andre should confirm".
2. Or store every translucent layer in the wide format. Runs would then break only at arena, index-type and (plain)
   region changes. The cost is 12 more bytes per vertex for translucent layers that are compact today (water is
   already wide).
3. Or keep the formats and gate translucent draws per frame on the indirect profiles, so the number cannot grow
   unseen.

### M1 (MAJOR): the hologram scene cannot catch an error where the hologram overlaps Brimstone's water

**Evidence (run 40130610610 screenshots, computed).**
- The outline covers 14,360 px, which is the mask where vanilla a and b differ through the outliner's brightness
  flip. Of these, 3,881 px are over the water, 7,144 over stone and 3,335 over sky.
- The 8/255 limit of 0.55% is 5,068 px. An error confined to the outline over the water (the outline hidden by the
  water, or the water blended over it) changes at most ~3,900 px, so it passes the 8/255 gate.
- The strict gate is blind there. Its limit of 21,840 px is larger than the whole outline, as the author documents.
- The camera does not move in this scene (no `velocity`), although the done-when says "when the camera moves".
  Vulkan is skipped; I verified the lavapipe SIGSEGV at `libvulkan_lvp.so+0xf68e0` in vanilla-vk as well as
  brimstone-vk in runs 40115084815 and 40119107821, so the skip itself is justified.
- The limits follow the documented rule, so this is a coverage gap, not a rule break.

**Fix.**
- Recompose the scene so that most of the outline is drawn over water: look down into the pool, or aim the Build
  Gun at a spot over the water.
- Find and pin the outliner's two-state brightness in the CI pin mod, as was done for the lightmap flicker and the
  texture animation, so that the strict gate can resolve the outline.
- Optionally move the camera during the bench.
- Then run a new marked SIFTEC collection run and set limits by the rule. No existing limit is raised by doing this.

### M2 (MAJOR, decision): the hologram draw-call gate fails depending on the runner's speed

This is the open decision; it is analysed in "Decision for Andre" below. Brimstone does not raise draw calls here:
its own draws are constant in every run (terrain 2.003-2.004, translucent 1.0 per frame on full).

### m1 (MINOR): draw calls in the two move scenes are ungated without reason

- The stated reason for memory-only gating is that resort uploads depend on the runner's timing. That applies to
  uploads, not to draw calls.
- translucent_move_oit does no sorting at all, and its draw count is exact (131.0 in the baseline, 140.0 now in every
  GPU profile). translucent_move is also exact on full and Vulkan (129.0).
- If draw calls were gated, everything would pass today: limits 137.1 / 157.3 / 140.6 / 146.1 / 179.1 against
  measured 129 / 140 / 129 / 140 / 155.
- **Fix:** gate draw calls in both scenes (this tightens a check, so the rules allow it). This would also make B1
  visible to CI.

### m2 (MINOR): the "Improved Transparency in every measured frame" check counts stage draws, not frames

- `TranslucencyStats.oitFrame()` runs once per stage draw (3 per frame): oitFrames is 519 for 173 frames.
- The gate in `compare.py` is `oitFrames < framesMeasured`. It still passes if OIT is on in only a third of the
  frames.
- **Fix:** count frames with at least one stage draw (or require `>= 3 x frames`), and rename the field.

### m3 (MINOR): what TranslucencyCheck cannot see

1. **Latest sort.** The check compares the mesh's `TranslucencyPointOfView`. Vanilla's `scheduleResort`
   (bytecode3.txt ~103556) re-sorts nearby sections on every block crossing even when the point of view is
   unchanged (it is forced for nearby sections and for axis-aligned views). A stale list with the same point of view
   is invisible to the check.
2. **Back to front.** Each list is checked against the camera it was sorted for, not the current one, so (1) is not
   covered here either.
3. **Brimstone's own copy.** It reads Brimstone's CPU copy of the list (the author notes this). It also does not
   check the offsets and base vertices put into the draw commands against `Entry.index` and `Part`.
4. **Section order.** The expected list is built from the same `ENTRIES` and the same `sortTranslucent` flag as the
   draw list. So it verifies batching and reversal, not independence from Brimstone's state. Set coverage rests on
   the pop-in audit (now including translucent) and on `mixedFrames`, which is adequate.

The practical risk is low: `stageIndices` decides "held" under the lock from `ENTRIES` and `STAGED`, and I found no
path that drops a resort for a live held mesh outside hand-back.

**Fix:** record vanilla's sort camera per mesh (already captured in `sortCamera`) and compare it with the camera of
the list Brimstone holds. Also compare the TRun offsets with `e.index.offset()`.

### m4 (MINOR): `ciSkipRuns` accepts any run names

- `compare.py` drops each profile whose Brimstone or vanilla run is listed. Listing `brimstone-full` would silently
  drop the full picture, strict and count gates for that scene. Listing `vanilla-a` would make `strict()` raise
  `KeyError`.
- It is per scene and visible in review, and today it lists only the Vulkan pair with a verified reason.
- **Fix:** fail unless the list is exactly one profile's pair and gives a `ciSkipReason`.

The other new switches are safe:
- `TRANSLUCENCY_DRY_RUN` is caught: `compare.py` fails any bench with `dryRun`, and the workflow no longer sets it
  (the net workflow diff against main is only the branch trigger).
- `clientOptions` apply to vanilla and Brimstone sessions alike, and scenes without them form the same sessions as
  before.
- A missing `translucency` block, `verify` false or any miss fails the run.

### N1 (NOTE): the draw-call average follows the frame rate

- docs/ci-limits.md says the average "held within 0.1%". That was true while scenes ran at 30 fps.
- Across six runs the hologram fits `avg = 27.6 + 137.2 x min(1, 20/fps)` within ±0.6. Frames with a game tick draw
  ~165 calls, frames without one ~28.
- The full gate (133.1) fails below about 26 fps.
- The Sift (11-27 fps here) has the same property, but there its baseline came from slow runs, so a fast runner only
  loosens its gate.

### N2 (NOTE): direct-buffer headroom on the hologram scene

- The direct-buffer baselines also come from one sample.
- Observed values: 4.24-8.24 MB on full (limit 8.66) and 3.73 MB baseline against 4.24-6.24 MB on old-GPU
  (limit 8.10). One more 2 MB step on either would fail.
- This is the same class of single-sample flakiness as M2, and the data does not show it is caused by Brimstone.

### N3 (NOTE): Improved Transparency with more than one 128 MiB heap

Vanilla would group by heap there; Brimstone keeps the list order. OIT is order-independent apart from the
float-accumulation order, so expect rounding-level differences at most. This case is untested and documented.

### N4 (NOTE): resorts during hand-back

`take` returns false once `takingMeshes()` is false, so during a hand-back a resort for a mesh Brimstone still draws
(`keepDrawing`) goes to vanilla. Brimstone may then draw that section with its previous list for those frames. This
happens only on the fallback path, which already fails CI.

### N5 (NOTE): documentation

- ci-limits.md says "Every sample was at most 0.001%", but water_glass_oit's samples are 0.003-0.0037%. The limits
  are still right.
- The branch-only `package-3` workflow trigger (dced55f) must be stripped at merge, as the hand-off says.

### N6 (NOTE): clean room, trailers, minimality, crash safety

- **Clean room and trailers:** all 9 commits carry both trailers. 168102e says "No idea taken from another mod". No
  game files or vanilla shader text are added: `terrain.vsh` only `#include`s the game's GLSL by reference and adds
  an `OIT_ALPHA_ONLY` branch.
- **Backend and platform:** no `org.lwjgl.opengl`/`vulkan` and no vendor or OS checks in the diff. The new drawing
  uses renderpearl only.
- **Window and options:** nothing touches the window or input. On a laptop the bench sets and then restores
  `improvedTransparency`.
- **Crash safety:** every new path fails into `Terrain.fail` with one ERROR line and terrain handed back:
  `takeTranslucent`, `rewriteIndices` (mismatch throws inside the guarded `Terrain.upload`), `drawTranslucent`
  (including an unknown pipeline override) and the pipeline build in `compileAll`. A layer Brimstone cannot hold
  turns the module off, by design.
- **Minimality:** the additions are what the package needs: the stats and check are debug or bench, and the
  `clientOptions`, `ciKeys` and `ciSkipRuns` switches serve the new scenes.
- **Memory:** staged index copies are freed on every path (`Staged.free`, the `finally` in `upload`, `dropStaged`,
  `clear`).
- **Thread safety:** the store's lock covers the worker-side `stageIndices`, the render-side
  `prepare`/`TranslucencyCheck.frame` and the end-of-frame `rewriteIndices`. Allocations are reused only in
  `upload`, after the frame's draws.

## Decision for Andre: the hologram draw-call gate

**Facts, verified from the bench JSON of runs 40119107821 to 40130610610:**

| run | profile | mode | fps | draw calls/frame (avg) | median (p50) |
|---|---|---|---|---|---|
| 40119107821 | full | dry | 21.69 | 153.63 | 166 |
| 40121527770 | full | dry | 23.71 | 143.09 | 166 |
| 40123696828 | full | dry | 29.96 | 119.16 | 166 |
| 40126212181 | full | real | 29.99 | 118.92 | 166 |
| 40128444322 | full | real | 23.65 | 144.24 | 166 |
| 40130610610 | full | real | 23.98 | 142.45 | 166 |

- **Old-GPU:**
  - dry runs 159.13 @ 21.4 fps, 123.16 @ 29.9 fps and 155.07 @ 22.2 fps (median 170);
  - real runs 124.18 @ 28.6 fps, 148.72 @ 22.8 fps and 146.9 @ 23.2 fps (median 167).
- **Brimstone's own draws:** constant, terrain 2.003-2.004 and translucent 1.0 on full.
- **Dry against real at the same fps:** 119.16 vs 118.92, so Brimstone adds nothing in this scene. The failure is
  the frame rate, as the author says.
- **Baseline and threshold:** the baseline (119.16) is the one 30 fps sample, and the gate fails whenever the
  runner draws this scene below about 26 fps.
- The setup has not changed since 4cb5419 (run 40123696828), so runs 4 to 7 are all samples of the current scene.

**Options and trade-offs:**

1. **Baseline from more clean runs** (the documented rule: the highest value across the clean runs after the last
   setup change).
   - Full would become 144.24 (run 40128444322). Old-GPU stays at 155.07.
   - This raises a baseline, which your rules forbid unless you decide it.
   - It does not fully fix the problem. At 20 fps or below the average is ~165, over 144.24 x 1.1 + 2 = 160.7. Only a
     sample taken at about 21 fps or lower makes it stable.
   - At 30 fps the gate would then have about 35% of slack.
2. **Gate this scene on the median.**
   - The p50 is 166 on full in all six runs, dry and real. With maxFps 30 and 20 ticks/s at least two thirds of the
     frames carry a tick, so the median is always the tick-frame count.
   - A baseline from the collection run's own p50 (166 full / 170 old-GPU) raises no number for the metric being
     gated. It does change what is gated for one scene (a `compare.py` change), which is your call.
   - Sensitivity is unchanged for Brimstone: its draws happen in every frame.
   - The cost is that a regression only on frames without a tick would not show.
3. **Report the draw calls only** (memory-gated, like the fly-through and occlusion_move).
   - Simplest. SIFTEC draw calls stay gated in The Sift, HUB and MAM.
   - It switches off a gate. Your rules forbid that unless you decide it (this gate was never merged).
4. **Make the frame pacing deterministic.**
   - Give the scene `clientOptions: {"maxFps": "20"}` (the per-session mechanism added in this package). At 20 fps
     or below every frame has a tick, so the average is the tick-frame count (~165) whatever the runner's speed.
     Every run reached at least 21.7 fps, and The Sift at 15 fps shows avg = p50 = max = 156.
   - This changes the scene's setup, so the documented rule calls for a new marked SIFTEC collection run, with new
     pixel and count limits from it. No existing limit is raised, but the new baseline (~165) is numerically higher
     than 119 for what you may see as the same scene.
   - The laptop bench would need maxFps applied too: `BenchRun` applies only `improvedTransparency` today.
   - It adds one more client session of about a minute.
5. **Fix it in the bench for every scene.**
   - `FrameRecorder` would record draw calls separately for frames with and without a client tick, and `compare.py`
     would gate the tick-frame average.
   - This is deterministic everywhere and also removes the latent frame-rate dependence in The Sift and MAM.
   - It is a larger change that re-bases every draw-call baseline, so it is probably its own small package.
6. **In addition to any of the above:** gate Brimstone's own draws, `terrain.drawCallsPerFrame` plus
   `translucency.drawCallsPerFrame`. These do not depend on the frame rate (2.003-2.004 + 1.0 in every run). Adding a
   check is within your rules.

Not an option: rerunning CI until a 30 fps runner turns it green. That passes by luck and hides the property.

Options 4 and 6, and 5 if you want it, stay inside your rules as written: a scene change with new limits from noise
by the documented rule, plus added checks. Options 1, 2 and 3 change a baseline or what is gated, so only you can
choose them.

## Verdict

**has blocking findings.**

- B1 is the translucent draw-call increase over vanilla on full and Vulkan, against the doc's "draw calls must go
  down". It blocks until you accept it as a deviation or choose a fix.
- M2, the hologram draw-call gate, is open for your decision.
- M1 (hologram coverage) should be fixed before merge.

Apart from these, the package meets its done-when, and nothing in it breaks the CI rules or the clean-room rules.
