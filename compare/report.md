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
| spawn | 0.0494 | 10.05 | 0.2909 | 0.4309 | 0.0552 |
| flythrough | 0.0 | 14.95 | 0.001 | 0.001 | 0.001 |
| water_glass | 0.0033 | 6.45 | 0.0046 | 0.0042 | 0.0036 |
| nether | 0.0039 | 0.8 | 0.0084 | 0.004 | 0.0003 |
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
| translucent_move | 0.0001 | 0.3 | 0.0001 | 0.0003 | 0.0 |
| translucent_move.x2404 | 0.0 | 0.3 | 0.0002 | 0.0003 | 0.0003 |
| translucent_move.x2414 | 0.0001 | 0.3 | 0.0002 | 0.0003 | 0.0001 |
| translucent_move.x2424 | 0.0002 | 0.3 | 0.0005 | 0.0004 | 0.0002 |
| translucent_move.low | 0.0007 | 0.3 | 0.0011 | 0.0012 | 0.0015 |
| translucent_move.back | 0.0 | 0.3 | 0.0 | 0.0 | 0.0001 |
| translucent_move.bare_back | 0.0001 | 0.3 | 0.0 | 0.0 | 0.0001 |
| water_glass_oit | 0.0033 | 0.3 | 0.0039 | 0.0039 | 0.0036 |
| translucent_move_oit | 0.0003 | 0.3 | 0.0001 | 0.0001 | 0.0001 |
| translucent_move_oit.x2404 | 0.0003 | 0.3 | 0.0008 | 0.0003 | 0.0004 |
| translucent_move_oit.x2414 | 0.0 | 0.3 | 0.0002 | 0.0001 | 0.0003 |
| translucent_move_oit.x2424 | 0.0002 | 0.3 | 0.0003 | 0.0004 | 0.0003 |
| translucent_move_oit.low | 0.0008 | 0.3 | 0.0012 | 0.0008 | 0.0011 |
| translucent_move_oit.back | 0.0001 | 0.3 | 0.0002 | 0.0 | 0.0002 |
| translucent_move_oit.bare_back | 0.0001 | 0.3 | 0.0001 | 0.0 | 0.0002 |
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
| spawn | 92.85 | 18057 | 306 (816) | 1545 (2054) | 340 (877) |
| flythrough | 97.27 | 6020 | 50 (303) | 50 (303) | 52 (301) |
| water_glass | 95.81 | 25508 | 20 (2619) | 18 (2617) | 15 (2728) |
| nether | 99.41 | 905 | 73 (527) | 3 (457) | 3 (469) |
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
| translucent_move | 96.93 | 13 | 2 (77) | 2 (77) | 1 (67) |
| translucent_move.x2404 | 95.93 | 15 | 4 (105) | 3 (103) | 4 (88) |
| translucent_move.x2414 | 95.86 | 13 | 3 (106) | 2 (105) | 1 (99) |
| translucent_move.x2424 | 96.74 | 13 | 3 (80) | 3 (80) | 2 (68) |
| translucent_move.low | 96.11 | 41 | 14 (19) | 15 (20) | 14 (17) |
| translucent_move.back | 96.93 | 13 | 2 (77) | 2 (77) | 3 (69) |
| translucent_move.bare_back | 96.9 | 13 | 1 (76) | 1 (77) | 1 (67) |
| water_glass_oit | 95.81 | 57 | 18 (2618) | 14 (2615) | 18 (2732) |
| translucent_move_oit | 96.93 | 12 | 2 (77) | 3 (78) | 1 (67) |
| translucent_move_oit.x2404 | 95.93 | 16 | 4 (104) | 5 (105) | 5 (90) |
| translucent_move_oit.x2414 | 95.86 | 10 | 5 (109) | 1 (104) | 3 (100) |
| translucent_move_oit.x2424 | 96.74 | 15 | 2 (79) | 3 (80) | 2 (68) |
| translucent_move_oit.low | 96.11 | 40 | 14 (19) | 13 (18) | 15 (17) |
| translucent_move_oit.back | 96.93 | 12 | 3 (78) | 1 (77) | 5 (71) |
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
| spawn | 0.0564 / 306 | 0.4374 / 1577 | 0.2921 / 324 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| water_glass | 0.0031 / 9 | 0.0027 / 10 | 0.0039 / 22 |
| nether | 0.0 / 0 | 0.0037 / 0 | 0.0 / 0 |
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
| translucent_move | 0.0001 / 0 | 0.0002 / 1 | 0.0002 / 4 |
| translucent_move.x2404 | 0.0 / 0 | 0.0002 / 1 | 0.0 / 0 |
| translucent_move.x2414 | 0.0001 / 2 | 0.0002 / 2 | 0.0 / 1 |
| translucent_move.x2424 | 0.0003 / 1 | 0.0002 / 0 | 0.0002 / 3 |
| translucent_move.low | 0.0002 / 3 | 0.0008 / 6 | 0.0004 / 3 |
| translucent_move.back | 0.0001 / 2 | 0.0001 / 1 | 0.0 / 0 |
| translucent_move.bare_back | 0.0001 / 1 | 0.0 / 1 | 0.0002 / 2 |
| water_glass_oit | 0.0034 / 13 | 0.0039 / 18 | 0.0027 / 14 |
| translucent_move_oit | 0.0 / 0 | 0.0001 / 0 | 0.0 / 0 |
| translucent_move_oit.x2404 | 0.0003 / 0 | 0.0 / 4 | 0.0002 / 1 |
| translucent_move_oit.x2414 | 0.0001 / 1 | 0.0001 / 1 | 0.0001 / 1 |
| translucent_move_oit.x2424 | 0.0001 / 0 | 0.0001 / 0 | 0.0001 / 2 |
| translucent_move_oit.low | 0.001 / 7 | 0.0001 / 3 | 0.0003 / 7 |
| translucent_move_oit.back | 0.0001 / 2 | 0.0 / 0 | 0.0002 / 1 |
| translucent_move_oit.bare_back | 0.0001 / 2 | 0.0001 / 1 | 0.0001 / 0 |
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
| spawn | 13.23 (base 13.6) | 14 (base 14) | 25064 (base 22476) | 261.15 (base 259.07) | 3.48 (base 3.99) |
| flythrough | 13.06 (reported, not gated) | 40 (reported, not gated) | 819760 (reported, not gated) | 314.78 (base 324.25) | 3.48 (base 4.24) |
| water_glass | 9.06 (base 8.97) | 15 (base 15) | 15880 (base 15576) | 245.56 (base 250.02) | 3.48 (base 5.99) |
| nether | 15.84 (base 15.63) | 12 (base 12) | 21124 (base 19648) | 266.26 (base 273.53) | 3.48 (base 5.99) |
| mobs | 19.86 (base 19.77) | 17 (base 17) | 616288 (base 1010948) | 265.0 (base 268.37) | 3.48 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6356 (base 6356) | 264.61 (base 264.69) | 3.48 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100688 (base 126320) | 270.58 (base 265.1) | 3.48 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82580 (reported, not gated) | 271.07 (base 265.74) | 3.48 (base 4.24) |
| translucent_move | 9.0 (reported, not gated) | 15 (reported, not gated) | 11132 (reported, not gated) | 272.92 (base 267.48) | 5.48 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15432 (base 14688) | 244.58 (base 245.02) | 3.6 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 11020 (reported, not gated) | 247.3 (base 247.19) | 3.73 (base 3.73) |
| flat_plants | 19.0 (base 18.29) | 15 (base 15) | 10224 (base 9812) | 247.42 (base 246.92) | 3.48 (base 3.99) |
| renderer_off | 7.69 (base 7.67) | 14 (base 14) | 7332 (base 7164) | 239.84 (base 241.33) | 3.6 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 267.93 (base 261.44) | 5.48 (base 5.99) |
| animation | 49 median (avg 39.98) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.24 (base 260.68) | 5.48 (base 5.99) |
| animated_items | 60 median (avg 46.76) (base 60) | 24 (base 23) | 22136 (base 21940) | 240.3 (base 239.81) | 5.48 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.23 / 13 (9.1) | 123.29 / 123 (11.25) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (6.23) | 123.06 / 123 (7.86) | median and average, at most vanilla + 2 |
| water_glass | 9.06 / 9 (19.79) | 122.84 / 124 (20.1) | median, at most vanilla |
| nether | 15.84 / 16 (16.61) | 120.85 / 121 (19.45) | median, x 1.10 + 2 |
| mobs | 19.86 / 20 (23.3) | 110.06 / 134 (25.17) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.27 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (29.99) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.86) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| translucent_move | 9.0 / 9 (17.61) | 120.3 / 124 (20.41) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (9.56) | 129.0 / 129 (11.07) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (9.05) | 129.0 / 129 (10.54) | median and average, at most vanilla |
| flat_plants | 19.0 / 19 (18.14) | 120.89 / 124 (20.45) | median, x 1.10 + 2 |
| renderer_off | 7.69 / 8 (28.92) | 84.46 / 123 (29.93) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.33 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.98 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.76 / 60 (30.0) | 155.22 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.83 | 36.0 / 22.83 | 0.0 / 0.0 | 416.0 / 39.595 |
| flythrough | 112.0 / 83.002 | 112.0 / 83.002 | 0.0 / 0.0 | 416.0 / 146.375 |
| water_glass | 36.0 / 12.432 | 36.0 / 12.432 | 0.0 / 0.0 | 416.0 / 21.677 |
| nether | 20.0 / 15.307 | 20.0 / 15.307 | 0.0 / 0.0 | 256.0 / 26.783 |
| mobs | 16.0 / 9.627 | 16.0 / 9.627 | 0.0 / 0.0 | 256.0 / 16.884 |
| outline | 16.0 / 1.346 | 16.0 / 1.346 | 0.0 / 0.0 | 256.0 / 2.362 |
| occlusion | 20.0 / 2.754 | 20.0 / 2.754 | 0.0 / 0.0 | 416.0 / 4.286 |
| occlusion_move | 20.0 / 3.496 | 20.0 / 3.496 | 0.0 / 0.0 | 416.0 / 5.758 |
| translucent_move | 28.0 / 13.16 | 28.0 / 13.16 | 0.0 / 0.0 | 416.0 / 22.569 |
| water_glass_oit | 20.0 / 12.432 | 20.0 / 12.432 | 0.0 / 0.0 | 416.0 / 21.677 |
| translucent_move_oit | 28.0 / 12.804 | 28.0 / 12.804 | 0.0 / 0.0 | 416.0 / 21.95 |
| flat_plants | 24.0 / 13.826 | 24.0 / 13.826 | 0.0 / 0.0 | 416.0 / 23.792 |
| renderer_off | 24.0 / 8.545 | 24.0 / 8.545 | 0.0 / 0.0 | 416.0 / 15.057 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 514801.812 (693469.503) | 25.764 | 1.158 | 738032 / 744040 | 712968 (2097152.0) | 181 / 224 | 0.0 / 0 |
| flythrough | 546833.855 (874707.742) | 37.484 | 1.082 | 2338236 / 6777668 | 2096304 (2097152.0) | 124 / 157 | 6.752 / 50 |
| water_glass | 216205.975 (386997.266) | 44.132 | 1.117 | 339768 / 336344 | 323776 (2097152.0) | 179 / 99 | 0.0 / 0 |
| nether | 321149.307 (481211.976) | 33.262 | 1.273 | 300856 / 215348 | 280640 (2097152.0) | 332 / 373 | 0.0 / 0 |
| mobs | 166809.961 (301304.576) | 44.637 | 0.99 | 616288 / 937768 | 0 (2097152.0) | 2 / 5 | 0.0 / 0 |
| outline | 25159.933 (42344.574) | 40.583 | 1.0 | 6356 / 6356 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 48931.553 (86636.394) | 43.521 | 1.192 | 100688 / 425700 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 53197.25 (94136.332) | 43.489 | 1.217 | 644872 / 1191056 | 567936 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 205370.455 (364892.801) | 43.718 | 1.173 | 766164 / 1332244 | 756096 (2097152.0) | 343 / 121 | 0.0 / 0 |
| water_glass_oit | 216795.053 (388051.684) | 44.132 | 1.117 | 154408 / 17928 | 140096 (2097152.0) | 190 / 221 | 0.0 / 0 |
| translucent_move_oit | 205187.911 (364158.311) | 43.654 | 1.171 | 736740 / 1279808 | 725632 (2097152.0) | 180 / 211 | 0.0 / 0 |
| flat_plants | 236272.89 (433634.586) | 45.513 | 1.056 | 10448 / 271580 | 0 (2097152.0) | 362 / 43 | 0.0 / 0 |
| renderer_off | 148915.194 (269607.644) | 44.766 | 1.01 | 7332 / 7292 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86832 / 150176 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.78 (4890.137) | 53.544 | 1.0 | 79864 / 123160 | 57728 (2097152.0) | 1 / 0 | 0.0 / 0 |

Terrain module, core full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.017 | 789.337 | 16 (vanilla 28) | 1379556 | 40.491 / 0.09 | 22.83 / 36.0 | 36.838 | 2442 / 0 | 4.712 (9.247) |
| flythrough | indirect, one multi-draw per arena and layer | 3.024 | 894.452 | 16 (vanilla 28) | 5021588 | 15.609 / 0.999 | 83.002 / 112.0 | 134.091 | 6318 / 0 | 7.839 (19.954) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 273.691 | 16 (vanilla 28) | 772040 | 32.409 / 0.077 | 12.432 / 36.0 | 20.616 | 3127 / 0 | 6.744 (8.434) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 470.413 | 16 (vanilla 28) | 959616 | 27.366 / 0.165 | 15.307 / 20.0 | 25.625 | 4161 / 0 | 3.059 (5.362) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.424 | 16 (vanilla 28) | 601316 | 24.886 / 0.144 | 9.627 / 16.0 | 16.057 | 4612 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 44.073 | 16 (vanilla 28) | 84548 | 24.47 / 0.143 | 1.346 / 16.0 | 2.258 | 4710 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 73.122 | 16 (vanilla 28) | 172984 | 23.192 / 0.136 | 2.754 / 20.0 | 4.619 | 4967 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 78.444 | 16 (vanilla 28) | 220152 | 21.885 / 0.128 | 3.496 / 20.0 | 5.879 | 5271 / 0 | 6.314 (12.727) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.009 | 250.983 | 16 (vanilla 28) | 814936 | 18.992 / 0.116 | 13.16 / 28.0 | 21.761 | 6064 / 0 | 9.645 (20.162) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.026 | 282.479 | 16 (vanilla 28) | 772040 | 7.63 / 0.043 | 12.432 / 20.0 | 20.616 | 1093 / 0 | 5.265 (4.58) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.028 | 265.533 | 16 (vanilla 28) | 792404 | 6.638 / 0.1 | 12.804 / 28.0 | 21.159 | 2428 / 0 | 9.331 (18.641) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.008 | 287.793 | 16 (vanilla 28) | 864880 | 6.131 / 0.01 | 13.826 / 24.0 | 23.095 | 939 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 206.356 | 16 (vanilla 28) | 538284 | 2.45 / 0.021 | 8.545 / 24.0 | 14.374 | 599 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 18.899 / 0.115 | 0.131 / 28.0 | 0.225 | 6120 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 18.745 / 0.114 | 0.302 / 28.0 | 0.518 | 6222 / 0 | 2.128 (4.767) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.692 / 0.115 | 0.153 / 28.0 | 0.261 | 6272 / 0 | 1.08 (1.547) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 153.0 | 1.0 | 0.0 | 0 | 0 | 42 (0.034 / 0.064 / 0.097; 0) | 0 | 27846 / 0 / 0 |
| flythrough | 185.328 | 1.0 | 0.0 | 0 | 0 | 836 (0.036 / 0.05 / 8.184; 0) | 42 | 23166 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1584 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.008 / 0.012 / 0.016; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.119 / 0.193 / 0.236; 0) | 180 | 2471 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 573 | 0 (None / None / None; 0) | 0 | 764 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 543 | 0 (None / None / None; 0) | 0 | 1267 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1452 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2316 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 344.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.04 (0.441; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 433.512 | 1.0 (0.0, 0.0) | 1.28 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.021 (0.057; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.143 (0.271; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 50.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.079 (0.23; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 52.351 | 10.0 (9.0, 2.361) | 6.0 (6.0, 2.278) | 0.065 (0.322; 0) | 0 | 2774 / 1501670 / 0 / 0 |
| translucent_move | 136.351 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.768 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 153.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.011 (0.065; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.286 / 0.232 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 101.468 / 0.231 | 0.029 / 0.008 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.168 / 0.0 | 0.004 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.816 |
| water_glass | 113.853 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.515 / 54.556 | 0.021 / 0.027 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.853 / 0.843 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 38.995 / 8.946 | 0.087 / 0.015 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 91.27 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 111.336 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.214 / 12.246 | 0.006 / 0.017 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 51.493 / 51.267 | 0.016 / 0.027 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.943 / 11.807 | 0.007 / 0.012 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 112.181 / 10.0 | 0.733 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.81 / 2.565 | 0.019 / 0.014 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.008 / 0.064 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.311 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.006 / 0.016 | 56 (3) | 40.608 / 9.248 / 31.36 | 3 / 0 | 10.987 |
| animated_items | 136.553 / 28.09 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.564 / 7.98 / 32.584 | 0 / 0 | 9.0 |

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
| spawn | 49.28 (base 49.0) | 12 (base 13) | 1380 (base 6372) | 261.5 (base 256.29) | 3.48 (base 3.73) |
| flythrough | 66.46 (reported, not gated) | 47 (reported, not gated) | 797396 (reported, not gated) | 315.09 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 19.99 (base 20.0) | 13 (base 14) | 7204 (base 10264) | 245.15 (base 249.83) | 3.48 (base 5.99) |
| nether | 33.92 (base 33.79) | 10 (base 11) | 5072 (base 10656) | 265.7 (base 272.57) | 3.48 (base 5.99) |
| mobs | 28.9 (base 28.97) | 15 (base 16) | 610676 (base 1015892) | 264.65 (base 268.23) | 3.48 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 264.17 (base 264.41) | 3.48 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 269.92 (base 264.64) | 5.48 (base 4.24) |
| occlusion_move | 24.04 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 270.54 (base 265.66) | 5.48 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 2724 (reported, not gated) | 272.34 (base 268.19) | 5.48 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 6868 (base 7604) | 244.57 (base 245.78) | 7.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 247.42 (base 248.15) | 7.73 (base 3.73) |
| flat_plants | 32.83 (base 33.0) | 13 (base 13) | 1492 (base 1716) | 247.05 (base 248.11) | 3.48 (base 5.99) |
| renderer_off | 17.67 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 239.6 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 267.74 (base 261.4) | 5.48 (base 4.24) |
| animation | 49 median (avg 39.95) (base 49) | 13 (base 13) | 1716 (base 1716) | 265.14 (base 260.68) | 5.48 (base 4.24) |
| animated_items | 60 median (avg 46.77) (base 60) | 22 (base 22) | 21668 (base 21668) | 240.12 (base 239.53) | 5.48 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.28 / 49 (11.08) | 875.0 / 875 (8.96) | median and average, x 1.10 + 2 |
| flythrough | 66.46 / 64 (6.41) | 993.86 / 997 (5.72) | median and average, at most vanilla |
| water_glass | 19.99 / 20 (19.99) | 390.0 / 390 (16.76) | median, at most vanilla |
| nether | 33.92 / 34 (18.29) | 587.82 / 588 (16.56) | median, x 1.10 + 2 |
| mobs | 28.9 / 29 (22.04) | 320.39 / 329 (21.57) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 129.27 / 168 (30.0) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (29.99) | 161.33 / 200 (29.98) | median and average, x 1.10 + 2 |
| occlusion_move | 24.04 / 24 (29.82) | 166.6 / 201 (29.99) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (18.53) | 367.85 / 367 (17.46) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (9.82) | 401.0 / 401 (9.41) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (8.93) | 384.73 / 384 (8.72) | median and average, at most vanilla |
| flat_plants | 32.83 / 33 (18.88) | 398.97 / 399 (18.05) | median, x 1.10 + 2 |
| renderer_off | 17.67 / 18 (29.7) | 298.84 / 326 (26.08) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.27 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 156.46 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.0) | 167.22 / 202 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 23.146 | 36.0 / 23.146 | 0.0 / 0.0 | 416.0 / 38.085 |
| flythrough | 112.0 / 83.137 | 112.0 / 83.137 | 0.0 / 0.0 | 416.0 / 142.87 |
| water_glass | 36.0 / 12.432 | 36.0 / 12.432 | 0.0 / 0.0 | 416.0 / 21.677 |
| nether | 20.0 / 15.303 | 20.0 / 15.303 | 0.0 / 0.0 | 256.0 / 26.783 |
| mobs | 16.0 / 9.627 | 16.0 / 9.627 | 0.0 / 0.0 | 256.0 / 16.884 |
| outline | 16.0 / 1.346 | 16.0 / 1.346 | 0.0 / 0.0 | 256.0 / 2.362 |
| occlusion | 20.0 / 2.594 | 20.0 / 2.594 | 0.0 / 0.0 | 416.0 / 4.497 |
| occlusion_move | 20.0 / 3.395 | 20.0 / 3.395 | 0.0 / 0.0 | 416.0 / 5.82 |
| translucent_move | 28.0 / 13.111 | 28.0 / 13.111 | 0.0 / 0.0 | 416.0 / 22.468 |
| water_glass_oit | 20.0 / 12.432 | 20.0 / 12.432 | 0.0 / 0.0 | 416.0 / 21.677 |
| translucent_move_oit | 28.0 / 12.755 | 28.0 / 12.755 | 0.0 / 0.0 | 416.0 / 21.849 |
| flat_plants | 24.0 / 13.719 | 24.0 / 13.719 | 0.0 / 0.0 | 416.0 / 23.609 |
| renderer_off | 24.0 / 8.545 | 24.0 / 8.545 | 0.0 / 0.0 | 416.0 / 15.057 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 457630.591 (701990.427) | 34.81 | 1.774 | 480228 / 606260 | 478848 (2097152.0) | 220 / 179 | 0.0 / 0 |
| flythrough | 546163.422 (873541.516) | 37.477 | 1.083 | 2106004 / 7242120 | 2096304 (2097152.0) | 128 / 114 | 10.256 / 70 |
| water_glass | 216200.501 (386987.469) | 44.132 | 1.117 | 148308 / 253908 | 140096 (2097152.0) | 158 / 335 | 0.0 / 0 |
| nether | 270923.912 (481044.011) | 43.68 | 1.701 | 219088 / 426944 | 212224 (2097152.0) | 365 / 331 | 0.0 / 0 |
| mobs | 166830.3 (301341.314) | 44.637 | 0.99 | 934452 / 612388 | 323776 (2097152.0) | 36 / 14 | 0.0 / 0 |
| outline | 25159.933 (42344.574) | 40.583 | 1.0 | 5012 / 5476 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 46329.215 (81451.753) | 43.121 | 1.2 | 98148 / 124924 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 50876.596 (89505.095) | 43.158 | 1.226 | 654780 / 1097484 | 579712 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 203585.195 (361833.422) | 43.735 | 1.175 | 758372 / 1323844 | 756096 (2097152.0) | 320 / 348 | 0.0 / 0 |
| water_glass_oit | 216760.306 (387989.49) | 44.132 | 1.117 | 8996 / 11316 | 0 (2097152.0) | 196 / 188 | 0.0 / 0 |
| translucent_move_oit | 203907.618 (362194.753) | 43.702 | 1.173 | 730772 / 1274916 | 725632 (2097152.0) | 178 / 174 | 0.0 / 0 |
| flat_plants | 234215.618 (429540.35) | 45.473 | 1.056 | 1716 / 306388 | 0 (2097152.0) | 377 / 360 | 0.0 / 0 |
| renderer_off | 148906.685 (269591.858) | 44.766 | 1.01 | 1380 / 3172 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 8772 / 8964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86232 / 149988 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79396 / 122740 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.177 | 805.645 | 16 (vanilla 28) | 1397940 | 41.035 / 0.106 | 23.146 / 36.0 | 37.329 | 2390 / 0 | 6.481 (12.895) |
| flythrough | plain, one multi-draw per region and layer | 56.789 | 892.492 | 16 (vanilla 28) | 5034312 | 15.551 / 1.008 | 83.137 / 112.0 | 134.431 | 6408 / 0 | 8.428 (20.425) |
| water_glass | plain, one multi-draw per region and layer | 14.035 | 273.684 | 16 (vanilla 28) | 772040 | 32.919 / 0.091 | 12.432 / 36.0 | 20.616 | 3039 / 0 | 5.234 (4.591) |
| nether | plain, one multi-draw per region and layer | 20.055 | 470.285 | 16 (vanilla 28) | 959520 | 27.722 / 0.179 | 15.303 / 20.0 | 25.622 | 4059 / 0 | 3.015 (4.19) |
| mobs | plain, one multi-draw per region and layer | 11.025 | 197.448 | 16 (vanilla 28) | 601316 | 25.057 / 0.155 | 9.627 / 16.0 | 16.057 | 4513 / 0 | 11.292 (11.633) |
| outline | plain, one multi-draw per region and layer | 11.018 | 44.073 | 16 (vanilla 28) | 84548 | 24.626 / 0.154 | 1.346 / 16.0 | 2.258 | 4611 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 70.117 | 16 (vanilla 28) | 162632 | 23.523 / 0.147 | 2.594 / 20.0 | 4.343 | 4842 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.993 | 75.439 | 16 (vanilla 28) | 213048 | 22.277 / 0.139 | 3.395 / 20.0 | 5.689 | 5121 / 0 | 7.928 (17.472) |
| translucent_move | plain, one multi-draw per region and layer | 12.032 | 248.484 | 16 (vanilla 28) | 810240 | 19.384 / 0.127 | 13.111 / 28.0 | 21.636 | 5883 / 0 | 8.012 (19.223) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.082 | 282.434 | 16 (vanilla 28) | 772040 | 7.515 / 0.039 | 12.432 / 20.0 | 20.616 | 1087 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.618 | 263.809 | 16 (vanilla 28) | 787700 | 6.553 / 0.104 | 12.755 / 28.0 | 21.034 | 2424 / 0 | 8.622 (23.551) |
| flat_plants | plain, one multi-draw per region and layer | 17.045 | 284.753 | 16 (vanilla 28) | 856808 | 6.305 / 0.01 | 13.719 / 24.0 | 22.879 | 941 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.022 | 206.347 | 16 (vanilla 28) | 538276 | 2.429 / 0.021 | 8.545 / 24.0 | 14.374 | 603 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 19.333 / 0.126 | 0.131 / 28.0 | 0.225 | 5927 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.169 / 0.125 | 0.302 / 28.0 | 0.518 | 6029 / 0 | 1.4 (1.948) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.111 / 0.125 | 0.153 / 28.0 | 0.261 | 6079 / 0 | 1.087 (1.555) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 156.0 | 17.0 | 0.0 | 0 | 0 | 50 (0.035 / 0.036 / 0.528; 0) | 0 | 34476 / 0 / 0 |
| flythrough | 184.729 | 25.86 | 0.0 | 0 | 0 | 848 (0.046 / 0.05 / 11.783; 0) | 43 | 23830 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1600 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.008 / 0.016 / 0.021; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 182 (0.102 / 0.158 / 0.184; 0) | 182 | 2597 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 591 | 0 (None / None / None; 0) | 0 | 788 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 537 | 0 (None / None / None; 0) | 0 | 1253 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1512 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2376 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 352.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.034 (0.084; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 433.736 | 1.0 (0.0, 0.0) | 1.248 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.042; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.143 (0.241; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.07 (0.171; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.351 | 10.0 (9.0, 2.363) | 6.0 (6.0, 2.278) | 0.065 (0.314; 0) | 0 | 2775 / 1502678 / 0 / 0 |
| translucent_move | 136.081 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.911 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.055; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.277 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 117.464 / 0.281 | 0.035 / 0.011 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.157 / 0.0 | 0.004 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.806 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.608 / 52.688 | 0.027 / 0.028 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.825 / 0.915 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.253 / 13.464 | 0.098 / 0.014 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 106.462 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.905 / 10.849 | 0.006 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 49.881 / 49.954 | 0.012 / 0.024 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.088 / 13.19 | 0.007 / 0.014 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.969 / 0.833 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.584 / 1.447 | 0.014 / 0.011 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 88.071 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.006 / 0.062 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.01 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.988 |
| animated_items | 136.553 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

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
| spawn | 13.06 (base 14.05) | 14 (base 15) | 25492 (base 27764) | 260.54 (base 257.2) | 3.46 (base 3.98) |
| flythrough | 13.06 (reported, not gated) | 34 (reported, not gated) | 415004 (reported, not gated) | 315.78 (base 318.06) | 3.71 (base 4.23) |
| water_glass | 8.88 (base 9.0) | 15 (base 15) | 15992 (base 15108) | 245.3 (base 244.39) | 3.46 (base 4.23) |
| nether | 15.12 (base 15.71) | 12 (base 12) | 21236 (base 19912) | 266.51 (base 268.95) | 3.46 (base 4.23) |
| mobs | 19.71 (base 19.97) | 17 (base 17) | 616288 (base 1008176) | 264.88 (base 264.31) | 5.46 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6356 (base 6356) | 264.13 (base 264.58) | 5.46 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100632 (base 126356) | 269.82 (base 264.78) | 5.46 (base 3.98) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82524 (reported, not gated) | 270.51 (base 265.16) | 5.46 (base 4.23) |
| translucent_move | 8.88 (reported, not gated) | 15 (reported, not gated) | 10700 (reported, not gated) | 272.45 (base 268.35) | 5.46 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 16216 (base 15024) | 244.35 (base 245.99) | 3.34 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10700 (reported, not gated) | 246.94 (base 248.38) | 3.46 (base 3.46) |
| flat_plants | 17.14 (base 18.96) | 15 (base 15) | 10036 (base 9864) | 246.98 (base 248.07) | 3.59 (base 5.98) |
| renderer_off | 7.67 (base 7.83) | 14 (base 14) | 7332 (base 7180) | 240.01 (base 241.13) | 3.59 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 267.42 (base 261.4) | 5.46 (base 5.98) |
| animation | 49 median (avg 39.98) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.02 (base 260.68) | 5.46 (base 5.98) |
| animated_items | 60 median (avg 46.74) (base 60) | 23 (base 23) | 21976 (base 21940) | 240.12 (base 239.74) | 5.46 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.06 / 13 (12.35) | 123.93 / 124 (9.52) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (8.55) | 123.07 / 123 (5.43) | median and average, at most vanilla + 2 |
| water_glass | 8.88 / 9 (22.71) | 124.0 / 124 (14.97) | median, at most vanilla |
| nether | 15.12 / 16 (21.38) | 120.97 / 121 (15.26) | median, x 1.10 + 2 |
| mobs | 19.71 / 20 (28.0) | 134.0 / 134 (19.02) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.27 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (29.99) | 94.46 / 133 (29.96) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.98) | 94.4 / 133 (29.95) | median and average, x 1.10 + 2 |
| translucent_move | 8.88 / 9 (22.58) | 124.0 / 124 (15.84) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (11.46) | 129.0 / 129 (8.52) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (10.42) | 129.0 / 129 (8.11) | median and average, at most vanilla |
| flat_plants | 17.14 / 19 (23.66) | 123.79 / 124 (16.06) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.94) | 106.54 / 123 (23.28) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.98 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.74 / 60 (30.0) | 155.09 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 23.077 | 36.0 / 23.077 | 0.0 / 0.0 | 416.0 / 37.313 |
| flythrough | 112.0 / 85.625 | 112.0 / 85.625 | 0.0 / 0.0 | 416.0 / 142.059 |
| water_glass | 36.0 / 12.432 | 36.0 / 12.432 | 0.0 / 0.0 | 416.0 / 21.677 |
| nether | 20.0 / 15.307 | 20.0 / 15.307 | 0.0 / 0.0 | 256.0 / 26.783 |
| mobs | 16.0 / 9.627 | 16.0 / 9.627 | 0.0 / 0.0 | 256.0 / 16.884 |
| outline | 16.0 / 1.346 | 16.0 / 1.346 | 0.0 / 0.0 | 256.0 / 2.362 |
| occlusion | 20.0 / 2.478 | 20.0 / 2.478 | 0.0 / 0.0 | 416.0 / 4.45 |
| occlusion_move | 20.0 / 3.32 | 20.0 / 3.32 | 0.0 / 0.0 | 416.0 / 5.868 |
| translucent_move | 28.0 / 12.992 | 28.0 / 12.992 | 0.0 / 0.0 | 416.0 / 22.667 |
| water_glass_oit | 20.0 / 12.432 | 20.0 / 12.432 | 0.0 / 0.0 | 416.0 / 21.677 |
| translucent_move_oit | 28.0 / 12.581 | 28.0 / 12.581 | 0.0 / 0.0 | 416.0 / 22.044 |
| flat_plants | 24.0 / 13.811 | 24.0 / 13.811 | 0.0 / 0.0 | 416.0 / 23.667 |
| renderer_off | 24.0 / 8.545 | 24.0 / 8.545 | 0.0 / 0.0 | 416.0 / 15.057 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 519082.016 (699620.943) | 25.805 | 1.155 | 524564 / 799020 | 499072 (2097152.0) | 246 / 190 | 0.0 / 0 |
| flythrough | 556698.865 (890649.006) | 37.495 | 1.082 | 2125228 / 7264548 | 2096384 (2097152.0) | 171 / 108 | 5.634 / 65 |
| water_glass | 216133.978 (386868.396) | 44.132 | 1.117 | 156984 / 17704 | 140096 (2097152.0) | 8 / 299 | 0.0 / 0 |
| nether | 320958.258 (480913.986) | 33.261 | 1.273 | 168260 / 402184 | 145792 (2097152.0) | 21 / 305 | 0.0 / 0 |
| mobs | 166749.767 (301195.85) | 44.637 | 0.99 | 616288 / 616328 | 0 (2097152.0) | 1 / 380 | 0.0 / 0 |
| outline | 25159.794 (42344.339) | 40.583 | 1.0 | 6356 / 6356 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 44309.727 (77458.883) | 42.796 | 1.214 | 263128 / 126344 | 162496 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 49061.8 (85929.897) | 42.905 | 1.239 | 674384 / 1107860 | 597504 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 201306.337 (357782.133) | 43.735 | 1.171 | 766180 / 1332600 | 756096 (2097152.0) | 19 / 316 | 0.0 / 0 |
| water_glass_oit | 216601.747 (387705.677) | 44.132 | 1.117 | 17336 / 16248 | 0 (2097152.0) | 229 / 170 | 0.0 / 0 |
| translucent_move_oit | 201426.692 (357751.317) | 43.696 | 1.17 | 736308 / 1280500 | 725632 (2097152.0) | 208 / 162 | 0.0 / 0 |
| flat_plants | 235490.816 (432075.552) | 45.498 | 1.056 | 10260 / 9904 | 0 (2097152.0) | 20 / 321 | 0.0 / 0 |
| renderer_off | 148902.17 (269581.302) | 44.765 | 1.01 | 7332 / 7292 | 0 (2097152.0) | 0 / 9 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86740 / 150176 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.774 (4890.123) | 53.544 | 1.0 | 79704 / 123160 | 57728 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, core vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.012 | 803.252 | 16 (vanilla 28) | 1394024 | 41.951 / 0.125 | 23.077 / 36.0 | 37.224 | 2411 / 0 | 2.561 (3.356) |
| flythrough | indirect, one multi-draw per arena and layer | 3.018 | 917.667 | 16 (vanilla 28) | 5182440 | 15.226 / 0.955 | 85.625 / 112.0 | 138.386 | 6636 / 0 | 5.31 (13.209) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 273.6 | 16 (vanilla 28) | 772040 | 34.224 / 0.109 | 12.432 / 36.0 | 20.616 | 3085 / 0 | 3.054 (2.635) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 470.098 | 16 (vanilla 28) | 959600 | 28.801 / 0.19 | 15.307 / 20.0 | 25.624 | 4121 / 0 | 1.074 (2.203) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.352 | 16 (vanilla 28) | 601316 | 26.158 / 0.166 | 9.627 / 16.0 | 16.057 | 4577 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 44.073 | 16 (vanilla 28) | 84548 | 25.711 / 0.165 | 1.346 / 16.0 | 2.258 | 4676 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 154660 | 24.357 / 0.156 | 2.478 / 20.0 | 4.13 | 4955 / 0 | 1.818 (1.734) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.482 | 16 (vanilla 28) | 207052 | 23.155 / 0.148 | 3.32 / 20.0 | 5.529 | 5228 / 0 | 4.082 (14.338) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.007 | 247.559 | 16 (vanilla 28) | 801912 | 20.045 / 0.134 | 12.992 / 28.0 | 21.413 | 6039 / 0 | 4.549 (8.595) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.022 | 282.227 | 16 (vanilla 28) | 772040 | 7.616 / 0.043 | 12.432 / 20.0 | 20.616 | 1091 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.024 | 262.38 | 16 (vanilla 28) | 776064 | 6.771 / 0.111 | 12.581 / 28.0 | 20.723 | 2422 / 0 | 4.417 (9.017) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.006 | 284.6 | 16 (vanilla 28) | 862328 | 6.539 / 0.01 | 13.811 / 24.0 | 23.027 | 956 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 206.344 | 16 (vanilla 28) | 538264 | 2.445 / 0.021 | 8.545 / 24.0 | 14.373 | 601 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.998 / 0.134 | 0.131 / 28.0 | 0.225 | 6081 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.833 / 0.133 | 0.302 / 28.0 | 0.518 | 6183 / 0 | 1.291 (1.622) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.775 / 0.133 | 0.153 / 28.0 | 0.261 | 6233 / 0 | 0.862 (1.25) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 154.0 | 1.0 | 0.0 | 0 | 0 | 58 (0.017 / 0.026 / 0.027; 0) | 0 | 38038 / 0 / 0 |
| flythrough | 191.669 | 1.0 | 0.0 | 0 | 0 | 885 (0.026 / 0.04 / 6.034; 0) | 53 | 32967 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1824 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.006 / 0.007 / 0.007; 0) | 32 | 1202 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.077 / 0.12 / 0.135; 0) | 180 | 3164 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 690 | 0 (None / None / None; 0) | 0 | 920 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 627 | 0 (None / None / None; 0) | 0 | 1463 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1896 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 352.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.029 (0.079; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 439.349 | 1.0 (0.0, 0.0) | 1.145 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.054; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.109 (0.281; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.062 (0.077; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 51.364 | 10.0 (9.0, 2.359) | 6.0 (6.0, 2.276) | 0.057 (0.301; 0) | 0 | 2786 / 1507966 / 0 / 0 |
| translucent_move | 135.168 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.608 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.007; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.006 (0.043; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.932 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 83.153 / 0.0 | 0.023 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.336 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.849 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 56.825 / 56.825 | 0.015 / 0.02 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 9.321 | 0.967 / 0.864 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 40.243 / 9.428 | 0.06 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.731 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.72 / 10.639 | 0.005 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 57.594 / 57.696 | 0.012 / 0.02 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.701 / 11.746 | 0.003 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 8.457 | 0.788 / 0.841 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.905 / 1.73 | 0.013 / 0.007 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 98.677 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.007 / 0.051 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.311 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.012 | 56 (3) | 40.608 / 9.248 / 31.36 | 3 / 0 | 10.988 |
| animated_items | 136.426 / 28.078 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.628 / 7.993 / 32.636 | 0 / 0 | 9.0 |

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
| factory | 0.003 | 0.3 | 0.0177 | 0.0 | 0.0 |
| factory_flat | 0.0 | 0.3 | 0.0 | 0.0 | 0.0 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | skipped: MapMod is being finished (Andre, 2026-10-30): the map scene is out of the bench and CI until then; with it, its limits are re-collected | | | | |
| hologram | 0.0014 | 0.55 | 0.0053 | 0.0056 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | skipped: MapMod is being finished (Andre, 2026-10-30): the HUD scene (its minimap) is out of the bench and CI until then; with it, its limits are re-collected | | | | |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.56 | 500 | 0 (14) | 0 (14) | 0 (14) |
| factory_flat | 89.7 | 40 | 0 (14) | 0 (14) | 0 (14) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 87.02 | 10 | 0 (0) | 0 (0) | 0 (27) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 94.16 | 413 | 37 (4049) | 34 (4048) | None (None) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Quicksilver's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0177 / 0 | 0.0001 / 0 | 0.0 / 0 |
| factory_flat | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0011 / 6 | 0.0029 / 12 | skipped |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.21 (base 12.23) | 15 (base 14) | 6956 (base 6488) | 424.15 (base 421.74) | 4.24 (base 5.48) |
| factory_flat | 11.67 (reported, not gated) | 15 (base 14) | 6844 (base 6264) | 421.02 (base 422.37) | 3.48 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 441.14 (base 440.07) | 4.24 (base 5.87) |
| hub | 21.53 (base 21.58) | 16 (base 18) | 66576 (base 67484) | 454.1 (base 451.33) | 4.24 (base 4.24) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 467.27 (base 471.8) | 8.24 (base 8.12) |
| ponder | 87.23 (base 87.25) | 14 (base 14) | 52268 (base 52248) | 466.27 (base 463.81) | 8.24 (base 8.24) |
| hologram | 25 median (avg 24.81) (reported, not gated) | 22 (base 24) | 69696 (base 70368) | 454.58 (base 453.57) | 8.12 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 455.58 (base 470.81) | 8.12 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.21 / 12 (29.05) | 106.91 / 152 (29.51) | median and average, x 1.10 + 2 |
| factory_flat | 11.67 / 12 (29.9) | 105.16 / 152 (29.86) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (15.81) | 153.0 / 153 (18.87) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (29.9) | 114.79 / 161 (29.98) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.92) | 116.56 / 162 (29.67) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (29.91) | 180.74 / 227 (29.77) | median and average, x 1.10 + 2 |
| hologram | 24.81 / 25 (24.64) | 125.48 / 165 (27.7) | median, at most vanilla |
| create_ct | 6.67 / 7 (29.96) | 99.92 / 147 (30.01) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.2 | 32.0 / 20.2 | 0.0 / 0.0 | 416.0 / 33.916 |
| ponder | 32.0 / 17.5 | 32.0 / 17.5 | 0.0 / 0.0 | 416.0 / 29.099 |
| hologram | 32.0 / 0.238 | 32.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 32.0 / 0.171 | 32.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26338.121 (48130.417) | 45.278 | 1.0 | 114684 / 190188 | 100928 (2097152.0) | 11 / 7 | 0.0 / 0 |
| factory_flat | 26337.97 (48130.351) | 45.278 | 1.0 | 6844 / 6844 | 0 (2097152.0) | 1 / 3 | 0.0 / 0 |
| sift | 147736.044 (205036.804) | 27.947 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 316 / 356 | 0.0 / 0 |
| hub | 33471.973 (58844.402) | 43.118 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 2 / 1 | 0.0 / 0 |
| mam | 66328.732 (116185.967) | 42.912 | 1.011 | 304760 / 304740 | 0 (2097152.0) | 1 / 5 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61244 / 61224 | 0 (2097152.0) | 3 / 4 | 0.0 / 0 |
| hologram | 3911.919 (6873.915) | 43.09 | 1.0 | 72180 / 70840 | 0 (2097152.0) | 12 / 9 | 0.0 / 0 |
| create_ct | 2538.223 (5312.84) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 1 / 0 | 0.0 / 0 |

Terrain module, siftec full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.128 | 16 (vanilla 28) | 96100 | 26.348 / 0.239 | 1.511 / 28.0 | 2.566 | 1669 / 0 | 5.119 (10.139) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 96100 | 1.156 / 0.004 | 1.511 / 12.0 | 2.566 | 162 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.009 | 430.358 | 16 (vanilla 28) | 462608 | 23.951 / 0.282 | 7.389 / 16.0 | 12.353 | 2679 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 23.22 / 0.273 | 1.879 / 12.0 | 3.137 | 2762 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1186852 | 23.9 / 0.312 | 20.2 / 32.0 | 31.692 | 3406 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1015872 | 23.9 / 0.312 | 17.5 / 32.0 | 27.127 | 3407 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.026 | 16 (vanilla 28) | 13744 | 23.694 / 0.307 | 0.238 / 32.0 | 0.367 | 3471 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 23.274 / 0.301 | 0.171 / 32.0 | 0.283 | 3570 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1902 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1976 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.004 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.678 / 0.0 | 0.554 / 0.534 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.495 / 10.721 | 0.02 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.49 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.04 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.49 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| hologram | 100.759 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 159.549 / 99.097 | 0.18 / 0.063 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.86 / 13.87 | 0.0 / 0.0 | 573.99 / 574.49 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.86 / 13.87 (8.0 / 8.0) |
| mam | 13.86 / 13.85 | 1.0 / 1.0 | 113.19 / 112.67 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.85 (8.0 / 8.0) |
| ponder | 82.57 / 82.59 | 0.11 / 0.0 | 203.59 / 203.72 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.61 (75.0 / 75.0) |
| hologram | 10.0 / 10.0 | 0.0 / 0.0 | 103.0 / 103.0 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 10.0 / 10.0 (6.0 / 6.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.19 (base 20.23) | 13 (base 13) | 49032 (base 48604) | 421.87 (base 421.54) | 4.24 (base 5.48) |
| factory_flat | 19.67 (reported, not gated) | 13 (base 13) | 48916 (base 48340) | 421.25 (base 422.36) | 3.48 (base 5.48) |
| sift | 41.0 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 439.85 (base 441.24) | 4.24 (base 4.24) |
| hub | 25.53 (base 25.54) | 14 (base 17) | 64948 (base 66484) | 452.44 (base 451.46) | 4.24 (base 5.99) |
| mam | 24.52 (base 24.53) | 17 (base 20) | 21108 (base 22916) | 465.22 (base 472.87) | 8.24 (base 5.99) |
| ponder | 87.24 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 464.02 (base 463.76) | 8.24 (base 8.24) |
| hologram | 26 median (avg 25.84) (reported, not gated) | 20 (base 23) | 67972 (base 70624) | 453.12 (base 452.39) | 8.12 (base 6.31) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 454.29 (base 470.06) | 8.12 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.19 / 20 (29.58) | 180.69 / 226 (29.39) | median and average, x 1.10 + 2 |
| factory_flat | 19.67 / 20 (29.89) | 179.24 / 226 (29.9) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (15.6) | 579.0 / 579 (13.12) | median, x 1.10 + 2 |
| hub | 25.53 / 25 (29.71) | 164.44 / 208 (29.1) | median and average, x 1.10 + 2 |
| mam | 24.52 / 24 (29.89) | 204.03 / 246 (28.65) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (29.63) | 181.2 / 227 (29.66) | median and average, x 1.10 + 2 |
| hologram | 25.84 / 26 (23.45) | 176.0 / 176 (18.11) | median, at most vanilla |
| create_ct | 6.67 / 7 (29.99) | 111.08 / 158 (29.93) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.633 | 12.0 / 1.633 | 0.0 / 0.0 | 416.0 / 2.766 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.229 | 32.0 / 20.229 | 0.0 / 0.0 | 416.0 / 33.811 |
| ponder | 32.0 / 17.577 | 32.0 / 17.577 | 0.0 / 0.0 | 416.0 / 29.004 |
| hologram | 32.0 / 0.238 | 32.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 32.0 / 0.171 | 32.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26337.266 (48128.853) | 45.278 | 1.0 | 156760 / 233368 | 100928 (2097152.0) | 7 / 10 | 0.0 / 0 |
| factory_flat | 26337.97 (48130.351) | 45.278 | 1.0 | 48916 / 50020 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| sift | 145928.224 (205045.096) | 28.831 | 1.434 | 4148 / 8052 | 0 (2097152.0) | 312 / 262 | 0.0 / 0 |
| hub | 33472.161 (58844.733) | 43.118 | 1.02 | 83908 / 84516 | 0 (2097152.0) | 6 / 8 | 0.0 / 0 |
| mam | 66244.777 (116018.01) | 42.901 | 1.011 | 40068 / 40948 | 0 (2097152.0) | 1 / 7 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61212 / 61228 | 0 (2097152.0) | 6 / 8 | 0.0 / 0 |
| hologram | 3912.342 (6874.658) | 43.09 | 1.0 | 71892 / 69572 | 0 (2097152.0) | 35 / 343 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1124 / 1316 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.014 | 74.125 | 16 (vanilla 28) | 96100 | 24.069 / 0.277 | 1.511 / 28.0 | 2.566 | 1663 / 0 | 4.764 (10.262) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 103664 | 1.114 / 0.004 | 1.633 / 12.0 | 2.768 | 174 / 0 | None (None) |
| sift | plain, one multi-draw per region and layer | 21.067 | 430.375 | 16 (vanilla 28) | 462608 | 22.211 / 0.311 | 7.389 / 16.0 | 12.353 | 2673 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.084 | 16 (vanilla 28) | 117492 | 21.563 / 0.301 | 1.879 / 12.0 | 3.137 | 2755 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.146 | 16 (vanilla 28) | 1188232 | 22.565 / 0.331 | 20.229 / 32.0 | 31.729 | 3402 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1019852 | 22.565 / 0.331 | 17.577 / 32.0 | 27.233 | 3403 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.006 | 13.028 | 16 (vanilla 28) | 13744 | 22.381 / 0.326 | 0.238 / 32.0 | 0.367 | 3467 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 21.955 / 0.32 | 0.171 / 32.0 | 0.283 | 3575 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1878 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 596 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1876 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.036; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.037; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.446 / 0.0 | 0.567 / 0.519 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 9.585 / 9.649 | 0.02 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.568 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 95.904 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 97.487 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.963 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| hologram | 140.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 148.115 / 88.06 | 0.163 / 0.061 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.86 / 13.86 | 0.0 / 0.0 | 574.33 / 574.0 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.86 / 13.86 (8.0 / 8.0) |
| mam | 13.85 / 13.85 | 1.0 / 1.0 | 112.5 / 112.39 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.85 (8.0 / 8.0) |
| ponder | 82.56 / 82.57 | 0.0 / 0.11 | 203.49 / 203.31 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.59 / 81.59 (75.0 / 75.0) |
| hologram | 10.0 / 10.0 | 0.0 / 0.0 | 103.0 / 103.0 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 10.0 / 10.0 (6.0 / 6.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.23 (base 14.22) | 15 (base 15) | 51636 (base 51148) | 421.97 (base 420.23) | 4.23 (base 3.71) |
| factory_flat | 13.67 (reported, not gated) | 15 (base 15) | 51500 (base 50904) | 420.58 (base 421.17) | 3.46 (base 3.46) |
| sift | 21.5 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 439.85 (base 439.9) | 4.23 (base 4.23) |
| hub | 21.54 (base 21.55) | 16 (base 18) | 66576 (base 67484) | 452.37 (base 450.49) | 4.23 (base 4.23) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 466.06 (base 462.56) | 8.23 (base 5.46) |
| ponder | 87.23 (base 87.24) | 14 (base 14) | 52268 (base 52248) | 464.71 (base 462.87) | 8.23 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 457.18 (base 475.26) | 8.04 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.23 / 14 (29.99) | 107.71 / 154 (29.97) | median and average, x 1.10 + 2 |
| factory_flat | 13.67 / 14 (30.01) | 107.0 / 154 (29.98) | median and average, x 1.10 + 2 |
| sift | 21.5 / 23 (23.1) | 138.58 / 153 (22.02) | median, x 1.10 + 2 |
| hub | 21.54 / 21 (29.96) | 114.87 / 161 (29.97) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.89) | 116.88 / 162 (29.61) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (30.02) | 180.58 / 227 (29.97) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 99.92 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.299 | 32.0 / 20.299 | 0.0 / 0.0 | 416.0 / 33.943 |
| ponder | 32.0 / 17.602 | 32.0 / 17.602 | 0.0 / 0.0 | 416.0 / 29.245 |
| create_ct | 32.0 / 0.171 | 32.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26336.623 (48127.683) | 45.278 | 1.0 | 159344 / 234840 | 100928 (2097152.0) | 0 / 0 | 0.0 / 0 |
| factory_flat | 26337.823 (48130.083) | 45.278 | 1.0 | 51500 / 51500 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| sift | 147588.766 (204832.403) | 27.947 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 23 / 30 | 0.0 / 0 |
| hub | 33471.693 (58843.91) | 43.118 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 1 / 0 | 0.0 / 0 |
| mam | 66244.592 (116017.686) | 42.901 | 1.011 | 42708 / 42688 | 0 (2097152.0) | 6 / 16 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61244 / 61224 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.123 | 16 (vanilla 28) | 96100 | 26.246 / 0.322 | 1.511 / 28.0 | 2.566 | 1632 / 0 | 2.451 (2.314) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.123 | 16 (vanilla 28) | 96100 | 1.294 / 0.004 | 1.511 / 12.0 | 2.566 | 168 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.006 | 429.929 | 16 (vanilla 28) | 462608 | 23.869 / 0.347 | 7.389 / 16.0 | 12.353 | 2639 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.083 | 16 (vanilla 28) | 117492 | 23.166 / 0.336 | 1.879 / 12.0 | 3.137 | 2722 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1192420 | 23.873 / 0.363 | 20.299 / 32.0 | 31.841 | 3377 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1021568 | 23.873 / 0.363 | 17.602 / 32.0 | 27.279 | 3378 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 23.425 / 0.357 | 0.171 / 32.0 | 0.283 | 3482 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2778 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.012; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.012; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.489 / 0.0 | 0.554 / 0.56 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.691 / 10.681 | 0.02 / 0.009 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.004 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 125.682 / 8.636 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.358 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.87 | 0.0 / 0.0 | 574.62 / 574.48 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.87 (8.0 / 8.0) |
| mam | 13.86 / 13.85 | 1.0 / 1.0 | 113.06 / 112.84 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.85 (8.0 / 8.0) |
| ponder | 82.57 / 82.59 | 0.11 / 0.11 | 203.7 / 203.86 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.61 (75.0 / 75.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

## Bench-only jar (quicksilver-bench)

| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |
|---|---|---|---|---|---|---|---|
| bench-plain | spawn | 10.74 | 114.96 | 214 | 123 | 1097852 | [] |
| bench-plain | flythrough | 7.88 | 252.0 | 157 | 123 | 7641896 | [] |
| bench-plain | water_glass | 18.39 | 83.01 | 341 | 124 | 261528 | [] |
| bench-plain | nether | 18.96 | 67.4 | 319 | 121 | 332836 | [] |
| bench-sodium | spawn | 16.55 | 87.69 | 330 | 148 | 9040 | [] |
| bench-sodium | flythrough | 11.99 | 195.74 | 239 | 159 | 266456 | [] |
| bench-sodium | water_glass | 29.73 | 40.04 | 0 | 135 | 10236 | [] |
| bench-sodium | nether | 30.01 | 40.0 | 0 | 129 | 263776 | [] |

## Direct buffers per scene (reported; open bug, see docs/progress.md)

| run | scene | start MB | peak MB (tick) | end MB | rise |
|---|---|---|---|---|---|
| core-bench-plain | spawn | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-plain | water_glass | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-plain | nether | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-plain | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-sodium | spawn | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-sodium | water_glass | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-sodium | nether | 3.6 | 3.6 (0) | 3.6 |  |
| core-bench-sodium | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | spawn | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | water_glass | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | nether | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | mobs | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | outline | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | occlusion | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | occlusion_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | translucent_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | particles | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | animation | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | animated_items | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-full | water_glass_oit | 3.35 | 3.35 (0) | 3.35 |  |
| core-measure-full | translucent_move_oit | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | spawn | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | water_glass | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | nether | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | mobs | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | outline | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | occlusion | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | occlusion_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | translucent_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | particles | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | animation | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | animated_items | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | flat_plants | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-old | renderer_off | 3.35 | 3.35 (0) | 3.35 |  |
| core-measure-old | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-old | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-vk | spawn | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | water_glass | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | nether | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | mobs | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | outline | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | occlusion | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | occlusion_move | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | translucent_move | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | particles | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | animation | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | animated_items | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | flat_plants | 3.46 | 3.46 (0) | 3.46 |  |
| core-measure-vk | renderer_off | 3.59 | 3.59 (0) | 3.59 |  |
| core-measure-vk | water_glass_oit | 3.59 | 3.59 (0) | 3.59 |  |
| core-measure-vk | translucent_move_oit | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | flythrough | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-full | spawn | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | water_glass | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | nether | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | mobs | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | outline | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | occlusion | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | occlusion_move | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | translucent_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | particles | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | animation | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | animated_items | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-full | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-full | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-full | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-full | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | flythrough | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | spawn | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | water_glass | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | nether | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | mobs | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | outline | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | occlusion | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | occlusion_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | translucent_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | particles | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | animation | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | animated_items | 5.48 | 5.48 (0) | 5.48 |  |
| core-quicksilver-old | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | water_glass_oit | 7.6 | 7.6 (0) | 7.6 |  |
| core-quicksilver-old | translucent_move_oit | 7.73 | 7.73 (0) | 7.73 |  |
| core-quicksilver-old | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-vk | spawn | 3.46 | 3.46 (0) | 3.46 |  |
| core-quicksilver-vk | water_glass | 3.46 | 3.46 (0) | 3.46 |  |
| core-quicksilver-vk | nether | 3.46 | 3.46 (0) | 3.46 |  |
| core-quicksilver-vk | mobs | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | outline | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | occlusion | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | occlusion_move | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | translucent_move | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | particles | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | animation | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | animated_items | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | flat_plants | 3.59 | 3.59 (0) | 3.59 |  |
| core-quicksilver-vk | renderer_off | 3.59 | 3.59 (0) | 3.59 |  |
| core-quicksilver-vk | water_glass_oit | 3.34 | 3.34 (0) | 3.34 |  |
| core-quicksilver-vk | translucent_move_oit | 3.46 | 3.46 (0) | 3.46 |  |
| core-quicksilver-vk | flythrough | 3.71 | 3.71 (0) | 3.71 |  |
| siftec-measure-full | factory | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-measure-full | sift | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-measure-full | hub | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-measure-full | mam | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-measure-full | ponder | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-measure-full | hologram | 5.93 | 5.93 (0) | 5.93 |  |
| siftec-measure-full | create_ct | 5.93 | 5.93 (0) | 5.93 |  |
| siftec-measure-full | factory_flat | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-measure-old | factory | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-old | sift | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-old | hub | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-old | mam | 8.24 | 8.24 (0) | 8.24 | between scenes +4.00 |
| siftec-measure-old | ponder | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-measure-old | hologram | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-measure-old | create_ct | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-measure-old | factory_flat | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-measure-vk | factory | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | sift | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | hub | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | mam | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | ponder | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | create_ct | 5.79 | 5.79 (0) | 5.79 |  |
| siftec-measure-vk | factory_flat | 3.46 | 3.46 (0) | 3.46 |  |
| siftec-quicksilver-full | factory | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-full | sift | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-full | hub | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-full | mam | 8.24 | 8.24 (0) | 8.24 | between scenes +4.00 |
| siftec-quicksilver-full | ponder | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-quicksilver-full | hologram | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-quicksilver-full | create_ct | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-quicksilver-full | factory_flat | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-quicksilver-old | factory | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-old | sift | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-old | hub | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-old | mam | 8.24 | 8.24 (0) | 8.24 | between scenes +4.00 |
| siftec-quicksilver-old | ponder | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-quicksilver-old | hologram | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-quicksilver-old | create_ct | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-quicksilver-old | factory_flat | 3.48 | 3.48 (0) | 3.48 |  |
| siftec-quicksilver-vk | factory | 4.23 | 4.23 (0) | 4.23 |  |
| siftec-quicksilver-vk | sift | 4.23 | 4.23 (0) | 4.23 |  |
| siftec-quicksilver-vk | hub | 4.23 | 4.23 (0) | 4.23 |  |
| siftec-quicksilver-vk | mam | 8.23 | 8.23 (0) | 8.23 | between scenes +4.00 |
| siftec-quicksilver-vk | ponder | 8.23 | 8.23 (0) | 8.23 |  |
| siftec-quicksilver-vk | create_ct | 8.04 | 8.04 (0) | 8.04 |  |
| siftec-quicksilver-vk | factory_flat | 3.46 | 3.46 (0) | 3.46 |  |

Rises of 4 MB or more: siftec-measure-old/mam: between scenes +4.00; siftec-quicksilver-full/mam: between scenes +4.00; siftec-quicksilver-old/mam: between scenes +4.00; siftec-quicksilver-vk/mam: between scenes +4.00

