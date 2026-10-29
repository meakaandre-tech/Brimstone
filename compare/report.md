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
| spawn | 0.0528 | 10.05 | 0.0511 | 0.0588 | 0.0622 |
| flythrough | 0.0 | 14.95 | 0.0011 | 0.0011 | 0.0012 |
| water_glass | 0.0047 | 6.45 | 0.0042 | 0.0037 | 0.0034 |
| nether | 0.0 | 0.8 | 0.0003 | 0.0028 | 0.0003 |
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
| translucent_move.x2404 | 0.0 | 0.3 | 0.0002 | 0.0003 | 0.0004 |
| translucent_move.x2414 | 0.0002 | 0.3 | 0.0004 | 0.0003 | 0.0003 |
| translucent_move.x2424 | 0.0001 | 0.3 | 0.0002 | 0.0002 | 0.0001 |
| translucent_move.low | 0.0005 | 0.3 | 0.001 | 0.0011 | 0.0015 |
| translucent_move.back | 0.0 | 0.3 | 0.0001 | 0.0 | 0.0001 |
| translucent_move.bare_back | 0.0 | 0.3 | 0.0001 | 0.0001 | 0.0 |
| water_glass_oit | 0.0044 | 0.3 | 0.004 | 0.0039 | 0.0048 |
| translucent_move_oit | 0.0001 | 0.3 | 0.0001 | 0.0001 | 0.0 |
| translucent_move_oit.x2404 | 0.0001 | 0.3 | 0.0004 | 0.0004 | 0.0003 |
| translucent_move_oit.x2414 | 0.0003 | 0.3 | 0.0003 | 0.0001 | 0.0001 |
| translucent_move_oit.x2424 | 0.0 | 0.3 | 0.0001 | 0.0002 | 0.0002 |
| translucent_move_oit.low | 0.0003 | 0.3 | 0.0013 | 0.001 | 0.0009 |
| translucent_move_oit.back | 0.0003 | 0.3 | 0.0003 | 0.0002 | 0.0001 |
| translucent_move_oit.bare_back | 0.0 | 0.3 | 0.0001 | 0.0001 | 0.0002 |
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
| spawn | 92.78 | 18057 | 226 (713) | 204 (695) | 246 (759) |
| flythrough | 97.25 | 6020 | 48 (309) | 48 (309) | 51 (309) |
| water_glass | 95.81 | 25508 | 18 (2619) | 20 (2621) | 21 (2736) |
| nether | 99.42 | 905 | 3 (458) | 3 (458) | 3 (470) |
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
| translucent_move | 96.93 | 13 | 1 (76) | 1 (76) | 2 (68) |
| translucent_move.x2404 | 95.93 | 15 | 4 (104) | 4 (105) | 6 (90) |
| translucent_move.x2414 | 95.86 | 13 | 3 (106) | 2 (105) | 2 (100) |
| translucent_move.x2424 | 96.74 | 13 | 2 (79) | 1 (78) | 1 (67) |
| translucent_move.low | 96.11 | 41 | 16 (21) | 16 (21) | 16 (18) |
| translucent_move.back | 96.93 | 13 | 2 (77) | 3 (78) | 2 (68) |
| translucent_move.bare_back | 96.9 | 13 | 2 (77) | 3 (80) | 2 (68) |
| water_glass_oit | 95.81 | 57 | 14 (2616) | 17 (2619) | 22 (2736) |
| translucent_move_oit | 96.93 | 12 | 1 (76) | 2 (77) | 1 (67) |
| translucent_move_oit.x2404 | 95.93 | 16 | 5 (105) | 4 (104) | 4 (89) |
| translucent_move_oit.x2414 | 95.86 | 10 | 1 (104) | 2 (106) | 1 (98) |
| translucent_move_oit.x2424 | 96.74 | 15 | 1 (78) | 3 (80) | 2 (68) |
| translucent_move_oit.low | 96.11 | 40 | 18 (24) | 12 (17) | 13 (15) |
| translucent_move_oit.back | 96.93 | 12 | 2 (77) | 3 (78) | 4 (70) |
| translucent_move_oit.bare_back | 96.9 | 15 | 3 (79) | 2 (77) | 1 (67) |
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
| spawn | 0.4304 / 168 | 0.4416 / 248 | 0.4388 / 224 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| water_glass | 0.0031 / 11 | 0.0043 / 19 | 0.0036 / 18 |
| nether | 0.0173 / 159 | 0.0025 / 0 | 0.0 / 0 |
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
| translucent_move | 0.0001 / 0 | 0.0002 / 3 | 0.0 / 0 |
| translucent_move.x2404 | 0.0 / 2 | 0.0001 / 1 | 0.0002 / 1 |
| translucent_move.x2414 | 0.0002 / 3 | 0.0002 / 0 | 0.0003 / 1 |
| translucent_move.x2424 | 0.0001 / 2 | 0.0001 / 3 | 0.0 / 2 |
| translucent_move.low | 0.0002 / 1 | 0.0009 / 8 | 0.0007 / 5 |
| translucent_move.back | 0.0 / 0 | 0.0 / 0 | 0.0001 / 1 |
| translucent_move.bare_back | 0.0001 / 1 | 0.0 / 0 | 0.0 / 0 |
| water_glass_oit | 0.0042 / 18 | 0.0044 / 23 | 0.004 / 20 |
| translucent_move_oit | 0.0002 / 2 | 0.0 / 0 | 0.0 / 1 |
| translucent_move_oit.x2404 | 0.0 / 0 | 0.0001 / 0 | 0.0001 / 1 |
| translucent_move_oit.x2414 | 0.0004 / 4 | 0.0 / 1 | 0.0 / 1 |
| translucent_move_oit.x2424 | 0.0001 / 1 | 0.0 / 2 | 0.0002 / 1 |
| translucent_move_oit.low | 0.0005 / 5 | 0.0003 / 4 | 0.0008 / 7 |
| translucent_move_oit.back | 0.0003 / 0 | 0.0001 / 3 | 0.0 / 1 |
| translucent_move_oit.bare_back | 0.0001 / 3 | 0.0001 / 3 | 0.0002 / 0 |
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
| spawn | 13.0 (base 13.6) | 14 (base 14) | 24080 (base 22476) | 259.33 (base 259.07) | 3.73 (base 3.99) |
| flythrough | 13.06 (reported, not gated) | 30 (reported, not gated) | 260084 (reported, not gated) | 315.94 (base 324.25) | 3.48 (base 4.24) |
| water_glass | 8.88 (base 8.97) | 15 (base 15) | 15788 (base 15576) | 245.2 (base 250.02) | 3.73 (base 5.99) |
| nether | 14.41 (base 15.63) | 12 (base 12) | 20700 (base 19648) | 265.83 (base 273.53) | 3.73 (base 5.99) |
| mobs | 19.7 (base 19.77) | 17 (base 17) | 616288 (base 1010948) | 264.65 (base 268.37) | 3.73 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.13 (base 264.69) | 3.73 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100652 (base 126320) | 270.96 (base 265.1) | 3.73 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82544 (reported, not gated) | 270.22 (base 265.74) | 3.73 (base 4.24) |
| translucent_move | 8.85 (reported, not gated) | 15 (reported, not gated) | 10504 (reported, not gated) | 272.23 (base 267.48) | 3.73 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15208 (base 14688) | 244.26 (base 245.02) | 3.6 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10872 (reported, not gated) | 247.19 (base 247.19) | 3.73 (base 3.73) |
| flat_plants | 17.48 (base 18.29) | 15 (base 15) | 10128 (base 9812) | 247.21 (base 246.92) | 3.21 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7352 (base 7164) | 240.03 (base 241.33) | 3.6 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 267.64 (base 261.44) | 3.73 (base 5.99) |
| animation | 49 median (avg 39.94) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.02 (base 260.68) | 3.73 (base 5.99) |
| animated_items | 60 median (avg 46.73) (base 60) | 23 (base 23) | 21976 (base 21940) | 240.23 (base 239.81) | 3.73 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (12.46) | 123.0 / 123 (16.2) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (8.84) | 123.07 / 123 (11.53) | median and average, at most vanilla + 2 |
| water_glass | 8.88 / 9 (22.83) | 91.15 / 124 (27.87) | median, at most vanilla |
| nether | 14.41 / 16 (22.55) | 88.86 / 121 (27.62) | median, x 1.10 + 2 |
| mobs | 19.7 / 20 (28.56) | 95.33 / 134 (29.99) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.27 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.98) | 94.27 / 133 (30.0) | median and average, x 1.10 + 2 |
| translucent_move | 8.85 / 9 (23.48) | 88.92 / 124 (28.61) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (12.58) | 129.0 / 129 (15.72) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (11.4) | 129.0 / 129 (14.84) | median and average, at most vanilla |
| flat_plants | 17.48 / 19 (23.07) | 90.84 / 124 (27.97) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.88) | 84.33 / 123 (29.99) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 144.46 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.01) | 155.22 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 21.783 | 40.0 / 21.783 | 0.0 / 0.0 | 416.0 / 37.307 |
| flythrough | 112.0 / 86.31 | 112.0 / 86.31 | 0.0 / 0.0 | 416.0 / 152.263 |
| water_glass | 40.0 / 12.475 | 40.0 / 12.475 | 0.0 / 0.0 | 416.0 / 21.671 |
| nether | 20.0 / 15.185 | 20.0 / 15.185 | 0.0 / 0.0 | 256.0 / 26.671 |
| mobs | 16.0 / 9.599 | 16.0 / 9.599 | 0.0 / 0.0 | 256.0 / 16.82 |
| outline | 16.0 / 1.539 | 16.0 / 1.539 | 0.0 / 0.0 | 256.0 / 2.711 |
| occlusion | 20.0 / 2.553 | 20.0 / 2.553 | 0.0 / 0.0 | 416.0 / 4.45 |
| occlusion_move | 20.0 / 3.412 | 20.0 / 3.412 | 0.0 / 0.0 | 416.0 / 5.864 |
| translucent_move | 28.0 / 12.992 | 28.0 / 12.992 | 0.0 / 0.0 | 416.0 / 22.37 |
| water_glass_oit | 20.0 / 12.475 | 20.0 / 12.475 | 0.0 / 0.0 | 416.0 / 21.671 |
| translucent_move_oit | 28.0 / 12.636 | 28.0 / 12.636 | 0.0 / 0.0 | 416.0 / 21.657 |
| flat_plants | 24.0 / 14.493 | 24.0 / 14.493 | 0.0 / 0.0 | 416.0 / 24.789 |
| renderer_off | 24.0 / 8.907 | 24.0 / 8.907 | 0.0 / 0.0 | 416.0 / 15.652 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 489649.427 (659392.573) | 25.742 | 1.165 | 516832 / 1075664 | 492752 (2097152.0) | 248 / 323 | 0.0 / 0 |
| flythrough | 560148.795 (896055.841) | 37.487 | 1.084 | 2300192 / 7231760 | 2095232 (2097152.0) | 176 / 230 | 5.26 / 66 |
| water_glass | 216614.958 (388838.993) | 44.292 | 1.117 | 18344 / 18376 | 0 (2097152.0) | 7 / 0 | 0.0 / 0 |
| nether | 319223.911 (477225.472) | 33.108 | 1.273 | 186120 / 412952 | 165888 (2097152.0) | 11 / 0 | 0.0 / 0 |
| mobs | 166416.43 (300825.0) | 44.68 | 0.99 | 616288 / 616328 | 0 (2097152.0) | 1 / 0 | 0.0 / 0 |
| outline | 28291.073 (48530.75) | 41.705 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 45347.579 (79522.538) | 42.975 | 1.211 | 100652 / 440728 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 50218.753 (88221.29) | 43.076 | 1.236 | 685128 / 1108308 | 608320 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 201025.318 (356406.614) | 43.597 | 1.179 | 767344 / 1332248 | 755968 (2097152.0) | 9 / 1 | 0.0 / 0 |
| water_glass_oit | 217003.124 (389535.777) | 44.292 | 1.117 | 17000 / 16920 | 0 (2097152.0) | 251 / 314 | 0.0 / 0 |
| translucent_move_oit | 201416.114 (356916.781) | 43.568 | 1.177 | 742576 / 1290992 | 731840 (2097152.0) | 228 / 296 | 0.0 / 0 |
| flat_plants | 246116.72 (452489.414) | 45.608 | 1.056 | 272160 / 271728 | 0 (2097152.0) | 17 / 0 | 0.0 / 0 |
| renderer_off | 155255.191 (279424.485) | 44.438 | 1.02 | 7352 / 7272 | 0 (2097152.0) | 2 / 0 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86832 / 150176 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79704 / 123000 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.012 | 754.028 | 16 (vanilla 28) | 1313784 | 43.819 / 0.112 | 21.783 / 40.0 | 35.082 | 2605 / 0 | 3.691 (6.046) |
| flythrough | indirect, one multi-draw per arena and layer | 3.017 | 919.784 | 16 (vanilla 28) | 5227152 | 15.825 / 1.073 | 86.31 / 112.0 | 139.58 | 6461 / 0 | 5.337 (13.434) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 273.597 | 16 (vanilla 28) | 775980 | 35.993 / 0.098 | 12.475 / 40.0 | 20.721 | 3289 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.004 | 470.04 | 16 (vanilla 28) | 952360 | 30.385 / 0.185 | 15.185 / 20.0 | 25.431 | 4313 / 0 | 2.642 (3.02) |
| mobs | indirect, one multi-draw per arena and layer | 2.003 | 197.344 | 16 (vanilla 28) | 600600 | 27.678 / 0.163 | 9.599 / 16.0 | 16.038 | 4749 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 96900 | 27.151 / 0.162 | 1.539 / 16.0 | 2.588 | 4844 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 71.119 | 16 (vanilla 28) | 158780 | 25.994 / 0.155 | 2.553 / 20.0 | 4.24 | 5077 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 76.47 | 16 (vanilla 28) | 212684 | 24.779 / 0.147 | 3.412 / 20.0 | 5.679 | 5336 / 0 | 5.973 (18.191) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.006 | 248.339 | 16 (vanilla 28) | 800100 | 21.685 / 0.135 | 12.992 / 28.0 | 21.365 | 6082 / 0 | 5.963 (12.994) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.02 | 282.12 | 16 (vanilla 28) | 775980 | 7.518 / 0.043 | 12.475 / 20.0 | 20.721 | 1088 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.022 | 263.531 | 16 (vanilla 28) | 777592 | 6.84 / 0.112 | 12.636 / 28.0 | 20.764 | 2424 / 0 | 5.418 (14.694) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.007 | 287.623 | 16 (vanilla 28) | 903020 | 6.058 / 0.01 | 14.493 / 24.0 | 24.113 | 949 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.343 | 16 (vanilla 28) | 557916 | 2.357 / 0.021 | 8.907 / 24.0 | 14.898 | 599 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 21.64 / 0.135 | 0.131 / 28.0 | 0.225 | 6121 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 21.46 / 0.134 | 0.302 / 28.0 | 0.518 | 6223 / 0 | 1.738 (1.449) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 21.397 / 0.134 | 0.153 / 28.0 | 0.261 | 6273 / 0 | 0.899 (1.28) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 152.0 | 1.0 | 0.0 | 0 | 0 | 58 (0.023 / 0.038 / 0.071; 0) | 0 | 37848 / 0 / 0 |
| flythrough | 192.328 | 1.0 | 0.0 | 0 | 0 | 906 (0.022 / 0.039 / 8.032; 0) | 71 | 34042 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1832 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.013 / 0.033 / 0.034; 0) | 32 | 1202 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.08 / 0.121 / 0.137; 0) | 180 | 3290 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 756 | 0 (None / None / None; 0) | 0 | 1008 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 687 | 0 (None / None / None; 0) | 0 | 1603 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1848 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2396 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 325.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.105; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 439.576 | 1.0 (0.0, 0.0) | 1.164 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.049; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.129 (0.212; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.058 (0.125; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.364 | 10.0 (9.0, 2.356) | 6.0 (6.0, 2.275) | 0.053 (0.237; 0) | 0 | 2783 / 1506546 / 0 / 0 |
| translucent_move | 135.077 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.703 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.01; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.007 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 98.823 / 0.0 | 0.025 / 0.01 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.313 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.847 |
| water_glass | 82.437 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.813 / 55.843 | 0.021 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 83.333 / 8.825 | 0.806 / 0.701 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 36.633 / 5.909 | 0.063 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 80.219 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.833 / 10.836 | 0.005 / 0.013 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 50.446 / 50.365 | 0.014 / 0.021 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.943 / 12.891 | 0.007 / 0.013 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 82.143 / 8.655 | 0.986 / 0.959 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.277 / 2.119 | 0.016 / 0.01 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.005 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.007 / 0.045 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.277 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.003 / 0.009 | 56 (3) | 40.51 / 9.24 / 31.27 | 3 / 0 | 10.987 |
| animated_items | 136.553 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

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
| spawn | 49.45 (base 49.0) | 13 (base 13) | 1492 (base 6372) | 260.11 (base 256.29) | 3.73 (base 3.73) |
| flythrough | 66.87 (reported, not gated) | 46 (reported, not gated) | 584932 (reported, not gated) | 315.38 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 19.96 (base 20.0) | 13 (base 14) | 7316 (base 10264) | 245.18 (base 249.83) | 3.73 (base 5.99) |
| nether | 33.75 (base 33.79) | 10 (base 11) | 4176 (base 10656) | 265.77 (base 272.57) | 3.73 (base 5.99) |
| mobs | 28.86 (base 28.97) | 15 (base 16) | 610676 (base 1015892) | 264.4 (base 268.23) | 3.73 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 264.21 (base 264.41) | 3.73 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 269.89 (base 264.64) | 3.73 (base 4.24) |
| occlusion_move | 24.04 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 270.44 (base 265.66) | 3.73 (base 5.99) |
| translucent_move | 17.99 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 272.15 (base 268.19) | 3.73 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7540 (base 7604) | 244.45 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 246.86 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 32.86 (base 33.0) | 13 (base 13) | 1604 (base 1716) | 247.38 (base 248.11) | 3.34 (base 5.99) |
| renderer_off | 17.68 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 239.52 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 267.53 (base 261.4) | 3.73 (base 4.24) |
| animation | 49 median (avg 39.95) (base 49) | 13 (base 13) | 1716 (base 1716) | 265.03 (base 260.68) | 3.73 (base 4.24) |
| animated_items | 60 median (avg 46.73) (base 60) | 21 (base 22) | 21508 (base 21668) | 240.17 (base 239.53) | 3.73 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.45 / 49 (11.02) | 899.0 / 899 (15.75) | median and average, x 1.10 + 2 |
| flythrough | 66.87 / 64 (6.73) | 1053.57 / 1048 (11.23) | median and average, at most vanilla |
| water_glass | 19.96 / 20 (20.66) | 358.46 / 390 (27.41) | median, at most vanilla |
| nether | 33.75 / 34 (19.28) | 556.24 / 588 (27.42) | median, x 1.10 + 2 |
| mobs | 28.86 / 29 (23.1) | 290.72 / 329 (29.86) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 127.27 / 166 (30.0) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (30.0) | 161.27 / 200 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 24.04 / 24 (29.78) | 166.62 / 201 (29.99) | median and average, x 1.10 + 2 |
| translucent_move | 17.99 / 18 (18.64) | 334.8 / 366 (28.05) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (10.1) | 401.0 / 401 (13.35) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (9.33) | 384.83 / 386 (12.98) | median and average, at most vanilla |
| flat_plants | 32.86 / 33 (18.67) | 370.73 / 403 (27.71) | median, x 1.10 + 2 |
| renderer_off | 17.68 / 18 (29.35) | 287.11 / 325 (29.77) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.27 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 156.22 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 167.46 / 202 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 40.0 / 22.445 | 40.0 / 22.445 | 0.0 / 0.0 | 416.0 / 38.129 |
| flythrough | 112.0 / 84.641 | 112.0 / 84.641 | 0.0 / 0.0 | 416.0 / 152.363 |
| water_glass | 40.0 / 12.475 | 40.0 / 12.475 | 0.0 / 0.0 | 416.0 / 21.671 |
| nether | 20.0 / 15.194 | 20.0 / 15.194 | 0.0 / 0.0 | 256.0 / 26.676 |
| mobs | 16.0 / 9.599 | 16.0 / 9.599 | 0.0 / 0.0 | 256.0 / 16.82 |
| outline | 16.0 / 1.539 | 16.0 / 1.539 | 0.0 / 0.0 | 256.0 / 2.711 |
| occlusion | 20.0 / 2.428 | 20.0 / 2.428 | 0.0 / 0.0 | 416.0 / 4.267 |
| occlusion_move | 20.0 / 3.229 | 20.0 / 3.229 | 0.0 / 0.0 | 416.0 / 5.716 |
| translucent_move | 28.0 / 12.877 | 28.0 / 12.877 | 0.0 / 0.0 | 416.0 / 22.222 |
| water_glass_oit | 20.0 / 12.475 | 20.0 / 12.475 | 0.0 / 0.0 | 416.0 / 21.671 |
| translucent_move_oit | 28.0 / 12.521 | 28.0 / 12.521 | 0.0 / 0.0 | 416.0 / 21.509 |
| flat_plants | 24.0 / 14.36 | 24.0 / 14.36 | 0.0 / 0.0 | 416.0 / 24.944 |
| renderer_off | 24.0 / 8.907 | 24.0 / 8.907 | 0.0 / 0.0 | 416.0 / 15.652 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 442955.564 (680427.527) | 34.9 | 1.774 | 402788 / 1017008 | 401408 (2097152.0) | 220 / 315 | 0.0 / 0 |
| flythrough | 554733.91 (887715.552) | 37.51 | 1.084 | 2107916 / 7607872 | 2097088 (2097152.0) | 134 / 224 | 6.237 / 53 |
| water_glass | 216665.346 (388929.443) | 44.292 | 1.117 | 9220 / 11204 | 0 (2097152.0) | 42 / 5 | 0.0 / 0 |
| nether | 269007.948 (477433.912) | 43.655 | 1.704 | 361984 / 218292 | 357248 (2097152.0) | 382 / 2 | 0.0 / 0 |
| mobs | 166486.36 (300951.41) | 44.68 | 0.99 | 610676 / 612388 | 0 (2097152.0) | 8 / 2 | 0.0 / 0 |
| outline | 28291.152 (48530.885) | 41.705 | 1.0 | 5012 / 5460 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 43396.427 (75656.184) | 42.64 | 1.214 | 258760 / 218780 | 160704 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 47931.664 (83686.567) | 42.725 | 1.239 | 658620 / 1149004 | 583552 (2097152.0) | 2 / 0 | 0.0 / 0 |
| translucent_move | 198865.226 (352638.608) | 43.607 | 1.179 | 758132 / 1332132 | 755968 (2097152.0) | 320 / 2 | 0.0 / 0 |
| water_glass_oit | 217212.01 (389910.743) | 44.292 | 1.117 | 9220 / 11988 | 0 (2097152.0) | 202 / 266 | 0.0 / 0 |
| translucent_move_oit | 199066.065 (352697.065) | 43.559 | 1.177 | 737540 / 1285780 | 731840 (2097152.0) | 186 / 259 | 0.0 / 0 |
| flat_plants | 244070.595 (448420.987) | 45.571 | 1.056 | 1828 / 4484 | 0 (2097152.0) | 373 / 0 | 0.0 / 0 |
| renderer_off | 155254.488 (279422.017) | 44.437 | 1.02 | 1380 / 3172 | 0 (2097152.0) | 1 / 4 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 8772 / 8964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86324 / 149988 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79236 / 122900 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.177 | 792.586 | 16 (vanilla 28) | 1354916 | 42.4 / 0.104 | 22.445 / 40.0 | 36.18 | 2489 / 0 | 5.653 (8.571) |
| flythrough | plain, one multi-draw per region and layer | 57.187 | 904.291 | 16 (vanilla 28) | 5127832 | 15.471 / 1.036 | 84.641 / 112.0 | 136.928 | 6400 / 0 | 8.364 (20.177) |
| water_glass | plain, one multi-draw per region and layer | 14.034 | 273.661 | 16 (vanilla 28) | 775980 | 34.564 / 0.094 | 12.475 / 40.0 | 20.721 | 3137 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.052 | 470.218 | 16 (vanilla 28) | 952440 | 29.069 / 0.185 | 15.194 / 20.0 | 25.433 | 4167 / 0 | 6.772 (20.745) |
| mobs | plain, one multi-draw per region and layer | 11.024 | 197.427 | 16 (vanilla 28) | 600600 | 26.412 / 0.162 | 9.599 / 16.0 | 16.038 | 4607 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 96900 | 25.897 / 0.161 | 1.539 / 16.0 | 2.588 | 4702 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 70.117 | 16 (vanilla 28) | 151068 | 24.851 / 0.154 | 2.428 / 20.0 | 4.034 | 4927 / 0 | 4.684 (4.863) |
| occlusion_move | plain, one multi-draw per region and layer | 10.99 | 75.424 | 16 (vanilla 28) | 201508 | 23.637 / 0.146 | 3.229 / 20.0 | 5.381 | 5196 / 0 | 6.997 (12.64) |
| translucent_move | plain, one multi-draw per region and layer | 12.032 | 248.43 | 16 (vanilla 28) | 792312 | 20.599 / 0.133 | 12.877 / 28.0 | 21.157 | 5950 / 0 | 8.694 (19.955) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.079 | 282.391 | 16 (vanilla 28) | 775980 | 7.526 / 0.043 | 12.475 / 20.0 | 20.721 | 1086 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.618 | 263.371 | 16 (vanilla 28) | 769780 | 6.57 / 0.088 | 12.521 / 28.0 | 20.555 | 2391 / 0 | 8.104 (21.077) |
| flat_plants | plain, one multi-draw per region and layer | 17.046 | 286.767 | 16 (vanilla 28) | 894444 | 6.142 / 0.01 | 14.36 / 24.0 | 23.884 | 948 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.022 | 205.35 | 16 (vanilla 28) | 557892 | 2.365 / 0.02 | 8.907 / 24.0 | 14.897 | 607 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 20.55 / 0.133 | 0.131 / 28.0 | 0.225 | 5992 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.376 / 0.131 | 0.302 / 28.0 | 0.518 | 6094 / 0 | 2.822 (7.705) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.315 / 0.132 | 0.153 / 28.0 | 0.261 | 6143 / 0 | 1.374 (1.935) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 155.0 | 17.0 | 0.0 | 0 | 0 | 35 (0.058 / 0.062 / 1.046; 0) | 0 | 34255 / 0 / 0 |
| flythrough | 188.2 | 26.156 | 0.0 | 0 | 0 | 852 (0.023 / 0.05 / 6.044; 0) | 51 | 25407 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1656 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.009 / 0.012 / 0.052; 0) | 32 | 1194 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.106 / 0.164 / 0.23; 0) | 180 | 2611 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 609 | 0 (None / None / None; 0) | 0 | 812 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 561 | 0 (None / None / None; 0) | 0 | 1309 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1496 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2348 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 345.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.03 (0.165; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 437.356 | 1.0 (0.0, 0.0) | 1.259 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.058; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.139 (0.305; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.073 (0.38; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.347 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.28) | 0.067 (0.281; 0) | 0 | 2768 / 1497614 / 0 / 0 |
| translucent_move | 136.07 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.594 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.068; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.009 (0.038; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.45 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 85.012 / 0.452 | 0.026 / 0.011 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.06 / 0.0 | 0.003 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.822 |
| water_glass | 83.732 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.589 / 52.614 | 0.013 / 0.024 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 83.732 / 10.0 | 0.779 / 0.745 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 41.02 / 7.083 | 0.079 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 77.052 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 81.938 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.68 / 12.638 | 0.006 / 0.017 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.182 / 54.099 | 0.012 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.87 / 13.861 | 0.004 / 0.011 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 83.032 / 10.0 | 0.971 / 0.855 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.838 / 1.711 | 0.011 / 0.008 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 77.441 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.056 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.004 / 0.012 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.987 |
| animated_items | 136.793 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

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
| spawn | 13.0 (base 14.05) | 14 (base 15) | 25160 (base 27764) | 260.62 (base 257.2) | 3.71 (base 3.98) |
| flythrough | 13.05 (reported, not gated) | 43 (reported, not gated) | 1069720 (reported, not gated) | 315.47 (base 318.06) | 3.46 (base 4.23) |
| water_glass | 9.0 (base 9.0) | 15 (base 15) | 15544 (base 15108) | 245.74 (base 244.39) | 3.71 (base 4.23) |
| nether | 15.9 (base 15.71) | 12 (base 12) | 21140 (base 19912) | 266.43 (base 268.95) | 3.71 (base 4.23) |
| mobs | 19.87 (base 19.97) | 17 (base 17) | 616288 (base 1008176) | 265.16 (base 264.31) | 3.71 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.78 (base 264.58) | 3.71 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100652 (base 126356) | 270.56 (base 264.78) | 3.71 (base 3.98) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82544 (reported, not gated) | 270.73 (base 265.16) | 3.71 (base 4.23) |
| translucent_move | 9.0 (reported, not gated) | 15 (reported, not gated) | 10800 (reported, not gated) | 273.12 (base 268.35) | 3.71 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15768 (base 15024) | 244.49 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 11040 (reported, not gated) | 246.97 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 18.97 (base 18.96) | 15 (base 15) | 10128 (base 9864) | 247.21 (base 248.07) | 3.45 (base 5.98) |
| renderer_off | 7.73 (base 7.83) | 14 (base 14) | 7352 (base 7180) | 239.84 (base 241.13) | 3.59 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 267.99 (base 261.4) | 3.71 (base 5.98) |
| animation | 49 median (avg 39.95) (base 49) | 15 (base 15) | 2224 (base 2056) | 265.52 (base 260.68) | 3.71 (base 5.98) |
| animated_items | 60 median (avg 46.78) (base 60) | 24 (base 23) | 22136 (base 21940) | 240.41 (base 239.74) | 3.71 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (9.92) | 123.35 / 123 (11.72) | median and average, x 1.10 + 2 |
| flythrough | 13.05 / 13 (5.61) | 123.06 / 123 (7.21) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (18.09) | 124.0 / 124 (18.68) | median, at most vanilla |
| nether | 15.9 / 16 (17.43) | 120.96 / 121 (18.55) | median, x 1.10 + 2 |
| mobs | 19.87 / 20 (22.75) | 116.72 / 134 (23.53) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.27 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.33 / 133 (29.97) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.94) | 94.46 / 133 (29.96) | median and average, x 1.10 + 2 |
| translucent_move | 9.0 / 9 (18.09) | 122.81 / 124 (19.48) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (9.06) | 129.0 / 129 (10.37) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (8.44) | 129.0 / 129 (9.71) | median and average, at most vanilla |
| flat_plants | 18.97 / 19 (18.75) | 123.95 / 124 (18.87) | median, x 1.10 + 2 |
| renderer_off | 7.73 / 8 (27.47) | 91.06 / 123 (27.62) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.78 / 60 (30.01) | 155.46 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.452 | 36.0 / 22.452 | 0.0 / 0.0 | 416.0 / 38.075 |
| flythrough | 112.0 / 84.481 | 112.0 / 84.481 | 0.0 / 0.0 | 416.0 / 146.901 |
| water_glass | 36.0 / 12.475 | 36.0 / 12.475 | 0.0 / 0.0 | 416.0 / 21.671 |
| nether | 20.0 / 15.183 | 20.0 / 15.183 | 0.0 / 0.0 | 256.0 / 26.663 |
| mobs | 16.0 / 9.599 | 16.0 / 9.599 | 0.0 / 0.0 | 256.0 / 16.82 |
| outline | 16.0 / 1.539 | 16.0 / 1.539 | 0.0 / 0.0 | 256.0 / 2.711 |
| occlusion | 20.0 / 2.46 | 20.0 / 2.46 | 0.0 / 0.0 | 416.0 / 4.508 |
| occlusion_move | 20.0 / 3.302 | 20.0 / 3.302 | 0.0 / 0.0 | 416.0 / 5.83 |
| translucent_move | 28.0 / 12.895 | 28.0 / 12.895 | 0.0 / 0.0 | 416.0 / 22.434 |
| water_glass_oit | 20.0 / 12.475 | 20.0 / 12.475 | 0.0 / 0.0 | 416.0 / 21.671 |
| translucent_move_oit | 28.0 / 12.483 | 28.0 / 12.483 | 0.0 / 0.0 | 416.0 / 21.721 |
| flat_plants | 24.0 / 14.417 | 24.0 / 14.417 | 0.0 / 0.0 | 416.0 / 24.773 |
| renderer_off | 24.0 / 8.907 | 24.0 / 8.907 | 0.0 / 0.0 | 416.0 / 15.652 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 505560.202 (680915.384) | 25.753 | 1.157 | 654152 / 559240 | 628992 (2097152.0) | 198 / 234 | 0.0 / 0 |
| flythrough | 542620.536 (869124.875) | 37.567 | 1.083 | 2124220 / 7150440 | 2095808 (2097152.0) | 112 / 144 | 6.54 / 52 |
| water_glass | 216740.731 (389064.765) | 44.292 | 1.117 | 17672 / 16808 | 0 (2097152.0) | 361 / 373 | 0.0 / 0 |
| nether | 319412.598 (477517.621) | 33.11 | 1.273 | 117668 / 238224 | 96192 (2097152.0) | 348 / 370 | 0.0 / 0 |
| mobs | 166491.916 (300961.454) | 44.68 | 0.99 | 616288 / 616328 | 0 (2097152.0) | 21 / 6 | 0.0 / 0 |
| outline | 28291.152 (48530.885) | 41.705 | 1.0 | 6300 / 6300 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 43973.289 (76783.973) | 42.731 | 1.211 | 248656 / 126324 | 148096 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 48684.689 (85188.334) | 42.851 | 1.236 | 674340 / 1098412 | 597440 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 199215.706 (353323.047) | 43.617 | 1.179 | 766448 / 1332244 | 755968 (2097152.0) | 361 / 312 | 0.0 / 0 |
| water_glass_oit | 217336.155 (390133.591) | 44.292 | 1.117 | 19240 / 16920 | 0 (2097152.0) | 181 / 207 | 0.0 / 0 |
| translucent_move_oit | 199367.667 (353274.548) | 43.566 | 1.177 | 743360 / 1289888 | 731840 (2097152.0) | 168 / 194 | 0.0 / 0 |
| flat_plants | 244831.147 (449882.496) | 45.579 | 1.056 | 10576 / 10012 | 0 (2097152.0) | 375 / 377 | 0.0 / 0 |
| renderer_off | 155269.796 (279449.167) | 44.437 | 1.02 | 7352 / 7272 | 0 (2097152.0) | 0 / 5 | 0.0 / 0 |
| particles | 1927.207 (4215.013) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86740 / 150176 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79864 / 123000 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.015 | 792.985 | 16 (vanilla 28) | 1355228 | 40.925 / 0.108 | 22.452 / 36.0 | 36.188 | 2379 / 0 | 3.557 (4.867) |
| flythrough | indirect, one multi-draw per arena and layer | 3.027 | 883.83 | 16 (vanilla 28) | 5114980 | 15.385 / 0.877 | 84.481 / 112.0 | 136.585 | 6264 / 0 | 8.45 (21.884) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 273.756 | 16 (vanilla 28) | 775980 | 33.101 / 0.093 | 12.475 / 36.0 | 20.721 | 3020 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 470.348 | 16 (vanilla 28) | 952344 | 27.837 / 0.184 | 15.183 / 20.0 | 25.43 | 4024 / 0 | 2.479 (3.336) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.434 | 16 (vanilla 28) | 600600 | 25.243 / 0.168 | 9.599 / 16.0 | 16.038 | 4470 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 96900 | 24.738 / 0.167 | 1.539 / 16.0 | 2.588 | 4564 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 71.119 | 16 (vanilla 28) | 153312 | 23.602 / 0.159 | 2.46 / 20.0 | 4.094 | 4807 / 0 | 4.018 (3.502) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 76.455 | 16 (vanilla 28) | 205668 | 22.365 / 0.15 | 3.302 / 20.0 | 5.492 | 5089 / 0 | 5.539 (10.701) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.008 | 248.535 | 16 (vanilla 28) | 793372 | 19.216 / 0.134 | 12.895 / 28.0 | 21.185 | 5920 / 0 | 4.498 (11.24) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.028 | 282.552 | 16 (vanilla 28) | 775980 | 7.53 / 0.043 | 12.475 / 20.0 | 20.721 | 1086 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.03 | 263.405 | 16 (vanilla 28) | 767508 | 6.764 / 0.095 | 12.483 / 28.0 | 20.495 | 2400 / 0 | 5.533 (10.743) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.008 | 287.765 | 16 (vanilla 28) | 897372 | 5.789 / 0.01 | 14.417 / 24.0 | 23.962 | 928 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.373 | 16 (vanilla 28) | 557884 | 2.341 / 0.02 | 8.907 / 24.0 | 14.897 | 605 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.162 / 0.134 | 0.131 / 28.0 | 0.225 | 5966 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 18.998 / 0.133 | 0.302 / 28.0 | 0.518 | 6068 / 0 | 1.387 (1.836) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.941 / 0.133 | 0.153 / 28.0 | 0.261 | 6118 / 0 | 1.24 (1.898) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 155.0 | 1.0 | 0.0 | 0 | 0 | 35 (0.024 / 0.034 / 0.041; 0) | 0 | 30845 / 0 / 0 |
| flythrough | 183.009 | 1.0 | 0.0 | 0 | 0 | 787 (0.023 / 0.048 / 3.11; 0) | 32 | 20680 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1448 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.012 / 0.017; 0) | 32 | 1198 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.144 / 0.149 / 8.707; 0) | 180 | 2534 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 546 | 0 (None / None / None; 0) | 0 | 728 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 507 | 0 (None / None / None; 0) | 0 | 1183 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1504 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2204 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 345.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.032 (0.083; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 432.673 | 1.0 (0.0, 0.0) | 1.646 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.044; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.124 (0.244; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.07 (0.165; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.356 | 10.0 (9.0, 2.367) | 6.0 (6.0, 2.28) | 0.055 (0.286; 0) | 0 | 2784 / 1507334 / 0 / 0 |
| translucent_move | 135.099 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.58 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.355 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 67.037 / 0.0 | 0.025 / 0.006 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.294 / 0.0 | 0.005 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.814 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 51.773 / 51.796 | 0.018 / 0.025 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.957 / 0.902 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 45.796 / 8.378 | 0.069 / 0.012 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 97.872 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 113.817 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.075 / 11.03 | 0.005 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.269 / 55.341 | 0.013 / 0.024 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.748 / 13.669 | 0.006 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.95 / 0.968 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.83 / 2.582 | 0.015 / 0.011 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 83.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.053 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.012 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.988 |
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
| factory | 0.0017 | 0.3 | 0.0023 | 0.0169 | 0.0017 |
| factory_flat | 0.0061 | 0.3 | 0.0063 | 0.0051 | 0.0033 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| hologram | 0.002 | 0.55 | 0.0258 | 0.0054 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | 0.0 | 0.25 | 0.0005 | 0.0005 | 0.0005 |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.59 | 500 | 4 (18) | 223 (352) | 2 (16) |
| factory_flat | 95.53 | 40 | 13 (27) | 0 (14) | 6 (20) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 87.02 | 10 | 0 (0) | 0 (0) | 0 (33) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| map | 99.99 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 100.0 | 413 | 248 (4517) | 45 (4296) | None (None) |
| hud | 44.33 | 10 | 5 (5) | 5 (5) | 5 (5) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Quicksilver's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.002 / 2 | 0.0169 / 223 | 0.0018 / 3 |
| factory_flat | 0.0053 / 2 | 0.0001 / 1 | 0.0035 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| map | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0231 / 219 | 0.0016 / 6 | skipped |
| hud | 0.0005 / 5 | 0.0005 / 5 | 0.0005 / 5 |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.23 (base 12.23) | 15 (base 14) | 6956 (base 6488) | 422.77 (base 421.74) | 3.99 (base 5.48) |
| factory_flat | 20.31 (base 20.25) | 15 (base 14) | 10052 (base 6264) | 422.51 (base 422.37) | 3.54 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19172 (base 81788) | 440.87 (base 440.07) | 3.99 (base 5.87) |
| hub | 21.53 (base 21.58) | 16 (base 18) | 66576 (base 67484) | 453.25 (base 451.33) | 3.99 (base 4.24) |
| mam | 22.54 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 466.42 (base 471.8) | 5.99 (base 8.12) |
| ponder | 87.24 (base 87.25) | 14 (base 14) | 52268 (base 52248) | 464.9 (base 463.81) | 5.99 (base 8.24) |
| map | 30.0 (base 30.0) | 14 (base 14) | 48912 (base 47032) | 493.07 (base 504.13) | 5.81 (base 8.24) |
| hologram | 33 median (avg 32.85) (base 33) | 22 (base 24) | 71856 (base 70368) | 471.57 (base 453.57) | 5.93 (base 8.24) |
| hud | 31.0 (base 31.0) | 16 (base 16) | 74580 (base 71636) | 491.97 (base 494.92) | 5.99 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 480.92 (base 470.81) | 5.99 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.23 / 12 (29.13) | 106.26 / 152 (29.53) | median and average, x 1.10 + 2 |
| factory_flat | 20.31 / 20 (29.68) | 114.89 / 160 (29.61) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (15.87) | 153.0 / 153 (15.18) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (29.55) | 116.37 / 161 (29.42) | median and average, x 1.10 + 2 |
| mam | 22.54 / 22 (29.44) | 117.63 / 162 (29.41) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (29.52) | 181.44 / 227 (29.66) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (10.97) | 170.0 / 170 (9.78) | median and average, x 1.10 + 2 |
| hologram | 32.85 / 33 (23.26) | 162.49 / 173 (20.81) | median, at most vanilla |
| hud | 31.0 / 31 (17.4) | 171.0 / 171 (13.97) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 100.08 / 147 (29.94) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.388 | 16.0 / 7.388 | 0.0 / 0.0 | 416.0 / 12.917 |
| hub | 12.0 / 1.886 | 12.0 / 1.886 | 0.0 / 0.0 | 416.0 / 3.307 |
| mam | 32.0 / 20.241 | 32.0 / 20.241 | 0.0 / 0.0 | 416.0 / 33.485 |
| ponder | 32.0 / 17.56 | 32.0 / 17.56 | 0.0 / 0.0 | 416.0 / 28.743 |
| map | 40.0 / 27.944 | 40.0 / 27.944 | 0.0 / 0.0 | 416.0 / 47.596 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.37 | 40.0 / 17.37 | 0.0 / 0.0 | 416.0 / 30.533 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26337.921 (48130.058) | 45.278 | 1.0 | 114684 / 190188 | 100928 (2097152.0) | 8 / 7 | 0.0 / 0 |
| factory_flat | 26336.845 (48128.051) | 45.278 | 1.0 | 117668 / 193172 | 100928 (2097152.0) | 1 / 1 | 0.0 / 0 |
| sift | 147678.397 (204978.587) | 27.954 | 1.299 | 19172 / 16612 | 0 (2097152.0) | 317 / 303 | 0.0 / 0 |
| hub | 33694.917 (59330.22) | 43.208 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 10 / 7 | 0.0 / 0 |
| mam | 65623.037 (114836.308) | 42.855 | 1.011 | 304760 / 304832 | 0 (2097152.0) | 9 / 3 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61244 / 61224 | 0 (2097152.0) | 6 / 5 | 0.0 / 0 |
| map | 399363.288 (627227.032) | 36.329 | 1.23 | 48912 / 46316 | 0 (2097152.0) | 219 / 195 | 0.0 / 0 |
| hologram | 3912.396 (6874.753) | 43.09 | 1.0 | 74656 / 73088 | 0 (2097152.0) | 53 / 75 | 0.0 / 0 |
| hud | 291297.061 (454971.389) | 35.975 | 1.317 | 74580 / 72020 | 0 (2097152.0) | 347 / 279 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 0 / 1 | 0.0 / 0 |

Terrain module, siftec full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.127 | 16 (vanilla 28) | 96100 | 26.731 / 0.24 | 1.511 / 28.0 | 2.566 | 1726 / 0 | 4.478 (6.701) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.125 | 16 (vanilla 28) | 96100 | 3.043 / 0.001 | 1.511 / 12.0 | 2.566 | 298 / 0 | 2.529 (2.241) |
| sift | indirect, one multi-draw per arena and layer | 3.009 | 429.35 | 16 (vanilla 28) | 462496 | 24.32 / 0.282 | 7.388 / 16.0 | 12.35 | 2734 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.085 | 16 (vanilla 28) | 118460 | 23.521 / 0.272 | 1.886 / 12.0 | 3.163 | 2826 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.147 | 16 (vanilla 28) | 1188072 | 24.161 / 0.31 | 20.241 / 32.0 | 31.725 | 3476 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1019516 | 24.161 / 0.31 | 17.56 / 32.0 | 27.224 | 3477 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.014 | 549.498 | 16 (vanilla 28) | 1751708 | 20.81 / 0.245 | 27.944 / 40.0 | 46.776 | 4318 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.028 | 16 (vanilla 28) | 13744 | 20.712 / 0.243 | 0.238 / 40.0 | 0.367 | 4380 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.009 | 405.164 | 16 (vanilla 28) | 1089916 | 16.936 / 0.21 | 17.37 / 40.0 | 29.104 | 6168 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.783 / 0.208 | 0.171 / 40.0 | 0.283 | 6272 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1908 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 592 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 591 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 32.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7040 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1864 / 0 / 0 |
| hud | 35.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12180 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.004 (0.049; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.004 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 283.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.044 / 0.0 | 0.54 / 0.551 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.08 / 10.187 | 0.018 / 0.006 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 94.595 / 0.0 | 0.615 / 0.641 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.574 / 11.65 | 0.018 / 0.006 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.839 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 95.076 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 94.199 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 33.157 / 0.0 | 0.009 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 129.567 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 145.509 / 88.631 | 0.157 / 0.059 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.86 / 13.85 | 0.0 / 0.0 | 574.06 / 574.05 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.86 / 13.85 (8.0 / 8.0) |
| mam | 13.87 / 13.88 | 1.0 / 1.0 | 113.05 / 113.51 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.87 / 13.88 (8.0 / 8.0) |
| ponder | 82.56 / 82.56 | 0.12 / 0.11 | 203.57 / 203.47 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.59 / 81.59 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.19 (base 20.23) | 13 (base 13) | 49024 (base 48604) | 423.68 (base 421.54) | 4.24 (base 5.48) |
| factory_flat | 28.29 (base 28.32) | 13 (base 13) | 52132 (base 48340) | 422.86 (base 422.36) | 3.54 (base 5.48) |
| sift | 41.0 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 440.41 (base 441.24) | 4.24 (base 4.24) |
| hub | 25.54 (base 25.54) | 14 (base 17) | 64948 (base 66484) | 453.28 (base 451.46) | 4.24 (base 5.99) |
| mam | 24.52 (base 24.53) | 17 (base 20) | 21108 (base 22916) | 466.62 (base 472.87) | 8.24 (base 5.99) |
| ponder | 87.23 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 465.12 (base 463.76) | 8.24 (base 8.24) |
| map | 66.0 (base 67.0) | 12 (base 12) | 30924 (base 31404) | 493.33 (base 506.16) | 8.12 (base 6.31) |
| hologram | 34 median (avg 33.72) (base 34) | 20 (base 23) | 71004 (base 70624) | 471.14 (base 452.39) | 8.24 (base 6.31) |
| hud | 57.0 (base 58.0) | 14 (base 14) | 60356 (base 59972) | 491.46 (base 493.56) | 8.18 (base 8.18) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 481.05 (base 470.06) | 8.24 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.19 / 20 (29.78) | 181.34 / 226 (29.3) | median and average, x 1.10 + 2 |
| factory_flat | 28.29 / 28 (30.0) | 187.88 / 234 (29.88) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (17.65) | 578.0 / 578 (14.47) | median, x 1.10 + 2 |
| hub | 25.54 / 25 (29.71) | 162.88 / 208 (29.63) | median and average, x 1.10 + 2 |
| mam | 24.52 / 24 (29.91) | 202.54 / 246 (29.15) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (29.83) | 180.72 / 227 (29.94) | median and average, x 1.10 + 2 |
| map | 66.0 / 66 (12.07) | 711.0 / 711 (10.12) | median and average, x 1.10 + 2 |
| hologram | 33.72 / 34 (27.79) | 169.29 / 184 (22.06) | median, at most vanilla |
| hud | 57.0 / 57 (19.59) | 571.0 / 571 (15.46) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 111.0 / 158 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.553 | 12.0 / 1.553 | 0.0 / 0.0 | 416.0 / 2.933 |
| sift | 16.0 / 7.388 | 16.0 / 7.388 | 0.0 / 0.0 | 416.0 / 12.89 |
| hub | 12.0 / 1.886 | 12.0 / 1.886 | 0.0 / 0.0 | 416.0 / 3.307 |
| mam | 32.0 / 20.135 | 32.0 / 20.135 | 0.0 / 0.0 | 416.0 / 33.54 |
| ponder | 32.0 / 17.454 | 32.0 / 17.454 | 0.0 / 0.0 | 416.0 / 28.879 |
| map | 40.0 / 27.944 | 40.0 / 27.944 | 0.0 / 0.0 | 416.0 / 47.208 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.37 | 40.0 / 17.37 | 0.0 / 0.0 | 416.0 / 30.531 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26336.909 (48128.205) | 45.278 | 1.0 | 156752 / 233268 | 100928 (2097152.0) | 5 / 9 | 0.0 / 0 |
| factory_flat | 26336.597 (48127.63) | 45.278 | 1.0 | 159736 / 236352 | 100928 (2097145.928) | 0 / 2 | 0.0 / 0 |
| sift | 145817.915 (204912.85) | 28.839 | 1.435 | 4148 / 8052 | 0 (2097152.0) | 345 / 289 | 0.0 / 0 |
| hub | 33694.63 (59329.714) | 43.208 | 1.02 | 83908 / 84516 | 0 (2097152.0) | 6 / 5 | 0.0 / 0 |
| mam | 65621.552 (114833.709) | 42.855 | 1.011 | 40068 / 40948 | 0 (2097152.0) | 2 / 5 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61188 / 61228 | 0 (2097152.0) | 4 / 3 | 0.0 / 0 |
| map | 365333.643 (626966.772) | 41.73 | 1.803 | 30924 / 35436 | 0 (2097152.0) | 241 / 202 | 0.0 / 0 |
| hologram | 3911.022 (6872.338) | 43.09 | 1.0 | 73020 / 73488 | 0 (2097152.0) | 11 / 29 | 0.0 / 0 |
| hud | 262327.202 (454824.266) | 42.323 | 1.839 | 60356 / 63924 | 0 (2097152.0) | 189 / 309 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1124 / 1316 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 96100 | 26.147 / 0.34 | 1.511 / 28.0 | 2.566 | 1957 / 0 | 4.345 (6.042) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.123 | 16 (vanilla 28) | 98664 | 3.007 / 0.001 | 1.553 / 12.0 | 2.635 | 310 / 0 | 3.346 (4.863) |
| sift | plain, one multi-draw per region and layer | 21.059 | 429.212 | 16 (vanilla 28) | 462496 | 24.009 / 0.36 | 7.388 / 16.0 | 12.35 | 2964 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.084 | 16 (vanilla 28) | 118460 | 23.363 / 0.349 | 1.886 / 12.0 | 3.163 | 3045 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.145 | 16 (vanilla 28) | 1182412 | 23.973 / 0.373 | 20.135 / 32.0 | 31.574 | 3691 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1013856 | 23.973 / 0.373 | 17.454 / 32.0 | 27.073 | 3692 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 39.162 | 549.27 | 16 (vanilla 28) | 1751708 | 20.789 / 0.297 | 27.944 / 40.0 | 46.776 | 4533 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.005 | 13.023 | 16 (vanilla 28) | 13744 | 20.695 / 0.294 | 0.238 / 40.0 | 0.367 | 4595 / 0 | None (None) |
| hud | plain, one multi-draw per region and layer | 29.074 | 405.033 | 16 (vanilla 28) | 1089916 | 17.027 / 0.247 | 17.37 / 40.0 | 29.104 | 6396 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.871 / 0.244 | 0.171 / 40.0 | 0.283 | 6503 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2124 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 595 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 32.0 | 15.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7744 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2228 / 0 / 0 |
| hud | 35.0 | 8.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 13720 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.01; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 283.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 95.085 / 0.0 | 0.578 / 0.522 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.219 / 10.079 | 0.015 / 0.006 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.568 / 0.0 | 0.648 / 0.623 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.052 / 12.18 | 0.018 / 0.008 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.005 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.358 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 95.979 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 16.698 / 0.0 | 0.006 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 125.397 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 139.047 / 85.386 | 0.124 / 0.042 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.86 / 13.85 | 0.0 / 0.0 | 574.24 / 573.56 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.86 / 13.85 (8.0 / 8.0) |
| mam | 13.85 / 13.87 | 1.0 / 1.0 | 112.56 / 113.32 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.87 (8.0 / 8.0) |
| ponder | 82.57 / 82.56 | 0.11 / 0.11 | 203.56 / 203.4 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.59 / 81.59 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.2 (base 14.22) | 15 (base 15) | 51608 (base 51148) | 421.42 (base 420.23) | 4.23 (base 3.71) |
| factory_flat | 22.32 (base 22.31) | 15 (base 15) | 54808 (base 50904) | 422.26 (base 421.17) | 3.46 (base 3.46) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 19172 (base 81788) | 439.1 (base 439.9) | 4.23 (base 4.23) |
| hub | 21.55 (base 21.55) | 16 (base 18) | 66576 (base 67484) | 451.81 (base 450.49) | 4.23 (base 4.23) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23748 (base 24656) | 465.43 (base 462.56) | 8.23 (base 5.46) |
| ponder | 87.26 (base 87.24) | 14 (base 14) | 52268 (base 52248) | 463.9 (base 462.87) | 8.23 (base 8.23) |
| map | 30.0 (base 30.0) | 14 (base 14) | 48912 (base 47072) | 491.36 (base 504.09) | 8.04 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| hud | 31.0 (base 31.0) | 16 (base 16) | 75568 (base 72908) | 477.44 (base 490.43) | 8.16 (base 8.16) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 471.48 (base 475.26) | 8.23 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.2 / 14 (29.66) | 108.54 / 154 (29.35) | median and average, x 1.10 + 2 |
| factory_flat | 22.32 / 22 (29.98) | 117.25 / 162 (29.44) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (14.5) | 153.0 / 153 (13.21) | median, x 1.10 + 2 |
| hub | 21.55 / 21 (29.68) | 116.14 / 161 (29.5) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.5) | 120.28 / 162 (28.6) | median, x 1.10 + 2 |
| ponder | 87.26 / 87 (29.58) | 181.06 / 227 (29.83) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (10.76) | 170.0 / 170 (9.65) | median and average, x 1.10 + 2 |
| hud | 31.0 / 31 (15.68) | 171.0 / 171 (13.25) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 100.0 / 147 (29.98) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.511 | 28.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| factory_flat | 12.0 / 1.511 | 12.0 / 1.511 | 0.0 / 0.0 | 256.0 / 2.723 |
| sift | 16.0 / 7.388 | 16.0 / 7.388 | 0.0 / 0.0 | 416.0 / 12.917 |
| hub | 12.0 / 1.886 | 12.0 / 1.886 | 0.0 / 0.0 | 416.0 / 3.307 |
| mam | 32.0 / 20.209 | 32.0 / 20.209 | 0.0 / 0.0 | 416.0 / 33.07 |
| ponder | 32.0 / 17.59 | 32.0 / 17.59 | 0.0 / 0.0 | 416.0 / 28.615 |
| map | 40.0 / 27.944 | 40.0 / 27.944 | 0.0 / 0.0 | 416.0 / 47.373 |
| hud | 56.0 / 16.143 | 56.0 / 16.143 | 0.0 / 0.0 | 416.0 / 27.51 |
| create_ct | 56.0 / 0.687 | 56.0 / 0.687 | 0.0 / 0.0 | 416.0 / 0.294 |


Package 4b, siftec vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| factory | 26337.206 (48128.747) | 45.278 | 1.0 | 159336 / 234840 | 100928 (2097152.0) | 4 / 9 | 0.0 / 0 |
| factory_flat | 26336.423 (48127.283) | 45.278 | 1.0 | 162320 / 237824 | 100928 (2097152.0) | 0 / 3 | 0.0 / 0 |
| sift | 147721.634 (205038.6) | 27.954 | 1.299 | 19172 / 16612 | 0 (2097152.0) | 290 / 264 | 0.0 / 0 |
| hub | 33694.725 (59329.882) | 43.208 | 1.02 | 85536 / 85516 | 0 (2097152.0) | 3 / 3 | 0.0 / 0 |
| mam | 65706.992 (115004.264) | 42.866 | 1.011 | 42708 / 42688 | 0 (2097152.0) | 22 / 18 | 0.0 / 0 |
| ponder | 10.017 (12.02) | 16.667 | 2.0 | 61268 / 61248 | 0 (2097152.0) | 8 / 1 | 0.0 / 0 |
| map | 399397.06 (627280.074) | 36.329 | 1.23 | 48912 / 46316 | 0 (2097152.0) | 215 / 192 | 0.0 / 0 |
| hud | 318139.182 (492183.463) | 35.362 | 1.286 | 75568 / 73104 | 0 (2097152.0) | 313 / 265 | 0.0 / 0 |
| create_ct | 2538.23 (5312.855) | 52.225 | 1.0 | 1596 / 1596 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.125 | 16 (vanilla 28) | 96100 | 24.533 / 0.282 | 1.511 / 28.0 | 2.566 | 1798 / 0 | 3.649 (4.243) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.123 | 16 (vanilla 28) | 96100 | 3.006 / 0.001 | 1.511 / 12.0 | 2.566 | 290 / 0 | 2.008 (2.158) |
| sift | indirect, one multi-draw per arena and layer | 3.01 | 429.476 | 16 (vanilla 28) | 462496 | 22.657 / 0.313 | 7.388 / 16.0 | 12.35 | 2806 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 118460 | 21.944 / 0.302 | 1.886 / 12.0 | 3.163 | 2892 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.147 | 16 (vanilla 28) | 1185856 | 22.852 / 0.335 | 20.209 / 32.0 | 31.666 | 3542 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1021324 | 22.852 / 0.335 | 17.59 / 32.0 | 27.272 | 3543 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.014 | 549.544 | 16 (vanilla 28) | 1751708 | 19.882 / 0.266 | 27.944 / 40.0 | 46.776 | 4384 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.01 | 442.409 | 16 (vanilla 28) | 1013300 | 16.179 / 0.226 | 16.143 / 56.0 | 27.058 | 5991 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 42676 | 16.037 / 0.224 | 0.687 / 56.0 | 1.14 | 6091 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1746 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 594 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 592 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 32.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 6912 / 0 / 0 |
| hud | 39.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12246 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.037; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.038; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 283.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 243.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 94.369 / 0.0 | 0.493 / 0.525 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.733 / 10.929 | 0.018 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 95.0 / 0.0 | 0.568 / 0.648 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.988 / 11.882 | 0.02 / 0.007 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.601 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 97.731 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.802 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 33.052 / 0.0 | 0.007 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.86 | 0.0 / 0.0 | 574.84 / 573.91 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.86 (8.0 / 8.0) |
| mam | 13.85 / 13.85 | 1.0 / 1.0 | 112.56 / 112.59 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.85 (8.0 / 8.0) |
| ponder | 82.58 / 82.59 | 0.11 / 0.11 | 203.58 / 203.62 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.61 / 81.61 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 281.0 / 281.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

## Bench-only jar (quicksilver-bench)

| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |
|---|---|---|---|---|---|---|---|
| bench-plain | spawn | 9.48 | 126.72 | 189 | 123 | 581068 | [] |
| bench-plain | flythrough | 7.14 | 295.7 | 142 | 123 | 6941564 | [] |
| bench-plain | water_glass | 17.2 | 67.61 | 344 | 124 | 16472 | [] |
| bench-plain | nether | 18.13 | 62.25 | 362 | 121 | 438356 | [] |
| bench-sodium | spawn | 11.12 | 111.06 | 222 | 148 | 8144 | [] |
| bench-sodium | flythrough | 7.63 | 303.96 | 152 | 158 | 268492 | [] |
| bench-sodium | water_glass | 19.61 | 66.56 | 220 | 135 | 9564 | [] |
| bench-sodium | nether | 25.38 | 52.06 | 14 | 129 | 263888 | [] |

## Direct buffers per scene (reported; open bug, see docs/progress.md)

| run | scene | start MB | peak MB (tick) | end MB | rise |
|---|---|---|---|---|---|
| core-bench-plain | spawn | 3.48 | 3.48 (0) | 3.48 |  |
| core-bench-plain | water_glass | 3.48 | 3.48 (0) | 3.48 |  |
| core-bench-plain | nether | 3.48 | 3.48 (0) | 3.48 |  |
| core-bench-plain | flythrough | 3.48 | 3.48 (0) | 3.48 |  |
| core-bench-sodium | spawn | 3.48 | 3.48 (0) | 3.48 |  |
| core-bench-sodium | water_glass | 3.48 | 3.48 (0) | 3.48 |  |
| core-bench-sodium | nether | 3.35 | 3.35 (0) | 3.35 |  |
| core-bench-sodium | flythrough | 3.48 | 3.48 (0) | 3.48 |  |
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
| core-measure-full | flat_plants | 3.21 | 3.21 (0) | 3.21 |  |
| core-measure-full | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-full | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-full | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | spawn | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | water_glass | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | nether | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | mobs | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | outline | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | occlusion | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-old | occlusion_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-old | translucent_move | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-old | particles | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-old | animation | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-old | animated_items | 5.48 | 5.48 (0) | 5.48 |  |
| core-measure-old | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-old | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-old | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-measure-old | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-old | flythrough | 3.48 | 3.48 (0) | 3.48 |  |
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
| core-measure-vk | flat_plants | 3.45 | 3.45 (0) | 3.45 |  |
| core-measure-vk | renderer_off | 3.59 | 3.59 (0) | 3.59 |  |
| core-measure-vk | water_glass_oit | 3.59 | 3.59 (0) | 3.59 |  |
| core-measure-vk | translucent_move_oit | 3.71 | 3.71 (0) | 3.71 |  |
| core-measure-vk | flythrough | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-full | spawn | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | water_glass | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | nether | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | mobs | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | outline | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | occlusion | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | occlusion_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | translucent_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | particles | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | animation | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | animated_items | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | flat_plants | 3.21 | 3.21 (0) | 3.21 |  |
| core-quicksilver-full | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-full | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-full | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | flythrough | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | spawn | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | water_glass | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | nether | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | mobs | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | outline | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | occlusion | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | occlusion_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | translucent_move | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | particles | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | animation | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | animated_items | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | flat_plants | 3.34 | 3.34 (0) | 3.34 |  |
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
| core-quicksilver-vk | flat_plants | 3.45 | 3.45 (0) | 3.45 |  |
| core-quicksilver-vk | renderer_off | 3.59 | 3.59 (0) | 3.59 |  |
| core-quicksilver-vk | water_glass_oit | 3.59 | 3.59 (0) | 3.59 |  |
| core-quicksilver-vk | translucent_move_oit | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | flythrough | 3.46 | 3.46 (0) | 3.46 |  |
| siftec-measure-full | factory | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-full | sift | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-full | hub | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-full | mam | 8.24 | 8.24 (0) | 8.24 | between scenes +4.00 |
| siftec-measure-full | ponder | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-measure-full | map | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-measure-full | hologram | 8.18 | 8.18 (0) | 8.18 |  |
| siftec-measure-full | hud | 8.18 | 8.18 (0) | 8.18 |  |
| siftec-measure-full | create_ct | 8.18 | 8.18 (0) | 8.18 |  |
| siftec-measure-full | factory_flat | 3.54 | 3.54 (0) | 3.54 |  |
| siftec-measure-old | factory | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-old | sift | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-old | hub | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-measure-old | mam | 8.24 | 8.24 (0) | 8.24 | between scenes +4.00 |
| siftec-measure-old | ponder | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-measure-old | map | 8.06 | 8.06 (0) | 8.06 |  |
| siftec-measure-old | hologram | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-measure-old | hud | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-measure-old | create_ct | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-measure-old | factory_flat | 5.54 | 5.54 (0) | 5.54 |  |
| siftec-measure-vk | factory | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | sift | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | hub | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | mam | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | ponder | 5.98 | 5.98 (0) | 5.98 |  |
| siftec-measure-vk | map | 5.79 | 5.79 (0) | 5.79 |  |
| siftec-measure-vk | hud | 5.85 | 5.85 (0) | 5.85 |  |
| siftec-measure-vk | create_ct | 5.85 | 5.85 (0) | 5.85 |  |
| siftec-measure-vk | factory_flat | 5.52 | 5.52 (0) | 5.52 |  |
| siftec-quicksilver-full | factory | 3.99 | 3.99 (0) | 3.99 |  |
| siftec-quicksilver-full | sift | 3.99 | 3.99 (0) | 3.99 |  |
| siftec-quicksilver-full | hub | 3.99 | 3.99 (0) | 3.99 |  |
| siftec-quicksilver-full | mam | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-quicksilver-full | ponder | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-quicksilver-full | map | 5.81 | 5.81 (0) | 5.81 |  |
| siftec-quicksilver-full | hologram | 5.93 | 5.93 (0) | 5.93 |  |
| siftec-quicksilver-full | hud | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-quicksilver-full | create_ct | 5.99 | 5.99 (0) | 5.99 |  |
| siftec-quicksilver-full | factory_flat | 3.54 | 3.54 (0) | 3.54 |  |
| siftec-quicksilver-old | factory | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-old | sift | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-old | hub | 4.24 | 4.24 (0) | 4.24 |  |
| siftec-quicksilver-old | mam | 8.24 | 8.24 (0) | 8.24 | between scenes +4.00 |
| siftec-quicksilver-old | ponder | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-quicksilver-old | map | 8.12 | 8.12 (0) | 8.12 |  |
| siftec-quicksilver-old | hologram | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-quicksilver-old | hud | 8.18 | 8.18 (0) | 8.18 |  |
| siftec-quicksilver-old | create_ct | 8.24 | 8.24 (0) | 8.24 |  |
| siftec-quicksilver-old | factory_flat | 3.54 | 3.54 (0) | 3.54 |  |
| siftec-quicksilver-vk | factory | 4.23 | 4.23 (0) | 4.23 |  |
| siftec-quicksilver-vk | sift | 4.23 | 4.23 (0) | 4.23 |  |
| siftec-quicksilver-vk | hub | 4.23 | 4.23 (0) | 4.23 |  |
| siftec-quicksilver-vk | mam | 8.23 | 8.23 (0) | 8.23 | between scenes +4.00 |
| siftec-quicksilver-vk | ponder | 8.23 | 8.23 (0) | 8.23 |  |
| siftec-quicksilver-vk | map | 8.04 | 8.04 (0) | 8.04 |  |
| siftec-quicksilver-vk | hud | 8.16 | 8.16 (0) | 8.16 |  |
| siftec-quicksilver-vk | create_ct | 8.23 | 8.23 (0) | 8.23 |  |
| siftec-quicksilver-vk | factory_flat | 3.46 | 3.46 (0) | 3.46 |  |

Rises of 4 MB or more: siftec-measure-full/mam: between scenes +4.00; siftec-measure-old/mam: between scenes +4.00; siftec-quicksilver-old/mam: between scenes +4.00; siftec-quicksilver-vk/mam: between scenes +4.00

