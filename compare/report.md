# Quicksilver CI comparison

**passed**

## Suite core

- vanilla-a: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-b: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
- quicksilver-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- quicksilver-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- quicksilver-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- measure-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- measure-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- measure-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`

GPU error messages in the client logs (Quicksilver run / vanilla run of the same profile):

- full: 0 / 0
- old-gpu: 0 / 0
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| spawn | 0.4447 | 10.05 | 0.0592 | 0.0577 | 0.2904 |
| flythrough | 0.0 | 14.95 | 0.0014 | 0.0011 | 0.0012 |
| water_glass | 0.0048 | 6.45 | 0.0043 | 0.0047 | 0.0056 |
| nether | 0.0015 | 0.8 | 0.0018 | 0.0003 | 0.0167 |
| mobs | 0.0 | 0.4 | 0.0 | 0.0 | 0.0 |
| outline | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| outline.bare | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| occlusion_move | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2297 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.x2309 | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| occlusion_move.open | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.closed | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2300 | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2297 | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2305 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_x2309 | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| occlusion_move.bare_open | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| translucent_move | 0.0002 | 0.3 | 0.0001 | 0.0002 | 0.0001 |
| translucent_move.x2404 | 0.0002 | 0.3 | 0.0005 | 0.0003 | 0.0004 |
| translucent_move.x2414 | 0.0001 | 0.3 | 0.0002 | 0.0004 | 0.0002 |
| translucent_move.x2424 | 0.0 | 0.3 | 0.0001 | 0.0005 | 0.0001 |
| translucent_move.low | 0.0007 | 0.3 | 0.0014 | 0.0013 | 0.001 |
| translucent_move.back | 0.0002 | 0.3 | 0.0002 | 0.0 | 0.0001 |
| translucent_move.bare_back | 0.0002 | 0.3 | 0.0001 | 0.0001 | 0.0001 |
| water_glass_oit | 0.0026 | 0.3 | 0.0026 | 0.0056 | 0.0038 |
| translucent_move_oit | 0.0003 | 0.3 | 0.0001 | 0.0001 | 0.0003 |
| translucent_move_oit.x2404 | 0.0001 | 0.3 | 0.0004 | 0.0004 | 0.0003 |
| translucent_move_oit.x2414 | 0.0 | 0.3 | 0.0001 | 0.0002 | 0.0004 |
| translucent_move_oit.x2424 | 0.0 | 0.3 | 0.0001 | 0.0004 | 0.0002 |
| translucent_move_oit.low | 0.0004 | 0.3 | 0.0012 | 0.0015 | 0.0011 |
| translucent_move_oit.back | 0.0001 | 0.3 | 0.0 | 0.0 | 0.0 |
| translucent_move_oit.bare_back | 0.0001 | 0.3 | 0.0001 | 0.0 | 0.0 |
| flat_plants | 0.0 | 0.3 | 0.023 | 0.023 | 0.0226 |
| renderer_off | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| particles | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| particles.bare | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| animation | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| animation.behind | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| animation.front | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| animated_items | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| spawn | 92.63 | 18057 | 285 (802) | 254 (753) | 221 (728) |
| flythrough | 97.31 | 6020 | 51 (307) | 48 (304) | 52 (304) |
| water_glass | 95.8 | 25508 | 16 (2613) | 26 (2624) | 21 (2734) |
| nether | 99.42 | 905 | 3 (458) | 3 (458) | 154 (621) |
| mobs | 99.39 | 10 | 0 (0) | 0 (0) | 0 (0) |
| outline | 98.19 | 10 | 0 (0) | 0 (0) | 0 (0) |
| outline.bare | 98.2 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion | 98.62 | 22 | 1 (74) | 1 (74) | 1 (74) |
| occlusion_move | 99.15 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2297 | 98.94 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2305 | 98.96 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.x2309 | 99.13 | 32 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.open | 98.55 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.closed | 98.92 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2300 | 98.89 | 90 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2297 | 98.92 | 70 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2305 | 98.95 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_x2309 | 99.1 | 10 | 0 (0) | 0 (0) | 0 (0) |
| occlusion_move.bare_open | 98.5 | 110 | 0 (0) | 0 (0) | 0 (0) |
| translucent_move | 96.93 | 13 | 1 (76) | 2 (77) | 2 (68) |
| translucent_move.x2404 | 95.93 | 15 | 6 (106) | 6 (106) | 4 (88) |
| translucent_move.x2414 | 95.86 | 13 | 1 (104) | 2 (105) | 3 (101) |
| translucent_move.x2424 | 96.74 | 13 | 2 (79) | 3 (80) | 2 (68) |
| translucent_move.low | 96.11 | 41 | 15 (22) | 14 (20) | 12 (14) |
| translucent_move.back | 96.93 | 13 | 2 (76) | 3 (77) | 2 (67) |
| translucent_move.bare_back | 96.9 | 13 | 3 (78) | 1 (76) | 3 (69) |
| water_glass_oit | 95.81 | 57 | 14 (2616) | 26 (2626) | 14 (2729) |
| translucent_move_oit | 96.93 | 12 | 1 (76) | 3 (78) | 2 (68) |
| translucent_move_oit.x2404 | 95.93 | 16 | 5 (105) | 4 (105) | 4 (88) |
| translucent_move_oit.x2414 | 95.86 | 10 | 4 (108) | 2 (105) | 4 (101) |
| translucent_move_oit.x2424 | 96.74 | 15 | 2 (79) | 5 (82) | 1 (67) |
| translucent_move_oit.low | 96.11 | 40 | 14 (20) | 14 (19) | 14 (16) |
| translucent_move_oit.back | 96.93 | 12 | 2 (77) | 1 (76) | 1 (67) |
| translucent_move_oit.bare_back | 96.9 | 15 | 3 (78) | 1 (76) | 2 (68) |
| flat_plants | 98.22 | 350 | 225 (7416) | 225 (7416) | 217 (7347) |
| renderer_off | 99.2 | 10 | 0 (0) | 0 (0) | 0 (0) |
| particles | 99.99 | 4 | 0 (0) | 0 (0) | 0 (0) |
| particles.bare | 99.99 | 4 | 0 (0) | 0 (0) | 0 (0) |
| animation | 99.62 | 10 | 1 (3) | 1 (3) | 1 (3) |
| animation.behind | 99.74 | 10 | 0 (2) | 0 (2) | 0 (1) |
| animation.front | 99.62 | 10 | 1 (3) | 1 (3) | 1 (3) |
| animated_items | 98.9 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Quicksilver's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| spawn | 0.4408 / 256 | 0.4379 / 309 | 0.3076 / 311 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| water_glass | 0.0043 / 17 | 0.004 / 18 | 0.0066 / 28 |
| nether | 0.0015 / 0 | 0.0 / 0 | 0.0008 / 7 |
| mobs | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| outline | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| outline.bare | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2297 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2305 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.x2309 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.open | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.closed | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2300 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2297 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2305 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_x2309 | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| occlusion_move.bare_open | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| translucent_move | 0.0001 / 0 | 0.0002 / 0 | 0.0 / 0 |
| translucent_move.x2404 | 0.0001 / 2 | 0.0 / 1 | 0.0002 / 4 |
| translucent_move.x2414 | 0.0001 / 1 | 0.0002 / 0 | 0.0001 / 3 |
| translucent_move.x2424 | 0.0002 / 2 | 0.0004 / 3 | 0.0001 / 1 |
| translucent_move.low | 0.0007 / 5 | 0.0005 / 3 | 0.0003 / 4 |
| translucent_move.back | 0.0002 / 0 | 0.0 / 0 | 0.0003 / 2 |
| translucent_move.bare_back | 0.0001 / 1 | 0.0002 / 1 | 0.0002 / 2 |
| water_glass_oit | 0.0026 / 18 | 0.004 / 13 | 0.0036 / 13 |
| translucent_move_oit | 0.0001 / 1 | 0.0001 / 2 | 0.0004 / 3 |
| translucent_move_oit.x2404 | 0.0 / 0 | 0.0002 / 3 | 0.0001 / 0 |
| translucent_move_oit.x2414 | 0.0001 / 1 | 0.0001 / 1 | 0.0002 / 2 |
| translucent_move_oit.x2424 | 0.0 / 0 | 0.0004 / 3 | 0.0001 / 1 |
| translucent_move_oit.low | 0.0004 / 2 | 0.0011 / 7 | 0.0002 / 8 |
| translucent_move_oit.back | 0.0 / 0 | 0.0 / 2 | 0.0001 / 5 |
| translucent_move_oit.bare_back | 0.0001 / 1 | 0.0 / 0 | 0.0001 / 2 |
| flat_plants | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| renderer_off | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| particles | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| particles.bare | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| animation | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| animation.behind | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| animation.front | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| animated_items | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, core full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 13.0 (base 13.6) | 14 (base 14) | 25012 (base 22476) | 259.21 (base 259.07) | 4.24 (base 3.99) |
| flythrough | 13.05 (reported, not gated) | 40 (reported, not gated) | 858304 (reported, not gated) | 316.84 (base 324.25) | 3.73 (base 4.24) |
| water_glass | 9.0 (base 8.97) | 15 (base 15) | 15636 (base 15576) | 244.94 (base 250.02) | 4.24 (base 5.99) |
| nether | 15.88 (base 15.63) | 12 (base 12) | 20764 (base 19648) | 265.81 (base 273.53) | 4.24 (base 5.99) |
| mobs | 19.87 (base 19.77) | 17 (base 17) | 616268 (base 1010948) | 264.16 (base 268.37) | 4.24 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 263.96 (base 264.69) | 4.24 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100632 (base 126320) | 269.87 (base 265.1) | 4.24 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82524 (reported, not gated) | 270.05 (base 265.74) | 4.24 (base 4.24) |
| translucent_move | 8.98 (reported, not gated) | 15 (reported, not gated) | 11040 (reported, not gated) | 272.12 (base 267.48) | 4.24 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15972 (base 14688) | 244.41 (base 245.02) | 3.35 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10892 (reported, not gated) | 246.99 (base 247.19) | 3.48 (base 3.73) |
| flat_plants | 18.85 (base 18.29) | 15 (base 15) | 9980 (base 9812) | 248.23 (base 246.92) | 3.21 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7352 (base 7164) | 239.53 (base 241.33) | 3.6 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 267.11 (base 261.44) | 4.24 (base 5.99) |
| animation | 49 median (avg 39.95) (base 49) | 15 (base 15) | 2224 (base 2056) | 264.65 (base 260.68) | 4.24 (base 5.99) |
| animated_items | 60 median (avg 46.77) (base 60) | 24 (base 23) | 22136 (base 21940) | 239.87 (base 239.81) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (9.62) | 123.19 / 123 (8.75) | median and average, x 1.10 + 2 |
| flythrough | 13.05 / 13 (6.48) | 123.05 / 123 (5.37) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (18.04) | 124.0 / 124 (15.35) | median, at most vanilla |
| nether | 15.88 / 16 (18.24) | 120.93 / 121 (15.55) | median, x 1.10 + 2 |
| mobs | 19.87 / 20 (22.88) | 134.0 / 134 (19.33) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.01) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.46 / 133 (29.96) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.89) | 94.46 / 133 (29.95) | median and average, x 1.10 + 2 |
| translucent_move | 8.98 / 9 (19.12) | 124.0 / 124 (16.09) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (9.42) | 129.0 / 129 (8.85) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (9.38) | 129.0 / 129 (8.07) | median and average, at most vanilla |
| flat_plants | 18.85 / 19 (18.83) | 124.0 / 124 (15.79) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.68) | 101.31 / 123 (24.57) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.33 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 144.42 / 179 (29.99) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.0) | 155.22 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.705 | 36.0 / 22.705 | 0.0 / 0.0 | 416.0 / 39.488 |
| flythrough | 112.0 / 84.856 | 112.0 / 84.856 | 0.0 / 0.0 | 416.0 / 143.488 |
| water_glass | 36.0 / 12.448 | 36.0 / 12.448 | 0.0 / 0.0 | 416.0 / 21.538 |
| nether | 20.0 / 15.164 | 20.0 / 15.164 | 0.0 / 0.0 | 256.0 / 26.575 |
| mobs | 16.0 / 9.581 | 16.0 / 9.581 | 0.0 / 0.0 | 256.0 / 16.787 |
| outline | 16.0 / 1.46 | 16.0 / 1.46 | 0.0 / 0.0 | 256.0 / 2.533 |
| occlusion | 20.0 / 2.478 | 20.0 / 2.478 | 0.0 / 0.0 | 416.0 / 4.508 |
| occlusion_move | 20.0 / 3.32 | 20.0 / 3.32 | 0.0 / 0.0 | 416.0 / 5.83 |
| translucent_move | 28.0 / 12.921 | 28.0 / 12.921 | 0.0 / 0.0 | 416.0 / 22.364 |
| water_glass_oit | 20.0 / 12.448 | 20.0 / 12.448 | 0.0 / 0.0 | 416.0 / 21.538 |
| translucent_move_oit | 28.0 / 12.565 | 28.0 / 12.565 | 0.0 / 0.0 | 416.0 / 21.745 |
| flat_plants | 24.0 / 13.996 | 24.0 / 13.996 | 0.0 / 0.0 | 416.0 / 24.007 |
| renderer_off | 24.0 / 8.836 | 24.0 / 8.836 | 0.0 / 0.0 | 416.0 / 15.51 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 511060.885 (689600.552) | 25.89 | 1.16 | 587324 / 890684 | 562312 (2097152.0) | 192 / 175 | 0.0 / 0 |
| flythrough | 554907.163 (889127.938) | 37.59 | 1.084 | 2359296 / 7719384 | 2096648 (2097152.0) | 129 / 107 | 9.462 / 70 |
| water_glass | 216630.083 (387996.783) | 44.167 | 1.118 | 17764 / 17124 | 0 (2097152.0) | 360 / 307 | 0.0 / 0 |
| nether | 318400.275 (476862.418) | 33.23 | 1.274 | 258572 / 486588 | 238144 (2097152.0) | 364 / 310 | 0.0 / 0 |
| mobs | 166325.155 (300492.096) | 44.649 | 0.99 | 616268 / 616148 | 0 (2097152.0) | 8 / 305 | 0.0 / 0 |
| outline | 26864.626 (45701.917) | 41.218 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 44310.113 (77461.273) | 42.797 | 1.214 | 263192 / 663588 | 162560 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 49008.704 (85838.687) | 42.906 | 1.239 | 674256 / 1098992 | 597376 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 198942.168 (352781.277) | 43.608 | 1.184 | 906088 / 1574144 | 893760 (2097152.0) | 307 / 321 | 0.0 / 0 |
| water_glass_oit | 217179.096 (388980.096) | 44.167 | 1.118 | 17316 / 184340 | 0 (2097152.0) | 188 / 176 | 0.0 / 0 |
| translucent_move_oit | 199143.305 (352904.043) | 43.57 | 1.182 | 905372 / 1574424 | 893760 (2097152.0) | 187 / 161 | 0.0 / 0 |
| flat_plants | 239112.25 (438222.394) | 45.436 | 1.06 | 272012 / 10144 | 0 (2097152.0) | 372 / 315 | 0.0 / 0 |
| renderer_off | 153010.027 (278056.899) | 44.972 | 1.02 | 7352 / 7272 | 0 (2097152.0) | 1 / 8 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86832 / 150268 | 84608 (2097152.0) | 0 / 1 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79864 / 123000 | 57728 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, core full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 787.078 | 16 (vanilla 28) | 1372272 | 41.533 / 0.095 | 22.705 / 36.0 | 36.644 | 2322 / 0 | 4.528 (7.795) |
| flythrough | indirect, one multi-draw per arena and layer | 3.023 | 896.14 | 16 (vanilla 28) | 5137072 | 15.44 / 0.998 | 84.856 / 112.0 | 137.175 | 6408 / 0 | 7.463 (18.194) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 272.756 | 16 (vanilla 28) | 773844 | 33.5 / 0.088 | 12.448 / 36.0 | 20.664 | 2963 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 468.283 | 16 (vanilla 28) | 951168 | 27.908 / 0.17 | 15.164 / 20.0 | 25.399 | 4015 / 0 | 3.258 (4.301) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 196.429 | 16 (vanilla 28) | 599672 | 25.397 / 0.149 | 9.581 / 16.0 | 16.013 | 4454 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 91252 | 24.904 / 0.148 | 1.46 / 16.0 | 2.437 | 4550 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 154664 | 23.678 / 0.14 | 2.478 / 20.0 | 4.13 | 4811 / 0 | 3.145 (3.101) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.439 | 16 (vanilla 28) | 207020 | 22.379 / 0.132 | 3.32 / 20.0 | 5.528 | 5108 / 0 | 5.571 (12.233) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.008 | 247.115 | 16 (vanilla 28) | 797232 | 19.331 / 0.119 | 12.921 / 28.0 | 21.288 | 5904 / 0 | 7.752 (20.513) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.027 | 281.489 | 16 (vanilla 28) | 773844 | 7.537 / 0.043 | 12.448 / 20.0 | 20.664 | 1083 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.027 | 262.091 | 16 (vanilla 28) | 774672 | 6.657 / 0.101 | 12.565 / 28.0 | 20.686 | 2403 / 0 | 8.124 (18.818) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.008 | 281.747 | 16 (vanilla 28) | 874120 | 6.044 / 0.01 | 13.996 / 24.0 | 23.342 | 935 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.346 | 16 (vanilla 28) | 555176 | 2.401 / 0.021 | 8.836 / 24.0 | 14.825 | 600 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.277 / 0.119 | 0.131 / 28.0 | 0.225 | 5948 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.115 / 0.118 | 0.302 / 28.0 | 0.518 | 6050 / 0 | 1.851 (2.357) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.058 / 0.118 | 0.153 / 28.0 | 0.261 | 6100 / 0 | 1.054 (1.437) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 152.0 | 1.0 | 0.0 | 0 | 0 | 37 (0.031 / 0.048 / 0.067; 0) | 0 | 29336 / 0 / 0 |
| flythrough | 186.2 | 1.0 | 0.0 | 0 | 0 | 843 (0.02 / 0.052 / 3.069; 0) | 46 | 24206 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1444 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.014 / 0.05 / 0.069; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.115 / 0.155 / 2.606; 0) | 180 | 2681 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 567 | 0 (None / None / None; 0) | 0 | 756 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 564 | 0 (None / None / None; 0) | 0 | 1316 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1508 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2372 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 342.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.032 (0.092; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 434.3 | 1.0 (0.0, 0.0) | 1.269 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.019 (1.07; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.148 (0.318; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.069 (0.109; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 51.351 | 10.0 (9.0, 2.349) | 6.0 (6.0, 2.278) | 0.071 (0.268; 0) | 0 | 2767 / 1496544 / 0 / 0 |
| translucent_move | 135.047 | 1.0 (0.0, 0.0) | 0.765 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.585 | 1.0 (0.0, 0.0) | 0.745 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 151.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.0 (0.023; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.121; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.189 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 133.915 / 0.0 | 0.034 / 0.008 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.294 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.823 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.454 / 53.382 | 0.02 / 0.027 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.926 / 0.879 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 48.788 / 7.142 | 0.087 / 0.011 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 0.99 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.475 / 12.358 | 0.005 / 0.011 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.903 / 53.921 | 0.013 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.518 / 12.5 | 0.005 / 0.012 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 1.0 / 0.851 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.845 / 1.751 | 0.013 / 0.01 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 93.496 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.006 / 0.046 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.757 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.002 / 0.005 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.987 |
| animated_items | 136.553 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

GUI pass, core full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| spawn | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| flythrough | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| water_glass | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| nether | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| mobs | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| outline | 3.0 / 3.0 | 0.0 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 (pipelines 3.0, textures 2.0, scissors 1.0) | 3.0 / 3.0 (3.0 / 3.0) |
| occlusion | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| occlusion_move | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| translucent_move | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| water_glass_oit | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| translucent_move_oit | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| flat_plants | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| renderer_off | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| particles | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| animation | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| animated_items | 4.0 / 4.0 | 3.0 / 3.0 | 9.0 / 9.0 | 4.0 / 4.0 (pipelines 4.0, textures 3.0, scissors 1.0) | 4.0 / 4.0 (4.0 / 4.0) |

### Counts, core old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 50.1 (base 49.0) | 13 (base 13) | 7428 (base 6372) | 260.78 (base 256.29) | 4.24 (base 3.73) |
| flythrough | 65.11 (reported, not gated) | 51 (reported, not gated) | 1178852 (reported, not gated) | 312.98 (base 324.79) | 3.48 (base 5.99) |
| water_glass | 20.0 (base 20.0) | 13 (base 14) | 7652 (base 10264) | 244.8 (base 249.83) | 4.24 (base 5.99) |
| nether | 33.88 (base 33.79) | 10 (base 11) | 4288 (base 10656) | 265.83 (base 272.57) | 4.24 (base 5.99) |
| mobs | 29.0 (base 28.97) | 15 (base 16) | 610676 (base 1015892) | 264.28 (base 268.23) | 4.24 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 263.98 (base 264.41) | 4.24 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 269.56 (base 264.64) | 4.24 (base 4.24) |
| occlusion_move | 24.05 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 270.16 (base 265.66) | 4.24 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 272.22 (base 268.19) | 4.24 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7092 (base 7604) | 244.22 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 246.91 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 32.87 (base 33.0) | 13 (base 13) | 1492 (base 1716) | 247.09 (base 248.11) | 3.21 (base 5.99) |
| renderer_off | 17.79 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 240.82 (base 240.73) | 3.34 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 267.27 (base 261.4) | 4.24 (base 4.24) |
| animation | 49 median (avg 39.95) (base 49) | 13 (base 13) | 1716 (base 1716) | 264.93 (base 260.68) | 4.24 (base 4.24) |
| animated_items | 60 median (avg 46.77) (base 60) | 22 (base 22) | 21668 (base 21668) | 239.67 (base 239.53) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 50.1 / 50 (9.17) | 846.02 / 846 (10.51) | median and average, x 1.10 + 2 |
| flythrough | 65.11 / 64 (5.14) | 1001.85 / 1009 (5.65) | median and average, at most vanilla |
| water_glass | 20.0 / 20 (16.9) | 393.0 / 393 (17.05) | median, at most vanilla |
| nether | 33.88 / 34 (15.91) | 586.93 / 587 (16.85) | median, x 1.10 + 2 |
| mobs | 29.0 / 29 (19.17) | 317.22 / 328 (22.06) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.01) | 127.37 / 166 (30.01) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (29.96) | 161.27 / 200 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 24.05 / 24 (29.84) | 166.62 / 201 (29.98) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (15.75) | 367.48 / 367 (17.17) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (8.37) | 400.0 / 400 (8.79) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (7.78) | 384.49 / 386 (8.81) | median and average, at most vanilla |
| flat_plants | 32.87 / 33 (15.65) | 397.97 / 398 (17.96) | median, x 1.10 + 2 |
| renderer_off | 17.79 / 18 (25.38) | 299.4 / 325 (25.64) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.33 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 156.46 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.01) | 167.46 / 202 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 23.085 | 36.0 / 23.085 | 0.0 / 0.0 | 416.0 / 37.902 |
| flythrough | 112.0 / 83.911 | 112.0 / 83.911 | 0.0 / 0.0 | 416.0 / 143.283 |
| water_glass | 36.0 / 12.448 | 36.0 / 12.448 | 0.0 / 0.0 | 416.0 / 22.714 |
| nether | 20.0 / 15.161 | 20.0 / 15.161 | 0.0 / 0.0 | 256.0 / 26.579 |
| mobs | 16.0 / 9.581 | 16.0 / 9.581 | 0.0 / 0.0 | 256.0 / 16.787 |
| outline | 16.0 / 1.46 | 16.0 / 1.46 | 0.0 / 0.0 | 256.0 / 2.533 |
| occlusion | 20.0 / 2.478 | 20.0 / 2.478 | 0.0 / 0.0 | 416.0 / 4.508 |
| occlusion_move | 20.0 / 3.32 | 20.0 / 3.32 | 0.0 / 0.0 | 416.0 / 5.83 |
| translucent_move | 28.0 / 12.918 | 28.0 / 12.918 | 0.0 / 0.0 | 416.0 / 22.369 |
| water_glass_oit | 20.0 / 12.448 | 20.0 / 12.448 | 0.0 / 0.0 | 416.0 / 21.538 |
| translucent_move_oit | 28.0 / 12.506 | 28.0 / 12.506 | 0.0 / 0.0 | 416.0 / 21.75 |
| flat_plants | 24.0 / 13.998 | 24.0 / 13.998 | 0.0 / 0.0 | 416.0 / 24.049 |
| renderer_off | 24.0 / 8.836 | 24.0 / 8.836 | 0.0 / 0.0 | 416.0 / 15.502 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 455075.322 (698026.328) | 34.805 | 1.779 | 383404 / 809364 | 375976 (2097152.0) | 183 / 210 | 0.0 / 0 |
| flythrough | 539729.981 (865444.0) | 37.635 | 1.084 | 2108180 / 7116968 | 2095128 (2097152.0) | 103 / 113 | 11.76 / 78 |
| water_glass | 216672.361 (388069.959) | 44.167 | 1.118 | 223732 / 11460 | 215744 (2097152.0) | 338 / 340 | 0.0 / 0 |
| nether | 268488.396 (476996.025) | 43.713 | 1.706 | 141920 / 212244 | 137856 (2097152.0) | 318 / 337 | 0.0 / 0 |
| mobs | 166403.342 (300626.883) | 44.648 | 0.99 | 610676 / 612388 | 0 (2097152.0) | 317 / 11 | 0.0 / 0 |
| outline | 26864.7 (45702.043) | 41.218 | 1.0 | 5012 / 8308 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 44311.853 (77463.105) | 42.796 | 1.214 | 98148 / 124924 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 49027.966 (85874.946) | 42.908 | 1.239 | 672444 / 1097168 | 597376 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 198980.806 (352802.635) | 43.6 | 1.184 | 898676 / 1568852 | 893760 (2097152.0) | 315 / 343 | 0.0 / 0 |
| water_glass_oit | 217331.641 (389246.946) | 44.166 | 1.118 | 9220 / 11428 | 0 (2097152.0) | 167 / 175 | 0.0 / 0 |
| translucent_move_oit | 199091.768 (352664.142) | 43.546 | 1.181 | 899236 / 1569140 | 893760 (2097152.0) | 155 / 176 | 0.0 / 0 |
| flat_plants | 239350.262 (438685.086) | 45.439 | 1.06 | 1828 / 4212 | 0 (2097152.0) | 313 / 359 | 0.0 / 0 |
| renderer_off | 153053.286 (278137.515) | 44.972 | 1.02 | 1380 / 3172 | 0 (2097152.0) | 10 / 1 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 8772 / 8964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86232 / 149988 | 84608 (2097151.138) | 0 / 1 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79304 / 122900 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.213 | 801.973 | 16 (vanilla 28) | 1394916 | 40.105 / 0.101 | 23.085 / 36.0 | 37.248 | 2363 / 0 | 6.587 (11.458) |
| flythrough | plain, one multi-draw per region and layer | 55.524 | 865.709 | 16 (vanilla 28) | 5083904 | 15.702 / 0.985 | 83.911 / 112.0 | 135.755 | 6155 / 0 | 9.891 (24.207) |
| water_glass | plain, one multi-draw per region and layer | 14.041 | 272.805 | 16 (vanilla 28) | 773860 | 32.849 / 0.088 | 12.448 / 36.0 | 20.664 | 3029 / 0 | 7.957 (6.518) |
| nether | plain, one multi-draw per region and layer | 20.063 | 469.06 | 16 (vanilla 28) | 951056 | 27.638 / 0.178 | 15.161 / 20.0 | 25.396 | 4060 / 0 | 3.312 (7.035) |
| mobs | plain, one multi-draw per region and layer | 11.029 | 196.512 | 16 (vanilla 28) | 599688 | 25.125 / 0.155 | 9.581 / 16.0 | 16.013 | 4494 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 91252 | 24.631 / 0.154 | 1.46 / 16.0 | 2.437 | 4591 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 70.117 | 16 (vanilla 28) | 154668 | 23.509 / 0.147 | 2.478 / 20.0 | 4.13 | 4837 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 11.002 | 75.46 | 16 (vanilla 28) | 207028 | 22.038 / 0.137 | 3.32 / 20.0 | 5.528 | 5170 / 0 | 5.202 (11.348) |
| translucent_move | plain, one multi-draw per region and layer | 12.038 | 247.203 | 16 (vanilla 28) | 797040 | 19.114 / 0.124 | 12.918 / 28.0 | 21.283 | 5952 / 0 | 10.12 (20.739) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.096 | 281.677 | 16 (vanilla 28) | 773860 | 7.434 / 0.039 | 12.448 / 20.0 | 20.664 | 1079 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.632 | 262.252 | 16 (vanilla 28) | 771160 | 6.587 / 0.099 | 12.506 / 28.0 | 20.592 | 2396 / 0 | 9.981 (27.702) |
| flat_plants | plain, one multi-draw per region and layer | 17.054 | 281.898 | 16 (vanilla 28) | 874576 | 5.931 / 0.01 | 13.998 / 24.0 | 23.354 | 934 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.026 | 205.404 | 16 (vanilla 28) | 555180 | 2.354 / 0.02 | 8.836 / 24.0 | 14.825 | 607 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 19.068 / 0.124 | 0.131 / 28.0 | 0.225 | 5994 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 18.907 / 0.123 | 0.302 / 28.0 | 0.518 | 6096 / 0 | 2.512 (5.382) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.851 / 0.123 | 0.153 / 28.0 | 0.261 | 6145 / 0 | 1.245 (1.826) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 154.641 | 17.0 | 0.0 | 0 | 0 | 49 (0.035 / 0.058 / 0.069; 0) | 0 | 28454 / 0 / 0 |
| flythrough | 178.798 | 24.74 | 0.0 | 0 | 0 | 795 (0.066 / 0.059 / 9.061; 0) | 37 | 18595 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1356 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.011 / 0.017 / 0.032; 0) | 32 | 1194 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.133 / 0.218 / 0.231; 0) | 180 | 2212 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 504 | 0 (None / None / None; 0) | 0 | 672 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 468 | 0 (None / None / None; 0) | 0 | 1092 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1256 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2032 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 349.196 | 2.0 (1.0, 0.0) | 11.0 (1.0, 0.0) | 0.047 (0.176; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 426.712 | 1.0 (0.0, 0.0) | 1.308 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.018 (0.06; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.141 (0.304; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.069 (0.114; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.353 | 10.0 (9.0, 2.362) | 6.0 (6.0, 2.281) | 0.066 (0.264; 0) | 0 | 2772 / 1500174 / 0 / 0 |
| translucent_move | 135.028 | 1.0 (0.0, 0.0) | 0.759 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.724 | 1.0 (0.0, 0.0) | 0.731 (0.0, 0.0) | 0.001 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 151.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.011 (0.044; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.019 / 0.098 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 86.204 / 0.098 | 0.027 / 0.013 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.214 / 0.0 | 0.004 / 0.019 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.788 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 56.165 / 56.065 | 0.014 / 0.041 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.926 / 0.884 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 43.197 / 7.298 | 0.112 / 0.013 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 104.308 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.003 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.242 / 12.19 | 0.004 / 0.014 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 51.348 / 51.494 | 0.012 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.038 / 13.071 | 0.006 / 0.011 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.967 / 0.869 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.571 / 1.465 | 0.013 / 0.011 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 89.619 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.009 / 0.054 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.006 / 0.011 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.988 |
| animated_items | 136.793 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

GUI pass, core old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| spawn | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| flythrough | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| water_glass | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| nether | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| mobs | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| outline | 3.0 / 3.0 | 0.0 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 (pipelines 3.0, textures 2.0, scissors 1.0) | 3.0 / 3.0 (3.0 / 3.0) |
| occlusion | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| occlusion_move | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| translucent_move | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| water_glass_oit | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| translucent_move_oit | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| flat_plants | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| renderer_off | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| particles | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| animation | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| animated_items | 4.0 / 4.0 | 3.0 / 3.0 | 9.0 / 9.0 | 4.0 / 4.0 (pipelines 4.0, textures 3.0, scissors 1.0) | 4.0 / 4.0 (4.0 / 4.0) |

### Counts, core vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 13.36 (base 14.05) | 14 (base 15) | 25012 (base 27764) | 260.28 (base 257.2) | 4.23 (base 3.98) |
| flythrough | 13.05 (reported, not gated) | 31 (reported, not gated) | 324544 (reported, not gated) | 317.23 (base 318.06) | 3.71 (base 4.23) |
| water_glass | 8.88 (base 9.0) | 15 (base 15) | 15860 (base 15108) | 245.45 (base 244.39) | 4.23 (base 4.23) |
| nether | 14.85 (base 15.71) | 12 (base 12) | 20876 (base 19912) | 266.34 (base 268.95) | 4.23 (base 4.23) |
| mobs | 19.68 (base 19.97) | 17 (base 17) | 616268 (base 1008176) | 264.63 (base 264.31) | 4.23 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.53 (base 264.58) | 4.23 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100612 (base 126356) | 269.79 (base 264.78) | 4.23 (base 3.98) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82504 (reported, not gated) | 270.27 (base 265.16) | 4.23 (base 4.23) |
| translucent_move | 8.87 (reported, not gated) | 15 (reported, not gated) | 10836 (reported, not gated) | 272.67 (base 268.35) | 4.23 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15636 (base 15024) | 245.01 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10784 (reported, not gated) | 247.45 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 16.87 (base 18.96) | 15 (base 15) | 9944 (base 9864) | 247.92 (base 248.07) | 3.2 (base 5.98) |
| renderer_off | 7.67 (base 7.83) | 14 (base 14) | 7352 (base 7180) | 239.72 (base 241.13) | 3.59 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 267.57 (base 261.4) | 4.23 (base 5.98) |
| animation | 49 median (avg 39.93) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.1 (base 260.68) | 4.23 (base 5.98) |
| animated_items | 60 median (avg 46.73) (base 60) | 24 (base 23) | 22136 (base 21940) | 240.06 (base 239.74) | 4.23 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.36 / 13 (12.9) | 124.69 / 125 (10.65) | median and average, x 1.10 + 2 |
| flythrough | 13.05 / 13 (8.37) | 123.06 / 123 (6.14) | median and average, at most vanilla + 2 |
| water_glass | 8.88 / 9 (22.58) | 124.0 / 124 (15.59) | median, at most vanilla |
| nether | 14.85 / 16 (21.93) | 120.67 / 121 (16.38) | median, x 1.10 + 2 |
| mobs | 19.68 / 20 (29.2) | 127.69 / 134 (21.16) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.33 / 133 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.99) | 94.46 / 133 (29.97) | median and average, x 1.10 + 2 |
| translucent_move | 8.87 / 9 (23.05) | 124.0 / 124 (17.36) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (11.49) | 129.0 / 129 (8.58) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (10.38) | 129.0 / 129 (8.57) | median and average, at most vanilla |
| flat_plants | 16.87 / 19 (23.92) | 123.95 / 124 (18.7) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.98) | 104.44 / 123 (23.8) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.32 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.93 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 155.46 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 22.711 | 40.0 / 22.711 | 0.0 / 0.0 | 416.0 / 36.894 |
| flythrough | 112.0 / 86.471 | 112.0 / 86.471 | 0.0 / 0.0 | 416.0 / 144.32 |
| water_glass | 40.0 / 12.448 | 40.0 / 12.448 | 0.0 / 0.0 | 416.0 / 21.572 |
| nether | 20.0 / 15.167 | 20.0 / 15.167 | 0.0 / 0.0 | 256.0 / 26.774 |
| mobs | 16.0 / 9.581 | 16.0 / 9.581 | 0.0 / 0.0 | 256.0 / 16.787 |
| outline | 16.0 / 1.46 | 16.0 / 1.46 | 0.0 / 0.0 | 256.0 / 2.533 |
| occlusion | 20.0 / 2.611 | 20.0 / 2.611 | 0.0 / 0.0 | 416.0 / 4.508 |
| occlusion_move | 20.0 / 3.366 | 20.0 / 3.366 | 0.0 / 0.0 | 416.0 / 5.831 |
| translucent_move | 28.0 / 13.017 | 28.0 / 13.017 | 0.0 / 0.0 | 416.0 / 22.474 |
| water_glass_oit | 20.0 / 12.448 | 20.0 / 12.448 | 0.0 / 0.0 | 416.0 / 21.538 |
| translucent_move_oit | 28.0 / 12.606 | 28.0 / 12.606 | 0.0 / 0.0 | 416.0 / 21.847 |
| flat_plants | 24.0 / 13.865 | 24.0 / 13.865 | 0.0 / 0.0 | 416.0 / 24.19 |
| renderer_off | 24.0 / 8.836 | 24.0 / 8.836 | 0.0 / 0.0 | 416.0 / 15.51 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 510348.903 (688674.086) | 25.894 | 1.16 | 437492 / 1363432 | 412480 (2097152.0) | 257 / 213 | 0.0 / 0 |
| flythrough | 559050.97 (895872.671) | 37.597 | 1.083 | 2124864 / 7771068 | 2096680 (2097152.0) | 167 / 122 | 6.25 / 71 |
| water_glass | 216509.002 (387779.92) | 44.167 | 1.118 | 18772 / 17516 | 0 (2097152.0) | 12 / 311 | 0.0 / 0 |
| nether | 318253.982 (476642.776) | 33.23 | 1.274 | 233820 / 737344 | 212160 (2097152.0) | 18 / 327 | 0.0 / 0 |
| mobs | 166245.696 (300348.54) | 44.649 | 0.99 | 616268 / 616308 | 0 (2097152.0) | 0 / 14 | 0.0 / 0 |
| outline | 26864.775 (45702.17) | 41.218 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 46573.493 (81921.23) | 43.148 | 1.2 | 271592 / 425608 | 171072 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 50938.114 (89623.559) | 43.164 | 1.226 | 652768 / 1098636 | 576000 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 201213.289 (356956.803) | 43.631 | 1.187 | 905472 / 1574964 | 893760 (2097152.0) | 6 / 347 | 0.0 / 0 |
| water_glass_oit | 216973.362 (388611.616) | 44.167 | 1.118 | 17204 / 17348 | 0 (2097152.0) | 229 / 171 | 0.0 / 0 |
| translucent_move_oit | 201513.401 (357243.295) | 43.592 | 1.185 | 903972 / 1574684 | 893760 (2097152.0) | 207 / 171 | 0.0 / 0 |
| flat_plants | 237185.167 (434402.895) | 45.4 | 1.061 | 10616 / 9752 | 0 (2097152.0) | 19 / 374 | 0.0 / 0 |
| renderer_off | 153009.015 (278057.429) | 44.972 | 1.02 | 7352 / 7272 | 0 (2097152.0) | 0 / 3 | 0.0 / 0 |
| particles | 1927.207 (4215.013) | 54.278 | 1.0 | 9204 / 9876 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86832 / 150176 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79864 / 123068 | 57728 (2097152.0) | 0 / 2 | 0.0 / 0 |

Terrain module, core vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.012 | 786.047 | 16 (vanilla 28) | 1372276 | 40.031 / 0.118 | 22.711 / 40.0 | 36.644 | 2486 / 0 | 2.518 (3.569) |
| flythrough | indirect, one multi-draw per arena and layer | 3.018 | 913.94 | 16 (vanilla 28) | 5234652 | 15.078 / 0.963 | 86.471 / 112.0 | 139.78 | 6520 / 0 | 5.512 (14.294) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 272.603 | 16 (vanilla 28) | 773844 | 33.167 / 0.104 | 12.448 / 40.0 | 20.664 | 3147 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 468.066 | 16 (vanilla 28) | 951200 | 27.872 / 0.185 | 15.167 / 20.0 | 25.4 | 4202 / 0 | 2.144 (2.432) |
| mobs | indirect, one multi-draw per arena and layer | 2.003 | 196.335 | 16 (vanilla 28) | 599672 | 25.423 / 0.163 | 9.581 / 16.0 | 16.013 | 4637 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 91252 | 24.938 / 0.161 | 1.46 / 16.0 | 2.437 | 4734 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 163572 | 23.786 / 0.153 | 2.611 / 20.0 | 4.368 | 4978 / 0 | 5.461 (4.697) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.454 | 16 (vanilla 28) | 211760 | 22.636 / 0.146 | 3.366 / 20.0 | 5.655 | 5247 / 0 | 3.866 (9.36) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.007 | 248.165 | 16 (vanilla 28) | 805352 | 19.619 / 0.132 | 13.017 / 28.0 | 21.505 | 6052 / 0 | 3.921 (8.378) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.022 | 281.223 | 16 (vanilla 28) | 773844 | 7.537 / 0.043 | 12.448 / 20.0 | 20.664 | 1083 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.024 | 263.227 | 16 (vanilla 28) | 779496 | 6.811 / 0.114 | 12.606 / 28.0 | 20.815 | 2450 / 0 | 4.74 (9.838) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.006 | 280.586 | 16 (vanilla 28) | 866992 | 6.396 / 0.01 | 13.865 / 24.0 | 23.151 | 943 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.342 | 16 (vanilla 28) | 555188 | 2.418 / 0.021 | 8.836 / 24.0 | 14.825 | 595 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.578 / 0.132 | 0.131 / 28.0 | 0.225 | 6091 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.416 / 0.131 | 0.302 / 28.0 | 0.518 | 6193 / 0 | 1.263 (1.631) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.359 / 0.131 | 0.153 / 28.0 | 0.261 | 6243 / 0 | 0.972 (1.386) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 152.0 | 1.0 | 0.0 | 0 | 0 | 40 (0.018 / 0.027 / 0.033; 0) | 0 | 39216 / 0 / 0 |
| flythrough | 191.149 | 1.0 | 0.0 | 0 | 0 | 859 (0.017 / 0.038 / 3.055; 0) | 51 | 32113 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1808 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.01 / 0.015 / 0.02; 0) | 32 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 182 (0.197 / 0.127 / 7.37; 0) | 182 | 3234 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 690 | 0 (None / None / None; 0) | 0 | 920 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 624 | 0 (None / None / None; 0) | 0 | 1456 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1916 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 342.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.026 (0.072; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 437.333 | 1.0 (0.0, 0.0) | 1.19 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.047; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.112 (0.236; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.065 (0.13; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 51.36 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.28) | 0.057 (0.284; 0) | 0 | 2782 / 1505432 / 0 / 0 |
| translucent_move | 136.1 | 1.0 (0.0, 0.0) | 0.773 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.659 | 1.0 (0.0, 0.0) | 0.75 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 150.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.695 / 0.362 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 134.393 / 0.36 | 0.024 / 0.01 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.476 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.839 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.815 / 55.746 | 0.017 / 0.021 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 9.087 | 0.673 / 0.854 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 37.093 / 6.38 | 0.062 / 0.007 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 108.747 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.439 / 12.385 | 0.005 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.348 / 53.4 | 0.011 / 0.019 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.865 / 11.909 | 0.005 / 0.008 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 8.368 | 0.949 / 0.661 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.606 / 2.43 | 0.013 / 0.037 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 96.6 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.048 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.262 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.798 / 2.795 | 0.004 / 0.01 | 56 (3) | 40.58 / 9.238 / 31.343 | 3 / 0 | 10.982 |
| animated_items | 136.793 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

GUI pass, core vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| spawn | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| flythrough | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| water_glass | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| nether | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| mobs | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| outline | 3.0 / 3.0 | 0.0 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 (pipelines 3.0, textures 2.0, scissors 1.0) | 3.0 / 3.0 (3.0 / 3.0) |
| occlusion | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| occlusion_move | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| translucent_move | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| water_glass_oit | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| translucent_move_oit | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| flat_plants | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| renderer_off | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| particles | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| animation | 1.0 / 1.0 | 0.0 / 0.0 | 1.0 / 1.0 | 1.0 / 1.0 (pipelines 1.0, textures 1.0, scissors 1.0) | 1.0 / 1.0 (1.0 / 1.0) |
| animated_items | 4.0 / 4.0 | 3.0 / 3.0 | 9.0 / 9.0 | 4.0 / 4.0 (pipelines 4.0, textures 3.0, scissors 1.0) | 4.0 / 4.0 (4.0 / 4.0) |

## Suite siftec

- vanilla-a: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-b: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
- vanilla-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
- quicksilver-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- quicksilver-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- quicksilver-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- measure-full: backend `OpenGL` drivers `4.5 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
- measure-old: backend `OpenGL` drivers `3.3 (Core Profile) Mesa 25.2.8-0ubuntu0.24.04.4`
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- measure-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`

GPU error messages in the client logs (Quicksilver run / vanilla run of the same profile):

- full: 4 / 4
- old-gpu: 36 / 36
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| factory | 0.0021 | 0.3 | 0.0169 | 0.0169 | 0.0168 |
| factory_flat | 0.0044 | 0.3 | 0.0039 | 0.0038 | 0.0 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| hologram | 0.0224 | 0.55 | 0.0255 | 0.026 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | 0.0 | 0.25 | 0.0005 | 0.0005 | 0.0005 |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.56 | 500 | 0 (14) | 0 (14) | 0 (14) |
| factory_flat | 95.25 | 40 | 1 (15) | 0 (14) | 0 (14) |
| sift | 93.93 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 87.03 | 10 | 0 (0) | 0 (0) | 0 (37) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| map | 99.99 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 99.97 | 413 | 44 (4294) | 47 (4297) | None (None) |
| hud | 44.33 | 10 | 5 (5) | 5 (5) | 5 (5) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Quicksilver's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0169 / 0 | 0.0002 / 0 | 0.0014 / 0 |
| factory_flat | 0.0011 / 1 | 0.0041 / 0 | 0.0009 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| map | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.003 / 19 | 0.0225 / 10 | skipped |
| hud | 0.0005 / 5 | 0.0005 / 5 | 0.0005 / 5 |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.25 (base 12.23) | 15 (base 14) | 7068 (base 6488) | 422.83 (base 421.74) | 5.99 (base 5.48) |
| factory_flat | 20.29 (base 20.25) | 15 (base 14) | 10052 (base 6264) | 422.34 (base 422.37) | 5.48 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 440.74 (base 440.07) | 5.99 (base 5.87) |
| hub | 21.55 (base 21.58) | 16 (base 18) | 66576 (base 67484) | 452.94 (base 451.33) | 5.99 (base 4.24) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 465.84 (base 471.8) | 5.99 (base 8.12) |
| ponder | 87.25 (base 87.25) | 14 (base 14) | 52268 (base 52248) | 464.81 (base 463.81) | 5.99 (base 8.24) |
| map | 30.0 (base 30.0) | 14 (base 14) | 48980 (base 47032) | 492.29 (base 504.13) | 5.87 (base 8.24) |
| hologram | 33 median (avg 32.82) (base 33) | 22 (base 24) | 71840 (base 70368) | 471.37 (base 453.57) | 5.87 (base 8.24) |
| hud | 31.0 (base 31.0) | 16 (base 16) | 74540 (base 71636) | 492.1 (base 494.92) | 5.93 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 480.44 (base 470.81) | 5.93 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.25 / 12 (28.79) | 105.94 / 152 (29.79) | median and average, x 1.10 + 2 |
| factory_flat | 20.29 / 20 (29.72) | 113.6 / 160 (29.98) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (16.0) | 128.19 / 153 (24.19) | median, x 1.10 + 2 |
| hub | 21.55 / 21 (29.67) | 114.86 / 161 (30.01) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.92) | 115.94 / 162 (29.95) | median, x 1.10 + 2 |
| ponder | 87.25 / 87 (29.6) | 180.4 / 227 (30.03) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (11.22) | 170.0 / 170 (16.28) | median and average, x 1.10 + 2 |
| hologram | 32.82 / 33 (24.42) | 126.95 / 173 (29.72) | median, at most vanilla |
| hud | 31.0 / 31 (17.54) | 160.54 / 171 (20.91) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (29.97) | 99.92 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.399 | 16.0 / 7.399 | 0.0 / 0.0 | 416.0 / 12.921 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.276 | 32.0 / 20.276 | 0.0 / 0.0 | 416.0 / 34.167 |
| ponder | 32.0 / 17.534 | 32.0 / 17.534 | 0.0 / 0.0 | 416.0 / 29.254 |
| map | 40.0 / 27.83 | 40.0 / 27.83 | 0.0 / 0.0 | 416.0 / 47.422 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.383 | 40.0 / 17.383 | 0.0 / 0.0 | 416.0 / 30.506 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26338.511 (48131.13) | 45.278 | 1.0 | 114684 / 190188 | 100928 (2097152.0) | 15 / 6 | 0.0 / 0 |
| factory_flat | 26336.872 (48128.104) | 45.278 | 1.0 | 117668 / 193172 | 100928 (2097152.0) | 4 / 3 | 0.0 / 0 |
| sift | 147766.331 (205157.119) | 27.974 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 320 / 18 | 0.0 / 0 |
| hub | 33472.256 (58844.899) | 43.118 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 8 / 0 | 0.0 / 0 |
| mam | 66779.299 (117167.279) | 43.005 | 1.011 | 304760 / 304740 | 0 (2097152.0) | 0 / 2 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61244 / 61224 | 0 (2097152.0) | 6 / 1 | 0.0 / 0 |
| map | 395475.67 (621853.795) | 36.404 | 1.228 | 48980 / 46384 | 0 (2097152.0) | 224 / 325 | 0.0 / 0 |
| hologram | 3912.0 (6874.057) | 43.09 | 1.0 | 74324 / 75224 | 0 (2097152.0) | 22 / 3 | 0.0 / 0 |
| hud | 291763.234 (455768.486) | 35.984 | 1.318 | 74540 / 71980 | 0 (2097152.0) | 350 / 54 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 1 / 1 | 0.0 / 0 |

Terrain module, siftec full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.129 | 16 (vanilla 28) | 96100 | 23.969 / 0.238 | 1.511 / 28.0 | 2.566 | 1919 / 0 | 5.056 (9.622) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.125 | 16 (vanilla 28) | 96100 | 3.043 / 0.001 | 1.511 / 12.0 | 2.566 | 298 / 0 | 3.33 (5.119) |
| sift | indirect, one multi-draw per arena and layer | 3.009 | 430.341 | 16 (vanilla 28) | 462896 | 22.224 / 0.278 | 7.399 / 16.0 | 12.361 | 2928 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 21.719 / 0.271 | 1.879 / 12.0 | 3.137 | 2995 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1191072 | 22.645 / 0.308 | 20.276 / 32.0 | 31.805 | 3642 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1017604 | 22.645 / 0.308 | 17.534 / 32.0 | 27.173 | 3643 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.013 | 554.464 | 16 (vanilla 28) | 1744280 | 19.739 / 0.245 | 27.83 / 40.0 | 46.577 | 4488 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.027 | 16 (vanilla 28) | 13744 | 19.7 / 0.243 | 0.238 / 40.0 | 0.367 | 4538 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.009 | 403.149 | 16 (vanilla 28) | 1090120 | 16.278 / 0.204 | 17.383 / 40.0 | 29.109 | 6301 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.144 / 0.203 | 0.171 / 40.0 | 0.283 | 6398 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1926 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 595 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 8100 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1956 / 0 / 0 |
| hud | 35.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12285 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.004 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.033; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.008; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.008; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.725 / 0.0 | 0.545 / 0.562 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.34 / 10.094 | 0.018 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.602 / 0.62 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.369 / 10.95 | 0.021 / 0.006 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 115.362 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.178 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.264 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 94.276 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 145.16 / 88.393 | 0.16 / 0.051 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 129.617 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.86 | 0.0 / 0.0 | 574.66 / 573.85 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.86 (8.0 / 8.0) |
| mam | 13.86 / 13.86 | 1.0 / 1.0 | 112.96 / 112.88 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.86 (8.0 / 8.0) |
| ponder | 82.57 / 82.55 | 0.0 / 0.11 | 203.68 / 203.6 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.59 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.26 (base 20.23) | 13 (base 13) | 49052 (base 48604) | 423.22 (base 421.54) | 5.48 (base 5.48) |
| factory_flat | 28.31 (base 28.32) | 13 (base 13) | 52240 (base 48340) | 422.33 (base 422.36) | 3.54 (base 5.48) |
| sift | 41.0 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 441.01 (base 441.24) | 5.48 (base 4.24) |
| hub | 25.54 (base 25.54) | 14 (base 17) | 64948 (base 66484) | 453.42 (base 451.46) | 5.48 (base 5.99) |
| mam | 24.53 (base 24.53) | 17 (base 20) | 21108 (base 22916) | 466.55 (base 472.87) | 5.48 (base 5.99) |
| ponder | 87.26 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 464.88 (base 463.76) | 5.48 (base 8.24) |
| map | 66.0 (base 67.0) | 12 (base 12) | 30924 (base 31404) | 492.82 (base 506.16) | 5.81 (base 6.31) |
| hologram | 34 median (avg 33.84) (base 34) | 20 (base 23) | 71676 (base 70624) | 471.09 (base 452.39) | 5.87 (base 6.31) |
| hud | 57.0 (base 58.0) | 14 (base 14) | 60356 (base 59972) | 492.2 (base 493.56) | 5.99 (base 8.18) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 480.55 (base 470.06) | 6.06 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.26 / 20 (29.96) | 179.53 / 226 (29.98) | median and average, x 1.10 + 2 |
| factory_flat | 28.31 / 28 (29.99) | 187.55 / 234 (29.99) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (15.65) | 547.82 / 579 (25.58) | median, x 1.10 + 2 |
| hub | 25.54 / 25 (29.88) | 161.8 / 208 (30.0) | median and average, x 1.10 + 2 |
| mam | 24.53 / 24 (29.89) | 199.94 / 246 (29.93) | median, x 1.10 + 2 |
| ponder | 87.26 / 87 (29.79) | 180.5 / 227 (30.01) | median and average, x 1.10 + 2 |
| map | 66.0 / 66 (11.93) | 716.0 / 716 (17.92) | median and average, x 1.10 + 2 |
| hologram | 33.84 / 34 (23.72) | 138.11 / 184 (29.66) | median, at most vanilla |
| hud | 57.0 / 57 (17.46) | 536.03 / 570 (26.13) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 110.92 / 158 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.633 | 12.0 / 1.633 | 0.0 / 0.0 | 256.0 / 2.728 |
| sift | 16.0 / 7.399 | 16.0 / 7.399 | 0.0 / 0.0 | 416.0 / 12.894 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.257 | 32.0 / 20.257 | 0.0 / 0.0 | 416.0 / 34.135 |
| ponder | 32.0 / 17.515 | 32.0 / 17.515 | 0.0 / 0.0 | 416.0 / 29.214 |
| map | 40.0 / 27.83 | 40.0 / 27.83 | 0.0 / 0.0 | 416.0 / 47.295 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.382 | 40.0 / 17.382 | 0.0 / 0.0 | 416.0 / 30.482 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26336.773 (48127.957) | 45.278 | 1.0 | 156760 / 233360 | 100928 (2097152.0) | 0 / 1 | 0.0 / 0 |
| factory_flat | 26336.423 (48127.283) | 45.278 | 1.0 | 159744 / 236252 | 100928 (2097131.564) | 0 / 1 | 0.0 / 0 |
| sift | 145992.429 (205173.506) | 28.844 | 1.434 | 4148 / 8052 | 0 (2097152.0) | 312 / 15 | 0.0 / 0 |
| hub | 33471.973 (58844.402) | 43.118 | 1.02 | 83908 / 84516 | 0 (2097152.0) | 3 / 1 | 0.0 / 0 |
| mam | 66773.475 (117157.589) | 43.005 | 1.011 | 40068 / 40948 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61188 / 61228 | 0 (2097152.0) | 5 / 2 | 0.0 / 0 |
| map | 362472.622 (621691.218) | 41.696 | 1.801 | 30924 / 35404 | 0 (2097152.0) | 238 / 358 | 0.0 / 0 |
| hologram | 3912.236 (6874.473) | 43.09 | 1.0 | 73916 / 74172 | 0 (2097152.0) | 25 / 1 | 0.0 / 0 |
| hud | 262786.819 (455772.206) | 42.343 | 1.841 | 60356 / 63940 | 0 (2097152.0) | 348 / 11 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1124 / 1316 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 96100 | 26.923 / 0.32 | 1.511 / 28.0 | 2.566 | 1977 / 0 | 4.973 (10.107) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.123 | 16 (vanilla 28) | 103664 | 3.007 / 0.001 | 1.633 / 12.0 | 2.768 | 310 / 0 | 4.067 (17.933) |
| sift | plain, one multi-draw per region and layer | 21.067 | 430.375 | 16 (vanilla 28) | 462896 | 24.653 / 0.343 | 7.399 / 16.0 | 12.361 | 2989 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.084 | 16 (vanilla 28) | 117492 | 23.887 / 0.331 | 1.879 / 12.0 | 3.137 | 3079 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.145 | 16 (vanilla 28) | 1189900 | 24.404 / 0.356 | 20.257 / 32.0 | 31.774 | 3726 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1016432 | 24.404 / 0.356 | 17.515 / 32.0 | 27.142 | 3727 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 39.164 | 554.319 | 16 (vanilla 28) | 1744280 | 21.177 / 0.284 | 27.83 / 40.0 | 46.577 | 4572 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.006 | 13.027 | 16 (vanilla 28) | 13744 | 21.081 / 0.281 | 0.238 / 40.0 | 0.367 | 4634 / 0 | None (None) |
| hud | plain, one multi-draw per region and layer | 29.083 | 403.152 | 16 (vanilla 28) | 1090120 | 17.252 / 0.233 | 17.382 / 40.0 | 29.109 | 6424 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 17.094 / 0.23 | 0.171 / 40.0 | 0.283 | 6532 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1878 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 36.0 | 15.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 8604 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1900 / 0 / 0 |
| hud | 35.0 | 8.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12250 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.033; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.333 / 0.0 | 0.532 / 0.592 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.975 / 10.823 | 0.019 / 0.009 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.255 / 0.0 | 0.633 / 0.645 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.798 / 11.647 | 0.019 / 0.008 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 109.041 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.008 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 32.79 / 0.0 | 0.009 / 0.003 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 94.435 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 148.364 / 90.202 | 0.159 / 0.055 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 106.272 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.88 | 0.0 / 0.0 | 574.48 / 574.69 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.88 (8.0 / 8.0) |
| mam | 13.86 / 13.87 | 1.0 / 1.0 | 112.81 / 113.29 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.87 (8.0 / 8.0) |
| ponder | 82.59 / 82.57 | 0.11 / 0.11 | 203.74 / 203.89 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.62 / 81.6 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.21 (base 14.22) | 15 (base 15) | 51636 (base 51148) | 422.63 (base 420.23) | 3.71 (base 3.71) |
| factory_flat | 22.24 (base 22.31) | 15 (base 15) | 54712 (base 50904) | 422.31 (base 421.17) | 3.46 (base 3.46) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 440.42 (base 439.9) | 3.71 (base 4.23) |
| hub | 21.54 (base 21.55) | 16 (base 18) | 66576 (base 67484) | 451.94 (base 450.49) | 3.71 (base 4.23) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 465.87 (base 462.56) | 7.71 (base 5.46) |
| ponder | 87.25 (base 87.24) | 14 (base 14) | 52268 (base 52248) | 464.19 (base 462.87) | 7.71 (base 8.23) |
| map | 30.0 (base 30.0) | 14 (base 14) | 48980 (base 47072) | 491.84 (base 504.09) | 8.1 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| hud | 30.92 (base 31.0) | 16 (base 16) | 75584 (base 72908) | 477.51 (base 490.43) | 8.1 (base 8.16) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 471.54 (base 475.26) | 8.1 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.21 / 14 (29.87) | 109.47 / 154 (29.41) | median and average, x 1.10 + 2 |
| factory_flat | 22.24 / 22 (29.99) | 117.84 / 162 (29.27) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (18.74) | 153.0 / 153 (13.48) | median, x 1.10 + 2 |
| hub | 21.54 / 21 (29.85) | 116.63 / 161 (29.31) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.89) | 124.78 / 162 (27.18) | median, x 1.10 + 2 |
| ponder | 87.25 / 87 (29.99) | 181.14 / 227 (29.8) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (13.87) | 170.0 / 170 (9.56) | median and average, x 1.10 + 2 |
| hud | 30.92 / 31 (21.41) | 171.0 / 171 (12.89) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 99.69 / 147 (29.98) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.399 | 16.0 / 7.399 | 0.0 / 0.0 | 416.0 / 12.894 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.311 | 32.0 / 20.311 | 0.0 / 0.0 | 416.0 / 33.871 |
| ponder | 32.0 / 17.566 | 32.0 / 17.566 | 0.0 / 0.0 | 416.0 / 28.951 |
| map | 40.0 / 27.83 | 40.0 / 27.83 | 0.0 / 0.0 | 416.0 / 47.429 |
| hud | 56.0 / 16.225 | 56.0 / 16.225 | 0.0 / 0.0 | 416.0 / 28.413 |
| create_ct | 56.0 / 0.687 | 56.0 / 0.687 | 0.0 / 0.0 | 416.0 / 1.192 |


Package 4b, siftec vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26336.838 (48128.074) | 45.278 | 1.0 | 159344 / 234848 | 100928 (2097152.0) | 1 / 4 | 0.0 / 0 |
| factory_flat | 26336.694 (48127.813) | 45.278 | 1.0 | 162236 / 237832 | 100928 (2097152.0) | 0 / 4 | 0.0 / 0 |
| sift | 147699.866 (205064.84) | 27.974 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 332 / 269 | 0.0 / 0 |
| hub | 33472.067 (58844.567) | 43.118 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 3 / 7 | 0.0 / 0 |
| mam | 66773.289 (117157.262) | 43.005 | 1.011 | 42708 / 42688 | 0 (2097152.0) | 12 / 32 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61268 / 61224 | 0 (2097152.0) | 1 / 4 | 0.0 / 0 |
| map | 395139.365 (621324.982) | 36.404 | 1.228 | 48980 / 46384 | 0 (2097152.0) | 277 / 190 | 0.0 / 0 |
| hud | 319887.659 (494195.972) | 35.271 | 1.286 | 75584 / 73008 | 0 (2097152.0) | 68 / 257 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 96100 | 25.765 / 0.314 | 1.511 / 28.0 | 2.566 | 2003 / 0 | 2.341 (2.796) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 96084 | 2.91 / 0.001 | 1.511 / 12.0 | 2.566 | 272 / 0 | 1.477 (1.652) |
| sift | indirect, one multi-draw per arena and layer | 3.008 | 430.147 | 16 (vanilla 28) | 462896 | 23.69 / 0.338 | 7.399 / 16.0 | 12.361 | 3009 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 22.981 / 0.327 | 1.879 / 12.0 | 3.137 | 3094 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1193108 | 23.661 / 0.354 | 20.311 / 32.0 | 31.859 | 3746 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1019432 | 23.661 / 0.354 | 17.566 / 32.0 | 27.222 | 3747 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.011 | 553.993 | 16 (vanilla 28) | 1744280 | 20.565 / 0.282 | 27.83 / 40.0 | 46.577 | 4592 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.007 | 442.03 | 16 (vanilla 28) | 1018140 | 16.467 / 0.241 | 16.225 / 56.0 | 27.187 | 6241 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 42660 | 16.314 / 0.239 | 0.687 / 56.0 | 1.139 | 6349 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2250 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 597 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 10008 / 0 / 0 |
| hud | 39.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 16731 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.003 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.003 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 95.238 / 0.0 | 0.556 / 0.536 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.631 / 10.503 | 0.014 / 0.005 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 95.563 / 0.0 | 0.59 / 0.569 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.86 / 10.52 | 0.012 / 0.005 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 95.085 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 102.239 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.882 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.022 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.87 | 0.0 / 0.0 | 574.7 / 574.23 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.87 (8.0 / 8.0) |
| mam | 13.86 / 13.82 | 1.0 / 1.0 | 113.14 / 111.51 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.82 (8.0 / 8.0) |
| ponder | 82.58 / 82.58 | 0.0 / 0.11 | 203.88 / 203.98 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.61 / 81.61 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

## Bench-only jar (quicksilver-bench)

| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |
|---|---|---|---|---|---|---|---|
| bench-plain | spawn | 9.29 | 135.09 | 185 | 123 | 1060564 | [] |
| bench-plain | flythrough | 5.37 | 376.84 | 107 | 123 | 8449504 | [] |
| bench-plain | water_glass | 16.31 | 71.32 | 326 | 124 | 17460 | [] |
| bench-plain | nether | 16.03 | 74.93 | 320 | 121 | 434852 | [] |
| bench-sodium | spawn | 10.93 | 114.01 | 218 | 147 | 264352 | [] |
| bench-sodium | flythrough | 7.34 | 307.58 | 147 | 159 | 265784 | [] |
| bench-sodium | water_glass | 20.02 | 57.91 | 147 | 135 | 9228 | [] |
| bench-sodium | nether | 27.43 | 43.22 | 1 | 129 | 263664 | [] |

