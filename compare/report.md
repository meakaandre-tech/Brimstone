# Brimstone CI comparison

**passed**

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
- measure-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- measure-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- measure-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`

GPU error messages in the client logs (Brimstone run / vanilla run of the same profile):

- full: 0 / 0
- old-gpu: 0 / 0
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| spawn | 0.3196 | 10.05 | 0.3152 | 0.2062 | 0.4386 |
| flythrough | 0.0031 | 14.95 | 0.0048 | 0.0035 | 0.0079 |
| water_glass | 0.0049 | 6.45 | 0.005 | 0.0043 | 0.0042 |
| nether | 0.0 | 0.8 | 0.0003 | 0.0003 | 0.0003 |
| mobs | 0.0903 | 0.4 | 0.1009 | 0.0922 | 0.0488 |
| occlusion | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| occlusion_move | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2297 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2309 | 0.0047 | 0.3 | 0.0008 | 0.002 | 0.0069 |
| occlusion_move.open | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.closed | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2300 | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2297 | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2309 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_open | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| translucent_move | 0.0002 | 0.3 | 0.0002 | 0.0001 | 0.0 |
| translucent_move.x2404 | 0.0001 | 0.3 | 0.0003 | 0.0002 | 0.0004 |
| translucent_move.x2414 | 0.0003 | 0.3 | 0.0002 | 0.0001 | 0.0003 |
| translucent_move.x2424 | 0.0001 | 0.3 | 0.0003 | 0.0001 | 0.0002 |
| translucent_move.low | 0.0003 | 0.3 | 0.0011 | 0.0015 | 0.0013 |
| translucent_move.back | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| translucent_move.bare_back | 0.0003 | 0.3 | 0.0003 | 0.0001 | 0.0 |
| water_glass_oit | 0.0044 | 0.3 | 0.0042 | 0.0047 | 0.0043 |
| translucent_move_oit | 0.0001 | 0.3 | 0.0 | 0.0 | 0.0004 |
| translucent_move_oit.x2404 | 0.0002 | 0.3 | 0.0003 | 0.0005 | 0.0004 |
| translucent_move_oit.x2414 | 0.0004 | 0.3 | 0.0004 | 0.0004 | 0.0003 |
| translucent_move_oit.x2424 | 0.0003 | 0.3 | 0.0002 | 0.0001 | 0.0001 |
| translucent_move_oit.low | 0.0007 | 0.3 | 0.0014 | 0.0013 | 0.001 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0001 | 0.0002 | 0.0003 |
| translucent_move_oit.bare_back | 0.0001 | 0.3 | 0.0002 | 0.0003 | 0.0002 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| spawn | 92.72 | 18057 | 201 (697) | 1625 (2184) | 1577 (2141) |
| flythrough | 97.26 | 6020 | 57 (318) | 43 (296) | 78 (338) |
| water_glass | 95.81 | 25508 | 18 (2618) | 23 (2625) | 19 (2732) |
| nether | 99.42 | 905 | 3 (458) | 3 (458) | 3 (470) |
| mobs | 99.25 | 600 | 24 (1523) | 169 (1668) | 45 (1544) |
| occlusion | 98.62 | 22 | 1 (74) | 1 (74) | 1 (74) |
| occlusion_move | 99.15 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2297 | 98.94 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2305 | 98.96 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2309 | 99.09 | 32 | 0 (1) | 0 (2) | 19 (19) |
| occlusion_move.open | 98.55 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.closed | 98.92 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2300 | 98.89 | 90 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2297 | 98.92 | 70 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2305 | 98.95 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2309 | 99.1 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_open | 98.5 | 110 | 0 (0) | 0 (0) | 0 (0) |
| translucent_move | 96.93 | 13 | 2 (77) | 3 (78) | 1 (67) |
| translucent_move.x2404 | 95.93 | 15 | 5 (106) | 7 (108) | 5 (89) |
| translucent_move.x2414 | 95.86 | 13 | 2 (106) | 3 (107) | 4 (101) |
| translucent_move.x2424 | 96.74 | 13 | 3 (80) | 2 (80) | 2 (68) |
| translucent_move.low | 96.11 | 41 | 13 (18) | 15 (20) | 15 (18) |
| translucent_move.back | 96.93 | 13 | 1 (76) | 3 (78) | 1 (67) |
| translucent_move.bare_back | 96.9 | 13 | 3 (78) | 2 (77) | 2 (68) |
| water_glass_oit | 95.81 | 57 | 17 (2615) | 27 (2625) | 20 (2731) |
| translucent_move_oit | 96.93 | 12 | 1 (76) | 2 (77) | 5 (71) |
| translucent_move_oit.x2404 | 95.93 | 16 | 5 (105) | 5 (105) | 3 (87) |
| translucent_move_oit.x2414 | 95.86 | 10 | 1 (104) | 3 (106) | 3 (100) |
| translucent_move_oit.x2424 | 96.74 | 15 | 1 (78) | 1 (79) | 1 (67) |
| translucent_move_oit.low | 96.11 | 40 | 17 (23) | 15 (20) | 12 (14) |
| translucent_move_oit.back | 96.93 | 12 | 2 (77) | 1 (76) | 4 (70) |
| translucent_move_oit.bare_back | 96.9 | 15 | 4 (79) | 5 (80) | 2 (68) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| spawn | 0.3239 / 292 | 0.2892 / 220 | 0.1987 / 1594 |
| flythrough | 0.0038 / 14 | 0.0041 / 6 | 0.0051 / 37 |
| water_glass | 0.005 / 25 | 0.0049 / 27 | 0.0031 / 10 |
| nether | 0.0 / 0 | 0.0 / 0 | 0.008 / 73 |
| mobs | 0.0904 / 140 | 0.1309 / 96 | 0.0847 / 30 |
| occlusion | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2297 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2305 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2309 | 0.0074 / 25 | 0.0 / 0 | 0.0002 / 0 |
| occlusion_move.open | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.closed | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2300 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2297 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2305 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2309 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_open | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| translucent_move | 0.0002 / 1 | 0.0001 / 0 | 0.0 / 0 |
| translucent_move.x2404 | 0.0001 / 3 | 0.0 / 0 | 0.0001 / 1 |
| translucent_move.x2414 | 0.0002 / 0 | 0.0003 / 2 | 0.0 / 1 |
| translucent_move.x2424 | 0.0 / 0 | 0.0 / 0 | 0.0001 / 1 |
| translucent_move.low | 0.001 / 8 | 0.001 / 5 | 0.0003 / 2 |
| translucent_move.back | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| translucent_move.bare_back | 0.0001 / 1 | 0.0001 / 1 | 0.0001 / 1 |
| water_glass_oit | 0.0046 / 23 | 0.003 / 17 | 0.0041 / 19 |
| translucent_move_oit | 0.0001 / 3 | 0.0001 / 1 | 0.0002 / 3 |
| translucent_move_oit.x2404 | 0.0 / 0 | 0.0002 / 0 | 0.0003 / 2 |
| translucent_move_oit.x2414 | 0.0002 / 1 | 0.0002 / 2 | 0.0003 / 2 |
| translucent_move_oit.x2424 | 0.0002 / 1 | 0.0 / 0 | 0.0 / 2 |
| translucent_move_oit.low | 0.0004 / 3 | 0.0004 / 4 | 0.0001 / 1 |
| translucent_move_oit.back | 0.0 / 1 | 0.0004 / 1 | 0.0001 / 1 |
| translucent_move_oit.bare_back | 0.0001 / 0 | 0.0002 / 3 | 0.0004 / 3 |

### Counts, core full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 123.05 (base 123.09) | 14 (base 14) | 21548 (base 22476) | 255.33 (base 259.07) | 5.48 (base 3.99) |
| flythrough | 123.07 (reported, not gated) | 49 (reported, not gated) | 841764 (reported, not gated) | 317.49 (base 324.25) | 4.24 (base 4.24) |
| water_glass | 124.0 (base 182.47) | 15 (base 15) | 14680 (base 15576) | 243.49 (base 250.02) | 5.48 (base 5.99) |
| nether | 120.77 (base 176.56) | 12 (base 12) | 18612 (base 19648) | 264.36 (base 273.53) | 5.48 (base 5.99) |
| mobs | 132.16 (base 216.22) | 17 (base 17) | 616328 (base 1010948) | 263.22 (base 268.37) | 5.48 (base 6.99) |
| occlusion | 92.4 (base 96.43) | 15 (base 15) | 100296 (base 126320) | 264.94 (base 265.1) | 5.48 (base 4.24) |
| occlusion_move | 92.99 (reported, not gated) | 15 (reported, not gated) | 82168 (reported, not gated) | 265.19 (base 265.74) | 5.48 (base 4.24) |
| translucent_move | 124.0 (reported, not gated) | 15 (reported, not gated) | 10184 (reported, not gated) | 267.86 (base 267.48) | 5.48 (base 5.48) |
| water_glass_oit | 129.0 (base 131.0) | 15 (base 15) | 14848 (base 14688) | 245.49 (base 245.02) | 3.99 (base 3.73) |
| translucent_move_oit | 129.0 (reported, not gated) | 15 (reported, not gated) | 9996 (reported, not gated) | 248.19 (base 247.19) | 3.99 (base 3.73) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 123.05 / 123 (9.62) | 123.0 / 123 (11.28) | median and average, x 1.10 + 2 |
| flythrough | 123.07 / 123 (5.83) | 123.05 / 123 (7.63) | median and average, at most vanilla + 2 |
| water_glass | 124.0 / 124 (16.3) | 123.41 / 124 (19.72) | median, at most vanilla |
| nether | 120.77 / 121 (15.63) | 120.79 / 121 (19.08) | median, x 1.10 + 2 |
| mobs | 132.16 / 135 (20.44) | 115.43 / 135 (24.05) | median, x 1.10 + 2 |
| occlusion | 92.4 / 131 (29.95) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 92.99 / 129 (29.88) | 94.46 / 133 (29.97) | median and average, x 1.10 + 2 |
| translucent_move | 124.0 / 124 (16.49) | 121.11 / 124 (20.07) | median, at most vanilla |
| water_glass_oit | 129.0 / 129 (8.79) | 129.0 / 129 (10.79) | median, at most vanilla |
| translucent_move_oit | 129.0 / 129 (8.23) | 129.0 / 129 (10.13) | median and average, at most vanilla |

Terrain GPU memory, core full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.077 | 36.0 / 22.077 | 0.0 / 0.0 | 416.0 / 36.947 |
| flythrough | 112.0 / 83.456 | 112.0 / 83.456 | 0.0 / 0.0 | 416.0 / 147.92 |
| water_glass | 36.0 / 12.595 | 36.0 / 12.595 | 0.0 / 0.0 | 416.0 / 21.814 |
| nether | 20.0 / 15.231 | 20.0 / 15.231 | 0.0 / 0.0 | 256.0 / 26.706 |
| mobs | 16.0 / 9.666 | 16.0 / 9.666 | 0.0 / 0.0 | 256.0 / 16.89 |
| occlusion | 20.0 / 2.595 | 20.0 / 2.595 | 0.0 / 0.0 | 416.0 / 4.492 |
| occlusion_move | 20.0 / 3.47 | 20.0 / 3.47 | 0.0 / 0.0 | 416.0 / 5.984 |
| translucent_move | 28.0 / 13.51 | 28.0 / 13.51 | 0.0 / 0.0 | 416.0 / 22.255 |
| water_glass_oit | 28.0 / 12.569 | 28.0 / 12.569 | 0.0 / 0.0 | 416.0 / 21.814 |
| translucent_move_oit | 28.0 / 12.919 | 28.0 / 12.919 | 0.0 / 0.0 | 416.0 / 21.518 |


Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 753.906 | 16 (vanilla 28) | 1332728 | 40.941 / 0.081 | 22.077 / 36.0 | 35.588 | 2295 / 0 | 4.665 (6.82) |
| flythrough | indirect, one multi-draw per arena and layer | 3.026 | 895.319 | 16 (vanilla 28) | 5056748 | 20.12 / 0.959 | 83.456 / 112.0 | 135.03 | 6520 / 0 | 6.647 (16.238) |
| water_glass | indirect, one multi-draw per arena and layer | 3.009 | 273.837 | 16 (vanilla 28) | 780296 | 33.116 / 0.075 | 12.595 / 36.0 | 20.836 | 2949 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 472.51 | 16 (vanilla 28) | 955944 | 27.814 / 0.166 | 15.231 / 20.0 | 25.526 | 3984 / 0 | 3.086 (5.481) |
| mobs | indirect, one multi-draw per arena and layer | 2.005 | 197.483 | 16 (vanilla 28) | 603124 | 25.216 / 0.144 | 9.666 / 16.0 | 16.105 | 4444 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 69.115 | 16 (vanilla 28) | 162164 | 23.817 / 0.136 | 2.595 / 20.0 | 4.33 | 4711 / 0 | 4.352 (4.775) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 74.452 | 16 (vanilla 28) | 216180 | 22.394 / 0.128 | 3.47 / 20.0 | 5.773 | 4996 / 0 | 7.409 (17.022) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.009 | 247.773 | 16 (vanilla 28) | 832812 | 19.279 / 0.116 | 13.51 / 28.0 | 22.238 | 5776 / 0 | 7.42 (21.13) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.029 | 280.594 | 16 (vanilla 28) | 778636 | 19.786 / 0.081 | 12.569 / 28.0 | 20.792 | 1551 / 0 | 7.126 (9.156) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.03 | 261.841 | 16 (vanilla 28) | 795760 | 12.815 / 0.117 | 12.919 / 28.0 | 21.249 | 2945 / 0 | 7.168 (23.329) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 152.0 | 1.0 | 0.0 | 0 | 0 | 33 (0.028 / 0.039 / 0.054; 0) | 0 | 29336 / 0 / 0 |
| flythrough | 185.872 | 1.0 | 0.0 | 0 | 0 | 845 (0.027 / 0.055 / 3.155; 0) | 34 | 21747 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1308 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1198 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.046 / 0.043 / 1.186; 0) | 32 | 1198 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.144 / 0.163 / 2.716; 0) | 180 | 2317 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 528 | 0 (None / None / None; 0) | 0 | 704 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 495 | 0 (None / None / None; 0) | 0 | 1155 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 323.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.075; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 433.017 | 1.0 (0.0, 0.0) | 1.197 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.039; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.13 (0.386; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.073 (0.379; 0) | 0 | 3594 / 1626884 / 0 / 0 |
| occlusion_move | 50.356 | 10.0 (9.0, 2.361) | 6.0 (6.0, 2.275) | 0.06 (0.276; 0) | 0 | 2777 / 1503460 / 0 / 0 |
| translucent_move | 133.456 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 133.697 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, core old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 158.02 (base 844.2) | 12 (base 13) | 1380 (base 6372) | 257.58 (base 256.29) | 3.73 (base 3.73) |
| flythrough | 176.37 (reported, not gated) | 52 (reported, not gated) | 684508 (reported, not gated) | 320.11 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 135.0 (base 457.8) | 13 (base 14) | 7316 (base 10264) | 242.95 (base 249.83) | 3.73 (base 5.99) |
| nether | 138.85 (base 640.77) | 10 (base 11) | 4512 (base 10656) | 263.61 (base 272.57) | 3.73 (base 5.99) |
| mobs | 142.27 (base 410.19) | 15 (base 16) | 610676 (base 1015892) | 262.61 (base 268.23) | 3.73 (base 5.99) |
| occlusion | 99.46 (base 106.62) | 13 (base 14) | 98148 (base 124172) | 264.29 (base 264.64) | 3.73 (base 4.24) |
| occlusion_move | 100.9 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 264.61 (base 265.66) | 3.73 (base 5.99) |
| translucent_move | 133.0 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 267.24 (base 268.19) | 3.73 (base 3.73) |
| water_glass_oit | 140.0 (base 154.0) | 13 (base 14) | 7092 (base 7604) | 244.8 (base 245.78) | 3.48 (base 3.73) |
| translucent_move_oit | 140.0 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 247.52 (base 248.15) | 3.48 (base 3.73) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 158.02 / 158 (10.54) | 846.0 / 846 (14.06) | median and average, x 1.10 + 2 |
| flythrough | 176.37 / 174 (5.92) | 1043.65 / 1040 (10.78) | median and average, at most vanilla |
| water_glass | 135.0 / 135 (17.57) | 360.83 / 390 (26.61) | median, at most vanilla |
| nether | 138.85 / 139 (16.25) | 560.22 / 590 (26.84) | median, x 1.10 + 2 |
| mobs | 142.27 / 144 (20.14) | 291.53 / 330 (29.9) | median, x 1.10 + 2 |
| occlusion | 99.46 / 138 (29.95) | 161.33 / 200 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 100.9 / 136 (29.89) | 166.6 / 201 (30.0) | median and average, x 1.10 + 2 |
| translucent_move | 133.0 / 133 (17.26) | 332.42 / 366 (28.89) | median, at most vanilla |
| water_glass_oit | 140.0 / 140 (9.07) | 401.0 / 401 (14.34) | median, at most vanilla |
| translucent_move_oit | 140.0 / 140 (8.47) | 383.99 / 384 (13.58) | median and average, at most vanilla |

Terrain GPU memory, core old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.192 | 36.0 / 22.192 | 0.0 / 0.0 | 416.0 / 36.877 |
| flythrough | 112.0 / 84.053 | 112.0 / 84.053 | 0.0 / 0.0 | 416.0 / 152.07 |
| water_glass | 36.0 / 12.595 | 36.0 / 12.595 | 0.0 / 0.0 | 416.0 / 21.814 |
| nether | 20.0 / 15.226 | 20.0 / 15.226 | 0.0 / 0.0 | 256.0 / 26.704 |
| mobs | 16.0 / 9.666 | 16.0 / 9.666 | 0.0 / 0.0 | 256.0 / 16.89 |
| occlusion | 20.0 / 2.621 | 20.0 / 2.621 | 0.0 / 0.0 | 416.0 / 4.276 |
| occlusion_move | 20.0 / 3.402 | 20.0 / 3.402 | 0.0 / 0.0 | 416.0 / 5.804 |
| translucent_move | 28.0 / 13.118 | 28.0 / 13.118 | 0.0 / 0.0 | 416.0 / 22.611 |
| water_glass_oit | 28.0 / 12.595 | 28.0 / 12.595 | 0.0 / 0.0 | 416.0 / 21.814 |
| translucent_move_oit | 28.0 / 12.509 | 28.0 / 12.509 | 0.0 / 0.0 | 416.0 / 21.52 |


Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 38.181 | 758.595 | 16 (vanilla 28) | 1338312 | 40.002 / 0.088 | 22.192 / 36.0 | 35.737 | 2293 / 0 | 4.853 (9.816) |
| flythrough | plain, one multi-draw per region and layer | 56.72 | 896.407 | 16 (vanilla 28) | 5088916 | 19.994 / 0.958 | 84.053 / 112.0 | 135.889 | 6333 / 0 | 6.571 (17.112) |
| water_glass | plain, one multi-draw per region and layer | 14.04 | 273.778 | 16 (vanilla 28) | 780296 | 32.472 / 0.078 | 12.595 / 36.0 | 20.836 | 2974 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.062 | 472.454 | 16 (vanilla 28) | 955784 | 27.383 / 0.199 | 15.226 / 20.0 | 25.522 | 3991 / 0 | 2.85 (4.162) |
| mobs | plain, one multi-draw per region and layer | 11.027 | 197.49 | 16 (vanilla 28) | 603124 | 24.887 / 0.173 | 9.666 / 16.0 | 16.105 | 4430 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 72.12 | 16 (vanilla 28) | 164148 | 23.808 / 0.165 | 2.621 / 20.0 | 4.383 | 4653 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.993 | 77.442 | 16 (vanilla 28) | 213936 | 22.456 / 0.156 | 3.402 / 20.0 | 5.713 | 4952 / 0 | 6.586 (19.702) |
| translucent_move | plain, one multi-draw per region and layer | 12.035 | 253.104 | 16 (vanilla 28) | 813004 | 19.458 / 0.14 | 13.118 / 28.0 | 21.71 | 5709 / 0 | 10.104 (23.207) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.088 | 282.552 | 16 (vanilla 28) | 780296 | 23.09 / 0.082 | 12.595 / 28.0 | 20.836 | 1744 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.095 | 267.095 | 16 (vanilla 28) | 774324 | 15.108 / 0.12 | 12.509 / 28.0 | 20.677 | 3124 / 0 | 9.227 (18.773) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 154.0 | 16.0 | 0.0 | 0 | 0 | 49 (0.039 / 0.085 / 0.154; 0) | 0 | 32494 / 0 / 0 |
| flythrough | 186.403 | 25.655 | 0.0 | 0 | 0 | 854 (0.016 / 0.045 / 0.353; 0) | 43 | 22182 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1408 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.018 / 0.053 / 0.059; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 199 (0.099 / 0.155 / 0.178; 0) | 199 | 2422 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 546 | 0 (None / None / None; 0) | 0 | 728 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 510 | 0 (None / None / None; 0) | 0 | 1190 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 325.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.027 (0.074; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 434.462 | 1.0 (0.0, 0.0) | 1.185 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.019 (0.08; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.141 (0.453; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 50.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.066 (0.121; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 52.351 | 10.0 (9.0, 2.356) | 6.0 (6.0, 2.281) | 0.063 (0.266; 0) | 0 | 2773 / 1500080 / 0 / 0 |
| translucent_move | 136.642 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.0 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.759 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.004 (0.012; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, core vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 123.05 (base 124.84) | 14 (base 15) | 20932 (base 27764) | 255.86 (base 257.2) | 3.71 (base 3.98) |
| flythrough | 123.06 (reported, not gated) | 46 (reported, not gated) | 866536 (reported, not gated) | 317.82 (base 318.06) | 3.71 (base 4.23) |
| water_glass | 124.0 (base 153.28) | 15 (base 15) | 15128 (base 15108) | 243.95 (base 244.39) | 3.71 (base 4.23) |
| nether | 120.71 (base 142.02) | 12 (base 12) | 17964 (base 19912) | 265.09 (base 268.95) | 3.71 (base 4.23) |
| mobs | 135.0 (base 208.6) | 17 (base 17) | 616328 (base 1008176) | 263.56 (base 264.31) | 3.71 (base 6.23) |
| occlusion | 92.46 (base 97.95) | 15 (base 15) | 100388 (base 126356) | 265.24 (base 264.78) | 3.71 (base 3.98) |
| occlusion_move | 93.31 (reported, not gated) | 15 (reported, not gated) | 82260 (reported, not gated) | 265.53 (base 265.16) | 3.71 (base 4.23) |
| translucent_move | 124.0 (reported, not gated) | 15 (reported, not gated) | 10032 (reported, not gated) | 267.93 (base 268.35) | 3.71 (base 5.46) |
| water_glass_oit | 129.0 (base 131.0) | 15 (base 15) | 15184 (base 15024) | 245.44 (base 245.99) | 3.46 (base 3.46) |
| translucent_move_oit | 129.0 (reported, not gated) | 15 (reported, not gated) | 10032 (reported, not gated) | 247.48 (base 248.38) | 3.46 (base 3.46) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 123.05 / 123 (9.35) | 123.52 / 124 (11.4) | median and average, x 1.10 + 2 |
| flythrough | 123.06 / 123 (5.76) | 123.07 / 123 (7.23) | median and average, at most vanilla + 2 |
| water_glass | 124.0 / 124 (15.17) | 124.0 / 124 (18.4) | median, at most vanilla |
| nether | 120.71 / 121 (15.57) | 120.78 / 121 (18.29) | median, x 1.10 + 2 |
| mobs | 135.0 / 135 (19.33) | 117.51 / 135 (23.58) | median, x 1.10 + 2 |
| occlusion | 92.46 / 131 (29.95) | 94.33 / 133 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 93.31 / 129 (29.75) | 94.4 / 133 (29.95) | median and average, x 1.10 + 2 |
| translucent_move | 124.0 / 124 (15.78) | 123.7 / 124 (19.09) | median, at most vanilla |
| water_glass_oit | 129.0 / 129 (8.18) | 129.0 / 129 (9.99) | median, at most vanilla |
| translucent_move_oit | 129.0 / 129 (7.71) | 129.0 / 129 (9.35) | median and average, at most vanilla |

Terrain GPU memory, core vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.581 | 36.0 / 21.581 | 0.0 / 0.0 | 416.0 / 39.033 |
| flythrough | 112.0 / 83.17 | 112.0 / 83.17 | 0.0 / 0.0 | 416.0 / 144.898 |
| water_glass | 36.0 / 12.595 | 36.0 / 12.595 | 0.0 / 0.0 | 416.0 / 21.814 |
| nether | 20.0 / 15.228 | 20.0 / 15.228 | 0.0 / 0.0 | 256.0 / 26.705 |
| mobs | 16.0 / 9.666 | 16.0 / 9.666 | 0.0 / 0.0 | 256.0 / 16.89 |
| occlusion | 20.0 / 2.485 | 20.0 / 2.485 | 0.0 / 0.0 | 416.0 / 4.492 |
| occlusion_move | 20.0 / 3.374 | 20.0 / 3.374 | 0.0 / 0.0 | 416.0 / 5.984 |
| translucent_move | 28.0 / 12.946 | 28.0 / 12.946 | 0.0 / 0.0 | 416.0 / 22.647 |
| water_glass_oit | 28.0 / 12.569 | 28.0 / 12.569 | 0.0 / 0.0 | 416.0 / 21.814 |
| translucent_move_oit | 28.0 / 12.506 | 28.0 / 12.506 | 0.0 / 0.0 | 416.0 / 21.736 |


Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 731.914 | 16 (vanilla 28) | 1300752 | 42.361 / 0.081 | 21.581 / 36.0 | 34.734 | 2378 / 0 | 3.58 (5.456) |
| flythrough | indirect, one multi-draw per arena and layer | 3.026 | 892.296 | 16 (vanilla 28) | 5036224 | 22.756 / 0.904 | 83.17 / 112.0 | 134.482 | 6753 / 0 | 5.661 (14.312) |
| water_glass | indirect, one multi-draw per arena and layer | 3.01 | 273.901 | 16 (vanilla 28) | 780296 | 34.447 / 0.075 | 12.595 / 36.0 | 20.836 | 3030 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 472.514 | 16 (vanilla 28) | 955928 | 28.944 / 0.179 | 15.228 / 20.0 | 25.526 | 4072 / 0 | 2.421 (3.601) |
| mobs | indirect, one multi-draw per arena and layer | 2.005 | 197.51 | 16 (vanilla 28) | 603124 | 26.306 / 0.157 | 9.666 / 16.0 | 16.105 | 4512 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 72.12 | 16 (vanilla 28) | 155056 | 24.916 / 0.148 | 2.485 / 20.0 | 4.14 | 4791 / 0 | 2.808 (4.196) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 77.421 | 16 (vanilla 28) | 210424 | 23.492 / 0.14 | 3.374 / 20.0 | 5.619 | 5100 / 0 | 5.557 (12.158) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.01 | 248.171 | 16 (vanilla 28) | 796364 | 20.152 / 0.125 | 12.946 / 28.0 | 21.265 | 5942 / 0 | 5.164 (13.252) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.031 | 280.712 | 16 (vanilla 28) | 778636 | 20.509 / 0.068 | 12.569 / 28.0 | 20.792 | 1533 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.032 | 262.136 | 16 (vanilla 28) | 768628 | 13.469 / 0.115 | 12.506 / 28.0 | 20.525 | 2912 / 0 | 5.456 (13.916) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 150.0 | 1.0 | 0.0 | 0 | 0 | 66 (0.03 / 0.058 / 0.101; 0) | 0 | 28050 / 0 / 0 |
| flythrough | 185.388 | 1.0 | 0.0 | 0 | 0 | 847 (0.022 / 0.062 / 0.664; 0) | 42 | 21505 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1216 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.01 / 0.014 / 0.014; 0) | 32 | 1190 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 186 (0.139 / 0.219 / 0.711; 0) | 186 | 2212 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 492 | 0 (None / None / None; 0) | 0 | 656 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 465 | 0 (None / None / None; 0) | 0 | 1085 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 312.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.03 (0.079; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 433.164 | 1.0 (0.0, 0.0) | 1.164 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.039; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.114 (0.247; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 50.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.065 (0.102; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 52.345 | 10.0 (9.0, 2.36) | 6.0 (6.0, 2.279) | 0.059 (0.525; 0) | 0 | 2760 / 1493790 / 0 / 0 |
| translucent_move | 133.785 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 133.832 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

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
- measure-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- measure-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- measure-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`

GPU error messages in the client logs (Brimstone run / vanilla run of the same profile):

- full: 2 / 2
- old-gpu: 18 / 18
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| factory | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 3.4359 | 8.75 | 3.3185 | 3.3857 | 4.3456 |
| mam | 3.4716 | 9.3 | 3.2299 | 3.4045 | 4.5799 |
| map | 3.5851 | 11.35 | 5.5846 | 5.1285 | 5.375 |
| hologram | 0.0221 | 0.55 | 0.0276 | 0.0051 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.57 | 500 | 0 (108) | 0 (108) | 0 (116) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 95.18 | 12506 | 1544 (1545) | 4 (5) | 32 (33) |
| mam | 84.79 | 155106 | 481 (481) | 5 (5) | 33 (59) |
| map | 91.85 | not gated (SIFTEC's map shows whichever tiles it has scanned when it opens, so vanilla runs differ from each other by up to 22,532 strict pixels there (35-58k pixels in all after two attempts to wait for a steady map); package 7 (GUI) must give it real strict coverage) | 5760 (5760) | 6160 (6160) | 1584 (1584) |
| hologram | 99.97 | 413 | 50 (4303) | 44 (4296) | None (None) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 3.438 / 8 | 4.3704 / 0 | 3.3037 / 8 |
| mam | 3.4877 / 8 | 4.6366 / 0 | 3.1985 / 8 |
| map | 4.556 / 864 | 2.3346 / 4080 | 5.5859 / 2912 |
| hologram | 0.0221 / 12 | 0.0025 / 16 | skipped |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 105.43 (base 111.52) | 14 (base 14) | 6396 (base 6488) | 421.53 (base 421.74) | 4.24 (base 5.48) |
| sift | 153.0 (base 155.82) | 18 (base 19) | 16632 (base 81788) | 438.89 (base 440.07) | 4.24 (base 5.87) |
| hub | 114.79 (base 116.59) | 16 (base 18) | 66076 (base 67484) | 451.52 (base 451.33) | 4.24 (base 4.24) |
| mam | 116.09 (base 158.03) | 19 (base 21) | 23248 (base 24656) | 464.1 (base 471.8) | 8.24 (base 8.12) |
| map | 170.06 (base 177.0) | 14 (base 14) | 46880 (base 47032) | 503.26 (base 504.13) | 8.24 (base 8.24) |
| hologram | 173 median (avg 134.92) (base 165) | 22 (base 24) | 71652 (base 70368) | 468.8 (base 453.57) | 8.31 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 105.43 / 151 (29.66) | 106.15 / 151 (29.24) | median and average, x 1.10 + 2 |
| sift | 153.0 / 153 (18.53) | 153.0 / 153 (15.27) | median, x 1.10 + 2 |
| hub | 114.79 / 161 (29.97) | 115.73 / 161 (29.64) | median and average, x 1.10 + 2 |
| mam | 116.09 / 162 (29.91) | 118.41 / 162 (29.02) | median and average, x 1.10 + 2 |
| map | 170.06 / 170 (10.94) | 170.0 / 170 (9.24) | median and average, x 1.10 + 2 |
| hologram | 134.92 / 173 (27.42) | 159.48 / 173 (21.92) | median, at most vanilla |

Terrain GPU memory, siftec full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| sift | 16.0 / 7.39 | 16.0 / 7.39 | 0.0 / 0.0 | 416.0 / 12.893 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.231 | 32.0 / 20.231 | 0.0 / 0.0 | 416.0 / 34.139 |
| map | 72.0 / 32.541 | 72.0 / 32.541 | 0.0 / 0.0 | 416.0 / 52.111 |
| hologram | 72.0 / 0.238 | 72.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |


Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 73.123 | 16 (vanilla 28) | 91456 | 22.278 / 0.192 | 1.448 / 28.0 | 2.442 | 2537 / 0 | 3.026 (7.922) |
| sift | indirect, one multi-draw per arena and layer | 3.008 | 430.159 | 16 (vanilla 28) | 462640 | 21.263 / 0.227 | 7.39 / 16.0 | 12.354 | 3549 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.083 | 16 (vanilla 28) | 117492 | 20.808 / 0.222 | 1.879 / 12.0 | 3.137 | 3640 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.146 | 16 (vanilla 28) | 1187976 | 21.64 / 0.256 | 20.231 / 32.0 | 31.722 | 4287 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.014 | 551.518 | 16 (vanilla 28) | 2036668 | 21.097 / 0.829 | 32.541 / 72.0 | 54.385 | 8110 / 0 | 3.241 (7.908) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.024 | 16 (vanilla 28) | 13720 | 21.05 / 0.825 | 0.238 / 72.0 | 0.366 | 8172 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2226 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| map | 36.0 | 1.0 | 0.0 | 0 | 0 | 5 (0.005 / 0.008 / 0.008; 0) | 0 | 7884 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2196 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 33.0 (0.0, 0.0) | 0.003 (0.019; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 115.66 (base 119.43) | 13 (base 13) | 48472 (base 48604) | 421.55 (base 421.54) | 4.24 (base 5.48) |
| sift | 171.0 (base 670.27) | 16 (base 18) | 4148 (base 81788) | 439.37 (base 441.24) | 4.24 (base 4.24) |
| hub | 121.39 (base 162.94) | 14 (base 17) | 64564 (base 66484) | 451.74 (base 451.46) | 4.24 (base 5.99) |
| mam | 122.43 (base 222.38) | 17 (base 20) | 20724 (base 22916) | 464.67 (base 472.87) | 4.24 (base 5.99) |
| map | 206.0 (base 225.0) | 12 (base 12) | 31404 (base 31404) | 504.65 (base 506.16) | 4.24 (base 6.31) |
| hologram | 174 median (avg 173.25) (base 166) | 20 (base 23) | 71452 (base 70624) | 469.24 (base 452.39) | 4.24 (base 6.31) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 115.66 / 159 (28.89) | 177.73 / 224 (29.96) | median and average, x 1.10 + 2 |
| sift | 171.0 / 171 (14.48) | 562.74 / 579 (22.55) | median, x 1.10 + 2 |
| hub | 121.39 / 165 (28.98) | 161.87 / 208 (29.96) | median and average, x 1.10 + 2 |
| mam | 122.43 / 164 (28.44) | 200.02 / 246 (29.93) | median and average, x 1.10 + 2 |
| map | 206.0 / 206 (8.77) | 717.87 / 718 (15.2) | median and average, x 1.10 + 2 |
| hologram | 173.25 / 174 (18.91) | 138.27 / 184 (29.61) | median, at most vanilla |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| sift | 16.0 / 7.39 | 16.0 / 7.39 | 0.0 / 0.0 | 416.0 / 12.92 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.248 | 32.0 / 20.248 | 0.0 / 0.0 | 416.0 / 34.175 |
| map | 72.0 / 34.111 | 72.0 / 34.111 | 0.0 / 0.0 | 416.0 / 54.37 |
| hologram | 72.0 / 0.238 | 72.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |


Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.014 | 73.126 | 16 (vanilla 28) | 91456 | 19.678 / 0.233 | 1.448 / 28.0 | 2.442 | 2230 / 0 | 4.286 (11.493) |
| sift | plain, one multi-draw per region and layer | 21.073 | 430.484 | 16 (vanilla 28) | 462640 | 18.933 / 0.267 | 7.39 / 16.0 | 12.354 | 3239 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.086 | 16 (vanilla 28) | 117492 | 18.47 / 0.259 | 1.879 / 12.0 | 3.137 | 3332 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.009 | 87.153 | 16 (vanilla 28) | 1189260 | 19.955 / 0.292 | 20.248 / 32.0 | 31.757 | 3986 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 39.223 | 550.126 | 16 (vanilla 28) | 2132592 | 19.975 / 0.932 | 34.111 / 72.0 | 56.946 | 8211 / 0 | 4.307 (8.984) |
| hologram | plain, one multi-draw per region and layer | 3.008 | 13.034 | 16 (vanilla 28) | 13720 | 19.932 / 0.926 | 0.238 / 72.0 | 0.366 | 8284 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1740 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 581 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 569 / 0 / 0 |
| map | 35.0 | 15.0 | 0.0 | 0 | 0 | 1 (0.014 / 0.014 / 0.014; 0) | 0 | 6160 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1516 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 44.0 (0.0, 0.0) | 0.005 (0.048; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 107.63 (base 111.25) | 15 (base 15) | 50956 (base 51148) | 420.2 (base 420.23) | 4.23 (base 3.71) |
| sift | 153.0 (base 150.31) | 18 (base 19) | 16632 (base 81788) | 437.22 (base 439.9) | 4.23 (base 4.23) |
| hub | 115.02 (base 136.98) | 16 (base 18) | 66172 (base 67484) | 449.99 (base 450.49) | 4.23 (base 4.23) |
| mam | 117.03 (base 171.53) | 19 (base 21) | 23344 (base 24656) | 463.33 (base 462.56) | 8.23 (base 5.46) |
| map | 170.0 (base 177.0) | 14 (base 14) | 46876 (base 47072) | 500.41 (base 504.09) | 8.23 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 107.63 / 153 (29.54) | 109.42 / 153 (28.8) | median and average, x 1.10 + 2 |
| sift | 153.0 / 153 (16.61) | 153.0 / 153 (13.09) | median, x 1.10 + 2 |
| hub | 115.02 / 161 (29.93) | 116.55 / 161 (29.44) | median and average, x 1.10 + 2 |
| mam | 117.03 / 162 (29.55) | 124.21 / 162 (27.46) | median and average, x 1.10 + 2 |
| map | 170.0 / 170 (11.07) | 170.0 / 170 (8.43) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| sift | 16.0 / 7.374 | 16.0 / 7.374 | 0.0 / 0.0 | 416.0 / 12.893 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.335 | 32.0 / 20.335 | 0.0 / 0.0 | 416.0 / 34.13 |
| map | 56.0 / 31.032 | 56.0 / 31.032 | 0.0 / 0.0 | 416.0 / 57.741 |


Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 73.124 | 16 (vanilla 28) | 91456 | 21.967 / 0.207 | 1.448 / 28.0 | 2.442 | 2732 / 0 | 2.131 (2.392) |
| sift | indirect, one multi-draw per arena and layer | 3.009 | 430.292 | 16 (vanilla 28) | 461676 | 21.023 / 0.24 | 7.374 / 16.0 | 12.328 | 3739 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.083 | 16 (vanilla 28) | 117492 | 20.56 / 0.234 | 1.879 / 12.0 | 3.137 | 3832 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.147 | 16 (vanilla 28) | 1194532 | 21.413 / 0.266 | 20.335 / 32.0 | 31.897 | 4486 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.014 | 550.48 | 16 (vanilla 28) | 1939604 | 21.429 / 0.922 | 31.032 / 56.0 | 51.793 | 8852 / 0 | 4.138 (9.41) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1998 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 592 / 0 / 0 |
| map | 35.0 | 1.0 | 0.0 | 0 | 0 | 3 (0.004 / 0.005 / 0.005; 0) | 0 | 7770 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 44.0 (0.0, 0.0) | 0.004 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

