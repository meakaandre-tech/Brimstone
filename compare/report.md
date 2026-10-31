# Quicksilver CI comparison

**passed**

## SIFTEC: report only

Until SIFTEC starts with MapMod's latest build again, SIFTEC's suites fail nothing (docs/progress.md, open items). Core and Terralith gate as usual.

SIFTEC started in every run.

SIFTEC failures (not gating):

- siftec: run vanilla-a produced no results
- siftec: run vanilla-b produced no results
- siftec: run vanilla-old produced no results
- siftec: run vanilla-vk produced no results
- siftec: run quicksilver-full produced no results
- siftec: run quicksilver-old produced no results
- siftec: run quicksilver-vk produced no results
- siftec: run measure-full produced no results
- siftec: run measure-old produced no results
- siftec: run measure-vk produced no results
- siftec/vanilla-vk: vanilla is not on the Vulkan backend (no backend line)
- siftec/quicksilver-full: no Quicksilver capability line
- siftec/quicksilver-full: the server did not list Quicksilver among its mods
- siftec/quicksilver-old: no Quicksilver capability line
- siftec/quicksilver-old: the server did not list Quicksilver among its mods
- siftec/quicksilver-vk: vanilla is not on the Vulkan backend (no backend line)
- siftec/quicksilver-vk: no Quicksilver capability line
- siftec/quicksilver-vk: the server did not list Quicksilver among its mods
- siftec/measure-full: no Quicksilver capability line
- siftec/measure-full: the server did not list Quicksilver among its mods
- siftec/measure-old: no Quicksilver capability line
- siftec/measure-old: the server did not list Quicksilver among its mods
- siftec/measure-vk: vanilla is not on the Vulkan backend (no backend line)
- siftec/measure-vk: no Quicksilver capability line
- siftec/measure-vk: the server did not list Quicksilver among its mods
- siftec/factory: no full screenshot pair
- siftec/factory: no old-gpu screenshot pair
- siftec/factory: no vulkan screenshot pair
- siftec/factory: strict picture check is missing a vanilla screenshot
- siftec/factory: no measure-full screenshot
- siftec/factory: no measure-old screenshot
- siftec/factory: no measure-vk screenshot
- siftec/factory_flat: no full screenshot pair
- siftec/factory_flat: no old-gpu screenshot pair
- siftec/factory_flat: no vulkan screenshot pair
- siftec/factory_flat: strict picture check is missing a vanilla screenshot
- siftec/factory_flat: no measure-full screenshot
- siftec/factory_flat: no measure-old screenshot
- siftec/factory_flat: no measure-vk screenshot
- siftec/sift: no full screenshot pair
- siftec/sift: no old-gpu screenshot pair
- siftec/sift: no vulkan screenshot pair
- siftec/sift: strict picture check is missing a vanilla screenshot
- siftec/sift: no measure-full screenshot
- siftec/sift: no measure-old screenshot
- siftec/sift: no measure-vk screenshot
- siftec/hub: no full screenshot pair
- siftec/hub: no old-gpu screenshot pair
- siftec/hub: no vulkan screenshot pair
- siftec/hub: strict picture check is missing a vanilla screenshot
- siftec/hub: no measure-full screenshot
- siftec/hub: no measure-old screenshot
- siftec/hub: no measure-vk screenshot
- siftec/mam: no full screenshot pair
- siftec/mam: no old-gpu screenshot pair
- siftec/mam: no vulkan screenshot pair
- siftec/mam: strict picture check is missing a vanilla screenshot
- siftec/mam: no measure-full screenshot
- siftec/mam: no measure-old screenshot
- siftec/mam: no measure-vk screenshot
- siftec/ponder: no full screenshot pair
- siftec/ponder: no old-gpu screenshot pair
- siftec/ponder: no vulkan screenshot pair
- siftec/ponder: strict picture check is missing a vanilla screenshot
- siftec/ponder: no measure-full screenshot
- siftec/ponder: no measure-old screenshot
- siftec/ponder: no measure-vk screenshot
- siftec/hologram: no full screenshot pair
- siftec/hologram: no old-gpu screenshot pair
- siftec/hologram: strict picture check is missing a vanilla screenshot
- siftec/hologram: no measure-full screenshot
- siftec/hologram: no measure-old screenshot
- siftec/create_ct: no full screenshot pair
- siftec/create_ct: no old-gpu screenshot pair
- siftec/create_ct: no vulkan screenshot pair
- siftec/create_ct: strict picture check is missing a vanilla screenshot
- siftec/create_ct: no measure-full screenshot
- siftec/create_ct: no measure-old screenshot
- siftec/create_ct: no measure-vk screenshot
- siftec/full: no bench result for factory
- siftec/full: no bench result for factory_flat
- siftec/full: no bench result for sift
- siftec/full: no bench result for hub
- siftec/full: no bench result for mam
- siftec/full: no bench result for ponder
- siftec/full: no bench result for hologram
- siftec/full: no bench result for create_ct
- siftec/old-gpu: no bench result for factory
- siftec/old-gpu: no bench result for factory_flat
- siftec/old-gpu: no bench result for sift
- siftec/old-gpu: no bench result for hub
- siftec/old-gpu: no bench result for mam
- siftec/old-gpu: no bench result for ponder
- siftec/old-gpu: no bench result for hologram
- siftec/old-gpu: no bench result for create_ct
- siftec/vulkan: no bench result for factory
- siftec/vulkan: no bench result for factory_flat
- siftec/vulkan: no bench result for sift
- siftec/vulkan: no bench result for hub
- siftec/vulkan: no bench result for mam
- siftec/vulkan: no bench result for ponder
- siftec/vulkan: no bench result for create_ct

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
| spawn | 0.4236 | 10.05 | 0.4374 | 0.2977 | 0.2031 |
| flythrough | 0.0 | 14.95 | 0.001 | 0.001 | 0.0011 |
| water_glass | 0.0044 | 6.45 | 0.0053 | 0.0043 | 0.0038 |
| nether | 0.0 | 0.8 | 0.0104 | 0.0127 | 0.0489 |
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
| translucent_move | 0.0002 | 0.3 | 0.0002 | 0.0 | 0.0 |
| translucent_move.x2404 | 0.0003 | 0.3 | 0.0005 | 0.0004 | 0.0004 |
| translucent_move.x2414 | 0.0002 | 0.3 | 0.0003 | 0.0001 | 0.0003 |
| translucent_move.x2424 | 0.0004 | 0.3 | 0.0004 | 0.0001 | 0.0003 |
| translucent_move.low | 0.0002 | 0.3 | 0.001 | 0.0013 | 0.0017 |
| translucent_move.back | 0.0 | 0.3 | 0.0 | 0.0001 | 0.0002 |
| translucent_move.bare_back | 0.0 | 0.3 | 0.0 | 0.0 | 0.0003 |
| water_glass_oit | 0.0033 | 0.3 | 0.0037 | 0.005 | 0.0061 |
| translucent_move_oit | 0.0 | 0.3 | 0.0 | 0.0001 | 0.0 |
| translucent_move_oit.x2404 | 0.0 | 0.3 | 0.0002 | 0.0004 | 0.0004 |
| translucent_move_oit.x2414 | 0.0002 | 0.3 | 0.0003 | 0.0003 | 0.0003 |
| translucent_move_oit.x2424 | 0.0 | 0.3 | 0.0001 | 0.0001 | 0.0002 |
| translucent_move_oit.low | 0.0007 | 0.3 | 0.0013 | 0.0015 | 0.0013 |
| translucent_move_oit.back | 0.0003 | 0.3 | 0.0001 | 0.0001 | 0.0002 |
| translucent_move_oit.bare_back | 0.0002 | 0.3 | 0.0001 | 0.0 | 0.0001 |
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
| spawn | 92.68 | 18057 | 229 (750) | 258 (766) | 239 (753) |
| flythrough | 97.31 | 6020 | 47 (292) | 47 (292) | 51 (292) |
| water_glass | 95.81 | 25508 | 24 (2626) | 15 (2616) | 19 (2734) |
| nether | 99.36 | 905 | 96 (550) | 3 (458) | 3 (470) |
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
| translucent_move | 96.93 | 13 | 3 (79) | 1 (76) | 1 (67) |
| translucent_move.x2404 | 95.93 | 15 | 3 (104) | 4 (104) | 5 (89) |
| translucent_move.x2414 | 95.86 | 13 | 4 (107) | 1 (104) | 2 (99) |
| translucent_move.x2424 | 96.74 | 13 | 3 (80) | 1 (78) | 1 (68) |
| translucent_move.low | 96.11 | 41 | 15 (20) | 11 (16) | 17 (19) |
| translucent_move.back | 96.93 | 13 | 2 (77) | 3 (78) | 3 (69) |
| translucent_move.bare_back | 96.9 | 13 | 2 (77) | 1 (76) | 1 (68) |
| water_glass_oit | 95.8 | 57 | 18 (2618) | 18 (2619) | 27 (2740) |
| translucent_move_oit | 96.93 | 12 | 1 (76) | 1 (76) | 1 (67) |
| translucent_move_oit.x2404 | 95.93 | 16 | 3 (103) | 6 (106) | 3 (87) |
| translucent_move_oit.x2414 | 95.86 | 10 | 1 (104) | 4 (107) | 2 (99) |
| translucent_move_oit.x2424 | 96.74 | 15 | 2 (79) | 2 (79) | 2 (68) |
| translucent_move_oit.low | 96.11 | 40 | 14 (21) | 17 (22) | 14 (17) |
| translucent_move_oit.back | 96.93 | 12 | 2 (77) | 2 (77) | 3 (69) |
| translucent_move_oit.bare_back | 96.9 | 15 | 1 (76) | 1 (76) | 1 (67) |
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
| spawn | 0.0647 / 262 | 0.4425 / 314 | 0.1975 / 233 |
| flythrough | 0.0 / 0 | 0.0 / 0 | 0.0003 / 2 |
| water_glass | 0.0047 / 24 | 0.0038 / 14 | 0.0041 / 21 |
| nether | 0.014 / 142 | 0.0124 / 0 | 0.0486 / 0 |
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
| translucent_move | 0.0002 / 3 | 0.0001 / 1 | 0.0001 / 1 |
| translucent_move.x2404 | 0.0003 / 0 | 0.0002 / 0 | 0.0001 / 0 |
| translucent_move.x2414 | 0.0005 / 4 | 0.0 / 0 | 0.0001 / 0 |
| translucent_move.x2424 | 0.0003 / 0 | 0.0 / 3 | 0.0002 / 0 |
| translucent_move.low | 0.0003 / 5 | 0.001 / 4 | 0.001 / 6 |
| translucent_move.back | 0.0 / 0 | 0.0 / 1 | 0.0004 / 2 |
| translucent_move.bare_back | 0.0 / 0 | 0.0003 / 3 | 0.0003 / 2 |
| water_glass_oit | 0.0035 / 17 | 0.0049 / 17 | 0.0047 / 17 |
| translucent_move_oit | 0.0 / 0 | 0.0002 / 1 | 0.0001 / 2 |
| translucent_move_oit.x2404 | 0.0001 / 1 | 0.0001 / 0 | 0.0003 / 1 |
| translucent_move_oit.x2414 | 0.0003 / 2 | 0.0 / 1 | 0.0004 / 6 |
| translucent_move_oit.x2424 | 0.0001 / 2 | 0.0001 / 2 | 0.0 / 0 |
| translucent_move_oit.low | 0.0005 / 2 | 0.0008 / 3 | 0.001 / 8 |
| translucent_move_oit.back | 0.0001 / 1 | 0.0003 / 2 | 0.0001 / 1 |
| translucent_move_oit.bare_back | 0.0001 / 1 | 0.0 / 0 | 0.0001 / 0 |
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
| spawn | 13.0 (base 13.6) | 14 (base 14) | 24996 (base 22476) | 248.21 (base 259.07) | 4.24 (base 3.99) |
| flythrough | 13.06 (reported, not gated) | 36 (reported, not gated) | 402492 (reported, not gated) | 304.8 (base 324.25) | 3.73 (base 4.24) |
| water_glass | 8.88 (base 8.97) | 15 (base 15) | 15416 (base 15576) | 233.12 (base 250.02) | 4.24 (base 5.99) |
| nether | 14.57 (base 15.63) | 12 (base 12) | 20732 (base 19648) | 253.82 (base 273.53) | 4.24 (base 5.99) |
| mobs | 19.71 (base 19.77) | 17 (base 17) | 616288 (base 1010948) | 252.53 (base 268.37) | 4.24 (base 6.99) |
| outline | 10.67 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 252.44 (base 264.69) | 4.24 (base 5.48) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100576 (base 126320) | 258.07 (base 265.1) | 4.24 (base 4.24) |
| occlusion_move | 16.07 (reported, not gated) | 15 (reported, not gated) | 82468 (reported, not gated) | 258.41 (base 265.74) | 4.24 (base 4.24) |
| translucent_move | 8.85 (reported, not gated) | 15 (reported, not gated) | 11156 (reported, not gated) | 260.35 (base 267.48) | 4.24 (base 5.48) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15732 (base 14688) | 232.29 (base 245.02) | 3.6 (base 3.73) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 10892 (reported, not gated) | 234.91 (base 247.19) | 3.73 (base 3.73) |
| flat_plants | 17.78 (base 18.29) | 15 (base 15) | 10108 (base 9812) | 235.75 (base 246.92) | 3.73 (base 3.99) |
| renderer_off | 7.67 (base 7.67) | 14 (base 14) | 7372 (base 7164) | 228.06 (base 241.33) | 3.35 (base 4.12) |
| particles | 9.67 (base 9.72) | 15 (base 15) | 9204 (base 17128) | 255.57 (base 261.44) | 4.24 (base 5.99) |
| animation | 49 median (avg 39.95) (base 49) | 15 (base 15) | 2224 (base 2056) | 253.07 (base 260.68) | 4.24 (base 5.99) |
| animated_items | 60 median (avg 46.73) (base 60) | 24 (base 23) | 22136 (base 21940) | 228.23 (base 239.81) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-full), core full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (11.91) | 124.42 / 124 (8.66) | median and average, x 1.10 + 2 |
| flythrough | 13.06 / 13 (8.54) | 123.06 / 123 (5.44) | median and average, at most vanilla + 2 |
| water_glass | 8.88 / 9 (22.55) | 124.0 / 124 (15.07) | median, at most vanilla |
| nether | 14.57 / 16 (22.33) | 120.8 / 121 (15.15) | median, x 1.10 + 2 |
| mobs | 19.71 / 20 (28.16) | 134.0 / 134 (18.9) | median, x 1.10 + 2 |
| outline | 10.67 / 11 (30.0) | 87.41 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (30.0) | 94.27 / 133 (29.97) | median and average, x 1.10 + 2 |
| occlusion_move | 16.07 / 17 (29.99) | 94.46 / 133 (29.96) | median and average, x 1.10 + 2 |
| translucent_move | 8.85 / 9 (23.48) | 124.0 / 124 (15.75) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (12.29) | 129.0 / 129 (8.34) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (11.23) | 129.0 / 129 (8.09) | median and average, at most vanilla |
| flat_plants | 17.78 / 19 (22.16) | 123.86 / 124 (15.85) | median, x 1.10 + 2 |
| renderer_off | 7.67 / 8 (29.97) | 98.65 / 123 (25.29) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.0) | 144.22 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 155.22 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 22.672 | 36.0 / 22.672 | 0.0 / 0.0 | 416.0 / 37.987 |
| flythrough | 112.0 / 86.761 | 112.0 / 86.761 | 0.0 / 0.0 | 416.0 / 143.186 |
| water_glass | 36.0 / 12.548 | 36.0 / 12.548 | 0.0 / 0.0 | 416.0 / 21.838 |
| nether | 20.0 / 15.133 | 20.0 / 15.133 | 0.0 / 0.0 | 256.0 / 26.572 |
| mobs | 16.0 / 9.91 | 16.0 / 9.91 | 0.0 / 0.0 | 256.0 / 17.319 |
| outline | 16.0 / 1.46 | 16.0 / 1.46 | 0.0 / 0.0 | 256.0 / 2.533 |
| occlusion | 20.0 / 2.678 | 20.0 / 2.678 | 0.0 / 0.0 | 416.0 / 4.487 |
| occlusion_move | 20.0 / 3.466 | 20.0 / 3.466 | 0.0 / 0.0 | 416.0 / 5.859 |
| translucent_move | 28.0 / 12.92 | 28.0 / 12.92 | 0.0 / 0.0 | 416.0 / 22.175 |
| water_glass_oit | 20.0 / 12.548 | 20.0 / 12.548 | 0.0 / 0.0 | 416.0 / 21.838 |
| translucent_move_oit | 28.0 / 12.51 | 28.0 / 12.51 | 0.0 / 0.0 | 416.0 / 21.462 |
| flat_plants | 24.0 / 13.833 | 24.0 / 13.833 | 0.0 / 0.0 | 416.0 / 23.856 |
| renderer_off | 24.0 / 8.678 | 24.0 / 8.678 | 0.0 / 0.0 | 416.0 / 15.261 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 510835.857 (687641.654) | 25.712 | 1.158 | 628540 / 1061480 | 603544 (2097152.0) | 237 / 173 | 0.0 / 0 |
| flythrough | 565622.965 (905926.835) | 37.564 | 1.083 | 2373960 / 7133624 | 2096752 (2097152.0) | 170 / 108 | 7.076 / 74 |
| water_glass | 217416.076 (389880.48) | 44.235 | 1.116 | 173604 / 292908 | 156864 (2097152.0) | 9 / 301 | 0.0 / 0 |
| nether | 317990.987 (476714.072) | 33.295 | 1.272 | 193016 / 408460 | 171968 (2097152.0) | 3 / 302 | 0.0 / 0 |
| mobs | 170690.643 (309434.643) | 44.838 | 0.99 | 773152 / 893816 | 156864 (2097152.0) | 0 / 376 | 0.0 / 0 |
| outline | 26864.626 (45701.917) | 41.218 | 1.0 | 6300 / 9012 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 47425.042 (83689.482) | 43.332 | 1.203 | 100576 / 360312 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 51904.053 (91600.003) | 43.336 | 1.229 | 660056 / 1101344 | 583232 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 199340.866 (353021.689) | 43.533 | 1.183 | 906220 / 1575116 | 893760 (2097152.0) | 10 / 315 | 0.0 / 0 |
| water_glass_oit | 217819.445 (390603.82) | 44.235 | 1.116 | 17076 / 16716 | 0 (2097152.0) | 245 / 166 | 0.0 / 0 |
| translucent_move_oit | 199554.946 (353212.732) | 43.503 | 1.181 | 904720 / 1574724 | 893760 (2097152.0) | 224 / 161 | 0.0 / 0 |
| flat_plants | 236430.501 (432826.835) | 45.375 | 1.056 | 272140 / 271764 | 0 (2097152.0) | 49 / 317 | 0.0 / 0 |
| renderer_off | 150550.918 (273245.409) | 44.903 | 1.019 | 7372 / 7292 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| particles | 1927.212 (4215.025) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.736 (5631.76) | 42.864 | 1.071 | 86832 / 150268 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79864 / 123000 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.013 | 787.308 | 16 (vanilla 28) | 1369684 | 41.043 / 0.075 | 22.672 / 36.0 | 36.575 | 2376 / 0 | 4.004 (8.5) |
| flythrough | indirect, one multi-draw per arena and layer | 3.018 | 913.124 | 16 (vanilla 28) | 5254304 | 15.01 / 0.965 | 86.761 / 112.0 | 140.305 | 6581 / 0 | 5.57 (13.559) |
| water_glass | indirect, one multi-draw per arena and layer | 3.007 | 276.613 | 16 (vanilla 28) | 778032 | 33.191 / 0.072 | 12.548 / 36.0 | 20.776 | 3049 / 0 | 4.476 (3.763) |
| nether | indirect, one multi-draw per arena and layer | 2.004 | 471.054 | 16 (vanilla 28) | 951352 | 27.855 / 0.165 | 15.133 / 20.0 | 25.404 | 4095 / 0 | 2.604 (2.998) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.35 | 16 (vanilla 28) | 617772 | 25.125 / 0.142 | 9.91 / 16.0 | 16.496 | 4553 / 0 | 4.913 (4.195) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 91252 | 24.67 / 0.142 | 1.46 / 16.0 | 2.437 | 4646 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 69.115 | 16 (vanilla 28) | 167100 | 23.714 / 0.136 | 2.678 / 20.0 | 4.462 | 4853 / 0 | None (None) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 74.452 | 16 (vanilla 28) | 216552 | 22.564 / 0.129 | 3.466 / 20.0 | 5.783 | 5113 / 0 | 3.825 (7.189) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.006 | 248.013 | 16 (vanilla 28) | 797856 | 19.671 / 0.12 | 12.92 / 28.0 | 21.305 | 5879 / 0 | 6.228 (14.918) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.02 | 285.159 | 16 (vanilla 28) | 778032 | 7.458 / 0.043 | 12.548 / 20.0 | 20.776 | 1098 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.022 | 263.004 | 16 (vanilla 28) | 772052 | 6.684 / 0.11 | 12.51 / 28.0 | 20.616 | 2435 / 0 | 5.868 (12.491) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.007 | 286.646 | 16 (vanilla 28) | 863704 | 6.461 / 0.01 | 13.833 / 24.0 | 23.063 | 963 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 206.344 | 16 (vanilla 28) | 545580 | 2.442 / 0.021 | 8.678 / 24.0 | 14.569 | 600 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.598 / 0.119 | 0.131 / 28.0 | 0.225 | 5925 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.433 / 0.118 | 0.302 / 28.0 | 0.518 | 6027 / 0 | 1.224 (1.661) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.375 / 0.118 | 0.153 / 28.0 | 0.261 | 6077 / 0 | 1.046 (1.466) |

