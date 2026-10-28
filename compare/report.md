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
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=3.3 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=no GL_ARB_buffer_storage=no deviceType=CPU vanilla.multiDrawIndirect=no vanilla.persistentMapping=no vanilla.mdiKnownIssues=yes`
- measure-vk: backend `Vulkan` drivers `1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)`
  - probe: `gpuVendor="0x10005" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="1.4.318 llvmpipe Mesa 25.2.8-0ubuntu0.24.04.4 (LLVM 20.1.2)" gl=n/a backend=Vulkan os="Linux 6.17.0-1022-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=n/a GL_ARB_buffer_storage=n/a deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`

GPU error messages in the client logs (Brimstone run / vanilla run of the same profile):

- full: 0 / 0
- old-gpu: 0 / 0
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| spawn | 0.3008 | 10.05 | 0.4384 | 0.4429 | 0.3073 |
| flythrough | 0.0 | 14.95 | 0.0012 | 0.0012 | 0.0011 |
| water_glass | 0.0047 | 6.45 | 0.0039 | 0.0031 | 0.0051 |
| nether | 0.0024 | 0.8 | 0.0003 | 0.0003 | 0.0013 |
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
| translucent_move | 0.0001 | 0.3 | 0.0001 | 0.0 | 0.0001 |
| translucent_move.x2404 | 0.0002 | 0.3 | 0.0005 | 0.0002 | 0.0007 |
| translucent_move.x2414 | 0.0002 | 0.3 | 0.0002 | 0.0002 | 0.0002 |
| translucent_move.x2424 | 0.0002 | 0.3 | 0.0004 | 0.0001 | 0.0003 |
| translucent_move.low | 0.0003 | 0.3 | 0.0008 | 0.0011 | 0.001 |
| translucent_move.back | 0.0 | 0.3 | 0.0 | 0.0001 | 0.0001 |
| translucent_move.bare_back | 0.0003 | 0.3 | 0.0002 | 0.0001 | 0.0001 |
| water_glass_oit | 0.004 | 0.3 | 0.0038 | 0.0039 | 0.0043 |
| translucent_move_oit | 0.0 | 0.3 | 0.0 | 0.0 | 0.0001 |
| translucent_move_oit.x2404 | 0.0002 | 0.3 | 0.0003 | 0.0003 | 0.0003 |
| translucent_move_oit.x2414 | 0.0002 | 0.3 | 0.0001 | 0.0002 | 0.0002 |
| translucent_move_oit.x2424 | 0.0 | 0.3 | 0.0001 | 0.0003 | 0.0001 |
| translucent_move_oit.low | 0.0004 | 0.3 | 0.0009 | 0.0012 | 0.001 |
| translucent_move_oit.back | 0.0001 | 0.3 | 0.0001 | 0.0 | 0.0001 |
| translucent_move_oit.bare_back | 0.0002 | 0.3 | 0.0001 | 0.0 | 0.0003 |
| flat_plants | 0.0 | 0.3 | 0.023 | 0.023 | 0.0226 |
| renderer_off | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| particles | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| particles.bare | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| animation | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| animation.behind | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| animation.front | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| animated_items | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| spawn | 92.75 | 18057 | 1509 (2017) | 1580 (2086) | 291 (812) |
| flythrough | 97.28 | 6020 | 50 (296) | 48 (294) | 51 (292) |
| water_glass | 95.81 | 25508 | 10 (2609) | 13 (2611) | 26 (2739) |
| nether | 99.42 | 905 | 3 (458) | 3 (458) | 12 (479) |
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
| translucent_move | 96.93 | 13 | 1 (76) | 2 (77) | 3 (69) |
| translucent_move.x2404 | 95.93 | 15 | 4 (104) | 3 (103) | 5 (90) |
| translucent_move.x2414 | 95.86 | 13 | 5 (108) | 2 (105) | 3 (100) |
| translucent_move.x2424 | 96.74 | 13 | 1 (78) | 2 (79) | 3 (69) |
| translucent_move.low | 96.11 | 41 | 12 (17) | 13 (18) | 13 (15) |
| translucent_move.back | 96.93 | 13 | 2 (77) | 1 (76) | 3 (69) |
| translucent_move.bare_back | 96.9 | 13 | 2 (77) | 3 (78) | 2 (68) |
| water_glass_oit | 95.81 | 57 | 17 (2616) | 18 (2617) | 16 (2729) |
| translucent_move_oit | 96.93 | 12 | 2 (78) | 1 (76) | 2 (69) |
| translucent_move_oit.x2404 | 95.93 | 16 | 4 (104) | 3 (103) | 5 (89) |
| translucent_move_oit.x2414 | 95.86 | 10 | 1 (104) | 3 (106) | 1 (98) |
| translucent_move_oit.x2424 | 96.74 | 15 | 2 (79) | 3 (80) | 1 (67) |
| translucent_move_oit.low | 96.11 | 40 | 13 (18) | 12 (17) | 13 (15) |
| translucent_move_oit.back | 96.93 | 12 | 3 (78) | 2 (77) | 1 (67) |
| translucent_move_oit.bare_back | 96.9 | 15 | 2 (78) | 2 (77) | 2 (68) |
| flat_plants | 98.22 | 350 | 225 (7416) | 225 (7416) | 217 (7347) |
| renderer_off | 99.2 | 10 | 0 (0) | 0 (0) | 0 (0) |
| particles | 99.99 | 4 | 0 (0) | 0 (0) | 0 (0) |
| particles.bare | 99.99 | 4 | 0 (0) | 0 (0) | 0 (0) |
| animation | 99.62 | 10 | 1 (3) | 1 (3) | 1 (3) |
| animation.behind | 99.74 | 10 | 0 (2) | 0 (2) | 0 (1) |
| animation.front | 99.62 | 10 | 1 (3) | 1 (3) | 1 (3) |
| animated_items | 98.9 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| spawn | 0.1978 / 1558 | 0.2044 / 1518 | 0.3013 / 318 |
| flythrough | 0.0 / 0 | 0.0001 / 0 | 0.0 / 0 |
| water_glass | 0.0048 / 20 | 0.0035 / 17 | 0.0049 / 23 |
| nether | 0.0023 / 21 | 0.0107 / 97 | 0.0021 / 19 |
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
| translucent_move | 0.0002 / 1 | 0.0 / 1 | 0.0 / 1 |
| translucent_move.x2404 | 0.0003 / 2 | 0.0001 / 1 | 0.0003 / 3 |
| translucent_move.x2414 | 0.0001 / 1 | 0.0002 / 3 | 0.0 / 1 |
| translucent_move.x2424 | 0.0002 / 1 | 0.0001 / 1 | 0.0001 / 2 |
| translucent_move.low | 0.0002 / 2 | 0.0007 / 6 | 0.0002 / 3 |
| translucent_move.back | 0.0002 / 2 | 0.0003 / 2 | 0.0001 / 1 |
| translucent_move.bare_back | 0.0001 / 1 | 0.0001 / 1 | 0.0 / 1 |
| water_glass_oit | 0.0029 / 13 | 0.004 / 18 | 0.0037 / 14 |
| translucent_move_oit | 0.0 / 1 | 0.0 / 1 | 0.0002 / 2 |
| translucent_move_oit.x2404 | 0.0001 / 0 | 0.0003 / 3 | 0.0001 / 1 |
| translucent_move_oit.x2414 | 0.0001 / 3 | 0.0002 / 3 | 0.0001 / 2 |
| translucent_move_oit.x2424 | 0.0001 / 1 | 0.0 / 1 | 0.0 / 0 |
| translucent_move_oit.low | 0.0007 / 8 | 0.0005 / 2 | 0.0003 / 2 |
| translucent_move_oit.back | 0.0002 / 1 | 0.0 / 0 | 0.0002 / 2 |
| translucent_move_oit.bare_back | 0.0003 / 3 | 0.0002 / 3 | 0.0002 / 0 |
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
| spawn | 13.0 (base 13.6) | 14 (base 14) | 25536 (base 22476) | 261.01 (base 259.07) | 3.99 (base 3.99) |
| flythrough | 13.07 (reported, not gated) | 42 (reported, not gated) | 779012 (reported, not gated) | 316.85 (base 324.25) | 3.48 (base 4.24) |
| water_glass | 9.0 (base 8.97) | 15 (base 15) | 15060 (base 15576) | 245.35 (base 250.02) | 3.99 (base 5.99) |
| nether | 15.87 (base 15.63) | 12 (base 12) | 21068 (base 19648) | 266.13 (base 273.53) | 3.99 (base 5.99) |
| mobs | 19.88 (base 19.77) | 17 (base 17) | 616324 (base 1010948) | 264.83 (base 268.37) | 3.99 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6336 (base 6356) | 264.64 (base 264.69) | 3.99 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100652 (base 126320) | 270.27 (base 265.1) | 3.99 (base 4.24) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82544 (reported, not gated) | 270.71 (base 265.74) | 3.99 (base 4.24) |
| translucent_move | 9.0 (reported, not gated) | 15 (reported, not gated) | 10800 (reported, not gated) | 272.55 (base 267.48) | 5.99 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15956 (base 14688) | 244.41 (base 245.02) | 3.35 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 11004 (reported, not gated) | 246.61 (base 247.19) | 3.48 (base 3.73) |
| flat_plants | 18.79 (base 18.29) | 15 (base 15) | 10020 (base 9812) | 247.06 (base 246.92) | 3.73 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7352 (base 7164) | 239.53 (base 241.33) | 3.6 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 267.95 (base 261.44) | 5.99 (base 5.99) |
| animation | 49 median (avg 39.95) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.18 (base 260.68) | 5.99 (base 5.99) |
| animated_items | 60 median (avg 46.77) (base 60) | 24 (base 23) | 22136 (base 21940) | 240.12 (base 239.81) | 5.99 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (9.55) | 123.0 / 123 (7.93) | median and average, x 1.10 + 2 |
| flythrough | 13.07 / 13 (6.47) | 123.05 / 123 (4.63) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (18.67) | 124.0 / 124 (13.5) | median, at most vanilla |
| nether | 15.87 / 16 (18.42) | 120.94 / 121 (13.47) | median, x 1.10 + 2 |
| mobs | 19.88 / 20 (22.74) | 134.0 / 134 (16.87) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.27 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.4 / 133 (29.93) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.92) | 92.72 / 131 (29.85) | median and average, x 1.10 + 2 |
| translucent_move | 9.0 / 9 (18.62) | 124.0 / 124 (13.74) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (10.36) | 129.0 / 129 (7.77) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (9.22) | 129.0 / 129 (7.09) | median and average, at most vanilla |
| flat_plants | 18.79 / 19 (19.03) | 123.66 / 124 (13.66) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.71) | 111.01 / 123 (22.28) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.32 / 125 (30.01) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 144.46 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.0) | 155.22 / 190 (29.99) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.91 | 36.0 / 22.91 | 0.0 / 0.0 | 416.0 / 37.063 |
| flythrough | 112.0 / 84.335 | 112.0 / 84.335 | 0.0 / 0.0 | 416.0 / 143.743 |
| water_glass | 36.0 / 12.378 | 36.0 / 12.378 | 0.0 / 0.0 | 416.0 / 21.772 |
| nether | 20.0 / 15.057 | 20.0 / 15.057 | 0.0 / 0.0 | 256.0 / 26.895 |
| mobs | 16.0 / 9.672 | 16.0 / 9.672 | 0.0 / 0.0 | 256.0 / 16.897 |
| outline | 16.0 / 1.231 | 16.0 / 1.231 | 0.0 / 0.0 | 256.0 / 2.174 |
| occlusion | 20.0 / 2.461 | 20.0 / 2.461 | 0.0 / 0.0 | 416.0 / 4.263 |
| occlusion_move | 20.0 / 3.302 | 20.0 / 3.302 | 0.0 / 0.0 | 416.0 / 5.97 |
| translucent_move | 28.0 / 12.878 | 28.0 / 12.878 | 0.0 / 0.0 | 416.0 / 22.533 |
| water_glass_oit | 20.0 / 12.378 | 20.0 / 12.378 | 0.0 / 0.0 | 416.0 / 21.465 |
| translucent_move_oit | 28.0 / 12.522 | 28.0 / 12.522 | 0.0 / 0.0 | 416.0 / 21.811 |
| flat_plants | 24.0 / 13.872 | 24.0 / 13.872 | 0.0 / 0.0 | 416.0 / 24.172 |
| renderer_off | 24.0 / 8.445 | 24.0 / 8.445 | 0.0 / 0.0 | 416.0 / 14.933 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core full: Brimstone's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Brimstone / vanilla | Brimstone terrain bytes, largest frame (budget avg) | frames over 50 ms: Brimstone / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 515853.021 (695879.568) | 25.87 | 1.154 | 338368 / 577316 | 312832 (2097152.0) | 190 / 158 | 0.0 / 0 |
| flythrough | 552801.395 (886411.798) | 37.636 | 1.082 | 2357400 / 7926540 | 2097088 (2097152.0) | 129 / 92 | 8.885 / 71 |
| water_glass | 215482.155 (386076.29) | 44.187 | 1.118 | 155268 / 259476 | 140096 (2097152.0) | 371 / 270 | 0.0 / 0 |
| nether | 316820.158 (474822.348) | 33.276 | 1.272 | 259772 / 628360 | 238592 (2097152.0) | 368 / 269 | 0.0 / 0 |
| mobs | 167502.137 (303474.978) | 44.805 | 0.99 | 756420 / 616364 | 140096 (2097152.0) | 26 / 337 | 0.0 / 0 |
| outline | 23378.965 (38742.571) | 39.656 | 1.0 | 6336 / 6336 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 43971.619 (76780.301) | 42.731 | 1.211 | 410220 / 331920 | 309568 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 48702.816 (85224.525) | 42.854 | 1.236 | 678628 / 1372944 | 601728 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 200605.989 (356099.102) | 43.666 | 1.183 | 766308 / 1334524 | 756480 (2097152.0) | 328 / 274 | 0.0 / 0 |
| water_glass_oit | 215952.232 (386912.155) | 44.186 | 1.118 | 17412 / 17780 | 0 (2097152.0) | 207 / 155 | 0.0 / 0 |
| translucent_move_oit | 200784.587 (356171.783) | 43.627 | 1.181 | 735120 / 1278564 | 724864 (2097152.0) | 184 / 141 | 0.0 / 0 |
| flat_plants | 237068.226 (434157.521) | 45.396 | 1.06 | 272052 / 271620 | 0 (2097152.0) | 373 / 273 | 0.0 / 0 |
| renderer_off | 147169.343 (266503.906) | 44.778 | 1.02 | 7352 / 7272 | 0 (2097152.0) | 0 / 11 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9876 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86832 / 150176 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79772 / 122908 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 807.226 | 16 (vanilla 28) | 1384772 | 39.823 / 0.111 | 22.91 / 36.0 | 36.977 | 2354 / 0 | 4.434 (7.689) |
| flythrough | indirect, one multi-draw per arena and layer | 3.023 | 895.171 | 16 (vanilla 28) | 5096412 | 15.211 / 0.993 | 84.335 / 112.0 | 136.089 | 6421 / 0 | 8.237 (19.835) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 272.729 | 16 (vanilla 28) | 770088 | 32.231 / 0.096 | 12.378 / 36.0 | 20.564 | 3018 / 0 | 4.575 (3.793) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 472.28 | 16 (vanilla 28) | 947088 | 27.113 / 0.187 | 15.057 / 20.0 | 25.29 | 4049 / 0 | 3.761 (3.774) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 198.436 | 16 (vanilla 28) | 605616 | 24.587 / 0.163 | 9.672 / 16.0 | 16.172 | 4496 / 0 | 5.93 (5.152) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 43.072 | 16 (vanilla 28) | 77356 | 24.181 / 0.162 | 1.231 / 16.0 | 2.066 | 4594 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 71.119 | 16 (vanilla 28) | 153316 | 22.942 / 0.153 | 2.461 / 20.0 | 4.094 | 4862 / 0 | 3.676 (4.971) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 76.441 | 16 (vanilla 28) | 205960 | 21.619 / 0.144 | 3.302 / 20.0 | 5.5 | 5166 / 0 | 5.202 (13.395) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.008 | 248.476 | 16 (vanilla 28) | 795708 | 18.859 / 0.132 | 12.878 / 28.0 | 21.248 | 5936 / 0 | 7.492 (17.292) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.024 | 281.353 | 16 (vanilla 28) | 770104 | 7.507 / 0.042 | 12.378 / 20.0 | 20.564 | 1081 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.027 | 263.375 | 16 (vanilla 28) | 773128 | 7.443 / 0.208 | 12.522 / 28.0 | 20.645 | 2533 / 0 | 10.324 (25.844) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.008 | 283.745 | 16 (vanilla 28) | 866036 | 6.337 / 0.01 | 13.872 / 24.0 | 23.126 | 940 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.345 | 16 (vanilla 28) | 532112 | 2.463 / 0.021 | 8.445 / 24.0 | 14.209 | 598 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 18.794 / 0.132 | 0.131 / 28.0 | 0.225 | 5984 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 18.635 / 0.131 | 0.302 / 28.0 | 0.518 | 6086 / 0 | 1.672 (2.029) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.579 / 0.131 | 0.153 / 28.0 | 0.261 | 6136 / 0 | 1.374 (1.961) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 155.0 | 1.0 | 0.0 | 0 | 0 | 46 (0.027 / 0.048 / 0.063; 0) | 0 | 29605 / 0 / 0 |
| flythrough | 186.4 | 1.0 | 0.0 | 0 | 0 | 843 (0.03 / 0.05 / 6.078; 0) | 48 | 24232 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1496 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 38 (0.008 / 0.011 / 0.018; 0) | 34 | 1198 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.117 / 0.159 / 2.585; 0) | 180 | 2611 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 624 | 0 (None / None / None; 0) | 0 | 832 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 555 | 0 (None / None / None; 0) | 0 | 1295 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1524 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2380 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 351.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.03 (0.08; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 433.285 | 1.0 (0.0, 0.0) | 1.254 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.043; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.139 (0.298; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.071 (0.118; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.356 | 10.0 (9.0, 2.366) | 6.0 (6.0, 2.279) | 0.065 (0.275; 0) | 0 | 2782 / 1506596 / 0 / 0 |
| translucent_move | 135.075 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.595 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 151.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.009 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 84.915 / 0.0 | 0.028 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.18 / 0.0 | 0.02 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.823 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 49.327 / 49.302 | 0.01 / 0.024 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.941 / 0.872 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 49.796 / 8.276 | 0.079 / 0.011 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.731 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 77.052 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 9.915 / 9.861 | 0.004 / 0.016 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.075 / 54.962 | 0.013 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.92 / 12.805 | 0.004 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.663 / 0.789 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.155 / 1.997 | 0.015 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 103.112 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.009 / 0.067 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.004 / 0.013 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.983 |
| animated_items | 136.553 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

GUI pass, core full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

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
| spawn | 49.43 (base 49.0) | 13 (base 13) | 1492 (base 6372) | 261.04 (base 256.29) | 4.24 (base 3.73) |
| flythrough | 66.81 (reported, not gated) | 47 (reported, not gated) | 592652 (reported, not gated) | 314.92 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 20.0 (base 20.0) | 13 (base 14) | 7092 (base 10264) | 245.32 (base 249.83) | 4.24 (base 5.99) |
| nether | 33.91 (base 33.79) | 10 (base 11) | 4644 (base 10656) | 266.09 (base 272.57) | 4.24 (base 5.99) |
| mobs | 28.88 (base 28.97) | 14 (base 16) | 610516 (base 1015892) | 264.58 (base 268.23) | 4.24 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 264.39 (base 264.41) | 4.24 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 270.01 (base 264.64) | 4.24 (base 4.24) |
| occlusion_move | 24.03 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 270.58 (base 265.66) | 4.24 (base 5.99) |
| translucent_move | 17.99 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 272.41 (base 268.19) | 4.24 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7204 (base 7604) | 244.1 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 3060 (reported, not gated) | 246.76 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 33.0 (base 33.0) | 13 (base 13) | 1604 (base 1716) | 247.07 (base 248.11) | 3.48 (base 5.99) |
| renderer_off | 17.68 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 239.59 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 267.72 (base 261.4) | 4.24 (base 4.24) |
| animation | 49 median (avg 39.95) (base 49) | 13 (base 13) | 1716 (base 1716) | 265.1 (base 260.68) | 4.24 (base 4.24) |
| animated_items | 60 median (avg 46.77) (base 60) | 21 (base 22) | 21508 (base 21668) | 240.24 (base 239.53) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.43 / 49 (10.96) | 847.0 / 847 (10.28) | median and average, x 1.10 + 2 |
| flythrough | 66.81 / 64 (6.73) | 1003.01 / 1007 (5.9) | median and average, at most vanilla |
| water_glass | 20.0 / 20 (19.74) | 389.0 / 389 (17.08) | median, at most vanilla |
| nether | 33.91 / 34 (18.73) | 589.97 / 590 (16.68) | median, x 1.10 + 2 |
| mobs | 28.88 / 29 (22.74) | 320.65 / 330 (21.72) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 128.27 / 167 (30.0) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (30.01) | 162.33 / 201 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 24.03 / 24 (29.87) | 167.6 / 202 (29.98) | median and average, x 1.10 + 2 |
| translucent_move | 17.99 / 18 (18.96) | 368.81 / 368 (17.18) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (9.44) | 400.0 / 400 (8.6) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (9.13) | 385.85 / 387 (8.29) | median and average, at most vanilla |
| flat_plants | 33.0 / 33 (18.73) | 401.89 / 402 (17.26) | median, x 1.10 + 2 |
| renderer_off | 17.68 / 18 (29.52) | 300.52 / 325 (25.38) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.33 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 156.46 / 191 (29.99) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.0) | 167.22 / 202 (29.99) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 22.663 | 40.0 / 22.663 | 0.0 / 0.0 | 416.0 / 36.702 |
| flythrough | 112.0 / 83.794 | 112.0 / 83.794 | 0.0 / 0.0 | 416.0 / 144.229 |
| water_glass | 40.0 / 12.378 | 40.0 / 12.378 | 0.0 / 0.0 | 416.0 / 21.465 |
| nether | 20.0 / 15.054 | 20.0 / 15.054 | 0.0 / 0.0 | 256.0 / 26.453 |
| mobs | 16.0 / 9.672 | 16.0 / 9.672 | 0.0 / 0.0 | 256.0 / 16.897 |
| outline | 16.0 / 1.231 | 16.0 / 1.231 | 0.0 / 0.0 | 256.0 / 2.174 |
| occlusion | 20.0 / 2.478 | 20.0 / 2.478 | 0.0 / 0.0 | 416.0 / 4.253 |
| occlusion_move | 20.0 / 3.32 | 20.0 / 3.32 | 0.0 / 0.0 | 416.0 / 5.733 |
| translucent_move | 28.0 / 12.885 | 28.0 / 12.885 | 0.0 / 0.0 | 416.0 / 22.377 |
| water_glass_oit | 20.0 / 12.378 | 20.0 / 12.378 | 0.0 / 0.0 | 416.0 / 21.465 |
| translucent_move_oit | 28.0 / 12.527 | 28.0 / 12.527 | 0.0 / 0.0 | 416.0 / 21.662 |
| flat_plants | 24.0 / 13.891 | 24.0 / 13.891 | 0.0 / 0.0 | 416.0 / 24.184 |
| renderer_off | 24.0 / 8.445 | 24.0 / 8.445 | 0.0 / 0.0 | 416.0 / 14.876 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core old-gpu: Brimstone's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Brimstone / vanilla | Brimstone terrain bytes, largest frame (budget avg) | frames over 50 ms: Brimstone / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 448234.119 (687504.676) | 34.803 | 1.769 | 591636 / 787716 | 590256 (2097152.0) | 219 / 205 | 0.0 / 0 |
| flythrough | 551061.612 (883426.328) | 37.622 | 1.082 | 2105148 / 7465428 | 2094840 (2097152.0) | 134 / 118 | 7.963 / 53 |
| water_glass | 215451.447 (386021.269) | 44.187 | 1.118 | 9332 / 254228 | 0 (2097152.0) | 191 / 341 | 0.0 / 0 |
| nether | 267373.155 (474838.396) | 43.692 | 1.701 | 231700 / 454512 | 226048 (2097152.0) | 373 / 333 | 0.0 / 0 |
| mobs | 167501.327 (303473.512) | 44.805 | 0.99 | 750612 / 857572 | 140096 (2097152.0) | 14 / 16 | 0.0 / 0 |
| outline | 23378.965 (38742.571) | 39.656 | 1.0 | 5012 / 5476 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 44315.679 (77468.935) | 42.796 | 1.214 | 196808 / 124924 | 98752 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 49023.849 (85874.673) | 42.912 | 1.239 | 676796 / 1202540 | 601728 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 200836.923 (356514.865) | 43.667 | 1.184 | 758964 / 1327764 | 756352 (2097152.0) | 312 / 343 | 0.0 / 0 |
| water_glass_oit | 216049.117 (387092.106) | 44.187 | 1.118 | 8772 / 255236 | 0 (2097152.0) | 188 / 172 | 0.0 / 0 |
| translucent_move_oit | 201121.505 (356777.923) | 43.628 | 1.181 | 729780 / 1272788 | 724864 (2097152.0) | 182 / 165 | 0.0 / 0 |
| flat_plants | 237515.374 (435066.176) | 45.407 | 1.06 | 1828 / 4228 | 0 (2097152.0) | 373 / 345 | 0.0 / 0 |
| renderer_off | 147169.017 (266502.936) | 44.778 | 1.02 | 1380 / 3172 | 0 (2097152.0) | 0 / 4 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 8772 / 8964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86324 / 149896 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79144 / 122900 | 57728 (2097147.642) | 0 / 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.178 | 792.603 | 16 (vanilla 28) | 1369052 | 43.407 / 0.159 | 22.663 / 40.0 | 36.558 | 2756 / 0 | 5.929 (8.707) |
| flythrough | plain, one multi-draw per region and layer | 57.119 | 897.104 | 16 (vanilla 28) | 5065168 | 15.352 / 1.002 | 83.794 / 112.0 | 135.255 | 6458 / 0 | 8.76 (21.28) |
| water_glass | plain, one multi-draw per region and layer | 14.036 | 272.69 | 16 (vanilla 28) | 770088 | 35.763 / 0.134 | 12.378 / 40.0 | 20.564 | 3390 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.053 | 472.259 | 16 (vanilla 28) | 947232 | 30.232 / 0.213 | 15.054 / 20.0 | 25.294 | 4441 / 0 | 5.033 (6.215) |
| mobs | plain, one multi-draw per region and layer | 11.024 | 198.435 | 16 (vanilla 28) | 605616 | 27.419 / 0.186 | 9.672 / 16.0 | 16.172 | 4901 / 0 | 5.68 (4.918) |
| outline | plain, one multi-draw per region and layer | 11.018 | 43.072 | 16 (vanilla 28) | 77356 | 27.017 / 0.185 | 1.231 / 16.0 | 2.066 | 4996 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 70.117 | 16 (vanilla 28) | 154680 | 25.94 / 0.178 | 2.478 / 20.0 | 4.13 | 5223 / 0 | 3.331 (2.769) |
| occlusion_move | plain, one multi-draw per region and layer | 10.987 | 75.425 | 16 (vanilla 28) | 207308 | 24.449 / 0.167 | 3.32 / 20.0 | 5.536 | 5546 / 0 | 5.906 (18.665) |
| translucent_move | plain, one multi-draw per region and layer | 12.032 | 247.369 | 16 (vanilla 28) | 796816 | 21.169 / 0.15 | 12.885 / 28.0 | 21.277 | 6383 / 0 | 7.478 (15.936) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.085 | 281.489 | 16 (vanilla 28) | 770088 | 7.491 / 0.041 | 12.378 / 20.0 | 20.564 | 1085 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.615 | 262.621 | 16 (vanilla 28) | 774252 | 6.587 / 0.106 | 12.527 / 28.0 | 20.675 | 2408 / 0 | 9.225 (19.439) |
| flat_plants | plain, one multi-draw per region and layer | 17.045 | 282.754 | 16 (vanilla 28) | 867812 | 6.297 / 0.01 | 13.891 / 24.0 | 23.173 | 944 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.022 | 205.347 | 16 (vanilla 28) | 532104 | 2.429 / 0.021 | 8.445 / 24.0 | 14.209 | 604 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 21.094 / 0.149 | 0.131 / 28.0 | 0.225 | 6431 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.927 / 0.148 | 0.302 / 28.0 | 0.518 | 6533 / 0 | 1.524 (1.937) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.868 / 0.148 | 0.153 / 28.0 | 0.261 | 6583 / 0 | 1.214 (1.653) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 154.0 | 17.0 | 0.0 | 0 | 0 | 50 (0.074 / 0.041 / 2.514; 0) | 0 | 33880 / 0 / 0 |
| flythrough | 187.956 | 26.126 | 0.0 | 0 | 0 | 857 (0.034 / 0.048 / 6.069; 0) | 49 | 25374 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1580 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.01 / 0.017; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.127 / 0.182 / 2.456; 0) | 180 | 2660 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 567 | 0 (None / None / None; 0) | 0 | 756 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 549 | 0 (None / None / None; 0) | 0 | 1281 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1500 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2364 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 344.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.034 (0.751; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 432.163 | 1.0 (0.0, 0.0) | 1.296 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.018 (0.058; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.14 (0.311; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.068 (0.132; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.351 | 10.0 (9.0, 2.36) | 6.0 (6.0, 2.281) | 0.06 (0.3; 0) | 0 | 2775 / 1502228 / 0 / 0 |
| translucent_move | 135.026 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.831 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 151.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.009 (0.05; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.429 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 102.6 / 0.432 | 0.028 / 0.029 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.053 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.822 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 50.733 / 50.714 | 0.017 / 0.021 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.967 / 0.912 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 46.885 / 10.088 | 0.081 / 0.012 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 105.726 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.975 / 11.926 | 0.004 / 0.014 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.078 / 52.026 | 0.011 / 0.021 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.463 / 13.546 | 0.006 / 0.011 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.887 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.145 / 2.029 | 0.015 / 0.01 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 90.73 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.056 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.005 / 0.014 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.983 |
| animated_items | 136.553 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

GUI pass, core old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

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
| spawn | 13.0 (base 14.05) | 14 (base 15) | 25480 (base 27764) | 260.61 (base 257.2) | 3.98 (base 3.98) |
| flythrough | 13.06 (reported, not gated) | 42 (reported, not gated) | 733864 (reported, not gated) | 314.01 (base 318.06) | 3.46 (base 4.23) |
| water_glass | 9.0 (base 9.0) | 15 (base 15) | 15956 (base 15108) | 245.32 (base 244.39) | 3.98 (base 4.23) |
| nether | 15.93 (base 15.71) | 12 (base 12) | 21404 (base 19912) | 265.88 (base 268.95) | 3.98 (base 4.23) |
| mobs | 19.85 (base 19.97) | 17 (base 17) | 616324 (base 1008176) | 265.05 (base 264.31) | 3.98 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6336 (base 6356) | 264.22 (base 264.58) | 3.98 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100612 (base 126356) | 269.61 (base 264.78) | 3.98 (base 3.98) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82504 (reported, not gated) | 270.28 (base 265.16) | 3.98 (base 4.23) |
| translucent_move | 9.0 (reported, not gated) | 15 (reported, not gated) | 10612 (reported, not gated) | 272.63 (base 268.35) | 5.98 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15508 (base 15024) | 244.82 (base 245.99) | 3.34 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 11284 (reported, not gated) | 247.12 (base 248.38) | 3.46 (base 3.46) |
| flat_plants | 18.89 (base 18.96) | 15 (base 15) | 10036 (base 9864) | 247.53 (base 248.07) | 3.2 (base 5.98) |
| renderer_off | 7.68 (base 7.83) | 14 (base 14) | 7352 (base 7180) | 239.93 (base 241.13) | 3.07 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 267.49 (base 261.4) | 5.98 (base 5.98) |
| animation | 49 median (avg 39.94) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.05 (base 260.68) | 5.98 (base 5.98) |
| animated_items | 60 median (avg 46.73) (base 60) | 24 (base 23) | 22136 (base 21940) | 240.21 (base 239.74) | 5.98 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (9.91) | 123.67 / 124 (11.29) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (6.31) | 123.06 / 123 (7.28) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (18.42) | 124.0 / 124 (18.15) | median, at most vanilla |
| nether | 15.93 / 16 (17.76) | 120.77 / 121 (18.43) | median, x 1.10 + 2 |
| mobs | 19.85 / 20 (23.38) | 117.57 / 134 (23.27) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.27 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.27 / 133 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.93) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| translucent_move | 9.0 / 9 (18.64) | 123.39 / 124 (19.16) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (9.35) | 129.0 / 129 (10.01) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (8.55) | 129.0 / 129 (9.42) | median and average, at most vanilla |
| flat_plants | 18.89 / 19 (19.34) | 123.91 / 124 (19.46) | median, x 1.10 + 2 |
| renderer_off | 7.68 / 8 (29.35) | 89.5 / 123 (28.1) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.33 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.01) | 155.22 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 23.237 | 36.0 / 23.237 | 0.0 / 0.0 | 416.0 / 39.303 |
| flythrough | 112.0 / 84.131 | 112.0 / 84.131 | 0.0 / 0.0 | 416.0 / 145.257 |
| water_glass | 36.0 / 12.717 | 36.0 / 12.717 | 0.0 / 0.0 | 416.0 / 21.465 |
| nether | 20.0 / 15.138 | 20.0 / 15.138 | 0.0 / 0.0 | 256.0 / 26.451 |
| mobs | 16.0 / 9.672 | 16.0 / 9.672 | 0.0 / 0.0 | 256.0 / 16.897 |
| outline | 16.0 / 1.231 | 16.0 / 1.231 | 0.0 / 0.0 | 256.0 / 2.174 |
| occlusion | 20.0 / 2.599 | 20.0 / 2.599 | 0.0 / 0.0 | 416.0 / 4.482 |
| occlusion_move | 20.0 / 3.427 | 20.0 / 3.427 | 0.0 / 0.0 | 416.0 / 5.896 |
| translucent_move | 28.0 / 13.051 | 28.0 / 13.051 | 0.0 / 0.0 | 416.0 / 22.462 |
| water_glass_oit | 20.0 / 12.378 | 20.0 / 12.378 | 0.0 / 0.0 | 416.0 / 21.465 |
| translucent_move_oit | 28.0 / 12.64 | 28.0 / 12.64 | 0.0 / 0.0 | 416.0 / 21.843 |
| flat_plants | 24.0 / 13.844 | 24.0 / 13.844 | 0.0 / 0.0 | 416.0 / 23.889 |
| renderer_off | 24.0 / 8.445 | 24.0 / 8.445 | 0.0 / 0.0 | 416.0 / 14.876 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core vulkan: Brimstone's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Brimstone / vanilla | Brimstone terrain bytes, largest frame (budget avg) | frames over 50 ms: Brimstone / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 515656.697 (695472.293) | 25.855 | 1.155 | 499728 / 848400 | 474248 (2097152.0) | 198 / 225 | 0.0 / 0 |
| flythrough | 549901.952 (882102.256) | 37.66 | 1.081 | 2122884 / 7801288 | 2096400 (2097152.0) | 125 / 145 | 9.548 / 77 |
| water_glass | 215489.984 (386090.315) | 44.187 | 1.118 | 17748 / 17108 | 0 (2097152.0) | 368 / 363 | 0.0 / 0 |
| nether | 316869.994 (474887.865) | 33.275 | 1.272 | 266524 / 408940 | 245568 (2097152.0) | 355 / 368 | 0.0 / 0 |
| mobs | 167491.889 (303456.411) | 44.805 | 0.99 | 796804 / 616364 | 180480 (2097152.0) | 19 / 12 | 0.0 / 0 |
| outline | 23378.965 (38742.571) | 39.656 | 1.0 | 6336 / 6336 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 46317.195 (81419.699) | 43.113 | 1.2 | 280168 / 126324 | 179648 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 50942.287 (89655.472) | 43.18 | 1.226 | 657504 / 1115884 | 580736 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 203537.688 (361624.952) | 43.716 | 1.187 | 766648 / 1332992 | 756352 (2097152.0) | 348 / 315 | 0.0 / 0 |
| water_glass_oit | 216061.409 (387114.129) | 44.187 | 1.118 | 17412 / 16548 | 0 (2097152.0) | 186 / 200 | 0.0 / 0 |
| translucent_move_oit | 203810.718 (361823.941) | 43.671 | 1.185 | 735476 / 1277928 | 724864 (2097152.0) | 170 / 188 | 0.0 / 0 |
| flat_plants | 236761.788 (433578.358) | 45.394 | 1.06 | 10372 / 10332 | 0 (2097152.0) | 300 / 322 | 0.0 / 0 |
| renderer_off | 147172.293 (266509.247) | 44.778 | 1.02 | 7352 / 7272 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86832 / 150268 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79864 / 123160 | 57728 (2097152.0) | 2 / 3 | 0.0 / 0 |

Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.015 | 805.045 | 16 (vanilla 28) | 1403832 | 42.203 / 0.099 | 23.237 / 36.0 | 37.486 | 2495 / 0 | 3.217 (5.213) |
| flythrough | indirect, one multi-draw per arena and layer | 3.024 | 890.4 | 16 (vanilla 28) | 5083016 | 15.383 / 1.014 | 84.131 / 112.0 | 135.731 | 6321 / 0 | 7.773 (19.384) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 272.739 | 16 (vanilla 28) | 789668 | 34.575 / 0.09 | 12.717 / 36.0 | 21.086 | 3166 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 472.327 | 16 (vanilla 28) | 952640 | 29.06 / 0.185 | 15.138 / 20.0 | 25.438 | 4217 / 0 | 2.886 (4.22) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 198.424 | 16 (vanilla 28) | 605616 | 26.327 / 0.161 | 9.672 / 16.0 | 16.172 | 4673 / 0 | 4.141 (3.961) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 43.072 | 16 (vanilla 28) | 77356 | 25.927 / 0.16 | 1.231 / 16.0 | 2.066 | 4768 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 162568 | 24.754 / 0.153 | 2.599 / 20.0 | 4.341 | 5008 / 0 | 3.816 (3.39) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.454 | 16 (vanilla 28) | 213880 | 23.339 / 0.144 | 3.427 / 20.0 | 5.711 | 5314 / 0 | 4.774 (13.308) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.008 | 247.53 | 16 (vanilla 28) | 806456 | 20.159 / 0.13 | 13.051 / 28.0 | 21.535 | 6156 / 0 | 5.475 (14.699) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.027 | 281.505 | 16 (vanilla 28) | 770088 | 7.492 / 0.042 | 12.378 / 20.0 | 20.564 | 1079 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.029 | 262.759 | 16 (vanilla 28) | 780600 | 6.541 / 0.106 | 12.64 / 28.0 | 20.844 | 2404 / 0 | 5.751 (11.471) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.008 | 283.733 | 16 (vanilla 28) | 864916 | 5.989 / 0.01 | 13.844 / 24.0 | 23.096 | 932 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.349 | 16 (vanilla 28) | 532112 | 2.472 / 0.021 | 8.445 / 24.0 | 14.209 | 596 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 20.086 / 0.129 | 0.131 / 28.0 | 0.225 | 6206 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.923 / 0.128 | 0.302 / 28.0 | 0.518 | 6308 / 0 | 1.561 (1.955) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.871 / 0.128 | 0.153 / 28.0 | 0.261 | 6355 / 0 | 0.943 (1.325) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 155.0 | 1.0 | 0.0 | 0 | 0 | 47 (0.027 / 0.047 / 0.059; 0) | 0 | 30845 / 0 / 0 |
| flythrough | 185.365 | 1.0 | 0.0 | 0 | 0 | 837 (0.047 / 0.053 / 10.063; 0) | 48 | 23356 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1476 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.01 / 0.01; 0) | 32 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.126 / 0.194 / 3.427; 0) | 180 | 2611 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 561 | 0 (None / None / None; 0) | 0 | 748 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 513 | 0 (None / None / None; 0) | 0 | 1197 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1548 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2352 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 350.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.025 (0.113; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 430.683 | 1.0 (0.0, 0.0) | 1.238 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.057; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.111 (0.181; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.069 (0.147; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.36 | 10.0 (9.0, 2.355) | 6.0 (6.0, 2.277) | 0.055 (0.284; 0) | 0 | 2779 / 1503986 / 0 / 0 |
| translucent_move | 135.131 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.877 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.036; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.667 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 87.413 / 0.0 | 0.021 / 0.003 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.131 / 0.0 | 0.003 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.817 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.685 / 55.653 | 0.01 / 0.017 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.769 / 0.93 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.434 / 11.017 | 0.103 / 0.009 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 98.712 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 114.399 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.68 / 10.611 | 0.004 / 0.01 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.64 / 52.658 | 0.008 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 14.345 / 14.187 | 0.005 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.912 / 0.889 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.513 / 2.38 | 0.014 / 0.008 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 81.791 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.009 / 0.062 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.27 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.01 | 56 (3) | 40.58 / 9.24 / 31.34 | 3 / 0 | 10.985 |
| animated_items | 136.553 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

GUI pass, core vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

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
| factory | 0.0002 | 0.3 | 0.0151 | 0.0 | 0.003 |
| factory_flat | 0.0009 | 0.3 | 0.0009 | 0.0034 | 0.0 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| hologram | 0.0219 | 0.55 | 0.0061 | 0.0258 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.59 | 500 | 0 (15) | 0 (15) | 0 (14) |
| factory_flat | 95.28 | 40 | 0 (14) | 18 (32) | 0 (14) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 87.91 | 10 | 0 (0) | 0 (20) | 0 (37) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| map | 99.99 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 99.97 | 413 | 48 (4298) | 43 (4294) | None (None) |
| hud | 44.33 | 10 | 0 (0) | 0 (0) | 0 (0) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0156 / 0 | 0.0146 / 0 | 0.0028 / 0 |
| factory_flat | 0.0009 / 0 | 0.0009 / 0 | 0.0 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| map | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0224 / 8 | 0.0018 / 4 | skipped |
| hud | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.18 (base 12.23) | 15 (base 14) | 6864 (base 6488) | 423.16 (base 421.74) | 4.24 (base 5.48) |
| factory_flat | 20.23 (base 20.25) | 15 (base 14) | 9940 (base 6264) | 422.61 (base 422.37) | 3.54 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 440.96 (base 440.07) | 4.24 (base 5.87) |
| hub | 21.55 (base 21.58) | 16 (base 18) | 66384 (base 67484) | 453.53 (base 451.33) | 4.24 (base 4.24) |
| mam | 22.52 (base 22.52) | 19 (base 21) | 23556 (base 24656) | 466.44 (base 471.8) | 6.24 (base 8.12) |
| ponder | 87.24 (base 87.25) | 14 (base 14) | 52268 (base 52248) | 465.33 (base 463.81) | 6.24 (base 8.24) |
| map | 30.0 (base 30.0) | 14 (base 14) | 48840 (base 47032) | 493.02 (base 504.13) | 10.06 (base 8.24) |
| hologram | 33 median (avg 32.83) (base 33) | 22 (base 24) | 72304 (base 70368) | 471.46 (base 453.57) | 10.12 (base 8.24) |
| hud | 31.0 (base 31.0) | 16 (base 16) | 74196 (base 71636) | 492.14 (base 494.92) | 10.12 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 481.33 (base 470.81) | 10.12 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.18 / 12 (29.79) | 108.6 / 152 (28.53) | median and average, x 1.10 + 2 |
| factory_flat | 20.23 / 20 (29.68) | 115.12 / 160 (29.5) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (16.11) | 153.0 / 153 (14.97) | median, x 1.10 + 2 |
| hub | 21.55 / 21 (29.39) | 115.41 / 161 (29.77) | median and average, x 1.10 + 2 |
| mam | 22.52 / 22 (29.91) | 116.49 / 162 (29.7) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (29.95) | 181.2 / 227 (29.83) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (11.14) | 170.0 / 170 (10.27) | median and average, x 1.10 + 2 |
| hologram | 32.83 / 33 (23.6) | 154.61 / 173 (23.03) | median, at most vanilla |
| hud | 31.0 / 31 (17.02) | 171.0 / 171 (14.58) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (29.99) | 99.92 / 147 (30.01) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.277 | 32.0 / 20.277 | 0.0 / 0.0 | 416.0 / 33.739 |
| ponder | 32.0 / 17.573 | 32.0 / 17.573 | 0.0 / 0.0 | 416.0 / 28.928 |
| map | 40.0 / 27.789 | 40.0 / 27.789 | 0.0 / 0.0 | 416.0 / 47.258 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.353 | 40.0 / 17.353 | 0.0 / 0.0 | 416.0 / 31.024 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.872 |


Package 4b, siftec full: Brimstone's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Brimstone / vanilla | Brimstone terrain bytes, largest frame (budget avg) | frames over 50 ms: Brimstone / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 25070.864 (45890.595) | 45.368 | 1.0 | 94716 / 155244 | 80960 (2097152.0) | 2 / 12 | 0.0 / 0 |
| factory_flat | 25070.799 (45890.442) | 45.368 | 1.0 | 97700 / 158228 | 80960 (2097152.0) | 1 / 1 | 0.0 / 0 |
| sift | 147727.36 (205024.752) | 27.947 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 322 / 299 | 0.0 / 0 |
| hub | 33472.927 (58846.078) | 43.118 | 1.02 | 84960 / 84940 | 0 (2097152.0) | 13 / 5 | 0.0 / 0 |
| mam | 66204.525 (115879.455) | 42.868 | 1.011 | 42132 / 304164 | 0 (2097152.0) | 1 / 2 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 60884 / 60864 | 0 (2097152.0) | 2 / 3 | 0.0 / 0 |
| map | 397171.036 (623708.901) | 36.321 | 1.231 | 48840 / 46244 | 0 (2097152.0) | 222 / 205 | 0.0 / 0 |
| hologram | 3912.271 (6874.534) | 43.09 | 1.0 | 74656 / 74432 | 0 (2097152.0) | 31 / 22 | 0.0 / 0 |
| hud | 291751.576 (455792.635) | 35.99 | 1.317 | 74196 / 71580 | 0 (2097152.0) | 340 / 291 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 24.907 / 0.264 | 1.453 / 28.0 | 2.447 | 1771 / 0 | 4.103 (5.962) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.125 | 16 (vanilla 28) | 91632 | 3.574 / 0.001 | 1.453 / 12.0 | 2.447 | 298 / 0 | 2.364 (3.866) |
| sift | indirect, one multi-draw per arena and layer | 3.009 | 430.332 | 16 (vanilla 28) | 462608 | 22.805 / 0.302 | 7.389 / 16.0 | 12.353 | 2781 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.085 | 16 (vanilla 28) | 117492 | 22.101 / 0.292 | 1.879 / 12.0 | 3.137 | 2867 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1190524 | 23.033 / 0.327 | 20.277 / 32.0 | 31.79 | 3521 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1019776 | 23.033 / 0.327 | 17.573 / 32.0 | 27.231 | 3522 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.014 | 547.455 | 16 (vanilla 28) | 1744116 | 19.901 / 0.257 | 27.789 / 40.0 | 46.573 | 4366 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.028 | 16 (vanilla 28) | 13744 | 19.836 / 0.255 | 0.238 / 40.0 | 0.367 | 4422 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.009 | 405.188 | 16 (vanilla 28) | 1088964 | 16.317 / 0.211 | 17.353 / 40.0 | 29.078 | 6217 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.168 / 0.209 | 0.171 / 40.0 | 0.283 | 6321 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1938 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 588 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 32.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7136 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1892 / 0 / 0 |
| hud | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12276 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.035; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.019; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 96.357 / 0.0 | 0.55 / 0.516 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.068 / 10.015 | 0.018 / 0.006 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 94.839 / 0.0 | 0.601 / 0.562 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.42 / 11.227 | 0.019 / 0.006 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.882 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.963 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.96 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.935 / 0.0 | 0.005 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 121.739 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 152.22 / 93.036 | 0.144 / 0.056 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.86 | 0.0 / 0.0 | 571.51 / 571.19 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.86 (8.0 / 8.0) |
| mam | 13.85 / 13.86 | 1.0 / 1.0 | 109.8 / 109.83 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.86 (8.0 / 8.0) |
| ponder | 82.58 / 82.57 | 0.11 / 0.0 | 203.21 / 203.18 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.6 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.22 (base 20.23) | 13 (base 13) | 49024 (base 48604) | 422.93 (base 421.54) | 4.24 (base 5.48) |
| factory_flat | 28.31 (base 28.32) | 13 (base 13) | 52240 (base 48340) | 422.78 (base 422.36) | 3.54 (base 5.48) |
| sift | 39.03 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 440.87 (base 441.24) | 4.24 (base 4.24) |
| hub | 25.54 (base 25.54) | 14 (base 17) | 64756 (base 66484) | 453.68 (base 451.46) | 4.24 (base 5.99) |
| mam | 24.52 (base 24.53) | 17 (base 20) | 20916 (base 22916) | 466.94 (base 472.87) | 8.24 (base 5.99) |
| ponder | 87.26 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 465.63 (base 463.76) | 8.24 (base 8.24) |
| map | 66.0 (base 67.0) | 12 (base 12) | 30924 (base 31404) | 493.71 (base 506.16) | 8.06 (base 6.31) |
| hologram | 34 median (avg 33.67) (base 34) | 20 (base 23) | 71920 (base 70624) | 471.55 (base 452.39) | 8.12 (base 6.31) |
| hud | 57.74 (base 58.0) | 14 (base 14) | 59972 (base 59972) | 492.2 (base 493.56) | 8.12 (base 8.18) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 481.31 (base 470.06) | 8.12 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.22 / 20 (29.85) | 180.42 / 226 (29.66) | median and average, x 1.10 + 2 |
| factory_flat | 28.31 / 28 (30.01) | 187.6 / 234 (29.97) | median and average, x 1.10 + 2 |
| sift | 39.03 / 41 (24.31) | 579.0 / 579 (15.19) | median, x 1.10 + 2 |
| hub | 25.54 / 25 (29.97) | 162.83 / 208 (29.57) | median and average, x 1.10 + 2 |
| mam | 24.52 / 24 (30.0) | 200.88 / 246 (29.65) | median, x 1.10 + 2 |
| ponder | 87.26 / 87 (29.95) | 181.07 / 227 (29.84) | median and average, x 1.10 + 2 |
| map | 66.0 / 66 (17.2) | 709.0 / 709 (10.71) | median and average, x 1.10 + 2 |
| hologram | 33.67 / 34 (29.97) | 166.61 / 184 (22.26) | median, at most vanilla |
| hud | 57.74 / 58 (26.78) | 572.0 / 572 (15.34) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 110.84 / 158 (30.01) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.5 | 12.0 / 1.5 | 0.0 / 0.0 | 256.0 / 2.609 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.302 | 32.0 / 20.302 | 0.0 / 0.0 | 416.0 / 33.869 |
| ponder | 32.0 / 17.597 | 32.0 / 17.597 | 0.0 / 0.0 | 416.0 / 29.054 |
| map | 40.0 / 27.789 | 40.0 / 27.789 | 0.0 / 0.0 | 416.0 / 47.405 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.353 | 40.0 / 17.353 | 0.0 / 0.0 | 416.0 / 30.481 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec old-gpu: Brimstone's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Brimstone / vanilla | Brimstone terrain bytes, largest frame (budget avg) | frames over 50 ms: Brimstone / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 25070.72 (45890.332) | 45.368 | 1.0 | 136692 / 198416 | 80960 (2097152.0) | 3 / 7 | 0.0 / 0 |
| factory_flat | 25070.384 (45889.683) | 45.368 | 1.0 | 139776 / 201408 | 80960 (2097152.0) | 0 / 1 | 0.0 / 0 |
| sift | 145761.305 (204810.556) | 28.831 | 1.434 | 4148 / 8052 | 0 (2097152.0) | 29 / 303 | 0.0 / 0 |
| hub | 33471.693 (58843.91) | 43.118 | 1.02 | 83332 / 83940 | 0 (2097152.0) | 2 / 9 | 0.0 / 0 |
| mam | 66204.341 (115879.132) | 42.868 | 1.011 | 39492 / 40372 | 0 (2097152.0) | 1 / 3 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 60828 / 60820 | 0 (2097152.0) | 4 / 4 | 0.0 / 0 |
| map | 363499.622 (622716.977) | 41.627 | 1.815 | 30924 / 35404 | 0 (2097152.0) | 330 / 214 | 0.0 / 0 |
| hologram | 3910.518 (6871.452) | 43.09 | 1.0 | 75252 / 75720 | 0 (2097152.0) | 1 / 57 | 0.0 / 0 |
| hud | 262538.899 (455303.866) | 42.338 | 1.837 | 59972 / 63556 | 0 (2097152.0) | 8 / 306 | 0.0 / 0 |
| create_ct | 2538.223 (5312.84) | 52.225 | 1.0 | 1124 / 1316 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 91632 | 27.418 / 0.332 | 1.453 / 28.0 | 2.447 | 2015 / 0 | 1.728 (1.734) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 94504 | 3.525 / 0.001 | 1.5 / 12.0 | 2.524 | 310 / 0 | 1.11 (0.948) |
| sift | plain, one multi-draw per region and layer | 21.043 | 429.883 | 16 (vanilla 28) | 462608 | 24.996 / 0.354 | 7.389 / 16.0 | 12.353 | 3021 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.083 | 16 (vanilla 28) | 117492 | 24.278 / 0.343 | 1.879 / 12.0 | 3.137 | 3103 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.145 | 16 (vanilla 28) | 1192000 | 24.749 / 0.367 | 20.302 / 32.0 | 31.83 | 3759 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1021252 | 24.749 / 0.367 | 17.597 / 32.0 | 27.27 | 3760 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 39.113 | 546.584 | 16 (vanilla 28) | 1744116 | 21.385 / 0.292 | 27.789 / 40.0 | 46.573 | 4604 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.005 | 13.022 | 16 (vanilla 28) | 13744 | 21.259 / 0.289 | 0.238 / 40.0 | 0.367 | 4672 / 0 | None (None) |
| hud | plain, one multi-draw per region and layer | 30.056 | 404.754 | 16 (vanilla 28) | 1088964 | 17.2 / 0.245 | 17.353 / 40.0 | 29.078 | 6494 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 17.06 / 0.243 | 0.171 / 40.0 | 0.283 | 6590 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2922 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 32.0 | 15.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 11040 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| hud | 36.0 | 9.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 19332 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.011; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.199 / 0.0 | 0.553 / 0.548 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.608 / 10.448 | 0.015 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.597 / 0.639 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.14 / 11.937 | 0.019 / 0.008 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 8.21 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.281 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.358 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.802 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.835 / 0.0 | 0.004 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 122.735 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 152.344 / 90.955 | 0.094 / 0.039 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.178 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.88 | 0.0 / 0.0 | 571.94 / 571.89 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.88 (8.0 / 8.0) |
| mam | 13.86 / 13.85 | 1.0 / 1.0 | 109.98 / 109.53 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.85 (8.0 / 8.0) |
| ponder | 82.59 / 82.59 | 0.11 / 0.0 | 203.21 / 203.16 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.62 / 81.61 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.21 (base 14.22) | 15 (base 15) | 51636 (base 51148) | 422.24 (base 420.23) | 4.23 (base 3.71) |
| factory_flat | 22.24 (base 22.31) | 15 (base 15) | 54712 (base 50904) | 421.53 (base 421.17) | 5.46 (base 3.46) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19192 (base 81788) | 439.48 (base 439.9) | 4.23 (base 4.23) |
| hub | 21.53 (base 21.55) | 16 (base 18) | 66384 (base 67484) | 451.79 (base 450.49) | 4.23 (base 4.23) |
| mam | 22.54 (base 22.52) | 19 (base 21) | 23556 (base 24656) | 465.26 (base 462.56) | 4.23 (base 5.46) |
| ponder | 87.23 (base 87.24) | 14 (base 14) | 52268 (base 52248) | 464.16 (base 462.87) | 4.23 (base 8.23) |
| map | 30.0 (base 30.0) | 14 (base 14) | 48840 (base 47072) | 491.43 (base 504.09) | 8.04 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| hud | 31.0 (base 31.0) | 16 (base 16) | 74196 (base 72908) | 480.3 (base 490.43) | 8.1 (base 8.16) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 471.12 (base 475.26) | 8.16 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.21 / 14 (29.82) | 108.49 / 154 (29.67) | median and average, x 1.10 + 2 |
| factory_flat | 22.24 / 22 (29.84) | 116.7 / 162 (29.66) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (14.6) | 153.0 / 153 (13.39) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (29.66) | 117.56 / 161 (28.97) | median and average, x 1.10 + 2 |
| mam | 22.54 / 22 (29.55) | 123.34 / 162 (27.62) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (29.99) | 180.98 / 227 (29.84) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (10.69) | 170.0 / 170 (9.31) | median and average, x 1.10 + 2 |
| hud | 31.0 / 31 (15.61) | 171.0 / 171 (13.36) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 100.0 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.21 | 32.0 / 20.21 | 0.0 / 0.0 | 416.0 / 33.906 |
| ponder | 32.0 / 17.506 | 32.0 / 17.506 | 0.0 / 0.0 | 416.0 / 29.213 |
| map | 40.0 / 27.79 | 40.0 / 27.79 | 0.0 / 0.0 | 416.0 / 47.252 |
| hud | 56.0 / 18.103 | 56.0 / 18.103 | 0.0 / 0.0 | 416.0 / 27.456 |
| create_ct | 56.0 / 0.171 | 56.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec vulkan: Brimstone's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Brimstone / vanilla | Brimstone terrain bytes, largest frame (budget avg) | frames over 50 ms: Brimstone / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 25070.72 (45890.332) | 45.368 | 1.0 | 139376 / 199896 | 80960 (2097152.0) | 3 / 1 | 0.0 / 0 |
| factory_flat | 25070.566 (45890.023) | 45.368 | 1.0 | 142360 / 202888 | 80960 (2097152.0) | 2 / 1 | 0.0 / 0 |
| sift | 147774.349 (205089.966) | 27.947 | 1.298 | 19192 / 16632 | 0 (2097152.0) | 292 / 267 | 0.0 / 0 |
| hub | 33472.351 (58845.066) | 43.118 | 1.02 | 84960 / 84940 | 0 (2097152.0) | 7 / 8 | 0.0 / 0 |
| mam | 66444.237 (116404.629) | 42.92 | 1.011 | 42132 / 42112 | 0 (2097152.0) | 22 / 15 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 60884 / 60840 | 0 (2097152.0) | 0 / 3 | 0.0 / 0 |
| map | 397246.291 (623827.08) | 36.321 | 1.231 | 48840 / 46244 | 0 (2097152.0) | 213 / 185 | 0.0 / 0 |
| hud | 291828.359 (455912.59) | 35.99 | 1.317 | 74196 / 72664 | 0 (2097152.0) | 312 / 267 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 26.42 / 0.343 | 1.453 / 28.0 | 2.447 | 1766 / 0 | 3.15 (4.274) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91616 | 3.508 / 0.002 | 1.453 / 12.0 | 2.446 | 288 / 0 | 1.84 (2.278) |
| sift | indirect, one multi-draw per arena and layer | 3.01 | 430.469 | 16 (vanilla 28) | 462608 | 24.064 / 0.364 | 7.389 / 16.0 | 12.353 | 2775 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 23.361 / 0.352 | 1.879 / 12.0 | 3.137 | 2859 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.147 | 16 (vanilla 28) | 1186532 | 24.033 / 0.376 | 20.21 / 32.0 | 31.684 | 3507 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1015784 | 24.033 / 0.376 | 17.506 / 32.0 | 27.124 | 3508 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.014 | 547.559 | 16 (vanilla 28) | 1744212 | 20.716 / 0.296 | 27.79 / 40.0 | 46.575 | 4354 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.01 | 405.295 | 16 (vanilla 28) | 1135692 | 16.561 / 0.244 | 18.103 / 56.0 | 30.326 | 6129 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.403 / 0.242 | 0.171 / 56.0 | 0.283 | 6237 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1758 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 594 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 592 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 32.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 6848 / 0 / 0 |
| hud | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 11268 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.031; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.276 / 0.0 | 0.544 / 0.543 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.623 / 10.699 | 0.018 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 94.435 / 0.0 | 0.59 / 0.571 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.411 / 11.229 | 0.02 / 0.007 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 95.993 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 100.759 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.725 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.367 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.86 / 13.88 | 0.0 / 0.0 | 571.31 / 571.76 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.86 / 13.88 (8.0 / 8.0) |
| mam | 13.87 / 13.86 | 1.0 / 1.0 | 110.37 / 110.11 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.87 / 13.86 (8.0 / 8.0) |
| ponder | 82.56 / 82.59 | 0.11 / 0.11 | 203.09 / 203.26 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.59 / 81.62 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

## Bench-only jar (brimstone-bench)

| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |
|---|---|---|---|---|---|---|---|
| bench-plain | spawn | 13.93 | 101.52 | 278 | 123 | 905032 | [] |
| bench-plain | flythrough | 9.22 | 235.05 | 184 | 123 | 7719136 | [] |
| bench-plain | water_glass | 25.2 | 46.78 | 3 | 124 | 329608 | [] |
| bench-plain | nether | 25.47 | 50.67 | 6 | 121 | 318220 | [] |
| bench-sodium | spawn | 17.68 | 77.46 | 350 | 148 | 8144 | [] |
| bench-sodium | flythrough | 13.87 | 155.99 | 268 | 159 | 265132 | [] |
| bench-sodium | water_glass | 29.97 | 39.09 | 0 | 135 | 9116 | [] |
| bench-sodium | nether | 30.0 | 38.13 | 0 | 128 | 263776 | [] |

