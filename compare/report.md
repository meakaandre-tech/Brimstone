# Brimstone CI comparison

**FAILED**

- core/flythrough: vulkan differs from vanilla on 7384 pixels where every vanilla run agrees exactly (strict limit 5900)

## Suite core

- vanilla-a: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-b: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
- brimstone-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- brimstone-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- brimstone-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`

GPU error messages in the client logs (Brimstone run / vanilla run of the same profile):

- full: 0 / 0
- old-gpu: 0 / 0
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| spawn | 0.576 | 10.05 | 0.5818 | 0.6713 | 0.1899 |
| flythrough | 0.0203 | 14.95 | 0.0537 | 0.0386 | 0.0509 |
| water_glass | 1.4283 | 6.45 | 1.1339 | 0.3255 | 1.3631 |
| nether | 0.1673 | 0.8 | 0.3141 | 0.2586 | 0.0676 |
| mobs | 0.0801 | 0.4 | 0.1066 | 0.0802 | 0.0582 |
| occlusion | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2297 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2309 | 0.0051 | 0.3 | 0.0018 | 0.0022 | 0.0077 |
| occlusion_move.open | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.closed | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2300 | 0.0 | 0.3 | 0.0 | 0.0031 | 0.0 |
| occlusion_move.bare_x2297 | 0.0 | 0.3 | 0.0 | 0.0114 | 0.0 |
| occlusion_move.bare_x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2309 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_open | 0.0 | 0.3 | 0.0 | 0.0129 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs at all:

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| spawn | 81.31 | 18000 | 751 | 851 | 726 |
| flythrough | 95.21 | 5900 | 5653 | 5152 | 7384 |
| water_glass | 90.97 | 25500 | 5108 | 789 | 5237 |
| nether | 98.7 | 900 | 0 | 0 | 1 |
| mobs | 99.25 | 600 | 113 | 77 | 23 |
| occlusion | 98.62 | 20 | 0 | 0 | 0 |
| occlusion_move | 99.15 | 10 | 0 | 0 | 0 |
| occlusion_move.x2297 | 98.94 | 10 | 0 | 0 | 0 |
| occlusion_move.x2305 | 98.96 | 10 | 0 | 0 | 0 |
| occlusion_move.x2309 | 99.08 | 30 | 0 | 0 | 8 |
| occlusion_move.open | 98.55 | 10 | 0 | 0 | 0 |
| occlusion_move.closed | 98.92 | 10 | 0 | 0 | 0 |
| occlusion_move.bare_x2300 | 98.89 | 90 | 0 | 20 | 0 |
| occlusion_move.bare_x2297 | 98.92 | 70 | 0 | 40 | 0 |
| occlusion_move.bare_x2305 | 98.95 | 10 | 0 | 0 | 0 |
| occlusion_move.bare_x2309 | 99.1 | 10 | 0 | 0 | 0 |
| occlusion_move.bare_open | 98.49 | 110 | 0 | 33 | 0 |

### Counts, core full

| scene | draw calls/frame (avg) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 220.96 (base 218.25) | 15 (base 14) | 25128 (base 22476) | 254.19 (base 259.07) | 4.24 (base 3.99) |
| flythrough | 222.41 (reported, not gated) | 32 (reported, not gated) | 600540 (reported, not gated) | 315.86 (base 324.25) | 5.99 (base 4.24) |
| water_glass | 221.16 (base 219.1) | 15 (base 15) | 14724 (base 15576) | 243.03 (base 250.02) | 4.24 (base 5.99) |
| nether | 216.84 (base 215.94) | 12 (base 12) | 18644 (base 19648) | 264.49 (base 273.53) | 4.24 (base 5.99) |
| mobs | 210.6 (base 240.07) | 17 (base 17) | 961640 (base 1010948) | 262.9 (base 268.37) | 4.24 (base 6.99) |
| occlusion | 157.67 (base 159.53) | 15 (base 15) | 100328 (base 126320) | 264.4 (base 265.1) | 4.24 (base 4.24) |
| occlusion_move | 158.1 (reported, not gated) | 15 (reported, not gated) | 82200 (reported, not gated) | 264.98 (base 265.74) | 4.24 (base 4.24) |

Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex (compact/wide; avg) | live vertices | wide % | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 4.018 | 578.571 | 16/28; 21.789 (vanilla 28) | 1141296 | 48.242 | 24.885 / 64.0 | 30.476 | 1852 / 0 | 3.119 (5.732) |
| flythrough | indirect, one multi-draw per arena and layer | 6.235 | 722.503 | 16/28; 23.112 (vanilla 28) | 4689776 | 59.268 | 108.433 / 128.0 | 125.231 | 5261 / 0 | 5.008 (13.591) |
| water_glass | indirect, one multi-draw per arena and layer | 4.01 | 268.675 | 16/28; 26.011 (vanilla 28) | 748424 | 83.425 | 19.386 / 64.0 | 19.985 | 2443 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 3.008 | 469.282 | 16/28; 22.376 (vanilla 28) | 960428 | 53.13 | 21.409 / 64.0 | 25.646 | 3478 / 0 | 2.063 (2.658) |
| mobs | indirect, one multi-draw per arena and layer | 4.009 | 197.428 | 16/28; 26.619 (vanilla 28) | 591688 | 88.495 | 15.705 / 64.0 | 15.8 | 3912 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 4.007 | 67.112 | 16/28; 23.239 (vanilla 28) | 161972 | 60.322 | 3.734 / 64.0 | 4.325 | 4121 / 0 | 2.132 (2.195) |
| occlusion_move | indirect, one multi-draw per arena and layer | 4.007 | 72.49 | 16/28; 22.983 (vanilla 28) | 214460 | 58.189 | 4.889 / 64.0 | 5.727 | 4370 / 0 | 4.253 (12.125) |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 312.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.077; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 435.429 | 1.0 (0.0, 0.0) | 1.442 (0.0, 0.0) | 0.001 (0.009; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 92.0 (91.0, 0.0) | 0.0 (0.0, 0.0) | 0.139 (0.288; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.066 (0.272; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 50.364 | 10.0 (9.0, 2.363) | 6.0 (6.0, 2.276) | 0.058 (0.216; 0) | 0 | 2788 / 1509744 / 0 / 0 |

### Counts, core old-gpu

| scene | draw calls/frame (avg) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 406.92 (base 942.31) | 13 (base 13) | 3844 (base 6372) | 258.77 (base 256.29) | 3.73 (base 3.73) |
| flythrough | 461.69 (reported, not gated) | 51 (reported, not gated) | 1004160 (reported, not gated) | 314.51 (base 324.79) | 3.99 (base 5.99) |
| water_glass | 237.98 (base 489.1) | 14 (base 14) | 7044 (base 10264) | 243.51 (base 249.83) | 3.73 (base 5.99) |
| nether | 244.97 (base 687.04) | 10 (base 11) | 4736 (base 10656) | 264.22 (base 272.57) | 3.73 (base 5.99) |
| mobs | 253.04 (base 434.39) | 15 (base 16) | 983060 (base 1015892) | 263.42 (base 268.23) | 3.73 (base 5.99) |
| occlusion | 167.53 (base 169.7) | 14 (base 14) | 98180 (base 124172) | 264.86 (base 264.64) | 3.73 (base 4.24) |
| occlusion_move | 169.14 (reported, not gated) | 14 (reported, not gated) | 80016 (reported, not gated) | 265.04 (base 265.66) | 3.73 (base 5.99) |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex (compact/wide; avg) | live vertices | wide % | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 38.244 | 648.128 | 16/28; 21.647 (vanilla 28) | 1226304 | 47.058 | 26.541 / 64.0 | 32.746 | 1861 / 0 | 5.4 (9.427) |
| flythrough | plain, one multi-draw per region and layer | 60.308 | 706.829 | 16/28; 23.115 (vanilla 28) | 4548624 | 59.293 | 105.084 / 128.0 | 121.461 | 5019 / 0 | 7.449 (18.092) |
| water_glass | plain, one multi-draw per region and layer | 18.054 | 268.802 | 16/28; 26.011 (vanilla 28) | 748424 | 83.425 | 19.386 / 64.0 | 19.985 | 2479 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 31.103 | 469.56 | 16/28; 22.375 (vanilla 28) | 960348 | 53.126 | 21.404 / 64.0 | 25.644 | 3483 / 0 | 4.01 (6.392) |
| mobs | plain, one multi-draw per region and layer | 17.046 | 197.535 | 16/28; 26.619 (vanilla 28) | 591688 | 88.495 | 15.705 / 64.0 | 15.8 | 3923 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 13.022 | 67.112 | 16/28; 22.815 (vanilla 28) | 154080 | 56.791 | 3.492 / 64.0 | 4.114 | 4168 / 0 | 5.406 (6.544) |
| occlusion_move | plain, one multi-draw per region and layer | 12.975 | 72.391 | 16/28; 22.691 (vanilla 28) | 208080 | 55.755 | 4.682 / 64.0 | 5.556 | 4405 / 0 | 6.311 (15.136) |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 350.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.079; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 431.907 | 1.0 (0.0, 0.0) | 1.492 (0.0, 0.0) | 0.01 (1.036; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.013 (0.073; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 96.0 (95.0, 0.0) | 0.0 (0.0, 0.0) | 0.146 (0.34; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.067 (0.402; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 50.338 | 10.0 (9.0, 2.358) | 6.0 (6.0, 2.282) | 0.054 (0.304; 0) | 0 | 2761 / 1493696 / 0 / 0 |

### Counts, core vulkan

| scene | draw calls/frame (avg) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 221.32 (base 218.94) | 15 (base 15) | 31032 (base 27764) | 256.45 (base 257.2) | 4.46 (base 3.98) |
| flythrough | 222.17 (reported, not gated) | 50 (reported, not gated) | 1228460 (reported, not gated) | 312.96 (base 318.06) | 5.98 (base 4.23) |
| water_glass | 221.04 (base 219.09) | 15 (base 15) | 14892 (base 15108) | 243.05 (base 244.39) | 4.46 (base 4.23) |
| nether | 216.76 (base 216.09) | 12 (base 12) | 17984 (base 19912) | 264.46 (base 268.95) | 4.46 (base 4.23) |
| mobs | 241.1 (base 240.09) | 17 (base 17) | 985976 (base 1008176) | 263.11 (base 264.31) | 4.46 (base 6.23) |
| occlusion | 158.42 (base 161.13) | 15 (base 15) | 100328 (base 126356) | 264.43 (base 264.78) | 4.46 (base 3.98) |
| occlusion_move | 161.28 (reported, not gated) | 15 (reported, not gated) | 82200 (reported, not gated) | 264.75 (base 265.16) | 4.46 (base 4.23) |

Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex (compact/wide; avg) | live vertices | wide % | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 4.022 | 635.454 | 16/28; 21.674 (vanilla 28) | 1210340 | 47.286 | 26.241 / 64.0 | 32.32 | 1824 / 0 | 2.798 (4.914) |
| flythrough | indirect, one multi-draw per arena and layer | 6.241 | 698.13 | 16/28; 23.143 (vanilla 28) | 4556608 | 59.525 | 105.487 / 128.0 | 121.675 | 5036 / 0 | 5.711 (12.895) |
| water_glass | indirect, one multi-draw per arena and layer | 4.014 | 270.912 | 16/28; 26.001 (vanilla 28) | 750084 | 83.342 | 19.422 / 64.0 | 20.029 | 2423 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 3.011 | 469.671 | 16/28; 22.376 (vanilla 28) | 960556 | 53.136 | 21.409 / 64.0 | 25.65 | 3515 / 0 | 2.473 (4.171) |
| mobs | indirect, one multi-draw per arena and layer | 4.011 | 197.558 | 16/28; 26.619 (vanilla 28) | 591704 | 88.496 | 15.705 / 64.0 | 15.8 | 3959 / 0 | 6.327 (6.64) |
| occlusion | indirect, one multi-draw per arena and layer | 4.007 | 67.113 | 16/28; 22.824 (vanilla 28) | 154352 | 56.867 | 3.499 / 64.0 | 4.122 | 4185 / 0 | 3.874 (4.267) |
| occlusion_move | indirect, one multi-draw per arena and layer | 4.007 | 72.369 | 16/28; 22.699 (vanilla 28) | 207700 | 55.825 | 4.682 / 64.0 | 5.546 | 4468 / 0 | 4.344 (9.217) |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 343.0 | 2.0 (1.0, 0.0) | 11.0 (1.0, 0.0) | 0.038 (0.122; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 430.101 | 1.0 (0.0, 0.0) | 1.404 (0.0, 0.0) | 0.001 (0.008; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 141.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.013 (0.044; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 96.0 (95.0, 0.0) | 0.0 (0.0, 0.0) | 0.139 (0.334; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.066 (0.101; 0) | 0 | 3576 / 1618736 / 0 / 0 |
| occlusion_move | 50.331 | 10.0 (9.0, 2.365) | 6.0 (6.0, 2.283) | 0.06 (0.415; 0) | 0 | 2724 / 1474250 / 0 / 0 |

## Suite siftec

- vanilla-a: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-b: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
- brimstone-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- brimstone-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- brimstone-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`

