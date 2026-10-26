# Brimstone CI comparison

**passed**

## Suite core

- vanilla-a: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-b: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- brimstone-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`

GPU error messages in the client logs (Brimstone run / vanilla run of the same profile):

- old-gpu: 0 / 0

**Quick mode** (core suite; vanilla a, vanilla b, Brimstone old-GPU against vanilla a): the measure-run gates (same-run draw calls, terrain GPU memory, measure pictures) and the full and Vulkan profiles are not run. A merge needs a full run.

| scene | noise (vanilla a/b) % | limit % | old-GPU % |
|---|---|---|---|
| spawn | 0.3018 | 10.05 | 0.054 |
| flythrough | 0.0065 | 14.95 | 0.0062 |
| water_glass | 0.0031 | 6.45 | 0.0042 |
| nether | 0.0 | 0.8 | 0.006 |
| mobs | 0.1086 | 0.4 | 0.0271 |
| occlusion | 0.0 | 0.25 | 0.0001 |
| occlusion_move | 0.0 | 0.25 | 0.0 |
| occlusion_move.x2297 | 0.0 | 0.25 | 0.0 |
| occlusion_move.x2305 | 0.0 | 0.25 | 0.0 |
| occlusion_move.x2309 | 0.0014 | 0.3 | 0.0035 |
| occlusion_move.open | 0.0 | 0.25 | 0.0 |
| occlusion_move.closed | 0.0 | 0.25 | 0.0 |
| occlusion_move.bare_x2300 | 0.0 | 0.3 | 0.0 |
| occlusion_move.bare_x2297 | 0.0 | 0.3 | 0.0 |
| occlusion_move.bare_x2305 | 0.0 | 0.25 | 0.0 |
| occlusion_move.bare_x2309 | 0.0 | 0.25 | 0.0 |
| occlusion_move.bare_open | 0.0 | 0.3 | 0.0 |
| translucent_move | 0.0002 | 0.3 | 0.0001 |
| translucent_move.x2404 | 0.0003 | 0.3 | 0.0005 |
| translucent_move.x2414 | 0.0001 | 0.3 | 0.0002 |
| translucent_move.x2424 | 0.0001 | 0.3 | 0.0002 |
| translucent_move.low | 0.0008 | 0.3 | 0.0015 |
| translucent_move.back | 0.0002 | 0.3 | 0.0003 |
| translucent_move.bare_back | 0.0 | 0.3 | 0.0 |
| water_glass_oit | 0.0038 | 0.3 | 0.0043 |
| translucent_move_oit | 0.0 | 0.3 | 0.0 |
| translucent_move_oit.x2404 | 0.0 | 0.3 | 0.0004 |
| translucent_move_oit.x2414 | 0.0 | 0.3 | 0.0004 |
| translucent_move_oit.x2424 | 0.0 | 0.3 | 0.0003 |
| translucent_move_oit.low | 0.0003 | 0.3 | 0.0009 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 |
| translucent_move_oit.bare_back | 0.0004 | 0.3 | 0.0005 |

Strict picture: pixels where both vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | old-GPU |
|---|---|---|---|
| spawn | 99.48 | 18057 | 298 (939) |
| flythrough | 99.99 | 6020 | 87 (358) |
| water_glass | 100.0 | 25508 | 31 (3516) |
| nether | 100.0 | 905 | 57 (518) |
| mobs | 99.89 | 600 | 108 (108) |
| occlusion | 100.0 | 22 | 1 (76) |
| occlusion_move | 100.0 | 10 | 0 (0) |
| occlusion_move.x2297 | 100.0 | 10 | 0 (0) |
| occlusion_move.x2305 | 100.0 | 10 | 0 (0) |
| occlusion_move.x2309 | 99.96 | 32 | 24 (44) |
| occlusion_move.open | 100.0 | 10 | 0 (0) |
| occlusion_move.closed | 100.0 | 10 | 0 (0) |
| occlusion_move.bare_x2300 | 100.0 | 90 | 0 (0) |
| occlusion_move.bare_x2297 | 100.0 | 70 | 0 (0) |
| occlusion_move.bare_x2305 | 100.0 | 10 | 0 (0) |
| occlusion_move.bare_x2309 | 100.0 | 10 | 0 (0) |
| occlusion_move.bare_open | 100.0 | 110 | 0 (0) |
| translucent_move | 100.0 | 13 | 2 (124) |
| translucent_move.x2404 | 100.0 | 15 | 6 (176) |
| translucent_move.x2414 | 100.0 | 13 | 3 (163) |
| translucent_move.x2424 | 100.0 | 13 | 2 (121) |
| translucent_move.low | 100.0 | 41 | 16 (22) |
| translucent_move.back | 100.0 | 13 | 5 (127) |
| translucent_move.bare_back | 100.0 | 13 | 2 (124) |
| water_glass_oit | 100.0 | 57 | 28 (3514) |
| translucent_move_oit | 100.0 | 12 | 3 (125) |
| translucent_move_oit.x2404 | 100.0 | 16 | 5 (174) |
| translucent_move_oit.x2414 | 100.0 | 10 | 7 (167) |
| translucent_move_oit.x2424 | 100.0 | 15 | 5 (124) |
| translucent_move_oit.low | 100.0 | 40 | 13 (19) |
| translucent_move_oit.back | 100.0 | 12 | 1 (123) |
| translucent_move_oit.bare_back | 100.0 | 15 | 2 (124) |

### Counts, core old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 158.0 (base 844.2) | 12 (base 13) | 1380 (base 6372) | 256.3 (base 256.29) | 3.99 (base 3.73) |
| flythrough | 176.94 (reported, not gated) | 50 (reported, not gated) | 579788 (reported, not gated) | 317.82 (base 324.79) | 4.24 (base 5.99) |
| water_glass | 135.0 (base 457.8) | 13 (base 14) | 7316 (base 10264) | 243.71 (base 249.83) | 3.99 (base 5.99) |
| nether | 138.95 (base 640.77) | 10 (base 11) | 4860 (base 10656) | 264.36 (base 272.57) | 3.99 (base 5.99) |
| mobs | 140.7 (base 410.19) | 15 (base 16) | 610676 (base 1015892) | 263.3 (base 268.23) | 3.99 (base 5.99) |
| occlusion | 99.33 (base 106.62) | 13 (base 14) | 98148 (base 124172) | 264.88 (base 264.64) | 3.99 (base 4.24) |
| occlusion_move | 101.03 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 265.33 (base 265.66) | 3.99 (base 5.99) |
| translucent_move | 133.0 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 267.93 (base 268.19) | 3.99 (base 3.73) |
| water_glass_oit | 140.0 (base 154.0) | 13 (base 14) | 7652 (base 7604) | 246.11 (base 245.78) | 3.99 (base 3.73) |
| translucent_move_oit | 140.0 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 248.71 (base 248.15) | 3.99 (base 3.73) |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 38.203 | 756.021 | 16 (vanilla 28) | 1345176 | 41.91 / 0.076 | 22.279 / 36.0 | 35.92 | 2245 / 0 | 4.241 (8.13) |
| flythrough | plain, one multi-draw per region and layer | 57.262 | 897.452 | 16 (vanilla 28) | 5087004 | 20.738 / 0.895 | 84.057 / 112.0 | 135.838 | 6579 / 0 | 7.27 (17.925) |
| water_glass | plain, one multi-draw per region and layer | 14.043 | 273.83 | 16 (vanilla 28) | 773344 | 33.904 / 0.075 | 12.454 / 36.0 | 20.651 | 2926 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.065 | 473.542 | 16 (vanilla 28) | 954560 | 28.495 / 0.171 | 15.206 / 20.0 | 25.49 | 3937 / 0 | 2.853 (4.099) |
| mobs | plain, one multi-draw per region and layer | 11.027 | 198.491 | 16 (vanilla 28) | 602956 | 25.804 / 0.149 | 9.647 / 16.0 | 16.101 | 4382 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 69.115 | 16 (vanilla 28) | 163160 | 24.62 / 0.142 | 2.597 / 20.0 | 4.357 | 4604 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.997 | 74.436 | 16 (vanilla 28) | 212528 | 23.284 / 0.134 | 3.399 / 20.0 | 5.675 | 4880 / 0 | 5.259 (11.707) |
| translucent_move | plain, one multi-draw per region and layer | 12.036 | 245.458 | 16 (vanilla 28) | 801104 | 20.065 / 0.121 | 12.979 / 28.0 | 21.392 | 5649 / 0 | 6.713 (19.103) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.089 | 280.559 | 16 (vanilla 28) | 771684 | 17.294 / 0.076 | 12.428 / 28.0 | 20.606 | 1476 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.092 | 259.293 | 16 (vanilla 28) | 773248 | 11.413 / 0.098 | 12.531 / 28.0 | 20.648 | 2836 / 0 | 6.733 (17.393) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 151.0 | 16.0 | 0.0 | 0 | 0 | 50 (0.03 / 0.05 / 0.06; 0) | 0 | 28388 / 0 / 0 |
| flythrough | 187.512 | 26.197 | 0.0 | 0 | 0 | 862 (0.038 / 0.049 / 8.122; 0) | 40 | 23814 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1320 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.015 / 0.033 / 0.056; 0) | 32 | 1194 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.147 / 0.201 / 2.412; 0) | 180 | 2331 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 540 | 0 (None / None / None; 0) | 0 | 720 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 525 | 0 (None / None / None; 0) | 0 | 1225 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 324.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.032 (0.447; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 429.866 | 1.0 (0.0, 0.0) | 1.701 (0.0, 0.0) | 0.002 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.149; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.136 (0.264; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.068 (0.328; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 50.347 | 10.0 (9.0, 2.353) | 6.0 (6.0, 2.28) | 0.061 (0.376; 0) | 0 | 2766 / 1496206 / 0 / 0 |
| translucent_move | 132.294 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 132.303 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |

