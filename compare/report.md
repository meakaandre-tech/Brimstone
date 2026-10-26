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
| spawn | 0.3002 | 10.05 | 0.3054 | 0.1938 | 0.4454 |
| flythrough | 0.0003 | 14.95 | 0.001 | 0.0013 | 0.0011 |
| water_glass | 0.0033 | 6.45 | 0.0048 | 0.0044 | 0.0049 |
| nether | 0.0 | 0.8 | 0.0072 | 0.0003 | 0.0003 |
| mobs | 0.0143 | 0.4 | 0.0435 | 0.1043 | 0.0528 |
| occlusion | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| occlusion_move | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2297 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2309 | 0.0018 | 0.3 | 0.0034 | 0.0039 | 0.0035 |
| occlusion_move.open | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.closed | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2300 | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2297 | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2309 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_open | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| translucent_move | 0.0002 | 0.3 | 0.0001 | 0.0001 | 0.0 |
| translucent_move.x2404 | 0.0004 | 0.3 | 0.0005 | 0.0004 | 0.0003 |
| translucent_move.x2414 | 0.0002 | 0.3 | 0.0002 | 0.0003 | 0.0003 |
| translucent_move.x2424 | 0.0001 | 0.3 | 0.0003 | 0.0002 | 0.0001 |
| translucent_move.low | 0.0002 | 0.3 | 0.001 | 0.0011 | 0.0012 |
| translucent_move.back | 0.0001 | 0.3 | 0.0 | 0.0002 | 0.0001 |
| translucent_move.bare_back | 0.0001 | 0.3 | 0.0 | 0.0001 | 0.0001 |
| water_glass_oit | 0.0038 | 0.3 | 0.0043 | 0.004 | 0.0043 |
| translucent_move_oit | 0.0001 | 0.3 | 0.0 | 0.0004 | 0.0001 |
| translucent_move_oit.x2404 | 0.0001 | 0.3 | 0.0002 | 0.0003 | 0.0002 |
| translucent_move_oit.x2414 | 0.0001 | 0.3 | 0.0002 | 0.0002 | 0.0003 |
| translucent_move_oit.x2424 | 0.0001 | 0.3 | 0.0001 | 0.0003 | 0.0002 |
| translucent_move_oit.low | 0.0002 | 0.3 | 0.0013 | 0.001 | 0.0016 |
| translucent_move_oit.back | 0.0002 | 0.3 | 0.0001 | 0.0001 | 0.0002 |
| translucent_move_oit.bare_back | 0.0001 | 0.3 | 0.0 | 0.0004 | 0.0003 |
| flat_plants | 0.0 | 0.3 | 0.023 | 0.023 | 0.0226 |
| renderer_off | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| spawn | 92.7 | 18057 | 263 (765) | 256 (756) | 361 (910) |
| flythrough | 97.27 | 6020 | 47 (293) | 47 (293) | 51 (294) |
| water_glass | 95.81 | 25508 | 24 (2623) | 19 (2618) | 22 (2737) |
| nether | 99.42 | 905 | 66 (521) | 3 (458) | 3 (470) |
| mobs | 99.28 | 600 | 20 (20) | 0 (0) | 37 (39) |
| occlusion | 98.62 | 22 | 1 (74) | 1 (74) | 1 (74) |
| occlusion_move | 99.15 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2297 | 98.94 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2305 | 98.96 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2309 | 99.09 | 32 | 0 (0) | 19 (19) | 19 (19) |
| occlusion_move.open | 98.55 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.closed | 98.92 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2300 | 98.89 | 90 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2297 | 98.92 | 70 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2305 | 98.95 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2309 | 99.1 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_open | 98.5 | 110 | 0 (0) | 0 (0) | 0 (0) |
| translucent_move | 96.93 | 13 | 1 (77) | 1 (76) | 3 (69) |
| translucent_move.x2404 | 95.93 | 15 | 4 (105) | 4 (104) | 4 (88) |
| translucent_move.x2414 | 95.86 | 13 | 2 (105) | 2 (105) | 3 (101) |
| translucent_move.x2424 | 96.74 | 13 | 2 (79) | 2 (79) | 1 (67) |
| translucent_move.low | 96.11 | 41 | 13 (18) | 14 (20) | 16 (18) |
| translucent_move.back | 96.93 | 13 | 2 (77) | 3 (78) | 2 (68) |
| translucent_move.bare_back | 96.9 | 13 | 1 (77) | 3 (78) | 2 (68) |
| water_glass_oit | 95.81 | 57 | 27 (2627) | 17 (2615) | 17 (2730) |
| translucent_move_oit | 96.93 | 12 | 1 (76) | 6 (81) | 3 (69) |
| translucent_move_oit.x2404 | 95.93 | 16 | 4 (104) | 4 (105) | 3 (87) |
| translucent_move_oit.x2414 | 95.86 | 10 | 3 (106) | 3 (106) | 4 (101) |
| translucent_move_oit.x2424 | 96.74 | 15 | 3 (81) | 2 (79) | 2 (68) |
| translucent_move_oit.low | 96.11 | 40 | 18 (23) | 16 (22) | 14 (16) |
| translucent_move_oit.back | 96.93 | 12 | 1 (76) | 1 (76) | 3 (69) |
| translucent_move_oit.bare_back | 96.9 | 15 | 2 (77) | 4 (79) | 3 (69) |
| flat_plants | 98.22 | 350 | 225 (7416) | 225 (7416) | 217 (7347) |
| renderer_off | 99.2 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| spawn | 0.4452 / 209 | 0.1911 / 259 | 0.4357 / 264 |
| flythrough | 0.0003 / 0 | 0.0003 / 0 | 0.0 / 0 |
| water_glass | 0.0035 / 15 | 0.0038 / 16 | 0.004 / 12 |
| nether | 0.0001 / 1 | 0.0 / 0 | 0.0 / 0 |
| mobs | 0.0433 / 36 | 0.0629 / 39 | 0.1387 / 496 |
| occlusion | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2297 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2305 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2309 | 0.0053 / 0 | 0.0039 / 19 | 0.0055 / 0 |
| occlusion_move.open | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.closed | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2300 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2297 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2305 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2309 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_open | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| translucent_move | 0.0001 / 0 | 0.0002 / 2 | 0.0 / 0 |
| translucent_move.x2404 | 0.0002 / 0 | 0.0001 / 3 | 0.0002 / 3 |
| translucent_move.x2414 | 0.0002 / 1 | 0.0002 / 2 | 0.0001 / 4 |
| translucent_move.x2424 | 0.0003 / 2 | 0.0 / 0 | 0.0 / 0 |
| translucent_move.low | 0.0003 / 5 | 0.0004 / 5 | 0.0007 / 4 |
| translucent_move.back | 0.0 / 0 | 0.0001 / 2 | 0.0001 / 2 |
| translucent_move.bare_back | 0.0 / 0 | 0.0 / 1 | 0.0001 / 0 |
| water_glass_oit | 0.0033 / 17 | 0.0038 / 13 | 0.0048 / 19 |
| translucent_move_oit | 0.0001 / 1 | 0.0001 / 1 | 0.0 / 1 |
| translucent_move_oit.x2404 | 0.0 / 0 | 0.0001 / 1 | 0.0001 / 4 |
| translucent_move_oit.x2414 | 0.0001 / 1 | 0.0002 / 2 | 0.0002 / 1 |
| translucent_move_oit.x2424 | 0.0001 / 3 | 0.0001 / 0 | 0.0 / 0 |
| translucent_move_oit.low | 0.0003 / 4 | 0.0002 / 2 | 0.0011 / 6 |
| translucent_move_oit.back | 0.0001 / 0 | 0.0001 / 3 | 0.0001 / 1 |
| translucent_move_oit.bare_back | 0.0 / 1 | 0.0002 / 1 | 0.0002 / 1 |
| flat_plants | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| renderer_off | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, core full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 123.31 (base 123.09) | 14 (base 14) | 20912 (base 22476) | 255.51 (base 259.07) | 4.24 (base 3.99) |
| flythrough | 123.07 (reported, not gated) | 33 (reported, not gated) | 322284 (reported, not gated) | 320.03 (base 324.25) | 5.99 (base 4.24) |
| water_glass | 120.05 (base 182.47) | 15 (base 15) | 15148 (base 15576) | 243.62 (base 250.02) | 4.24 (base 5.99) |
| nether | 120.73 (base 176.56) | 12 (base 12) | 18376 (base 19648) | 264.65 (base 273.53) | 4.24 (base 5.99) |
| mobs | 110.34 (base 216.22) | 16 (base 17) | 616168 (base 1010948) | 263.21 (base 268.37) | 4.24 (base 6.99) |
| occlusion | 92.27 (base 96.43) | 15 (base 15) | 100296 (base 126320) | 264.92 (base 265.1) | 4.24 (base 4.24) |
| occlusion_move | 92.74 (reported, not gated) | 15 (reported, not gated) | 82168 (reported, not gated) | 265.03 (base 265.74) | 4.24 (base 4.24) |
| translucent_move | 118.7 (reported, not gated) | 15 (reported, not gated) | 10144 (reported, not gated) | 267.88 (base 267.48) | 4.24 (base 5.48) |
| water_glass_oit | 129.0 (base 131.0) | 15 (base 15) | 14532 (base 14688) | 245.9 (base 245.02) | 3.87 (base 3.73) |
| translucent_move_oit | 129.0 (reported, not gated) | 15 (reported, not gated) | 10240 (reported, not gated) | 248.72 (base 247.19) | 3.99 (base 3.73) |
| flat_plants | 121.19 (base 123.98) | 15 (base 15) | 9476 (base 9812) | 247.68 (base 246.92) | 4.24 (base 3.99) |
| renderer_off | 84.66 (base 96.01) | 14 (base 14) | 7148 (base 7164) | 240.51 (base 241.33) | 3.87 (base 4.12) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 123.31 / 123 (11.98) | 124.95 / 125 (8.56) | median and average, x 1.10 + 2 |
| flythrough | 123.07 / 123 (8.07) | 123.05 / 123 (5.79) | median and average, at most vanilla + 2 |
| water_glass | 120.05 / 124 (20.57) | 124.0 / 124 (15.26) | median, at most vanilla |
| nether | 120.73 / 121 (18.89) | 121.0 / 121 (15.33) | median, x 1.10 + 2 |
| mobs | 110.34 / 135 (25.41) | 134.0 / 134 (19.51) | median, x 1.10 + 2 |
| occlusion | 92.27 / 131 (30.0) | 94.27 / 133 (29.98) | median and average, x 1.10 + 2 |
| occlusion_move | 92.74 / 129 (29.95) | 94.4 / 133 (29.95) | median and average, x 1.10 + 2 |
| translucent_move | 118.7 / 124 (20.8) | 124.0 / 124 (15.54) | median, at most vanilla |
| water_glass_oit | 129.0 / 129 (9.96) | 129.0 / 129 (8.63) | median, at most vanilla |
| translucent_move_oit | 129.0 / 129 (9.62) | 129.0 / 129 (8.09) | median and average, at most vanilla |
| flat_plants | 121.19 / 124 (20.43) | 123.9 / 124 (16.05) | median, x 1.10 + 2 |
| renderer_off | 84.66 / 123 (29.84) | 95.67 / 123 (26.15) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.669 | 36.0 / 21.669 | 0.0 / 0.0 | 416.0 / 39.885 |
| flythrough | 112.0 / 83.951 | 112.0 / 83.951 | 0.0 / 0.0 | 416.0 / 142.601 |
| water_glass | 36.0 / 12.57 | 36.0 / 12.57 | 0.0 / 0.0 | 416.0 / 22.401 |
| nether | 20.0 / 15.302 | 20.0 / 15.302 | 0.0 / 0.0 | 256.0 / 26.84 |
| mobs | 16.0 / 9.627 | 16.0 / 9.627 | 0.0 / 0.0 | 256.0 / 16.813 |
| occlusion | 20.0 / 2.607 | 20.0 / 2.607 | 0.0 / 0.0 | 416.0 / 4.282 |
| occlusion_move | 20.0 / 3.476 | 20.0 / 3.476 | 0.0 / 0.0 | 416.0 / 5.777 |
| translucent_move | 28.0 / 12.924 | 28.0 / 12.924 | 0.0 / 0.0 | 416.0 / 22.522 |
| water_glass_oit | 28.0 / 12.431 | 28.0 / 12.431 | 0.0 / 0.0 | 416.0 / 21.634 |
| translucent_move_oit | 28.0 / 12.585 | 28.0 / 12.585 | 0.0 / 0.0 | 416.0 / 21.831 |
| flat_plants | 28.0 / 13.801 | 28.0 / 13.801 | 0.0 / 0.0 | 416.0 / 23.256 |
| renderer_off | 28.0 / 8.663 | 28.0 / 8.663 | 0.0 / 0.0 | 416.0 / 14.79 |


Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.013 | 730.042 | 16 (vanilla 28) | 1307608 | 42.25 / 0.089 | 21.669 / 36.0 | 34.917 | 2317 / 0 | 3.053 (4.676) |
| flythrough | indirect, one multi-draw per arena and layer | 3.019 | 913.143 | 16 (vanilla 28) | 5080388 | 16.667 / 1.028 | 83.951 / 112.0 | 135.661 | 6256 / 0 | 4.87 (12.382) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 274.667 | 16 (vanilla 28) | 779604 | 34.045 / 0.083 | 12.57 / 36.0 | 20.818 | 2979 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 471.247 | 16 (vanilla 28) | 960324 | 28.473 / 0.172 | 15.302 / 20.0 | 25.643 | 4019 / 0 | 2.339 (4.239) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.388 | 16 (vanilla 28) | 601156 | 25.814 / 0.15 | 9.627 / 16.0 | 16.053 | 4473 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 69.115 | 16 (vanilla 28) | 163500 | 24.383 / 0.141 | 2.607 / 20.0 | 4.366 | 4738 / 0 | 4.782 (4.679) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 74.48 | 16 (vanilla 28) | 216792 | 22.739 / 0.131 | 3.476 / 20.0 | 5.789 | 5084 / 0 | 4.094 (10.052) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.007 | 245.615 | 16 (vanilla 28) | 801096 | 19.649 / 0.12 | 12.924 / 28.0 | 21.392 | 5889 / 0 | 6.645 (16.776) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.025 | 281.414 | 16 (vanilla 28) | 771468 | 10.197 / 0.069 | 12.431 / 28.0 | 20.6 | 1319 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.026 | 260.286 | 16 (vanilla 28) | 779520 | 8.048 / 0.118 | 12.585 / 28.0 | 20.815 | 2655 / 0 | 6.734 (22.328) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.007 | 280.686 | 16 (vanilla 28) | 864936 | 13.127 / 0.035 | 13.801 / 28.0 | 23.096 | 1506 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 202.339 | 16 (vanilla 28) | 545312 | 10.172 / 0.055 | 8.663 / 28.0 | 14.561 | 846 / 0 | None (None) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 150.0 | 1.0 | 0.0 | 0 | 0 | 44 (0.021 / 0.032 / 0.034; 0) | 0 | 36000 / 0 / 0 |
| flythrough | 192.284 | 1.0 | 0.0 | 0 | 0 | 882 (0.023 / 0.043 / 6.063; 0) | 53 | 31150 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1648 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.014 / 0.034 / 0.035; 0) | 32 | 1202 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.099 / 0.126 / 1.664; 0) | 180 | 2919 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 597 | 0 (None / None / None; 0) | 0 | 796 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 579 | 0 (None / None / None; 0) | 0 | 1351 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1636 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2388 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 312.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.027 (0.091; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 430.698 | 1.0 (0.0, 0.0) | 1.142 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.121 (0.43; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.067 (0.181; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 50.364 | 10.0 (9.0, 2.363) | 6.0 (6.0, 2.276) | 0.058 (0.257; 0) | 0 | 2788 / 1509462 / 0 / 0 |
| translucent_move | 132.343 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 132.699 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 149.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.011; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 108.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, core old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 159.43 (base 844.2) | 13 (base 13) | 9276 (base 6372) | 257.33 (base 256.29) | 4.24 (base 3.73) |
| flythrough | 176.44 (reported, not gated) | 50 (reported, not gated) | 664740 (reported, not gated) | 316.55 (base 324.79) | 4.24 (base 5.99) |
| water_glass | 135.0 (base 457.8) | 13 (base 14) | 7092 (base 10264) | 244.15 (base 249.83) | 4.24 (base 5.99) |
| nether | 138.91 (base 640.77) | 10 (base 11) | 4736 (base 10656) | 264.87 (base 272.57) | 4.24 (base 5.99) |
| mobs | 140.9 (base 410.19) | 15 (base 16) | 610676 (base 1015892) | 263.73 (base 268.23) | 4.24 (base 5.99) |
| occlusion | 99.59 (base 106.62) | 13 (base 14) | 98148 (base 124172) | 265.51 (base 264.64) | 4.24 (base 4.24) |
| occlusion_move | 101.21 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 265.99 (base 265.66) | 4.24 (base 5.99) |
| translucent_move | 133.0 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 268.37 (base 268.19) | 4.24 (base 3.73) |
| water_glass_oit | 140.0 (base 154.0) | 13 (base 14) | 7764 (base 7604) | 244.38 (base 245.78) | 4.12 (base 3.73) |
| translucent_move_oit | 140.53 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 247.29 (base 248.15) | 4.24 (base 3.73) |
| flat_plants | 136.76 (base 137.98) | 13 (base 13) | 1492 (base 1716) | 246.59 (base 248.11) | 4.24 (base 5.99) |
| renderer_off | 110.93 (base 107.98) | 12 (base 12) | 1380 (base 1380) | 241.27 (base 240.73) | 4.12 (base 5.87) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 159.43 / 160 (10.54) | 902.0 / 902 (12.97) | median and average, x 1.10 + 2 |
| flythrough | 176.44 / 174 (6.1) | 1027.56 / 1027 (8.46) | median and average, at most vanilla |
| water_glass | 135.0 / 135 (17.79) | 382.87 / 391 (21.4) | median, at most vanilla |
| nether | 138.91 / 139 (16.17) | 582.54 / 589 (21.06) | median, x 1.10 + 2 |
| mobs | 140.9 / 144 (20.59) | 299.09 / 329 (26.93) | median, x 1.10 + 2 |
| occlusion | 99.59 / 138 (29.9) | 160.27 / 199 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 101.21 / 136 (29.79) | 165.62 / 200 (29.99) | median and average, x 1.10 + 2 |
| translucent_move | 133.0 / 133 (16.0) | 360.96 / 369 (21.59) | median, at most vanilla |
| water_glass_oit | 140.0 / 140 (9.15) | 402.0 / 402 (12.02) | median, at most vanilla |
| translucent_move_oit | 140.53 / 141 (8.27) | 386.59 / 386 (11.04) | median and average, at most vanilla |
| flat_plants | 136.76 / 137 (15.84) | 381.86 / 396 (22.71) | median, x 1.10 + 2 |
| renderer_off | 110.93 / 133 (24.72) | 283.12 / 321 (29.59) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.081 | 36.0 / 22.081 | 0.0 / 0.0 | 416.0 / 38.869 |
| flythrough | 112.0 / 83.409 | 112.0 / 83.409 | 0.0 / 0.0 | 416.0 / 146.73 |
| water_glass | 36.0 / 12.48 | 36.0 / 12.48 | 0.0 / 0.0 | 416.0 / 21.634 |
| nether | 20.0 / 15.298 | 20.0 / 15.298 | 0.0 / 0.0 | 256.0 / 26.828 |
| mobs | 16.0 / 9.627 | 16.0 / 9.627 | 0.0 / 0.0 | 256.0 / 16.813 |
| occlusion | 20.0 / 2.525 | 20.0 / 2.525 | 0.0 / 0.0 | 416.0 / 4.515 |
| occlusion_move | 20.0 / 3.418 | 20.0 / 3.418 | 0.0 / 0.0 | 416.0 / 5.959 |
| translucent_move | 28.0 / 13.659 | 28.0 / 13.659 | 0.0 / 0.0 | 416.0 / 22.674 |
| water_glass_oit | 28.0 / 12.431 | 28.0 / 12.431 | 0.0 / 0.0 | 416.0 / 21.634 |
| translucent_move_oit | 28.0 / 13.222 | 28.0 / 13.222 | 0.0 / 0.0 | 416.0 / 21.847 |
| flat_plants | 28.0 / 14.251 | 28.0 / 14.251 | 0.0 / 0.0 | 416.0 / 23.894 |
| renderer_off | 28.0 / 9.017 | 28.0 / 9.017 | 0.0 / 0.0 | 416.0 / 15.617 |


Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 38.181 | 752.567 | 16 (vanilla 28) | 1333040 | 37.028 / 0.101 | 22.081 / 36.0 | 35.596 | 2140 / 0 | 5.76 (9.342) |
| flythrough | plain, one multi-draw per region and layer | 56.803 | 894.943 | 16 (vanilla 28) | 5049356 | 16.405 / 0.989 | 83.409 / 112.0 | 134.832 | 6114 / 0 | 7.887 (20.462) |
| water_glass | plain, one multi-draw per region and layer | 14.039 | 274.772 | 16 (vanilla 28) | 774484 | 30.109 / 0.095 | 12.48 / 36.0 | 20.681 | 2794 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.062 | 471.455 | 16 (vanilla 28) | 959988 | 25.38 / 0.196 | 15.298 / 20.0 | 25.634 | 3798 / 0 | 3.125 (3.265) |
| mobs | plain, one multi-draw per region and layer | 11.027 | 197.479 | 16 (vanilla 28) | 601156 | 23.043 / 0.17 | 9.627 / 16.0 | 16.053 | 4234 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.119 | 16 (vanilla 28) | 157516 | 21.97 / 0.162 | 2.525 / 20.0 | 4.206 | 4461 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.99 | 76.426 | 16 (vanilla 28) | 213148 | 20.765 / 0.153 | 3.418 / 20.0 | 5.692 | 4735 / 0 | 8.726 (20.156) |
| translucent_move | plain, one multi-draw per region and layer | 12.038 | 248.688 | 16 (vanilla 28) | 843620 | 17.85 / 0.137 | 13.659 / 28.0 | 22.527 | 5488 / 0 | 6.833 (14.242) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.088 | 281.538 | 16 (vanilla 28) | 771468 | 12.946 / 0.069 | 12.431 / 28.0 | 20.6 | 1411 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.624 | 263.267 | 16 (vanilla 28) | 816432 | 9.028 / 0.095 | 13.222 / 28.0 | 21.801 | 2744 / 0 | 8.426 (19.74) |
| flat_plants | plain, one multi-draw per region and layer | 16.051 | 282.892 | 16 (vanilla 28) | 888948 | 18.996 / 0.047 | 14.251 / 28.0 | 23.737 | 1599 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.026 | 200.405 | 16 (vanilla 28) | 566364 | 8.111 / 0.063 | 9.017 / 28.0 | 15.124 | 916 / 0 | None (None) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 152.0 | 16.0 | 0.0 | 0 | 0 | 40 (0.157 / 0.037 / 5.222; 0) | 0 | 32072 / 0 / 0 |
| flythrough | 187.976 | 25.878 | 0.0 | 0 | 0 | 831 (0.033 / 0.055 / 5.047; 0) | 32 | 23121 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1424 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1198 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.021 / 0.054 / 0.054; 0) | 32 | 1194 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.128 / 0.186 / 2.542; 0) | 180 | 2247 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 549 | 0 (None / None / None; 0) | 0 | 732 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 498 | 0 (None / None / None; 0) | 0 | 1162 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1268 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1980 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 323.0 | 1.73 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.097; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 429.618 | 1.0 (0.0, 0.0) | 1.22 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.023; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.017 (0.069; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.149 (0.31; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.076 (0.194; 0) | 0 | 3594 / 1626884 / 0 / 0 |
| occlusion_move | 51.347 | 10.0 (9.0, 2.36) | 6.0 (6.0, 2.281) | 0.059 (0.344; 0) | 0 | 2771 / 1500100 / 0 / 0 |
| translucent_move | 134.324 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 134.699 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.01; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 109.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, core vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 123.0 (base 124.84) | 14 (base 15) | 20932 (base 27764) | 256.29 (base 257.2) | 4.23 (base 3.98) |
| flythrough | 123.07 (reported, not gated) | 45 (reported, not gated) | 796756 (reported, not gated) | 317.45 (base 318.06) | 3.98 (base 4.23) |
| water_glass | 124.0 (base 153.28) | 15 (base 15) | 15372 (base 15108) | 244.62 (base 244.39) | 4.23 (base 4.23) |
| nether | 120.72 (base 142.02) | 12 (base 12) | 18376 (base 19912) | 265.68 (base 268.95) | 4.23 (base 4.23) |
| mobs | 135.0 (base 208.6) | 17 (base 17) | 616328 (base 1008176) | 264.14 (base 264.31) | 4.23 (base 6.23) |
| occlusion | 92.27 (base 97.95) | 15 (base 15) | 100296 (base 126356) | 265.57 (base 264.78) | 4.23 (base 3.98) |
| occlusion_move | 93.18 (reported, not gated) | 15 (reported, not gated) | 82168 (reported, not gated) | 266.18 (base 265.16) | 4.23 (base 4.23) |
| translucent_move | 124.0 (reported, not gated) | 15 (reported, not gated) | 9620 (reported, not gated) | 268.65 (base 268.35) | 4.23 (base 5.46) |
| water_glass_oit | 129.0 (base 131.0) | 15 (base 15) | 15204 (base 15024) | 246.09 (base 245.99) | 4.1 (base 3.46) |
| translucent_move_oit | 129.0 (reported, not gated) | 15 (reported, not gated) | 9956 (reported, not gated) | 249.15 (base 248.38) | 4.23 (base 3.46) |
| flat_plants | 123.64 (base 124.0) | 15 (base 15) | 9364 (base 9864) | 248.09 (base 248.07) | 4.23 (base 5.98) |
| renderer_off | 103.87 (base 109.4) | 14 (base 14) | 7036 (base 7180) | 240.38 (base 241.13) | 3.85 (base 4.1) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 123.0 / 123 (9.68) | 124.71 / 125 (14.01) | median and average, x 1.10 + 2 |
| flythrough | 123.07 / 123 (5.87) | 123.07 / 123 (9.84) | median and average, at most vanilla + 2 |
| water_glass | 124.0 / 124 (15.68) | 102.07 / 124 (24.63) | median, at most vanilla |
| nether | 120.72 / 121 (15.84) | 99.56 / 121 (24.24) | median, x 1.10 + 2 |
| mobs | 135.0 / 135 (20.0) | 96.19 / 134 (29.46) | median, x 1.10 + 2 |
| occlusion | 92.27 / 131 (30.0) | 94.27 / 133 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 93.18 / 129 (29.72) | 94.33 / 133 (29.97) | median and average, x 1.10 + 2 |
| translucent_move | 124.0 / 124 (16.11) | 102.84 / 124 (24.4) | median, at most vanilla |
| water_glass_oit | 129.0 / 129 (8.76) | 129.0 / 129 (13.12) | median, at most vanilla |
| translucent_move_oit | 129.0 / 129 (8.17) | 129.0 / 129 (12.51) | median and average, at most vanilla |
| flat_plants | 123.64 / 124 (16.61) | 102.72 / 124 (24.33) | median, x 1.10 + 2 |
| renderer_off | 103.87 / 123 (23.94) | 84.4 / 123 (29.94) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.657 | 36.0 / 21.657 | 0.0 / 0.0 | 416.0 / 37.086 |
| flythrough | 112.0 / 83.235 | 112.0 / 83.235 | 0.0 / 0.0 | 416.0 / 148.134 |
| water_glass | 36.0 / 12.48 | 36.0 / 12.48 | 0.0 / 0.0 | 416.0 / 21.634 |
| nether | 20.0 / 15.299 | 20.0 / 15.299 | 0.0 / 0.0 | 256.0 / 26.838 |
| mobs | 16.0 / 9.627 | 16.0 / 9.627 | 0.0 / 0.0 | 256.0 / 16.813 |
| occlusion | 20.0 / 2.515 | 20.0 / 2.515 | 0.0 / 0.0 | 416.0 / 4.513 |
| occlusion_move | 20.0 / 3.414 | 20.0 / 3.414 | 0.0 / 0.0 | 416.0 / 5.909 |
| translucent_move | 28.0 / 13.309 | 28.0 / 13.309 | 0.0 / 0.0 | 416.0 / 22.542 |
| water_glass_oit | 28.0 / 12.431 | 28.0 / 12.431 | 0.0 / 0.0 | 416.0 / 21.634 |
| translucent_move_oit | 28.0 / 12.987 | 28.0 / 12.987 | 0.0 / 0.0 | 416.0 / 21.819 |
| flat_plants | 28.0 / 13.789 | 28.0 / 13.789 | 0.0 / 0.0 | 416.0 / 24.353 |
| renderer_off | 28.0 / 8.541 | 28.0 / 8.541 | 0.0 / 0.0 | 416.0 / 15.656 |


Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 731.772 | 16 (vanilla 28) | 1307484 | 42.814 / 0.087 | 21.657 / 36.0 | 34.914 | 2286 / 0 | 3.962 (6.046) |
| flythrough | indirect, one multi-draw per arena and layer | 3.026 | 890.128 | 16 (vanilla 28) | 5035864 | 16.333 / 1.036 | 83.235 / 112.0 | 134.472 | 5962 / 0 | 6.478 (16.911) |
| water_glass | indirect, one multi-draw per arena and layer | 3.01 | 274.875 | 16 (vanilla 28) | 774484 | 34.681 / 0.08 | 12.48 / 36.0 | 20.681 | 2935 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 471.487 | 16 (vanilla 28) | 960164 | 28.891 / 0.173 | 15.299 / 20.0 | 25.639 | 3976 / 0 | 3.087 (3.948) |
| mobs | indirect, one multi-draw per arena and layer | 2.005 | 197.494 | 16 (vanilla 28) | 601156 | 26.209 / 0.151 | 9.627 / 16.0 | 16.053 | 4412 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 69.115 | 16 (vanilla 28) | 157252 | 24.991 / 0.143 | 2.515 / 20.0 | 4.199 | 4645 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 74.408 | 16 (vanilla 28) | 213412 | 23.263 / 0.133 | 3.414 / 20.0 | 5.699 | 4995 / 0 | 5.542 (13.746) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.009 | 245.398 | 16 (vanilla 28) | 819356 | 20.096 / 0.12 | 13.309 / 28.0 | 21.879 | 5763 / 0 | 4.943 (13.532) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.029 | 281.6 | 16 (vanilla 28) | 771468 | 9.508 / 0.057 | 12.431 / 28.0 | 20.6 | 1304 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.031 | 260.546 | 16 (vanilla 28) | 799044 | 7.571 / 0.109 | 12.987 / 28.0 | 21.337 | 2610 / 0 | 6.155 (13.52) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.009 | 276.831 | 16 (vanilla 28) | 859688 | 16.213 / 0.037 | 13.789 / 28.0 | 22.956 | 1438 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.006 | 198.413 | 16 (vanilla 28) | 534020 | 10.764 / 0.048 | 8.541 / 28.0 | 14.26 | 949 / 0 | None (None) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 151.0 | 1.0 | 0.0 | 0 | 0 | 40 (0.036 / 0.044 / 0.505; 0) | 0 | 29294 / 0 / 0 |
| flythrough | 186.449 | 1.0 | 0.0 | 0 | 0 | 833 (0.026 / 0.048 / 6.04; 0) | 36 | 22001 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1256 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.008 / 0.018 / 0.019; 0) | 32 | 1192 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.11 / 0.159 / 1.236; 0) | 180 | 2261 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 528 | 0 (None / None / None; 0) | 0 | 704 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 492 | 0 (None / None / None; 0) | 0 | 1148 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1332 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1920 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 312.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.029 (0.104; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 428.754 | 1.0 (0.0, 0.0) | 1.178 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.013 (0.043; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.102 (0.195; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.058 (0.102; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 50.342 | 10.0 (9.0, 2.352) | 6.0 (6.0, 2.277) | 0.053 (0.294; 0) | 0 | 2759 / 1492632 / 0 / 0 |
| translucent_move | 133.012 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.0; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 133.774 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 147.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.009; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 106.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |

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

- full: 4 / 4
- old-gpu: 36 / 36
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| factory | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| factory_flat | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| sift | 0.0003 | 1.15 | 0.0072 | 0.0068 | 0.0067 |
| hub | 4.2209 | 8.75 | 1.167 | 1.4979 | 3.8729 |
| mam | 4.4679 | 9.3 | 1.1978 | 1.5461 | 4.0731 |
| map | 4.0968 | 11.35 | 5.3394 | 3.5534 | 4.7791 |
| hologram | 0.0258 | 0.55 | 0.0087 | 0.0071 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.57 | 500 | 0 (1) | 0 (1) | 0 (0) |
| factory_flat | 95.5 | 40 | 0 (0) | 0 (0) | 0 (0) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 95.31 | 12506 | 44 (45) | 4 (5) | 390 (391) |
| mam | 84.88 | 155106 | 45 (45) | 5 (5) | 391 (409) |
| map | 92.59 | not gated (SIFTEC's map shows whichever tiles it has scanned when it opens, so vanilla runs differ from each other by up to 22,532 strict pixels there (35-58k pixels in all after two attempts to wait for a steady map); package 7 (GUI) must give it real strict coverage) | 15672 (15816) | 2016 (2160) | 7976 (7984) |
| hologram | 99.97 | 413 | 56 (4307) | 56 (4316) | None (None) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| factory_flat | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| sift | 0.0003 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 3.9977 / 1135 | 3.046 / 0 | 3.1527 / 28 |
| mam | 4.2699 / 800 | 3.1503 / 0 | 3.1861 / 29 |
| map | 2.2487 / 1872 | 4.0161 / 13816 | 5.651 / 12932 |
| hologram | 0.0047 / 15 | 0.0036 / 12 | skipped |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 105.04 (base 111.52) | 14 (base 14) | 6376 (base 6488) | 422.05 (base 421.74) | 3.73 (base 5.48) |
| factory_flat | 104.08 (base 107.77) | 14 (base 14) | 6264 (base 6264) | 422.51 (base 422.37) | 3.48 (base 3.99) |
| sift | 153.0 (base 155.82) | 18 (base 19) | 16632 (base 81788) | 439.79 (base 440.07) | 3.73 (base 5.87) |
| hub | 115.02 (base 116.59) | 16 (base 18) | 66172 (base 67484) | 452.65 (base 451.33) | 3.73 (base 4.24) |
| mam | 116.32 (base 158.03) | 19 (base 21) | 23344 (base 24656) | 465.37 (base 471.8) | 3.73 (base 8.12) |
| map | 170.0 (base 177.0) | 14 (base 14) | 46844 (base 47032) | 505.33 (base 504.13) | 4.24 (base 8.24) |
| hologram | 173 median (avg 131.15) (base 165) | 22 (base 24) | 72192 (base 70368) | 469.65 (base 453.57) | 4.24 (base 8.24) |
| create_ct | 99.92 (base 100.31) | 12 (base 12) | 1596 (base 1596) | 471.21 (base 470.81) | 4.24 (base 4.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 105.04 / 151 (29.67) | 105.59 / 151 (29.64) | median and average, x 1.10 + 2 |
| factory_flat | 104.08 / 151 (29.93) | 104.31 / 151 (29.92) | median and average, x 1.10 + 2 |
| sift | 153.0 / 153 (18.23) | 140.36 / 153 (21.76) | median, x 1.10 + 2 |
| hub | 115.02 / 161 (29.93) | 114.88 / 161 (29.96) | median and average, x 1.10 + 2 |
| mam | 116.32 / 162 (29.82) | 115.78 / 162 (29.99) | median, x 1.10 + 2 |
| map | 170.0 / 170 (11.56) | 170.0 / 170 (14.92) | median and average, x 1.10 + 2 |
| hologram | 131.15 / 173 (28.32) | 127.76 / 173 (29.32) | median, at most vanilla |
| create_ct | 99.92 / 147 (29.99) | 100.0 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| factory_flat | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| sift | 16.0 / 7.374 | 16.0 / 7.374 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.22 | 32.0 / 20.22 | 0.0 / 0.0 | 416.0 / 34.183 |
| map | 72.0 / 33.729 | 72.0 / 33.729 | 0.0 / 0.0 | 416.0 / 56.376 |
| hologram | 72.0 / 0.238 | 72.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 72.0 / 0.171 | 72.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 73.123 | 16 (vanilla 28) | 91456 | 21.687 / 0.221 | 1.448 / 28.0 | 2.442 | 2417 / 0 | 2.721 (4.48) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 73.122 | 16 (vanilla 28) | 91456 | 26.68 / 0.078 | 1.448 / 28.0 | 2.442 | 1035 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.008 | 430.179 | 16 (vanilla 28) | 461644 | 20.7 / 0.254 | 7.374 / 16.0 | 12.327 | 3424 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.083 | 16 (vanilla 28) | 117492 | 20.252 / 0.248 | 1.879 / 12.0 | 3.137 | 3510 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.146 | 16 (vanilla 28) | 1187788 | 21.497 / 0.28 | 20.22 / 32.0 | 31.717 | 4175 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.013 | 550.372 | 16 (vanilla 28) | 2107848 | 19.809 / 0.907 | 33.729 / 72.0 | 56.286 | 8714 / 0 | 3.994 (6.843) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.023 | 16 (vanilla 28) | 13720 | 19.759 / 0.9 | 0.238 / 72.0 | 0.366 | 8798 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 19.631 / 0.894 | 0.171 / 72.0 | 0.283 | 8899 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2190 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| map | 37.0 | 1.0 | 0.0 | 0 | 0 | 1 (0.007 / 0.007 / 0.007; 0) | 0 | 8584 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2268 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 33.0 (0.0, 0.0) | 0.002 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 33.0 (0.0, 0.0) | 0.002 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.0 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 280.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 114.99 (base 119.43) | 13 (base 13) | 48500 (base 48604) | 421.41 (base 421.54) | 3.99 (base 5.48) |
| factory_flat | 113.03 (base 112.31) | 13 (base 13) | 48340 (base 48340) | 422.99 (base 422.36) | 3.99 (base 5.48) |
| sift | 171.0 (base 670.27) | 16 (base 18) | 4148 (base 81788) | 438.91 (base 441.24) | 3.99 (base 4.24) |
| hub | 120.73 (base 162.94) | 14 (base 17) | 64564 (base 66484) | 451.58 (base 451.46) | 3.99 (base 5.99) |
| mam | 122.77 (base 222.38) | 17 (base 20) | 20724 (base 22916) | 464.3 (base 472.87) | 5.99 (base 5.99) |
| map | 207.0 (base 225.0) | 12 (base 12) | 31404 (base 31404) | 504.26 (base 506.16) | 6.06 (base 6.31) |
| hologram | 174 median (avg 168.51) (base 166) | 20 (base 23) | 71544 (base 70624) | 469.19 (base 452.39) | 5.99 (base 6.31) |
| create_ct | 99.92 (base 99.92) | 10 (base 10) | 1124 (base 1124) | 470.19 (base 470.06) | 5.99 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 114.99 / 159 (28.96) | 178.43 / 224 (29.65) | median and average, x 1.10 + 2 |
| factory_flat | 113.03 / 159 (29.65) | 181.6 / 224 (28.57) | median and average, x 1.10 + 2 |
| sift | 171.0 / 171 (14.33) | 579.0 / 579 (12.37) | median, x 1.10 + 2 |
| hub | 120.73 / 165 (29.33) | 164.36 / 208 (29.16) | median and average, x 1.10 + 2 |
| mam | 122.77 / 164 (28.28) | 212.05 / 246 (26.39) | median, x 1.10 + 2 |
| map | 207.0 / 207 (9.4) | 716.0 / 716 (8.28) | median and average, x 1.10 + 2 |
| hologram | 168.51 / 174 (20.58) | 184.0 / 184 (16.41) | median, at most vanilla |
| create_ct | 99.92 / 147 (29.98) | 111.0 / 158 (30.01) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| factory_flat | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.125 | 32.0 / 20.125 | 0.0 / 0.0 | 416.0 / 33.762 |
| map | 72.0 / 34.524 | 72.0 / 34.524 | 0.0 / 0.0 | 416.0 / 60.835 |
| hologram | 72.0 / 0.238 | 72.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 72.0 / 0.171 | 72.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.014 | 73.126 | 16 (vanilla 28) | 91456 | 21.595 / 0.211 | 1.448 / 28.0 | 2.442 | 2482 / 0 | 3.404 (5.334) |
| factory_flat | plain, one multi-draw per region and layer | 8.014 | 73.123 | 16 (vanilla 28) | 91456 | 19.364 / 0.096 | 1.448 / 28.0 | 2.442 | 1017 / 0 | None (None) |
| sift | plain, one multi-draw per region and layer | 21.073 | 430.5 | 16 (vanilla 28) | 462608 | 20.637 / 0.245 | 7.389 / 16.0 | 12.353 | 3493 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.085 | 16 (vanilla 28) | 117492 | 20.207 / 0.239 | 1.879 / 12.0 | 3.137 | 3579 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.009 | 87.154 | 16 (vanilla 28) | 1181836 | 21.323 / 0.272 | 20.125 / 32.0 | 31.558 | 4228 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 40.213 | 554.936 | 16 (vanilla 28) | 2154400 | 21.57 / 1.108 | 34.524 / 72.0 | 57.529 | 8762 / 0 | 6.097 (11.999) |
| hologram | plain, one multi-draw per region and layer | 3.007 | 13.032 | 16 (vanilla 28) | 13720 | 21.498 / 1.101 | 0.238 / 72.0 | 0.366 | 8828 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 21.354 / 1.093 | 0.171 / 72.0 | 0.283 | 8932 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1722 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 588 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 567 / 0 / 0 |
| map | 37.0 | 16.0 | 0.0 | 0 | 0 | 3 (0.007 / 0.007 / 0.007; 0) | 0 | 6993 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1648 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 44.0 (0.0, 0.0) | 0.005 (0.122; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 44.0 (0.0, 0.0) | 0.004 (0.034; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.023; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 115.33 (base 111.25) | 15 (base 15) | 51056 (base 51148) | 421.34 (base 420.23) | 3.98 (base 3.71) |
| factory_flat | 113.3 (base 107.27) | 15 (base 15) | 50904 (base 50904) | 421.12 (base 421.17) | 5.46 (base 3.46) |
| sift | 153.0 (base 150.31) | 18 (base 19) | 16632 (base 81788) | 439.24 (base 439.9) | 3.98 (base 4.23) |
| hub | 120.24 (base 136.98) | 16 (base 18) | 66172 (base 67484) | 451.78 (base 450.49) | 3.98 (base 4.23) |
| mam | 142.54 (base 171.53) | 19 (base 21) | 23344 (base 24656) | 464.82 (base 462.56) | 5.98 (base 5.46) |
| map | 170.0 (base 177.0) | 14 (base 14) | 46956 (base 47072) | 503.94 (base 504.09) | 5.98 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| create_ct | 100.08 (base 100.16) | 12 (base 12) | 1596 (base 1596) | 475.68 (base 475.26) | 5.98 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 115.33 / 153 (27.32) | 108.32 / 153 (29.16) | median and average, x 1.10 + 2 |
| factory_flat | 113.3 / 153 (27.71) | 106.08 / 153 (29.89) | median and average, x 1.10 + 2 |
| sift | 153.0 / 153 (11.12) | 153.0 / 153 (13.54) | median, x 1.10 + 2 |
| hub | 120.24 / 161 (28.31) | 117.56 / 161 (28.92) | median and average, x 1.10 + 2 |
| mam | 142.54 / 162 (23.22) | 120.68 / 162 (28.41) | median, x 1.10 + 2 |
| map | 170.0 / 170 (7.3) | 170.0 / 170 (9.47) | median and average, x 1.10 + 2 |
| create_ct | 100.08 / 147 (29.94) | 99.92 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.449 | 28.0 / 1.449 | 0.0 / 0.0 | 256.0 / 2.598 |
| factory_flat | 28.0 / 1.448 | 28.0 / 1.448 | 0.0 / 0.0 | 256.0 / 2.598 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.263 | 32.0 / 20.263 | 0.0 / 0.0 | 416.0 / 34.048 |
| map | 72.0 / 33.226 | 72.0 / 33.226 | 0.0 / 0.0 | 416.0 / 60.229 |
| create_ct | 72.0 / 0.171 | 72.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.004 | 73.134 | 16 (vanilla 28) | 91456 | 20.272 / 0.208 | 1.449 / 28.0 | 2.442 | 2285 / 0 | 3.751 (6.333) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.004 | 73.132 | 16 (vanilla 28) | 91456 | 29.616 / 0.108 | 1.448 / 28.0 | 2.442 | 947 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.014 | 430.932 | 16 (vanilla 28) | 462608 | 19.456 / 0.245 | 7.389 / 16.0 | 12.353 | 3296 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.088 | 16 (vanilla 28) | 117492 | 19.008 / 0.238 | 1.879 / 12.0 | 3.137 | 3385 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.006 | 87.187 | 16 (vanilla 28) | 1189996 | 20.347 / 0.275 | 20.263 / 32.0 | 31.776 | 4042 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.021 | 555.807 | 16 (vanilla 28) | 2070588 | 21.196 / 1.046 | 33.226 / 72.0 | 55.291 | 8129 / 0 | 3.575 (6.955) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 21.059 / 1.039 | 0.171 / 72.0 | 0.283 | 8223 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1338 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 567 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 466 / 0 / 0 |
| map | 38.0 | 1.0 | 0.0 | 0 | 0 | 2 (0.006 / 0.007 / 0.007; 0) | 0 | 5548 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 44.0 (0.0, 0.0) | 0.004 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 44.0 (0.0, 0.0) | 0.004 (0.023; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

