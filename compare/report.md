# Quicksilver CI comparison

**passed**

## SIFTEC: report only

Until SIFTEC starts with MapMod's latest build again, SIFTEC's suites fail nothing (docs/progress.md, open items). Core and Terralith gate as usual.

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
| spawn | 0.2027 | 10.05 | 0.088 |
| flythrough | 0.0 | 14.95 | 0.0011 |
| water_glass | 0.0031 | 6.45 | 0.0047 |
| nether | 0.0 | 0.8 | 0.0279 |
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
| translucent_move | 0.0002 | 0.3 | 0.0001 |
| translucent_move.x2404 | 0.0001 | 0.3 | 0.0003 |
| translucent_move.x2414 | 0.0001 | 0.3 | 0.0004 |
| translucent_move.x2424 | 0.0002 | 0.3 | 0.0002 |
| translucent_move.low | 0.0005 | 0.3 | 0.0011 |
| translucent_move.back | 0.0001 | 0.3 | 0.0003 |
| translucent_move.bare_back | 0.0 | 0.3 | 0.0001 |
| water_glass_oit | 0.0038 | 0.3 | 0.0035 |
| translucent_move_oit | 0.0002 | 0.3 | 0.0001 |
| translucent_move_oit.x2404 | 0.0 | 0.3 | 0.0002 |
| translucent_move_oit.x2414 | 0.0003 | 0.3 | 0.0003 |
| translucent_move_oit.x2424 | 0.0001 | 0.3 | 0.0002 |
| translucent_move_oit.low | 0.0004 | 0.3 | 0.001 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 |
| translucent_move_oit.bare_back | 0.0001 | 0.3 | 0.0002 |
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
| spawn | 99.72 | 18057 | 687 (1371) |
| flythrough | 100.0 | 6020 | 53 (316) |
| water_glass | 100.0 | 25508 | 27 (3511) |
| nether | 100.0 | 905 | 263 (724) |
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
| translucent_move | 100.0 | 13 | 1 (123) |
| translucent_move.x2404 | 100.0 | 15 | 5 (174) |
| translucent_move.x2414 | 100.0 | 13 | 4 (164) |
| translucent_move.x2424 | 100.0 | 13 | 3 (122) |
| translucent_move.low | 100.0 | 41 | 12 (18) |
| translucent_move.back | 100.0 | 13 | 3 (125) |
| translucent_move.bare_back | 100.0 | 13 | 4 (127) |
| water_glass_oit | 100.0 | 57 | 21 (3505) |
| translucent_move_oit | 100.0 | 12 | 3 (125) |
| translucent_move_oit.x2404 | 100.0 | 16 | 3 (172) |
| translucent_move_oit.x2414 | 100.0 | 10 | 4 (164) |
| translucent_move_oit.x2424 | 100.0 | 15 | 3 (122) |
| translucent_move_oit.low | 100.0 | 40 | 14 (20) |
| translucent_move_oit.back | 100.0 | 12 | 3 (126) |
| translucent_move_oit.bare_back | 100.0 | 15 | 3 (125) |
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
| spawn | 50.31 (base 49.0) | 13 (base 13) | 7428 (base 6372) | 252.73 (base 256.29) | 3.99 (base 3.73) |
| flythrough | 66.83 (reported, not gated) | 42 (reported, not gated) | 511724 (reported, not gated) | 308.04 (base 324.79) | 3.48 (base 5.99) |
| water_glass | 19.97 (base 20.0) | 13 (base 14) | 7652 (base 10264) | 236.72 (base 249.83) | 3.99 (base 5.99) |
| nether | 33.95 (base 33.79) | 10 (base 11) | 4736 (base 10656) | 257.64 (base 272.57) | 3.99 (base 5.99) |
| mobs | 28.86 (base 28.97) | 14 (base 16) | 610516 (base 1015892) | 256.22 (base 268.23) | 3.99 (base 5.99) |
| outline | 19.78 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 256.03 (base 264.41) | 3.99 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 261.51 (base 264.64) | 3.99 (base 4.24) |
| occlusion_move | 24.05 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 261.94 (base 265.66) | 3.99 (base 5.99) |
| translucent_move | 17.99 (reported, not gated) | 13 (reported, not gated) | 3060 (reported, not gated) | 264.59 (base 268.19) | 5.99 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 8436 (base 7604) | 235.3 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 238.25 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 32.94 (base 33.0) | 13 (base 13) | 1604 (base 1716) | 238.04 (base 248.11) | 3.73 (base 5.99) |
| renderer_off | 17.68 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 231.87 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 259.62 (base 261.4) | 5.99 (base 4.24) |
| animation | 49 median (avg 39.98) (base 49) | 13 (base 13) | 1716 (base 1716) | 257.07 (base 260.68) | 5.99 (base 4.24) |
| animated_items | 60 median (avg 46.77) (base 60) | 21 (base 22) | 21508 (base 21668) | 231.91 (base 239.53) | 5.99 (base 5.99) |

Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 456517.673 (701036.619) | 34.88 | 1.78 | 348916 | 341376 (2097152.0) | 226 | 0.0 / 0 |
| flythrough | 556931.52 (892564.587) | 37.603 | 1.083 | 2107676 | 2094856 (2097152.0) | 150 | 6.914 / 68 |
| water_glass | 215030.655 (384813.752) | 44.121 | 1.117 | 163620 | 156864 (2097152.0) | 52 | 0.0 / 0 |
| nether | 270151.435 (478932.555) | 43.593 | 1.701 | 325152 | 321088 (2097152.0) | 382 | 0.0 / 0 |
| mobs | 166636.824 (300890.305) | 44.619 | 0.99 | 767380 | 156864 (2097152.0) | 20 | 0.0 / 0 |
| outline | 28295.08 (48534.757) | 41.701 | 1.0 | 6516 | 0 (2097152.0) | 0 | 0.0 / 0 |
| occlusion | 45217.362 (79282.137) | 42.967 | 1.211 | 98148 | 0 (2097152.0) | 0 | 0.0 / 0 |
| occlusion_move | 50547.314 (88906.485) | 43.146 | 1.232 | 786556 | 711488 (2097152.0) | 0 | 0.0 / 0 |
| translucent_move | 201005.2 (356769.616) | 43.66 | 1.179 | 759300 | 756352 (2097152.0) | 310 | 0.0 / 0 |
| water_glass_oit | 215556.39 (385754.595) | 44.121 | 1.117 | 10564 | 0 (2097152.0) | 205 | 0.0 / 0 |
| translucent_move_oit | 201305.505 (357115.361) | 43.63 | 1.178 | 736420 | 731840 (2097152.0) | 194 | 0.0 / 0 |
| flat_plants | 246642.493 (453265.114) | 45.585 | 1.06 | 1716 | 0 (2097152.0) | 377 | 0.0 / 0 |
| renderer_off | 155109.568 (279120.693) | 44.429 | 1.02 | 1380 | 0 (2097152.0) | 1 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 8772 | 0 (2097152.0) | 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86324 | 84608 (2097152.0) | 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79236 | 57728 (2097152.0) | 0 | 0.0 / 0 |