GPU error messages in the client logs (Brimstone run / vanilla run of the same profile):

- full: 2 / 2
- old-gpu: 18 / 18
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| factory | skipped: needs the mod siftec_selftest (Builds SIFTEC's buildings_a row with SIFTEC's test command (siftec worldtest), which lives in SIFTEC's CI-only selftest mod, not in its release.) | | | | |
| sift | 0.3045 | 1.15 | 0.1452 | 0.0511 | 0.5454 |
| hub | 4.2161 | 8.75 | 1.3244 | 0.02 | 1.4397 |
| mam | 4.4659 | 9.3 | 1.0027 | 0.0518 | 1.4699 |
| map | skipped: needs the mod siftec_selftest (SIFTEC's map unlocks through progression; its test command (siftec worldtest map) opens it, and that lives in SIFTEC's CI-only selftest mod, not in its release.) | | | | |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs at all:

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| sift | 93.17 | 4900 | 242 | 59 | 981 |
| hub | 95.43 | 12500 | 4 | 52 | 4 |
| mam | 84.96 | 155100 | 4 | 52 | 4 |

### Counts, siftec full

| scene | draw calls/frame (avg) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| sift | 425.28 (base 424.41) | 19 (base 19) | 30028 (base 55764) | 441.37 (base 440.07) | 4.24 (base 5.87) |
| hub | 297.22 (base 295.96) | 18 (base 18) | 67500 (base 67484) | 453.12 (base 451.33) | 4.24 (base 4.24) |
| mam | 301.89 (base 340.12) | 21 (base 21) | 24672 (base 24656) | 464.74 (base 471.8) | 6.24 (base 8.12) |

Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex (compact/wide; avg) | live vertices | wide % | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| sift | indirect, one multi-draw per arena and layer | 3.01 | 424.424 | 16/28; 17.768 (vanilla 28) | 458712 | 14.735 | 8.142 / 64.0 | 12.249 | 2272 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 4.007 | 49.082 | 16/28; 24.98 (vanilla 28) | 117092 | 74.83 | 2.903 / 64.0 | 3.127 | 2360 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 4.007 | 86.145 | 16/28; 21.815 (vanilla 28) | 938424 | 48.46 | 20.458 / 64.0 | 25.059 | 2817 / 0 | None (None) |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| sift | 244.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| sift | 447.62 (base 849.48) | 18 (base 18) | 17564 (base 73228) | 439.18 (base 441.24) | 4.24 (base 4.24) |
| hub | 303.53 (base 342.2) | 17 (base 17) | 65892 (base 66484) | 451.41 (base 451.46) | 4.24 (base 5.99) |
| mam | 303.92 (base 400.79) | 20 (base 20) | 22052 (base 22916) | 462.39 (base 472.87) | 6.24 (base 5.99) |

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex (compact/wide; avg) | live vertices | wide % | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| sift | plain, one multi-draw per region and layer | 20.066 | 424.387 | 16/28; 17.768 (vanilla 28) | 458712 | 14.735 | 8.142 / 64.0 | 12.249 | 2006 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 10.017 | 49.082 | 16/28; 24.98 (vanilla 28) | 117092 | 74.83 | 2.903 / 64.0 | 3.127 | 2086 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 7.012 | 86.145 | 16/28; 21.789 (vanilla 28) | 956960 | 48.243 | 20.829 / 64.0 | 25.554 | 2545 / 0 | None (None) |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| sift | 244.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, siftec vulkan

| scene | draw calls/frame (avg) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| sift | 425.05 (base 424.25) | 19 (base 19) | 30264 (base 55980) | 439.99 (base 439.9) | 3.71 (base 4.23) |
| hub | 334.92 (base 316.68) | 18 (base 18) | 67500 (base 67484) | 450.88 (base 450.49) | 3.71 (base 4.23) |
| mam | 379.96 (base 352.85) | 21 (base 21) | 24672 (base 24656) | 463.52 (base 462.56) | 3.71 (base 5.46) |

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex (compact/wide; avg) | live vertices | wide % | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| sift | indirect, one multi-draw per arena and layer | 3.012 | 424.755 | 16/28; 17.768 (vanilla 28) | 458712 | 14.735 | 8.142 / 64.0 | 12.249 | 2230 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 4.008 | 49.093 | 16/28; 24.98 (vanilla 28) | 117092 | 74.83 | 2.903 / 64.0 | 3.127 | 2305 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 4.009 | 86.187 | 16/28; 21.775 (vanilla 28) | 961320 | 48.124 | 20.91 / 64.0 | 25.67 | 2769 / 0 | None (None) |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| sift | 244.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |

