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
| spawn | 0.3005 | 10.05 | 0.2959 | 0.2914 | 0.1985 |
| flythrough | 0.0 | 14.95 | 0.001 | 0.001 | 0.0011 |
| water_glass | 0.0029 | 6.45 | 0.0041 | 0.0036 | 0.0033 |
| nether | 0.0098 | 0.8 | 0.0277 | 0.0014 | 0.0375 |
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
| translucent_move | 0.0004 | 0.3 | 0.0004 | 0.0 | 0.0002 |
| translucent_move.x2404 | 0.0001 | 0.3 | 0.0002 | 0.0004 | 0.0003 |
| translucent_move.x2414 | 0.0002 | 0.3 | 0.0002 | 0.0001 | 0.0001 |
| translucent_move.x2424 | 0.0 | 0.3 | 0.0002 | 0.0003 | 0.0001 |
| translucent_move.low | 0.0001 | 0.3 | 0.0013 | 0.0013 | 0.0014 |
| translucent_move.back | 0.0002 | 0.3 | 0.0001 | 0.0 | 0.0001 |
| translucent_move.bare_back | 0.0001 | 0.3 | 0.0001 | 0.0 | 0.0002 |
| water_glass_oit | 0.0036 | 0.3 | 0.0038 | 0.0052 | 0.0042 |
| translucent_move_oit | 0.0 | 0.3 | 0.0 | 0.0 | 0.0001 |
| translucent_move_oit.x2404 | 0.0002 | 0.3 | 0.0007 | 0.0007 | 0.0005 |
| translucent_move_oit.x2414 | 0.0 | 0.3 | 0.0002 | 0.0002 | 0.0003 |
| translucent_move_oit.x2424 | 0.0002 | 0.3 | 0.0004 | 0.0004 | 0.0002 |
| translucent_move_oit.low | 0.0003 | 0.3 | 0.0011 | 0.0014 | 0.001 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 | 0.0001 | 0.0 |
| translucent_move_oit.bare_back | 0.0002 | 0.3 | 0.0003 | 0.0001 | 0.0002 |
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
| spawn | 92.6 | 18057 | 247 (741) | 233 (731) | 297 (828) |
| flythrough | 97.29 | 6020 | 47 (299) | 47 (299) | 51 (300) |
| water_glass | 95.81 | 25508 | 21 (2622) | 15 (2615) | 10 (2725) |
| nether | 99.37 | 905 | 165 (620) | 13 (468) | 3 (470) |
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
| translucent_move | 96.93 | 13 | 3 (78) | 1 (76) | 1 (67) |
| translucent_move.x2404 | 95.93 | 15 | 4 (104) | 8 (109) | 4 (88) |
| translucent_move.x2414 | 95.86 | 13 | 3 (106) | 2 (105) | 3 (100) |
| translucent_move.x2424 | 96.74 | 13 | 2 (79) | 3 (80) | 1 (67) |
| translucent_move.low | 96.11 | 41 | 17 (23) | 18 (24) | 16 (18) |
| translucent_move.back | 96.93 | 13 | 3 (79) | 2 (77) | 2 (68) |
| translucent_move.bare_back | 96.9 | 13 | 2 (78) | 3 (79) | 2 (68) |
| water_glass_oit | 95.81 | 57 | 18 (2618) | 28 (2629) | 21 (2735) |
| translucent_move_oit | 96.93 | 12 | 4 (79) | 1 (76) | 3 (70) |
| translucent_move_oit.x2404 | 95.93 | 16 | 6 (106) | 6 (106) | 6 (90) |
| translucent_move_oit.x2414 | 95.86 | 10 | 4 (107) | 2 (105) | 1 (98) |
| translucent_move_oit.x2424 | 96.74 | 15 | 1 (79) | 2 (79) | 1 (67) |
| translucent_move_oit.low | 96.11 | 40 | 13 (18) | 16 (21) | 13 (16) |
| translucent_move_oit.back | 96.93 | 12 | 1 (76) | 1 (76) | 1 (67) |
| translucent_move_oit.bare_back | 96.9 | 15 | 3 (78) | 2 (77) | 7 (73) |
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
| spawn | 0.3018 / 208 | 0.4386 / 308 | 0.4428 / 222 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| water_glass | 0.003 / 14 | 0.0029 / 11 | 0.0039 / 17 |
| nether | 0.0098 / 0 | 0.0 / 0 | 0.0372 / 0 |
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
| translucent_move | 0.0004 / 2 | 0.0001 / 2 | 0.0001 / 2 |
| translucent_move.x2404 | 0.0 / 0 | 0.0 / 1 | 0.0002 / 4 |
| translucent_move.x2414 | 0.0 / 2 | 0.0 / 1 | 0.0001 / 1 |
| translucent_move.x2424 | 0.0 / 1 | 0.0001 / 0 | 0.0 / 0 |
| translucent_move.low | 0.0004 / 5 | 0.0004 / 1 | 0.001 / 7 |
| translucent_move.back | 0.0002 / 1 | 0.0 / 1 | 0.0001 / 1 |
| translucent_move.bare_back | 0.0001 / 0 | 0.0 / 1 | 0.0001 / 1 |
| water_glass_oit | 0.0037 / 16 | 0.0036 / 15 | 0.0033 / 13 |
| translucent_move_oit | 0.0 / 0 | 0.0001 / 2 | 0.0001 / 0 |
| translucent_move_oit.x2404 | 0.0004 / 4 | 0.0001 / 0 | 0.0002 / 0 |
| translucent_move_oit.x2414 | 0.0001 / 2 | 0.0001 / 2 | 0.0002 / 2 |
| translucent_move_oit.x2424 | 0.0002 / 1 | 0.0002 / 0 | 0.0001 / 1 |
| translucent_move_oit.low | 0.0005 / 8 | 0.0007 / 6 | 0.0002 / 2 |
| translucent_move_oit.back | 0.0001 / 1 | 0.0001 / 0 | 0.0 / 1 |
| translucent_move_oit.bare_back | 0.0001 / 0 | 0.0003 / 3 | 0.0001 / 0 |
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
| spawn | 13.06 (base 13.6) | 14 (base 14) | 25460 (base 22476) | 260.83 (base 259.07) | 4.24 (base 3.99) |
| flythrough | 13.07 (reported, not gated) | 21 (reported, not gated) | 154876 (reported, not gated) | 317.23 (base 324.25) | 3.48 (base 4.24) |
| water_glass | 8.7 (base 8.97) | 15 (base 15) | 16084 (base 15576) | 245.52 (base 250.02) | 4.24 (base 5.99) |
| nether | 12.41 (base 15.63) | 12 (base 12) | 20432 (base 19648) | 266.27 (base 273.53) | 4.24 (base 5.99) |
| mobs | 19.67 (base 19.77) | 17 (base 17) | 616492 (base 1010948) | 265.11 (base 268.37) | 4.24 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.64 (base 264.69) | 4.24 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100652 (base 126320) | 270.2 (base 265.1) | 4.24 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82544 (reported, not gated) | 270.53 (base 265.74) | 4.24 (base 4.24) |
| translucent_move | 8.69 (reported, not gated) | 15 (reported, not gated) | 10944 (reported, not gated) | 272.57 (base 267.48) | 4.24 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15860 (base 14688) | 244.36 (base 245.02) | 3.6 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 11020 (reported, not gated) | 247.02 (base 247.19) | 3.73 (base 3.73) |
| flat_plants | 16.26 (base 18.29) | 15 (base 15) | 10168 (base 9812) | 247.27 (base 246.92) | 3.48 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7332 (base 7164) | 239.77 (base 241.33) | 3.6 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 267.85 (base 261.44) | 4.24 (base 5.99) |
| animation | 49 median (avg 39.98) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.35 (base 260.68) | 4.24 (base 5.99) |
| animated_items | 60 median (avg 46.78) (base 60) | 23 (base 23) | 21976 (base 21940) | 240.29 (base 239.81) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.06 / 13 (14.55) | 123.0 / 123 (8.18) | median and average, x 1.10 + 2 |
| flythrough | 13.07 / 13 (10.92) | 123.07 / 123 (5.22) | median and average, at most vanilla + 2 |
| water_glass | 8.7 / 9 (28.76) | 124.0 / 124 (14.11) | median, at most vanilla |
| nether | 12.41 / 15 (28.15) | 120.77 / 121 (14.45) | median, x 1.10 + 2 |
| mobs | 19.67 / 20 (29.85) | 134.0 / 134 (18.07) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.01) | 87.27 / 126 (29.99) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.59 / 133 (29.91) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (30.0) | 94.53 / 133 (29.88) | median and average, x 1.10 + 2 |
| translucent_move | 8.69 / 9 (29.0) | 124.0 / 124 (14.44) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (15.55) | 129.0 / 129 (7.57) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (13.31) | 129.0 / 129 (7.3) | median and average, at most vanilla |
| flat_plants | 16.26 / 19 (26.64) | 123.88 / 124 (14.09) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (30.0) | 106.93 / 123 (23.15) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.46 / 125 (29.96) | median and average, x 1.10 + 2 |
| animation | 39.98 / 49 (30.0) | 144.42 / 179 (29.99) | median, x 1.10 + 2 |
| animated_items | 46.78 / 60 (30.0) | 155.27 / 190 (29.99) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.804 | 36.0 / 22.804 | 0.0 / 0.0 | 416.0 / 36.659 |
| flythrough | 112.0 / 87.309 | 112.0 / 87.309 | 0.0 / 0.0 | 416.0 / 143.515 |
| water_glass | 36.0 / 12.619 | 36.0 / 12.619 | 0.0 / 0.0 | 416.0 / 21.929 |
| nether | 20.0 / 15.264 | 20.0 / 15.264 | 0.0 / 0.0 | 256.0 / 26.776 |
| mobs | 16.0 / 9.904 | 16.0 / 9.904 | 0.0 / 0.0 | 256.0 / 17.082 |
| outline | 16.0 / 1.564 | 16.0 / 1.564 | 0.0 / 0.0 | 256.0 / 2.75 |
| occlusion | 20.0 / 2.46 | 20.0 / 2.46 | 0.0 / 0.0 | 416.0 / 4.161 |
| occlusion_move | 20.0 / 3.302 | 20.0 / 3.302 | 0.0 / 0.0 | 416.0 / 5.557 |
| translucent_move | 28.0 / 13.05 | 28.0 / 13.05 | 0.0 / 0.0 | 416.0 / 22.38 |
| water_glass_oit | 20.0 / 12.619 | 20.0 / 12.619 | 0.0 / 0.0 | 416.0 / 21.945 |
| translucent_move_oit | 28.0 / 12.64 | 28.0 / 12.64 | 0.0 / 0.0 | 416.0 / 21.667 |
| flat_plants | 24.0 / 14.293 | 24.0 / 14.293 | 0.0 / 0.0 | 416.0 / 24.527 |
| renderer_off | 24.0 / 9.036 | 24.0 / 9.036 | 0.0 / 0.0 | 416.0 / 15.928 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 512238.062 (690874.241) | 25.857 | 1.155 | 456820 / 590184 | 431360 (2097152.0) | 291 / 163 | 0.0 / 0 |
| flythrough | 567426.615 (910057.688) | 37.649 | 1.084 | 2291228 / 7444424 | 2088928 (2097152.0) | 218 / 104 | 5.251 / 63 |
| water_glass | 218507.353 (392287.054) | 44.299 | 1.116 | 17540 / 16844 | 0 (2097152.0) | 0 / 282 | 0.0 / 0 |
| nether | 320644.89 (479917.79) | 33.188 | 1.272 | 298704 / 415964 | 276416 (2097152.0) | 1 / 288 | 0.0 / 0 |
| mobs | 170971.906 (309681.863) | 44.791 | 0.99 | 616492 / 616364 | 0 (2097152.0) | 1 / 353 | 0.0 / 0 |
| outline | 28661.611 (49323.933) | 41.891 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |
| occlusion | 43977.295 (76787.98) | 42.729 | 1.211 | 263340 / 126324 | 162688 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 48704.912 (85222.601) | 42.85 | 1.236 | 673992 / 1136736 | 597184 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 202271.579 (359658.957) | 43.76 | 1.174 | 742748 / 1334036 | 731840 (2097152.0) | 3 / 288 | 0.0 / 0 |
| water_glass_oit | 218831.639 (392869.245) | 44.299 | 1.116 | 17652 / 16844 | 0 (2097152.0) | 310 / 151 | 0.0 / 0 |
| translucent_move_oit | 202696.203 (360312.391) | 43.744 | 1.172 | 743284 / 1290560 | 731840 (2097152.0) | 266 / 145 | 0.0 / 0 |
| flat_plants | 243230.987 (447156.871) | 45.605 | 1.06 | 179096 / 271672 | 169152 (2097152.0) | 4 / 281 | 0.0 / 0 |
| renderer_off | 157925.209 (284844.741) | 44.557 | 1.015 | 7332 / 7272 | 0 (2097152.0) | 0 / 9 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86832 / 150268 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79704 / 123160 | 57728 (2097152.0) | 1 / 0 | 0.0 / 0 |

Terrain module, core full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.01 | 801.756 | 16 (vanilla 28) | 1378324 | 40.39 / 0.119 | 22.804 / 36.0 | 36.805 | 2391 / 0 | 2.431 (2.867) |
| flythrough | indirect, one multi-draw per arena and layer | 3.014 | 923.775 | 16 (vanilla 28) | 5288196 | 15.206 / 1.003 | 87.309 / 112.0 | 141.21 | 6702 / 0 | 4.243 (11.179) |
| water_glass | indirect, one multi-draw per arena and layer | 3.005 | 276.48 | 16 (vanilla 28) | 783212 | 32.802 / 0.1 | 12.619 / 36.0 | 20.914 | 3076 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.004 | 471.838 | 16 (vanilla 28) | 958180 | 27.584 / 0.195 | 15.264 / 20.0 | 25.586 | 4143 / 0 | 2.174 (2.535) |
| mobs | indirect, one multi-draw per arena and layer | 2.003 | 204.342 | 16 (vanilla 28) | 618328 | 25.069 / 0.17 | 9.904 / 16.0 | 16.511 | 4587 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 98484 | 24.573 / 0.169 | 1.564 / 16.0 | 2.63 | 4682 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 71.119 | 16 (vanilla 28) | 153320 | 23.351 / 0.16 | 2.46 / 20.0 | 4.094 | 4953 / 0 | 2.121 (2.42) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 76.469 | 16 (vanilla 28) | 205684 | 22.135 / 0.151 | 3.302 / 20.0 | 5.492 | 5241 / 0 | 5.03 (10.208) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.005 | 249.722 | 16 (vanilla 28) | 805348 | 19.251 / 0.138 | 13.05 / 28.0 | 21.505 | 6042 / 0 | 5.351 (12.186) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.016 | 284.916 | 16 (vanilla 28) | 783212 | 7.455 / 0.043 | 12.619 / 20.0 | 20.914 | 1097 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.019 | 264.729 | 16 (vanilla 28) | 779512 | 6.89 / 0.116 | 12.64 / 28.0 | 20.815 | 2452 / 0 | 4.934 (11.929) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.006 | 284.533 | 16 (vanilla 28) | 892636 | 6.287 / 0.01 | 14.293 / 24.0 | 23.836 | 957 / 0 | 1.974 (1.402) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.342 | 16 (vanilla 28) | 568740 | 2.322 / 0.02 | 9.036 / 24.0 | 15.187 | 606 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.203 / 0.137 | 0.131 / 28.0 | 0.225 | 6086 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.044 / 0.136 | 0.302 / 28.0 | 0.518 | 6188 / 0 | 1.03 (1.423) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.988 / 0.137 | 0.153 / 28.0 | 0.261 | 6238 / 0 | 0.909 (1.233) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 153.935 | 1.0 | 0.0 | 0 | 0 | 41 (0.019 / 0.031 / 0.05; 0) | 0 | 44949 / 0 / 0 |
| flythrough | 195.16 | 1.0 | 0.0 | 0 | 0 | 906 (0.027 / 0.033 / 3.965; 0) | 48 | 42740 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2304 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.008 / 0.009 / 0.012; 0) | 32 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 202 (0.083 / 0.135 / 0.252; 0) | 202 | 4060 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 933 | 0 (None / None / None; 0) | 0 | 1244 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 801 | 0 (None / None / None; 0) | 0 | 1869 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2136 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 349.527 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.031 (0.11; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 434.041 | 1.0 (0.0, 0.0) | 1.877 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 142.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.075; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 111.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.102 (0.306; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.052 (0.117; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.36 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.278) | 0.044 (0.216; 0) | 0 | 2781 / 1504768 / 0 / 0 |
| translucent_move | 136.302 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 142.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.88 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.007; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.007 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 103.277 / 0.0 | 0.03 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.369 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.863 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.855 / 55.835 | 0.015 / 0.026 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 7.1 | 0.774 / 0.6 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 33.475 / 6.584 | 0.063 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.923 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.859 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.61 / 12.617 | 0.007 / 0.018 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.32 / 54.318 | 0.019 / 0.024 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.796 / 12.73 | 0.005 / 0.013 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 7.505 | 0.875 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 3.796 / 3.56 | 0.024 / 0.012 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 99.069 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.795 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.007 / 0.043 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.757 / 32.311 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.006 / 0.013 | 56 (3) | 40.608 / 9.248 / 31.36 | 3 / 0 | 10.987 |
| animated_items | 136.553 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

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
| spawn | 49.42 (base 49.0) | 13 (base 13) | 1492 (base 6372) | 260.91 (base 256.29) | 3.99 (base 3.73) |
| flythrough | 66.36 (reported, not gated) | 50 (reported, not gated) | 1067844 (reported, not gated) | 313.58 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 20.0 (base 20.0) | 13 (base 14) | 7540 (base 10264) | 245.61 (base 249.83) | 3.99 (base 5.99) |
| nether | 33.61 (base 33.79) | 10 (base 11) | 3728 (base 10656) | 266.87 (base 272.57) | 3.99 (base 5.99) |
| mobs | 28.96 (base 28.97) | 15 (base 16) | 610676 (base 1015892) | 265.22 (base 268.23) | 3.99 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 264.83 (base 264.41) | 3.99 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 270.39 (base 264.64) | 3.99 (base 4.24) |
| occlusion_move | 24.03 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 271.15 (base 265.66) | 3.99 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 273.28 (base 268.19) | 5.99 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7204 (base 7604) | 244.13 (base 245.78) | 3.35 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 246.93 (base 248.15) | 3.48 (base 3.73) |
| flat_plants | 32.78 (base 33.0) | 13 (base 13) | 1604 (base 1716) | 247.6 (base 248.11) | 3.21 (base 5.99) |
| renderer_off | 17.74 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 239.8 (base 240.73) | 3.34 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 268.13 (base 261.4) | 5.99 (base 4.24) |
| animation | 49 median (avg 39.98) (base 49) | 13 (base 13) | 1716 (base 1716) | 265.75 (base 260.68) | 5.99 (base 4.24) |
| animated_items | 60 median (avg 46.77) (base 60) | 21 (base 22) | 21508 (base 21668) | 241.1 (base 239.53) | 5.99 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.42 / 49 (8.34) | 871.1 / 873 (10.37) | median and average, x 1.10 + 2 |
| flythrough | 66.36 / 64 (5.73) | 1003.89 / 1006 (5.88) | median and average, at most vanilla |
| water_glass | 20.0 / 20 (15.39) | 395.0 / 395 (16.8) | median, at most vanilla |
| nether | 33.61 / 34 (16.51) | 588.95 / 589 (16.66) | median, x 1.10 + 2 |
| mobs | 28.96 / 29 (20.57) | 320.16 / 330 (21.81) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 127.25 / 166 (30.0) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (29.98) | 161.27 / 200 (30.01) | median and average, x 1.10 + 2 |
| occlusion_move | 24.03 / 24 (29.63) | 166.6 / 201 (29.99) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (16.53) | 368.87 / 368 (17.56) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (8.74) | 406.0 / 406 (8.85) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (8.09) | 385.33 / 387 (8.67) | median and average, at most vanilla |
| flat_plants | 32.78 / 33 (16.23) | 399.98 / 400 (17.56) | median, x 1.10 + 2 |
| renderer_off | 17.74 / 18 (26.94) | 301.57 / 325 (25.04) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.33 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.98 / 49 (30.0) | 156.46 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.01) | 167.22 / 202 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.913 | 36.0 / 22.913 | 0.0 / 0.0 | 416.0 / 37.719 |
| flythrough | 112.0 / 83.753 | 112.0 / 83.753 | 0.0 / 0.0 | 416.0 / 143.928 |
| water_glass | 36.0 / 12.619 | 36.0 / 12.619 | 0.0 / 0.0 | 416.0 / 21.945 |
| nether | 20.0 / 15.261 | 20.0 / 15.261 | 0.0 / 0.0 | 256.0 / 26.774 |
| mobs | 16.0 / 9.783 | 16.0 / 9.783 | 0.0 / 0.0 | 256.0 / 17.082 |
| outline | 16.0 / 1.564 | 16.0 / 1.564 | 0.0 / 0.0 | 256.0 / 2.75 |
| occlusion | 20.0 / 2.468 | 20.0 / 2.468 | 0.0 / 0.0 | 416.0 / 4.615 |
| occlusion_move | 20.0 / 3.313 | 20.0 / 3.313 | 0.0 / 0.0 | 416.0 / 5.957 |
| translucent_move | 28.0 / 13.065 | 28.0 / 13.065 | 0.0 / 0.0 | 416.0 / 22.768 |
| water_glass_oit | 20.0 / 12.619 | 20.0 / 12.619 | 0.0 / 0.0 | 416.0 / 21.945 |
| translucent_move_oit | 28.0 / 12.654 | 28.0 / 12.654 | 0.0 / 0.0 | 416.0 / 22.148 |
| flat_plants | 24.0 / 14.293 | 24.0 / 14.293 | 0.0 / 0.0 | 416.0 / 24.656 |
| renderer_off | 24.0 / 9.036 | 24.0 / 9.036 | 0.0 / 0.0 | 416.0 / 15.928 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 453563.145 (695584.651) | 34.794 | 1.775 | 466780 / 1291744 | 465288 (2097152.0) | 166 / 207 | 0.0 / 0 |
| flythrough | 547823.544 (878622.596) | 37.65 | 1.084 | 2107108 / 7612972 | 2096368 (2097152.0) | 114 / 117 | 9.174 / 53 |
| water_glass | 218838.515 (392881.59) | 44.299 | 1.116 | 8884 / 11700 | 0 (2097152.0) | 307 / 336 | 0.0 / 0 |
| nether | 270885.448 (480478.661) | 43.622 | 1.702 | 233328 / 448912 | 227584 (2097152.0) | 330 / 333 | 0.0 / 0 |
| mobs | 168994.18 (306100.964) | 44.791 | 0.99 | 610676 / 612404 | 0 (2097152.0) | 42 / 10 | 0.0 / 0 |
| outline | 28661.69 (49324.07) | 41.891 | 1.0 | 5012 / 6964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 44113.4 (77062.223) | 42.756 | 1.211 | 98148 / 124924 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 48828.877 (85457.528) | 42.862 | 1.236 | 695712 / 1118540 | 620736 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 202565.448 (359806.83) | 43.702 | 1.174 | 759188 / 1327556 | 756352 (2097152.0) | 330 / 351 | 0.0 / 0 |
| water_glass_oit | 219381.609 (393856.609) | 44.299 | 1.116 | 8436 / 11924 | 0 (2097152.0) | 174 / 177 | 0.0 / 0 |
| translucent_move_oit | 202808.72 (359963.764) | 43.659 | 1.172 | 737428 / 1285124 | 731840 (2097152.0) | 161 / 173 | 0.0 / 0 |
| flat_plants | 243543.364 (447741.667) | 45.606 | 1.06 | 1940 / 4676 | 0 (2097152.0) | 324 / 351 | 0.0 / 0 |
| renderer_off | 157954.508 (284897.588) | 44.557 | 1.015 | 1380 / 3172 | 0 (2097152.0) | 7 / 2 | 0.0 / 0 |
| particles | 1927.207 (4215.013) | 54.278 | 1.0 | 8772 / 8964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86232 / 149896 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79144 / 122740 | 57728 (2097138.097) | 0 / 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.235 | 808.277 | 16 (vanilla 28) | 1383496 | 41.478 / 0.103 | 22.913 / 36.0 | 36.943 | 2344 / 0 | 6.174 (10.071) |
| flythrough | plain, one multi-draw per region and layer | 56.737 | 880.447 | 16 (vanilla 28) | 5068304 | 15.407 / 1.004 | 83.753 / 112.0 | 135.338 | 6307 / 0 | 9.068 (22.149) |
| water_glass | plain, one multi-draw per region and layer | 14.046 | 276.899 | 16 (vanilla 28) | 783212 | 32.973 / 0.089 | 12.619 / 36.0 | 20.914 | 2993 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.061 | 471.424 | 16 (vanilla 28) | 958132 | 27.487 / 0.182 | 15.261 / 20.0 | 25.585 | 4050 / 0 | 6.304 (19.619) |
| mobs | plain, one multi-draw per region and layer | 11.027 | 198.482 | 16 (vanilla 28) | 610716 | 24.924 / 0.159 | 9.783 / 16.0 | 16.308 | 4490 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 98484 | 24.422 / 0.158 | 1.564 / 16.0 | 2.63 | 4584 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.118 | 16 (vanilla 28) | 153868 | 23.39 / 0.151 | 2.468 / 20.0 | 4.109 | 4817 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.987 | 76.396 | 16 (vanilla 28) | 206832 | 22.08 / 0.142 | 3.313 / 20.0 | 5.523 | 5115 / 0 | 5.914 (13.295) |
| translucent_move | plain, one multi-draw per region and layer | 12.036 | 249.382 | 16 (vanilla 28) | 806792 | 19.247 / 0.131 | 13.065 / 28.0 | 21.544 | 5877 / 0 | 7.147 (18.326) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.092 | 285.632 | 16 (vanilla 28) | 783212 | 7.386 / 0.041 | 12.619 / 20.0 | 20.914 | 1091 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.627 | 264.59 | 16 (vanilla 28) | 780948 | 6.662 / 0.102 | 12.654 / 28.0 | 20.854 | 2416 / 0 | 9.004 (22.294) |
| flat_plants | plain, one multi-draw per region and layer | 17.052 | 284.877 | 16 (vanilla 28) | 892728 | 6.135 / 0.01 | 14.293 / 24.0 | 23.838 | 924 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.024 | 205.38 | 16 (vanilla 28) | 568740 | 2.332 / 0.02 | 9.036 / 24.0 | 15.187 | 601 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 19.19 / 0.131 | 0.131 / 28.0 | 0.225 | 5923 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.026 / 0.13 | 0.302 / 28.0 | 0.518 | 6025 / 0 | 1.709 (2.243) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.969 / 0.13 | 0.153 / 28.0 | 0.261 | 6075 / 0 | 1.299 (1.749) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 156.0 | 17.0 | 0.0 | 0 | 0 | 48 (0.034 / 0.053 / 0.057; 0) | 0 | 26052 / 0 / 0 |
| flythrough | 183.261 | 25.835 | 0.0 | 0 | 0 | 823 (0.021 / 0.055 / 2.982; 0) | 36 | 21075 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1232 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.016 / 0.057 / 0.067; 0) | 32 | 1188 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.13 / 0.173 / 3.156; 0) | 180 | 2317 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 525 | 0 (None / None / None; 0) | 0 | 700 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 486 | 0 (None / None / None; 0) | 0 | 1134 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1300 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2160 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 350.725 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.029 (0.073; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 427.643 | 1.0 (0.0, 0.0) | 2.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 142.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.018 (0.037; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (63.864, 0.0) | 0.0 (0.0, 0.0) | 0.153 (4.305; 1) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.065 (0.159; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 51.333 | 10.0 (9.0, 2.36) | 6.0 (6.0, 2.279) | 0.057 (0.209; 0) | 0 | 2756 / 1492076 / 0 / 0 |
| translucent_move | 135.997 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 142.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.772 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.488 / 0.416 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 101.84 / 0.413 | 0.025 / 0.012 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.456 / 0.0 | 0.004 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.809 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.608 / 54.607 | 0.013 / 0.026 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.949 / 0.606 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 42.279 / 6.58 | 0.075 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 105.241 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.539 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.173 / 12.121 | 0.005 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.873 / 52.874 | 0.01 / 0.019 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.218 / 13.185 | 0.006 / 0.011 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.983 / 0.784 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.083 / 1.929 | 0.014 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 91.77 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.04 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.311 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.005 / 0.008 | 56 (3) | 40.608 / 9.248 / 31.36 | 3 / 0 | 10.988 |
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
| spawn | 13.0 (base 14.05) | 14 (base 15) | 25032 (base 27764) | 259.88 (base 257.2) | 4.23 (base 3.98) |
| flythrough | 13.05 (reported, not gated) | 35 (reported, not gated) | 532316 (reported, not gated) | 316.02 (base 318.06) | 3.71 (base 4.23) |
| water_glass | 9.0 (base 9.0) | 15 (base 15) | 15748 (base 15108) | 245.32 (base 244.39) | 4.23 (base 4.23) |
| nether | 15.97 (base 15.71) | 12 (base 12) | 22168 (base 19912) | 266.54 (base 268.95) | 4.23 (base 4.23) |
| mobs | 19.8 (base 19.97) | 17 (base 17) | 616324 (base 1008176) | 264.91 (base 264.31) | 4.23 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.4 (base 264.58) | 4.23 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100632 (base 126356) | 270.08 (base 264.78) | 4.23 (base 3.98) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82524 (reported, not gated) | 270.66 (base 265.16) | 4.23 (base 4.23) |
| translucent_move | 8.99 (reported, not gated) | 15 (reported, not gated) | 10964 (reported, not gated) | 272.78 (base 268.35) | 4.23 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15412 (base 15024) | 244.46 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10888 (reported, not gated) | 246.84 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 18.2 (base 18.96) | 15 (base 15) | 10036 (base 9864) | 247.73 (base 248.07) | 3.71 (base 5.98) |
| renderer_off | 7.67 (base 7.83) | 14 (base 14) | 7332 (base 7180) | 239.88 (base 241.13) | 3.34 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 267.93 (base 261.4) | 4.23 (base 5.98) |
| animation | 49 median (avg 39.94) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.14 (base 260.68) | 4.23 (base 5.98) |
| animated_items | 60 median (avg 46.73) (base 60) | 23 (base 23) | 21976 (base 21940) | 240.25 (base 239.74) | 4.23 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (10.67) | 123.45 / 123 (11.85) | median and average, x 1.10 + 2 |
| flythrough | 13.05 / 13 (7.6) | 123.06 / 123 (7.21) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (18.98) | 124.0 / 124 (18.41) | median, at most vanilla |
| nether | 15.97 / 16 (19.62) | 120.9 / 121 (18.7) | median, x 1.10 + 2 |
| mobs | 19.8 / 20 (25.15) | 115.65 / 134 (23.73) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.2 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.96) | 94.33 / 133 (30.0) | median and average, x 1.10 + 2 |
| translucent_move | 8.99 / 9 (19.26) | 122.82 / 124 (19.66) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (9.98) | 129.0 / 129 (10.41) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (9.23) | 129.0 / 129 (9.88) | median and average, at most vanilla |
| flat_plants | 18.2 / 19 (21.28) | 123.72 / 124 (19.23) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.77) | 90.3 / 123 (27.87) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.32 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 144.46 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 155.22 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 22.554 | 40.0 / 22.554 | 0.0 / 0.0 | 416.0 / 36.894 |
| flythrough | 112.0 / 86.219 | 112.0 / 86.219 | 0.0 / 0.0 | 416.0 / 147.586 |
| water_glass | 40.0 / 12.619 | 40.0 / 12.619 | 0.0 / 0.0 | 416.0 / 21.945 |
| nether | 20.0 / 15.267 | 20.0 / 15.267 | 0.0 / 0.0 | 256.0 / 26.781 |
| mobs | 16.0 / 9.783 | 16.0 / 9.783 | 0.0 / 0.0 | 256.0 / 17.082 |
| outline | 16.0 / 1.564 | 16.0 / 1.564 | 0.0 / 0.0 | 256.0 / 2.75 |
| occlusion | 20.0 / 2.428 | 20.0 / 2.428 | 0.0 / 0.0 | 416.0 / 4.411 |
| occlusion_move | 20.0 / 3.229 | 20.0 / 3.229 | 0.0 / 0.0 | 416.0 / 5.918 |
| translucent_move | 28.0 / 12.915 | 28.0 / 12.915 | 0.0 / 0.0 | 416.0 / 22.728 |
| water_glass_oit | 20.0 / 12.619 | 20.0 / 12.619 | 0.0 / 0.0 | 416.0 / 21.945 |
| translucent_move_oit | 28.0 / 12.559 | 28.0 / 12.559 | 0.0 / 0.0 | 416.0 / 22.109 |
| flat_plants | 24.0 / 14.218 | 24.0 / 14.218 | 0.0 / 0.0 | 416.0 / 24.789 |
| renderer_off | 24.0 / 9.036 | 24.0 / 9.036 | 0.0 / 0.0 | 416.0 / 15.928 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 506916.498 (683786.873) | 25.866 | 1.158 | 463112 / 733596 | 438080 (2097152.0) | 213 / 237 | 0.0 / 0 |
| flythrough | 557753.803 (894664.342) | 37.658 | 1.084 | 2122916 / 7749384 | 2096168 (2097152.0) | 152 / 144 | 6.32 / 63 |
| water_glass | 218703.536 (392639.261) | 44.299 | 1.116 | 17428 / 17180 | 0 (2097152.0) | 319 / 368 | 0.0 / 0 |
| nether | 320836.398 (480231.985) | 33.191 | 1.272 | 252696 / 422696 | 230976 (2097152.0) | 263 / 374 | 0.0 / 0 |
| mobs | 168919.157 (305965.074) | 44.791 | 0.99 | 616324 / 616364 | 0 (2097152.0) | 5 / 3 | 0.0 / 0 |
| outline | 28661.611 (49323.933) | 41.891 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 43398.331 (75660.1) | 42.64 | 1.214 | 100632 / 601560 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 47977.657 (83773.443) | 42.729 | 1.239 | 666740 / 1169104 | 589952 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 200149.756 (355519.512) | 43.702 | 1.175 | 766888 / 1333048 | 756224 (2097152.0) | 288 / 309 | 0.0 / 0 |
| water_glass_oit | 219224.121 (393573.869) | 44.299 | 1.116 | 17540 / 16172 | 0 (2097152.0) | 199 / 208 | 0.0 / 0 |
| translucent_move_oit | 200090.815 (355123.163) | 43.656 | 1.174 | 743320 / 1291028 | 731840 (2097152.0) | 184 / 197 | 0.0 / 0 |
| flat_plants | 242222.598 (445168.998) | 45.589 | 1.06 | 10484 / 9828 | 0 (2097152.0) | 40 / 384 | 0.0 / 0 |
| renderer_off | 157924.53 (284845.128) | 44.558 | 1.015 | 7332 / 7272 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9876 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86832 / 150268 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79704 / 123160 | 57728 (2097152.0) | 1 / 2 | 0.0 / 0 |

Terrain module, core vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.014 | 788.685 | 16 (vanilla 28) | 1361464 | 41.403 / 0.093 | 22.554 / 40.0 | 36.355 | 2497 / 0 | 2.809 (4.294) |
| flythrough | indirect, one multi-draw per arena and layer | 3.02 | 902.967 | 16 (vanilla 28) | 5221388 | 15.245 / 0.979 | 86.219 / 112.0 | 139.426 | 6469 / 0 | 6.176 (16.229) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 276.728 | 16 (vanilla 28) | 783212 | 33.755 / 0.084 | 12.619 / 40.0 | 20.914 | 3154 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 471.199 | 16 (vanilla 28) | 958068 | 28.417 / 0.176 | 15.267 / 20.0 | 25.583 | 4194 / 0 | 2.04 (3.207) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 198.394 | 16 (vanilla 28) | 610716 | 25.821 / 0.154 | 9.783 / 16.0 | 16.308 | 4638 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 98484 | 25.312 / 0.153 | 1.564 / 16.0 | 2.63 | 4733 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 151068 | 24.267 / 0.147 | 2.428 / 20.0 | 4.034 | 4962 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.468 | 16 (vanilla 28) | 201520 | 23.154 / 0.14 | 3.229 / 20.0 | 5.381 | 5223 / 0 | 4.963 (10.802) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.008 | 247.483 | 16 (vanilla 28) | 797884 | 20.236 / 0.13 | 12.915 / 28.0 | 21.306 | 5992 / 0 | 4.761 (10.43) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.025 | 285.427 | 16 (vanilla 28) | 783212 | 7.431 / 0.043 | 12.619 / 20.0 | 20.914 | 1095 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.027 | 261.957 | 16 (vanilla 28) | 775352 | 6.839 / 0.114 | 12.559 / 28.0 | 20.704 | 2421 / 0 | 7.154 (17.439) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.007 | 283.666 | 16 (vanilla 28) | 888248 | 6.388 / 0.01 | 14.218 / 24.0 | 23.719 | 947 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.344 | 16 (vanilla 28) | 568736 | 2.35 / 0.021 | 9.036 / 24.0 | 15.187 | 600 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 20.188 / 0.129 | 0.131 / 28.0 | 0.225 | 6034 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.018 / 0.128 | 0.302 / 28.0 | 0.518 | 6136 / 0 | 1.431 (2.04) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.959 / 0.128 | 0.153 / 28.0 | 0.261 | 6186 / 0 | 1.038 (1.366) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 153.0 | 1.0 | 0.0 | 0 | 0 | 44 (0.026 / 0.039 / 0.042; 0) | 0 | 32742 / 0 / 0 |
| flythrough | 189.49 | 1.0 | 0.0 | 0 | 0 | 866 (0.022 / 0.045 / 6.109; 0) | 53 | 28992 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1520 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 38 (0.016 / 0.019 / 0.027; 0) | 34 | 1202 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.117 / 0.182 / 0.468; 0) | 180 | 2702 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 600 | 0 (None / None / None; 0) | 0 | 800 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 555 | 0 (None / None / None; 0) | 0 | 1295 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1704 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2388 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 342.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.032 (0.118; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 431.203 | 1.0 (0.0, 0.0) | 1.915 (0.0, 0.0) | 0.001 (0.036; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 142.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.057; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.112 (0.219; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.061 (0.085; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.364 | 10.0 (9.0, 2.364) | 6.0 (6.0, 2.28) | 0.05 (0.287; 0) | 0 | 2791 / 1511102 / 0 / 0 |
| translucent_move | 135.109 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 142.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.497 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.023; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.009; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.005 (0.019; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.451 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 85.167 / 0.0 | 0.026 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.351 / 0.0 | 0.004 / 0.003 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.843 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.368 / 53.413 | 0.009 / 0.019 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 9.974 | 0.901 / 0.995 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.107 / 16.763 | 0.069 / 0.012 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 96.804 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.539 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 113.83 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.915 / 12.93 | 0.005 / 0.013 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 51.035 / 50.965 | 0.01 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 14.84 / 14.746 | 0.005 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 9.341 | 0.719 / 0.925 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.763 / 2.535 | 0.014 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 82.585 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.003 / 0.043 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.27 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.01 | 56 (3) | 40.58 / 9.24 / 31.34 | 3 / 0 | 10.985 |
| animated_items | 136.553 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

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
| factory | 0.0148 | 0.3 | 0.0029 | 0.0144 | 0.0 |
| factory_flat | 0.0034 | 0.3 | 0.0042 | 0.0044 | 0.0 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| hologram | 0.0241 | 0.55 | 0.0051 | 0.0255 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | 0.0 | 0.25 | 0.0005 | 0.0005 | 0.0005 |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.59 | 500 | 0 (15) | 0 (15) | 0 (14) |
| factory_flat | 95.28 | 40 | 0 (14) | 3 (17) | 0 (14) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 87.03 | 10 | 0 (74) | 0 (74) | 0 (0) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| map | 99.99 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 99.97 | 413 | 44 (4295) | 41 (4293) | None (None) |
| hud | 44.33 | 10 | 5 (5) | 5 (5) | 5 (5) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Quicksilver's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0144 / 0 | 0.0 / 0 | 0.0 / 0 |
| factory_flat | 0.0 / 0 | 0.0 / 0 | 0.0009 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| map | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0024 / 15 | 0.0046 / 14 | skipped |
| hud | 0.0005 / 5 | 0.0005 / 5 | 0.0005 / 5 |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.24 (base 12.23) | 15 (base 14) | 6976 (base 6488) | 422.31 (base 421.74) | 4.24 (base 5.48) |
| factory_flat | 20.32 (base 20.25) | 15 (base 14) | 10164 (base 6264) | 423.42 (base 422.37) | 3.48 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 440.28 (base 440.07) | 4.24 (base 5.87) |
| hub | 21.53 (base 21.58) | 16 (base 18) | 66576 (base 67484) | 452.72 (base 451.33) | 4.24 (base 4.24) |
| mam | 22.52 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 466.2 (base 471.8) | 8.24 (base 8.12) |
| ponder | 87.23 (base 87.25) | 14 (base 14) | 52268 (base 52248) | 465.0 (base 463.81) | 8.24 (base 8.24) |
| map | 30.0 (base 30.0) | 14 (base 14) | 49164 (base 47032) | 492.3 (base 504.13) | 8.06 (base 8.24) |
| hologram | 33 median (avg 32.84) (base 33) | 22 (base 24) | 72100 (base 70368) | 470.72 (base 453.57) | 8.12 (base 8.24) |
| hud | 31.0 (base 31.0) | 16 (base 16) | 74580 (base 71636) | 491.27 (base 494.92) | 8.18 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 480.23 (base 470.81) | 8.24 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.24 / 12 (28.76) | 105.61 / 152 (29.87) | median and average, x 1.10 + 2 |
| factory_flat | 20.32 / 20 (29.8) | 113.64 / 160 (29.96) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (15.65) | 125.38 / 153 (24.55) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (29.89) | 114.78 / 161 (30.0) | median and average, x 1.10 + 2 |
| mam | 22.52 / 22 (29.91) | 116.01 / 162 (29.94) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (29.87) | 180.57 / 227 (30.02) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (11.19) | 170.0 / 170 (15.48) | median and average, x 1.10 + 2 |
| hologram | 32.84 / 33 (23.47) | 127.68 / 173 (29.44) | median, at most vanilla |
| hud | 31.0 / 31 (16.91) | 160.2 / 171 (21.55) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (29.98) | 99.92 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.535 | 12.0 / 1.535 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.894 |
| hub | 12.0 / 1.886 | 12.0 / 1.886 | 0.0 / 0.0 | 416.0 / 3.307 |
| mam | 32.0 / 20.189 | 32.0 / 20.189 | 0.0 / 0.0 | 416.0 / 33.992 |
| ponder | 32.0 / 17.481 | 32.0 / 17.481 | 0.0 / 0.0 | 416.0 / 29.175 |
| map | 40.0 / 27.961 | 40.0 / 27.961 | 0.0 / 0.0 | 416.0 / 47.53 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.402 | 40.0 / 17.402 | 0.0 / 0.0 | 416.0 / 30.634 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 25072.369 (45893.357) | 45.368 | 1.0 | 94716 / 155244 | 80960 (2097152.0) | 14 / 4 | 0.0 / 0 |
| factory_flat | 25070.634 (45890.148) | 45.368 | 1.0 | 97700 / 158228 | 80960 (2097152.0) | 2 / 1 | 0.0 / 0 |
| sift | 147766.096 (205069.173) | 27.943 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 312 / 30 | 0.0 / 0 |
| hub | 33694.251 (59329.047) | 43.208 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 1 / 1 | 0.0 / 0 |
| mam | 66547.283 (116761.253) | 43.006 | 1.011 | 42708 / 42688 | 0 (2097152.0) | 1 / 1 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61268 / 61248 | 0 (2097152.0) | 2 / 1 | 0.0 / 0 |
| map | 399013.309 (627187.946) | 36.381 | 1.226 | 49164 / 46568 | 0 (2097152.0) | 223 / 309 | 0.0 / 0 |
| hologram | 3912.324 (6874.627) | 43.09 | 1.0 | 74432 / 73872 | 0 (2097152.0) | 46 / 4 | 0.0 / 0 |
| hud | 293515.828 (457696.136) | 35.871 | 1.317 | 74580 / 72020 | 0 (2097152.0) | 338 / 38 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 1 / 0 | 0.0 / 0 |

Terrain module, siftec full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.129 | 16 (vanilla 28) | 91632 | 27.963 / 0.307 | 1.453 / 28.0 | 2.447 | 1735 / 0 | 3.882 (5.769) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 96692 | 3.562 / 0.002 | 1.535 / 12.0 | 2.582 | 296 / 0 | 2.834 (7.346) |
| sift | indirect, one multi-draw per arena and layer | 3.01 | 430.375 | 16 (vanilla 28) | 462656 | 25.203 / 0.336 | 7.389 / 16.0 | 12.354 | 2745 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 118460 | 24.31 / 0.323 | 1.886 / 12.0 | 3.163 | 2837 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.146 | 16 (vanilla 28) | 1186520 | 24.784 / 0.352 | 20.189 / 32.0 | 31.684 | 3480 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1015528 | 24.784 / 0.352 | 17.481 / 32.0 | 27.118 | 3481 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.013 | 560.502 | 16 (vanilla 28) | 1751684 | 21.257 / 0.276 | 27.961 / 40.0 | 46.775 | 4337 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.028 | 16 (vanilla 28) | 13744 | 21.153 / 0.273 | 0.238 / 40.0 | 0.367 | 4399 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.009 | 405.195 | 16 (vanilla 28) | 1093336 | 16.921 / 0.227 | 17.402 / 40.0 | 29.195 | 6237 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.772 / 0.225 | 0.171 / 40.0 | 0.283 | 6339 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1878 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 34.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7616 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1880 / 0 / 0 |
| hud | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12204 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.236; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 285.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.038; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.411 / 0.0 | 0.535 / 0.56 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.292 / 10.436 | 0.018 / 0.009 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.411 / 0.0 | 0.557 / 0.653 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.88 / 12.039 | 0.019 / 0.008 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 112.571 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 34.785 / 0.0 | 0.01 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 95.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 151.748 / 91.845 | 0.169 / 0.054 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 129.281 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.86 / 13.86 | 0.0 / 0.0 | 574.08 / 574.14 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.86 / 13.86 (8.0 / 8.0) |
| mam | 13.86 / 13.85 | 1.0 / 1.0 | 112.71 / 112.38 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.85 (8.0 / 8.0) |
| ponder | 82.57 / 82.57 | 0.0 / 0.11 | 203.62 / 203.7 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.6 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.25 (base 20.23) | 13 (base 13) | 49132 (base 48604) | 422.87 (base 421.54) | 5.99 (base 5.48) |
| factory_flat | 28.29 (base 28.32) | 13 (base 13) | 52240 (base 48340) | 422.38 (base 422.36) | 3.54 (base 5.48) |
| sift | 41.0 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 440.8 (base 441.24) | 5.99 (base 4.24) |
| hub | 25.55 (base 25.54) | 14 (base 17) | 64948 (base 66484) | 453.23 (base 451.46) | 5.99 (base 5.99) |
| mam | 24.52 (base 24.53) | 17 (base 20) | 21108 (base 22916) | 467.12 (base 472.87) | 5.99 (base 5.99) |
| ponder | 87.23 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 465.32 (base 463.76) | 5.99 (base 8.24) |
| map | 67.0 (base 67.0) | 12 (base 12) | 30924 (base 31404) | 493.54 (base 506.16) | 5.87 (base 6.31) |
| hologram | 34 median (avg 33.84) (base 34) | 20 (base 23) | 72444 (base 70624) | 471.21 (base 452.39) | 5.87 (base 6.31) |
| hud | 58.0 (base 58.0) | 14 (base 14) | 60356 (base 59972) | 491.97 (base 493.56) | 5.99 (base 8.18) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 481.19 (base 470.06) | 5.99 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.25 / 20 (29.57) | 180.0 / 226 (29.83) | median and average, x 1.10 + 2 |
| factory_flat | 28.29 / 28 (29.98) | 187.56 / 234 (30.01) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (16.09) | 572.39 / 579 (20.28) | median, x 1.10 + 2 |
| hub | 25.55 / 25 (29.8) | 162.17 / 208 (29.87) | median and average, x 1.10 + 2 |
| mam | 24.52 / 24 (29.66) | 199.77 / 246 (29.99) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (29.85) | 180.58 / 227 (30.03) | median and average, x 1.10 + 2 |
| map | 67.0 / 67 (11.16) | 722.0 / 722 (13.28) | median and average, x 1.10 + 2 |
| hologram | 33.84 / 34 (23.64) | 140.94 / 184 (28.64) | median, at most vanilla |
| hud | 58.0 / 58 (18.07) | 567.4 / 571 (19.63) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (29.98) | 110.84 / 158 (30.01) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 416.0 / 2.7 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.921 |
| hub | 12.0 / 1.886 | 12.0 / 1.886 | 0.0 / 0.0 | 416.0 / 3.307 |
| mam | 32.0 / 20.25 | 32.0 / 20.25 | 0.0 / 0.0 | 416.0 / 34.048 |
| ponder | 32.0 / 17.546 | 32.0 / 17.546 | 0.0 / 0.0 | 416.0 / 29.223 |
| map | 40.0 / 27.961 | 40.0 / 27.961 | 0.0 / 0.0 | 416.0 / 47.102 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.403 | 40.0 / 17.403 | 0.0 / 0.0 | 416.0 / 30.633 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 25071.113 (45891.046) | 45.368 | 1.0 | 136784 / 198424 | 80960 (2097152.0) | 8 / 4 | 0.0 / 0 |
| factory_flat | 25070.391 (45889.696) | 45.368 | 1.0 | 139776 / 201408 | 80960 (2097152.0) | 0 / 0 | 0.0 / 0 |
| sift | 145939.227 (205050.804) | 28.828 | 1.434 | 4148 / 8052 | 0 (2097152.0) | 321 / 124 | 0.0 / 0 |
| hub | 33694.534 (59329.546) | 43.208 | 1.02 | 83908 / 84516 | 0 (2097152.0) | 3 / 2 | 0.0 / 0 |
| mam | 66225.679 (116071.736) | 42.944 | 1.011 | 40068 / 40948 | 0 (2097152.0) | 5 / 1 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61212 / 61228 | 0 (2097152.0) | 5 / 0 | 0.0 / 0 |
| map | 365531.839 (627187.946) | 41.719 | 1.812 | 30924 / 35468 | 0 (2097152.0) | 223 / 265 | 0.0 / 0 |
| hologram | 3912.271 (6874.534) | 43.09 | 1.0 | 75036 / 75740 | 0 (2097152.0) | 46 / 11 | 0.0 / 0 |
| hud | 264270.028 (457610.116) | 42.25 | 1.834 | 60356 / 63924 | 0 (2097152.0) | 361 / 158 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1124 / 1316 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.014 | 74.125 | 16 (vanilla 28) | 91632 | 25.726 / 0.279 | 1.453 / 28.0 | 2.447 | 1973 / 0 | 4.506 (8.354) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 91632 | 3.505 / 0.001 | 1.453 / 12.0 | 2.447 | 312 / 0 | 1.936 (1.82) |
| sift | plain, one multi-draw per region and layer | 21.065 | 430.336 | 16 (vanilla 28) | 462656 | 23.638 / 0.311 | 7.389 / 16.0 | 12.354 | 2980 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.084 | 16 (vanilla 28) | 118460 | 22.955 / 0.301 | 1.886 / 12.0 | 3.163 | 3066 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.147 | 16 (vanilla 28) | 1189652 | 23.671 / 0.333 | 20.25 / 32.0 | 31.767 | 3717 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1018788 | 23.671 / 0.333 | 17.546 / 32.0 | 27.205 | 3718 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 40.179 | 560.502 | 16 (vanilla 28) | 1751684 | 20.564 / 0.265 | 27.961 / 40.0 | 46.775 | 4574 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.006 | 13.028 | 16 (vanilla 28) | 13744 | 20.473 / 0.262 | 0.238 / 40.0 | 0.367 | 4636 / 0 | None (None) |
| hud | plain, one multi-draw per region and layer | 30.083 | 405.119 | 16 (vanilla 28) | 1093336 | 16.852 / 0.22 | 17.403 / 40.0 | 29.195 | 6428 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.716 / 0.218 | 0.171 / 40.0 | 0.283 | 6522 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1932 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 596 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 593 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 34.0 | 16.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7616 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1892 / 0 / 0 |
| hud | 36.0 | 9.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 13032 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 285.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.802 / 0.0 | 0.529 / 0.572 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.578 / 10.645 | 0.018 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.255 / 0.0 | 0.636 / 0.626 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.875 / 11.67 | 0.018 / 0.005 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 133.432 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.645 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.647 / 0.0 | 0.011 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 97.243 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 158.398 / 96.345 | 0.175 / 0.053 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 136.429 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.178 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.89 / 13.86 | 0.0 / 0.0 | 575.06 / 574.08 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.89 / 13.86 (8.0 / 8.0) |
| mam | 13.85 / 13.85 | 1.0 / 1.0 | 112.7 / 112.62 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.85 (8.0 / 8.0) |
| ponder | 82.56 / 82.58 | 0.11 / 0.11 | 203.56 / 203.7 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.59 / 81.61 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.24 (base 14.22) | 15 (base 15) | 51728 (base 51148) | 422.0 (base 420.23) | 4.23 (base 3.71) |
| factory_flat | 22.24 (base 22.31) | 15 (base 15) | 54700 (base 50904) | 421.81 (base 421.17) | 3.46 (base 3.46) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 440.02 (base 439.9) | 4.23 (base 4.23) |
| hub | 21.54 (base 21.55) | 16 (base 18) | 66576 (base 67484) | 452.14 (base 450.49) | 4.23 (base 4.23) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 466.04 (base 462.56) | 8.23 (base 5.46) |
| ponder | 87.26 (base 87.24) | 14 (base 14) | 52268 (base 52248) | 464.39 (base 462.87) | 8.23 (base 8.23) |
| map | 30.0 (base 30.0) | 14 (base 14) | 49164 (base 47072) | 491.23 (base 504.09) | 8.1 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| hud | 31.0 (base 31.0) | 16 (base 16) | 75624 (base 72908) | 477.14 (base 490.43) | 8.1 (base 8.16) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 471.41 (base 475.26) | 8.1 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.24 / 14 (29.87) | 110.45 / 154 (28.8) | median and average, x 1.10 + 2 |
| factory_flat | 22.24 / 22 (29.98) | 116.32 / 162 (29.74) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (17.76) | 153.0 / 153 (12.99) | median, x 1.10 + 2 |
| hub | 21.54 / 21 (29.89) | 117.11 / 161 (29.31) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.94) | 125.67 / 162 (26.9) | median, x 1.10 + 2 |
| ponder | 87.26 / 87 (29.9) | 180.64 / 227 (29.93) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (13.28) | 170.0 / 170 (9.3) | median and average, x 1.10 + 2 |
| hud | 31.0 / 31 (19.15) | 171.0 / 171 (12.7) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 99.92 / 147 (29.96) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.894 |
| hub | 12.0 / 1.886 | 12.0 / 1.886 | 0.0 / 0.0 | 416.0 / 3.307 |
| mam | 32.0 / 20.297 | 32.0 / 20.297 | 0.0 / 0.0 | 416.0 / 33.978 |
| ponder | 32.0 / 17.586 | 32.0 / 17.586 | 0.0 / 0.0 | 416.0 / 29.153 |
| map | 40.0 / 27.961 | 40.0 / 27.961 | 0.0 / 0.0 | 416.0 / 47.109 |
| hud | 56.0 / 16.26 | 56.0 / 16.26 | 0.0 / 0.0 | 416.0 / 28.534 |
| create_ct | 56.0 / 0.687 | 56.0 / 0.687 | 0.0 / 0.0 | 416.0 / 1.192 |


Package 4b, siftec vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 25070.652 (45890.207) | 45.368 | 1.0 | 139376 / 199896 | 80960 (2097152.0) | 2 / 11 | 0.0 / 0 |
| factory_flat | 25070.391 (45889.696) | 45.368 | 1.0 | 142352 / 202796 | 80960 (2097152.0) | 1 / 0 | 0.0 / 0 |
| sift | 147708.913 (204989.814) | 27.943 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 355 / 259 | 0.0 / 0 |
| hub | 33694.345 (59329.213) | 43.208 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 2 / 6 | 0.0 / 0 |
| mam | 66552.922 (116770.618) | 43.005 | 1.011 | 42708 / 42688 | 0 (2097152.0) | 8 / 23 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61244 / 61248 | 0 (2097152.0) | 4 / 1 | 0.0 / 0 |
| map | 398730.989 (626744.181) | 36.381 | 1.226 | 49164 / 46568 | 0 (2097152.0) | 265 / 185 | 0.0 / 0 |
| hud | 321687.916 (496177.503) | 35.167 | 1.284 | 75624 / 73104 | 0 (2097152.0) | 192 / 254 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 1 / 1 | 0.0 / 0 |

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 24.689 / 0.329 | 1.453 / 28.0 | 2.447 | 2127 / 0 | 2.051 (2.257) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 3.405 / 0.002 | 1.453 / 12.0 | 2.447 | 274 / 0 | 1.46 (1.343) |
| sift | indirect, one multi-draw per arena and layer | 3.008 | 430.208 | 16 (vanilla 28) | 462656 | 22.841 / 0.351 | 7.389 / 16.0 | 12.354 | 3135 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 118460 | 22.233 / 0.34 | 1.886 / 12.0 | 3.163 | 3215 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1192732 | 23.053 / 0.365 | 20.297 / 32.0 | 31.849 | 3868 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1021532 | 23.053 / 0.365 | 17.586 / 32.0 | 27.278 | 3869 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.011 | 560.106 | 16 (vanilla 28) | 1751684 | 20.118 / 0.29 | 27.961 / 40.0 | 46.775 | 4725 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.008 | 444.16 | 16 (vanilla 28) | 1021832 | 16.179 / 0.246 | 16.26 / 56.0 | 27.286 | 6385 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 42676 | 16.035 / 0.243 | 0.687 / 56.0 | 1.14 | 6488 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2136 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 34.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 9044 / 0 / 0 |
| hud | 40.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 15320 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.034; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.007; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 285.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 96.25 / 0.0 | 0.512 / 0.567 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.848 / 11.055 | 0.016 / 0.006 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 94.04 / 0.0 | 0.604 / 0.576 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.665 / 11.615 | 0.018 / 0.004 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.0 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 95.563 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 103.048 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.323 / 0.0 | 0.005 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.86 | 0.0 / 0.0 | 574.61 / 574.19 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.86 (8.0 / 8.0) |
| mam | 13.86 / 13.88 | 1.0 / 1.0 | 113.0 / 113.68 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.88 (8.0 / 8.0) |
| ponder | 82.59 / 82.56 | 0.11 / 0.11 | 203.93 / 203.83 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.62 / 81.6 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

## Bench-only jar (quicksilver-bench)

| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |
|---|---|---|---|---|---|---|---|
| bench-plain | spawn | 9.24 | 132.42 | 184 | 124 | 540432 | [] |
| bench-plain | flythrough | 5.35 | 381.52 | 107 | 123 | 7639584 | [] |
| bench-plain | water_glass | 15.88 | 75.17 | 317 | 124 | 17740 | [] |
| bench-plain | nether | 15.85 | 73.93 | 317 | 121 | 423160 | [] |
| bench-sodium | spawn | 10.98 | 112.81 | 219 | 148 | 8144 | [] |
| bench-sodium | flythrough | 7.32 | 278.35 | 146 | 159 | 266476 | [] |
| bench-sodium | water_glass | 20.01 | 59.55 | 151 | 135 | 9564 | [] |
| bench-sodium | nether | 27.47 | 44.12 | 0 | 129 | 263664 | [] |

