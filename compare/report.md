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
| spawn | 0.0558 | 10.05 | 0.2007 | 0.1999 | 0.0638 |
| flythrough | 0.0 | 14.95 | 0.001 | 0.001 | 0.001 |
| water_glass | 0.0035 | 6.45 | 0.0039 | 0.0049 | 0.0049 |
| nether | 0.0039 | 0.8 | 0.0048 | 0.0011 | 0.0003 |
| mobs | 0.0829 | 0.4 | 0.1104 | 0.0802 | 0.1186 |
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
| translucent_move | 0.0 | 0.3 | 0.0 | 0.0001 | 0.0 |
| translucent_move.x2404 | 0.0002 | 0.3 | 0.0002 | 0.0004 | 0.0007 |
| translucent_move.x2414 | 0.0004 | 0.3 | 0.0003 | 0.0002 | 0.0001 |
| translucent_move.x2424 | 0.0001 | 0.3 | 0.0002 | 0.0004 | 0.0003 |
| translucent_move.low | 0.0005 | 0.3 | 0.0016 | 0.0014 | 0.0011 |
| translucent_move.back | 0.0002 | 0.3 | 0.0001 | 0.0 | 0.0 |
| translucent_move.bare_back | 0.0001 | 0.3 | 0.0002 | 0.0002 | 0.0 |
| water_glass_oit | 0.005 | 0.3 | 0.0056 | 0.0046 | 0.0036 |
| translucent_move_oit | 0.0003 | 0.3 | 0.0001 | 0.0 | 0.0003 |
| translucent_move_oit.x2404 | 0.0001 | 0.3 | 0.0003 | 0.0004 | 0.0008 |
| translucent_move_oit.x2414 | 0.0003 | 0.3 | 0.0002 | 0.0002 | 0.0002 |
| translucent_move_oit.x2424 | 0.0001 | 0.3 | 0.0003 | 0.0003 | 0.0002 |
| translucent_move_oit.low | 0.0002 | 0.3 | 0.0011 | 0.0011 | 0.0016 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 | 0.0 | 0.0002 |
| translucent_move_oit.bare_back | 0.0001 | 0.3 | 0.0001 | 0.0001 | 0.0 |
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
| spawn | 92.81 | 18057 | 1566 (2074) | 1512 (2019) | 334 (872) |
| flythrough | 97.26 | 6020 | 48 (293) | 48 (293) | 51 (294) |
| water_glass | 95.81 | 25508 | 15 (2617) | 24 (2626) | 19 (2734) |
| nether | 99.42 | 905 | 8 (463) | 7 (462) | 3 (470) |
| mobs | 99.27 | 600 | 115 (115) | 89 (89) | 270 (270) |
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
| translucent_move | 96.93 | 13 | 3 (79) | 2 (77) | 1 (67) |
| translucent_move.x2404 | 95.93 | 15 | 3 (103) | 4 (104) | 8 (93) |
| translucent_move.x2414 | 95.86 | 13 | 2 (105) | 1 (104) | 1 (98) |
| translucent_move.x2424 | 96.74 | 13 | 2 (79) | 2 (79) | 3 (69) |
| translucent_move.low | 96.11 | 41 | 17 (22) | 16 (21) | 14 (16) |
| translucent_move.back | 96.93 | 13 | 5 (80) | 1 (76) | 2 (68) |
| translucent_move.bare_back | 96.9 | 13 | 2 (77) | 4 (79) | 3 (70) |
| water_glass_oit | 95.8 | 57 | 27 (2629) | 20 (2623) | 13 (2728) |
| translucent_move_oit | 96.93 | 12 | 2 (77) | 1 (76) | 3 (69) |
| translucent_move_oit.x2404 | 95.93 | 16 | 6 (106) | 5 (105) | 7 (91) |
| translucent_move_oit.x2414 | 95.86 | 10 | 1 (105) | 1 (104) | 3 (100) |
| translucent_move_oit.x2424 | 96.74 | 15 | 3 (80) | 3 (80) | 2 (68) |
| translucent_move_oit.low | 96.11 | 40 | 12 (18) | 14 (19) | 15 (17) |
| translucent_move_oit.back | 96.93 | 12 | 1 (76) | 1 (76) | 3 (69) |
| translucent_move_oit.bare_back | 96.9 | 15 | 3 (78) | 4 (79) | 2 (68) |
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
| spawn | 0.197 / 1460 | 0.0646 / 183 | 0.0541 / 235 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| water_glass | 0.0055 / 31 | 0.0033 / 11 | 0.0046 / 22 |
| nether | 0.0039 / 0 | 0.0065 / 57 | 0.0 / 0 |
| mobs | 0.112 / 173 | 0.1323 / 317 | 0.1011 / 86 |
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
| translucent_move | 0.0001 / 2 | 0.0001 / 0 | 0.0001 / 1 |
| translucent_move.x2404 | 0.0001 / 2 | 0.0002 / 1 | 0.0002 / 2 |
| translucent_move.x2414 | 0.0002 / 1 | 0.0001 / 1 | 0.0001 / 2 |
| translucent_move.x2424 | 0.0 / 1 | 0.0002 / 0 | 0.0003 / 1 |
| translucent_move.low | 0.0012 / 8 | 0.0002 / 2 | 0.0003 / 3 |
| translucent_move.back | 0.0 / 0 | 0.0001 / 1 | 0.0001 / 1 |
| translucent_move.bare_back | 0.0001 / 1 | 0.0002 / 0 | 0.0 / 0 |
| water_glass_oit | 0.005 / 19 | 0.0042 / 19 | 0.0031 / 14 |
| translucent_move_oit | 0.0002 / 2 | 0.0001 / 2 | 0.0001 / 0 |
| translucent_move_oit.x2404 | 0.0 / 0 | 0.0001 / 2 | 0.0002 / 0 |
| translucent_move_oit.x2414 | 0.0002 / 3 | 0.0002 / 4 | 0.0001 / 3 |
| translucent_move_oit.x2424 | 0.0001 / 2 | 0.0001 / 2 | 0.0001 / 0 |
| translucent_move_oit.low | 0.0007 / 4 | 0.0004 / 6 | 0.0009 / 5 |
| translucent_move_oit.back | 0.0 / 0 | 0.0001 / 4 | 0.0002 / 2 |
| translucent_move_oit.bare_back | 0.0 / 0 | 0.0 / 2 | 0.0 / 0 |
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
| spawn | 14.35 (base 123.09) | 15 (base 14) | 27068 (base 22476) | 257.38 (base 259.07) | 3.99 (base 3.99) |
| flythrough | 13.06 (reported, not gated) | 33 (reported, not gated) | 346604 (reported, not gated) | 319.82 (base 324.25) | 5.99 (base 4.24) |
| water_glass | 8.96 (base 182.47) | 15 (base 15) | 15240 (base 15576) | 244.91 (base 250.02) | 3.99 (base 5.99) |
| nether | 15.64 (base 176.56) | 12 (base 12) | 18224 (base 19648) | 266.32 (base 273.53) | 3.99 (base 5.99) |
| mobs | 19.72 (base 216.22) | 17 (base 17) | 616328 (base 1010948) | 264.58 (base 268.37) | 3.99 (base 6.99) |
| occlusion | 15.67 (base 96.43) | 15 (base 15) | 100408 (base 126320) | 266.2 (base 265.1) | 3.99 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82280 (reported, not gated) | 266.34 (base 265.74) | 3.99 (base 4.24) |
| translucent_move | 8.9 (reported, not gated) | 15 (reported, not gated) | 10256 (reported, not gated) | 268.86 (base 267.48) | 5.99 (base 5.48) |
| water_glass_oit | 14.0 (base 131.0) | 15 (base 15) | 15296 (base 14688) | 246.64 (base 245.02) | 4.12 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10124 (reported, not gated) | 248.81 (base 247.19) | 4.24 (base 3.73) |
| flat_plants | 8.85 (base 123.98) | 15 (base 15) | 9940 (base 9812) | 248.27 (base 246.92) | 3.99 (base 3.99) |
| renderer_off | 7.67 (base 96.01) | 14 (base 14) | 7272 (base 7164) | 242.53 (base 241.33) | 3.87 (base 4.12) |
| particles | 9.67 (base 10.67) | 15 (base 15) | 9204 (base 17128) | 263.86 (base 261.44) | 5.99 (base 5.99) |
| animation | 49 median (avg 39.93) (base 49) | 15 (base 15) | 2168 (base 2056) | 264.84 (base 260.68) | 5.99 (base 5.99) |
| animated_items | 60 median (avg 46.69) (base 60) | 23 (base 23) | 21940 (base 21940) | 239.93 (base 239.81) | 5.99 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 14.35 / 14 (11.97) | 124.0 / 124 (9.41) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (7.92) | 123.05 / 123 (5.76) | median and average, at most vanilla + 2 |
| water_glass | 8.96 / 9 (20.83) | 124.0 / 124 (16.34) | median, at most vanilla |
| nether | 15.64 / 16 (20.16) | 120.74 / 121 (16.01) | median, x 1.10 + 2 |
| mobs | 19.72 / 20 (27.82) | 130.86 / 134 (20.37) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.33 / 133 (29.97) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.98) | 94.33 / 133 (29.98) | median and average, x 1.10 + 2 |
| translucent_move | 8.9 / 9 (22.09) | 124.0 / 124 (16.6) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (11.11) | 129.0 / 129 (9.25) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (10.64) | 129.0 / 129 (8.65) | median and average, at most vanilla |
| flat_plants | 8.85 / 9 (20.1) | 123.89 / 124 (16.58) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.91) | 95.21 / 123 (26.31) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.93 / 49 (30.0) | 144.09 / 179 (30.01) | median, x 1.10 + 2 |
| animated_items | 46.69 / 60 (30.0) | 155.09 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.777 | 36.0 / 21.777 | 0.0 / 0.0 | 416.0 / 37.362 |
| flythrough | 112.0 / 86.463 | 112.0 / 86.463 | 0.0 / 0.0 | 416.0 / 144.741 |
| water_glass | 36.0 / 12.456 | 36.0 / 12.456 | 0.0 / 0.0 | 416.0 / 21.564 |
| nether | 20.0 / 15.183 | 20.0 / 15.183 | 0.0 / 0.0 | 256.0 / 26.624 |
| mobs | 16.0 / 9.65 | 16.0 / 9.65 | 0.0 / 0.0 | 256.0 / 16.833 |
| occlusion | 20.0 / 2.812 | 20.0 / 2.812 | 0.0 / 0.0 | 416.0 / 4.496 |
| occlusion_move | 20.0 / 3.686 | 20.0 / 3.686 | 0.0 / 0.0 | 416.0 / 6.088 |
| translucent_move | 28.0 / 13.5 | 28.0 / 13.5 | 0.0 / 0.0 | 416.0 / 22.81 |
| water_glass_oit | 28.0 / 12.43 | 28.0 / 12.43 | 0.0 / 0.0 | 416.0 / 21.564 |
| translucent_move_oit | 28.0 / 13.14 | 28.0 / 13.14 | 0.0 / 0.0 | 416.0 / 22.191 |
| flat_plants | 28.0 / 14.559 | 28.0 / 14.559 | 0.0 / 0.0 | 416.0 / 24.838 |
| renderer_off | 28.0 / 9.031 | 28.0 / 9.031 | 0.0 / 0.0 | 416.0 / 15.824 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.297 | 28.0 / 0.297 | 0.0 / 0.0 | 256.0 / 0.536 |
| animated_items | 28.0 / 0.146 | 28.0 / 0.146 | 0.0 / 0.0 | 128.0 / 0.26 |


Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.013 | 733.054 | 16 (vanilla 28) | 1313940 | 40.672 / 0.088 | 21.777 / 36.0 | 35.086 | 2317 / 0 | 3.594 (6.599) |
| flythrough | indirect, one multi-draw per arena and layer | 3.019 | 920.911 | 16 (vanilla 28) | 5238000 | 16.684 / 0.976 | 86.463 / 112.0 | 139.87 | 6373 / 0 | 5.105 (12.954) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 273.656 | 16 (vanilla 28) | 771136 | 32.822 / 0.083 | 12.456 / 36.0 | 20.592 | 2995 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 469.161 | 16 (vanilla 28) | 952916 | 27.541 / 0.171 | 15.183 / 20.0 | 25.446 | 4044 / 0 | 2.439 (4.995) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.354 | 16 (vanilla 28) | 600924 | 25.068 / 0.151 | 9.65 / 16.0 | 16.046 | 4487 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 73.122 | 16 (vanilla 28) | 176180 | 23.419 / 0.14 | 2.812 / 20.0 | 4.705 | 4778 / 0 | 3.526 (6.34) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 78.5 | 16 (vanilla 28) | 231276 | 22.118 / 0.132 | 3.686 / 20.0 | 6.176 | 5056 / 0 | 4.406 (9.262) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.007 | 253.374 | 16 (vanilla 28) | 834432 | 18.885 / 0.119 | 13.5 / 28.0 | 22.282 | 5910 / 0 | 6.921 (18.355) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.023 | 280.262 | 16 (vanilla 28) | 769476 | 11.304 / 0.062 | 12.43 / 28.0 | 20.547 | 1322 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.024 | 268.476 | 16 (vanilla 28) | 811872 | 8.406 / 0.106 | 13.14 / 28.0 | 21.679 | 2661 / 0 | 5.616 (13.588) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.007 | 288.718 | 16 (vanilla 28) | 909560 | 18.141 / 0.052 | 14.559 / 28.0 | 24.288 | 1600 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.342 | 16 (vanilla 28) | 566496 | 9.369 / 0.054 | 9.031 / 28.0 | 15.127 | 885 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 18.839 / 0.118 | 0.131 / 28.0 | 0.225 | 5954 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 13.022 | 16 (vanilla 28) | 19088 | 18.685 / 0.117 | 0.297 / 28.0 | 0.51 | 6054 / 0 | 1.243 (1.733) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 9304 | 18.637 / 0.118 | 0.146 / 28.0 | 0.248 | 6099 / 0 | 0.716 (1.043) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 149.0 | 1.0 | 0.0 | 0 | 0 | 45 (0.029 / 0.058 / 0.066; 0) | 0 | 35760 / 0 / 0 |
| flythrough | 193.145 | 1.0 | 0.0 | 0 | 0 | 885 (0.02 / 0.036 / 4.176; 0) | 56 | 30710 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1668 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.168 / 0.026 / 5.768; 0) | 32 | 1202 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 191 (0.082 / 0.131 / 0.28; 0) | 191 | 3094 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 666 | 0 (None / None / None; 0) | 0 | 888 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 639 | 0 (None / None / None; 0) | 0 | 1491 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1608 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 315.0 | 2.0 (1.0, 0.0) | 11.0 (1.0, 0.0) | 0.04 (0.115; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 439.333 | 1.0 (0.0, 0.0) | 1.151 (0.0, 0.0) | 0.001 (0.01; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.059; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.123 (0.27; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 50.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.062 (0.092; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 52.364 | 10.0 (9.0, 2.359) | 6.0 (6.0, 2.273) | 0.055 (0.96; 0) | 0 | 2784 / 1507378 / 0 / 0 |
| translucent_move | 137.079 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 137.653 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.007; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 12.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 12.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.035; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.351 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 134.673 / 0.496 | 0.031 / 0.012 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 3.863 / 0.0 | 0.018 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.855 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.693 / 55.549 | 0.016 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 9.851 | 0.744 / 0.801 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 36.359 / 6.064 | 0.065 / 0.008 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 111.892 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.398 / 13.348 | 0.005 / 0.014 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.28 / 55.252 | 0.012 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.3 / 12.277 | 0.005 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 0.0 | 0.885 / 0.863 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.825 / 2.59 | 0.016 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| renderer_off | 87.452 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.006 / 0.05 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.26 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.003 / 0.01 | 56 (3) | 40.51 / 9.235 / 31.275 | 3 / 0 | 10.985 |
| animated_items | 136.426 / 28.028 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.51 / 7.97 / 32.54 | 0 / 0 | 9.0 |

### Counts, core old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 46.52 (base 844.2) | 13 (base 13) | 1492 (base 6372) | 258.27 (base 256.29) | 4.24 (base 3.73) |
| flythrough | 66.46 (reported, not gated) | 52 (reported, not gated) | 776100 (reported, not gated) | 317.96 (base 324.79) | 3.99 (base 5.99) |
| water_glass | 20.0 (base 457.8) | 13 (base 14) | 7204 (base 10264) | 244.65 (base 249.83) | 4.24 (base 5.99) |
| nether | 33.92 (base 640.77) | 10 (base 11) | 4644 (base 10656) | 265.39 (base 272.57) | 4.24 (base 5.99) |
| mobs | 29.0 (base 410.19) | 15 (base 16) | 610676 (base 1015892) | 264.07 (base 268.23) | 4.24 (base 5.99) |
| occlusion | 22.67 (base 106.62) | 13 (base 14) | 98148 (base 124172) | 265.84 (base 264.64) | 4.24 (base 4.24) |
| occlusion_move | 24.04 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 265.96 (base 265.66) | 4.24 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 268.81 (base 268.19) | 4.24 (base 3.73) |
| water_glass_oit | 25.0 (base 154.0) | 13 (base 14) | 7428 (base 7604) | 245.66 (base 245.78) | 4.12 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 248.44 (base 248.15) | 4.24 (base 3.73) |
| flat_plants | 22.88 (base 137.98) | 13 (base 13) | 1604 (base 1716) | 248.13 (base 248.11) | 5.99 (base 5.99) |
| renderer_off | 17.77 (base 107.98) | 12 (base 12) | 1380 (base 1380) | 240.75 (base 240.73) | 4.12 (base 5.87) |
| particles | 10.67 (base 11.67) | 13 (base 13) | 8772 (base 16164) | 263.3 (base 261.4) | 4.24 (base 4.24) |
| animation | 49 median (avg 39.94) (base 49) | 13 (base 13) | 1716 (base 1716) | 264.42 (base 260.68) | 4.24 (base 4.24) |
| animated_items | 60 median (avg 46.73) (base 60) | 21 (base 22) | 21508 (base 21668) | 239.9 (base 239.53) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 46.52 / 47 (9.37) | 876.0 / 877 (10.03) | median and average, x 1.10 + 2 |
| flythrough | 66.46 / 64 (5.9) | 1009.29 / 1013 (5.72) | median and average, at most vanilla |
| water_glass | 20.0 / 20 (16.71) | 390.0 / 390 (16.63) | median, at most vanilla |
| nether | 33.92 / 34 (16.12) | 586.53 / 587 (16.41) | median, x 1.10 + 2 |
| mobs | 29.0 / 29 (19.84) | 322.85 / 329 (20.78) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (29.97) | 162.33 / 201 (29.97) | median and average, x 1.10 + 2 |
| occlusion_move | 24.04 / 24 (29.81) | 167.62 / 202 (29.97) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (16.71) | 369.92 / 369 (17.05) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (9.37) | 401.0 / 401 (8.28) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (8.98) | 386.7 / 386 (8.54) | median and average, at most vanilla |
| flat_plants | 22.88 / 23 (16.43) | 397.73 / 398 (17.23) | median, x 1.10 + 2 |
| renderer_off | 17.77 / 18 (26.07) | 304.43 / 325 (24.27) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.01) | 97.32 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 155.09 / 190 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 166.22 / 201 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.862 | 36.0 / 21.862 | 0.0 / 0.0 | 416.0 / 38.336 |
| flythrough | 112.0 / 83.207 | 112.0 / 83.207 | 0.0 / 0.0 | 416.0 / 143.12 |
| water_glass | 36.0 / 12.456 | 36.0 / 12.456 | 0.0 / 0.0 | 416.0 / 21.564 |
| nether | 20.0 / 15.176 | 20.0 / 15.176 | 0.0 / 0.0 | 256.0 / 26.628 |
| mobs | 16.0 / 9.65 | 16.0 / 9.65 | 0.0 / 0.0 | 256.0 / 16.833 |
| occlusion | 20.0 / 2.63 | 20.0 / 2.63 | 0.0 / 0.0 | 416.0 / 4.323 |
| occlusion_move | 20.0 / 3.504 | 20.0 / 3.504 | 0.0 / 0.0 | 416.0 / 5.878 |
| translucent_move | 28.0 / 13.176 | 28.0 / 13.176 | 0.0 / 0.0 | 416.0 / 22.497 |
| water_glass_oit | 28.0 / 12.456 | 28.0 / 12.456 | 0.0 / 0.0 | 416.0 / 21.564 |
| translucent_move_oit | 28.0 / 12.819 | 28.0 / 12.819 | 0.0 / 0.0 | 416.0 / 21.878 |
| flat_plants | 28.0 / 14.249 | 28.0 / 14.249 | 0.0 / 0.0 | 416.0 / 24.667 |
| renderer_off | 20.0 / 9.023 | 20.0 / 9.023 | 0.0 / 0.0 | 416.0 / 15.875 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.297 | 28.0 / 0.297 | 0.0 / 0.0 | 256.0 / 0.536 |
| animated_items | 28.0 / 0.146 | 28.0 / 0.146 | 0.0 / 0.0 | 128.0 / 0.26 |


Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 36.193 | 738.93 | 16 (vanilla 28) | 1318896 | 41.588 / 0.078 | 21.862 / 36.0 | 35.218 | 2265 / 0 | 5.099 (8.269) |
| flythrough | plain, one multi-draw per region and layer | 56.814 | 900.678 | 16 (vanilla 28) | 5039324 | 17.363 / 1.022 | 83.207 / 112.0 | 134.564 | 6013 / 0 | 7.42 (18.724) |
| water_glass | plain, one multi-draw per region and layer | 14.042 | 273.817 | 16 (vanilla 28) | 771136 | 33.719 / 0.071 | 12.456 / 36.0 | 20.592 | 2962 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.062 | 469.453 | 16 (vanilla 28) | 952836 | 28.213 / 0.172 | 15.176 / 20.0 | 25.443 | 4013 / 0 | 1.928 (2.897) |
| mobs | plain, one multi-draw per region and layer | 11.028 | 197.497 | 16 (vanilla 28) | 600924 | 25.615 / 0.15 | 9.65 / 16.0 | 16.046 | 4455 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.118 | 16 (vanilla 28) | 164180 | 24.373 / 0.143 | 2.63 / 20.0 | 4.384 | 4694 / 0 | 6.597 (6.057) |
| occlusion_move | plain, one multi-draw per region and layer | 10.997 | 76.44 | 16 (vanilla 28) | 219040 | 22.948 / 0.134 | 3.504 / 20.0 | 5.849 | 4990 / 0 | 6.108 (13.637) |
| translucent_move | plain, one multi-draw per region and layer | 12.036 | 249.299 | 16 (vanilla 28) | 813524 | 19.859 / 0.122 | 13.176 / 28.0 | 21.723 | 5765 / 0 | 7.075 (18.231) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.086 | 282.503 | 16 (vanilla 28) | 771136 | 13.174 / 0.072 | 12.456 / 28.0 | 20.592 | 1453 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.615 | 264.425 | 16 (vanilla 28) | 790960 | 9.368 / 0.099 | 12.819 / 28.0 | 21.121 | 2761 / 0 | 7.645 (18.334) |
| flat_plants | plain, one multi-draw per region and layer | 17.052 | 284.866 | 16 (vanilla 28) | 889204 | 19.342 / 0.047 | 14.249 / 28.0 | 23.744 | 1527 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.025 | 205.393 | 16 (vanilla 28) | 566352 | 16.437 / 0.093 | 9.023 / 20.0 | 15.123 | 1152 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 19.81 / 0.122 | 0.131 / 28.0 | 0.225 | 5807 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 13.022 | 16 (vanilla 28) | 19088 | 19.641 / 0.121 | 0.297 / 28.0 | 0.51 | 5907 / 0 | 1.751 (2.141) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 12.02 | 16 (vanilla 28) | 9304 | 19.583 / 0.121 | 0.146 / 28.0 | 0.248 | 5955 / 0 | 0.902 (1.177) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 151.0 | 14.0 | 0.0 | 0 | 0 | 35 (0.116 / 0.052 / 3.054; 0) | 0 | 28388 / 0 / 0 |
| flythrough | 187.101 | 25.723 | 0.0 | 0 | 0 | 818 (0.032 / 0.052 / 6.049; 0) | 39 | 22265 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1340 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.011 / 0.018 / 0.027; 0) | 32 | 1194 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.117 / 0.163 / 2.937; 0) | 180 | 2345 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 564 | 0 (None / None / None; 0) | 0 | 752 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 540 | 0 (None / None / None; 0) | 0 | 1260 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1316 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2088 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 317.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.027 (0.087; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 435.798 | 1.0 (0.0, 0.0) | 1.218 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.041; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.143 (0.215; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.074 (0.208; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 51.347 | 10.0 (9.0, 2.36) | 6.0 (6.0, 2.28) | 0.064 (0.288; 0) | 0 | 2770 / 1499110 / 0 / 0 |
| translucent_move | 134.982 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.611 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 12.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 12.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.046; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.519 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 117.342 / 0.521 | 0.023 / 0.009 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 4.83 / 0.0 | 0.019 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.824 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.761 / 52.764 | 0.014 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.534 / 0.919 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 47.113 / 10.511 | 0.076 / 0.009 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 108.904 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.224 / 12.128 | 0.004 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.439 / 54.415 | 0.012 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.893 / 12.783 | 0.006 / 0.008 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 0.0 | 0.73 / 0.881 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.19 / 2.012 | 0.013 / 0.007 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| renderer_off | 94.608 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.042 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.277 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.005 / 0.011 | 56 (3) | 40.51 / 9.24 / 31.27 | 3 / 0 | 10.987 |
| animated_items | 136.553 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

### Counts, core vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| spawn | 13.61 (base 124.84) | 15 (base 15) | 21264 (base 27764) | 257.24 (base 257.2) | 3.98 (base 3.98) |
| flythrough | 13.06 (reported, not gated) | 44 (reported, not gated) | 755684 (reported, not gated) | 318.21 (base 318.06) | 3.71 (base 4.23) |
| water_glass | 9.0 (base 153.28) | 15 (base 15) | 15352 (base 15108) | 244.84 (base 244.39) | 3.98 (base 4.23) |
| nether | 15.88 (base 142.02) | 12 (base 12) | 19008 (base 19912) | 267.69 (base 268.95) | 3.98 (base 4.23) |
| mobs | 19.97 (base 208.6) | 17 (base 17) | 616328 (base 1008176) | 264.33 (base 264.31) | 3.98 (base 6.23) |
| occlusion | 15.67 (base 97.95) | 15 (base 15) | 100352 (base 126356) | 266.24 (base 264.78) | 3.98 (base 3.98) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82224 (reported, not gated) | 266.16 (base 265.16) | 3.98 (base 4.23) |
| translucent_move | 9.0 (reported, not gated) | 15 (reported, not gated) | 10256 (reported, not gated) | 268.98 (base 268.35) | 5.98 (base 5.46) |
| water_glass_oit | 14.0 (base 131.0) | 15 (base 15) | 15184 (base 15024) | 245.79 (base 245.99) | 5.34 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10348 (reported, not gated) | 248.44 (base 248.38) | 5.46 (base 3.46) |
| flat_plants | 9.0 (base 124.0) | 15 (base 15) | 9716 (base 9864) | 249.41 (base 248.07) | 3.46 (base 5.98) |
| renderer_off | 7.83 (base 109.4) | 14 (base 14) | 7272 (base 7180) | 241.67 (base 241.13) | 3.59 (base 4.1) |
| particles | 9.69 (base 10.67) | 15 (base 15) | 9204 (base 17128) | 263.86 (base 261.4) | 5.98 (base 5.98) |
| animation | 49 median (avg 39.93) (base 49) | 15 (base 15) | 2168 (base 2056) | 264.93 (base 260.68) | 5.98 (base 5.98) |
| animated_items | 60 median (avg 46.66) (base 60) | 24 (base 23) | 22100 (base 21940) | 239.86 (base 239.74) | 5.98 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.61 / 14 (9.68) | 123.58 / 124 (9.36) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (5.73) | 123.05 / 123 (5.5) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (15.99) | 124.0 / 124 (15.22) | median, at most vanilla |
| nether | 15.88 / 16 (16.37) | 120.78 / 121 (14.95) | median, x 1.10 + 2 |
| mobs | 19.97 / 20 (20.39) | 134.0 / 134 (18.78) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (29.97) | 94.59 / 133 (29.91) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.81) | 94.66 / 133 (29.84) | median and average, x 1.10 + 2 |
| translucent_move | 9.0 / 9 (16.67) | 124.0 / 124 (15.68) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (8.77) | 129.0 / 129 (8.39) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (8.23) | 129.0 / 129 (7.84) | median and average, at most vanilla |
| flat_plants | 9.0 / 9 (16.66) | 123.89 / 124 (15.58) | median, x 1.10 + 2 |
| renderer_off | 7.83 / 8 (24.04) | 108.53 / 123 (22.84) | median, x 1.10 + 2 |
| particles | 9.69 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.93 / 49 (30.0) | 144.09 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.66 / 60 (30.0) | 155.27 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.862 | 36.0 / 21.862 | 0.0 / 0.0 | 416.0 / 37.4 |
| flythrough | 112.0 / 83.186 | 112.0 / 83.186 | 0.0 / 0.0 | 416.0 / 143.512 |
| water_glass | 36.0 / 12.456 | 36.0 / 12.456 | 0.0 / 0.0 | 416.0 / 21.564 |
| nether | 20.0 / 15.182 | 20.0 / 15.182 | 0.0 / 0.0 | 256.0 / 26.624 |
| mobs | 16.0 / 9.65 | 16.0 / 9.65 | 0.0 / 0.0 | 256.0 / 16.833 |
| occlusion | 20.0 / 2.518 | 20.0 / 2.518 | 0.0 / 0.0 | 416.0 / 4.56 |
| occlusion_move | 20.0 / 3.426 | 20.0 / 3.426 | 0.0 / 0.0 | 416.0 / 6.136 |
| translucent_move | 28.0 / 13.098 | 28.0 / 13.098 | 0.0 / 0.0 | 416.0 / 22.852 |
| water_glass_oit | 32.0 / 12.43 | 32.0 / 12.43 | 0.0 / 0.0 | 416.0 / 21.564 |
| translucent_move_oit | 40.0 / 12.743 | 40.0 / 12.743 | 0.0 / 0.0 | 416.0 / 22.139 |
| flat_plants | 28.0 / 14.315 | 28.0 / 14.315 | 0.0 / 0.0 | 416.0 / 24.784 |
| renderer_off | 28.0 / 9.023 | 28.0 / 9.023 | 0.0 / 0.0 | 416.0 / 15.824 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.297 | 28.0 / 0.297 | 0.0 / 0.0 | 256.0 / 0.536 |
| animated_items | 28.0 / 0.146 | 28.0 / 0.146 | 0.0 / 0.0 | 128.0 / 0.26 |


Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 738.808 | 16 (vanilla 28) | 1318916 | 40.455 / 0.091 | 21.862 / 36.0 | 35.219 | 2246 / 0 | 3.524 (5.66) |
| flythrough | indirect, one multi-draw per arena and layer | 3.026 | 893.395 | 16 (vanilla 28) | 5035232 | 16.821 / 0.945 | 83.186 / 112.0 | 134.455 | 6103 / 0 | 7.222 (17.909) |
| water_glass | indirect, one multi-draw per arena and layer | 3.009 | 273.856 | 16 (vanilla 28) | 771136 | 32.942 / 0.081 | 12.456 / 36.0 | 20.592 | 2928 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 469.431 | 16 (vanilla 28) | 952932 | 27.659 / 0.176 | 15.182 / 20.0 | 25.446 | 3974 / 0 | 2.892 (4.769) |
| mobs | indirect, one multi-draw per arena and layer | 2.005 | 197.484 | 16 (vanilla 28) | 600924 | 25.123 / 0.154 | 9.65 / 16.0 | 16.046 | 4413 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 71.118 | 16 (vanilla 28) | 156552 | 23.891 / 0.146 | 2.518 / 20.0 | 4.18 | 4662 / 0 | 2.921 (3.143) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 76.427 | 16 (vanilla 28) | 212892 | 22.552 / 0.137 | 3.426 / 20.0 | 5.685 | 4959 / 0 | 4.407 (11.022) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.009 | 249.366 | 16 (vanilla 28) | 807352 | 19.478 / 0.124 | 13.098 / 28.0 | 21.559 | 5754 / 0 | 5.186 (17.634) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.029 | 280.594 | 16 (vanilla 28) | 769476 | 16.034 / 0.084 | 12.43 / 32.0 | 20.547 | 1656 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.03 | 264.457 | 16 (vanilla 28) | 784796 | 10.875 / 0.113 | 12.743 / 40.0 | 20.956 | 2972 / 0 | 5.19 (10.846) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.009 | 284.853 | 16 (vanilla 28) | 892964 | 16.573 / 0.048 | 14.315 / 28.0 | 23.845 | 1596 / 0 | 1.758 (1.453) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.006 | 205.427 | 16 (vanilla 28) | 566352 | 9.247 / 0.053 | 9.023 / 28.0 | 15.123 | 896 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.435 / 0.124 | 0.131 / 28.0 | 0.225 | 5793 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 13.022 | 16 (vanilla 28) | 19088 | 19.268 / 0.123 | 0.297 / 28.0 | 0.51 | 5893 / 0 | 2.587 (5.914) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 9304 | 19.21 / 0.123 | 0.146 / 28.0 | 0.248 | 5940 / 0 | 1.18 (1.728) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 151.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.028 / 0.044 / 0.049; 0) | 0 | 29294 / 0 / 0 |
| flythrough | 185.609 | 1.0 | 0.0 | 0 | 0 | 813 (0.034 / 0.053 / 6.035; 0) | 39 | 21345 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1280 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.02 / 0.071 / 0.079; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.133 / 0.194 / 1.648; 0) | 180 | 2338 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 528 | 0 (None / None / None; 0) | 0 | 704 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 495 | 0 (None / None / None; 0) | 0 | 1155 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1336 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1924 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 317.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.027 (0.136; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 434.504 | 1.0 (0.0, 0.0) | 1.209 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.013 (0.042; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.109 (0.226; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.067 (0.105; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 51.351 | 10.0 (9.0, 2.348) | 6.0 (6.0, 2.276) | 0.058 (0.276; 0) | 0 | 2765 / 1495436 / 0 / 0 |
| translucent_move | 134.991 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.612 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 152.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 12.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 12.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.009 (0.062; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.578 / 0.611 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 68.853 / 0.613 | 0.019 / 0.015 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 5.082 / 0.0 | 0.012 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.826 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 57.533 / 57.525 | 0.013 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.775 / 0.884 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 46.484 / 11.674 | 0.064 / 0.009 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion | 76.923 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.988 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.86 / 12.817 | 0.004 / 0.014 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 57.313 / 57.415 | 0.009 / 0.017 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 14.333 / 14.224 | 0.003 / 0.008 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 0.0 | 0.887 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.524 / 2.293 | 0.014 / 0.008 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| renderer_off | 100.656 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.007 / 0.057 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.265 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.005 / 0.012 | 56 (3) | 40.473 / 9.238 / 31.235 | 3 / 0 | 10.987 |
| animated_items | 136.553 / 27.992 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.473 / 7.96 / 32.513 | 0 / 0 | 9.0 |

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
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 4.1968 | 8.75 | 0.0143 | 3.0499 | 4.2222 |
| mam | 4.4329 | 9.3 | 0.0628 | 3.1359 | 4.4564 |
| map | 5.2687 | 11.35 | 4.4562 | 4.1788 | 4.615 |
| hologram | 0.0023 | 0.55 | 0.0266 | 0.0266 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.62 | 500 | 0 (15) | 0 (15) | 0 (14) |
| factory_flat | 95.56 | 40 | 0 (14) | 0 (14) | 0 (14) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 95.43 | 12506 | 28 (28) | 1197 (1197) | 72 (72) |
| mam | 83.85 | 155106 | 28 (28) | 818 (818) | 72 (73) |
| map | 92.59 | not gated (SIFTEC's map shows whichever tiles it has scanned when it opens, so vanilla runs differ from each other by up to 22,532 strict pixels there (35-58k pixels in all after two attempts to wait for a steady map); package 7 (GUI) must give it real strict coverage) | 14576 (15296) | 2988 (3132) | 5260 (5404) |
| hologram | 100.0 | 413 | 257 (4525) | 261 (4530) | None (None) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| factory_flat | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 4.2059 / 48 | 1.2822 / 8 | 4.0461 / 28 |
| mam | 4.452 / 48 | 0.91 / 8 | 4.3869 / 28 |
| map | 3.0916 / 5072 | 5.332 / 10568 | 5.2253 / 13016 |
| hologram | 0.0228 / 218 | 0.004 / 33 | skipped |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.26 (base 111.52) | 15 (base 14) | 6976 (base 6488) | 422.01 (base 421.74) | 3.99 (base 5.48) |
| factory_flat | 11.67 (base 107.77) | 15 (base 14) | 6844 (base 6264) | 423.26 (base 422.37) | 5.48 (base 3.99) |
| sift | 23.0 (base 155.82) | 18 (base 19) | 16632 (base 81788) | 439.98 (base 440.07) | 3.99 (base 5.87) |
| hub | 21.53 (base 116.59) | 16 (base 18) | 66172 (base 67484) | 452.59 (base 451.33) | 3.99 (base 4.24) |
| mam | 22.52 (base 158.03) | 19 (base 21) | 23344 (base 24656) | 465.33 (base 471.8) | 5.99 (base 8.12) |
| map | 30.06 (base 177.0) | 14 (base 14) | 46836 (base 47032) | 506.25 (base 504.13) | 5.99 (base 8.24) |
| hologram | 33 median (avg 32.91) (base 165) | 22 (base 24) | 72080 (base 70368) | 469.59 (base 453.57) | 5.99 (base 8.24) |
| create_ct | 6.67 (base 100.31) | 12 (base 12) | 1596 (base 1596) | 470.93 (base 470.81) | 5.99 (base 4.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.26 / 12 (29.17) | 106.07 / 152 (29.76) | median and average, x 1.10 + 2 |
| factory_flat | 11.67 / 12 (29.88) | 105.0 / 152 (30.01) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (15.03) | 153.0 / 153 (18.27) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (29.62) | 115.11 / 161 (29.87) | median and average, x 1.10 + 2 |
| mam | 22.52 / 22 (29.25) | 116.72 / 162 (29.67) | median, x 1.10 + 2 |
| map | 30.06 / 30 (9.22) | 170.0 / 170 (10.63) | median and average, x 1.10 + 2 |
| hologram | 32.91 / 33 (21.45) | 134.9 / 173 (27.17) | median, at most vanilla |
| create_ct | 6.67 / 7 (29.94) | 100.0 / 147 (29.99) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.39 | 16.0 / 7.39 | 0.0 / 0.0 | 416.0 / 12.894 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.179 | 32.0 / 20.179 | 0.0 / 0.0 | 416.0 / 33.857 |
| map | 72.0 / 36.232 | 72.0 / 36.232 | 0.0 / 0.0 | 416.0 / 60.606 |
| hologram | 72.0 / 0.238 | 72.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 72.0 / 0.171 | 72.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.127 | 16 (vanilla 28) | 91632 | 22.201 / 0.221 | 1.453 / 28.0 | 2.447 | 2330 / 0 | 3.533 (5.306) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 22.852 / 0.077 | 1.453 / 28.0 | 2.447 | 930 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.01 | 430.43 | 16 (vanilla 28) | 462656 | 21.087 / 0.256 | 7.39 / 16.0 | 12.354 | 3343 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 20.609 / 0.249 | 1.879 / 12.0 | 3.137 | 3430 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.149 | 16 (vanilla 28) | 1185868 | 21.617 / 0.283 | 20.179 / 32.0 | 31.666 | 4085 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.016 | 548.967 | 16 (vanilla 28) | 2260464 | 21.065 / 1.073 | 36.232 / 72.0 | 60.361 | 7992 / 0 | 5.871 (11.245) |
| hologram | indirect, one multi-draw per arena and layer | 2.005 | 13.03 | 16 (vanilla 28) | 13720 | 21.012 / 1.066 | 0.238 / 72.0 | 0.366 | 8048 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 20.853 / 1.058 | 0.171 / 72.0 | 0.283 | 8154 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1806 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 593 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 586 / 0 / 0 |
| map | 34.0 | 1.0 | 0.0 | 0 | 0 | 2 (0.014 / 0.025 / 0.025; 0) | 0 | 6290 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1720 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.04; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.037; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.882 / 0.0 | 0.521 / 0.577 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.395 / 11.54 | 0.018 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.568 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.199 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 67.656 / 0.0 | 0.013 / 0.003 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 102.169 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 151.102 / 90.288 | 0.173 / 0.05 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.18 (base 119.43) | 13 (base 13) | 49032 (base 48604) | 421.7 (base 421.54) | 3.73 (base 5.48) |
| factory_flat | 19.67 (base 112.31) | 13 (base 13) | 48916 (base 48340) | 422.08 (base 422.36) | 3.48 (base 5.48) |
| sift | 41.0 (base 670.27) | 16 (base 18) | 4148 (base 81788) | 439.47 (base 441.24) | 3.73 (base 4.24) |
| hub | 25.53 (base 162.94) | 14 (base 17) | 64564 (base 66484) | 451.91 (base 451.46) | 3.73 (base 5.99) |
| mam | 24.52 (base 222.38) | 17 (base 20) | 20724 (base 22916) | 465.28 (base 472.87) | 7.73 (base 5.99) |
| map | 67.0 (base 225.0) | 12 (base 12) | 31404 (base 31404) | 504.13 (base 506.16) | 8.24 (base 6.31) |
| hologram | 34 median (avg 33.7) (base 166) | 20 (base 23) | 71248 (base 70624) | 469.03 (base 452.39) | 8.31 (base 6.31) |
| create_ct | 6.67 (base 99.92) | 10 (base 10) | 1124 (base 1124) | 470.46 (base 470.06) | 8.24 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.18 / 20 (29.99) | 180.79 / 226 (29.59) | median and average, x 1.10 + 2 |
| factory_flat | 19.67 / 20 (29.99) | 179.0 / 226 (29.99) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (19.35) | 579.0 / 579 (18.28) | median, x 1.10 + 2 |
| hub | 25.53 / 25 (29.94) | 162.03 / 208 (29.96) | median and average, x 1.10 + 2 |
| mam | 24.52 / 24 (29.85) | 200.1 / 246 (29.89) | median, x 1.10 + 2 |
| map | 67.0 / 67 (11.42) | 716.0 / 716 (12.29) | median and average, x 1.10 + 2 |
| hologram | 33.7 / 34 (28.44) | 144.37 / 184 (27.76) | median, at most vanilla |
| create_ct | 6.67 / 7 (30.0) | 110.99 / 158 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.39 | 16.0 / 7.39 | 0.0 / 0.0 | 416.0 / 12.894 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.24 | 32.0 / 20.24 | 0.0 / 0.0 | 416.0 / 33.982 |
| map | 72.0 / 33.037 | 72.0 / 33.037 | 0.0 / 0.0 | 416.0 / 61.626 |
| hologram | 72.0 / 0.238 | 72.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| create_ct | 72.0 / 0.171 | 72.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 91632 | 24.342 / 0.27 | 1.453 / 28.0 | 2.447 | 2369 / 0 | 2.395 (2.291) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.123 | 16 (vanilla 28) | 91632 | 29.125 / 0.074 | 1.453 / 28.0 | 2.447 | 977 / 0 | None (None) |
| sift | plain, one multi-draw per region and layer | 21.054 | 430.109 | 16 (vanilla 28) | 462656 | 23.013 / 0.295 | 7.39 / 16.0 | 12.354 | 3377 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.083 | 16 (vanilla 28) | 117492 | 22.491 / 0.287 | 1.879 / 12.0 | 3.137 | 3469 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.146 | 16 (vanilla 28) | 1188756 | 23.42 / 0.314 | 20.24 / 32.0 | 31.743 | 4148 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 40.175 | 553.417 | 16 (vanilla 28) | 2063260 | 22.297 / 1.216 | 33.037 / 72.0 | 55.095 | 8520 / 0 | 3.467 (5.953) |
| hologram | plain, one multi-draw per region and layer | 3.005 | 13.023 | 16 (vanilla 28) | 13720 | 22.211 / 1.208 | 0.238 / 72.0 | 0.366 | 8591 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 22.057 / 1.199 | 0.171 / 72.0 | 0.283 | 8699 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2328 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 597 / 0 / 0 |
| map | 34.0 | 16.0 | 0.0 | 0 | 0 | 2 (0.006 / 0.006 / 0.006; 0) | 0 | 7786 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2276 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.003 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 284.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.595 / 0.0 | 0.519 / 0.518 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.708 / 10.893 | 0.015 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.568 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 50.681 / 0.0 | 0.012 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 100.649 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 147.3 / 88.524 | 0.131 / 0.036 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.21 (base 111.25) | 15 (base 15) | 51616 (base 51148) | 422.25 (base 420.23) | 3.71 (base 3.71) |
| factory_flat | 13.67 (base 107.27) | 15 (base 15) | 51500 (base 50904) | 421.27 (base 421.17) | 3.46 (base 3.46) |
| sift | 21.11 (base 150.31) | 18 (base 19) | 16632 (base 81788) | 440.14 (base 439.9) | 3.71 (base 4.23) |
| hub | 21.53 (base 136.98) | 16 (base 18) | 66172 (base 67484) | 452.68 (base 450.49) | 3.71 (base 4.23) |
| mam | 22.52 (base 171.53) | 19 (base 21) | 23344 (base 24656) | 466.13 (base 462.56) | 7.71 (base 5.46) |
| map | 30.0 (base 177.0) | 14 (base 14) | 46932 (base 47072) | 506.4 (base 504.09) | 8.23 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| create_ct | 6.67 (base 100.16) | 12 (base 12) | 1596 (base 1596) | 476.23 (base 475.26) | 8.23 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.21 / 14 (30.0) | 111.79 / 154 (28.37) | median and average, x 1.10 + 2 |
| factory_flat | 13.67 / 14 (30.0) | 107.87 / 154 (29.7) | median and average, x 1.10 + 2 |
| sift | 21.11 / 23 (23.89) | 153.0 / 153 (12.38) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (30.0) | 119.7 / 161 (28.43) | median and average, x 1.10 + 2 |
| mam | 22.52 / 22 (29.92) | 132.0 / 162 (25.34) | median, x 1.10 + 2 |
| map | 30.0 / 30 (15.23) | 170.0 / 170 (8.26) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 100.06 / 147 (29.97) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.39 | 16.0 / 7.39 | 0.0 / 0.0 | 416.0 / 12.921 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.165 | 32.0 / 20.165 | 0.0 / 0.0 | 416.0 / 33.388 |
| map | 72.0 / 34.765 | 72.0 / 34.765 | 0.0 / 0.0 | 416.0 / 56.658 |
| create_ct | 72.0 / 0.171 | 72.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 24.248 / 0.288 | 1.453 / 28.0 | 2.447 | 2561 / 0 | 1.49 (1.689) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 25.896 / 0.124 | 1.453 / 28.0 | 2.447 | 1078 / 0 | None (None) |
| sift | indirect, one multi-draw per arena and layer | 3.006 | 429.899 | 16 (vanilla 28) | 462656 | 22.946 / 0.31 | 7.39 / 16.0 | 12.354 | 3568 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.083 | 16 (vanilla 28) | 117492 | 22.466 / 0.302 | 1.879 / 12.0 | 3.137 | 3651 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1185024 | 23.158 / 0.328 | 20.165 / 32.0 | 31.644 | 4309 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.01 | 551.809 | 16 (vanilla 28) | 2172912 | 20.536 / 0.669 | 34.765 / 72.0 | 58.023 | 8941 / 0 | 2.2 (3.738) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 20.381 / 0.663 | 0.171 / 72.0 | 0.283 | 9053 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2868 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| map | 34.0 | 1.0 | 0.0 | 0 | 0 | 2 (0.006 / 0.006 / 0.006; 0) | 0 | 10370 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.003 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 283.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.013; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 97.531 / 0.0 | 0.561 / 0.539 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 9.858 / 9.695 | 0.014 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 94.199 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| sift | 140.0 / 8.281 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 98.099 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 109.349 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 5.344 / 0.0 | 0.007 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

