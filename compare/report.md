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
| spawn | 0.2929 | 10.05 | 0.1998 | 0.4489 | 0.2058 |
| flythrough | 0.0 | 14.95 | 0.0015 | 0.0015 | 0.0015 |
| water_glass | 0.0043 | 6.45 | 0.0051 | 0.0036 | 0.0048 |
| nether | 0.0 | 0.8 | 0.0003 | 0.0008 | 0.0095 |
| mobs | 0.1449 | 0.4 | 0.0569 | 0.0327 | 0.1429 |
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
| translucent_move | 0.0 | 0.3 | 0.0002 | 0.0003 | 0.0002 |
| translucent_move.x2404 | 0.0002 | 0.3 | 0.0004 | 0.0003 | 0.0003 |
| translucent_move.x2414 | 0.0001 | 0.3 | 0.0002 | 0.0002 | 0.0004 |
| translucent_move.x2424 | 0.0004 | 0.3 | 0.0003 | 0.0004 | 0.0002 |
| translucent_move.low | 0.0005 | 0.3 | 0.0012 | 0.0013 | 0.0012 |
| translucent_move.back | 0.0003 | 0.3 | 0.0001 | 0.0 | 0.0001 |
| translucent_move.bare_back | 0.0003 | 0.3 | 0.0002 | 0.0001 | 0.0002 |
| water_glass_oit | 0.0031 | 0.3 | 0.0052 | 0.006 | 0.004 |
| translucent_move_oit | 0.0001 | 0.3 | 0.0001 | 0.0001 | 0.0002 |
| translucent_move_oit.x2404 | 0.0002 | 0.3 | 0.0004 | 0.0002 | 0.0002 |
| translucent_move_oit.x2414 | 0.0005 | 0.3 | 0.0007 | 0.0001 | 0.0002 |
| translucent_move_oit.x2424 | 0.0002 | 0.3 | 0.0003 | 0.0002 | 0.0001 |
| translucent_move_oit.low | 0.0004 | 0.3 | 0.001 | 0.0013 | 0.0016 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 | 0.0002 | 0.0003 |
| translucent_move_oit.bare_back | 0.0003 | 0.3 | 0.0 | 0.0003 | 0.0001 |
| flat_plants | 0.0 | 0.3 | 0.023 | 0.023 | 0.0226 |
| renderer_off | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| particles | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| particles.bare | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| animation | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| animation.behind | 0.0 | 0.25 | 0.0 | 0.0 | 0.0253 |
| animation.front | 0.0 | 0.25 | 0.0001 | 0.0001 | 0.0001 |
| animated_items | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| spawn | 92.61 | 18057 | 266 (743) | 316 (833) | 239 (770) |
| flythrough | 97.2 | 6020 | 54 (304) | 54 (304) | 57 (303) |
| water_glass | 95.8 | 25508 | 23 (2624) | 11 (2612) | 22 (2737) |
| nether | 99.42 | 905 | 3 (458) | 7 (461) | 87 (554) |
| mobs | 99.24 | 600 | 36 (36) | 4 (4) | 46 (46) |
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
| translucent_move | 96.93 | 13 | 3 (78) | 3 (79) | 1 (67) |
| translucent_move.x2404 | 95.93 | 15 | 3 (103) | 4 (105) | 4 (88) |
| translucent_move.x2414 | 95.86 | 13 | 2 (105) | 4 (107) | 5 (104) |
| translucent_move.x2424 | 96.74 | 13 | 5 (82) | 2 (80) | 2 (69) |
| translucent_move.low | 96.11 | 41 | 14 (20) | 16 (21) | 16 (18) |
| translucent_move.back | 96.93 | 13 | 3 (78) | 1 (76) | 2 (68) |
| translucent_move.bare_back | 96.9 | 13 | 1 (76) | 3 (78) | 2 (68) |
| water_glass_oit | 95.8 | 57 | 23 (2623) | 23 (2624) | 14 (2728) |
| translucent_move_oit | 96.93 | 12 | 1 (76) | 3 (78) | 3 (69) |
| translucent_move_oit.x2404 | 95.93 | 16 | 4 (106) | 4 (104) | 3 (88) |
| translucent_move_oit.x2414 | 95.86 | 10 | 3 (106) | 4 (107) | 3 (100) |
| translucent_move_oit.x2424 | 96.74 | 15 | 3 (80) | 2 (79) | 2 (68) |
| translucent_move_oit.low | 96.11 | 40 | 11 (16) | 16 (23) | 17 (19) |
| translucent_move_oit.back | 96.93 | 12 | 1 (76) | 2 (77) | 2 (68) |
| translucent_move_oit.bare_back | 96.9 | 15 | 1 (76) | 2 (77) | 1 (67) |
| flat_plants | 98.22 | 350 | 225 (7416) | 225 (7416) | 217 (7347) |
| renderer_off | 99.2 | 10 | 0 (0) | 0 (0) | 0 (0) |
| particles | 99.99 | 4 | 0 (0) | 0 (0) | 0 (0) |
| particles.bare | 99.99 | 4 | 0 (0) | 0 (0) | 0 (0) |
| animation | 98.94 | 10 | 1 (3) | 1 (3) | 3 (133) |
| animation.behind | 99.54 | 10 | 0 (2) | 0 (2) | 0 (24) |
| animation.front | 98.94 | 10 | 1 (3) | 1 (3) | 3 (133) |
| animated_items | 98.9 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| spawn | 0.2029 / 270 | 0.2141 / 288 | 0.2088 / 282 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| water_glass | 0.0035 / 11 | 0.0043 / 18 | 0.0044 / 17 |
| nether | 0.0754 / 694 | 0.0 / 0 | 0.0 / 0 |
| mobs | 0.1102 / 81 | 0.0743 / 33 | 0.0461 / 39 |
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
| translucent_move | 0.0001 / 3 | 0.0002 / 1 | 0.0001 / 0 |
| translucent_move.x2404 | 0.0001 / 0 | 0.0001 / 1 | 0.0 / 1 |
| translucent_move.x2414 | 0.0001 / 0 | 0.0 / 1 | 0.0004 / 4 |
| translucent_move.x2424 | 0.0002 / 1 | 0.0002 / 1 | 0.0001 / 1 |
| translucent_move.low | 0.0001 / 2 | 0.0005 / 1 | 0.0002 / 3 |
| translucent_move.back | 0.0 / 0 | 0.0 / 0 | 0.0001 / 1 |
| translucent_move.bare_back | 0.0002 / 0 | 0.0 / 0 | 0.0003 / 1 |
| water_glass_oit | 0.0034 / 12 | 0.0053 / 24 | 0.0049 / 21 |
| translucent_move_oit | 0.0002 / 2 | 0.0 / 0 | 0.0001 / 0 |
| translucent_move_oit.x2404 | 0.0001 / 3 | 0.0 / 1 | 0.0002 / 4 |
| translucent_move_oit.x2414 | 0.0008 / 3 | 0.0001 / 2 | 0.0 / 3 |
| translucent_move_oit.x2424 | 0.0001 / 0 | 0.0001 / 4 | 0.0 / 0 |
| translucent_move_oit.low | 0.0007 / 7 | 0.0007 / 3 | 0.0007 / 2 |
| translucent_move_oit.back | 0.0 / 1 | 0.0002 / 3 | 0.0001 / 0 |
| translucent_move_oit.bare_back | 0.0 / 1 | 0.0002 / 0 | 0.0002 / 0 |
| flat_plants | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| renderer_off | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| particles | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| particles.bare | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| animation | 0.0 / 0 | 0.0 / 0 | 0.0 / 2 |
| animation.behind | 0.0 / 0 | 0.0 / 0 | 0.0253 / 0 |
| animation.front | 0.0 / 0 | 0.0 / 0 | 0.0 / 2 |
| animated_items | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, core full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 13.6 (base 123.09) | 15 (base 14) | 21100 (base 22476) | 256.9 (base 259.07) | 4.48 (base 3.99) |
| flythrough | 13.06 (reported, not gated) | 33 (reported, not gated) | 366708 (reported, not gated) | 313.23 (base 324.25) | 3.73 (base 4.24) |
| water_glass | 8.97 (base 182.47) | 15 (base 15) | 15036 (base 15576) | 244.29 (base 250.02) | 4.48 (base 5.99) |
| nether | 15.63 (base 176.56) | 12 (base 12) | 18188 (base 19648) | 265.4 (base 273.53) | 4.48 (base 5.99) |
| mobs | 19.77 (base 216.22) | 17 (base 17) | 616348 (base 1010948) | 263.76 (base 268.37) | 4.48 (base 6.99) |
| outline | 10.67 (base 10.72) | 18 (base 18) | 6356 (base 6356) | 263.72 (base 264.69) | 4.48 (base 5.48) |
| occlusion | 15.67 (base 96.43) | 15 (base 15) | 100332 (base 126320) | 270.47 (base 265.1) | 4.48 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82204 (reported, not gated) | 269.67 (base 265.74) | 4.48 (base 4.24) |
| translucent_move | 8.94 (reported, not gated) | 15 (reported, not gated) | 10140 (reported, not gated) | 271.6 (base 267.48) | 6.48 (base 5.48) |
| water_glass_oit | 14.0 (base 131.0) | 15 (base 15) | 15204 (base 14688) | 243.95 (base 245.02) | 3.35 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10232 (reported, not gated) | 246.38 (base 247.19) | 3.48 (base 3.73) |
| flat_plants | 18.29 (base 123.98) | 15 (base 15) | 9716 (base 9812) | 246.53 (base 246.92) | 3.48 (base 3.99) |
| renderer_off | 7.67 (base 96.01) | 14 (base 14) | 7328 (base 7164) | 239.75 (base 241.33) | 3.35 (base 4.12) |
| particles | 9.67 (base 10.67) | 15 (base 15) | 9204 (base 17128) | 267.0 (base 261.44) | 6.48 (base 5.99) |
| animation | 49 median (avg 39.93) (base 49) | 15 (base 15) | 2204 (base 2056) | 264.4 (base 260.68) | 6.48 (base 5.99) |
| animated_items | 60 median (avg 46.66) (base 60) | 24 (base 23) | 22136 (base 21940) | 239.76 (base 239.81) | 6.48 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.6 / 14 (11.94) | 123.79 / 124 (11.57) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (7.89) | 123.06 / 123 (7.77) | median and average, at most vanilla + 2 |
| water_glass | 8.97 / 9 (20.36) | 122.26 / 124 (20.01) | median, at most vanilla |
| nether | 15.63 / 16 (20.11) | 120.38 / 121 (20.05) | median, x 1.10 + 2 |
| mobs | 19.77 / 20 (25.79) | 107.18 / 134 (25.99) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.2 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.33 / 133 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.98) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| translucent_move | 8.94 / 9 (21.33) | 121.71 / 124 (20.31) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (11.99) | 129.0 / 129 (10.86) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (10.96) | 129.0 / 129 (10.57) | median and average, at most vanilla |
| flat_plants | 18.29 / 19 (20.79) | 120.51 / 124 (20.58) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.87) | 84.72 / 123 (29.86) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.93 / 49 (30.0) | 144.09 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.66 / 60 (30.0) | 155.09 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.694 | 36.0 / 21.694 | 0.0 / 0.0 | 416.0 / 38.057 |
| flythrough | 112.0 / 86.712 | 112.0 / 86.712 | 0.0 / 0.0 | 416.0 / 145.626 |
| water_glass | 36.0 / 12.556 | 36.0 / 12.556 | 0.0 / 0.0 | 416.0 / 21.886 |
| nether | 20.0 / 15.124 | 20.0 / 15.124 | 0.0 / 0.0 | 256.0 / 26.51 |
| mobs | 16.0 / 9.582 | 16.0 / 9.582 | 0.0 / 0.0 | 256.0 / 16.853 |
| outline | 16.0 / 1.439 | 16.0 / 1.439 | 0.0 / 0.0 | 256.0 / 2.488 |
| occlusion | 20.0 / 2.541 | 20.0 / 2.541 | 0.0 / 0.0 | 416.0 / 4.267 |
| occlusion_move | 20.0 / 3.304 | 20.0 / 3.304 | 0.0 / 0.0 | 416.0 / 5.716 |
| translucent_move | 28.0 / 12.94 | 28.0 / 12.94 | 0.0 / 0.0 | 416.0 / 22.33 |
| water_glass_oit | 20.0 / 12.529 | 20.0 / 12.529 | 0.0 / 0.0 | 416.0 / 21.886 |
| translucent_move_oit | 28.0 / 12.584 | 28.0 / 12.584 | 0.0 / 0.0 | 416.0 / 21.617 |
| flat_plants | 24.0 / 14.237 | 24.0 / 14.237 | 0.0 / 0.0 | 416.0 / 24.796 |
| renderer_off | 24.0 / 8.888 | 24.0 / 8.888 | 0.0 / 0.0 | 416.0 / 15.613 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.013 | 733.067 | 16 (vanilla 28) | 1309052 | 41.085 / 0.084 | 21.694 / 36.0 | 34.955 | 2197 / 0 | 3.735 (6.812) |
| flythrough | indirect, one multi-draw per arena and layer | 3.019 | 914.987 | 16 (vanilla 28) | 5252980 | 15.436 / 1.021 | 86.712 / 112.0 | 140.27 | 6542 / 0 | 4.987 (12.874) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 274.673 | 16 (vanilla 28) | 779572 | 32.596 / 0.075 | 12.556 / 36.0 | 20.817 | 2825 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 468.162 | 16 (vanilla 28) | 950204 | 27.116 / 0.198 | 15.124 / 20.0 | 25.373 | 3873 / 0 | 2.922 (5.352) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 198.384 | 16 (vanilla 28) | 599580 | 24.606 / 0.172 | 9.582 / 16.0 | 16.011 | 4330 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 44.073 | 16 (vanilla 28) | 89596 | 24.142 / 0.17 | 1.439 / 16.0 | 2.392 | 4428 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 158908 | 23.134 / 0.163 | 2.541 / 20.0 | 4.243 | 4643 / 0 | 4.472 (4.012) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.81 | 16 (vanilla 28) | 206628 | 22.054 / 0.155 | 3.304 / 20.0 | 5.518 | 4893 / 0 | 3.515 (6.725) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.007 | 248.326 | 16 (vanilla 28) | 799432 | 19.208 / 0.143 | 12.94 / 28.0 | 21.347 | 5650 / 0 | 6.846 (18.345) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.021 | 281.172 | 16 (vanilla 28) | 777912 | 7.48 / 0.043 | 12.529 / 20.0 | 20.772 | 1087 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.023 | 263.63 | 16 (vanilla 28) | 776924 | 7.562 / 0.212 | 12.584 / 28.0 | 20.746 | 2552 / 0 | 6.932 (19.457) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.007 | 284.684 | 16 (vanilla 28) | 888564 | 6.126 / 0.01 | 14.237 / 24.0 | 23.727 | 937 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 207.347 | 16 (vanilla 28) | 558304 | 2.396 / 0.021 | 8.888 / 24.0 | 14.908 | 598 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.121 / 0.142 | 0.131 / 28.0 | 0.225 | 5698 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 18.953 / 0.141 | 0.302 / 28.0 | 0.518 | 5800 / 0 | 0.962 (1.454) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.894 / 0.141 | 0.153 / 28.0 | 0.261 | 5849 / 0 | 0.936 (1.376) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 150.0 | 1.0 | 0.0 | 0 | 0 | 46 (0.025 / 0.039 / 0.044; 0) | 0 | 35850 / 0 / 0 |
| flythrough | 191.994 | 1.0 | 0.0 | 0 | 0 | 893 (0.023 / 0.036 / 6.109; 0) | 47 | 30335 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1632 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.062 / 0.632 / 0.974; 0) | 32 | 1202 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.161 / 0.123 / 5.204; 0) | 180 | 2989 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 720 | 0 (None / None / None; 0) | 0 | 960 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 660 | 0 (None / None / None; 0) | 0 | 1540 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1664 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2392 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 313.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.027 (0.069; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 434.93 | 1.0 (0.0, 0.0) | 1.152 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.053; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.122 (0.279; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.062 (0.186; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.705 | 10.0 (9.0, 2.359) | 6.0 (6.0, 2.276) | 0.053 (0.196; 0) | 0 | 2786 / 1508380 / 0 / 0 |
| translucent_move | 136.047 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.75 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 113.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.792 / 0.601 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 135.743 / 0.741 | 0.028 / 0.013 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 6.035 / 0.0 | 0.02 / 0.003 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.854 |
| water_glass | 113.279 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.815 / 53.863 | 0.015 / 0.019 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 114.425 / 9.876 | 0.958 / 0.769 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 41.172 / 6.129 | 0.07 / 0.009 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 88.41 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.539 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 112.734 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 14.008 / 13.974 | 0.004 / 0.014 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 57.459 / 57.254 | 0.013 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.362 / 13.214 | 0.004 / 0.008 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 111.642 / 9.59 | 0.9 / 0.745 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.49 / 2.399 | 0.012 / 0.008 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 77.052 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.007 / 0.052 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.26 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.005 / 0.01 | 56 (3) | 40.51 / 9.235 / 31.275 | 3 / 0 | 10.985 |
| animated_items | 136.426 / 27.992 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.473 / 7.96 / 32.513 | 0 / 0 | 9.0 |

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
| spawn | 49.0 (base 844.2) | 12 (base 13) | 1380 (base 6372) | 257.97 (base 256.29) | 5.48 (base 3.73) |
| flythrough | 66.84 (reported, not gated) | 51 (reported, not gated) | 632884 (reported, not gated) | 314.98 (base 324.79) | 3.48 (base 5.99) |
| water_glass | 20.0 (base 457.8) | 13 (base 14) | 7540 (base 10264) | 244.56 (base 249.83) | 5.48 (base 5.99) |
| nether | 33.73 (base 640.77) | 10 (base 11) | 3840 (base 10656) | 266.45 (base 272.57) | 5.48 (base 5.99) |
| mobs | 28.96 (base 410.19) | 15 (base 16) | 610676 (base 1015892) | 264.35 (base 268.23) | 5.48 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 264.36 (base 264.41) | 5.48 (base 3.73) |
| occlusion | 22.67 (base 106.62) | 13 (base 14) | 98148 (base 124172) | 269.81 (base 264.64) | 5.48 (base 4.24) |
| occlusion_move | 24.05 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 270.63 (base 265.66) | 5.48 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 272.22 (base 268.19) | 5.48 (base 3.73) |
| water_glass_oit | 25.0 (base 154.0) | 13 (base 14) | 7092 (base 7604) | 244.39 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 3060 (reported, not gated) | 246.85 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 33.0 (base 137.98) | 13 (base 13) | 1716 (base 1716) | 246.62 (base 248.11) | 3.48 (base 5.99) |
| renderer_off | 17.74 (base 107.98) | 12 (base 12) | 1380 (base 1380) | 239.89 (base 240.73) | 3.35 (base 5.87) |
| particles | 10.72 (base 11.67) | 13 (base 13) | 8772 (base 16164) | 267.38 (base 261.4) | 5.48 (base 4.24) |
| animation | 49 median (avg 39.92) (base 49) | 13 (base 13) | 1716 (base 1716) | 265.0 (base 260.68) | 5.48 (base 4.24) |
| animated_items | 60 median (avg 46.69) (base 60) | 21 (base 22) | 21508 (base 21668) | 239.89 (base 239.53) | 5.48 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.0 / 49 (9.9) | 855.26 / 855 (10.38) | median and average, x 1.10 + 2 |
| flythrough | 66.84 / 64 (6.01) | 1011.78 / 1017 (6.01) | median and average, at most vanilla |
| water_glass | 20.0 / 20 (17.83) | 392.0 / 392 (17.24) | median, at most vanilla |
| nether | 33.73 / 34 (16.44) | 586.85 / 587 (17.12) | median, x 1.10 + 2 |
| mobs | 28.96 / 29 (20.83) | 319.17 / 330 (21.95) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 129.2 / 168 (30.01) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (29.99) | 162.27 / 201 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 24.05 / 24 (29.87) | 167.61 / 202 (29.99) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (16.99) | 373.62 / 375 (17.52) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (9.24) | 403.0 / 403 (9.68) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (9.11) | 386.69 / 386 (9.18) | median and average, at most vanilla |
| flat_plants | 33.0 / 33 (16.6) | 401.98 / 402 (17.47) | median, x 1.10 + 2 |
| renderer_off | 17.74 / 18 (26.81) | 299.89 / 327 (26.12) | median, x 1.10 + 2 |
| particles | 10.72 / 11 (30.01) | 97.27 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.92 / 49 (30.01) | 156.09 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.69 / 60 (30.0) | 167.09 / 202 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.837 | 36.0 / 22.837 | 0.0 / 0.0 | 416.0 / 37.319 |
| flythrough | 112.0 / 84.685 | 112.0 / 84.685 | 0.0 / 0.0 | 416.0 / 145.669 |
| water_glass | 36.0 / 12.653 | 36.0 / 12.653 | 0.0 / 0.0 | 416.0 / 21.886 |
| nether | 20.0 / 15.129 | 20.0 / 15.129 | 0.0 / 0.0 | 256.0 / 26.511 |
| mobs | 16.0 / 9.582 | 16.0 / 9.582 | 0.0 / 0.0 | 256.0 / 16.853 |
| outline | 16.0 / 1.439 | 16.0 / 1.439 | 0.0 / 0.0 | 256.0 / 2.488 |
| occlusion | 20.0 / 2.568 | 20.0 / 2.568 | 0.0 / 0.0 | 416.0 / 4.234 |
| occlusion_move | 20.0 / 3.383 | 20.0 / 3.383 | 0.0 / 0.0 | 416.0 / 5.687 |
| translucent_move | 28.0 / 13.146 | 28.0 / 13.146 | 0.0 / 0.0 | 416.0 / 22.407 |
| water_glass_oit | 20.0 / 12.556 | 20.0 / 12.556 | 0.0 / 0.0 | 416.0 / 21.886 |
| translucent_move_oit | 28.0 / 12.789 | 28.0 / 12.789 | 0.0 / 0.0 | 416.0 / 21.787 |
| flat_plants | 24.0 / 14.285 | 24.0 / 14.285 | 0.0 / 0.0 | 416.0 / 24.763 |
| renderer_off | 24.0 / 8.888 | 24.0 / 8.888 | 0.0 / 0.0 | 416.0 / 15.613 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.198 | 783.959 | 16 (vanilla 28) | 1378964 | 42.549 / 0.081 | 22.837 / 36.0 | 36.822 | 2395 / 0 | 4.93 (8.253) |
| flythrough | plain, one multi-draw per region and layer | 57.192 | 896.575 | 16 (vanilla 28) | 5126776 | 15.139 / 0.987 | 84.685 / 112.0 | 136.9 | 6413 / 0 | 7.869 (19.703) |
| water_glass | plain, one multi-draw per region and layer | 14.039 | 274.77 | 16 (vanilla 28) | 785204 | 34.45 / 0.075 | 12.653 / 36.0 | 20.967 | 3052 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.061 | 468.424 | 16 (vanilla 28) | 950316 | 28.805 / 0.184 | 15.129 / 20.0 | 25.376 | 4109 / 0 | 3.097 (4.157) |
| mobs | plain, one multi-draw per region and layer | 11.026 | 198.476 | 16 (vanilla 28) | 599580 | 26.206 / 0.161 | 9.582 / 16.0 | 16.011 | 4551 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 44.073 | 16 (vanilla 28) | 89596 | 25.739 / 0.16 | 1.439 / 16.0 | 2.392 | 4648 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.119 | 16 (vanilla 28) | 160664 | 24.518 / 0.152 | 2.568 / 20.0 | 4.29 | 4897 / 0 | 4.463 (5.0) |
| occlusion_move | plain, one multi-draw per region and layer | 11.003 | 76.468 | 16 (vanilla 28) | 212936 | 23.064 / 0.143 | 3.383 / 20.0 | 5.686 | 5204 / 0 | 5.443 (11.3) |
| translucent_move | plain, one multi-draw per region and layer | 12.035 | 249.395 | 16 (vanilla 28) | 813960 | 20.002 / 0.129 | 13.146 / 28.0 | 21.735 | 5983 / 0 | 6.964 (21.635) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.087 | 283.533 | 16 (vanilla 28) | 779572 | 7.482 / 0.043 | 12.556 / 20.0 | 20.817 | 1091 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.615 | 264.522 | 16 (vanilla 28) | 791428 | 7.395 / 0.21 | 12.789 / 28.0 | 21.133 | 2549 / 0 | 6.187 (14.066) |
| flat_plants | plain, one multi-draw per region and layer | 17.051 | 285.861 | 16 (vanilla 28) | 892088 | 5.988 / 0.01 | 14.285 / 24.0 | 23.821 | 930 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.024 | 207.386 | 16 (vanilla 28) | 558288 | 2.385 / 0.021 | 8.888 / 24.0 | 14.908 | 599 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 19.939 / 0.129 | 0.131 / 28.0 | 0.225 | 6029 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.774 / 0.128 | 0.302 / 28.0 | 0.518 | 6131 / 0 | 2.711 (4.998) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.721 / 0.128 | 0.153 / 28.0 | 0.261 | 6178 / 0 | 1.175 (1.876) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 153.0 | 17.0 | 0.0 | 0 | 0 | 36 (0.026 / 0.039 / 0.043; 0) | 0 | 30294 / 0 / 0 |
| flythrough | 186.223 | 25.909 | 0.0 | 0 | 0 | 840 (0.016 / 0.047 / 1.167; 0) | 43 | 22533 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1428 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.011 / 0.016; 0) | 32 | 1198 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.267 / 0.159 / 13.582; 0) | 180 | 2380 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 555 | 0 (None / None / None; 0) | 0 | 740 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 549 | 0 (None / None / None; 0) | 0 | 1281 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1328 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2148 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 339.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.025 (0.074; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 432.843 | 1.0 (0.0, 0.0) | 1.231 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.058; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.133 (0.268; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.065 (0.113; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.356 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.277) | 0.057 (0.226; 0) | 0 | 2776 / 1502584 / 0 / 0 |
| translucent_move | 136.012 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.667 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 113.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.007 (0.049; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.261 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 83.461 / 0.0 | 0.016 / 0.003 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 5.99 / 0.0 | 0.027 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.736 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.943 / 55.056 | 0.01 / 0.021 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.848 / 0.732 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 39.41 / 3.799 | 0.072 / 0.008 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 104.26 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.539 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.523 / 12.468 | 0.004 / 0.012 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 50.783 / 50.832 | 0.01 / 0.019 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 14.247 / 14.197 | 0.004 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.977 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.943 / 2.729 | 0.014 / 0.01 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 88.123 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.006 / 0.061 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.257 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.798 / 2.795 | 0.005 / 0.008 | 56 (3) | 40.473 / 9.235 / 31.238 | 3 / 0 | 10.985 |
| animated_items | 136.426 / 28.028 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.51 / 7.97 / 32.54 | 0 / 0 | 9.0 |

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
| spawn | 13.52 (base 124.84) | 15 (base 15) | 22532 (base 27764) | 258.71 (base 257.2) | 5.46 (base 3.98) |
| flythrough | 13.08 (reported, not gated) | 24 (reported, not gated) | 206776 (reported, not gated) | 314.43 (base 318.06) | 3.71 (base 4.23) |
| water_glass | 8.78 (base 153.28) | 15 (base 15) | 14980 (base 15108) | 244.9 (base 244.39) | 5.46 (base 4.23) |
| nether | 13.71 (base 142.02) | 12 (base 12) | 18984 (base 19912) | 265.95 (base 268.95) | 5.46 (base 4.23) |
| mobs | 19.67 (base 208.6) | 17 (base 17) | 616348 (base 1008176) | 264.4 (base 264.31) | 5.46 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6356 (base 6356) | 264.29 (base 264.58) | 5.46 (base 3.71) |
| occlusion | 15.67 (base 97.95) | 15 (base 15) | 100352 (base 126356) | 269.98 (base 264.78) | 5.46 (base 3.98) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82224 (reported, not gated) | 270.54 (base 265.16) | 5.46 (base 4.23) |
| translucent_move | 8.73 (reported, not gated) | 15 (reported, not gated) | 10032 (reported, not gated) | 272.28 (base 268.35) | 5.46 (base 5.46) |
| water_glass_oit | 14.0 (base 131.0) | 15 (base 15) | 15092 (base 15024) | 244.39 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 9900 (reported, not gated) | 246.56 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 16.66 (base 124.0) | 15 (base 15) | 9772 (base 9864) | 247.42 (base 248.07) | 3.46 (base 5.98) |
| renderer_off | 7.67 (base 109.4) | 14 (base 14) | 7328 (base 7180) | 239.46 (base 241.13) | 3.59 (base 4.1) |
| particles | 9.67 (base 10.67) | 15 (base 15) | 9204 (base 17128) | 267.55 (base 261.4) | 5.46 (base 5.98) |
| animation | 49 median (avg 39.95) (base 49) | 15 (base 15) | 2204 (base 2056) | 265.23 (base 260.68) | 5.46 (base 5.98) |
| animated_items | 60 median (avg 46.73) (base 60) | 23 (base 23) | 21976 (base 21940) | 240.2 (base 239.74) | 5.46 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.52 / 14 (15.18) | 123.0 / 123 (9.44) | median and average, x 1.10 + 2 |
| flythrough | 13.08 / 13 (9.87) | 123.05 / 123 (5.42) | median and average, at most vanilla + 2 |
| water_glass | 8.78 / 9 (25.49) | 124.0 / 124 (14.61) | median, at most vanilla |
| nether | 13.71 / 16 (24.81) | 120.87 / 121 (15.07) | median, x 1.10 + 2 |
| mobs | 19.67 / 20 (29.92) | 134.0 / 134 (18.82) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.46 / 133 (29.97) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.99) | 94.33 / 133 (29.96) | median and average, x 1.10 + 2 |
| translucent_move | 8.73 / 9 (27.51) | 124.0 / 124 (15.7) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (14.21) | 129.0 / 129 (8.39) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (13.25) | 129.0 / 129 (7.85) | median and average, at most vanilla |
| flat_plants | 16.66 / 19 (25.39) | 124.0 / 124 (15.51) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.95) | 110.54 / 123 (22.36) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.33 / 125 (29.99) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 144.09 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 155.09 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 22.66 | 40.0 / 22.66 | 0.0 / 0.0 | 416.0 / 36.963 |
| flythrough | 112.0 / 88.025 | 112.0 / 88.025 | 0.0 / 0.0 | 416.0 / 144.534 |
| water_glass | 40.0 / 12.529 | 40.0 / 12.529 | 0.0 / 0.0 | 416.0 / 21.886 |
| nether | 20.0 / 15.122 | 20.0 / 15.122 | 0.0 / 0.0 | 256.0 / 26.509 |
| mobs | 16.0 / 9.582 | 16.0 / 9.582 | 0.0 / 0.0 | 256.0 / 16.853 |
| outline | 16.0 / 1.439 | 16.0 / 1.439 | 0.0 / 0.0 | 256.0 / 2.488 |
| occlusion | 20.0 / 2.46 | 20.0 / 2.46 | 0.0 / 0.0 | 416.0 / 4.267 |
| occlusion_move | 20.0 / 3.302 | 20.0 / 3.302 | 0.0 / 0.0 | 416.0 / 5.72 |
| translucent_move | 28.0 / 12.942 | 28.0 / 12.942 | 0.0 / 0.0 | 416.0 / 22.439 |
| water_glass_oit | 20.0 / 12.529 | 20.0 / 12.529 | 0.0 / 0.0 | 416.0 / 21.886 |
| translucent_move_oit | 28.0 / 12.531 | 28.0 / 12.531 | 0.0 / 0.0 | 416.0 / 21.82 |
| flat_plants | 24.0 / 14.278 | 24.0 / 14.278 | 0.0 / 0.0 | 416.0 / 24.627 |
| renderer_off | 24.0 / 8.888 | 24.0 / 8.888 | 0.0 / 0.0 | 416.0 / 15.613 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.01 | 782.574 | 16 (vanilla 28) | 1370344 | 42.377 / 0.076 | 22.66 / 40.0 | 36.592 | 2471 / 0 | 1.95 (3.659) |
| flythrough | indirect, one multi-draw per arena and layer | 3.015 | 925.081 | 16 (vanilla 28) | 5330120 | 15.241 / 1.011 | 88.025 / 112.0 | 142.33 | 6690 / 0 | 3.922 (10.422) |
| water_glass | indirect, one multi-draw per arena and layer | 3.006 | 272.533 | 16 (vanilla 28) | 777912 | 34.619 / 0.072 | 12.529 / 40.0 | 20.772 | 3131 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.004 | 467.942 | 16 (vanilla 28) | 950188 | 29.137 / 0.181 | 15.122 / 20.0 | 25.373 | 4170 / 0 | 1.566 (2.405) |
| mobs | indirect, one multi-draw per arena and layer | 2.003 | 198.331 | 16 (vanilla 28) | 599580 | 26.529 / 0.159 | 9.582 / 16.0 | 16.011 | 4606 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 44.073 | 16 (vanilla 28) | 89596 | 26.054 / 0.158 | 1.439 / 16.0 | 2.392 | 4704 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 71.119 | 16 (vanilla 28) | 153316 | 25.038 / 0.151 | 2.46 / 20.0 | 4.094 | 4934 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 76.469 | 16 (vanilla 28) | 205688 | 23.739 / 0.143 | 3.302 / 20.0 | 5.492 | 5222 / 0 | 3.116 (7.309) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.005 | 248.385 | 16 (vanilla 28) | 798956 | 20.72 / 0.132 | 12.942 / 28.0 | 21.334 | 5994 / 0 | 3.578 (10.576) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.018 | 280.986 | 16 (vanilla 28) | 777912 | 7.465 / 0.043 | 12.529 / 20.0 | 20.772 | 1093 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.019 | 262.909 | 16 (vanilla 28) | 773096 | 6.803 / 0.114 | 12.531 / 28.0 | 20.644 | 2459 / 0 | 3.066 (6.181) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.006 | 286.563 | 16 (vanilla 28) | 889932 | 6.306 / 0.01 | 14.278 / 24.0 | 23.764 | 957 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 207.346 | 16 (vanilla 28) | 558288 | 2.383 / 0.021 | 8.888 / 24.0 | 14.908 | 606 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 20.646 / 0.131 | 0.131 / 28.0 | 0.225 | 6042 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.473 / 0.13 | 0.302 / 28.0 | 0.518 | 6144 / 0 | 1.011 (1.408) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.413 / 0.131 | 0.153 / 28.0 | 0.261 | 6194 / 0 | 0.69 (0.987) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 151.0 | 1.0 | 0.0 | 0 | 0 | 44 (0.017 / 0.029 / 0.035; 0) | 0 | 45904 / 0 / 0 |
| flythrough | 194.864 | 1.0 | 0.0 | 0 | 0 | 908 (0.075 / 0.033 / 47.992; 0) | 46 | 38583 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2044 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.012 / 0.023 / 0.028; 0) | 32 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.068 / 0.097 / 0.51; 0) | 180 | 3864 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 855 | 0 (None / None / None; 0) | 0 | 1140 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 798 | 0 (None / None / None; 0) | 0 | 1862 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2036 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2396 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 340.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.097; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 436.485 | 1.0 (0.0, 0.0) | 1.566 (0.0, 0.0) | 0.001 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 299.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.054; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.093 (0.245; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 29.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.044 (0.144; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.36 | 10.0 (9.0, 2.363) | 6.0 (6.0, 2.282) | 0.049 (0.272; 0) | 0 | 2787 / 1508956 / 0 / 0 |
| translucent_move | 135.109 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.444 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 153.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 113.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.0 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.005 (0.019; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.521 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 84.359 / 0.727 | 0.027 / 0.012 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 6.007 / 0.0 | 0.017 / 0.003 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.798 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.93 / 52.967 | 0.01 / 0.025 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 8.065 | 0.867 / 0.835 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 39.392 / 12.024 | 0.053 / 0.012 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.486 / 12.437 | 0.005 / 0.016 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.943 / 54.947 | 0.012 / 0.017 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.776 / 12.684 | 0.007 / 0.008 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 7.874 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.234 / 2.037 | 0.018 / 0.01 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 102.651 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.003 / 0.028 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.002 / 0.005 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.988 |
| animated_items | 136.426 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

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
| factory | 0.0 | 0.3 | 0.0 | 0.0 | 0.0002 |
| factory_flat | 0.0009 | 0.3 | 0.0 | 0.0043 | 0.0053 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| hologram | 0.0233 | 0.55 | 0.0066 | 0.0244 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.62 | 500 | 0 (15) | 0 (15) | 1 (15) |
| factory_flat | 95.28 | 40 | 0 (14) | 20 (34) | 23 (37) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 89.12 | 10 | 0 (0) | 0 (0) | 0 (0) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| map | 99.99 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 99.97 | 413 | 49 (4298) | 37 (4288) | None (None) |
| hud | 44.33 | 10 | 0 (0) | 0 (0) | 0 (0) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0 / 0 | 0.0 / 0 | 0.0141 / 245 |
| factory_flat | 0.0046 / 21 | 0.0058 / 32 | 0.0 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| map | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0025 / 7 | 0.0219 / 10 | skipped |
| hud | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.21 (base 111.52) | 15 (base 14) | 6956 (base 6488) | 422.1 (base 421.74) | 3.99 (base 5.48) |
| factory_flat | 20.23 (base 107.77) | 15 (base 14) | 10052 (base 6264) | 422.63 (base 422.37) | 5.48 (base 3.99) |
| sift | 22.94 (base 155.82) | 18 (base 19) | 16632 (base 81788) | 440.16 (base 440.07) | 3.99 (base 5.87) |
| hub | 21.54 (base 116.59) | 16 (base 18) | 66364 (base 67484) | 452.61 (base 451.33) | 3.99 (base 4.24) |
| mam | 22.52 (base 158.03) | 19 (base 21) | 23536 (base 24656) | 466.02 (base 471.8) | 5.99 (base 8.12) |
| ponder | 87.25 (base 181.05) | 14 (base 14) | 52248 (base 52248) | 464.72 (base 463.81) | 5.99 (base 8.24) |
| map | 30.0 (base 177.0) | 14 (base 14) | 46420 (base 47032) | 491.74 (base 504.13) | 5.81 (base 8.24) |
| hologram | 33 median (avg 32.69) (base 165) | 22 (base 24) | 73064 (base 70368) | 470.71 (base 453.57) | 5.93 (base 8.24) |
| hud | 30.94 (base 31.0) | 16 (base 16) | 71636 (base 71636) | 491.01 (base 494.92) | 5.99 (base 8.24) |
| create_ct | 6.67 (base 100.31) | 12 (base 12) | 1596 (base 1596) | 479.99 (base 470.81) | 6.06 (base 4.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.21 / 12 (29.98) | 105.71 / 152 (29.95) | median and average, x 1.10 + 2 |
| factory_flat | 20.23 / 20 (29.98) | 113.55 / 160 (29.97) | median and average, x 1.10 + 2 |
| sift | 22.94 / 23 (18.66) | 126.63 / 153 (24.59) | median, x 1.10 + 2 |
| hub | 21.54 / 21 (29.82) | 114.79 / 161 (30.0) | median and average, x 1.10 + 2 |
| mam | 22.52 / 22 (29.93) | 116.16 / 162 (29.91) | median, x 1.10 + 2 |
| ponder | 87.25 / 87 (29.99) | 180.57 / 227 (29.99) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (12.92) | 170.0 / 170 (15.83) | median and average, x 1.10 + 2 |
| hologram | 32.69 / 33 (28.81) | 126.24 / 173 (29.88) | median, at most vanilla |
| hud | 30.94 / 31 (19.86) | 158.36 / 171 (21.75) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 99.92 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.321 | 32.0 / 20.321 | 0.0 / 0.0 | 416.0 / 34.114 |
| ponder | 32.0 / 17.575 | 32.0 / 17.575 | 0.0 / 0.0 | 416.0 / 29.201 |
| map | 40.0 / 27.977 | 40.0 / 27.977 | 0.0 / 0.0 | 416.0 / 47.578 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.887 | 40.0 / 17.887 | 0.0 / 0.0 | 416.0 / 31.511 |
| create_ct | 40.0 / 0.649 | 40.0 / 0.649 | 0.0 / 0.0 | 416.0 / 1.192 |


Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.123 | 16 (vanilla 28) | 91632 | 25.996 / 0.308 | 1.453 / 28.0 | 2.447 | 1801 / 0 | 2.998 (5.205) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.123 | 16 (vanilla 28) | 91616 | 3.574 / 0.001 | 1.453 / 12.0 | 2.446 | 298 / 0 | 1.324 (1.239) |
| sift | indirect, one multi-draw per arena and layer | 3.008 | 430.15 | 16 (vanilla 28) | 462608 | 23.734 / 0.336 | 7.389 / 16.0 | 12.353 | 2811 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 23.056 / 0.325 | 1.879 / 12.0 | 3.137 | 2894 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1193636 | 23.763 / 0.354 | 20.321 / 32.0 | 31.874 | 3547 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1019844 | 23.762 / 0.354 | 17.575 / 32.0 | 27.233 | 3548 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.012 | 552.132 | 16 (vanilla 28) | 1755916 | 20.507 / 0.279 | 27.977 / 40.0 | 46.888 | 4389 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.003 | 13.023 | 16 (vanilla 28) | 13744 | 20.411 / 0.276 | 0.238 / 40.0 | 0.367 | 4451 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.008 | 405.018 | 16 (vanilla 28) | 1123060 | 16.65 / 0.235 | 17.887 / 40.0 | 29.989 | 6226 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 40272 | 16.492 / 0.233 | 0.649 / 40.0 | 1.075 | 6334 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2244 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 596 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 38.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 9842 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2308 / 0 / 0 |
| hud | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 14328 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.004 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.489 / 0.0 | 0.551 / 0.54 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 9.738 / 9.932 | 0.016 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.255 / 0.0 | 0.626 / 0.56 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.187 / 11.2 | 0.017 / 0.007 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 113.821 / 9.946 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.003 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.645 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 93.568 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 163.83 / 98.333 | 0.133 / 0.045 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 127.448 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.87 | 0.0 / 0.0 | 571.74 / 571.79 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.87 (8.0 / 8.0) |
| mam | 13.85 / 13.85 | 1.0 / 1.0 | 109.53 / 109.71 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.85 (8.0 / 8.0) |
| ponder | 82.58 / 82.57 | 0.0 / 0.11 | 203.09 / 203.16 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.6 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.21 (base 119.43) | 13 (base 13) | 49032 (base 48604) | 423.3 (base 421.54) | 4.24 (base 5.48) |
| factory_flat | 28.32 (base 112.31) | 13 (base 13) | 52224 (base 48340) | 422.08 (base 422.36) | 3.48 (base 5.48) |
| sift | 41.0 (base 670.27) | 16 (base 18) | 4148 (base 81788) | 441.22 (base 441.24) | 4.24 (base 4.24) |
| hub | 25.54 (base 162.94) | 14 (base 17) | 64756 (base 66484) | 453.87 (base 451.46) | 4.24 (base 5.99) |
| mam | 24.53 (base 222.38) | 17 (base 20) | 20916 (base 22916) | 465.99 (base 472.87) | 8.24 (base 5.99) |
| ponder | 87.25 (base 180.81) | 12 (base 12) | 52212 (base 52212) | 464.66 (base 463.76) | 8.24 (base 8.24) |
| map | 67.0 (base 225.0) | 12 (base 12) | 30924 (base 31404) | 492.58 (base 506.16) | 8.06 (base 6.31) |
| hologram | 34 median (avg 33.81) (base 166) | 20 (base 23) | 71472 (base 70624) | 471.6 (base 452.39) | 8.18 (base 6.31) |
| hud | 58.0 (base 58.0) | 14 (base 14) | 59972 (base 59972) | 491.69 (base 493.56) | 8.24 (base 8.18) |
| create_ct | 6.67 (base 99.92) | 10 (base 10) | 1124 (base 1124) | 481.47 (base 470.06) | 8.24 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.21 / 20 (29.67) | 179.31 / 226 (29.96) | median and average, x 1.10 + 2 |
| factory_flat | 28.32 / 28 (29.85) | 187.55 / 234 (30.01) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (15.62) | 556.64 / 579 (23.69) | median, x 1.10 + 2 |
| hub | 25.54 / 25 (29.8) | 162.03 / 208 (29.92) | median and average, x 1.10 + 2 |
| mam | 24.53 / 24 (29.75) | 199.78 / 246 (29.85) | median, x 1.10 + 2 |
| ponder | 87.25 / 87 (29.83) | 180.5 / 227 (29.99) | median and average, x 1.10 + 2 |
| map | 67.0 / 67 (10.97) | 714.0 / 714 (16.8) | median and average, x 1.10 + 2 |
| hologram | 33.81 / 34 (24.48) | 138.03 / 184 (29.62) | median, at most vanilla |
| hud | 58.0 / 58 (16.29) | 544.2 / 572 (24.81) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 111.0 / 158 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.575 | 12.0 / 1.575 | 0.0 / 0.0 | 416.0 / 2.645 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 19.997 | 32.0 / 19.997 | 0.0 / 0.0 | 416.0 / 34.137 |
| ponder | 32.0 / 17.257 | 32.0 / 17.257 | 0.0 / 0.0 | 416.0 / 29.224 |
| map | 40.0 / 27.977 | 40.0 / 27.977 | 0.0 / 0.0 | 416.0 / 47.575 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.614 | 40.0 / 17.614 | 0.0 / 0.0 | 416.0 / 31.51 |
| create_ct | 40.0 / 0.376 | 40.0 / 0.376 | 0.0 / 0.0 | 416.0 / 1.192 |


Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.013 | 74.125 | 16 (vanilla 28) | 91632 | 24.84 / 0.343 | 1.453 / 28.0 | 2.447 | 1924 / 0 | 3.714 (5.968) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 99196 | 3.525 / 0.001 | 1.575 / 12.0 | 2.649 | 310 / 0 | 1.639 (1.899) |
| sift | plain, one multi-draw per region and layer | 21.067 | 430.375 | 16 (vanilla 28) | 462608 | 22.867 / 0.363 | 7.389 / 16.0 | 12.353 | 2937 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.084 | 16 (vanilla 28) | 117492 | 22.247 / 0.352 | 1.879 / 12.0 | 3.137 | 3014 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.146 | 16 (vanilla 28) | 1175476 | 23.042 / 0.375 | 19.997 / 32.0 | 31.389 | 3645 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1002052 | 23.042 / 0.375 | 17.257 / 32.0 | 26.758 | 3646 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 40.183 | 552.511 | 16 (vanilla 28) | 1755916 | 19.993 / 0.296 | 27.977 / 40.0 | 46.888 | 4487 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.006 | 13.027 | 16 (vanilla 28) | 13744 | 19.906 / 0.293 | 0.238 / 40.0 | 0.367 | 4549 / 0 | None (None) |
| hud | plain, one multi-draw per region and layer | 30.092 | 405.243 | 16 (vanilla 28) | 1106324 | 16.32 / 0.238 | 17.614 / 40.0 | 29.542 | 6308 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 23536 | 16.174 / 0.236 | 0.376 / 40.0 | 0.628 | 6412 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1878 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 596 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 595 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 38.0 | 16.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 8360 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1960 / 0 / 0 |
| hud | 36.0 | 9.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 11736 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.043; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.033; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 281.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.008; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.1 / 0.0 | 0.543 / 0.535 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.783 / 10.165 | 0.017 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.255 / 0.0 | 0.626 / 0.648 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.265 / 12.05 | 0.018 / 0.005 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 117.801 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 94.358 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 150.97 / 90.541 | 0.148 / 0.041 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 112.394 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.87 | 0.0 / 0.0 | 571.56 / 571.68 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.87 (8.0 / 8.0) |
| mam | 13.86 / 13.86 | 1.0 / 1.0 | 109.89 / 110.11 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.86 (8.0 / 8.0) |
| ponder | 82.58 / 82.58 | 0.11 / 0.11 | 203.03 / 203.27 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.61 / 81.61 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.18 (base 111.25) | 15 (base 15) | 51524 (base 51148) | 421.53 (base 420.23) | 4.23 (base 3.71) |
| factory_flat | 22.31 (base 107.27) | 15 (base 15) | 54824 (base 50904) | 422.3 (base 421.17) | 3.46 (base 3.46) |
| sift | 23.0 (base 150.31) | 18 (base 19) | 16632 (base 81788) | 439.36 (base 439.9) | 4.23 (base 4.23) |
| hub | 21.55 (base 136.98) | 16 (base 18) | 66364 (base 67484) | 452.34 (base 450.49) | 4.23 (base 4.23) |
| mam | 22.52 (base 171.53) | 19 (base 21) | 23536 (base 24656) | 465.26 (base 462.56) | 8.23 (base 5.46) |
| ponder | 87.23 (base 180.89) | 14 (base 14) | 52248 (base 52248) | 464.18 (base 462.87) | 8.23 (base 8.23) |
| map | 30.0 (base 177.0) | 14 (base 14) | 46344 (base 47072) | 490.55 (base 504.09) | 8.1 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| hud | 31.0 (base 31.0) | 16 (base 16) | 72644 (base 72908) | 477.79 (base 490.43) | 8.16 (base 8.16) |
| create_ct | 6.67 (base 100.16) | 12 (base 12) | 1596 (base 1596) | 471.82 (base 475.26) | 8.16 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.18 / 14 (29.58) | 109.12 / 154 (29.2) | median and average, x 1.10 + 2 |
| factory_flat | 22.31 / 22 (29.96) | 117.18 / 162 (29.39) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (15.6) | 153.0 / 153 (12.8) | median, x 1.10 + 2 |
| hub | 21.55 / 21 (29.75) | 118.36 / 161 (28.75) | median and average, x 1.10 + 2 |
| mam | 22.52 / 22 (29.86) | 126.66 / 162 (26.65) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (29.97) | 180.89 / 227 (29.92) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (11.41) | 170.0 / 170 (9.1) | median and average, x 1.10 + 2 |
| hud | 31.0 / 31 (16.26) | 171.0 / 171 (12.61) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 99.92 / 147 (29.99) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.374 | 16.0 / 7.374 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.2 | 32.0 / 20.2 | 0.0 / 0.0 | 416.0 / 33.926 |
| ponder | 32.0 / 17.455 | 32.0 / 17.455 | 0.0 / 0.0 | 416.0 / 29.013 |
| map | 40.0 / 27.169 | 40.0 / 27.169 | 0.0 / 0.0 | 416.0 / 47.413 |
| hud | 56.0 / 15.705 | 56.0 / 15.705 | 0.0 / 0.0 | 416.0 / 27.447 |
| create_ct | 56.0 / 0.171 | 56.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.125 | 16 (vanilla 28) | 91632 | 27.534 / 0.3 | 1.453 / 28.0 | 2.447 | 1840 / 0 | 2.219 (2.168) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 3.372 / 0.002 | 1.453 / 12.0 | 2.447 | 270 / 0 | 1.179 (1.2) |
| sift | indirect, one multi-draw per arena and layer | 3.01 | 430.375 | 16 (vanilla 28) | 461644 | 24.969 / 0.329 | 7.374 / 16.0 | 12.327 | 2846 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 24.249 / 0.319 | 1.879 / 12.0 | 3.137 | 2927 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.146 | 16 (vanilla 28) | 1187212 | 24.7 / 0.349 | 20.2 / 32.0 | 31.702 | 3573 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1013420 | 24.7 / 0.349 | 17.455 / 32.0 | 27.061 | 3574 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.013 | 549.399 | 16 (vanilla 28) | 1705544 | 21.183 / 0.277 | 27.169 / 40.0 | 45.543 | 4376 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.009 | 441.354 | 16 (vanilla 28) | 986956 | 16.697 / 0.238 | 15.705 / 56.0 | 26.355 | 6040 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.558 / 0.236 | 0.171 / 56.0 | 0.283 | 6137 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1878 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 595 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 37.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 8473 / 0 / 0 |
| hud | 39.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12714 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 280.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 243.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.012; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.854 / 0.0 | 0.588 / 0.504 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 9.76 / 9.573 | 0.015 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 94.923 / 0.0 | 0.583 / 0.644 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.968 / 11.772 | 0.02 / 0.004 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.003 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 96.829 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 104.082 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.645 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 16.794 / 0.0 | 0.005 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.84 | 0.0 / 0.0 | 571.78 / 570.3 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.84 (8.0 / 8.0) |
| mam | 13.86 / 13.84 | 1.0 / 1.0 | 109.99 / 109.48 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.84 (8.0 / 8.0) |
| ponder | 82.57 / 82.58 | 0.11 / 0.11 | 203.03 / 203.21 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.61 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

