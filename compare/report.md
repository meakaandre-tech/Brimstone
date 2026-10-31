# Quicksilver CI comparison

**passed**

## SIFTEC: report only

Until SIFTEC starts with MapMod's latest build again, SIFTEC's suites fail nothing (docs/progress.md, open items). Core and Terralith gate as usual.

SIFTEC started in every run.

No SIFTEC failures.


## Suite core

- vanilla-a: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-b: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- quicksilver-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`

GPU error messages in the client logs (Quicksilver run / vanilla run of the same profile):

- old-gpu: 0 / 0

**Quick mode** (core suite; vanilla a, vanilla b, Quicksilver old-GPU against vanilla a): the measure-run gates (same-run draw calls, terrain GPU memory, measure pictures) and the full and Vulkan profiles are not run. A merge needs a full run.

| scene | noise (vanilla a/b) % | limit % | old-GPU % |
|---|---|---|---|
| spawn | 0.0667 | 10.05 | 0.4399 |
| flythrough | 0.0 | 14.95 | 0.0011 |
| water_glass | 0.0038 | 6.45 | 0.0046 |
| nether | 0.0 | 0.8 | 0.0167 |
| mobs | 0.0 | 0.4 | 0.0 |
| outline | 0.0 | 0.25 | 0.0 |
| outline.bare | 0.0 | 0.25 | 0.0 |
| occlusion | 0.0 | 0.25 | 0.0001 |
| occlusion_move | 0.0 | 0.25 | 0.0 |
| occlusion_move.x2297 | 0.0 | 0.25 | 0.0 |
| occlusion_move.x2305 | 0.0 | 0.25 | 0.0 |
| occlusion_move.x2309 | 0.0 | 0.3 | 0.0 |
| occlusion_move.open | 0.0 | 0.25 | 0.0 |
| occlusion_move.closed | 0.0 | 0.25 | 0.0 |
| occlusion_move.bare_x2300 | 0.0 | 0.3 | 0.0 |
| occlusion_move.bare_x2297 | 0.0 | 0.3 | 0.0 |
| occlusion_move.bare_x2305 | 0.0 | 0.25 | 0.0 |
| occlusion_move.bare_x2309 | 0.0 | 0.25 | 0.0 |
| occlusion_move.bare_open | 0.0 | 0.3 | 0.0 |
| translucent_move | 0.0001 | 0.3 | 0.0001 |
| translucent_move.x2404 | 0.0001 | 0.3 | 0.0002 |
| translucent_move.x2414 | 0.0 | 0.3 | 0.0003 |
| translucent_move.x2424 | 0.0 | 0.3 | 0.0002 |
| translucent_move.low | 0.0005 | 0.3 | 0.0012 |
| translucent_move.back | 0.0 | 0.3 | 0.0 |
| translucent_move.bare_back | 0.0 | 0.3 | 0.0 |
| water_glass_oit | 0.0044 | 0.3 | 0.0051 |
| translucent_move_oit | 0.0 | 0.3 | 0.0 |
| translucent_move_oit.x2404 | 0.0001 | 0.3 | 0.0002 |
| translucent_move_oit.x2414 | 0.0001 | 0.3 | 0.0002 |
| translucent_move_oit.x2424 | 0.0002 | 0.3 | 0.0004 |
| translucent_move_oit.low | 0.001 | 0.3 | 0.0015 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 |
| translucent_move_oit.bare_back | 0.0 | 0.3 | 0.0002 |
| flat_plants | 0.0 | 0.3 | 0.023 |
| renderer_off | 0.0 | 0.25 | 0.0 |
| particles | 0.0 | 0.25 | 0.0 |
| particles.bare | 0.0 | 0.25 | 0.0 |
| animation | 0.0 | 0.25 | 0.0001 |
| animation.behind | 0.0 | 0.25 | 0.0 |
| animation.front | 0.0 | 0.25 | 0.0001 |
| animated_items | 0.0 | 0.25 | 0.0 |

Strict picture: pixels where both vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | old-GPU |
|---|---|---|---|
| spawn | 99.83 | 18057 | 3916 (4583) |
| flythrough | 100.0 | 6020 | 52 (328) |
| water_glass | 100.0 | 25508 | 27 (3511) |
| nether | 100.0 | 905 | 154 (608) |
| mobs | 100.0 | 10 | 0 (0) |
| outline | 100.0 | 10 | 0 (0) |
| outline.bare | 100.0 | 10 | 0 (0) |
| occlusion | 100.0 | 22 | 1 (76) |
| occlusion_move | 100.0 | 10 | 0 (0) |
| occlusion_move.x2297 | 100.0 | 10 | 0 (0) |
| occlusion_move.x2305 | 100.0 | 10 | 0 (0) |
| occlusion_move.x2309 | 100.0 | 32 | 0 (0) |
| occlusion_move.open | 100.0 | 10 | 0 (0) |
| occlusion_move.closed | 100.0 | 10 | 0 (0) |
| occlusion_move.bare_x2300 | 100.0 | 90 | 0 (0) |
| occlusion_move.bare_x2297 | 100.0 | 70 | 0 (0) |
| occlusion_move.bare_x2305 | 100.0 | 10 | 0 (0) |
| occlusion_move.bare_x2309 | 100.0 | 10 | 0 (0) |
| occlusion_move.bare_open | 100.0 | 110 | 0 (0) |
| translucent_move | 100.0 | 13 | 1 (124) |
| translucent_move.x2404 | 100.0 | 15 | 3 (172) |
| translucent_move.x2414 | 100.0 | 13 | 4 (166) |
| translucent_move.x2424 | 100.0 | 13 | 2 (122) |
| translucent_move.low | 100.0 | 41 | 11 (17) |
| translucent_move.back | 100.0 | 13 | 2 (124) |
| translucent_move.bare_back | 100.0 | 13 | 4 (126) |
| water_glass_oit | 99.99 | 57 | 30 (3515) |
| translucent_move_oit | 100.0 | 12 | 1 (123) |
| translucent_move_oit.x2404 | 100.0 | 16 | 4 (174) |
| translucent_move_oit.x2414 | 100.0 | 10 | 4 (164) |
| translucent_move_oit.x2424 | 100.0 | 15 | 4 (123) |
| translucent_move_oit.low | 100.0 | 40 | 12 (18) |
| translucent_move_oit.back | 100.0 | 12 | 1 (124) |
| translucent_move_oit.bare_back | 100.0 | 15 | 4 (126) |
| flat_plants | 100.0 | 350 | 248 (8128) |
| renderer_off | 100.0 | 10 | 0 (0) |
| particles | 100.0 | 4 | 0 (0) |
| particles.bare | 100.0 | 4 | 0 (0) |
| animation | 100.0 | 10 | 1 (3) |
| animation.behind | 100.0 | 10 | 0 (2) |
| animation.front | 100.0 | 10 | 1 (3) |
| animated_items | 100.0 | 10 | 0 (0) |

### Counts, core old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 49.07 (base 49.0) | 12 (base 13) | 1380 (base 6372) | 248.33 (base 256.29) | 5.99 (base 3.73) |
| flythrough | 67.86 (reported, not gated) | 28 (reported, not gated) | 130404 (reported, not gated) | 305.86 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 19.68 (base 20.0) | 13 (base 14) | 7540 (base 10264) | 233.34 (base 249.83) | 5.99 (base 5.99) |
| nether | 30.08 (base 33.79) | 10 (base 11) | 4068 (base 10656) | 253.65 (base 272.57) | 5.99 (base 5.99) |
| mobs | 28.67 (base 28.97) | 14 (base 16) | 610516 (base 1015892) | 253.47 (base 268.23) | 5.99 (base 5.99) |
| outline | 19.72 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 252.33 (base 264.41) | 5.99 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 257.93 (base 264.64) | 5.99 (base 4.24) |
| occlusion_move | 24.04 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 258.65 (base 265.66) | 5.99 (base 5.99) |
| translucent_move | 17.67 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 260.55 (base 268.19) | 5.99 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7764 (base 7604) | 232.26 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 3172 (reported, not gated) | 235.46 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 29.16 (base 33.0) | 12 (base 13) | 1492 (base 1716) | 235.94 (base 248.11) | 3.48 (base 5.99) |
| renderer_off | 17.67 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 228.51 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 255.45 (base 261.4) | 5.99 (base 4.24) |
| animation | 49 median (avg 39.94) (base 49) | 13 (base 13) | 1716 (base 1716) | 252.97 (base 260.68) | 5.99 (base 4.24) |
| animated_items | 60 median (avg 46.77) (base 60) | 21 (base 22) | 21508 (base 21668) | 228.26 (base 239.53) | 5.99 (base 5.99) |

Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 449081.157 (687863.893) | 34.714 | 1.778 | 558500 | 557120 (2097152.0) | 318 | 0.0 / 0 |
| flythrough | 570223.187 (915234.341) | 37.696 | 1.086 | 2107660 | 2094536 (2097152.0) | 246 | 4.834 / 67 |
| water_glass | 216429.591 (388668.535) | 44.315 | 1.114 | 9444 | 0 (2097152.0) | 0 | 0.0 / 0 |
| nether | 270215.785 (479739.926) | 43.675 | 1.701 | 186828 | 182976 (2097152.0) | 0 | 0.0 / 0 |
| mobs | 166910.184 (301909.182) | 44.715 | 0.99 | 610516 | 0 (2097152.0) | 0 | 0.0 / 0 |
| outline | 28290.995 (48530.616) | 41.705 | 1.0 | 6380 | 0 (2097152.0) | 0 | 0.0 / 0 |
| occlusion | 44594.324 (78052.087) | 42.866 | 1.211 | 98148 | 0 (2097152.0) | 0 | 0.0 / 0 |
| occlusion_move | 49158.868 (86149.506) | 42.938 | 1.236 | 654524 | 579456 (2097152.0) | 1 | 0.0 / 0 |
| translucent_move | 205140.36 (365059.081) | 43.806 | 1.179 | 758500 | 756224 (2097152.0) | 0 | 0.0 / 0 |
| water_glass_oit | 216793.946 (389322.851) | 44.315 | 1.114 | 9556 | 0 (2097152.0) | 296 | 0.0 / 0 |
| translucent_move_oit | 205127.354 (364756.053) | 43.763 | 1.178 | 737428 | 731840 (2097152.0) | 263 | 0.0 / 0 |
| flat_plants | 246447.595 (452829.625) | 45.576 | 1.059 | 1828 | 0 (2097152.0) | 3 | 0.0 / 0 |
| renderer_off | 160416.255 (289622.318) | 44.612 | 1.019 | 1380 | 0 (2097152.0) | 2 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 8772 | 0 (2097152.0) | 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86324 | 84608 (2097152.0) | 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79144 | 57728 (2097152.0) | 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.123 | 792.484 | 16 (vanilla 28) | 1371708 | 40.597 / 0.08 | 22.721 / 36.0 | 36.629 | 2455 / 0 | 2.51 (4.745) |
| flythrough | plain, one multi-draw per region and layer | 57.996 | 937.516 | 16 (vanilla 28) | 5404480 | 14.966 / 1.0 | 89.248 / 144.0 | 144.315 | 6816 / 0 | 4.551 (11.72) |
| water_glass | plain, one multi-draw per region and layer | 14.024 | 273.462 | 16 (vanilla 28) | 776024 | 33.117 / 0.072 | 12.513 / 36.0 | 20.722 | 3104 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.034 | 469.79 | 16 (vanilla 28) | 957944 | 27.868 / 0.177 | 15.259 / 20.0 | 25.58 | 4141 / 0 | 1.954 (2.483) |
| mobs | plain, one multi-draw per region and layer | 11.018 | 199.332 | 16 (vanilla 28) | 602812 | 25.373 / 0.155 | 9.646 / 16.0 | 16.097 | 4579 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 96900 | 24.879 / 0.154 | 1.539 / 16.0 | 2.588 | 4674 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.119 | 16 (vanilla 28) | 155844 | 23.668 / 0.146 | 2.498 / 20.0 | 4.161 | 4938 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.993 | 76.456 | 16 (vanilla 28) | 206276 | 22.308 / 0.137 | 3.311 / 20.0 | 5.508 | 5260 / 0 | 3.125 (8.425) |
| translucent_move | plain, one multi-draw per region and layer | 12.02 | 253.737 | 16 (vanilla 28) | 815960 | 19.368 / 0.126 | 13.21 / 28.0 | 21.788 | 6064 / 0 | 4.709 (10.856) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.054 | 281.949 | 16 (vanilla 28) | 776024 | 7.425 / 0.043 | 12.513 / 20.0 | 20.722 | 1090 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.593 | 268.464 | 16 (vanilla 28) | 790108 | 7.271 / 0.199 | 12.797 / 28.0 | 21.098 | 2596 / 0 | 6.283 (14.746) |
| flat_plants | plain, one multi-draw per region and layer | 17.029 | 288.486 | 16 (vanilla 28) | 904132 | 6.148 / 0.01 | 14.477 / 24.0 | 24.143 | 958 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.022 | 209.35 | 16 (vanilla 28) | 578276 | 2.353 / 0.02 | 9.189 / 24.0 | 15.442 | 603 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 19.322 / 0.125 | 0.131 / 28.0 | 0.225 | 6106 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.163 / 0.124 | 0.302 / 28.0 | 0.518 | 6208 / 0 | 1.675 (4.333) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.107 / 0.125 | 0.153 / 28.0 | 0.261 | 6258 / 0 | 0.669 (0.884) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 154.0 | 17.0 | 0.0 | 0 | 0 | 47 (0.021 / 0.039 / 0.062; 0) | 0 | 49126 / 0 / 0 |
| flythrough | 196.733 | 26.81 | 0.0 | 0 | 0 | 937 (0.022 / 0.033 / 6.022; 0) | 65 | 48593 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2368 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.012 / 0.015 / 0.015; 0) | 32 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.065 / 0.096 / 0.125; 0) | 180 | 4165 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 891 | 0 (None / None / None; 0) | 0 | 1188 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 792 | 0 (None / None / None; 0) | 0 | 1848 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2372 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2392 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 344.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.029 (0.222; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 443.219 | 1.0 (0.0, 0.0) | 1.085 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.094 (0.257; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 50.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.046 (0.102; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 52.36 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.28) | 0.053 (0.229; 0) | 0 | 2782 / 1505432 / 0 / 0 |
| translucent_move | 139.316 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 139.708 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 155.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 114.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.008; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.005 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = no measure run in quick mode):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | - / 5.0 | - / 0.066 | - / 3.0 | - / 0.0 | - / 0.0 | 100.638 / 0.066 | 0.026 / 0.006 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | - / 5.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.541 / 0.0 | 0.006 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.883 |
| water_glass | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 55.855 / 55.846 | 0.025 / 0.028 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | - / 6.717 | - / 0.689 | - / 0.0 | - / 0.0 | - / 0.0 | 42.1 / 7.575 | 0.061 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 11.93 / 11.916 | 0.007 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 56.32 / 56.461 | 0.019 / 0.027 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 14.755 / 14.564 | 0.009 / 0.013 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | - / 6.757 | - / 0.731 | - / 3.0 | - / 0.0 | - / 0.0 | 1.382 / 1.322 | 0.012 / 0.007 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | - / 0.0 | - / 4.0 | - / 3.0 | - / 0.0 | - / 0.0 | 66.0 / 66.0 | 0.004 / 0.037 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | - / 32.27 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 2.8 / 2.798 | 0.003 / 0.011 | 56 (3) | 40.58 / 9.24 / 31.34 | 3 / 0 | 10.985 |
| animated_items | - / 28.104 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

GUI pass, core old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| spawn | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| flythrough | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| water_glass | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| nether | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| mobs | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| outline | 3.0 / - | 0.0 / - | 4.0 / - | 3.0 / - (pipelines 3.0, textures 2.0, scissors 1.0) | 3.0 / - (3.0 / -) |
| occlusion | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| occlusion_move | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| translucent_move | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| water_glass_oit | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| translucent_move_oit | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| flat_plants | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| renderer_off | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| particles | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| animation | 1.0 / - | 0.0 / - | 1.0 / - | 1.0 / - (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / - (1.0 / -) |
| animated_items | 4.0 / - | 3.0 / - | 9.0 / - | 4.0 / - (pipelines 4.0, textures 3.0, scissors 1.0) | 4.0 / - (4.0 / -) |

## Direct buffers per scene (reported; open bug, see docs/progress.md)

| run | scene | start MB | peak MB (tick) | end MB | rise |
|---|---|---|---|---|---|
| core-quicksilver-old | spawn | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | water_glass | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | nether | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | mobs | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | outline | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | occlusion | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | occlusion_move | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | translucent_move | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | particles | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | animation | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | animated_items | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | flythrough | 3.73 | 3.73 (0) | 3.73 |  |

Rises of 4 MB or more: none