Package 4c, core old-gpu: meshing ms per section over each bench (sections in brackets):

| scene | Quicksilver ms/section (sections) | while measuring: Quicksilver ms/section (sections) | to vanilla's compiler this bench | checked / mismatched since start-up | same sections, same thread: Quicksilver / vanilla's compiler ms (mean) | median (gated) |
|---|---|---|---|---|---|---|
| spawn | 5.062 (117) | 4.535 (37) | 0 | 938 / 0 | 9.34 / 12.771 | 4.997 / 7.306 |
| flythrough | 5.873 (1649) | 5.872 (1647) | 0 | 1395 / 0 | 8.125 / 11.432 | 5.608 / 7.765 |
| water_glass | 2.384 (21) | 5.133 (1) | 0 | 1258 / 0 | 8.527 / 11.437 | 4.536 / 6.542 |
| nether | 2.596 (28) | 2.376 (15) | 0 | 1878 / 0 | 7.016 / 9.374 | 3.492 / 5.154 |
| mobs | 1.888 (7) | 5.266 (1) | 0 | 2110 / 0 | 6.898 / 9.171 | 3.42 / 5.015 |
| outline | 0.702 (12) | None (0) | 0 | 2170 / 0 | 6.807 / 9.061 | 3.356 / 5.002 |
| occlusion | 0.721 (38) | None (0) | 0 | 2299 / 0 | 6.64 / 8.798 | 3.241 / 4.856 |
| occlusion_move | 2.123 (55) | 5.314 (16) | 0 | 2440 / 0 | 6.379 / 8.457 | 3.012 / 4.541 |
| translucent_move | 3.927 (108) | 6.694 (33) | 0 | 2758 / 0 | 6.145 / 8.203 | 3.01 / 4.499 |
| water_glass_oit | 3.892 (154) | None (0) | 0 | 413 / 0 | 9.031 / 12.487 | 5.072 / 7.198 |
| translucent_move_oit | 4.396 (218) | 4.792 (26) | 0 | 954 / 0 | 6.233 / 8.609 | 3.601 / 5.056 |
| flat_plants | 1.683 (96) | None (0) | 0 | 407 / 0 | 7.487 / 9.27 | 2.869 / 4.166 |
| renderer_off | 0.765 (21) | None (0) | 0 | 325 / 0 | 9.393 / 12.784 | 4.689 / 5.657 |
| particles | 0.383 (30) | None (0) | 0 | 2785 / 0 | 6.091 / 8.13 | 2.968 / 4.441 |
| animation | 1.63 (18) | 1.563 (4) | 0 | 2860 / 0 | 5.971 / 7.955 | 2.87 / 4.292 |
| animated_items | 0.396 (42) | 0.814 (2) | 0 | 2892 / 0 | 5.912 / 7.877 | 2.844 / 4.202 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.173 | 806.491 | 16 (vanilla 28) | 1404108 | 41.178 / 0.14 | 23.251 / 36.0 | 37.494 | 2555 / 0 | 4.535 (8.362) |
| flythrough | plain, one multi-draw per region and layer | 57.113 | 909.167 | 16 (vanilla 28) | 5233764 | 15.296 / 1.017 | 86.468 / 112.0 | 139.757 | 6520 / 0 | 5.872 (16.678) |
| water_glass | plain, one multi-draw per region and layer | 14.034 | 273.663 | 16 (vanilla 28) | 775704 | 34.132 / 0.121 | 12.52 / 36.0 | 20.714 | 3235 / 0 | 5.133 (5.129) |
| nether | plain, one multi-draw per region and layer | 20.052 | 472.233 | 16 (vanilla 28) | 955416 | 28.881 / 0.207 | 15.234 / 20.0 | 25.512 | 4271 / 0 | 2.376 (4.221) |
| mobs | plain, one multi-draw per region and layer | 11.024 | 199.427 | 16 (vanilla 28) | 600492 | 26.27 / 0.181 | 9.629 / 16.0 | 16.035 | 4715 / 0 | 5.266 (5.254) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 96908 | 25.76 / 0.18 | 1.539 / 16.0 | 2.588 | 4811 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.119 | 16 (vanilla 28) | 158300 | 24.742 / 0.172 | 2.535 / 20.0 | 4.227 | 5032 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.997 | 76.455 | 16 (vanilla 28) | 218116 | 23.556 / 0.164 | 3.49 / 20.0 | 5.824 | 5290 / 0 | 5.314 (12.435) |
| translucent_move | plain, one multi-draw per region and layer | 12.031 | 248.429 | 16 (vanilla 28) | 800616 | 20.643 / 0.15 | 12.987 / 28.0 | 21.379 | 6042 / 0 | 6.694 (15.917) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.078 | 282.371 | 16 (vanilla 28) | 767764 | 7.545 / 0.043 | 12.391 / 20.0 | 20.502 | 1086 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.613 | 263.521 | 16 (vanilla 28) | 778072 | 6.532 / 0.098 | 12.631 / 28.0 | 20.777 | 2404 / 0 | 4.792 (11.415) |
| flat_plants | plain, one multi-draw per region and layer | 17.045 | 283.751 | 16 (vanilla 28) | 904132 | 6.024 / 0.009 | 14.52 / 24.0 | 24.143 | 949 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.022 | 205.346 | 16 (vanilla 28) | 557300 | 2.335 / 0.02 | 8.894 / 24.0 | 14.882 | 606 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 20.594 / 0.149 | 0.131 / 28.0 | 0.225 | 6084 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.423 / 0.148 | 0.302 / 28.0 | 0.518 | 6186 / 0 | 1.563 (2.202) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.363 / 0.148 | 0.153 / 28.0 | 0.261 | 6236 / 0 | 0.814 (1.258) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 156.0 | 17.0 | 0.0 | 0 | 0 | 30 (0.027 / 0.038 / 0.05; 0) | 0 | 35412 / 0 / 0 |
| flythrough | 189.735 | 26.172 | 0.0 | 0 | 0 | 874 (0.04 / 0.049 / 9.062; 0) | 53 | 28650 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1652 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.016 / 0.066 / 0.149; 0) | 32 | 1198 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.122 / 0.153 / 4.164; 0) | 180 | 2702 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 618 | 0 (None / None / None; 0) | 0 | 824 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 585 | 0 (None / None / None; 0) | 0 | 1365 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1512 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2372 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 352.947 | 2.0 (1.0, 0.0) | 11.0 (1.0, 0.0) | 0.044 (0.185; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 437.099 | 1.0 (0.0, 0.0) | 1.205 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.1; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.132 (0.277; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.063 (0.098; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.356 | 10.0 (9.0, 2.362) | 6.0 (6.0, 2.275) | 0.059 (1.235; 0) | 0 | 2778 / 1504230 / 0 / 0 |
| translucent_move | 135.073 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.002 (0.263; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.677 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 151.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.0; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.006 (0.048; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = no measure run in quick mode):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | - / 5.0 | - / 0.305 | - / 3.0 | - / 0.0 | - / 0.0 | 83.64 / 0.304 | 0.024 / 0.012 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | - / 5.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.236 / 0.0 | 0.004 / 0.003 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.848 |
| water_glass | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 56.406 / 56.39 | 0.015 / 0.029 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | - / 10.0 | - / 0.945 | - / 0.0 | - / 0.0 | - / 0.0 | 42.287 / 10.984 | 0.078 / 0.011 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 13.696 / 13.598 | 0.006 / 0.011 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 61.69 / 61.762 | 0.012 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | - / 0.0 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 12.21 / 12.113 | 0.005 / 0.011 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | - / 10.0 | - / 0.939 | - / 3.0 | - / 0.0 | - / 0.0 | 1.796 / 1.704 | 0.011 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | - / 0.0 | - / 0.0 | - / 3.0 | - / 0.0 | - / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | - / 0.0 | - / 4.0 | - / 3.0 | - / 0.0 | - / 0.0 | 66.0 / 66.0 | 0.004 / 0.035 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | - / 32.311 | - / 1.0 | - / 3.0 | - / 0.0 | - / 0.0 | 2.8 / 2.797 | 0.004 / 0.007 | 56 (3) | 40.608 / 9.248 / 31.36 | 3 / 0 | 10.987 |
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
| core-quicksilver-old | spawn | 3.99 | 3.99 (0) | 3.99 |  |
| core-quicksilver-old | water_glass | 3.99 | 3.99 (0) | 3.99 |  |
| core-quicksilver-old | nether | 3.99 | 3.99 (0) | 3.99 |  |
| core-quicksilver-old | mobs | 3.99 | 3.99 (0) | 3.99 |  |
| core-quicksilver-old | outline | 3.99 | 3.99 (0) | 3.99 |  |
| core-quicksilver-old | occlusion | 3.99 | 3.99 (0) | 3.99 |  |
| core-quicksilver-old | occlusion_move | 3.99 | 3.99 (0) | 3.99 |  |
| core-quicksilver-old | translucent_move | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | particles | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | animation | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | animated_items | 5.99 | 5.99 (0) | 5.99 |  |
| core-quicksilver-old | flat_plants | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | flythrough | 3.48 | 3.48 (0) | 3.48 |  |

Rises of 4 MB or more: none

