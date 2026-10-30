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
| spawn | 0.2015 | 10.05 | 0.4385 | 0.1963 | 0.302 |
| flythrough | 0.0004 | 14.95 | 0.0009 | 0.001 | 0.001 |
| water_glass | 0.0034 | 6.45 | 0.0038 | 0.0048 | 0.0036 |
| nether | 0.0357 | 0.8 | 0.0447 | 0.0007 | 0.0003 |
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
| translucent_move | 0.0002 | 0.3 | 0.0003 | 0.0 | 0.0002 |
| translucent_move.x2404 | 0.0002 | 0.3 | 0.0004 | 0.0002 | 0.0003 |
| translucent_move.x2414 | 0.0003 | 0.3 | 0.0004 | 0.0002 | 0.0003 |
| translucent_move.x2424 | 0.0001 | 0.3 | 0.0002 | 0.0002 | 0.0001 |
| translucent_move.low | 0.0008 | 0.3 | 0.0014 | 0.0011 | 0.001 |
| translucent_move.back | 0.0001 | 0.3 | 0.0002 | 0.0002 | 0.0002 |
| translucent_move.bare_back | 0.0002 | 0.3 | 0.0001 | 0.0 | 0.0001 |
| water_glass_oit | 0.0039 | 0.3 | 0.0041 | 0.0039 | 0.0049 |
| translucent_move_oit | 0.0001 | 0.3 | 0.0002 | 0.0 | 0.0004 |
| translucent_move_oit.x2404 | 0.0001 | 0.3 | 0.0005 | 0.0004 | 0.0004 |
| translucent_move_oit.x2414 | 0.0001 | 0.3 | 0.0002 | 0.0005 | 0.0003 |
| translucent_move_oit.x2424 | 0.0003 | 0.3 | 0.0005 | 0.0002 | 0.0001 |
| translucent_move_oit.low | 0.0004 | 0.3 | 0.0009 | 0.0011 | 0.001 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 | 0.0002 | 0.0001 |
| translucent_move_oit.bare_back | 0.0003 | 0.3 | 0.0002 | 0.0001 | 0.0002 |
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
| spawn | 92.58 | 18057 | 244 (716) | 243 (741) | 302 (828) |
| flythrough | 97.29 | 6020 | 47 (292) | 47 (292) | 51 (293) |
| water_glass | 95.8 | 25508 | 17 (2618) | 22 (2623) | 13 (2727) |
| nether | 99.39 | 905 | 82 (537) | 5 (460) | 3 (470) |
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
| translucent_move | 96.93 | 13 | 2 (77) | 1 (76) | 3 (69) |
| translucent_move.x2404 | 95.93 | 15 | 5 (105) | 4 (104) | 8 (92) |
| translucent_move.x2414 | 95.86 | 13 | 3 (106) | 2 (105) | 3 (100) |
| translucent_move.x2424 | 96.74 | 13 | 2 (79) | 1 (78) | 1 (67) |
| translucent_move.low | 96.11 | 41 | 17 (25) | 14 (20) | 13 (15) |
| translucent_move.back | 96.93 | 13 | 2 (77) | 2 (77) | 1 (67) |
| translucent_move.bare_back | 96.9 | 13 | 1 (76) | 1 (76) | 2 (68) |
| water_glass_oit | 95.8 | 57 | 15 (2615) | 17 (2617) | 26 (2739) |
| translucent_move_oit | 96.93 | 12 | 2 (77) | 1 (76) | 4 (70) |
| translucent_move_oit.x2404 | 95.93 | 16 | 5 (106) | 5 (105) | 4 (88) |
| translucent_move_oit.x2414 | 95.86 | 10 | 4 (107) | 3 (106) | 2 (99) |
| translucent_move_oit.x2424 | 96.74 | 15 | 2 (79) | 2 (79) | 1 (67) |
| translucent_move_oit.low | 96.11 | 40 | 12 (17) | 14 (20) | 16 (18) |
| translucent_move_oit.back | 96.93 | 12 | 1 (76) | 2 (77) | 2 (69) |
| translucent_move_oit.bare_back | 96.9 | 15 | 2 (77) | 1 (76) | 2 (68) |
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
| spawn | 0.4359 / 222 | 0.3083 / 333 | 0.0528 / 203 |
| flythrough | 0.0 / 1 | 0.0001 / 0 | 0.0001 / 1 |
| water_glass | 0.0037 / 21 | 0.005 / 21 | 0.0038 / 14 |
| nether | 0.0401 / 41 | 0.0018 / 16 | 0.0 / 0 |
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
| translucent_move | 0.0003 / 2 | 0.0 / 0 | 0.0001 / 1 |
| translucent_move.x2404 | 0.0001 / 0 | 0.0 / 1 | 0.0001 / 0 |
| translucent_move.x2414 | 0.0002 / 1 | 0.0 / 4 | 0.0002 / 2 |
| translucent_move.x2424 | 0.0003 / 0 | 0.0001 / 0 | 0.0001 / 2 |
| translucent_move.low | 0.0008 / 8 | 0.0005 / 3 | 0.0003 / 3 |
| translucent_move.back | 0.0001 / 0 | 0.0001 / 0 | 0.0002 / 0 |
| translucent_move.bare_back | 0.0002 / 3 | 0.0 / 0 | 0.0001 / 1 |
| water_glass_oit | 0.0048 / 23 | 0.0048 / 24 | 0.0037 / 15 |
| translucent_move_oit | 0.0001 / 0 | 0.0001 / 1 | 0.0001 / 0 |
| translucent_move_oit.x2404 | 0.0002 / 2 | 0.0 / 1 | 0.0002 / 1 |
| translucent_move_oit.x2414 | 0.0003 / 6 | 0.0003 / 0 | 0.0004 / 3 |
| translucent_move_oit.x2424 | 0.0004 / 3 | 0.0001 / 1 | 0.0001 / 2 |
| translucent_move_oit.low | 0.0002 / 2 | 0.0004 / 5 | 0.0001 / 2 |
| translucent_move_oit.back | 0.0 / 0 | 0.0001 / 1 | 0.0002 / 1 |
| translucent_move_oit.bare_back | 0.0003 / 2 | 0.0001 / 1 | 0.0002 / 2 |
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
| spawn | 14.0 (base 13.6) | 15 (base 14) | 27364 (base 22476) | 249.17 (base 259.07) | 5.48 (base 3.99) |
| flythrough | 13.04 (reported, not gated) | 40 (reported, not gated) | 532020 (reported, not gated) | 304.31 (base 324.25) | 3.73 (base 4.24) |
| water_glass | 9.0 (base 8.97) | 15 (base 15) | 15432 (base 15576) | 233.22 (base 250.02) | 5.48 (base 5.99) |
| nether | 15.87 (base 15.63) | 12 (base 12) | 21252 (base 19648) | 253.96 (base 273.53) | 5.48 (base 5.99) |
| mobs | 19.84 (base 19.77) | 17 (base 17) | 616288 (base 1010948) | 252.96 (base 268.37) | 5.48 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 252.33 (base 264.69) | 5.48 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100612 (base 126320) | 257.94 (base 265.1) | 5.48 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82504 (reported, not gated) | 258.39 (base 265.74) | 5.48 (base 4.24) |
| translucent_move | 8.99 (reported, not gated) | 15 (reported, not gated) | 11040 (reported, not gated) | 260.9 (base 267.48) | 5.48 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 16104 (base 14688) | 232.67 (base 245.02) | 3.6 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10928 (reported, not gated) | 235.14 (base 247.19) | 3.73 (base 3.73) |
| flat_plants | 19.0 (base 18.29) | 15 (base 15) | 10128 (base 9812) | 235.2 (base 246.92) | 3.48 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7448 (base 7164) | 228.08 (base 241.33) | 3.6 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 255.65 (base 261.44) | 5.48 (base 5.99) |
| animation | 49 median (avg 39.93) (base 49) | 15 (base 15) | 2224 (base 2056) | 253.21 (base 260.68) | 5.48 (base 5.99) |
| animated_items | 60 median (avg 46.76) (base 60) | 24 (base 23) | 22136 (base 21940) | 228.33 (base 239.81) | 5.48 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 14.0 / 14 (9.84) | 124.0 / 124 (9.16) | median and average, x 1.10 + 2 |
| flythrough | 13.04 / 13 (6.81) | 123.05 / 123 (5.52) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (19.07) | 124.0 / 124 (16.12) | median, at most vanilla |
| nether | 15.87 / 16 (18.64) | 120.82 / 121 (15.81) | median, x 1.10 + 2 |
| mobs | 19.84 / 20 (23.85) | 133.7 / 134 (19.49) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.27 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.33 / 133 (29.97) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.87) | 94.27 / 133 (29.97) | median and average, x 1.10 + 2 |
| translucent_move | 8.99 / 9 (18.97) | 124.0 / 124 (16.16) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (10.71) | 129.0 / 129 (8.71) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (9.67) | 129.0 / 129 (8.34) | median and average, at most vanilla |
| flat_plants | 19.0 / 19 (19.52) | 124.0 / 124 (16.22) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.64) | 100.17 / 123 (24.87) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.33 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.93 / 49 (30.0) | 144.46 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.76 / 60 (30.0) | 155.22 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 23.157 | 40.0 / 23.157 | 0.0 / 0.0 | 416.0 / 37.882 |
| flythrough | 112.0 / 84.268 | 112.0 / 84.268 | 0.0 / 0.0 | 416.0 / 143.465 |
| water_glass | 40.0 / 12.582 | 40.0 / 12.582 | 0.0 / 0.0 | 416.0 / 21.573 |
| nether | 20.0 / 15.244 | 20.0 / 15.244 | 0.0 / 0.0 | 256.0 / 26.781 |
| mobs | 16.0 / 9.624 | 16.0 / 9.624 | 0.0 / 0.0 | 256.0 / 16.864 |
| outline | 16.0 / 1.384 | 16.0 / 1.384 | 0.0 / 0.0 | 256.0 / 2.457 |
| occlusion | 20.0 / 2.572 | 20.0 / 2.572 | 0.0 / 0.0 | 416.0 / 4.485 |
| occlusion_move | 20.0 / 3.414 | 20.0 / 3.414 | 0.0 / 0.0 | 416.0 / 5.886 |
| translucent_move | 28.0 / 13.273 | 28.0 / 13.273 | 0.0 / 0.0 | 416.0 / 22.742 |
| water_glass_oit | 20.0 / 12.434 | 20.0 / 12.434 | 0.0 / 0.0 | 416.0 / 21.573 |
| translucent_move_oit | 28.0 / 12.917 | 28.0 / 12.917 | 0.0 / 0.0 | 416.0 / 22.031 |
| flat_plants | 24.0 / 13.904 | 24.0 / 13.904 | 0.0 / 0.0 | 416.0 / 24.016 |
| renderer_off | 24.0 / 8.838 | 24.0 / 8.838 | 0.0 / 0.0 | 416.0 / 15.434 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 518258.082 (699040.724) | 25.862 | 1.155 | 495988 / 647784 | 468512 (2097152.0) | 196 / 183 | 0.0 / 0 |
| flythrough | 551323.544 (882392.529) | 37.519 | 1.085 | 2255636 / 7921100 | 2094576 (2097152.0) | 136 / 110 | 11.664 / 108 |
| water_glass | 215790.898 (386203.003) | 44.125 | 1.117 | 18008 / 17144 | 0 (2097152.0) | 381 / 322 | 0.0 / 0 |
| nether | 320498.57 (480218.774) | 33.26 | 1.273 | 289972 / 642296 | 268160 (2097152.0) | 372 / 316 | 0.0 / 0 |
| mobs | 166616.57 (301005.719) | 44.647 | 0.99 | 616288 / 627864 | 0 (2097152.0) | 10 / 255 | 0.0 / 0 |
| outline | 25905.103 (43794.87) | 40.849 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 45838.397 (80500.167) | 43.058 | 1.2 | 100612 / 453364 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 50650.935 (89114.479) | 43.162 | 1.222 | 684708 / 1123184 | 608000 (2097152.0) | 1 / 0 | 0.0 / 0 |
| translucent_move | 204892.739 (364663.314) | 43.813 | 1.178 | 836564 / 1455612 | 825600 (2097152.0) | 308 / 323 | 0.0 / 0 |
| water_glass_oit | 216231.729 (386991.963) | 44.125 | 1.117 | 17560 / 17144 | 0 (2097152.0) | 214 / 174 | 0.0 / 0 |
| translucent_move_oit | 205041.648 (364658.342) | 43.772 | 1.176 | 837156 / 1454736 | 825600 (2097152.0) | 193 / 166 | 0.0 / 0 |
| flat_plants | 236815.667 (433945.836) | 45.427 | 1.059 | 176512 / 471172 | 166272 (2097152.0) | 282 / 324 | 0.0 / 0 |
| renderer_off | 152214.253 (276685.801) | 44.987 | 1.029 | 7448 / 7328 | 0 (2097152.0) | 0 / 6 | 0.0 / 0 |
| particles | 1927.207 (4215.013) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86832 / 150268 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.78 (4890.137) | 53.544 | 1.0 | 79864 / 123000 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.015 | 804.082 | 16 (vanilla 28) | 1400132 | 42.64 / 0.107 | 23.157 / 40.0 | 37.388 | 2571 / 0 | 4.04 (6.529) |
| flythrough | indirect, one multi-draw per arena and layer | 3.022 | 893.441 | 16 (vanilla 28) | 5103492 | 15.482 / 1.024 | 84.268 / 112.0 | 136.278 | 6398 / 0 | 8.263 (21.399) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 273.717 | 16 (vanilla 28) | 779264 | 34.619 / 0.09 | 12.582 / 40.0 | 20.809 | 3354 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 470.261 | 16 (vanilla 28) | 957968 | 29.26 / 0.197 | 15.244 / 20.0 | 25.581 | 4436 / 0 | 2.171 (3.626) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.413 | 16 (vanilla 28) | 600752 | 26.678 / 0.174 | 9.624 / 16.0 | 16.042 | 4876 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 87444 | 26.229 / 0.173 | 1.384 / 16.0 | 2.335 | 4970 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 160732 | 25.041 / 0.164 | 2.572 / 20.0 | 4.292 | 5217 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.439 | 16 (vanilla 28) | 214568 | 23.869 / 0.156 | 3.414 / 20.0 | 5.73 | 5478 / 0 | 7.356 (15.548) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.008 | 249.631 | 16 (vanilla 28) | 817952 | 20.83 / 0.142 | 13.273 / 28.0 | 21.842 | 6256 / 0 | 6.604 (14.464) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.023 | 282.313 | 16 (vanilla 28) | 770384 | 7.554 / 0.043 | 12.434 / 20.0 | 20.571 | 1088 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.026 | 264.155 | 16 (vanilla 28) | 795596 | 6.676 / 0.094 | 12.917 / 28.0 | 21.245 | 2425 / 0 | 7.592 (18.175) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.008 | 286.733 | 16 (vanilla 28) | 865672 | 6.412 / 0.01 | 13.904 / 24.0 | 23.116 | 946 / 0 | 2.066 (1.479) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 207.349 | 16 (vanilla 28) | 552440 | 2.407 / 0.021 | 8.838 / 24.0 | 14.752 | 608 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 20.782 / 0.142 | 0.131 / 28.0 | 0.225 | 6298 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.612 / 0.141 | 0.302 / 28.0 | 0.518 | 6400 / 0 | 2.574 (4.575) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.557 / 0.141 | 0.153 / 28.0 | 0.261 | 6447 / 0 | 1.22 (1.755) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 156.0 | 1.0 | 0.0 | 0 | 0 | 42 (0.024 / 0.036 / 0.039; 0) | 0 | 30732 / 0 / 0 |
| flythrough | 187.007 | 1.0 | 0.0 | 0 | 0 | 846 (0.039 / 0.051 / 6.103; 0) | 45 | 25620 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1528 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.01 / 0.01; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.107 / 0.168 / 0.193; 0) | 180 | 2660 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 645 | 0 (None / None / None; 0) | 0 | 860 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 582 | 0 (None / None / None; 0) | 0 | 1358 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1564 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2376 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 350.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.03 (0.082; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 429.891 | 1.0 (0.0, 0.0) | 1.234 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.072; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.136 (0.224; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.064 (0.173; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.351 | 10.0 (9.0, 2.363) | 6.0 (6.0, 2.279) | 0.056 (0.265; 0) | 0 | 2776 / 1502690 / 0 / 0 |
| translucent_move | 136.295 | 1.0 (0.0, 0.0) | 0.763 (0.0, 0.0) | 0.001 (0.007; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.644 | 1.0 (0.0, 0.0) | 0.742 (0.0, 0.0) | 0.002 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.033; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 113.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.007 (0.038; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 83.883 / 16.538 | 0.021 / 0.014 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.155 / 0.0 | 0.003 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.832 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 50.476 / 50.44 | 0.013 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.823 / 0.866 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 34.98 / 10.102 | 0.067 / 0.011 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 114.704 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.279 / 13.292 | 0.005 / 0.013 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.841 / 55.916 | 0.012 / 0.021 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.362 / 12.263 | 0.005 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.997 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.277 / 2.161 | 0.014 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 92.369 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.004 / 0.038 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.269 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.798 / 2.795 | 0.004 / 0.006 | 56 (3) | 40.51 / 9.238 / 31.273 | 3 / 0 | 10.985 |
| animated_items | 136.553 / 28.09 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.564 / 7.98 / 32.584 | 0 / 0 | 9.0 |

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
| spawn | 49.0 (base 49.0) | 12 (base 13) | 1380 (base 6372) | 247.97 (base 256.29) | 3.48 (base 3.73) |
| flythrough | 67.12 (reported, not gated) | 42 (reported, not gated) | 540940 (reported, not gated) | 303.01 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 19.98 (base 20.0) | 13 (base 14) | 7652 (base 10264) | 233.6 (base 249.83) | 3.48 (base 5.99) |
| nether | 33.86 (base 33.79) | 10 (base 11) | 4644 (base 10656) | 254.46 (base 272.57) | 3.48 (base 5.99) |
| mobs | 28.84 (base 28.97) | 14 (base 16) | 610516 (base 1015892) | 253.08 (base 268.23) | 3.48 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 252.83 (base 264.41) | 3.48 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 258.44 (base 264.64) | 3.48 (base 4.24) |
| occlusion_move | 24.04 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 258.86 (base 265.66) | 3.48 (base 5.99) |
| translucent_move | 17.99 (reported, not gated) | 13 (reported, not gated) | 3060 (reported, not gated) | 260.87 (base 268.19) | 5.48 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7652 (base 7604) | 232.5 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.27 (reported, not gated) | 13 (reported, not gated) | 2724 (reported, not gated) | 235.38 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 33.0 (base 33.0) | 13 (base 13) | 1716 (base 1716) | 236.0 (base 248.11) | 3.48 (base 5.99) |
| renderer_off | 17.68 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 228.14 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 255.96 (base 261.4) | 5.48 (base 4.24) |
| animation | 49 median (avg 39.94) (base 49) | 13 (base 13) | 1716 (base 1716) | 253.52 (base 260.68) | 5.48 (base 4.24) |
| animated_items | 60 median (avg 46.77) (base 60) | 22 (base 22) | 21668 (base 21668) | 228.8 (base 239.53) | 5.48 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.0 / 49 (11.42) | 848.35 / 848 (16.93) | median and average, x 1.10 + 2 |
| flythrough | 67.12 / 64 (6.98) | 1044.99 / 1037 (11.42) | median and average, at most vanilla |
| water_glass | 19.98 / 20 (20.32) | 357.49 / 390 (27.63) | median, at most vanilla |
| nether | 33.86 / 34 (19.16) | 557.59 / 588 (26.97) | median, x 1.10 + 2 |
| mobs | 28.84 / 29 (23.65) | 290.27 / 329 (29.99) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 127.33 / 166 (30.0) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (30.0) | 163.27 / 202 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 24.04 / 24 (29.9) | 168.62 / 203 (29.98) | median and average, x 1.10 + 2 |
| translucent_move | 17.99 / 18 (18.54) | 336.36 / 370 (28.99) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (9.95) | 401.0 / 401 (13.83) | median, at most vanilla |
| translucent_move_oit | 25.27 / 25 (9.36) | 388.56 / 388 (13.13) | median and average, at most vanilla |
| flat_plants | 33.0 / 33 (19.16) | 371.14 / 405 (28.09) | median, x 1.10 + 2 |
| renderer_off | 17.68 / 18 (29.5) | 288.85 / 327 (29.81) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.27 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 156.22 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.0) | 167.09 / 202 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.766 | 36.0 / 22.766 | 0.0 / 0.0 | 416.0 / 36.944 |
| flythrough | 112.0 / 83.615 | 112.0 / 83.615 | 0.0 / 0.0 | 416.0 / 152.066 |
| water_glass | 36.0 / 12.434 | 36.0 / 12.434 | 0.0 / 0.0 | 416.0 / 21.573 |
| nether | 20.0 / 15.25 | 20.0 / 15.25 | 0.0 / 0.0 | 256.0 / 26.936 |
| mobs | 16.0 / 9.624 | 16.0 / 9.624 | 0.0 / 0.0 | 256.0 / 16.864 |
| outline | 16.0 / 1.384 | 16.0 / 1.384 | 0.0 / 0.0 | 256.0 / 2.457 |
| occlusion | 20.0 / 2.449 | 20.0 / 2.449 | 0.0 / 0.0 | 416.0 / 4.538 |
| occlusion_move | 20.0 / 3.389 | 20.0 / 3.389 | 0.0 / 0.0 | 416.0 / 5.954 |
| translucent_move | 28.0 / 13.282 | 28.0 / 13.282 | 0.0 / 0.0 | 416.0 / 22.879 |
| water_glass_oit | 20.0 / 12.434 | 20.0 / 12.434 | 0.0 / 0.0 | 416.0 / 21.573 |
| translucent_move_oit | 28.0 / 12.87 | 28.0 / 12.87 | 0.0 / 0.0 | 416.0 / 22.261 |
| flat_plants | 24.0 / 14.127 | 24.0 / 14.127 | 0.0 / 0.0 | 416.0 / 23.982 |
| renderer_off | 24.0 / 8.838 | 24.0 / 8.838 | 0.0 / 0.0 | 416.0 / 15.434 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 451018.412 (691044.0) | 34.734 | 1.772 | 500196 / 1255540 | 498816 (2097152.0) | 228 / 338 | 0.0 / 0 |
| flythrough | 551527.928 (883136.417) | 37.549 | 1.084 | 2110908 / 6997228 | 2096072 (2097152.0) | 139 / 228 | 6.136 / 50 |
| water_glass | 215756.113 (386140.749) | 44.125 | 1.117 | 9780 / 11092 | 0 (2097152.0) | 82 / 5 | 0.0 / 0 |
| nether | 270579.655 (480191.52) | 43.652 | 1.704 | 247888 / 264884 | 243712 (2097152.0) | 383 / 5 | 0.0 / 0 |
| mobs | 166620.263 (301012.39) | 44.647 | 0.99 | 610516 / 612388 | 0 (2097152.0) | 6 / 0 | 0.0 / 0 |
| outline | 25905.175 (43794.992) | 40.849 | 1.0 | 5012 / 5460 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 43843.072 (76559.599) | 42.733 | 1.214 | 98148 / 124940 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 49097.427 (86031.564) | 42.931 | 1.235 | 738860 / 1116540 | 664832 (2097152.0) | 0 / 2 | 0.0 / 0 |
| translucent_move | 204924.065 (364638.73) | 43.801 | 1.17 | 830628 / 1450068 | 825600 (2097152.0) | 307 / 0 | 0.0 / 0 |
| water_glass_oit | 216313.0 (387137.414) | 44.125 | 1.117 | 9892 / 11428 | 0 (2097152.0) | 198 / 276 | 0.0 / 0 |
| translucent_move_oit | 205192.107 (364904.139) | 43.768 | 1.168 | 831444 / 1450564 | 825600 (2097152.0) | 187 / 262 | 0.0 / 0 |
| flat_plants | 240335.508 (440955.319) | 45.497 | 1.059 | 1940 / 4164 | 0 (2097152.0) | 379 / 3 | 0.0 / 0 |
| renderer_off | 152217.997 (276692.971) | 44.987 | 1.029 | 1380 / 3188 | 0 (2097152.0) | 2 / 3 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 8772 / 8964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86324 / 149988 | 84608 (2097152.0) | 0 / 1 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79396 / 122740 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.171 | 791.456 | 16 (vanilla 28) | 1376384 | 43.056 / 0.081 | 22.766 / 36.0 | 36.753 | 2477 / 0 | 6.799 (10.548) |
| flythrough | plain, one multi-draw per region and layer | 57.424 | 898.216 | 16 (vanilla 28) | 5064364 | 15.317 / 0.991 | 83.615 / 112.0 | 135.233 | 6383 / 0 | 8.333 (21.017) |
| water_glass | plain, one multi-draw per region and layer | 14.034 | 273.672 | 16 (vanilla 28) | 770384 | 35.024 / 0.076 | 12.434 / 36.0 | 20.571 | 3114 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.052 | 470.225 | 16 (vanilla 28) | 957968 | 29.191 / 0.187 | 15.25 / 20.0 | 25.581 | 4215 / 0 | 3.274 (5.059) |
| mobs | plain, one multi-draw per region and layer | 11.023 | 197.417 | 16 (vanilla 28) | 600752 | 26.58 / 0.164 | 9.624 / 16.0 | 16.042 | 4661 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 87444 | 26.127 / 0.163 | 1.384 / 16.0 | 2.335 | 4755 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 70.117 | 16 (vanilla 28) | 152864 | 25.045 / 0.156 | 2.449 / 20.0 | 4.082 | 4992 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.993 | 75.439 | 16 (vanilla 28) | 211792 | 23.507 / 0.146 | 3.389 / 20.0 | 5.655 | 5317 / 0 | 7.484 (17.629) |
| translucent_move | plain, one multi-draw per region and layer | 12.032 | 249.535 | 16 (vanilla 28) | 818188 | 20.52 / 0.133 | 13.282 / 28.0 | 21.848 | 6091 / 0 | 7.823 (22.443) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.081 | 282.419 | 16 (vanilla 28) | 770384 | 7.554 / 0.043 | 12.434 / 20.0 | 20.571 | 1088 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.358 | 264.449 | 16 (vanilla 28) | 792548 | 6.427 / 0.087 | 12.87 / 28.0 | 21.163 | 2416 / 0 | 7.44 (22.128) |
| flat_plants | plain, one multi-draw per region and layer | 17.045 | 286.749 | 16 (vanilla 28) | 879608 | 6.047 / 0.01 | 14.127 / 24.0 | 23.488 | 938 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.022 | 207.351 | 16 (vanilla 28) | 552448 | 2.392 / 0.021 | 8.838 / 24.0 | 14.752 | 607 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 20.466 / 0.133 | 0.131 / 28.0 | 0.225 | 6134 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.298 / 0.132 | 0.302 / 28.0 | 0.518 | 6236 / 0 | 1.882 (2.348) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.24 / 0.132 | 0.153 / 28.0 | 0.261 | 6286 / 0 | 1.337 (1.873) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 155.0 | 17.0 | 0.0 | 0 | 0 | 61 (0.029 / 0.045 / 0.055; 0) | 0 | 35495 / 0 / 0 |
| flythrough | 188.157 | 26.243 | 0.0 | 0 | 0 | 836 (0.016 / 0.042 / 0.535; 0) | 48 | 26342 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1628 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.011 / 0.011; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 181 (0.118 / 0.193 / 0.223; 0) | 181 | 2597 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 597 | 0 (None / None / None; 0) | 0 | 796 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 564 | 0 (None / None / None; 0) | 0 | 1316 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1532 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2360 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 344.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.079; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 431.743 | 1.0 (0.0, 0.0) | 1.636 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.052; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.128 (0.212; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.068 (0.166; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.351 | 10.0 (9.0, 2.358) | 6.0 (6.0, 2.281) | 0.058 (0.276; 0) | 0 | 2774 / 1500568 / 0 / 0 |
| translucent_move | 136.251 | 1.0 (0.0, 0.0) | 0.763 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.888 | 1.0 (0.0, 0.0) | 0.745 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 113.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.007 (0.048; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.346 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 84.596 / 0.0 | 0.025 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.118 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.821 |
| water_glass | 82.767 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 56.087 / 56.059 | 0.016 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 84.917 / 10.0 | 0.696 / 0.864 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 41.728 / 9.214 | 0.078 / 0.011 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 79.249 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.81 / 12.668 | 0.005 / 0.014 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 56.215 / 56.171 | 0.013 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.948 / 11.835 | 0.006 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 81.646 / 10.0 | 0.786 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 3.082 / 2.794 | 0.016 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 77.181 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.004 / 0.045 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.27 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.003 / 0.009 | 56 (3) | 40.58 / 9.24 / 31.34 | 3 / 0 | 10.985 |
| animated_items | 136.426 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

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
| spawn | 13.17 (base 14.05) | 14 (base 15) | 24860 (base 27764) | 248.01 (base 257.2) | 3.71 (base 3.98) |
| flythrough | 13.05 (reported, not gated) | 22 (reported, not gated) | 178616 (reported, not gated) | 304.68 (base 318.06) | 5.46 (base 4.23) |
| water_glass | 8.78 (base 9.0) | 15 (base 15) | 15768 (base 15108) | 233.26 (base 244.39) | 3.71 (base 4.23) |
| nether | 14.11 (base 15.71) | 12 (base 12) | 20716 (base 19912) | 254.3 (base 268.95) | 3.71 (base 4.23) |
| mobs | 19.67 (base 19.97) | 16 (base 17) | 616128 (base 1008176) | 253.08 (base 264.31) | 3.71 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 252.76 (base 264.58) | 3.71 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100612 (base 126356) | 258.61 (base 264.78) | 3.71 (base 3.98) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82504 (reported, not gated) | 258.95 (base 265.16) | 3.71 (base 4.23) |
| translucent_move | 8.76 (reported, not gated) | 15 (reported, not gated) | 10928 (reported, not gated) | 260.77 (base 268.35) | 3.71 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15656 (base 15024) | 233.22 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 11076 (reported, not gated) | 235.5 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 15.57 (base 18.96) | 15 (base 15) | 10240 (base 9864) | 235.2 (base 248.07) | 3.46 (base 5.98) |
| renderer_off | 7.67 (base 7.83) | 14 (base 14) | 7448 (base 7180) | 228.46 (base 241.13) | 3.59 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 255.83 (base 261.4) | 3.71 (base 5.98) |
| animation | 49 median (avg 39.95) (base 49) | 15 (base 15) | 2224 (base 2056) | 253.38 (base 260.68) | 3.71 (base 5.98) |
| animated_items | 60 median (avg 46.73) (base 60) | 23 (base 23) | 21976 (base 21940) | 228.31 (base 239.74) | 3.71 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.17 / 13 (14.35) | 123.0 / 123 (11.7) | median and average, x 1.10 + 2 |
| flythrough | 13.05 / 13 (11.07) | 123.06 / 123 (7.19) | median and average, at most vanilla + 2 |
| water_glass | 8.78 / 9 (25.77) | 124.0 / 124 (18.56) | median, at most vanilla |
| nether | 14.11 / 16 (23.65) | 120.87 / 121 (18.78) | median, x 1.10 + 2 |
| mobs | 19.67 / 20 (29.77) | 114.67 / 134 (24.0) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.2 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (30.0) | 94.27 / 133 (29.99) | median and average, x 1.10 + 2 |
| translucent_move | 8.76 / 9 (26.29) | 123.09 / 124 (19.25) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (13.92) | 129.0 / 129 (10.42) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (13.88) | 129.0 / 129 (9.55) | median and average, at most vanilla |
| flat_plants | 15.57 / 19 (28.91) | 123.8 / 124 (19.32) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (30.0) | 90.85 / 123 (27.65) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 144.09 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 155.46 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 22.81 | 40.0 / 22.81 | 0.0 / 0.0 | 416.0 / 37.976 |
| flythrough | 112.0 / 88.654 | 112.0 / 88.654 | 0.0 / 0.0 | 416.0 / 144.79 |
| water_glass | 40.0 / 12.434 | 40.0 / 12.434 | 0.0 / 0.0 | 416.0 / 21.573 |
| nether | 20.0 / 15.245 | 20.0 / 15.245 | 0.0 / 0.0 | 256.0 / 26.776 |
| mobs | 16.0 / 9.624 | 16.0 / 9.624 | 0.0 / 0.0 | 256.0 / 16.86 |
| outline | 16.0 / 1.384 | 16.0 / 1.384 | 0.0 / 0.0 | 256.0 / 2.457 |
| occlusion | 20.0 / 2.559 | 20.0 / 2.559 | 0.0 / 0.0 | 416.0 / 4.742 |
| occlusion_move | 20.0 / 3.468 | 20.0 / 3.468 | 0.0 / 0.0 | 416.0 / 6.259 |
| translucent_move | 28.0 / 13.373 | 28.0 / 13.373 | 0.0 / 0.0 | 416.0 / 23.2 |
| water_glass_oit | 20.0 / 12.434 | 20.0 / 12.434 | 0.0 / 0.0 | 416.0 / 21.573 |
| translucent_move_oit | 28.0 / 12.962 | 28.0 / 12.962 | 0.0 / 0.0 | 416.0 / 22.579 |
| flat_plants | 24.0 / 14.071 | 24.0 / 14.071 | 0.0 / 0.0 | 416.0 / 24.516 |
| renderer_off | 24.0 / 8.838 | 24.0 / 8.838 | 0.0 / 0.0 | 416.0 / 15.473 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 507637.224 (686142.755) | 26.016 | 1.159 | 716732 / 530652 | 691872 (2097152.0) | 286 / 234 | 0.0 / 0 |
| flythrough | 563951.376 (903131.738) | 37.556 | 1.083 | 2125856 / 6847544 | 2096472 (2097152.0) | 221 / 143 | 4.041 / 63 |
| water_glass | 215643.915 (385939.946) | 44.125 | 1.117 | 17672 / 17032 | 0 (2097152.0) | 6 / 371 | 0.0 / 0 |
| nether | 320268.161 (479896.93) | 33.263 | 1.273 | 263688 / 626508 | 243456 (2097152.0) | 30 / 375 | 0.0 / 0 |
| mobs | 166547.442 (300880.834) | 44.647 | 0.99 | 616128 / 616328 | 0 (2097152.0) | 0 / 5 | 0.0 / 0 |
| outline | 25905.175 (43794.992) | 40.849 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 45711.933 (80245.298) | 43.035 | 1.2 | 232260 / 126380 | 131648 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 50872.571 (89527.883) | 43.177 | 1.222 | 743780 / 1149224 | 667072 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 206491.596 (367773.215) | 43.854 | 1.178 | 837460 / 1455368 | 825600 (2097152.0) | 5 / 309 | 0.0 / 0 |
| water_glass_oit | 216000.194 (386577.583) | 44.125 | 1.117 | 17000 / 17032 | 0 (2097152.0) | 278 / 208 | 0.0 / 0 |
| translucent_move_oit | 206769.74 (368171.35) | 43.839 | 1.177 | 837288 / 1455836 | 825600 (2097152.0) | 277 / 191 | 0.0 / 0 |
| flat_plants | 239151.042 (438621.55) | 45.477 | 1.059 | 10576 / 10180 | 0 (2097152.0) | 3 / 386 | 0.0 / 0 |
| renderer_off | 152213.689 (276685.142) | 44.987 | 1.029 | 7448 / 7328 | 0 (2097152.0) | 0 / 2 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86740 / 150268 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79704 / 123160 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.01 | 784.535 | 16 (vanilla 28) | 1380144 | 44.162 / 0.106 | 22.81 / 40.0 | 36.854 | 2784 / 0 | 1.987 (2.669) |
| flythrough | indirect, one multi-draw per arena and layer | 3.014 | 922.824 | 16 (vanilla 28) | 5365796 | 15.248 / 1.006 | 88.654 / 112.0 | 143.282 | 6575 / 0 | 4.32 (12.493) |
| water_glass | indirect, one multi-draw per arena and layer | 3.006 | 273.53 | 16 (vanilla 28) | 770384 | 36.443 / 0.095 | 12.434 / 40.0 | 20.571 | 3435 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.004 | 469.992 | 16 (vanilla 28) | 957840 | 30.738 / 0.176 | 15.245 / 20.0 | 25.577 | 4494 / 0 | 1.679 (2.392) |
| mobs | indirect, one multi-draw per arena and layer | 2.003 | 197.331 | 16 (vanilla 28) | 600752 | 28.071 / 0.156 | 9.624 / 16.0 | 16.042 | 4926 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 87444 | 27.612 / 0.155 | 1.384 / 16.0 | 2.335 | 5019 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.116 | 16 (vanilla 28) | 160224 | 26.474 / 0.148 | 2.559 / 20.0 | 4.278 | 5263 / 0 | 1.48 (1.404) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.454 | 16 (vanilla 28) | 217796 | 25.079 / 0.14 | 3.468 / 20.0 | 5.816 | 5548 / 0 | 3.306 (5.083) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.006 | 249.691 | 16 (vanilla 28) | 823992 | 21.896 / 0.129 | 13.373 / 28.0 | 22.003 | 6337 / 0 | 3.182 (6.526) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.018 | 282.011 | 16 (vanilla 28) | 770384 | 7.544 / 0.043 | 12.434 / 20.0 | 20.571 | 1092 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.018 | 264.433 | 16 (vanilla 28) | 798360 | 6.748 / 0.112 | 12.962 / 28.0 | 21.319 | 2449 / 0 | 2.829 (5.665) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.005 | 286.495 | 16 (vanilla 28) | 875728 | 6.29 / 0.01 | 14.071 / 24.0 | 23.384 | 950 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 207.346 | 16 (vanilla 28) | 552448 | 2.407 / 0.021 | 8.838 / 24.0 | 14.752 | 606 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 21.847 / 0.129 | 0.131 / 28.0 | 0.225 | 6379 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 21.671 / 0.128 | 0.302 / 28.0 | 0.518 | 6481 / 0 | 0.779 (0.916) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 21.61 / 0.128 | 0.153 / 28.0 | 0.261 | 6531 / 0 | 0.662 (0.937) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 151.254 | 1.0 | 0.0 | 0 | 0 | 54 (0.018 / 0.029 / 0.029; 0) | 0 | 43410 / 0 / 0 |
| flythrough | 194.986 | 1.0 | 0.0 | 0 | 0 | 896 (0.013 / 0.032 / 2.232; 0) | 57 | 43287 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2064 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1204 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 38 (0.014 / 0.024 / 0.024; 0) | 34 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.13 / 0.104 / 5.671; 0) | 180 | 3682 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 837 | 0 (None / None / None; 0) | 0 | 1116 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 834 | 0 (None / None / None; 0) | 0 | 1946 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2316 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 342.084 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.029 (0.078; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 434.991 | 1.0 (0.0, 0.0) | 1.162 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.067; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.11 (0.226; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.056 (0.139; 0) | 0 | 3612 / 1635032 / 0 / 0 |
| occlusion_move | 51.36 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.28) | 0.057 (0.226; 0) | 0 | 2782 / 1505150 / 0 / 0 |
| translucent_move | 136.403 | 1.0 (0.0, 0.0) | 0.77 (0.0, 0.0) | 0.001 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.824 | 1.0 (0.0, 0.0) | 0.766 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 113.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.008; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.011 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.171 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 134.656 / 0.171 | 0.031 / 0.01 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.546 / 0.0 | 0.006 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.865 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.566 / 53.64 | 0.019 / 0.027 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 8.436 | 0.872 / 0.829 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 45.963 / 7.038 | 0.067 / 0.011 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 95.833 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.539 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 114.102 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.09 / 13.108 | 0.007 / 0.016 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.03 / 52.982 | 0.016 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 14.03 / 13.903 | 0.008 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 6.92 | 0.798 / 0.953 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 3.489 / 3.282 | 0.019 / 0.012 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 83.125 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.006 / 0.055 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.005 / 0.012 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.988 |
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
| factory | 0.0 | 0.3 | 0.0 | 0.0174 | 0.0171 |
| factory_flat | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | skipped: MapMod is being finished (Andre, 2026-10-30): the map scene is out of the bench and CI until then; with it, its limits are re-collected | | | | |
| hologram | 0.0028 | 0.55 | 0.0067 | 0.0064 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | skipped: MapMod is being finished (Andre, 2026-10-30): the HUD scene (its minimap) is out of the bench and CI until then; with it, its limits are re-collected | | | | |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.56 | 500 | 0 (14) | 0 (14) | 0 (14) |
| factory_flat | 89.7 | 40 | 0 (14) | 0 (14) | 0 (14) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 98.54 | 10 | 0 (106149) | 0 (106149) | 0 (106115) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 94.16 | 413 | 38 (4054) | 40 (4057) | None (None) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Quicksilver's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0 / 0 | 0.0175 / 0 | 0.0171 / 0 |
| factory_flat | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0027 / 9 | 0.0018 / 6 | skipped |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.23 (base 12.23) | 15 (base 14) | 6956 (base 6488) | 360.47 (base 421.74) | 3.48 (base 5.48) |
| factory_flat | 11.67 (reported, not gated) | 15 (base 14) | 6844 (base 6264) | 358.48 (base 422.37) | 3.48 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 378.62 (base 440.07) | 3.48 (base 5.87) |
| hub | 21.55 (base 21.58) | 16 (base 18) | 66576 (base 67484) | 391.37 (base 451.33) | 3.48 (base 4.24) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 404.81 (base 471.8) | 5.48 (base 8.12) |
| ponder | 87.24 (base 87.25) | 14 (base 14) | 52268 (base 52248) | 403.15 (base 463.81) | 5.48 (base 8.24) |
| hologram | 25 median (avg 24.86) (reported, not gated) | 22 (base 24) | 69172 (base 70368) | 392.19 (base 453.57) | 5.35 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 393.15 (base 470.81) | 5.35 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.23 / 12 (28.83) | 106.13 / 152 (29.76) | median and average, x 1.10 + 2 |
| factory_flat | 11.67 / 12 (29.71) | 105.08 / 152 (29.92) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (15.58) | 153.0 / 153 (19.1) | median, x 1.10 + 2 |
| hub | 21.55 / 21 (29.34) | 114.95 / 161 (29.95) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.33) | 116.34 / 162 (29.82) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (29.98) | 180.57 / 227 (30.02) | median and average, x 1.10 + 2 |
| hologram | 24.86 / 25 (23.22) | 122.72 / 165 (28.32) | median, at most vanilla |
| create_ct | 6.67 / 7 (29.92) | 100.16 / 147 (29.95) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.027 | 32.0 / 20.027 | 0.0 / 0.0 | 416.0 / 34.061 |
| ponder | 32.0 / 17.543 | 32.0 / 17.543 | 0.0 / 0.0 | 416.0 / 29.244 |
| hologram | 32.0 / 0.238 | 32.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 32.0 / 0.171 | 32.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26338.448 (48131.017) | 45.278 | 1.0 | 114684 / 190096 | 100928 (2097152.0) | 10 / 5 | 0.0 / 0 |
| factory_flat | 26338.341 (48131.029) | 45.278 | 1.0 | 6844 / 6844 | 0 (2097152.0) | 3 / 2 | 0.0 / 0 |
| sift | 147743.537 (205047.203) | 27.947 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 311 / 311 | 0.0 / 0 |
| hub | 33472.927 (58846.078) | 43.118 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 17 / 2 | 0.0 / 0 |
| mam | 66222.624 (115933.165) | 42.879 | 1.011 | 42708 / 42688 | 0 (2097152.0) | 7 / 3 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61268 / 61224 | 0 (2097152.0) | 1 / 0 | 0.0 / 0 |
| hologram | 3912.414 (6874.784) | 43.09 | 1.0 | 71712 / 70952 | 0 (2097152.0) | 31 / 11 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 1 / 1 | 0.0 / 0 |

Terrain module, siftec full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.128 | 16 (vanilla 28) | 96100 | 23.665 / 0.244 | 1.511 / 28.0 | 2.566 | 1914 / 0 | 4.35 (7.215) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.125 | 16 (vanilla 28) | 96100 | 1.156 / 0.004 | 1.511 / 12.0 | 2.566 | 162 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.01 | 430.379 | 16 (vanilla 28) | 462608 | 21.967 / 0.283 | 7.389 / 16.0 | 12.353 | 2924 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.085 | 16 (vanilla 28) | 117492 | 21.358 / 0.275 | 1.879 / 12.0 | 3.137 | 3006 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.148 | 16 (vanilla 28) | 1175684 | 22.397 / 0.31 | 20.027 / 32.0 | 31.394 | 3644 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1018004 | 22.397 / 0.31 | 17.543 / 32.0 | 27.184 | 3645 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.028 | 16 (vanilla 28) | 13744 | 22.221 / 0.305 | 0.238 / 32.0 | 0.367 | 3709 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 21.863 / 0.3 | 0.171 / 32.0 | 0.283 | 3803 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1872 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 588 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 588 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1860 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.004 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.004 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.023; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.882 / 0.0 | 0.575 / 0.54 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.678 / 10.619 | 0.019 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.802 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| hologram | 98.025 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 154.601 / 94.499 | 0.159 / 0.056 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.87 | 0.0 / 0.0 | 574.36 / 574.6 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.87 (8.0 / 8.0) |
| mam | 13.85 / 13.87 | 1.0 / 1.0 | 112.59 / 113.23 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.87 (8.0 / 8.0) |
| ponder | 82.57 / 82.57 | 0.11 / 0.11 | 203.72 / 203.7 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.6 (75.0 / 75.0) |
| hologram | 10.0 / 10.0 | 0.0 / 0.0 | 103.0 / 103.0 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 10.0 / 10.0 (6.0 / 6.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.23 (base 20.23) | 13 (base 13) | 49040 (base 48604) | 360.96 (base 421.54) | 3.73 (base 5.48) |
| factory_flat | 19.67 (reported, not gated) | 13 (base 13) | 48916 (base 48340) | 359.37 (base 422.36) | 5.48 (base 5.48) |
| sift | 41.0 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 379.05 (base 441.24) | 3.73 (base 4.24) |
| hub | 25.53 (base 25.54) | 14 (base 17) | 64948 (base 66484) | 391.58 (base 451.46) | 3.73 (base 5.99) |
| mam | 24.52 (base 24.53) | 17 (base 20) | 21108 (base 22916) | 404.75 (base 472.87) | 7.73 (base 5.99) |
| ponder | 87.24 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 403.44 (base 463.76) | 7.73 (base 8.24) |
| hologram | 26 median (avg 25.77) (reported, not gated) | 20 (base 23) | 68192 (base 70624) | 392.37 (base 452.39) | 7.6 (base 6.31) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 393.38 (base 470.06) | 7.6 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.23 / 20 (29.57) | 180.51 / 226 (29.55) | median and average, x 1.10 + 2 |
| factory_flat | 19.67 / 20 (29.93) | 179.16 / 226 (29.96) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (15.74) | 579.0 / 579 (14.8) | median, x 1.10 + 2 |
| hub | 25.53 / 25 (29.78) | 162.02 / 208 (29.93) | median and average, x 1.10 + 2 |
| mam | 24.52 / 24 (29.87) | 200.97 / 246 (29.55) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (29.94) | 180.74 / 227 (29.96) | median and average, x 1.10 + 2 |
| hologram | 25.77 / 26 (25.63) | 160.65 / 176 (22.06) | median, at most vanilla |
| create_ct | 6.67 / 7 (30.0) | 111.16 / 158 (29.94) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.633 | 12.0 / 1.633 | 0.0 / 0.0 | 416.0 / 2.758 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.213 | 32.0 / 20.213 | 0.0 / 0.0 | 416.0 / 33.535 |
| ponder | 32.0 / 17.514 | 32.0 / 17.514 | 0.0 / 0.0 | 416.0 / 29.014 |
| hologram | 32.0 / 0.238 | 32.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 32.0 / 0.171 | 32.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26337.306 (48128.934) | 45.278 | 1.0 | 156752 / 233360 | 100928 (2097152.0) | 10 / 7 | 0.0 / 0 |
| factory_flat | 26337.896 (48130.217) | 45.278 | 1.0 | 48916 / 50020 | 0 (2097115.235) | 1 / 0 | 0.0 / 0 |
| sift | 145925.255 (205040.924) | 28.831 | 1.434 | 4148 / 8052 | 0 (2097152.0) | 314 / 296 | 0.0 / 0 |
| hub | 33472.161 (58844.733) | 43.118 | 1.02 | 83908 / 84516 | 0 (2097152.0) | 5 / 0 | 0.0 / 0 |
| mam | 66543.09 (116620.692) | 42.941 | 1.011 | 40068 / 40948 | 0 (2097152.0) | 2 / 3 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61188 / 61204 | 0 (2097152.0) | 2 / 1 | 0.0 / 0 |
| hologram | 3911.625 (6873.398) | 43.09 | 1.0 | 70768 / 71020 | 0 (2097152.0) | 20 / 43 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1124 / 1316 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.014 | 74.125 | 16 (vanilla 28) | 96100 | 23.521 / 0.303 | 1.511 / 28.0 | 2.566 | 2080 / 0 | 4.701 (6.542) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 103664 | 1.114 / 0.004 | 1.633 / 12.0 | 2.768 | 174 / 0 | None (None) |
| sift | plain, one multi-draw per region and layer | 21.067 | 430.366 | 16 (vanilla 28) | 462608 | 21.946 / 0.329 | 7.389 / 16.0 | 12.353 | 3088 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.084 | 16 (vanilla 28) | 117492 | 21.414 / 0.32 | 1.879 / 12.0 | 3.137 | 3167 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.145 | 16 (vanilla 28) | 1187852 | 22.35 / 0.347 | 20.213 / 32.0 | 31.719 | 3811 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1016948 | 22.349 / 0.347 | 17.514 / 32.0 | 27.155 | 3812 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.006 | 13.025 | 16 (vanilla 28) | 13744 | 22.217 / 0.342 | 0.238 / 32.0 | 0.367 | 3870 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 21.908 / 0.338 | 0.171 / 32.0 | 0.283 | 3958 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1890 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 596 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2052 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.011; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.281 / 0.0 | 0.555 / 0.557 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.58 / 10.777 | 0.018 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.441 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| hologram | 124.762 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 147.115 / 90.854 | 0.143 / 0.052 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.86 / 13.86 | 0.0 / 0.0 | 574.12 / 574.07 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.86 / 13.86 (8.0 / 8.0) |
| mam | 13.85 / 13.86 | 1.0 / 1.0 | 112.66 / 113.1 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.86 (8.0 / 8.0) |
| ponder | 82.57 / 82.58 | 0.11 / 0.11 | 203.53 / 203.88 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.61 (75.0 / 75.0) |
| hologram | 10.0 / 10.0 | 0.0 / 0.0 | 103.0 / 103.0 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 10.0 / 10.0 (6.0 / 6.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.2 (base 14.22) | 15 (base 15) | 51616 (base 51148) | 359.51 (base 420.23) | 3.71 (base 3.71) |
| factory_flat | 13.67 (reported, not gated) | 15 (base 15) | 51500 (base 50904) | 359.43 (base 421.17) | 3.46 (base 3.46) |
| sift | 21.42 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 377.39 (base 439.9) | 3.71 (base 4.23) |
| hub | 21.53 (base 21.55) | 16 (base 18) | 66576 (base 67484) | 390.18 (base 450.49) | 3.71 (base 4.23) |
| mam | 22.54 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 403.36 (base 462.56) | 7.71 (base 5.46) |
| ponder | 87.24 (base 87.24) | 14 (base 14) | 52268 (base 52248) | 402.15 (base 462.87) | 7.71 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 394.91 (base 475.26) | 7.52 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.2 / 14 (29.82) | 110.0 / 154 (28.84) | median and average, x 1.10 + 2 |
| factory_flat | 13.67 / 14 (29.99) | 107.79 / 154 (29.65) | median and average, x 1.10 + 2 |
| sift | 21.42 / 23 (23.32) | 153.0 / 153 (13.27) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (29.97) | 116.29 / 161 (29.51) | median and average, x 1.10 + 2 |
| mam | 22.54 / 22 (29.9) | 125.22 / 162 (27.08) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (29.96) | 180.89 / 227 (29.81) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 100.31 / 147 (29.88) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.281 | 32.0 / 20.281 | 0.0 / 0.0 | 416.0 / 33.929 |
| ponder | 32.0 / 17.584 | 32.0 / 17.584 | 0.0 / 0.0 | 416.0 / 29.104 |
| create_ct | 32.0 / 0.171 | 32.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26336.909 (48128.205) | 45.278 | 1.0 | 159344 / 234840 | 100928 (2097152.0) | 5 / 11 | 0.0 / 0 |
| factory_flat | 26337.823 (48130.083) | 45.278 | 1.0 | 51500 / 51500 | 0 (2097152.0) | 0 / 4 | 0.0 / 0 |
| sift | 147586.03 (204828.605) | 27.947 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 14 / 265 | 0.0 / 0 |
| hub | 33471.693 (58843.91) | 43.118 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 1 / 2 | 0.0 / 0 |
| mam | 66220.737 (115929.863) | 42.879 | 1.011 | 42708 / 42688 | 0 (2097152.0) | 5 / 28 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61268 / 61224 | 0 (2097152.0) | 1 / 3 | 0.0 / 0 |
| create_ct | 2538.216 (5312.825) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 96100 | 25.143 / 0.338 | 1.511 / 28.0 | 2.566 | 1741 / 0 | 2.205 (2.299) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.123 | 16 (vanilla 28) | 96100 | 1.318 / 0.004 | 1.511 / 12.0 | 2.566 | 166 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.006 | 429.921 | 16 (vanilla 28) | 462608 | 23.096 / 0.359 | 7.389 / 16.0 | 12.353 | 2748 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.083 | 16 (vanilla 28) | 117492 | 22.435 / 0.347 | 1.879 / 12.0 | 3.137 | 2829 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.146 | 16 (vanilla 28) | 1191316 | 23.266 / 0.372 | 20.281 / 32.0 | 31.812 | 3482 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1020540 | 23.265 / 0.372 | 17.584 / 32.0 | 27.251 | 3483 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 22.822 / 0.365 | 0.171 / 32.0 | 0.283 | 3591 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2802 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 602 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 95.764 / 0.0 | 0.552 / 0.534 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 9.835 / 9.693 | 0.015 / 0.006 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 94.122 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.006 / 0.002 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 8.562 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.755 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 102.615 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.647 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| create_ct | 93.645 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.86 | 0.0 / 0.0 | 574.44 / 573.86 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.86 (8.0 / 8.0) |
| mam | 13.87 / 13.88 | 1.0 / 1.0 | 113.34 / 113.52 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.87 / 13.88 (8.0 / 8.0) |
| ponder | 82.58 / 82.58 | 0.11 / 0.11 | 203.64 / 203.58 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.6 (75.0 / 75.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

## Bench-only jar (quicksilver-bench)

| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |
|---|---|---|---|---|---|---|---|
| bench-plain | spawn | 14.58 | 87.15 | 291 | 123 | 589428 | [] |
| bench-plain | flythrough | 10.74 | 204.21 | 214 | 123 | 7466708 | [] |
| bench-plain | water_glass | 25.69 | 46.12 | 3 | 124 | 16920 | [] |
| bench-plain | nether | 25.18 | 49.14 | 5 | 121 | 472080 | [] |
| bench-sodium | spawn | 10.9 | 101.4 | 217 | 147 | 2096 | [] |
| bench-sodium | flythrough | 7.07 | 338.9 | 142 | 158 | 266456 | [] |
| bench-sodium | water_glass | 19.85 | 60.58 | 205 | 135 | 8556 | [] |
| bench-sodium | nether | 27.1 | 43.71 | 0 | 129 | 263664 | [] |

## Direct buffers per scene (reported; open bug, see docs/progress.md)

| run | scene | start MB | peak MB (tick) | end MB | rise |
|---|---|---|---|---|---|
| core-bench-plain | spawn | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-plain | water_glass | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-plain | nether | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-plain | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-sodium | spawn | 3.99 | 3.99 (0) | 3.99 |  |
| core-bench-sodium | water_glass | 3.99 | 3.99 (0) | 3.99 |  |
| core-bench-sodium | nether | 3.87 | 3.87 (0) | 3.87 |  |
| core-bench-sodium | flythrough | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | spawn | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | water_glass | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | nether | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | mobs | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | outline | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | occlusion | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-full | occlusion_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-full | translucent_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-full | particles | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-full | animation | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-full | animated_items | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-full | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | renderer_off | 3.35 | 3.35 (0) | 3.35 |  |
| core-measure-full | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-full | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | spawn | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | water_glass | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | nether | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | mobs | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | outline | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | occlusion | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | occlusion_move | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | translucent_move | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | particles | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | animation | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | animated_items | 4.24 | 4.24 (0) | 4.24 |  |
| core-measure-old | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-old | water_glass_oit | 3.35 | 3.35 (0) | 3.35 |  |
| core-measure-old | translucent_move_oit | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-vk | spawn | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | water_glass | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | nether | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | mobs | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | outline | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | occlusion | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | occlusion_move | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | translucent_move | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | particles | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | animation | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | animated_items | 4.23 | 4.23 (0) | 4.23 |  |
| core-measure-vk | flat_plants | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | renderer_off | 3.59 | 3.59 (0) | 3.59 |  |
| core-measure-vk | water_glass_oit | 3.59 | 3.59 (0) | 3.59 |  |
| core-measure-vk | translucent_move_oit | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | flythrough | 3.46 | 3.46 (0) | 3.46 |  |
| core-quicksilver-full | spawn | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | water_glass | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | nether | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | mobs | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | outline | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | occlusion | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | occlusion_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | translucent_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | particles | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | animation | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | animated_items | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-full | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-full | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | spawn | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | water_glass | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | nether | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | mobs | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | outline | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | occlusion | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | occlusion_move | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | translucent_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | particles | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | animation | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | animated_items | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-vk | spawn | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | water_glass | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | nether | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | mobs | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | outline | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | occlusion | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | occlusion_move | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | translucent_move | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | particles | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | animation | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | animated_items | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | flat_plants | 3.46 | 3.46 (0) | 3.46 |  |
| core-quicksilver-vk | renderer_off | 3.59 | 3.59 (0) | 3.59 |  |
| core-quicksilver-vk | water_glass_oit | 3.59 | 3.59 (0) | 3.59 |  |
| core-quicksilver-vk | translucent_move_oit | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | flythrough | 5.46 | 5.46 (0) | 5.46 |  |
| siftec-measure-full | factory | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-full | sift | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-full | hub | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-full | mam | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-full | ponder | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-full | hologram | 5.35 | 5.35 (0) | 5.35 |  |
| siftec-measure-full | create_ct | 5.35 | 5.35 (0) | 5.35 |  |
| siftec-measure-full | factory_flat | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-measure-old | factory | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-old | sift | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-old | hub | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-old | mam | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-old | ponder | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-measure-old | hologram | 5.35 | 5.35 (0) | 5.35 |  |
| siftec-measure-old | create_ct | 5.35 | 5.35 (0) | 5.35 |  |
| siftec-measure-old | factory_flat | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-measure-vk | factory | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-measure-vk | sift | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-measure-vk | hub | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-measure-vk | mam | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-measure-vk | ponder | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-measure-vk | create_ct | 7.52 | 7.52 (0) | 7.52 |  |
| siftec-measure-vk | factory_flat | 3.46 | 3.46 (0) | 3.46 |  |
| siftec-quicksilver-full | factory | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-quicksilver-full | sift | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-quicksilver-full | hub | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-quicksilver-full | mam | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-quicksilver-full | ponder | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-quicksilver-full | hologram | 5.35 | 5.35 (0) | 5.35 |  |
| siftec-quicksilver-full | create_ct | 5.35 | 5.35 (0) | 5.35 |  |
| siftec-quicksilver-full | factory_flat | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-quicksilver-old | factory | 3.73 | 3.73 (0) | 3.73 |  |
| siftec-quicksilver-old | sift | 3.73 | 3.73 (0) | 3.73 |  |
| siftec-quicksilver-old | hub | 3.73 | 3.73 (0) | 3.73 |  |
| siftec-quicksilver-old | mam | 7.73 | 7.73 (0) | 7.73 | between scenes +4.00 |
| siftec-quicksilver-old | ponder | 7.73 | 7.73 (0) | 7.73 |  |
| siftec-quicksilver-old | hologram | 7.6 | 7.6 (0) | 7.6 |  |
| siftec-quicksilver-old | create_ct | 7.6 | 7.6 (0) | 7.6 |  |
| siftec-quicksilver-old | factory_flat | 5.48 | 5.48 (0) | 5.48 |  |
| siftec-quicksilver-vk | factory | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-quicksilver-vk | sift | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-quicksilver-vk | hub | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-quicksilver-vk | mam | 7.71 | 7.71 (0) | 7.71 | between scenes +4.00 |
| siftec-quicksilver-vk | ponder | 7.71 | 7.71 (0) | 7.71 |  |
| siftec-quicksilver-vk | create_ct | 7.52 | 7.52 (0) | 7.52 |  |
| siftec-quicksilver-vk | factory_flat | 3.46 | 3.46 (0) | 3.46 |  |

Rises of 4 MB or more: siftec-quicksilver-old/mam: between scenes +4.00; siftec-quicksilver-vk/mam: between scenes +4.00

