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
| spawn | 0.0543 | 10.05 | 0.0635 | 0.0597 | 0.0632 |
| flythrough | 0.0 | 14.95 | 0.001 | 0.001 | 0.0011 |
| water_glass | 0.0037 | 6.45 | 0.0041 | 0.004 | 0.0064 |
| nether | 0.0 | 0.8 | 0.0003 | 0.0112 | 0.0003 |
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
| translucent_move | 0.0003 | 0.3 | 0.0003 | 0.0001 | 0.0001 |
| translucent_move.x2404 | 0.0002 | 0.3 | 0.0004 | 0.0007 | 0.0003 |
| translucent_move.x2414 | 0.0001 | 0.3 | 0.0002 | 0.0001 | 0.0003 |
| translucent_move.x2424 | 0.0 | 0.3 | 0.0001 | 0.0001 | 0.0003 |
| translucent_move.low | 0.0004 | 0.3 | 0.0014 | 0.0013 | 0.0013 |
| translucent_move.back | 0.0 | 0.3 | 0.0 | 0.0001 | 0.0001 |
| translucent_move.bare_back | 0.0 | 0.3 | 0.0001 | 0.0 | 0.0 |
| water_glass_oit | 0.0035 | 0.3 | 0.0039 | 0.0046 | 0.0038 |
| translucent_move_oit | 0.0002 | 0.3 | 0.0003 | 0.0 | 0.0004 |
| translucent_move_oit.x2404 | 0.0002 | 0.3 | 0.0004 | 0.0003 | 0.0002 |
| translucent_move_oit.x2414 | 0.0003 | 0.3 | 0.0003 | 0.0002 | 0.0003 |
| translucent_move_oit.x2424 | 0.0 | 0.3 | 0.0001 | 0.0005 | 0.0003 |
| translucent_move_oit.low | 0.0008 | 0.3 | 0.0016 | 0.0011 | 0.0012 |
| translucent_move_oit.back | 0.0001 | 0.3 | 0.0001 | 0.0001 | 0.0 |
| translucent_move_oit.bare_back | 0.0 | 0.3 | 0.0 | 0.0003 | 0.0 |
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
| spawn | 92.99 | 18057 | 295 (795) | 250 (759) | 283 (855) |
| flythrough | 97.24 | 6020 | 46 (290) | 46 (290) | 50 (290) |
| water_glass | 95.8 | 25508 | 20 (2622) | 14 (2616) | 33 (2745) |
| nether | 99.42 | 905 | 3 (458) | 72 (527) | 3 (470) |
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
| translucent_move | 96.93 | 13 | 2 (77) | 2 (77) | 2 (68) |
| translucent_move.x2404 | 95.93 | 15 | 4 (104) | 5 (105) | 3 (87) |
| translucent_move.x2414 | 95.86 | 13 | 3 (106) | 1 (104) | 3 (102) |
| translucent_move.x2424 | 96.74 | 13 | 3 (81) | 2 (79) | 2 (68) |
| translucent_move.low | 96.11 | 41 | 15 (21) | 14 (20) | 18 (20) |
| translucent_move.back | 96.93 | 13 | 1 (76) | 3 (78) | 2 (68) |
| translucent_move.bare_back | 96.9 | 13 | 4 (79) | 1 (77) | 1 (67) |
| water_glass_oit | 95.8 | 57 | 15 (2614) | 22 (2622) | 13 (2725) |
| translucent_move_oit | 96.93 | 12 | 2 (77) | 1 (76) | 4 (70) |
| translucent_move_oit.x2404 | 95.93 | 16 | 4 (104) | 5 (106) | 4 (88) |
| translucent_move_oit.x2414 | 95.86 | 10 | 2 (105) | 4 (107) | 2 (99) |
| translucent_move_oit.x2424 | 96.74 | 15 | 1 (78) | 2 (79) | 3 (69) |
| translucent_move_oit.low | 96.11 | 40 | 14 (19) | 19 (26) | 17 (19) |
| translucent_move_oit.back | 96.93 | 12 | 2 (77) | 1 (76) | 2 (69) |
| translucent_move_oit.bare_back | 96.9 | 15 | 2 (77) | 3 (78) | 2 (68) |
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
| spawn | 0.2035 / 279 | 0.4326 / 2343 | 0.4344 / 2376 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| water_glass | 0.0029 / 14 | 0.0048 / 23 | 0.0044 / 11 |
| nether | 0.0012 / 11 | 0.0038 / 0 | 0.0 / 0 |
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
| translucent_move | 0.0003 / 1 | 0.0001 / 1 | 0.0002 / 2 |
| translucent_move.x2404 | 0.0002 / 1 | 0.0003 / 2 | 0.0002 / 2 |
| translucent_move.x2414 | 0.0001 / 0 | 0.0002 / 3 | 0.0001 / 0 |
| translucent_move.x2424 | 0.0 / 1 | 0.0 / 2 | 0.0 / 2 |
| translucent_move.low | 0.0007 / 2 | 0.0003 / 5 | 0.0004 / 6 |
| translucent_move.back | 0.0 / 1 | 0.0 / 3 | 0.0 / 0 |
| translucent_move.bare_back | 0.0001 / 3 | 0.0 / 1 | 0.0 / 0 |
| water_glass_oit | 0.0037 / 16 | 0.004 / 19 | 0.0056 / 29 |
| translucent_move_oit | 0.0003 / 1 | 0.0 / 1 | 0.0002 / 2 |
| translucent_move_oit.x2404 | 0.0001 / 1 | 0.0 / 3 | 0.0002 / 2 |
| translucent_move_oit.x2414 | 0.0002 / 2 | 0.0001 / 2 | 0.0004 / 3 |
| translucent_move_oit.x2424 | 0.0001 / 1 | 0.0003 / 0 | 0.0 / 0 |
| translucent_move_oit.low | 0.0005 / 1 | 0.0002 / 3 | 0.0004 / 5 |
| translucent_move_oit.back | 0.0 / 1 | 0.0001 / 1 | 0.0 / 0 |
| translucent_move_oit.bare_back | 0.0 / 0 | 0.0004 / 3 | 0.0001 / 2 |
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
| spawn | 13.0 (base 13.6) | 14 (base 14) | 21004 (base 22476) | 256.1 (base 259.07) | 3.99 (base 3.99) |
| flythrough | 13.06 (reported, not gated) | 33 (reported, not gated) | 359152 (reported, not gated) | 314.93 (base 324.25) | 3.73 (base 4.24) |
| water_glass | 8.96 (base 8.97) | 15 (base 15) | 15092 (base 15576) | 244.38 (base 250.02) | 3.99 (base 5.99) |
| nether | 15.31 (base 15.63) | 12 (base 12) | 18160 (base 19648) | 265.66 (base 273.53) | 3.99 (base 5.99) |
| mobs | 19.77 (base 19.77) | 16 (base 17) | 616224 (base 1010948) | 264.05 (base 268.37) | 5.99 (base 6.99) |
| outline | 10.73 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 263.82 (base 264.69) | 5.99 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100352 (base 126320) | 269.62 (base 265.1) | 5.99 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82224 (reported, not gated) | 270.27 (base 265.74) | 5.99 (base 4.24) |
| translucent_move | 8.94 (reported, not gated) | 15 (reported, not gated) | 10236 (reported, not gated) | 271.76 (base 267.48) | 5.99 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 14980 (base 14688) | 244.04 (base 245.02) | 3.35 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10104 (reported, not gated) | 246.73 (base 247.19) | 3.48 (base 3.73) |
| flat_plants | 18.05 (base 18.29) | 15 (base 15) | 9680 (base 9812) | 246.99 (base 246.92) | 3.34 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7272 (base 7164) | 239.53 (base 241.33) | 3.34 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 267.05 (base 261.44) | 5.99 (base 5.99) |
| animation | 49 median (avg 39.94) (base 49) | 15 (base 15) | 2204 (base 2056) | 264.7 (base 260.68) | 5.99 (base 5.99) |
| animated_items | 60 median (avg 46.73) (base 60) | 23 (base 23) | 21976 (base 21940) | 239.63 (base 239.81) | 5.99 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (12.13) | 123.42 / 123 (9.7) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (7.96) | 123.05 / 123 (5.53) | median and average, at most vanilla + 2 |
| water_glass | 8.96 / 9 (20.7) | 124.0 / 124 (16.25) | median, at most vanilla |
| nether | 15.31 / 16 (20.7) | 120.9 / 121 (16.0) | median, x 1.10 + 2 |
| mobs | 19.77 / 20 (26.04) | 131.14 / 134 (20.26) | median, x 1.10 + 2 |
| outline | 10.73 / 11 (30.0) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.33 / 133 (29.96) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.96) | 94.27 / 133 (29.98) | median and average, x 1.10 + 2 |
| translucent_move | 8.94 / 9 (20.93) | 124.0 / 124 (16.78) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (12.13) | 129.0 / 129 (9.27) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (11.17) | 129.0 / 129 (8.14) | median and average, at most vanilla |
| flat_plants | 18.05 / 19 (21.31) | 123.71 / 124 (16.5) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.88) | 94.38 / 123 (26.57) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.01) | 155.22 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.672 | 36.0 / 21.672 | 0.0 / 0.0 | 416.0 / 36.993 |
| flythrough | 112.0 / 85.462 | 112.0 / 85.462 | 0.0 / 0.0 | 416.0 / 145.232 |
| water_glass | 36.0 / 12.604 | 36.0 / 12.604 | 0.0 / 0.0 | 416.0 / 21.92 |
| nether | 20.0 / 15.251 | 20.0 / 15.251 | 0.0 / 0.0 | 256.0 / 26.754 |
| mobs | 16.0 / 9.775 | 16.0 / 9.775 | 0.0 / 0.0 | 256.0 / 17.08 |
| outline | 16.0 / 1.338 | 16.0 / 1.338 | 0.0 / 0.0 | 256.0 / 2.35 |
| occlusion | 20.0 / 2.58 | 20.0 / 2.58 | 0.0 / 0.0 | 416.0 / 4.443 |
| occlusion_move | 20.0 / 3.322 | 20.0 / 3.322 | 0.0 / 0.0 | 416.0 / 5.951 |
| translucent_move | 28.0 / 12.923 | 28.0 / 12.923 | 0.0 / 0.0 | 416.0 / 22.516 |
| water_glass_oit | 20.0 / 12.604 | 20.0 / 12.604 | 0.0 / 0.0 | 416.0 / 21.92 |
| translucent_move_oit | 28.0 / 12.566 | 28.0 / 12.566 | 0.0 / 0.0 | 416.0 / 21.803 |
| flat_plants | 24.0 / 13.846 | 24.0 / 13.846 | 0.0 / 0.0 | 416.0 / 24.541 |
| renderer_off | 24.0 / 8.59 | 24.0 / 8.59 | 0.0 / 0.0 | 416.0 / 15.095 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.012 | 733.017 | 16 (vanilla 28) | 1307940 | 40.34 / 0.108 | 21.672 / 36.0 | 34.926 | 2209 / 0 | 3.667 (6.242) |
| flythrough | indirect, one multi-draw per arena and layer | 3.019 | 911.792 | 16 (vanilla 28) | 5175780 | 15.223 / 1.012 | 85.462 / 112.0 | 138.208 | 6532 / 0 | 5.277 (14.228) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 272.659 | 16 (vanilla 28) | 779680 | 32.249 / 0.092 | 12.604 / 36.0 | 20.82 | 2854 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 471.135 | 16 (vanilla 28) | 957636 | 26.971 / 0.209 | 15.251 / 20.0 | 25.572 | 3902 / 0 | 2.423 (3.053) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 199.383 | 16 (vanilla 28) | 608008 | 24.436 / 0.181 | 9.775 / 16.0 | 16.236 | 4348 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 83944 | 24.004 / 0.18 | 1.338 / 16.0 | 2.242 | 4442 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 71.119 | 16 (vanilla 28) | 161724 | 22.864 / 0.171 | 2.58 / 20.0 | 4.318 | 4685 / 0 | 3.607 (6.43) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 76.483 | 16 (vanilla 28) | 208888 | 21.595 / 0.161 | 3.322 / 20.0 | 5.578 | 4981 / 0 | 5.122 (12.175) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.007 | 248.316 | 16 (vanilla 28) | 798740 | 18.696 / 0.145 | 12.923 / 28.0 | 21.329 | 5784 / 0 | 6.595 (16.62) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.021 | 281.157 | 16 (vanilla 28) | 779680 | 7.498 / 0.043 | 12.604 / 20.0 | 20.82 | 1091 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.022 | 263.484 | 16 (vanilla 28) | 776176 | 6.753 / 0.111 | 12.566 / 28.0 | 20.726 | 2436 / 0 | 6.01 (13.648) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.007 | 283.664 | 16 (vanilla 28) | 863900 | 6.261 / 0.01 | 13.846 / 24.0 | 23.069 | 950 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 205.343 | 16 (vanilla 28) | 539320 | 2.397 / 0.021 | 8.59 / 24.0 | 14.401 | 602 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 18.635 / 0.144 | 0.131 / 28.0 | 0.225 | 5830 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 18.475 / 0.143 | 0.302 / 28.0 | 0.518 | 5932 / 0 | 1.239 (1.592) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 18.419 / 0.143 | 0.153 / 28.0 | 0.261 | 5982 / 0 | 1.099 (1.683) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 150.0 | 1.0 | 0.0 | 0 | 0 | 49 (0.021 / 0.037 / 0.041; 0) | 0 | 36450 / 0 / 0 |
| flythrough | 191.175 | 1.0 | 0.0 | 0 | 0 | 889 (0.016 / 0.038 / 2.35; 0) | 57 | 30588 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1656 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.022 / 0.054 / 0.057; 0) | 32 | 1202 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.165 / 0.161 / 3.125; 0) | 180 | 2933 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 729 | 0 (None / None / None; 0) | 0 | 972 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 672 | 0 (None / None / None; 0) | 0 | 1568 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1708 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2396 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 314.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.092; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 434.65 | 1.0 (0.0, 0.0) | 1.163 (0.0, 0.0) | 0.001 (0.046; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.016 (0.043; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.129 (0.423; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.06 (0.089; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.364 | 10.0 (9.0, 2.351) | 6.0 (6.0, 2.275) | 0.059 (1.206; 0) | 0 | 2780 / 1503998 / 0 / 0 |
| translucent_move | 135.041 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.015; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.012; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.679 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 151.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.007 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.42 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 84.663 / 0.0 | 0.023 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.719 / 0.0 | 0.004 / 0.014 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.85 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 54.51 / 54.519 | 0.01 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 9.638 | 0.903 / 0.713 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 36.005 / 4.658 | 0.058 / 0.008 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 112.16 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 14.028 / 13.9 | 0.004 / 0.013 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.204 / 53.152 | 0.013 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.187 / 13.076 | 0.006 / 0.009 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 9.319 | 0.708 / 0.798 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.621 / 1.499 | 0.013 / 0.008 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 86.629 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.05 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.27 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.004 / 0.011 | 56 (3) | 40.58 / 9.24 / 31.34 | 3 / 0 | 10.985 |
| animated_items | 136.553 / 28.063 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.58 / 7.98 / 32.6 | 0 / 0 | 9.0 |

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
| spawn | 49.0 (base 49.0) | 12 (base 13) | 1380 (base 6372) | 259.18 (base 256.29) | 4.24 (base 3.73) |
| flythrough | 66.29 (reported, not gated) | 51 (reported, not gated) | 680124 (reported, not gated) | 313.2 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 20.0 (base 20.0) | 13 (base 14) | 7428 (base 10264) | 244.52 (base 249.83) | 4.24 (base 5.99) |
| nether | 33.91 (base 33.79) | 10 (base 11) | 4176 (base 10656) | 265.82 (base 272.57) | 4.24 (base 5.99) |
| mobs | 28.98 (base 28.97) | 14 (base 16) | 610516 (base 1015892) | 264.09 (base 268.23) | 4.24 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 264.17 (base 264.41) | 4.24 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 269.76 (base 264.64) | 4.24 (base 4.24) |
| occlusion_move | 24.04 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 270.27 (base 265.66) | 4.24 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 3284 (reported, not gated) | 272.09 (base 268.19) | 4.24 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 7652 (base 7604) | 244.18 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2612 (reported, not gated) | 246.68 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 32.68 (base 33.0) | 13 (base 13) | 1604 (base 1716) | 248.15 (base 248.11) | 3.09 (base 5.99) |
| renderer_off | 17.75 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 239.63 (base 240.73) | 3.35 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 267.33 (base 261.4) | 4.24 (base 4.24) |
| animation | 49 median (avg 39.98) (base 49) | 13 (base 13) | 1716 (base 1716) | 264.85 (base 260.68) | 4.24 (base 4.24) |
| animated_items | 60 median (avg 46.69) (base 60) | 22 (base 22) | 21668 (base 21668) | 239.88 (base 239.53) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.0 / 49 (10.43) | 906.17 / 906 (8.37) | median and average, x 1.10 + 2 |
| flythrough | 66.29 / 64 (5.97) | 961.49 / 957 (4.65) | median and average, at most vanilla |
| water_glass | 20.0 / 20 (17.64) | 392.0 / 392 (13.89) | median, at most vanilla |
| nether | 33.91 / 34 (16.25) | 588.92 / 589 (13.83) | median, x 1.10 + 2 |
| mobs | 28.98 / 29 (20.29) | 331.0 / 331 (17.83) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.01) | 127.4 / 166 (29.95) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (29.99) | 161.66 / 200 (29.83) | median and average, x 1.10 + 2 |
| occlusion_move | 24.04 / 24 (29.76) | 167.8 / 201 (29.56) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (16.68) | 368.82 / 368 (14.45) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (8.1) | 403.0 / 403 (7.48) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (8.89) | 385.32 / 387 (7.25) | median and average, at most vanilla |
| flat_plants | 32.68 / 33 (17.88) | 398.98 / 399 (14.8) | median, x 1.10 + 2 |
| renderer_off | 17.75 / 18 (26.73) | 317.16 / 325 (21.46) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.27 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.98 / 49 (30.0) | 156.22 / 191 (29.99) | median, x 1.10 + 2 |
| animated_items | 46.69 / 60 (30.0) | 167.42 / 202 (29.97) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.776 | 36.0 / 22.776 | 0.0 / 0.0 | 416.0 / 39.06 |
| flythrough | 112.0 / 84.828 | 112.0 / 84.828 | 0.0 / 0.0 | 416.0 / 143.913 |
| water_glass | 36.0 / 12.653 | 36.0 / 12.653 | 0.0 / 0.0 | 416.0 / 21.92 |
| nether | 20.0 / 15.247 | 20.0 / 15.247 | 0.0 / 0.0 | 256.0 / 26.757 |
| mobs | 16.0 / 9.775 | 16.0 / 9.775 | 0.0 / 0.0 | 256.0 / 17.08 |
| outline | 16.0 / 1.338 | 16.0 / 1.338 | 0.0 / 0.0 | 256.0 / 2.35 |
| occlusion | 20.0 / 2.593 | 20.0 / 2.593 | 0.0 / 0.0 | 416.0 / 4.266 |
| occlusion_move | 20.0 / 3.529 | 20.0 / 3.529 | 0.0 / 0.0 | 416.0 / 5.719 |
| translucent_move | 28.0 / 13.162 | 28.0 / 13.162 | 0.0 / 0.0 | 416.0 / 22.409 |
| water_glass_oit | 20.0 / 12.653 | 20.0 / 12.653 | 0.0 / 0.0 | 416.0 / 21.92 |
| translucent_move_oit | 28.0 / 12.805 | 28.0 / 12.805 | 0.0 / 0.0 | 416.0 / 21.789 |
| flat_plants | 24.0 / 14.099 | 24.0 / 14.099 | 0.0 / 0.0 | 416.0 / 24.154 |
| renderer_off | 24.0 / 8.626 | 24.0 / 8.626 | 0.0 / 0.0 | 416.0 / 15.095 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.188 | 784.202 | 16 (vanilla 28) | 1377332 | 40.799 / 0.126 | 22.776 / 36.0 | 36.779 | 2461 / 0 | 5.768 (9.317) |
| flythrough | plain, one multi-draw per region and layer | 56.655 | 893.58 | 16 (vanilla 28) | 5143224 | 15.256 / 0.987 | 84.828 / 112.0 | 137.339 | 6372 / 0 | 7.71 (20.415) |
| water_glass | plain, one multi-draw per region and layer | 14.04 | 274.778 | 16 (vanilla 28) | 782696 | 33.524 / 0.107 | 12.653 / 36.0 | 20.9 | 3129 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.062 | 471.451 | 16 (vanilla 28) | 957588 | 28.176 / 0.201 | 15.247 / 20.0 | 25.57 | 4174 / 0 | 3.204 (4.482) |
| mobs | plain, one multi-draw per region and layer | 11.027 | 199.491 | 16 (vanilla 28) | 608008 | 25.627 / 0.176 | 9.775 / 16.0 | 16.236 | 4610 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 83944 | 25.163 / 0.175 | 1.338 / 16.0 | 2.242 | 4708 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.119 | 16 (vanilla 28) | 162228 | 23.922 / 0.166 | 2.593 / 20.0 | 4.332 | 4970 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.99 | 76.763 | 16 (vanilla 28) | 220272 | 22.562 / 0.156 | 3.529 / 20.0 | 5.882 | 5271 / 0 | 6.007 (13.449) |
| translucent_move | plain, one multi-draw per region and layer | 12.036 | 249.387 | 16 (vanilla 28) | 811532 | 19.541 / 0.14 | 13.162 / 28.0 | 21.67 | 6076 / 0 | 7.319 (16.798) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.099 | 283.752 | 16 (vanilla 28) | 782696 | 7.447 / 0.041 | 12.653 / 20.0 | 20.9 | 1087 / 0 | None (None) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.621 | 264.565 | 16 (vanilla 28) | 788996 | 6.68 / 0.109 | 12.805 / 28.0 | 21.068 | 2424 / 0 | 8.738 (16.975) |
| flat_plants | plain, one multi-draw per region and layer | 17.048 | 282.79 | 16 (vanilla 28) | 878572 | 6.078 / 0.01 | 14.099 / 24.0 | 23.46 | 933 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.024 | 205.384 | 16 (vanilla 28) | 541640 | 2.373 / 0.021 | 8.626 / 24.0 | 14.463 | 600 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 19.491 / 0.139 | 0.131 / 28.0 | 0.225 | 6120 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.33 / 0.138 | 0.302 / 28.0 | 0.518 | 6222 / 0 | 1.995 (3.527) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.274 / 0.138 | 0.153 / 28.0 | 0.261 | 6271 / 0 | 1.125 (1.794) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 151.182 | 17.0 | 0.0 | 0 | 0 | 69 (0.026 / 0.042 / 0.045; 0) | 0 | 31597 / 0 / 0 |
| flythrough | 186.558 | 25.567 | 0.0 | 0 | 0 | 847 (0.025 / 0.049 / 5.372; 0) | 40 | 22387 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1412 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.009 / 0.014 / 0.023; 0) | 32 | 1194 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.15 / 0.23 / 2.919; 0) | 180 | 2338 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 486 | 0 (None / None / None; 0) | 0 | 648 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 534 | 0 (None / None / None; 0) | 0 | 1246 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1432 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2140 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 341.201 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.033 (1.103; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 431.667 | 1.0 (0.0, 0.0) | 1.242 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.042; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.135 (0.302; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.068 (0.214; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.683 | 10.0 (9.0, 2.357) | 6.0 (6.0, 2.281) | 0.06 (0.25; 0) | 0 | 2769 / 1497908 / 0 / 0 |
| translucent_move | 136.021 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.792 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 150.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.12 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 85.781 / 0.0 | 0.022 / 0.008 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.12 / 0.0 | 0.003 / 0.016 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.742 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.796 / 53.785 | 0.01 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.924 / 0.907 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 44.883 / 8.766 | 0.068 / 0.011 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.731 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.988 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 77.834 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 15.383 / 15.347 | 0.005 / 0.016 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 55.633 / 55.623 | 0.013 / 0.024 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.793 / 10.73 | 0.003 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.983 / 0.678 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.903 / 1.768 | 0.011 / 0.008 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 107.226 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.009 / 0.056 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.311 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.003 / 0.013 | 56 (3) | 40.608 / 9.248 / 31.36 | 3 / 0 | 10.987 |
| animated_items | 136.757 / 28.028 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.51 / 7.97 / 32.54 | 0 / 0 | 9.0 |

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
| spawn | 13.05 (base 14.05) | 14 (base 15) | 21004 (base 27764) | 257.33 (base 257.2) | 3.98 (base 3.98) |
| flythrough | 13.04 (reported, not gated) | 43 (reported, not gated) | 757988 (reported, not gated) | 312.64 (base 318.06) | 3.71 (base 4.23) |
| water_glass | 9.0 (base 9.0) | 15 (base 15) | 15596 (base 15108) | 244.85 (base 244.39) | 3.98 (base 4.23) |
| nether | 15.82 (base 15.71) | 12 (base 12) | 18376 (base 19912) | 265.87 (base 268.95) | 3.98 (base 4.23) |
| mobs | 20.0 (base 19.97) | 16 (base 17) | 616224 (base 1008176) | 264.42 (base 264.31) | 3.98 (base 6.23) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 264.08 (base 264.58) | 3.98 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100332 (base 126356) | 269.8 (base 264.78) | 3.98 (base 3.98) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82204 (reported, not gated) | 270.3 (base 265.16) | 3.98 (base 4.23) |
| translucent_move | 9.0 (reported, not gated) | 15 (reported, not gated) | 10120 (reported, not gated) | 272.12 (base 268.35) | 3.98 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 14756 (base 15024) | 244.64 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10064 (reported, not gated) | 247.18 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 18.92 (base 18.96) | 15 (base 15) | 9548 (base 9864) | 247.05 (base 248.07) | 3.71 (base 5.98) |
| renderer_off | 7.84 (base 7.83) | 14 (base 14) | 7272 (base 7180) | 240.23 (base 241.13) | 3.34 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 267.53 (base 261.4) | 3.98 (base 5.98) |
| animation | 49 median (avg 39.94) (base 49) | 15 (base 15) | 2204 (base 2056) | 264.79 (base 260.68) | 5.98 (base 5.98) |
| animated_items | 60 median (avg 46.77) (base 60) | 24 (base 23) | 22136 (base 21940) | 239.99 (base 239.74) | 5.98 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.05 / 13 (9.29) | 123.0 / 123 (9.46) | median and average, x 1.10 + 2 |
| flythrough | 13.04 / 13 (5.48) | 123.06 / 123 (5.54) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (14.95) | 124.0 / 124 (15.19) | median, at most vanilla |
| nether | 15.82 / 16 (15.44) | 120.61 / 121 (15.44) | median, x 1.10 + 2 |
| mobs | 20.0 / 20 (18.22) | 134.0 / 134 (19.05) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (29.95) | 94.27 / 133 (29.99) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.46) | 94.33 / 133 (29.96) | median and average, x 1.10 + 2 |
| translucent_move | 9.0 / 9 (15.27) | 124.0 / 124 (16.19) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (8.11) | 129.0 / 129 (8.55) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (7.46) | 129.0 / 129 (8.06) | median and average, at most vanilla |
| flat_plants | 18.92 / 19 (16.05) | 123.93 / 124 (15.98) | median, x 1.10 + 2 |
| renderer_off | 7.84 / 8 (23.87) | 107.18 / 123 (23.14) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.94 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.0) | 155.09 / 190 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 21.665 | 36.0 / 21.665 | 0.0 / 0.0 | 416.0 / 39.041 |
| flythrough | 112.0 / 83.92 | 112.0 / 83.92 | 0.0 / 0.0 | 416.0 / 144.471 |
| water_glass | 36.0 / 12.653 | 36.0 / 12.653 | 0.0 / 0.0 | 416.0 / 21.92 |
| nether | 20.0 / 15.248 | 20.0 / 15.248 | 0.0 / 0.0 | 256.0 / 26.751 |
| mobs | 16.0 / 9.775 | 16.0 / 9.775 | 0.0 / 0.0 | 256.0 / 17.08 |
| outline | 16.0 / 1.338 | 16.0 / 1.338 | 0.0 / 0.0 | 256.0 / 2.35 |
| occlusion | 20.0 / 2.599 | 20.0 / 2.599 | 0.0 / 0.0 | 416.0 / 4.373 |
| occlusion_move | 20.0 / 3.451 | 20.0 / 3.451 | 0.0 / 0.0 | 416.0 / 5.734 |
| translucent_move | 28.0 / 13.232 | 28.0 / 13.232 | 0.0 / 0.0 | 416.0 / 22.317 |
| water_glass_oit | 20.0 / 12.604 | 20.0 / 12.604 | 0.0 / 0.0 | 416.0 / 21.92 |
| translucent_move_oit | 28.0 / 12.876 | 28.0 / 12.876 | 0.0 / 0.0 | 416.0 / 21.698 |
| flat_plants | 24.0 / 14.088 | 24.0 / 14.088 | 0.0 / 0.0 | 416.0 / 23.946 |
| renderer_off | 24.0 / 8.59 | 24.0 / 8.59 | 0.0 / 0.0 | 416.0 / 15.095 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Terrain module, core vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.016 | 733.946 | 16 (vanilla 28) | 1308056 | 41.242 / 0.136 | 21.665 / 36.0 | 34.929 | 2272 / 0 | 3.458 (6.529) |
| flythrough | indirect, one multi-draw per arena and layer | 3.028 | 880.982 | 16 (vanilla 28) | 5084856 | 15.33 / 0.949 | 83.92 / 112.0 | 135.78 | 6191 / 0 | 7.387 (18.951) |
| water_glass | indirect, one multi-draw per arena and layer | 3.01 | 274.919 | 16 (vanilla 28) | 782696 | 33.571 / 0.111 | 12.653 / 36.0 | 20.9 | 2934 / 0 | None (None) |
| nether | indirect, one multi-draw per arena and layer | 2.006 | 471.526 | 16 (vanilla 28) | 957572 | 28.069 / 0.209 | 15.248 / 20.0 | 25.57 | 3981 / 0 | 3.127 (4.793) |
| mobs | indirect, one multi-draw per arena and layer | 2.005 | 199.547 | 16 (vanilla 28) | 608008 | 25.465 / 0.182 | 9.775 / 16.0 | 16.236 | 4424 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 83944 | 24.992 / 0.181 | 1.338 / 16.0 | 2.242 | 4522 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 70.117 | 16 (vanilla 28) | 162620 | 23.871 / 0.172 | 2.599 / 20.0 | 4.342 | 4759 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 75.376 | 16 (vanilla 28) | 216592 | 22.483 / 0.162 | 3.451 / 20.0 | 5.784 | 5042 / 0 | 4.621 (9.758) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.01 | 248.561 | 16 (vanilla 28) | 817476 | 19.454 / 0.145 | 13.232 / 28.0 | 21.829 | 5815 / 0 | 5.282 (13.127) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.031 | 281.728 | 16 (vanilla 28) | 779680 | 7.466 / 0.041 | 12.604 / 20.0 | 20.82 | 1083 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.034 | 263.718 | 16 (vanilla 28) | 794896 | 6.431 / 0.086 | 12.876 / 28.0 | 21.226 | 2398 / 0 | 5.176 (10.376) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.009 | 282.881 | 16 (vanilla 28) | 879120 | 5.784 / 0.01 | 14.088 / 24.0 | 23.475 | 916 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.006 | 205.43 | 16 (vanilla 28) | 539320 | 2.387 / 0.021 | 8.59 / 24.0 | 14.401 | 598 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.397 / 0.144 | 0.131 / 28.0 | 0.225 | 5859 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.232 / 0.143 | 0.302 / 28.0 | 0.518 | 5961 / 0 | 1.746 (1.85) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.175 / 0.143 | 0.153 / 28.0 | 0.261 | 6011 / 0 | 1.28 (1.963) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 150.0 | 1.0 | 0.0 | 0 | 0 | 37 (0.033 / 0.052 / 0.052; 0) | 0 | 27900 / 0 / 0 |
| flythrough | 183.455 | 1.0 | 0.0 | 0 | 0 | 793 (0.03 / 0.051 / 9.081; 0) | 37 | 20180 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1196 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.009 / 0.016 / 0.017; 0) | 32 | 1182 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.133 / 0.195 / 1.064; 0) | 180 | 2142 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 489 | 0 (None / None / None; 0) | 0 | 652 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 450 | 0 (None / None / None; 0) | 0 | 1050 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1284 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1912 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 314.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.031 (0.09; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 428.718 | 1.0 (0.0, 0.0) | 1.627 (0.0, 0.0) | 0.002 (0.053; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 140.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.017 (0.052; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 108.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.126 (0.218; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.066 (0.14; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.34 | 10.0 (9.0, 2.354) | 6.0 (6.0, 2.277) | 0.062 (0.347; 0) | 0 | 2737 / 1481474 / 0 / 0 |
| translucent_move | 136.092 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 139.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.84 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.019; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 151.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.036; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.009; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.009 (0.04; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.0 / 0.049 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 84.023 / 0.048 | 0.017 / 0.011 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.005 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.727 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 58.53 / 58.518 | 0.008 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.607 / 0.818 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 44.165 / 7.469 | 0.067 / 0.009 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.723 / 12.686 | 0.004 / 0.011 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 50.679 / 50.748 | 0.009 / 0.019 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.382 / 12.28 | 0.005 / 0.013 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.928 / 0.916 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.733 / 1.645 | 0.012 / 0.009 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 99.318 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.005 / 0.053 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.277 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.005 / 0.011 | 56 (3) | 40.51 / 9.24 / 31.27 | 3 / 0 | 10.987 |
| animated_items | 136.426 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

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
  - probe: `gpuVendor="Mesa" renderer="llvmpipe (LLVM 20.1.2, 256 bits)" driver="Mesa 25.2.8-0ubuntu0.24.04.4" gl=4.5 backend=OpenGL os="Linux 7.0.0-1017-azure amd64" windowSystem=X11 cpuCores=4 GL_ARB_multi_draw_indirect=yes GL_ARB_buffer_storage=yes deviceType=CPU vanilla.multiDrawIndirect=yes vanilla.persistentMapping=yes vanilla.mdiKnownIssues=no`
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
| factory | 0.0003 | 0.3 | 0.0153 | 0.0 | 0.0143 |
| factory_flat | 0.0041 | 0.3 | 0.0016 | 0.0046 | 0.0043 |
| sift | 0.0 | 1.15 | 0.0068 | 0.0068 | 0.0067 |
| hub | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| mam | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| ponder | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| map | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| hologram | 0.0022 | 0.55 | 0.0052 | 0.024 | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |
| create_ct | 0.0 | 0.25 | 0.0 | 0.0 | 0.0 |

Strict picture: pixels where all four vanilla runs agree exactly but the Brimstone run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | 95.59 | 500 | 0 (16) | 0 (15) | 0 (15) |
| factory_flat | 95.28 | 40 | 1 (15) | 2 (16) | 0 (14) |
| sift | 93.9 | 100 | 55 (536) | 55 (536) | 61 (519) |
| hub | 99.84 | 10 | 0 (0) | 0 (0) | 0 (0) |
| mam | 99.76 | 10 | 0 (0) | 0 (98141) | 0 (0) |
| ponder | 99.69 | 10 | 0 (0) | 0 (0) | 0 (0) |
| map | 99.99 | 10 | 0 (0) | 0 (0) | 0 (0) |
| hologram | 99.97 | 413 | 46 (4298) | 35 (4287) | None (None) |
| hud | 44.33 | 10 | 0 (0) | 0 (0) | 0 (0) |
| create_ct | 98.36 | 10 | 0 (0) | 0 (0) | 0 (0) |

Measure runs (Brimstone loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Brimstone's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | 0.015 / 0 | 0.0 / 0 | 0.0143 / 0 |
| factory_flat | 0.0017 / 3 | 0.0059 / 15 | 0.0002 / 1 |
| sift | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hub | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| mam | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| ponder | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| map | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| hologram | 0.0014 / 7 | 0.0217 / 10 | skipped |
| hud | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |
| create_ct | 0.0 / 0 | 0.0 / 0 | 0.0 / 0 |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 12.21 (base 12.23) | 15 (base 14) | 6956 (base 6488) | 421.86 (base 421.74) | 4.24 (base 5.48) |
| factory_flat | 20.25 (base 20.25) | 15 (base 14) | 10052 (base 6264) | 422.58 (base 422.37) | 5.54 (base 3.99) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 16632 (base 81788) | 439.92 (base 440.07) | 4.24 (base 5.87) |
| hub | 21.53 (base 21.58) | 16 (base 18) | 66364 (base 67484) | 452.56 (base 451.33) | 4.24 (base 4.24) |
| mam | 22.53 (base 22.52) | 19 (base 21) | 23536 (base 24656) | 466.1 (base 471.8) | 8.24 (base 8.12) |
| ponder | 87.24 (base 87.25) | 14 (base 14) | 52248 (base 52248) | 464.48 (base 463.81) | 8.24 (base 8.24) |
| map | 30.0 (base 30.0) | 14 (base 14) | 46436 (base 47032) | 492.28 (base 504.13) | 8.06 (base 8.24) |
| hologram | 33 median (avg 32.7) (base 33) | 22 (base 24) | 72192 (base 70368) | 470.89 (base 453.57) | 8.12 (base 8.24) |
| hud | 31.0 (base 31.0) | 16 (base 16) | 71616 (base 71636) | 490.32 (base 494.92) | 8.12 (base 8.24) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 480.1 (base 470.81) | 8.12 (base 10.24) |

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 12.21 / 12 (29.98) | 109.24 / 152 (28.86) | median and average, x 1.10 + 2 |
| factory_flat | 20.25 / 20 (29.9) | 115.9 / 160 (29.19) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (17.17) | 153.0 / 153 (15.03) | median, x 1.10 + 2 |
| hub | 21.53 / 21 (29.9) | 116.4 / 161 (29.29) | median and average, x 1.10 + 2 |
| mam | 22.53 / 22 (29.92) | 117.2 / 162 (29.48) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (30.02) | 180.74 / 227 (30.0) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (11.16) | 170.0 / 170 (9.96) | median and average, x 1.10 + 2 |
| hologram | 32.7 / 33 (28.23) | 162.98 / 173 (21.15) | median, at most vanilla |
| hud | 31.0 / 31 (18.11) | 171.0 / 171 (13.87) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (29.98) | 100.08 / 147 (29.93) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec full (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.919 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.342 | 32.0 / 20.342 | 0.0 / 0.0 | 416.0 / 33.73 |
| ponder | 32.0 / 17.591 | 32.0 / 17.591 | 0.0 / 0.0 | 416.0 / 28.899 |
| map | 40.0 / 30.364 | 40.0 / 30.364 | 0.0 / 0.0 | 416.0 / 52.79 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.393 | 40.0 / 17.393 | 0.0 / 0.0 | 416.0 / 31.235 |
| create_ct | 40.0 / 0.171 | 40.0 / 0.171 | 0.0 / 0.0 | 416.0 / 0.946 |


Terrain module, siftec full (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.123 | 16 (vanilla 28) | 91632 | 23.636 / 0.264 | 1.453 / 28.0 | 2.447 | 1896 / 0 | 2.687 (3.448) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 3.574 / 0.001 | 1.453 / 12.0 | 2.447 | 298 / 0 | 1.796 (4.324) |
| sift | indirect, one multi-draw per arena and layer | 3.009 | 430.251 | 16 (vanilla 28) | 462608 | 21.867 / 0.301 | 7.389 / 16.0 | 12.353 | 2905 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 21.223 / 0.291 | 1.879 / 12.0 | 3.137 | 2989 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.145 | 16 (vanilla 28) | 1195036 | 22.272 / 0.326 | 20.342 / 32.0 | 31.911 | 3642 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1020924 | 22.272 / 0.326 | 17.591 / 32.0 | 27.262 | 3643 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.013 | 552.466 | 16 (vanilla 28) | 1905884 | 19.059 / 0.255 | 30.364 / 40.0 | 50.893 | 4562 / 0 | None (None) |
| hologram | indirect, one multi-draw per arena and layer | 2.004 | 13.023 | 16 (vanilla 28) | 13744 | 18.981 / 0.252 | 0.238 / 40.0 | 0.367 | 4624 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.008 | 404.113 | 16 (vanilla 28) | 1091464 | 15.736 / 0.215 | 17.393 / 40.0 | 29.145 | 6459 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 10608 | 15.598 / 0.213 | 0.171 / 40.0 | 0.283 | 6563 / 0 | None (None) |

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2064 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 33.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 7392 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2260 / 0 / 0 |
| hud | 36.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 13068 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 601 / 0 / 0 |

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.003 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 34.0 (0.0, 0.0) | 0.002 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.014; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.008; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.028; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.009; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.011; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 96.979 / 0.0 | 0.564 / 0.542 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.666 / 10.854 | 0.014 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 95.575 / 0.0 | 0.642 / 0.583 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.938 / 11.754 | 0.016 / 0.004 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 94.846 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.678 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 130.047 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 146.303 / 89.927 | 0.113 / 0.037 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.411 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.88 | 0.0 / 0.0 | 571.36 / 571.64 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.88 (8.0 / 8.0) |
| mam | 13.86 / 13.85 | 1.0 / 1.0 | 110.05 / 109.49 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.85 (8.0 / 8.0) |
| ponder | 82.58 / 82.58 | 0.11 / 0.11 | 203.09 / 203.04 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.61 / 81.61 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 20.18 (base 20.23) | 13 (base 13) | 49032 (base 48604) | 421.51 (base 421.54) | 3.73 (base 5.48) |
| factory_flat | 28.33 (base 28.32) | 13 (base 13) | 52116 (base 48340) | 422.29 (base 422.36) | 3.54 (base 5.48) |
| sift | 41.0 (base 41.0) | 16 (base 18) | 4148 (base 81788) | 439.49 (base 441.24) | 3.73 (base 4.24) |
| hub | 25.54 (base 25.54) | 14 (base 17) | 64756 (base 66484) | 452.06 (base 451.46) | 3.73 (base 5.99) |
| mam | 24.53 (base 24.53) | 17 (base 20) | 20916 (base 22916) | 464.86 (base 472.87) | 3.73 (base 5.99) |
| ponder | 87.24 (base 87.25) | 12 (base 12) | 52212 (base 52212) | 463.6 (base 463.76) | 3.73 (base 8.24) |
| map | 66.0 (base 67.0) | 12 (base 12) | 30924 (base 31404) | 491.45 (base 506.16) | 8.06 (base 6.31) |
| hologram | 34 median (avg 33.7) (base 34) | 20 (base 23) | 71564 (base 70624) | 470.19 (base 452.39) | 8.18 (base 6.31) |
| hud | 58.0 (base 58.0) | 14 (base 14) | 59972 (base 59972) | 490.15 (base 493.56) | 8.24 (base 8.18) |
| create_ct | 6.67 (base 6.67) | 10 (base 10) | 1124 (base 1124) | 479.76 (base 470.06) | 8.24 (base 8.24) |

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 20.18 / 20 (29.9) | 179.5 / 226 (29.98) | median and average, x 1.10 + 2 |
| factory_flat | 28.33 / 28 (30.01) | 187.62 / 234 (30.0) | median and average, x 1.10 + 2 |
| sift | 41.0 / 41 (18.89) | 545.98 / 579 (26.06) | median, x 1.10 + 2 |
| hub | 25.54 / 25 (29.9) | 161.8 / 208 (29.99) | median and average, x 1.10 + 2 |
| mam | 24.53 / 24 (29.95) | 199.78 / 246 (30.0) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (30.02) | 180.56 / 227 (29.98) | median and average, x 1.10 + 2 |
| map | 66.0 / 66 (13.07) | 717.0 / 717 (17.18) | median and average, x 1.10 + 2 |
| hologram | 33.7 / 34 (28.33) | 137.16 / 184 (29.85) | median, at most vanilla |
| hud | 58.0 / 58 (18.74) | 539.88 / 571 (25.62) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 110.84 / 158 (30.01) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec old-gpu (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.575 | 12.0 / 1.575 | 0.0 / 0.0 | 416.0 / 2.723 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.273 | 32.0 / 20.273 | 0.0 / 0.0 | 416.0 / 33.993 |
| ponder | 32.0 / 17.522 | 32.0 / 17.522 | 0.0 / 0.0 | 416.0 / 29.199 |
| map | 40.0 / 30.364 | 40.0 / 30.364 | 0.0 / 0.0 | 416.0 / 52.75 |
| hologram | 40.0 / 0.238 | 40.0 / 0.238 | 0.0 / 0.0 | 288.0 / 0.388 |
| hud | 40.0 / 17.909 | 40.0 / 17.909 | 0.0 / 0.0 | 416.0 / 30.583 |
| create_ct | 40.0 / 0.687 | 40.0 / 0.687 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 91632 | 25.028 / 0.426 | 1.453 / 28.0 | 2.447 | 1660 / 0 | 1.741 (1.935) |
| factory_flat | plain, one multi-draw per region and layer | 8.013 | 74.124 | 16 (vanilla 28) | 99196 | 3.525 / 0.001 | 1.575 / 12.0 | 2.649 | 310 / 0 | 1.414 (3.746) |
| sift | plain, one multi-draw per region and layer | 21.056 | 430.138 | 16 (vanilla 28) | 462608 | 22.8 / 0.429 | 7.389 / 16.0 | 12.353 | 2669 / 0 | None (None) |
| hub | plain, one multi-draw per region and layer | 7.012 | 50.084 | 16 (vanilla 28) | 117492 | 22.05 / 0.413 | 1.879 / 12.0 | 3.137 | 2758 / 0 | None (None) |
| mam | plain, one multi-draw per region and layer | 5.008 | 87.145 | 16 (vanilla 28) | 1191428 | 22.976 / 0.426 | 20.273 / 32.0 | 31.815 | 3409 / 0 | None (None) |
| ponder | plain, one multi-draw per region and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1017316 | 22.976 / 0.426 | 17.522 / 32.0 | 27.165 | 3410 / 0 | None (None) |
| map | plain, one multi-draw per region and layer | 39.149 | 552.107 | 16 (vanilla 28) | 1905884 | 19.429 / 0.325 | 30.364 / 40.0 | 50.893 | 4329 / 0 | None (None) |
| hologram | plain, one multi-draw per region and layer | 3.005 | 13.023 | 16 (vanilla 28) | 13744 | 19.344 / 0.322 | 0.238 / 40.0 | 0.367 | 4391 / 0 | None (None) |
| hud | plain, one multi-draw per region and layer | 30.08 | 404.078 | 16 (vanilla 28) | 1123516 | 15.883 / 0.261 | 17.909 / 40.0 | 30.001 | 6215 / 0 | None (None) |
| create_ct | plain, one multi-draw per region and layer | 3.005 | 14.023 | 16 (vanilla 28) | 42660 | 15.732 / 0.259 | 0.687 / 40.0 | 1.139 | 6323 / 0 | None (None) |

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2268 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 598 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 599 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 33.0 | 15.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 8646 / 0 / 0 |
| hologram | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2268 / 0 / 0 |
| hud | 36.0 | 9.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 13500 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.003 (0.039; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.043; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hologram | 9.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.012; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 225.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.005; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.255 / 0.0 | 0.583 / 0.513 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.299 / 10.187 | 0.015 / 0.006 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.333 / 0.0 | 0.623 / 0.659 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.945 / 12.163 | 0.018 / 0.008 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 107.217 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.004 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.333 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.711 / 0.0 | 0.004 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hologram | 93.49 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 150.703 / 88.97 | 0.12 / 0.058 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 109.102 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.178 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.87 / 13.88 | 0.0 / 0.0 | 571.57 / 572.0 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.87 / 13.88 (8.0 / 8.0) |
| mam | 13.86 / 13.86 | 1.0 / 1.0 | 110.25 / 109.98 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.86 / 13.86 (8.0 / 8.0) |
| ponder | 82.57 / 82.56 | 0.11 / 0.0 | 203.09 / 202.93 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.59 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hologram | 18.0 / 18.0 | 0.0 / 0.0 | 128.0 / 128.0 | 12.0 / 12.0 (pipelines 6.0, textures 8.0, scissors 2.0) | 18.0 / 18.0 (12.0 / 12.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| factory | 14.22 (base 14.22) | 15 (base 15) | 51608 (base 51148) | 421.82 (base 420.23) | 3.71 (base 3.71) |
| factory_flat | 22.31 (base 22.31) | 15 (base 15) | 54716 (base 50904) | 423.17 (base 421.17) | 3.52 (base 3.46) |
| sift | 23.0 (base 23.0) | 18 (base 19) | 16632 (base 81788) | 439.69 (base 439.9) | 3.71 (base 4.23) |
| hub | 21.55 (base 21.55) | 16 (base 18) | 66364 (base 67484) | 452.11 (base 450.49) | 3.71 (base 4.23) |
| mam | 22.52 (base 22.52) | 19 (base 21) | 23536 (base 24656) | 465.51 (base 462.56) | 3.71 (base 5.46) |
| ponder | 87.24 (base 87.24) | 14 (base 14) | 52248 (base 52248) | 464.06 (base 462.87) | 5.71 (base 8.23) |
| map | 30.0 (base 30.0) | 14 (base 14) | 46436 (base 47072) | 492.02 (base 504.09) | 10.1 (base 8.23) |
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |
| hud | 31.0 (base 31.0) | 16 (base 16) | 72608 (base 72908) | 476.75 (base 490.43) | 10.1 (base 8.16) |
| create_ct | 6.67 (base 6.67) | 12 (base 12) | 1596 (base 1596) | 471.62 (base 475.26) | 10.1 (base 8.29) |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Brimstone avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| factory | 14.22 / 14 (29.47) | 107.81 / 154 (29.9) | median and average, x 1.10 + 2 |
| factory_flat | 22.31 / 22 (29.94) | 115.8 / 162 (29.94) | median and average, x 1.10 + 2 |
| sift | 23.0 / 23 (14.42) | 153.0 / 153 (17.81) | median, x 1.10 + 2 |
| hub | 21.55 / 21 (29.85) | 115.18 / 161 (29.9) | median and average, x 1.10 + 2 |
| mam | 22.52 / 22 (29.16) | 116.57 / 162 (29.72) | median, x 1.10 + 2 |
| ponder | 87.24 / 87 (29.99) | 180.49 / 227 (30.0) | median and average, x 1.10 + 2 |
| map | 30.0 / 30 (10.17) | 170.0 / 170 (11.63) | median and average, x 1.10 + 2 |
| hud | 31.0 / 31 (14.44) | 171.0 / 171 (17.04) | median and average, x 1.10 + 2 |
| create_ct | 6.67 / 7 (30.0) | 100.16 / 147 (29.95) | median and average, x 1.10 + 2 |

Terrain GPU memory, siftec vulkan (MB; reserved in the Brimstone run must not be more than vanilla's):

| scene | Brimstone run reserved / used | of which Brimstone's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| factory | 28.0 / 1.453 | 28.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| factory_flat | 12.0 / 1.453 | 12.0 / 1.453 | 0.0 / 0.0 | 256.0 / 2.599 |
| sift | 16.0 / 7.389 | 16.0 / 7.389 | 0.0 / 0.0 | 416.0 / 12.892 |
| hub | 12.0 / 1.879 | 12.0 / 1.879 | 0.0 / 0.0 | 416.0 / 3.276 |
| mam | 32.0 / 20.321 | 32.0 / 20.321 | 0.0 / 0.0 | 416.0 / 34.142 |
| ponder | 32.0 / 17.57 | 32.0 / 17.57 | 0.0 / 0.0 | 416.0 / 29.229 |
| map | 40.0 / 30.364 | 40.0 / 30.364 | 0.0 / 0.0 | 416.0 / 52.675 |
| hud | 56.0 / 16.18 | 56.0 / 16.18 | 0.0 / 0.0 | 416.0 / 27.504 |
| create_ct | 56.0 / 0.687 | 56.0 / 0.687 | 0.0 / 0.0 | 416.0 / 0.294 |


Terrain module, siftec vulkan (reported, not gated):

| scene | path | Brimstone draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| factory | indirect, one multi-draw per arena and layer | 2.003 | 74.125 | 16 (vanilla 28) | 91632 | 27.522 / 0.317 | 1.453 / 28.0 | 2.447 | 1837 / 0 | 2.951 (5.108) |
| factory_flat | indirect, one multi-draw per arena and layer | 2.003 | 74.124 | 16 (vanilla 28) | 91632 | 3.495 / 0.002 | 1.453 / 12.0 | 2.447 | 286 / 0 | 1.442 (1.741) |
| sift | indirect, one multi-draw per arena and layer | 3.01 | 430.49 | 16 (vanilla 28) | 462608 | 24.971 / 0.342 | 7.389 / 16.0 | 12.353 | 2848 / 0 | None (None) |
| hub | indirect, one multi-draw per arena and layer | 3.005 | 50.084 | 16 (vanilla 28) | 117492 | 24.185 / 0.33 | 1.879 / 12.0 | 3.137 | 2929 / 0 | None (None) |
| mam | indirect, one multi-draw per arena and layer | 3.005 | 87.149 | 16 (vanilla 28) | 1193688 | 24.672 / 0.358 | 20.321 / 32.0 | 31.875 | 3580 / 0 | None (None) |
| ponder | indirect, one multi-draw per arena and layer | 1.002 | 1.002 | 16 (vanilla 28) | 1019576 | 24.672 / 0.358 | 17.57 / 32.0 | 27.226 | 3581 / 0 | None (None) |
| map | indirect, one multi-draw per arena and layer | 3.015 | 552.709 | 16 (vanilla 28) | 1905884 | 20.889 / 0.279 | 30.364 / 40.0 | 50.893 | 4500 / 0 | None (None) |
| hud | indirect, one multi-draw per arena and layer | 3.01 | 440.524 | 16 (vanilla 28) | 1015828 | 16.718 / 0.236 | 16.18 / 56.0 | 27.126 | 6112 / 0 | None (None) |
| create_ct | indirect, one multi-draw per arena and layer | 3.005 | 14.023 | 16 (vanilla 28) | 42660 | 16.568 / 0.234 | 0.687 / 56.0 | 1.139 | 6216 / 0 | None (None) |

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Brimstone's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| factory | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| factory_flat | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| sift | 6.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1734 / 0 / 0 |
| hub | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 597 / 0 / 0 |
| mam | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 584 / 0 / 0 |
| ponder | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| map | 33.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 6732 / 0 / 0 |
| hud | 40.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 11560 / 0 / 0 |
| create_ct | 1.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 600 / 0 / 0 |

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| factory | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.03; 0) | 0 | 0 / 0 / 0 / 0 |
| factory_flat | 69.0 | 1.0 (0.0, 0.0) | 45.0 (0.0, 0.0) | 0.004 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| sift | 244.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.027; 0) | 0 | 0 / 0 / 0 / 0 |
| hub | 38.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| mam | 55.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| ponder | 1.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.02; 0) | 0 | 0 / 0 / 0 / 0 |
| map | 282.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| hud | 242.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| create_ct | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Brimstone | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| factory | 93.568 / 0.0 | 0.571 / 0.542 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.406 / 10.428 | 0.016 / 0.007 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| factory_flat | 93.489 / 0.0 | 0.643 / 0.649 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.898 / 11.833 | 0.019 / 0.007 | 119 (1) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| sift | 140.0 / 10.0 | 0.0 / 0.0 | 4.0 / 4.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 119 (2) | 28.0 / 2.0 / 26.0 | 0 / 0 | 4.0 |
| hub | 93.645 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| mam | 94.04 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| ponder | 93.255 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |
| map | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| hud | 140.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 3.0 |
| create_ct | 93.489 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 119 (2) | 28.0 / 0.0 / 28.0 | 0 / 0 | 2.0 |

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Brimstone / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|
| factory | 5.0 / 5.0 | 0.0 / 0.0 | 21.0 / 21.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| factory_flat | 13.0 / 13.0 | 0.0 / 0.0 | 43.0 / 43.0 | 7.0 / 7.0 (pipelines 4.0, textures 6.0, scissors 2.0) | 12.0 / 12.0 (8.0 / 8.0) |
| sift | 5.0 / 5.0 | 0.0 / 0.0 | 20.0 / 20.0 | 3.0 / 3.0 (pipelines 3.0, textures 3.0, scissors 1.0) | 5.0 / 5.0 (3.0 / 3.0) |
| hub | 13.88 / 13.87 | 0.0 / 0.0 | 571.8 / 571.49 | 6.0 / 6.0 (pipelines 6.0, textures 5.0, scissors 1.0) | 13.88 / 13.87 (8.0 / 8.0) |
| mam | 13.84 / 13.86 | 1.0 / 1.0 | 109.32 / 110.12 | 7.0 / 7.0 (pipelines 6.0, textures 6.0, scissors 1.0) | 13.84 / 13.86 (8.0 / 8.0) |
| ponder | 82.57 / 82.57 | 0.11 / 0.11 | 203.09 / 203.04 | 73.0 / 73.0 (pipelines 6.0, textures 72.0, scissors 1.0) | 81.6 / 81.6 (75.0 / 75.0) |
| map | 23.0 / 23.0 | 0.0 / 0.0 | 312.0 / 312.0 | 12.0 / 12.0 (pipelines 5.0, textures 6.0, scissors 3.0) | 21.0 / 21.0 (15.0 / 15.0) |
| hud | 23.0 / 23.0 | 0.0 / 0.0 | 279.0 / 279.0 | 11.0 / 11.0 (pipelines 6.0, textures 7.0, scissors 2.0) | 23.0 / 23.0 (13.0 / 13.0) |
| create_ct | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 (pipelines 0.0, textures 0.0, scissors 0.0) | 0.0 / 0.0 (0.0 / 0.0) |