Translucency, core full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 153.0 | 1.0 | 0.0 | 0 | 0 | 58 (0.019 / 0.033 / 0.034; 0) | 0 | 36414 / 0 / 0 |
| flythrough | 192.082 | 1.0 | 0.0 | 0 | 0 | 883 (0.014 / 0.034 / 1.514; 0) | 64 | 32846 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1804 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.009 / 0.009; 0) | 32 | 1200 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 180 (0.093 / 0.124 / 1.812; 0) | 180 | 3290 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 738 | 0 (None / None / None; 0) | 0 | 984 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 675 | 0 (None / None / None; 0) | 0 | 1575 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1776 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2400 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 341.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.028 (0.097; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 435.292 | 1.0 (0.0, 0.0) | 1.187 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 141.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.015 (0.044; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.13 (0.23; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.011; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.058 (0.099; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 50.36 | 10.0 (9.0, 2.368) | 6.0 (6.0, 2.277) | 0.054 (0.178; 0) | 0 | 2787 / 1509644 / 0 / 0 |
| translucent_move | 135.034 | 1.0 (0.0, 0.0) | 0.772 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 141.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 135.596 | 1.0 (0.0, 0.0) | 0.747 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.04; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.018; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.593 (0.0, 0.0) | 0.001 (0.017; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.036; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.416 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 85.2 / 0.0 | 0.022 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.295 / 0.0 | 0.025 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.842 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.05 / 52.098 | 0.017 / 0.023 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 8.969 | 0.805 / 0.709 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 51.145 / 8.606 | 0.078 / 0.01 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.795 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 15.108 / 15.04 | 0.006 / 0.017 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 52.885 / 52.837 | 0.016 / 0.022 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.323 / 12.222 | 0.007 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 8.939 | 0.858 / 0.948 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.015 / 1.921 | 0.016 / 0.011 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 90.861 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.001 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.007 / 0.051 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.553 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.797 | 0.006 / 0.013 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.983 |
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
| spawn | 49.09 (base 49.0) | 12 (base 13) | 1380 (base 6372) | 249.37 (base 256.29) | 4.24 (base 3.73) |
| flythrough | 67.09 (reported, not gated) | 46 (reported, not gated) | 798668 (reported, not gated) | 303.69 (base 324.79) | 3.73 (base 5.99) |
| water_glass | 19.98 (base 20.0) | 13 (base 14) | 7316 (base 10264) | 233.41 (base 249.83) | 4.24 (base 5.99) |
| nether | 33.98 (base 33.79) | 10 (base 11) | 4392 (base 10656) | 254.57 (base 272.57) | 4.24 (base 5.99) |
| mobs | 28.87 (base 28.97) | 14 (base 16) | 610516 (base 1015892) | 252.82 (base 268.23) | 4.24 (base 5.99) |
| outline | 19.67 (base 19.67) | 16 (base 16) | 5012 (base 5012) | 252.64 (base 264.41) | 4.24 (base 3.73) |
| occlusion | 22.67 (base 22.67) | 13 (base 14) | 98148 (base 124172) | 258.09 (base 264.64) | 4.24 (base 4.24) |
| occlusion_move | 24.03 (reported, not gated) | 13 (reported, not gated) | 79984 (reported, not gated) | 258.58 (base 265.66) | 4.24 (base 5.99) |
| translucent_move | 18.0 (reported, not gated) | 13 (reported, not gated) | 2724 (reported, not gated) | 260.53 (base 268.19) | 4.24 (base 3.73) |
| water_glass_oit | 25.0 (base 25.0) | 13 (base 14) | 8212 (base 7604) | 232.86 (base 245.78) | 3.6 (base 3.73) |
| translucent_move_oit | 25.53 (reported, not gated) | 13 (reported, not gated) | 2836 (reported, not gated) | 235.68 (base 248.15) | 3.73 (base 3.73) |
| flat_plants | 32.97 (base 33.0) | 13 (base 13) | 1604 (base 1716) | 236.04 (base 248.11) | 3.48 (base 5.99) |
| renderer_off | 17.68 (base 17.77) | 12 (base 12) | 1380 (base 1380) | 228.72 (base 240.73) | 3.6 (base 5.87) |
| particles | 10.67 (base 10.72) | 13 (base 13) | 8772 (base 16164) | 255.79 (base 261.4) | 4.24 (base 4.24) |
| animation | 49 median (avg 39.95) (base 49) | 13 (base 13) | 1716 (base 1716) | 253.41 (base 260.68) | 4.24 (base 4.24) |
| animated_items | 60 median (avg 46.73) (base 60) | 22 (base 22) | 21668 (base 21668) | 228.36 (base 239.53) | 4.24 (base 5.99) |

Draw calls against vanilla measured in the same run (measure-old), core old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 49.09 / 49 (11.21) | 869.44 / 869 (10.23) | median and average, x 1.10 + 2 |
| flythrough | 67.09 / 67 (6.46) | 1001.25 / 1003 (5.92) | median and average, at most vanilla |
| water_glass | 19.98 / 20 (20.35) | 395.0 / 395 (16.84) | median, at most vanilla |
| nether | 33.98 / 34 (18.99) | 588.79 / 589 (16.77) | median, x 1.10 + 2 |
| mobs | 28.87 / 29 (22.73) | 321.12 / 329 (21.39) | median, x 1.10 + 2 |
| outline | 19.67 / 20 (30.0) | 127.27 / 166 (30.0) | median, x 1.10 + 2 |
| occlusion | 22.67 / 23 (29.98) | 160.33 / 199 (29.96) | median and average, x 1.10 + 2 |
| occlusion_move | 24.03 / 24 (29.81) | 165.68 / 200 (29.97) | median and average, x 1.10 + 2 |
| translucent_move | 18.0 / 18 (18.83) | 367.6 / 367 (17.38) | median, at most vanilla |
| water_glass_oit | 25.0 / 25 (10.0) | 406.0 / 406 (8.17) | median, at most vanilla |
| translucent_move_oit | 25.53 / 26 (9.38) | 384.9 / 386 (8.29) | median and average, at most vanilla |
| flat_plants | 32.97 / 33 (18.56) | 400.92 / 401 (17.87) | median, x 1.10 + 2 |
| renderer_off | 17.68 / 18 (29.57) | 299.06 / 326 (26.06) | median, x 1.10 + 2 |
| particles | 10.67 / 11 (30.0) | 97.33 / 136 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.95 / 49 (30.01) | 156.46 / 191 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.73 / 60 (30.0) | 167.46 / 202 (30.0) | median, x 1.10 + 2 |

Terrain GPU memory, core old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 23.077 | 36.0 / 23.077 | 0.0 / 0.0 | 416.0 / 37.845 |
| flythrough | 112.0 / 84.174 | 112.0 / 84.174 | 0.0 / 0.0 | 416.0 / 142.745 |
| water_glass | 36.0 / 12.674 | 36.0 / 12.674 | 0.0 / 0.0 | 416.0 / 21.838 |
| nether | 20.0 / 15.132 | 20.0 / 15.132 | 0.0 / 0.0 | 256.0 / 26.574 |
| mobs | 16.0 / 9.91 | 16.0 / 9.91 | 0.0 / 0.0 | 256.0 / 17.319 |
| outline | 16.0 / 1.46 | 16.0 / 1.46 | 0.0 / 0.0 | 256.0 / 2.533 |
| occlusion | 20.0 / 2.589 | 20.0 / 2.589 | 0.0 / 0.0 | 416.0 / 4.5 |
| occlusion_move | 20.0 / 3.391 | 20.0 / 3.391 | 0.0 / 0.0 | 416.0 / 5.854 |
| translucent_move | 28.0 / 12.867 | 28.0 / 12.867 | 0.0 / 0.0 | 416.0 / 22.058 |
| water_glass_oit | 20.0 / 12.548 | 20.0 / 12.548 | 0.0 / 0.0 | 416.0 / 21.838 |
| translucent_move_oit | 28.0 / 12.511 | 28.0 / 12.511 | 0.0 / 0.0 | 416.0 / 21.434 |
| flat_plants | 24.0 / 13.784 | 24.0 / 13.784 | 0.0 / 0.0 | 416.0 / 23.747 |
| renderer_off | 24.0 / 8.69 | 24.0 / 8.69 | 0.0 / 0.0 | 416.0 / 15.252 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 454237.75 (696670.714) | 34.799 | 1.781 | 527252 / 795732 | 525872 (2097152.0) | 224 / 204 | 0.0 / 0 |
| flythrough | 551930.419 (884346.388) | 37.589 | 1.085 | 2104820 / 7479144 | 2096040 (2097152.0) | 129 / 118 | 11.323 / 99 |
| water_glass | 217467.007 (389971.813) | 44.235 | 1.116 | 9444 / 12020 | 0 (2097152.0) | 79 / 336 | 0.0 / 0 |
| nether | 268454.354 (476890.871) | 43.707 | 1.702 | 196416 / 353396 | 191232 (2097152.0) | 379 / 335 | 0.0 / 0 |
| mobs | 170763.304 (309566.366) | 44.838 | 0.99 | 610516 / 612388 | 0 (2097152.0) | 18 / 12 | 0.0 / 0 |
| outline | 26864.7 (45702.043) | 41.218 | 1.0 | 5012 / 5460 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion | 46327.212 (81419.699) | 43.101 | 1.197 | 98148 / 124908 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 50868.235 (89465.946) | 43.142 | 1.223 | 765036 / 1102844 | 691008 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 198766.484 (352117.745) | 43.551 | 1.181 | 898228 / 1569060 | 893760 (2097152.0) | 320 / 346 | 0.0 / 0 |
| water_glass_oit | 218024.121 (390970.854) | 44.235 | 1.116 | 165860 / 284516 | 156864 (2097152.0) | 199 / 163 | 0.0 / 0 |
| translucent_move_oit | 199067.198 (352427.711) | 43.515 | 1.179 | 898900 / 1570020 | 893760 (2097152.0) | 187 / 165 | 0.0 / 0 |
| flat_plants | 236082.341 (432038.524) | 45.356 | 1.056 | 1940 / 315604 | 0 (2097152.0) | 370 / 357 | 0.0 / 0 |
| renderer_off | 150704.139 (273539.28) | 44.906 | 1.019 | 1380 / 3172 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| particles | 1927.207 (4215.013) | 54.278 | 1.0 | 8772 / 8964 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86232 / 149988 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79396 / 106772 | 57728 (2097152.0) | 0 / 0 | 0.0 / 0 |

Terrain module, core old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | plain, one multi-draw per region and layer | 39.174 | 802.567 | 16 (vanilla 28) | 1394940 | 43.309 / 0.099 | 23.077 / 36.0 | 37.249 | 2474 / 0 | 6.894 (10.39) |
| flythrough | plain, one multi-draw per region and layer | 57.411 | 888.674 | 16 (vanilla 28) | 5098552 | 15.176 / 1.024 | 84.174 / 112.0 | 136.146 | 6463 / 0 | 9.221 (24.968) |
| water_glass | plain, one multi-draw per region and layer | 14.034 | 276.678 | 16 (vanilla 28) | 785504 | 35.327 / 0.087 | 12.674 / 36.0 | 20.975 | 3155 / 0 | None (None) |
| nether | plain, one multi-draw per region and layer | 20.053 | 471.24 | 16 (vanilla 28) | 951352 | 29.709 / 0.176 | 15.132 / 20.0 | 25.404 | 4206 / 0 | 3.499 (9.993) |
| mobs | plain, one multi-draw per region and layer | 11.024 | 197.434 | 16 (vanilla 28) | 617772 | 26.892 / 0.159 | 9.91 / 16.0 | 16.496 | 4675 / 0 | None (None) |
| outline | plain, one multi-draw per region and layer | 11.018 | 42.07 | 16 (vanilla 28) | 91252 | 26.422 / 0.158 | 1.46 / 16.0 | 2.437 | 4768 / 0 | None (None) |
| occlusion | plain, one multi-draw per region and layer | 10.017 | 71.119 | 16 (vanilla 28) | 162568 | 25.379 / 0.151 | 2.589 / 20.0 | 4.341 | 4990 / 0 | None (None) |
| occlusion_move | plain, one multi-draw per region and layer | 10.993 | 76.441 | 16 (vanilla 28) | 212968 | 24.078 / 0.143 | 3.391 / 20.0 | 5.687 | 5276 / 0 | 6.909 (15.078) |
| translucent_move | plain, one multi-draw per region and layer | 12.032 | 250.157 | 16 (vanilla 28) | 795652 | 21.084 / 0.131 | 12.867 / 28.0 | 21.246 | 6031 / 0 | 7.665 (20.805) |
| water_glass_oit | plain, one multi-draw per region and layer | 16.08 | 285.427 | 16 (vanilla 28) | 778032 | 7.455 / 0.043 | 12.548 / 20.0 | 20.776 | 1098 / 0 | 6.045 (5.229) |
| translucent_move_oit | plain, one multi-draw per region and layer | 16.62 | 265.46 | 16 (vanilla 28) | 773120 | 6.484 / 0.097 | 12.511 / 28.0 | 20.645 | 2430 / 0 | 7.912 (19.716) |
| flat_plants | plain, one multi-draw per region and layer | 17.046 | 287.776 | 16 (vanilla 28) | 861748 | 6.289 / 0.01 | 13.784 / 24.0 | 23.011 | 943 / 0 | None (None) |
| renderer_off | plain, one multi-draw per region and layer | 13.022 | 206.348 | 16 (vanilla 28) | 546156 | 2.444 / 0.021 | 8.69 / 24.0 | 14.584 | 604 / 0 | None (None) |
| particles | plain, one multi-draw per region and layer | 2.003 | 12.02 | 16 (vanilla 28) | 8416 | 21.028 / 0.131 | 0.131 / 28.0 | 0.225 | 6075 / 0 | None (None) |
| animation | plain, one multi-draw per region and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 20.855 / 0.129 | 0.302 / 28.0 | 0.518 | 6177 / 0 | 1.503 (2.239) |
| animated_items | plain, one multi-draw per region and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 20.795 / 0.13 | 0.153 / 28.0 | 0.261 | 6226 / 0 | 1.057 (1.515) |

Translucency, core old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 154.0 | 17.0 | 0.0 | 0 | 0 | 55 (0.026 / 0.039 / 0.054; 0) | 0 | 34650 / 0 / 0 |
| flythrough | 184.631 | 26.308 | 0.0 | 0 | 0 | 851 (0.026 / 0.053 / 5.035; 0) | 45 | 24002 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1632 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1200 / 0 / 0 |
| occlusion_move | 2.0 | 2.0 | 0.0 | 0 | 0 | 36 (0.009 / 0.027 / 0.029; 0) | 32 | 1196 / 0 / 0 |
| translucent_move | 7.0 | 2.0 | 0.0 | 0 | 0 | 180 (0.104 / 0.161 / 0.21; 0) | 180 | 2639 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 600 | 0 (None / None / None; 0) | 0 | 800 / 0 / 0 |
| translucent_move_oit | 7.0 | 6.0 | 0.0 | 0 | 564 | 0 (None / None / None; 0) | 0 | 1316 / 0 / 0 |
| flat_plants | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1484 / 0 / 0 |
| renderer_off | 4.0 | 2.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2372 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 349.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.032 (0.182; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 429.8 | 1.0 (0.0, 0.0) | 1.269 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 141.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.017 (0.049; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.15 (0.352; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 49.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.066 (0.117; 0) | 0 | 3600 / 1629600 / 0 / 0 |
| occlusion_move | 51.351 | 10.0 (9.0, 2.366) | 6.0 (6.0, 2.281) | 0.068 (0.216; 0) | 0 | 2779 / 1505132 / 0 / 0 |
| translucent_move | 136.064 | 1.0 (0.0, 0.0) | 0.767 (0.0, 0.0) | 0.0 (0.024; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 141.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 136.878 | 1.0 (0.0, 0.0) | 0.745 (0.0, 0.0) | 0.001 (0.004; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.036; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.002 (0.029; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.008 (0.037; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.441 / 0.094 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 83.738 / 0.093 | 0.026 / 0.01 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.207 / 0.0 | 0.005 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.815 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.8 / 53.797 | 0.015 / 0.028 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.794 / 0.982 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 42.313 / 8.655 | 0.072 / 0.012 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 107.19 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.002 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.603 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 10.778 / 10.714 | 0.005 / 0.013 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 59.903 / 59.93 | 0.017 / 0.028 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 12.766 / 12.809 | 0.004 / 0.01 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 10.0 | 0.916 / 0.973 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.433 / 2.294 | 0.017 / 0.011 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 88.292 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.667 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.004 / 0.038 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.287 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.003 / 0.008 | 56 (3) | 40.58 / 9.245 / 31.335 | 3 / 0 | 10.988 |
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
| spawn | 13.0 (base 14.05) | 14 (base 15) | 25520 (base 27764) | 249.79 (base 257.2) | 4.23 (base 3.98) |
| flythrough | 13.04 (reported, not gated) | 41 (reported, not gated) | 907068 (reported, not gated) | 303.62 (base 318.06) | 3.46 (base 4.23) |
| water_glass | 9.0 (base 9.0) | 15 (base 15) | 15844 (base 15108) | 233.43 (base 244.39) | 4.23 (base 4.23) |
| nether | 15.89 (base 15.71) | 12 (base 12) | 21044 (base 19912) | 254.47 (base 268.95) | 4.23 (base 4.23) |
| mobs | 19.84 (base 19.97) | 17 (base 17) | 616288 (base 1008176) | 253.03 (base 264.31) | 4.23 (base 6.23) |
| outline | 10.72 (base 10.67) | 18 (base 18) | 6300 (base 6356) | 252.56 (base 264.58) | 4.23 (base 3.71) |
| occlusion | 15.67 (base 15.67) | 15 (base 15) | 100576 (base 126356) | 258.3 (base 264.78) | 4.23 (base 3.98) |
| occlusion_move | 16.06 (reported, not gated) | 15 (reported, not gated) | 82468 (reported, not gated) | 258.87 (base 265.16) | 4.23 (base 4.23) |
| translucent_move | 8.99 (reported, not gated) | 15 (reported, not gated) | 10800 (reported, not gated) | 260.64 (base 268.35) | 4.23 (base 5.46) |
| water_glass_oit | 14.0 (base 14.0) | 15 (base 15) | 15284 (base 15024) | 232.52 (base 245.99) | 3.59 (base 3.46) |
| translucent_move_oit | 14.0 (reported, not gated) | 15 (reported, not gated) | 11024 (reported, not gated) | 234.98 (base 248.38) | 3.71 (base 3.46) |
| flat_plants | 18.55 (base 18.96) | 15 (base 15) | 9996 (base 9864) | 235.56 (base 248.07) | 5.46 (base 5.98) |
| renderer_off | 7.68 (base 7.83) | 14 (base 14) | 7372 (base 7180) | 228.85 (base 241.13) | 3.34 (base 4.1) |
| particles | 9.67 (base 9.67) | 15 (base 15) | 9204 (base 17128) | 255.74 (base 261.4) | 4.23 (base 5.98) |
| animation | 49 median (avg 39.98) (base 49) | 15 (base 15) | 2224 (base 2056) | 253.23 (base 260.68) | 4.23 (base 5.98) |
| animated_items | 60 median (avg 46.77) (base 60) | 23 (base 23) | 21976 (base 21940) | 228.45 (base 239.74) | 4.23 (base 5.98) |

Draw calls against vanilla measured in the same run (measure-vk), core vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|
| spawn | 13.0 / 13 (10.15) | 123.17 / 123 (9.56) | median and average, x 1.10 + 2 |
| flythrough | 13.04 / 13 (6.27) | 123.07 / 123 (5.52) | median and average, at most vanilla + 2 |
| water_glass | 9.0 / 9 (18.8) | 124.0 / 124 (15.17) | median, at most vanilla |
| nether | 15.89 / 16 (18.48) | 120.68 / 121 (15.47) | median, x 1.10 + 2 |
| mobs | 19.84 / 20 (23.65) | 134.0 / 134 (18.86) | median, x 1.10 + 2 |
| outline | 10.72 / 11 (29.94) | 87.33 / 126 (30.0) | median, x 1.10 + 2 |
| occlusion | 15.67 / 16 (29.98) | 94.33 / 133 (29.98) | median and average, x 1.10 + 2 |
| occlusion_move | 16.06 / 17 (29.94) | 94.33 / 133 (29.95) | median and average, x 1.10 + 2 |
| translucent_move | 8.99 / 9 (19.27) | 124.0 / 124 (16.2) | median, at most vanilla |
| water_glass_oit | 14.0 / 14 (9.49) | 129.0 / 129 (8.53) | median, at most vanilla |
| translucent_move_oit | 14.0 / 14 (8.83) | 129.0 / 129 (8.08) | median and average, at most vanilla |
| flat_plants | 18.55 / 19 (19.8) | 124.0 / 124 (16.22) | median, x 1.10 + 2 |
| renderer_off | 7.68 / 8 (29.47) | 106.97 / 123 (23.21) | median, x 1.10 + 2 |
| particles | 9.67 / 10 (30.0) | 86.27 / 125 (30.0) | median and average, x 1.10 + 2 |
| animation | 39.98 / 49 (30.0) | 144.46 / 179 (30.0) | median, x 1.10 + 2 |
| animated_items | 46.77 / 60 (30.0) | 155.22 / 190 (30.01) | median, x 1.10 + 2 |

Terrain GPU memory, core vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|
| spawn | 36.0 / 23.04 | 36.0 / 23.04 | 0.0 / 0.0 | 416.0 / 37.109 |
| flythrough | 112.0 / 84.587 | 112.0 / 84.587 | 0.0 / 0.0 | 416.0 / 142.865 |
| water_glass | 36.0 / 12.548 | 36.0 / 12.548 | 0.0 / 0.0 | 416.0 / 21.838 |
| nether | 20.0 / 15.135 | 20.0 / 15.135 | 0.0 / 0.0 | 256.0 / 26.576 |
| mobs | 16.0 / 9.91 | 16.0 / 9.91 | 0.0 / 0.0 | 256.0 / 17.319 |
| outline | 16.0 / 1.46 | 16.0 / 1.46 | 0.0 / 0.0 | 256.0 / 2.533 |
| occlusion | 20.0 / 2.595 | 20.0 / 2.595 | 0.0 / 0.0 | 416.0 / 4.259 |
| occlusion_move | 20.0 / 3.43 | 20.0 / 3.43 | 0.0 / 0.0 | 416.0 / 5.774 |
| translucent_move | 28.0 / 12.841 | 28.0 / 12.841 | 0.0 / 0.0 | 416.0 / 22.098 |
| water_glass_oit | 20.0 / 12.548 | 20.0 / 12.548 | 0.0 / 0.0 | 416.0 / 21.838 |
| translucent_move_oit | 28.0 / 12.485 | 28.0 / 12.485 | 0.0 / 0.0 | 416.0 / 21.475 |
| flat_plants | 24.0 / 13.83 | 24.0 / 13.83 | 0.0 / 0.0 | 416.0 / 23.973 |
| renderer_off | 24.0 / 8.678 | 24.0 / 8.678 | 0.0 / 0.0 | 416.0 / 15.252 |
| particles | 28.0 / 0.131 | 28.0 / 0.131 | 0.0 / 0.0 | 128.0 / 0.235 |
| animation | 28.0 / 0.302 | 28.0 / 0.302 | 0.0 / 0.0 | 256.0 / 0.545 |
| animated_items | 28.0 / 0.153 | 28.0 / 0.153 | 0.0 / 0.0 | 128.0 / 0.272 |


Package 4b, core vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|
| spawn | 520177.792 (699571.149) | 25.643 | 1.154 | 529200 / 583028 | 503680 (2097152.0) | 202 / 191 | 0.0 / 0 |
| flythrough | 552196.928 (883865.712) | 37.525 | 1.084 | 2121700 / 7156592 | 2096040 (2097152.0) | 125 / 110 | 7.627 / 50 |
| water_glass | 217512.491 (390053.376) | 44.235 | 1.116 | 175620 / 16716 | 158656 (2097152.0) | 375 / 303 | 0.0 / 0 |
| nether | 318172.379 (476969.561) | 33.293 | 1.272 | 318948 / 359180 | 296320 (2097152.0) | 369 / 309 | 0.0 / 0 |
| mobs | 170748.228 (309539.036) | 44.838 | 0.99 | 616288 / 616328 | 0 (2097152.0) | 12 / 377 | 0.0 / 0 |
| outline | 26864.849 (45702.298) | 41.218 | 1.0 | 8340 / 6300 | 0 (2097152.0) | 1 / 0 | 0.0 / 0 |
| occlusion | 46292.187 (81369.713) | 43.109 | 1.203 | 474756 / 650448 | 374272 (2097152.0) | 0 / 0 | 0.0 / 0 |
| occlusion_move | 51014.221 (89796.87) | 43.189 | 1.229 | 667456 / 1155272 | 590784 (2097152.0) | 0 / 0 | 0.0 / 0 |
| translucent_move | 198322.026 (351368.545) | 43.557 | 1.184 | 904952 / 1574836 | 893760 (2097152.0) | 312 / 324 | 0.0 / 0 |
| water_glass_oit | 218081.799 (391074.286) | 44.235 | 1.116 | 18756 / 16828 | 0 (2097152.0) | 189 / 170 | 0.0 / 0 |
| translucent_move_oit | 198503.636 (351367.693) | 43.505 | 1.182 | 904796 / 1574444 | 893760 (2097152.0) | 176 / 161 | 0.0 / 0 |
| flat_plants | 236719.777 (433340.294) | 45.373 | 1.056 | 10444 / 9956 | 0 (2097152.0) | 102 / 324 | 0.0 / 0 |
| renderer_off | 150552.742 (273248.349) | 44.903 | 1.019 | 7372 / 7292 | 0 (2097152.0) | 0 / 5 | 0.0 / 0 |
| particles | 1927.201 (4215.002) | 54.278 | 1.0 | 9204 / 9204 | 0 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animation | 3217.743 (5631.766) | 42.864 | 1.071 | 86740 / 150176 | 84608 (2097152.0) | 0 / 0 | 0.0 / 0 |
| animated_items | 2271.786 (4890.15) | 53.544 | 1.0 | 79704 / 123000 | 57728 (2097152.0) | 5 / 0 | 0.0 / 0 |

Terrain module, core vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|
| spawn | indirect, one multi-draw per arena and layer | 3.015 | 806.975 | 16 (vanilla 28) | 1392520 | 41.157 / 0.108 | 23.04 / 36.0 | 37.184 | 2375 / 0 | 3.518 (5.286) |
| flythrough | indirect, one multi-draw per arena and layer | 3.024 | 890.056 | 16 (vanilla 28) | 5121808 | 15.439 / 1.01 | 84.587 / 112.0 | 136.767 | 6445 / 0 | 8.149 (20.046) |
| water_glass | indirect, one multi-draw per arena and layer | 3.008 | 276.736 | 16 (vanilla 28) | 778032 | 33.42 / 0.094 | 12.548 / 36.0 | 20.776 | 3040 / 0 | 3.702 (3.172) |
| nether | indirect, one multi-draw per arena and layer | 2.005 | 471.274 | 16 (vanilla 28) | 951432 | 27.935 / 0.171 | 15.135 / 20.0 | 25.406 | 4102 / 0 | 3.408 (4.342) |
| mobs | indirect, one multi-draw per arena and layer | 2.004 | 197.416 | 16 (vanilla 28) | 617772 | 25.265 / 0.148 | 9.91 / 16.0 | 16.496 | 4548 / 0 | None (None) |
| outline | indirect, one multi-draw per arena and layer | 2.003 | 42.07 | 16 (vanilla 28) | 91252 | 24.807 / 0.148 | 1.46 / 16.0 | 2.437 | 4641 / 0 | None (None) |
| occlusion | indirect, one multi-draw per arena and layer | 3.005 | 69.115 | 16 (vanilla 28) | 162472 | 23.7 / 0.141 | 2.595 / 20.0 | 4.338 | 4874 / 0 | 6.866 (6.681) |
| occlusion_move | indirect, one multi-draw per arena and layer | 3.005 | 74.452 | 16 (vanilla 28) | 214972 | 22.456 / 0.133 | 3.43 / 20.0 | 5.74 | 5155 / 0 | 5.51 (15.155) |
| translucent_move | indirect, one multi-draw per arena and layer | 3.008 | 247.294 | 16 (vanilla 28) | 793672 | 19.643 / 0.123 | 12.841 / 28.0 | 21.193 | 5909 / 0 | 5.582 (10.452) |
| water_glass_oit | indirect, one multi-draw per arena and layer | 5.026 | 285.503 | 16 (vanilla 28) | 778032 | 7.449 / 0.043 | 12.548 / 20.0 | 20.776 | 1094 / 0 | None (None) |
| translucent_move_oit | indirect, one multi-draw per arena and layer | 5.028 | 262.42 | 16 (vanilla 28) | 771096 | 6.592 / 0.106 | 12.485 / 28.0 | 20.59 | 2425 / 0 | 5.753 (11.541) |
| flat_plants | indirect, one multi-draw per arena and layer | 3.008 | 286.724 | 16 (vanilla 28) | 864492 | 6.367 / 0.01 | 13.83 / 24.0 | 23.084 | 940 / 0 | None (None) |
| renderer_off | indirect, one multi-draw per arena and layer | 3.005 | 206.349 | 16 (vanilla 28) | 545572 | 2.422 / 0.021 | 8.678 / 24.0 | 14.568 | 604 / 0 | None (None) |
| particles | indirect, one multi-draw per arena and layer | 1.002 | 12.02 | 16 (vanilla 28) | 8416 | 19.587 / 0.122 | 0.131 / 28.0 | 0.225 | 5955 / 0 | None (None) |
| animation | indirect, one multi-draw per arena and layer | 2.003 | 14.023 | 16 (vanilla 28) | 19416 | 19.422 / 0.121 | 0.302 / 28.0 | 0.518 | 6057 / 0 | 1.753 (2.456) |
| animated_items | indirect, one multi-draw per arena and layer | 1.002 | 13.022 | 16 (vanilla 28) | 9764 | 19.365 / 0.122 | 0.153 / 28.0 | 0.261 | 6106 / 0 | 1.363 (2.044) |

Translucency, core vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|
| spawn | 156.0 | 1.0 | 0.0 | 0 | 0 | 42 (0.028 / 0.039 / 0.058; 0) | 0 | 31668 / 0 / 0 |
| flythrough | 185.865 | 1.0 | 0.0 | 0 | 0 | 851 (0.034 / 0.048 / 8.054; 0) | 39 | 23419 / 0 / 0 |
| water_glass | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1504 / 0 / 0 |
| nether | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| mobs | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| outline | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| occlusion | 2.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1202 / 0 / 0 |
| occlusion_move | 2.0 | 1.0 | 0.0 | 0 | 0 | 36 (0.007 / 0.01 / 0.011; 0) | 32 | 1198 / 0 / 0 |
| translucent_move | 7.0 | 1.0 | 0.0 | 0 | 0 | 185 (0.114 / 0.189 / 0.446; 0) | 185 | 2702 / 0 / 0 |
| water_glass_oit | 4.0 | 3.0 | 0.0 | 0 | 570 | 0 (None / None / None; 0) | 0 | 760 / 0 / 0 |
| translucent_move_oit | 7.0 | 3.0 | 0.0 | 0 | 531 | 0 (None / None / None; 0) | 0 | 1239 / 0 / 0 |
| flat_plants | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 1584 / 0 / 0 |
| renderer_off | 4.0 | 1.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 2364 / 0 / 0 |
| particles | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animation | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |
| animated_items | 0.0 | 0.0 | 0.0 | 0 | 0 | 0 (None / None / None; 0) | 0 | 0 / 0 / 0 |

Visibility, core vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|
| spawn | 350.0 | 1.0 (0.0, 0.0) | 11.0 (1.0, 0.0) | 0.029 (0.078; 0) | 0 | 0 / 0 / 0 / 0 |
| flythrough | 430.492 | 1.0 (0.0, 0.0) | 1.651 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass | 141.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| nether | 300.0 | 1.0 (0.0, 0.0) | 1.0 (1.0, 0.0) | 0.013 (0.047; 0) | 0 | 0 / 0 / 0 / 0 |
| mobs | 107.0 | 65.0 (64.0, 0.0) | 0.0 (0.0, 0.0) | 0.116 (0.299; 0) | 0 | 0 / 0 / 0 / 0 |
| outline | 28.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.016; 0) | 0 | 0 / 0 / 0 / 0 |
| occlusion | 48.0 | 11.0 (10.0, 2.0) | 9.0 (9.0, 4.0) | 0.061 (0.086; 0) | 0 | 3606 / 1632316 / 0 / 0 |
| occlusion_move | 50.356 | 10.0 (9.0, 2.366) | 6.0 (6.0, 2.28) | 0.057 (0.26; 0) | 0 | 2783 / 1506608 / 0 / 0 |
| translucent_move | 134.137 | 1.0 (0.0, 0.0) | 0.769 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| water_glass_oit | 141.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| translucent_move_oit | 134.853 | 1.0 (0.0, 0.0) | 0.746 (0.0, 0.0) | 0.001 (0.003; 0) | 0 | 0 / 0 / 0 / 0 |
| flat_plants | 154.0 | 1.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.021; 0) | 0 | 0 / 0 / 0 / 0 |
| renderer_off | 112.0 | 2.0 (0.0, 0.0) | 1.0 (0.0, 0.0) | 0.001 (0.002; 0) | 0 | 0 / 0 / 0 / 0 |
| particles | 12.0 | 1.0 (0.0, 0.0) | 0.0 (0.0, 0.0) | 0.0 (0.0; 0) | 0 | 0 / 0 / 0 / 0 |
| animation | 13.0 | 1.0 (0.0, 0.0) | 2.595 (0.0, 0.0) | 0.0 (0.001; 0) | 0 | 0 / 0 / 0 / 0 |
| animated_items | 13.0 | 4.0 (1.0, 0.0) | 0.0 (0.0, 0.0) | 0.005 (0.012; 0) | 0 | 0 / 0 / 0 / 0 |

Effects, core vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|
| spawn | 115.0 / 5.0 | 0.173 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 101.683 / 0.0 | 0.022 / 0.004 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.0 |
| flythrough | 115.0 / 5.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.002 / 0.002 | 56 (0) | 23.0 / 1.0 / 22.0 | 0 / 0 | 10.825 |
| water_glass | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 53.105 / 53.043 | 0.012 / 0.016 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| nether | 115.0 / 10.0 | 0.68 / 0.886 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 30.9 / 8.849 | 0.054 / 0.008 | 56 (1) | 23.0 / 2.0 / 21.0 | 0 / 0 | 7.0 |
| mobs | 115.0 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.003 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| outline | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| occlusion | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| occlusion_move | 76.667 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.001 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 11.434 / 11.415 | 0.005 / 0.012 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| water_glass_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 51.62 / 51.537 | 0.01 / 0.018 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| translucent_move_oit | 115.0 / 0.0 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 13.416 / 13.429 | 0.005 / 0.011 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 2.0 |
| flat_plants | 115.0 / 9.949 | 1.0 / 0.61 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 1.177 / 1.03 | 0.01 / 0.006 | 56 (5) | 23.0 / 2.0 / 21.0 | 0 / 0 | 6.0 |
| renderer_off | 99.104 / 0.0 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.004 / 0.002 | 56 (0) | 23.0 / 0.0 / 23.0 | 0 / 0 | 3.0 |
| particles | 76.603 / 0.0 | 4.0 / 4.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 66.0 / 66.0 | 0.004 / 0.029 | 56 (1) | 23.0 / 0.0 / 23.0 | 0 / 0 | 1.0 |
| animation | 136.793 / 32.311 | 1.0 / 1.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 2.8 / 2.798 | 0.002 / 0.005 | 56 (3) | 40.608 / 9.248 / 31.36 | 3 / 0 | 10.988 |
| animated_items | 136.553 / 28.104 | 0.0 / 0.0 | 3.0 / 3.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.0 / 0.0 | 0.001 / 0.0 | 56 (9) | 40.608 / 7.99 / 32.618 | 0 / 0 | 9.0 |

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

- vanilla-a: backend `?` drivers `?`
- vanilla-b: backend `?` drivers `?`
- vanilla-old: backend `?` drivers `?`
- vanilla-vk: backend `?` drivers `?`
- quicksilver-full: backend `?` drivers `?`
- quicksilver-old: backend `?` drivers `?`
- quicksilver-vk: backend `?` drivers `?`
- measure-full: backend `?` drivers `?`
- measure-old: backend `?` drivers `?`
- measure-vk: backend `?` drivers `?`

GPU error messages in the client logs (Quicksilver run / vanilla run of the same profile):

- full: 0 / 0
- old-gpu: 0 / 0
- vulkan: 0 / 0

| scene | noise (vanilla a/b) % | limit % | full % | old-GPU % | Vulkan % |
|---|---|---|---|---|---|
| factory | missing | 0.3 | missing | missing | missing |
| factory_flat | missing | 0.3 | missing | missing | missing |
| sift | missing | 1.15 | missing | missing | missing |
| hub | missing | 0.25 | missing | missing | missing |
| mam | missing | 0.25 | missing | missing | missing |
| ponder | missing | 0.25 | missing | missing | missing |
| map | skipped: MapMod is being finished (Andre, 2026-10-30): the map scene is out of the bench and CI until then; with it, its limits are re-collected | | | | |
| hologram | missing | 0.55 | missing | missing | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) |
| hud | skipped: MapMod is being finished (Andre, 2026-10-30): the HUD scene (its minimap) is out of the bench and CI until then; with it, its limits are re-collected | | | | |
| create_ct | missing | 0.25 | missing | missing | missing |

Strict picture: pixels where all four vanilla runs agree exactly but the Quicksilver run differs by more than 2/255 (in brackets: differs at all, reported only):

| scene | vanilla runs agree on % of pixels | strict limit (pixels) | full | old-GPU | Vulkan |
|---|---|---|---|---|---|
| factory | None | 500 | None (None) | None (None) | None (None) |
| factory_flat | None | 40 | None (None) | None (None) | None (None) |
| sift | None | 100 | None (None) | None (None) | None (None) |
| hub | None | 10 | None (None) | None (None) | None (None) |
| mam | None | 10 | None (None) | None (None) | None (None) |
| ponder | None | 10 | None (None) | None (None) | None (None) |
| hologram | None | 413 | None (None) | None (None) | None (None) |
| create_ct | None | 10 | None (None) | None (None) | None (None) |

Measure runs (Quicksilver loaded, every module off) against vanilla's run of the same profile: % of pixels over 8/255 / strict pixels over 2/255; the same limits as Quicksilver's runs:

| scene | full | old-GPU | Vulkan |
|---|---|---|---|
| factory | missing | missing | missing |
| factory_flat | missing | missing | missing |
| sift | missing | missing | missing |
| hub | missing | missing | missing |
| mam | missing | missing | missing |
| ponder | missing | missing | missing |
| hologram | missing | missing | skipped |
| create_ct | missing | missing | missing |

### Counts, siftec full

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|

Draw calls against vanilla measured in the same run (measure-full), siftec full: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|

Terrain GPU memory, siftec full (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|


Package 4b, siftec full: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-full:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|

Terrain module, siftec full (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|

Translucency, siftec full (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|

Visibility, siftec full (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|

Effects, siftec full (reported; draw calls per frame by what they draw for; vanilla = measure-full):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|

GUI pass, siftec full (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|

### Counts, siftec old-gpu

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|

Draw calls against vanilla measured in the same run (measure-old), siftec old-gpu: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|

Terrain GPU memory, siftec old-gpu (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|


Package 4b, siftec old-gpu: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-old:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|

Terrain module, siftec old-gpu (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|

Translucency, siftec old-gpu (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|

Visibility, siftec old-gpu (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|

Effects, siftec old-gpu (reported; draw calls per frame by what they draw for; vanilla = measure-old):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|

GUI pass, siftec old-gpu (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|

### Counts, siftec vulkan

| scene | draw calls/frame (avg, or median where marked) | uploads/frame (median) | upload bytes/frame (median) | heap after GC MB | direct MB |
|---|---|---|---|---|---|
| hologram | skipped (Mesa's lavapipe Vulkan driver segfaults drawing Create's outliner (the hologram), in vanilla's run too) | | | | |

Draw calls against vanilla measured in the same run (measure-vk), siftec vulkan: average and median per frame; translucent scenes (ci/vanilla-draw-calls.json) at most vanilla's, the others at most vanilla's x 1.10 + 2:

| scene | Quicksilver avg / median (fps) | vanilla avg / median (fps) | gated on |
|---|---|---|---|

Terrain GPU memory, siftec vulkan (MB; reserved in the Quicksilver run must not be more than vanilla's):

| scene | Quicksilver run reserved / used | of which Quicksilver's arenas | of which vanilla's heaps | vanilla (measure run) reserved / used |
|---|---|---|---|---|


Package 4b, siftec vulkan: Quicksilver's terrain triangles per frame (with every face, as before 4b), and the largest upload bytes in one frame (all of the frame's uploads), frames over 50 ms and sections waiting; vanilla from measure-vk:

| scene | triangles/frame (every face) | left out % | ranges per section layer | largest upload bytes in one frame: Quicksilver / vanilla | Quicksilver terrain bytes, largest frame (budget avg) | frames over 50 ms: Quicksilver / vanilla | sections waiting avg / max |
|---|---|---|---|---|---|---|---|

Terrain module, siftec vulkan (reported, not gated):

| scene | path | Quicksilver draws/frame | section layers/frame | bytes per vertex | live vertices | % vertices with a position / UV rounded | arena MB used / allocated | vanilla format would need MB | layers taken / kept by vanilla | meshing ms/section (vanilla mesher p95) |
|---|---|---|---|---|---|---|---|---|---|---|

Translucency, siftec vulkan (package 3; the check, mixed frames and dry run gated above):

| scene | Quicksilver's translucent section layers/frame | its draws/frame | vanilla's own layers/frame | mixed frames | OIT stage draws | sorts (ms avg / p95 / max; on render thread) | new index lists uploaded | check: entries / unchecked / misses |
|---|---|---|---|---|---|---|---|---|

Visibility, siftec vulkan (reported; pop-in and fallbacks gated above):

| scene | sections visible/frame | entities/frame (tested, hidden) | block entities/frame (tested, hidden) | cull ms/frame (max; frames over budget) | section layers missing | ray check: objects / rays / violations / unchecked |
|---|---|---|---|---|---|---|

Effects, siftec vulkan (reported; draw calls per frame by what they draw for; vanilla = measure-vk):

| scene | animation draws: vanilla / Quicksilver | particles | sky | weather | clouds | particles alive / drawn per frame | particle tick ms / extract ms | animated sprites (used outside terrain) | per tick changed / redrawn / left out | caught up / late | seen per frame |
|---|---|---|---|---|---|---|---|---|---|---|---|

GUI pass, siftec vulkan (package 7, reported): draw calls/frame of the game's GUI pass (preparation: item atlas, picture-in-picture), elements, distinct states (fewest possible draws), draws if reordered without changing the picture (text counted as overlapping everything; in brackets as overlapping nothing, an upper bound); Quicksilver / vanilla (measure run):

| scene | GUI draws/frame | preparation draws/frame | elements/frame | states/frame | reordered draws/frame |
|---|---|---|---|---|---|

## Bench-only jar (quicksilver-bench)

| run | scene | fps avg | frame time p99 ms | frames over 50 ms | draw calls/frame (median) | largest upload bytes in one frame | counters missing |
|---|---|---|---|---|---|---|---|
| bench-plain | spawn | 15.31 | 73.32 | 306 | 123 | 588876 | [] |
| bench-plain | flythrough | 10.17 | 224.68 | 203 | 123 | 7388648 | [] |
| bench-plain | water_glass | 26.52 | 43.73 | 2 | 124 | 17836 | [] |
| bench-plain | nether | 25.45 | 48.46 | 4 | 121 | 360076 | [] |
| bench-sodium | spawn | 10.95 | 111.79 | 219 | 147 | 266928 | [] |
| bench-sodium | flythrough | 7.23 | 298.04 | 144 | 158 | 266456 | [] |
| bench-sodium | water_glass | 19.86 | 71.5 | 136 | 135 | 9788 | [] |
| bench-sodium | nether | 27.52 | 45.54 | 1 | 129 | 263776 | [] |

## Direct buffers per scene (reported; open bug, see docs/progress.md)

| run | scene | start MB | peak MB (tick) | end MB | rise |
|---|---|---|---|---|---|
| core-bench-plain | spawn | 4.24 | 4.24 (0) | 4.24 |  |
| core-bench-plain | water_glass | 4.24 | 4.24 (0) | 4.24 |  |
| core-bench-plain | nether | 4.24 | 4.24 (0) | 4.24 |  |
| core-bench-plain | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-bench-sodium | spawn | 4.24 | 4.24 (0) | 4.24 |  |
| core-bench-sodium | water_glass | 4.24 | 4.24 (0) | 4.24 |  |
| core-bench-sodium | nether | 4.12 | 4.12 (0) | 4.12 |  |
| core-bench-sodium | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-measure-full | spawn | 3.99 | 3.99 (0) | 3.99 |  |
| core-measure-full | water_glass | 3.99 | 3.99 (0) | 3.99 |  |
| core-measure-full | nether | 3.99 | 3.99 (0) | 3.99 |  |
| core-measure-full | mobs | 3.99 | 3.99 (0) | 3.99 |  |
| core-measure-full | outline | 3.99 | 3.99 (0) | 3.99 |  |
| core-measure-full | occlusion | 3.99 | 3.99 (0) | 3.99 |  |
| core-measure-full | occlusion_move | 3.99 | 3.99 (0) | 3.99 |  |
| core-measure-full | translucent_move | 5.99 | 5.99 (0) | 5.99 |  |
| core-measure-full | particles | 5.99 | 5.99 (0) | 5.99 |  |
| core-measure-full | animation | 5.99 | 5.99 (0) | 5.99 |  |
| core-measure-full | animated_items | 5.99 | 5.99 (0) | 5.99 |  |
| core-measure-full | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-measure-full | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
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
| core-measure-old | flat_plants | 3.6 | 3.6 (0) | 3.6 |  |
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
| core-measure-vk | flat_plants | 3.59 | 3.59 (0) | 3.59 |  |
| core-measure-vk | renderer_off | 3.34 | 3.34 (0) | 3.34 |  |
| core-measure-vk | water_glass_oit | 3.34 | 3.34 (0) | 3.34 |  |
| core-measure-vk | translucent_move_oit | 3.46 | 3.46 (0) | 3.46 |  |
| core-measure-vk | flythrough | 3.46 | 3.46 (0) | 3.46 |  |
| core-quicksilver-full | spawn | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | water_glass | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | nether | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | mobs | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | outline | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | occlusion | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | occlusion_move | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | translucent_move | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | particles | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | animation | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | animated_items | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-full | flat_plants | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | renderer_off | 3.35 | 3.35 (0) | 3.35 |  |
| core-quicksilver-full | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-full | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-full | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | spawn | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | water_glass | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | nether | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | mobs | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | outline | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | occlusion | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | occlusion_move | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | translucent_move | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | particles | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | animation | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | animated_items | 4.24 | 4.24 (0) | 4.24 |  |
| core-quicksilver-old | flat_plants | 3.48 | 3.48 (0) | 3.48 |  |
| core-quicksilver-old | renderer_off | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | water_glass_oit | 3.6 | 3.6 (0) | 3.6 |  |
| core-quicksilver-old | translucent_move_oit | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-old | flythrough | 3.73 | 3.73 (0) | 3.73 |  |
| core-quicksilver-vk | spawn | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | water_glass | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | nether | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | mobs | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | outline | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | occlusion | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | occlusion_move | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | translucent_move | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | particles | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | animation | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | animated_items | 4.23 | 4.23 (0) | 4.23 |  |
| core-quicksilver-vk | flat_plants | 5.46 | 5.46 (0) | 5.46 |  |
| core-quicksilver-vk | renderer_off | 3.34 | 3.34 (0) | 3.34 |  |
| core-quicksilver-vk | water_glass_oit | 3.59 | 3.59 (0) | 3.59 |  |
| core-quicksilver-vk | translucent_move_oit | 3.71 | 3.71 (0) | 3.71 |  |
| core-quicksilver-vk | flythrough | 3.46 | 3.46 (0) | 3.46 |  |

Rises of 4 MB or more: none

