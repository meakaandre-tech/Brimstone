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
| spawn | 0.0599 | 10.05 | 0.3006 | 0.0715 | 0.2041 |
| flythrough | 0.0003 | 14.95 | 0.001 | 0.001 | 0.0011 |
| water_glass | 0.0042 | 6.45 | 0.0054 | 0.0036 | 0.0044 |
| nether | 0.0 | 0.8 | 0.0003 | 0.0098 | 0.0003 |
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
| translucent_move | 0.0002 | 0.3 | 0.0003 | 0.0 | 0.0 |
| translucent_move.x2404 | 0.0003 | 0.3 | 0.0003 | 0.0003 | 0.0003 |
| translucent_move.x2414 | 0.0002 | 0.3 | 0.0003 | 0.0001 | 0.0001 |
| translucent_move.x2424 | 0.0 | 0.3 | 0.0001 | 0.0002 | 0.0003 |
| translucent_move.low | 0.0008 | 0.3 | 0.0015 | 0.0012 | 0.0011 |
| translucent_move.back | 0.0003 | 0.3 | 0.0002 | 0.0 | 0.0 |
| translucent_move.bare_back | 0.0001 | 0.3 | 0.0 | 0.0 | 0.0001 |
| water_glass_oit | 0.0043 | 0.3 | 0.0038 | 0.0054 | 0.0047 |
| translucent_move_oit | 0.0001 | 0.3 | 0.0001 | 0.0002 | 0.0002 |
| translucent_move_oit.x2404 | 0.0002 | 0.3 | 0.0003 | 0.0004 | 0.0005 |
| translucent_move_oit.x2414 | 0.0002 | 0.3 | 0.0003 | 0.0004 | 0.0002 |
| translucent_move_oit.x2424 | 0.0001 | 0.3 | 0.0002 | 0.0004 | 0.0001 |
| translucent_move_oit.low | 0.0003 | 0.3 | 0.0012 | 0.0013 | 0.0016 |
| translucent_move_oit.back | 0.0 | 0.3 | 0.0 | 0.0 | 0.0001 |
| translucent_move_oit.bare_back | 0.0002 | 0.3 | 0.0002 | 0.0 | 0.0 |
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
| spawn | 92.96 | 18057 | 2355 (2863) | 389 (931) | 368 (921) |
| flythrough | 97.27 | 6020 | 46 (290) | 46 (290) | 51 (293) |
| water_glass | 95.81 | 25508 | 32 (2630) | 11 (2608) | 13 (2724) |
| nether | 99.42 | 905 | 3 (458) | 89 (544) | 3 (470) |
| mobs | 99.39 | 600 | 0 (0) | 0 (0) | 0 (0) |
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
| translucent_move | 96.93 | 13 | 2 (77) | 1 (76) | 1 (67) |
| translucent_move.x2404 | 95.93 | 15 | 4 (104) | 4 (104) | 4 (88) |
| translucent_move.x2414 | 95.86 | 13 | 3 (106) | 2 (106) | 2 (99) |
| translucent_move.x2424 | 96.74 | 13 | 2 (79) | 1 (78) | 2 (68) |
| translucent_move.low | 96.11 | 41 | 17 (22) | 15 (21) | 12 (17) |
| translucent_move.back | 96.93 | 13 | 2 (77) | 3 (78) | 1 (67) |
| translucent_move.bare_back | 96.9 | 13 | 2 (78) | 3 (78) | 1 (67) |
| water_glass_oit | 95.81 | 57 | 16 (2618) | 29 (2632) | 21 (2737) |
| translucent_move_oit | 96.93 | 12 | 2 (77) | 2 (77) | 1 (67) |
| translucent_move_oit.x2404 | 95.93 | 16 | 7 (108) | 5 (105) | 5 (89) |
| translucent_move_oit.x2414 | 95.86 | 10 | 2 (105) | 4 (107) | 3 (104) |
| translucent_move_oit.x2424 | 96.74 | 15 | 1 (78) | 2 (79) | 1 (67) |
| translucent_move_oit.low | 96.11 | 40 | 16 (21) | 15 (21) | 18 (21) |
| translucent_move_oit.back | 96.93 | 12 | 2 (77) | 1 (76) | 3 (69) |
| translucent_move_oit.bare_back | 96.9 | 15 | 2 (77) | 2 (77) | 1 (67) |
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
| spawn | 0.2036 / 341 | 0.0662 / 308 | 0.4388 / 2289 |
| flythrough | 0.0 / 0 | 0.0 / 1 | 0.0 / 0 |
| water_glass | 0.0038 / 13 | 0.0039 / 15 | 0.0044 / 13 |
| nether | 0.0 / 0 | 0.0085 / 92 | 0.0034 / 31 |
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
| translucent_move | 0.0002 / 0 | 0.0003 / 3 | 0.0001 / 1 |
| translucent_move.x2404 | 0.0 / 0 | 0.0001 / 2 | 0.0001 / 0 |
| translucent_move.x2414 | 0.0001 / 0 | 0.0 / 1 | 0.0 / 2 |
| translucent_move.x2424 | 0.0 / 1 | 0.0001 / 0 | 0.0 / 0 |
| translucent_move.low | 0.0008 / 4 | 0.0003 / 5 | 0.0007 / 5 |
| translucent_move.back | 0.0003 / 1 | 0.0002 / 2 | 0.0001 / 3 |
| translucent_move.bare_back | 0.0 / 0 | 0.0 / 2 | 0.0002 / 1 |
| water_glass_oit | 0.0034 / 12 | 0.0048 / 22 | 0.0036 / 15 |
| translucent_move_oit | 0.0001 / 0 | 0.0002 / 3 | 0.0002 / 2 |
| translucent_move_oit.x2404 | 0.0001 / 0 | 0.0001 / 2 | 0.0002 / 0 |
| translucent_move_oit.x2414 | 0.0001 / 1 | 0.0001 / 2 | 0.0001 / 0 |
| translucent_move_oit.x2424 | 0.0001 / 1 | 0.0003 / 1 | 0.0001 / 0 |
| translucent_move_oit.low | 0.0004 / 3 | 0.0004 / 4 | 0.0005 / 5 |
| translucent_move_oit.back | 0.0 / 2 | 0.0001 / 2 | 0.0001 / 3 |
| translucent_move_oit.bare_back | 0.0004 / 3 | 0.0 / 0 | 0.0001 / 2 |
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
| spawn | 13.0 (base 13.6) | 14 (base 14) | 22288 (base 22476) | 259.75 (base 259.07) | 4.24 (base 3.99) |
| flythrough | 13.07 (reported, not gated) | 21 (reported, not gated) | 167356 (reported, not gated) | 315.21 (base 324.25) | 3.73 (base 4.24) |
| water_glass | 8.76 (base 8.97) | 15 (base 15) | 15072 (base 15576) | 244.54 (base 250.02) | 4.24 (base 5.99) |
| nether | 13.34 (base 15.63) | 12 (base 12) | 18884 (base 19648) | 266.0 (base 273.53) | 4.24 (base 5.99) |
| mobs | 19.67 (base 19.77) | 16 (base 17) | 616168 (base 1010948) | 264.3 (base 268.37) | 4.24 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.31 (base 264.69) | 4.24 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100388 (base 126320) | 269.91 (base 265.1) | 4.24 (base 4.24) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82260 (reported, not gated) | 270.19 (base 265.74) | 4.24 (base 4.24) |
| translucent_move | 8.74 (reported, not gated) | 15 (reported, not gated) | 10156 (reported, not gated) | 272.09 (base 267.48) | 4.24 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 14940 (base 14688) | 244.19 (base 245.02) | 3.35 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10212 (reported, not gated) | 247.25 (base 247.19) | 3.48 (base 3.73) |
| flat_plants | 16.48 (base 18.29) | 15 (base 15) | 9788 (base 9812) | 246.87 (base 246.92) | 3.48 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7364 (base 7164) | 239.64 (base 241.33) | 3.6 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 267.52 (base 261.44) | 4.24 (base 5.99) |
| animation | 49 median (avg 39.94) (base 49) | 15 (base 15) | 2204 (base 2056) | 264.87 (base 260.68) | 4.24 (base 5.99) |
| animated_items | 60 median (avg 46.66) (base 60) | 24 (base 23) | 22136 (base 21940) | 239.78 (base 239.81) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (15.29) | 123.1 / 123 (9.35) | median and average, x 1.10 + 2 |
| flythrough | 13.07 / 13 (10.86) | 123.05 / 123 (5.51) | median and average, at most vanilla + 2 |
| water_glass | 8.76 / 9 (26.34) | 124.0 / 124 (15.69) | median, at most vanilla |
| nether | 13.34 / 16 (26.03) | 120.65 / 121 (15.59) | median, x 1.10 + 2 |
| mobs | 19.67 / 20 (29.82) | 134.0 / 134 (19.49) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.33 / 133 (29.96) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (30.0) | 94.46 / 133 (29.96) | median and average, x 1.10 + 2 |
| translucent_move | 8.74 / 9 (26.89) | 124.0 / 124 (16.06) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (14.37) | 129.0 / 129 (8.78) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (14.01) | 129.0 / 129 (8.3) | median and average, at most vanilla |
| flat_plants | 16.48 / 19 (25.79) | 123.89 / 124 (15.87) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.94) | 100.17 / 123 (24.93) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.33 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 144.09 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.66 / 60 (30.0) | 155.09 / 190 (29.99) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.47 | 36.0 / 22.47 | 0.0 / 0.0 | 416.0 / 36.833 |
| flythrough | 144.0 / 89.211 | 144.0 / 89.211 | 0.0 / 0.0 | 416.0 / 144.172 |
| water_glass | 36.0 / 12.413 | 36.0 / 12.413 | 0.0 / 0.0 | 416.0 / 21.569 |
| nether | 20.0 / 15.329 | 20.0 / 15.329 | 0.0 / 0.0 | 256.0 / 26.899 |
| mobs | 16.0 / 9.689 | 16.0 / 9.689 | 0.0 / 0.0 | 256.0 / 16.934 |
| outline | 16.0 / 1.567 | 16.0 / 1.567 | 0.0 / 0.0 | 256.0 / 2.75 |
| occlusion | 20.0 / 2.508 | 20.0 / 2.508 | 0.0 / 0.0 | 416.0 / 4.521 |
| occlusion_move | 20.0 / 3.313 | 20.0 / 3.313 | 0.0 / 0.0 | 416.0 / 6.037 |
| translucent_move | 28.0 / 13.102 | 28.0 / 13.102 | 0.0 / 0.0 | 416.0 / 23.051 |
| water_glass_oit | 20.0 / 12.413 | 20.0 / 12.413 | 0.0 / 0.0 | 416.0 / 21.569 |
| translucent_move_oit | 28.0 / 12.689 | 28.0 / 12.689 | 0.0 / 0.0 | 416.0 / 22.428 |
| flat_plants | 24.0 / 14.358 | 24.0 / 14.358 | 0.0 / 0.0 | 416.0 / 24.847 |
| renderer_off | 24.0 / 9.034 | 24.0 / 9.034 | 0.0 / 0.0 | 416.0 / 15.858 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.01 | 766.269 | 16 (vanilla 28) | 1355068 | 39.365 / 0.112 | 22.47 / 36.0 | 36.184 | 2388 / 0 | 2.313 (4.764) |
| flythrough | indirect, one multi-draw per arena and layer | 3.014 | 933.705 | 16 (vanilla 28) | 5411240 | 14.482 / 0.911 | 89.211 / 144.0 | 144.496 | 6784 / 0 | 3.916 (10.696) |
| water_glass | indirect, one multi-draw per arena and layer | 3.006 | 270.513 | 16 (vanilla 28) | 769752 | 31.882 / 0.101 | 12.413 / 36.0 | 20.555 | 3042 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.004 | 472.908 | 16 (vanilla 28) | 962732 | 26.822 / 0.216 | 15.329 / 20.0 | 25.708 | 4118 / 0 | 1.81 (2.24) |
| mobs | indirect, one multi-draw per arena and layer | 2.003 | 197.33 | 16 (vanilla 28) | 604472 | 24.385 / 0.188 | 9.689 / 16.0 | 16.141 | 4556 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 98320 | 23.877 / 0.186 | 1.567 / 16.0 | 2.625 | 4654 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 72.12 | 16 (vanilla 28) | 156124 | 22.669 / 0.176 | 2.508 / 20.0 | 4.169 | 4923 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 77.457 | 16 (vanilla 28) | 206172 | 21.527 / 0.167 | 3.313 / 20.0 | 5.505 | 5204 / 0 | 2.848 (9.017) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.006 | 253.264 | 16 (vanilla 28) | 806588 | 18.508 / 0.15 | 13.102 / 28.0 | 21.538 | 6069 / 0 | 4.402 (12.761) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.017 | 278.972 | 16 (vanilla 28) | 769752 | 7.494 / 0.043 | 12.413 / 20.0 | 20.555 | 1092 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.018 | 267.832 | 16 (vanilla 28) | 780696 | 6.914 / 0.117 | 12.689 / 28.0 | 20.847 | 2459 / 0 | 4.98 (11.091) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.006 | 289.56 | 16 (vanilla 28) | 894468 | 5.97 / 0.009 | 14.358 / 24.0 | 23.885 | 961 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 208.347 | 16 (vanilla 28) | 566816 | 2.338 / 0.02 | 9.034 / 24.0 | 15.136 | 609 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 18.459 / 0.15 | 0.131 / 28.0 | 0.225 | 6115 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 18.306 / 0.149 | 0.302 / 28.0 | 0.518 | 6217 / 0 | 1.082 (1.538) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.253 / 0.149 | 0.153 / 28.0 | 0.261 | 6267 / 0 | 0.754 (0.944) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 152.588 | 1.0 | 0.0 | 0 | 0 | 53 (0.017 / 0.028 / 0.03; 0) | 0 | 46692 / 0 / 0 |
| flythrough | 195.803 | 1.0 | 0.0 | 0 | 0 | 897 (0.013 / 0.031 / 2.675; 0) | 56 | 42685 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2108 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.006 / 0.009 / 0.014; 0) | 32 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.292 / 2.614 / 4.57; 0) | 180 | 3766 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 861 | 0 (None / None / None; 0) | 0 | 1148 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 843 | 0 (None / None / None; 0) | 0 | 1967 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2068 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 331.817 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.027 (0.083; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 440.399 | 1.0 (0.0, 0.0) | 1.11 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.013 (0.05; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.102 (0.29; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 50.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.049 (0.176; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 52.36 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.278) | 0.045 (0.222; 0) | 0 | 2781 / 1505094 / 0 / 0 |
| translucent_move | 139.046 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 139.438 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 156.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.037; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 114.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.01 (0.071; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.096 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 99.469 / 0.0 | 0.024 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.387 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.803 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.878 / 54.86 | 0.011 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 7.673 | 0.65 / 0.896 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 56.078 / 10.601 | 0.078 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.323 / 12.266 | 0.005 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.24 / 54.244 | 0.017 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.703 / 12.698 | 0.007 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 7.752 | 0.89 / 0.95 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.908 / 1.764 | 0.017 / 0.01 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 92.369 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.004 / 0.059 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.426 / 32.277 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.013 | 56 (3) | 40.51 / 9.24 / 31.27 | 3 / 0 | 10.988 |
| animated_items | 136.426 / 27.992 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.473 / 7.96 / 32.513 | 0 / 0 | 9.0 |

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
| spawn | 48.37 (base 49.0) | 12 (base 13) | 1380 (base 6372) | 258.11 (base 256.29) | 4.24 (base 3.73) |
| flythrough | 67.3 (reported, not gated) | 51 (reported, not gated) | 734636 (reported, not gated) | 313.08 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 20.0 (base 20.0) | 13 (base 14) | 7204 (base 10264) | 244.77 (base 249.83) | 4.24 (base 5.99) |
| nether | 33.82 (base 33.79) | 10 (base 11) | 4736 (base 10656) | 265.54 (base 272.57) | 4.24 (base 5.99) |
| mobs | 28.95 (base 28.97) | 14 (base 16) | 610516 (base 1015892) | 263.92 (base 268.23) | 4.24 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 263.73 (base 264.41) | 4.24 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 269.68 (base 264.64) | 4.24 (base 4.24) |
| occlusion_move | 24.04 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 269.74 (base 265.66) | 4.24 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 2612 (reported, not gated) | 271.79 (base 268.19) | 4.24 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7316 (base 7604) | 244.31 (base 245.78) | 3.35 (base 3.73) |
| translucent_move_oit | 25.52 (reported, not gated) | 13 (reported, not gated) | 2948 (reported, not gated) | 246.97 (base 248.15) | 3.48 (base 3.73) |
| flat_plants | 32.82 (base 33.0) | 13 (base 13) | 1604 (base 1716) | 246.69 (base 248.11) | 3.48 (base 5.99) |
| renderer_off | 17.75 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 239.69 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.72 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 267.06 (base 261.4) | 4.24 (base 4.24) |
| animation | 49 median (avg 39.95) (base 49) | 13 (base 13) | 1716 (base 1716) | 264.55 (base 260.68) | 4.24 (base 4.24) |
| animated_items | 60 median (avg 46.75) (base 60) | 20 (base 22) | 21348 (base 21668) | 239.72 (base 239.53) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 48.37 / 48 (10.76) | 907.0 / 907 (10.21) | median and average, x 1.10 + 2 |
| flythrough | 67.3 / 68 (6.02) | 1004.92 / 1012 (5.96) | median and average, at most vanilla |
| water_glass | 20.0 / 20 (18.1) | 389.0 / 389 (17.2) | median, at most vanilla |
| nether | 33.82 / 34 (16.27) | 590.88 / 591 (16.87) | median, x 1.10 + 2 |
| mobs | 28.95 / 29 (20.87) | 317.45 / 329 (22.13) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 127.2 / 166 (30.0) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (30.01) | 163.27 / 202 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 24.04 / 24 (29.83) | 168.68 / 203 (29.97) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (16.78) | 374.01 / 373 (17.31) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (9.69) | 400.0 / 400 (8.69) | median, at most vanilla |
| translucent_move_oit | 25.52 / 26 (8.8) | 390.97 / 392 (8.7) | median and average, at most vanilla |
| flat_plants | 32.82 / 33 (16.62) | 403.0 / 403 (17.57) | median, x 1.10 + 2 |
| renderer_off | 17.75 / 18 (26.61) | 304.43 / 328 (25.08) | median, x 1.10 + 2 |
| particles | 10.72 / 11 (30.0) | 97.27 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.01) | 156.22 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.75 / 60 (30.0) | 166.91 / 202 (29.99) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.088 | 36.0 / 22.088 | 0.0 / 0.0 | 416.0 / 38.725 |
| flythrough | 112.0 / 84.269 | 112.0 / 84.269 | 0.0 / 0.0 | 416.0 / 145.233 |
| water_glass | 36.0 / 12.44 | 36.0 / 12.44 | 0.0 / 0.0 | 416.0 / 21.569 |
| nether | 20.0 / 15.328 | 20.0 / 15.328 | 0.0 / 0.0 | 256.0 / 26.899 |
| mobs | 16.0 / 9.689 | 16.0 / 9.689 | 0.0 / 0.0 | 256.0 / 16.934 |
| outline | 16.0 / 1.567 | 16.0 / 1.567 | 0.0 / 0.0 | 256.0 / 2.75 |
| occlusion | 20.0 / 2.629 | 20.0 / 2.629 | 0.0 / 0.0 | 416.0 / 4.562 |
| occlusion_move | 20.0 / 3.397 | 20.0 / 3.397 | 0.0 / 0.0 | 416.0 / 5.912 |
| translucent_move | 28.0 / 13.126 | 28.0 / 13.126 | 0.0 / 0.0 | 416.0 / 22.724 |
| water_glass_oit | 20.0 / 12.44 | 20.0 / 12.44 | 0.0 / 0.0 | 416.0 / 21.569 |
| translucent_move_oit | 28.0 / 12.715 | 28.0 / 12.715 | 0.0 / 0.0 | 416.0 / 22.101 |
| flat_plants | 24.0 / 14.308 | 24.0 / 14.308 | 0.0 / 0.0 | 416.0 / 24.616 |
| renderer_off | 24.0 / 9.034 | 24.0 / 9.034 | 0.0 / 0.0 | 416.0 / 15.858 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 38.178 | 756.519 | 16 (vanilla 28) | 1330612 | 42.979 / 0.106 | 22.088 / 36.0 | 35.531 | 2395 / 0 | 5.806 (9.572) |
| flythrough | plain, one multi-draw per region and layer | 57.642 | 899.6 | 16 (vanilla 28) | 5108872 | 15.301 / 0.962 | 84.269 / 112.0 | 136.422 | 6421 / 0 | 7.947 (19.595) |
| water_glass | plain, one multi-draw per region and layer | 14.039 | 272.753 | 16 (vanilla 28) | 771412 | 34.878 / 0.095 | 12.44 / 36.0 | 20.599 | 3072 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.062 | 473.452 | 16 (vanilla 28) | 962764 | 29.204 / 0.204 | 15.328 / 20.0 | 25.709 | 4144 / 0 | 3.036 (3.924) |
| mobs | plain, one multi-draw per region and layer | 11.026 | 197.472 | 16 (vanilla 28) | 604472 | 26.57 / 0.179 | 9.689 / 16.0 | 16.141 | 4580 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 98320 | 25.991 / 0.177 | 1.567 / 16.0 | 2.625 | 4681 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 72.12 | 16 (vanilla 28) | 164020 | 24.725 / 0.168 | 2.629 / 20.0 | 4.38 | 4934 / 0 | 2.718 (3.282) |
| occlusion_move | plain, one multi-draw per region and layer | 10.993 | 77.442 | 16 (vanilla 28) | 212596 | 23.47 / 0.159 | 3.397 / 20.0 | 5.677 | 5221 / 0 | 6.789 (16.54) |
| translucent_move | plain, one multi-draw per region and layer | 12.036 | 252.343 | 16 (vanilla 28) | 809716 | 20.405 / 0.143 | 13.126 / 28.0 | 21.622 | 6010 / 0 | 7.289 (20.999) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.083 | 281.451 | 16 (vanilla 28) | 771412 | 7.514 / 0.043 | 12.44 / 20.0 | 20.599 | 1086 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.614 | 267.341 | 16 (vanilla 28) | 783876 | 7.286 / 0.191 | 12.715 / 28.0 | 20.932 | 2528 / 0 | 10.946 (24.679) |
| flat_plants | plain, one multi-draw per region and layer | 17.051 | 289.87 | 16 (vanilla 28) | 891428 | 5.445 / 0.01 | 14.308 / 24.0 | 23.804 | 924 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.024 | 208.391 | 16 (vanilla 28) | 566828 | 2.344 / 0.02 | 9.034 / 24.0 | 15.136 | 607 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 20.34 / 0.143 | 0.131 / 28.0 | 0.225 | 6056 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.173 / 0.142 | 0.302 / 28.0 | 0.518 | 6158 / 0 | 1.576 (2.11) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.114 / 0.142 | 0.153 / 28.0 | 0.261 | 6208 / 0 | 1.221 (1.744) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 154.0 | 16.0 | 0.0 | 0 | 0 | 38 (0.026 / 0.044 / 0.049; 0) | 0 | 33110 / 0 / 0 |
| flythrough | 185.769 | 26.314 | 0.0 | 0 | 0 | 846 (0.046 / 0.044 / 8.85; 0) | 43 | 22478 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1448 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.058 / 0.02 / 1.814; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.136 / 0.176 / 2.521; 0) | 180 | 2352 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 582 | 0 (None / None / None; 0) | 0 | 776 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 531 | 0 (None / None / None; 0) | 0 | 1239 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1332 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2132 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 325.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.029 (0.083; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 437.455 | 1.0 (0.0, 0.0) | 1.231 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.078; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.138 (0.275; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 50.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.063 (0.098; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 52.351 | 10.0 (9.0, 2.36) | 6.0 (6.0, 2.276) | 0.057 (0.251; 0) | 0 | 2772 / 1500606 / 0 / 0 |
| translucent_move | 137.982 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 138.542 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 156.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.022; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 114.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.036; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.369 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 86.39 / 0.372 | 0.026 / 0.011 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.357 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.826 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.263 / 52.296 | 0.01 / 0.02 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.878 / 0.818 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 43.389 / 11.966 | 0.069 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 103.552 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.539 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.283 / 11.241 | 0.004 / 0.015 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.796 / 53.866 | 0.011 / 0.021 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.711 / 12.667 | 0.006 / 0.012 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 1.0 / 0.822 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.19 / 2.0 | 0.014 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 91.633 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.008 / 0.063 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.012 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.988 |
| animated_items | 136.247 / 28.028 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.51 / 7.97 / 32.54 | 0 / 0 | 9.0 |

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
| spawn | 13.39 (base 14.05) | 15 (base 15) | 22568 (base 27764) | 258.72 (base 257.2) | 4.23 (base 3.98) |
| flythrough | 13.06 (reported, not gated) | 42 (reported, not gated) | 685500 (reported, not gated) | 312.09 (base 318.06) | 3.46 (base 4.23) |
| water_glass | 9.0 (base 9.0) | 15 (base 15) | 15332 (base 15108) | 245.22 (base 244.39) | 4.23 (base 4.23) |
| nether | 15.85 (base 15.71) | 12 (base 12) | 18524 (base 19912) | 266.35 (base 268.95) | 4.23 (base 4.23) |
| mobs | 19.97 (base 19.97) | 17 (base 17) | 616328 (base 1008176) | 264.86 (base 264.31) | 4.23 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.64 (base 264.58) | 4.23 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100296 (base 126356) | 270.5 (base 264.78) | 4.23 (base 3.98) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82168 (reported, not gated) | 270.87 (base 265.16) | 4.23 (base 4.23) |
| translucent_move | 9.0 (reported, not gated) | 15 (reported, not gated) | 9972 (reported, not gated) | 272.93 (base 268.35) | 4.23 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15500 (base 15024) | 244.12 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10008 (reported, not gated) | 246.51 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 18.84 (base 18.96) | 15 (base 15) | 9752 (base 9864) | 247.49 (base 248.07) | 3.59 (base 5.98) |
| renderer_off | 7.83 (base 7.83) | 14 (base 14) | 7364 (base 7180) | 239.97 (base 241.13) | 3.34 (base 4.1) |
| particles | 9.72 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 267.75 (base 261.4) | 4.23 (base 5.98) |
| animation | 49 median (avg 39.95) (base 49) | 15 (base 15) | 2204 (base 2056) | 265.25 (base 260.68) | 4.23 (base 5.98) |
| animated_items | 60 median (avg 46.72) (base 60) | 24 (base 23) | 22136 (base 21940) | 240.52 (base 239.74) | 4.23 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.39 / 13 (9.68) | 124.01 / 124 (9.36) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (5.78) | 123.06 / 123 (5.44) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (16.28) | 124.0 / 124 (15.03) | median, at most vanilla |
| nether | 15.85 / 16 (16.24) | 120.88 / 121 (15.03) | median, x 1.10 + 2 |
| mobs | 19.97 / 20 (20.43) | 134.0 / 134 (18.88) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.53 / 133 (29.9) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.89) | 94.33 / 133 (29.96) | median and average, x 1.10 + 2 |
| translucent_move | 9.0 / 9 (16.85) | 124.0 / 124 (15.64) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (8.83) | 129.0 / 129 (8.31) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (8.18) | 129.0 / 129 (7.89) | median and average, at most vanilla |
| flat_plants | 18.84 / 19 (16.26) | 123.93 / 124 (15.22) | median, x 1.10 + 2 |
| renderer_off | 7.83 / 8 (24.15) | 111.04 / 123 (22.29) | median, x 1.10 + 2 |
| particles | 9.72 / 10 (30.0) | 86.33 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.72 / 60 (30.0) | 155.22 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.914 | 36.0 / 22.914 | 0.0 / 0.0 | 416.0 / 37.863 |
| flythrough | 112.0 / 83.807 | 112.0 / 83.807 | 0.0 / 0.0 | 416.0 / 144.337 |
| water_glass | 36.0 / 12.723 | 36.0 / 12.723 | 0.0 / 0.0 | 416.0 / 21.876 |
| nether | 20.0 / 15.584 | 20.0 / 15.584 | 0.0 / 0.0 | 256.0 / 26.898 |
| mobs | 16.0 / 9.689 | 16.0 / 9.689 | 0.0 / 0.0 | 256.0 / 16.934 |
| outline | 16.0 / 1.567 | 16.0 / 1.567 | 0.0 / 0.0 | 256.0 / 2.75 |
| occlusion | 20.0 / 2.575 | 20.0 / 2.575 | 0.0 / 0.0 | 416.0 / 4.853 |
| occlusion_move | 20.0 / 3.467 | 20.0 / 3.467 | 0.0 / 0.0 | 416.0 / 6.195 |
| translucent_move | 28.0 / 13.425 | 28.0 / 13.425 | 0.0 / 0.0 | 416.0 / 22.89 |
| water_glass_oit | 20.0 / 12.413 | 20.0 / 12.413 | 0.0 / 0.0 | 416.0 / 21.569 |
| translucent_move_oit | 28.0 / 12.967 | 28.0 / 12.967 | 0.0 / 0.0 | 416.0 / 22.169 |
| flat_plants | 24.0 / 14.552 | 24.0 / 14.552 | 0.0 / 0.0 | 416.0 / 25.161 |
| renderer_off | 24.0 / 9.085 | 24.0 / 9.085 | 0.0 / 0.0 | 416.0 / 15.858 |
| particles | 28.0 / 0.233 | 28.0 / 0.233 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.404 | 28.0 / 0.404 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.255 | 28.0 / 0.255 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 785.663 | 16 (vanilla 28) | 1382156 | 42.568 / 0.091 | 22.914 / 36.0 | 36.908 | 2556 / 0 | 3.492 (5.217) |
| flythrough | indirect, one multi-draw per arena and layer | 3.026 | 893.322 | 16 (vanilla 28) | 5078476 | 15.025 / 0.925 | 83.807 / 112.0 | 135.61 | 6304 / 0 | 6.969 (18.179) |
| water_glass | indirect, one multi-draw per arena and layer | 3.009 | 272.837 | 16 (vanilla 28) | 787892 | 34.94 / 0.081 | 12.723 / 36.0 | 21.039 | 3203 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 473.457 | 16 (vanilla 28) | 978676 | 29.381 / 0.164 | 15.584 / 20.0 | 26.133 | 4264 / 0 | 2.873 (4.659) |
| mobs | indirect, one multi-draw per arena and layer | 2.005 | 197.483 | 16 (vanilla 28) | 604472 | 26.736 / 0.144 | 9.689 / 16.0 | 16.141 | 4712 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 98320 | 26.193 / 0.143 | 1.567 / 16.0 | 2.625 | 4809 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 69.115 | 16 (vanilla 28) | 160600 | 25.066 / 0.137 | 2.575 / 20.0 | 4.288 | 5054 / 0 | 2.615 (3.002) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 74.437 | 16 (vanilla 28) | 216160 | 23.82 / 0.13 | 3.467 / 20.0 | 5.772 | 5330 / 0 | 4.083 (7.601) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.009 | 250.464 | 16 (vanilla 28) | 826096 | 20.613 / 0.118 | 13.425 / 28.0 | 22.059 | 6141 / 0 | 5.024 (10.746) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.028 | 279.58 | 16 (vanilla 28) | 769752 | 7.517 / 0.043 | 12.413 / 20.0 | 20.555 | 1080 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.031 | 265.712 | 16 (vanilla 28) | 797216 | 6.714 / 0.094 | 12.967 / 28.0 | 21.288 | 2389 / 0 | 5.322 (12.638) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.009 | 285.877 | 16 (vanilla 28) | 905780 | 5.888 / 0.01 | 14.552 / 24.0 | 24.187 | 920 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.006 | 208.432 | 16 (vanilla 28) | 569484 | 2.343 / 0.02 | 9.085 / 24.0 | 15.207 | 599 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 14756 | 20.566 / 0.118 | 0.233 / 28.0 | 0.394 | 6183 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 25756 | 20.399 / 0.117 | 0.404 / 28.0 | 0.688 | 6285 / 0 | 1.913 (2.903) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 16104 | 20.341 / 0.117 | 0.255 / 28.0 | 0.43 | 6334 / 0 | 1.099 (1.658) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 153.247 | 1.0 | 0.0 | 0 | 0 | 61 (0.032 / 0.05 / 0.301; 0) | 0 | 29730 / 0 / 0 |
| flythrough | 185.017 | 1.0 | 0.0 | 0 | 0 | 817 (0.029 / 0.049 / 6.227; 0) | 38 | 21462 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1304 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.012 / 0.015 / 0.02; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.098 / 0.153 / 0.185; 0) | 180 | 2359 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 531 | 0 (None / None / None; 0) | 0 | 708 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 492 | 0 (None / None / None; 0) | 0 | 1148 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1304 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1932 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 341.268 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.027 (0.074; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 435.095 | 1.0 (0.0, 0.0) | 1.336 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.014 (0.076; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.112 (0.232; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.065 (0.15; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 50.351 | 10.0 (9.0, 2.363) | 6.0 (6.0, 2.284) | 0.051 (0.296; 0) | 0 | 2779 / 1503986 / 0 / 0 |
| translucent_move | 137.05 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 137.848 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 153.0 | 1.0 (0.0, 0.0) | 2.0 (0.0, 0.0) | 0.002 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 114.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.0 (0.023; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.005 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 1.0 / 0.394 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 84.781 / 0.49 | 0.018 / 0.008 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.17 / 0.0 | 0.003 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.828 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 56.481 / 56.531 | 0.009 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.88 / 0.846 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 36.918 / 8.982 | 0.057 / 0.007 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.859 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.1 / 11.03 | 0.004 / 0.012 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 57.167 / 57.085 | 0.009 / 0.02 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.468 / 10.372 | 0.004 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.931 / 0.84 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.339 / 2.227 | 0.011 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 103.139 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.004 / 0.039 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.003 / 0.006 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.987 |
| animated_items | 136.553 / 28.05 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.536 / 7.97 / 32.566 | 0 / 0 | 9.0 |

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
| factory | 0.0 | 0.3 | 0.0146 | 0.0014 | 0.0149 |
| factory_flat | 0.0028 | 0.3 | 0.0038 | 0.0048 | 0.0009 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| hologram | 0.0015 | 0.55 | 0.0048 | 0.0054 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.59 | 500 | 0 (15) | 0 (15) | 0 (14) |
| factory_flat | 95.28 | 40 | 0 (14) | 4 (18) | 0 (14) |
| sift | 93.9 | 100 | 55 (537) | 55 (537) | 61 (520) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 89.12 | 10 | 0 (0) | 0 (0) | 0 (0) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| map | 99.99 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 100.0 | 413 | 45 (4296) | 46 (4297) | None (None) |
| hud | 44.33 | 10 | 0 (0) | 0 (0) | 0 (0) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.0 / 0 | 0.0142 / 0 | 0.0149 / 0 |
| factory_flat | 0.0038 / 0 | 0.0037 / 2 | 0.0 / 0 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| map | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0015 / 11 | 0.0218 / 209 | skipped |
| hud | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.17 (base 12.23) | 15 (base 14) | 6864 (base 6488) | 422.56 (base 421.74) | 4.24 (base 5.48) |
| factory_flat | 20.26 (base 20.25) | 15 (base 14) | 10052 (base 6264) | 421.94 (base 422.37) | 5.48 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 16612 (base 81788) | 439.68 (base 440.07) | 4.24 (base 5.87) |
| hub | 21.55 (base 21.58) | 16 (base 18) | 66364 (base 67484) | 452.53 (base 451.33) | 4.24 (base 4.24) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23536 (base 24656) | 465.48 (base 471.8) | 4.24 (base 8.12) |
| ponder | 87.24 (base 87.25) | 14 (base 14) | 52248 (base 52248) | 464.23 (base 463.81) | 4.24 (base 8.24) |
| map | 30.0 (base 30.0) | 14 (base 14) | 46396 (base 47032) | 489.28 (base 504.13) | 8.12 (base 8.24) |
| hologram | 33 median (avg 32.69) (base 33) | 22 (base 24) | 71744 (base 70368) | 471.09 (base 453.57) | 8.24 (base 8.24) |
| hud | 31.0 (base 31.0) | 16 (base 16) | 71656 (base 71636) | 490.14 (base 494.92) | 8.18 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 479.81 (base 470.81) | 8.18 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.17 / 12 (29.8) | 107.57 / 152 (29.04) | median and average, x 1.10 + 2 |
| factory_flat | 20.26 / 20 (29.95) | 114.06 / 160 (29.83) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (17.09) | 153.0 / 153 (15.32) | median, x 1.10 + 2 |
| hub | 21.55 / 21 (29.83) | 116.45 / 161 (29.37) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.89) | 119.08 / 162 (28.88) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (30.03) | 180.72 / 227 (29.95) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (11.7) | 170.0 / 170 (10.14) | median and average, x 1.10 + 2 |
| hologram | 32.69 / 33 (28.75) | 159.03 / 173 (21.71) | median, at most vanilla |
| hud | 31.0 / 31 (17.99) | 171.0 / 171 (14.3) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 100.0 / 147 (29.98) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.308 | 32.0 / 20.308 | 0.0 / 0.0 | 416.0 / 33.99 |
| ponder | 32.0 / 17.612 | 32.0 / 17.612 | 0.0 / 0.0 | 416.0 / 29.172 |
| map | 40.0 / 27.141 | 40.0 / 27.141 | 0.0 / 0.0 | 416.0 / 47.34 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.386 | 40.0 / 17.386 | 0.0 / 0.0 | 416.0 / 30.551 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 26.572 / 0.302 | 1.453 / 28.0 | 2.447 | 1584 / 0 | 2.174 (5.067) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 3.562 / 0.002 | 1.453 / 12.0 | 2.447 | 296 / 0 | 1.355 (1.328) |
| sift | indirect, one multi-draw per arena and layer | 3.009 | 429.255 | 16 (vanilla 28) | 462632 | 24.01 / 0.333 | 7.389 / 16.0 | 12.354 | 2589 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 23.224 / 0.321 | 1.879 / 12.0 | 3.137 | 2674 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1192748 | 23.941 / 0.352 | 20.308 / 32.0 | 31.85 | 3329 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1021936 | 23.941 / 0.352 | 17.612 / 32.0 | 27.289 | 3330 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.013 | 550.342 | 16 (vanilla 28) | 1701020 | 20.501 / 0.276 | 27.141 / 40.0 | 45.422 | 4133 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.003 | 13.023 | 16 (vanilla 28) | 13744 | 20.401 / 0.273 | 0.238 / 40.0 | 0.367 | 4195 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.008 | 406.125 | 16 (vanilla 28) | 1091380 | 16.379 / 0.229 | 17.386 / 40.0 | 29.143 | 6059 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.232 / 0.227 | 0.171 / 40.0 | 0.283 | 6161 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2052 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 597 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 33.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7755 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2304 / 0 / 0 |
| hud | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 12996 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.025; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 95.345 / 0.0 | 0.543 / 0.503 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 9.685 / 9.446 | 0.016 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.802 / 0.0 | 0.59 / 0.595 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.628 / 10.446 | 0.014 / 0.005 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.923 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 96.568 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 35.231 / 0.0 | 0.013 / 0.002 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 126.129 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 147.713 / 89.358 | 0.11 / 0.04 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.85 | 0.0 / 0.0 | 571.79 / 571.07 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.85 (8.0 / 8.0) |
| mam | 13.86 / 13.83 | 1.0 / 1.0 | 109.98 / 108.99 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.83 (8.0 / 8.0) |
| ponder | 82.58 / 82.57 | 0.11 / 0.0 | 203.14 / 202.88 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.6 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.21 (base 20.23) | 13 (base 13) | 49052 (base 48604) | 423.38 (base 421.54) | 4.24 (base 5.48) |
| factory_flat | 28.27 (base 28.32) | 13 (base 13) | 52240 (base 48340) | 422.22 (base 422.36) | 3.48 (base 5.48) |
| sift | 41.0 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 441.13 (base 441.24) | 4.24 (base 4.24) |
| hub | 25.52 (base 25.54) | 14 (base 17) | 64756 (base 66484) | 453.57 (base 451.46) | 4.24 (base 5.99) |
| mam | 24.52 (base 24.53) | 17 (base 20) | 20916 (base 22916) | 466.16 (base 472.87) | 4.24 (base 5.99) |
| ponder | 87.23 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 464.87 (base 463.76) | 4.24 (base 8.24) |
| map | 66.0 (base 67.0) | 12 (base 12) | 30924 (base 31404) | 491.33 (base 506.16) | 8.06 (base 6.31) |
| hologram | 34 median (avg 33.83) (base 34) | 20 (base 23) | 72444 (base 70624) | 471.75 (base 452.39) | 8.18 (base 6.31) |
| hud | 58.0 (base 58.0) | 14 (base 14) | 59972 (base 59972) | 491.23 (base 493.56) | 8.18 (base 8.18) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 480.92 (base 470.06) | 8.24 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.21 / 20 (29.76) | 181.22 / 226 (29.26) | median and average, x 1.10 + 2 |
| factory_flat | 28.27 / 28 (29.97) | 187.61 / 234 (29.98) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (15.44) | 578.0 / 578 (14.77) | median, x 1.10 + 2 |
| hub | 25.52 / 25 (29.29) | 163.41 / 208 (29.49) | median and average, x 1.10 + 2 |
| mam | 24.52 / 24 (29.93) | 201.22 / 246 (29.4) | median, x 1.10 + 2 |
| ponder | 87.23 / 87 (30.02) | 180.88 / 227 (29.88) | median and average, x 1.10 + 2 |
| map | 66.0 / 66 (10.99) | 715.0 / 715 (10.53) | median and average, x 1.10 + 2 |
| hologram | 33.83 / 34 (23.53) | 172.01 / 184 (21.76) | median, at most vanilla |
| hud | 58.0 / 58 (15.94) | 573.0 / 573 (15.43) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 111.0 / 158 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.575 | 12.0 / 1.575 | 0.0 / 0.0 | 416.0 / 2.809 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.011 | 32.0 / 20.011 | 0.0 / 0.0 | 416.0 / 33.85 |
| ponder | 32.0 / 17.358 | 32.0 / 17.358 | 0.0 / 0.0 | 416.0 / 29.038 |
| map | 40.0 / 27.143 | 40.0 / 27.143 | 0.0 / 0.0 | 416.0 / 47.343 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.386 | 40.0 / 17.386 | 0.0 / 0.0 | 416.0 / 30.55 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 91632 | 26.205 / 0.288 | 1.453 / 28.0 | 2.447 | 1914 / 0 | 4.073 (11.582) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 99196 | 3.525 / 0.001 | 1.575 / 12.0 | 2.649 | 310 / 0 | 1.593 (1.529) |
| sift | plain, one multi-draw per region and layer | 21.068 | 429.39 | 16 (vanilla 28) | 462632 | 23.919 / 0.319 | 7.389 / 16.0 | 12.354 | 2921 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.085 | 16 (vanilla 28) | 117492 | 23.17 / 0.308 | 1.879 / 12.0 | 3.137 | 3006 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.145 | 16 (vanilla 28) | 1174680 | 23.908 / 0.337 | 20.011 / 32.0 | 31.367 | 3645 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1006340 | 23.908 / 0.337 | 17.358 / 32.0 | 26.872 | 3646 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 39.178 | 550.502 | 16 (vanilla 28) | 1701116 | 20.633 / 0.268 | 27.143 / 40.0 | 45.425 | 4467 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.006 | 13.028 | 16 (vanilla 28) | 13744 | 20.535 / 0.265 | 0.238 / 40.0 | 0.367 | 4529 / 0 | None (None) |
| hud | plain, one multi-draw per region and layer | 30.094 | 406.274 | 16 (vanilla 28) | 1091380 | 16.633 / 0.218 | 17.386 / 40.0 | 29.143 | 6356 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 16.488 / 0.216 | 0.171 / 40.0 | 0.283 | 6457 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1854 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 586 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 33.0 | 15.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7260 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1884 / 0 / 0 |
| hud | 36.0 | 9.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 11484 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.005 (0.044; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.032; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 95.009 / 0.0 | 0.532 / 0.543 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.716 / 10.847 | 0.055 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.612 / 0.602 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.535 / 12.319 | 0.017 / 0.005 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.005 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.839 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.685 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.645 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.485 / 0.0 | 0.006 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 128.092 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 157.374 / 96.665 | 0.156 / 0.048 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.85 / 13.89 | 0.0 / 0.0 | 570.78 / 572.36 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.85 / 13.89 (8.0 / 8.0) |
| mam | 13.85 / 13.86 | 1.0 / 1.0 | 109.95 / 110.18 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.85 / 13.86 (8.0 / 8.0) |
| ponder | 82.56 / 82.56 | 0.11 / 0.11 | 203.01 / 202.98 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.59 / 81.59 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.25 (base 14.22) | 15 (base 15) | 51608 (base 51148) | 422.84 (base 420.23) | 4.23 (base 3.71) |
| factory_flat | 22.23 (base 22.31) | 15 (base 15) | 54608 (base 50904) | 421.23 (base 421.17) | 5.46 (base 3.46) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 16612 (base 81788) | 440.43 (base 439.9) | 4.23 (base 4.23) |
| hub | 21.54 (base 21.55) | 16 (base 18) | 66364 (base 67484) | 453.02 (base 450.49) | 4.23 (base 4.23) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23536 (base 24656) | 466.33 (base 462.56) | 4.23 (base 5.46) |
| ponder | 87.25 (base 87.24) | 14 (base 14) | 52248 (base 52248) | 464.93 (base 462.87) | 4.23 (base 8.23) |
| map | 30.0 (base 30.0) | 14 (base 14) | 46396 (base 47072) | 490.1 (base 504.09) | 8.04 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| hud | 31.0 (base 31.0) | 16 (base 16) | 72684 (base 72908) | 477.99 (base 490.43) | 8.1 (base 8.16) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 472.65 (base 475.26) | 8.16 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.25 / 14 (29.9) | 107.49 / 154 (29.99) | median and average, x 1.10 + 2 |
| factory_flat | 22.23 / 22 (29.87) | 115.59 / 162 (29.98) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (14.26) | 153.0 / 153 (16.77) | median, x 1.10 + 2 |
| hub | 21.54 / 21 (29.8) | 115.48 / 161 (29.81) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.85) | 116.18 / 162 (29.84) | median, x 1.10 + 2 |
| ponder | 87.25 / 87 (30.0) | 180.51 / 227 (30.0) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (9.67) | 170.0 / 170 (11.94) | median and average, x 1.10 + 2 |
| hud | 31.0 / 31 (14.28) | 171.0 / 171 (16.51) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 99.92 / 147 (30.0) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.267 | 32.0 / 20.267 | 0.0 / 0.0 | 416.0 / 33.911 |
| ponder | 32.0 / 17.57 | 32.0 / 17.57 | 0.0 / 0.0 | 416.0 / 29.086 |
| map | 40.0 / 27.141 | 40.0 / 27.141 | 0.0 / 0.0 | 416.0 / 47.113 |
| hud | 56.0 / 16.223 | 56.0 / 16.223 | 0.0 / 0.0 | 416.0 / 32.685 |
| create_ct | 56.0 / 0.687 | 56.0 / 0.687 | 0.0 / 0.0 | 416.0 / 1.192 |


Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 25.033 / 0.29 | 1.453 / 28.0 | 2.447 | 1850 / 0 | 2.663 (3.603) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91616 | 3.508 / 0.002 | 1.453 / 12.0 | 2.446 | 288 / 0 | 1.509 (1.682) |
| sift | indirect, one multi-draw per arena and layer | 3.011 | 429.502 | 16 (vanilla 28) | 462632 | 22.949 / 0.322 | 7.389 / 16.0 | 12.354 | 2860 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 22.336 / 0.313 | 1.879 / 12.0 | 3.137 | 2931 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.146 | 16 (vanilla 28) | 1190184 | 23.197 / 0.343 | 20.267 / 32.0 | 31.781 | 3582 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1019372 | 23.197 / 0.343 | 17.57 / 32.0 | 27.22 | 3583 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.016 | 550.839 | 16 (vanilla 28) | 1701020 | 20.046 / 0.272 | 27.141 / 40.0 | 45.422 | 4386 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.011 | 443.551 | 16 (vanilla 28) | 1018764 | 16.134 / 0.234 | 16.223 / 56.0 | 27.204 | 6024 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 42676 | 15.997 / 0.232 | 0.687 / 56.0 | 1.14 | 6121 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1716 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 596 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 597 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 33.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 6402 / 0 / 0 |
| hud | 40.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 11440 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.026; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.006; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 243.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.333 / 0.0 | 0.488 / 0.585 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.318 / 10.355 | 0.021 / 0.008 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.592 / 0.559 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.367 / 11.142 | 0.02 / 0.005 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.96 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.645 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.85 | 0.0 / 0.0 | 571.76 / 570.87 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.85 (8.0 / 8.0) |
| mam | 13.86 / 13.86 | 1.0 / 1.0 | 109.93 / 110.33 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.86 (8.0 / 8.0) |
| ponder | 82.59 / 82.59 | 0.11 / 0.11 | 203.26 / 203.34 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.62 / 81.62 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

