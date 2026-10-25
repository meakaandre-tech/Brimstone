# 26.3's graphics backend (package 0 findings)

Facts read from the 26.3 classes (CI's `javap` dump: `.github/dump-signatures`, `.github/dump-bytecode`) and from CI
client logs. No choice is made here.

Where the dumps are (since 2026-10-25): the build job's **`game-dumps` Actions artifact** of that run
(`javap-all.txt.gz`, `bytecode.txt.gz` and the class lists), kept for **1 day**. Nothing of Mojang's goes to the public
`ci-logs` branch (design doc, Rules: Mojang code); `ci-logs` carries only Brimstone's own `build.log`, the client runs
and the compare report. Until then the dumps were published on `ci-logs` under `build/`; that directory was removed
and `ci-logs` republished on 2026-10-25 (GitHub may keep older force-pushed commits fetchable by ID for a while).
Note for chats: the agent workspace cannot download Actions artifacts (their storage host isn't reachable), so a
chat that needs a dump asks Andre to download `game-dumps` from the run and attach it.

## What exists

26.3 moves rendering onto a library in `com.mojang.renderpearl`:

| Layer | Package | What it is |
|---|---|---|
| API | `renderpearl.api.*` | `GpuDevice`, `CommandEncoder`, `RenderPass`, `GpuBuffer`, `TransientMemory`, `DeviceInfo`, `DeviceFeatures` |
| Frontend | `renderpearl.frontend` | `FrontendGpuDevice`, `FrontendCommandEncoder`, `FrontendRenderPass`, `FrontendGpuSurface`: validation, then one call into the backend |
| Backend interface | `renderpearl.backend.api` | `GpuDeviceBackend`, `CommandEncoderBackend`, `RenderPassBackend`, `GpuSurfaceBackend` |
| OpenGL backend | `renderpearl.backend.opengl` (62 classes) | `GlBackend`, `GlDevice`, `GlCommandEncoder`, `GlRenderPass`, `GlBuffer`, `GlTransientMemory`, `GlHeuristics`, `DirectStateAccess`, `BufferStorage` |
| Vulkan backend | `renderpearl.backend.vulkan` (+`checkpoints`, `init`; 56 classes) | `VulkanBackend`, `VulkanDevice`, `VulkanCommandEncoder`, `VulkanRenderPass`, `VulkanGpuBuffer`, `VulkanTransientMemory`, VMA allocator |
| Shaders | `renderpearl.frontend.shaders` | GLSL compiled to SPIR-V (`GlslCompiler`, `SPIRVModule`); the GL backend recompiles for GL (`GlPipelineRecompiler`) |

So: **OpenGL and Vulkan, both shipped.** `com.mojang.blaze3d` keeps `RenderSystem` (holds the `GpuDevice`, `getDevice()`),
the window, vertex formats and render targets.

## How a backend is chosen

- Option `preferredGraphicsBackend` in `options.txt` (`net.minecraft.client.PreferredGraphicsApi`): `default`,
  `opengl` or `vulkan` (Video Settings: "Graphics API").
- `PreferredGraphicsApi.getBackendsToTry()`: `vulkan` tries **Vulkan, then OpenGL**; `default` and `opengl` try
  **OpenGL, then Vulkan**.
- In the `Minecraft` constructor: the launch argument `--graphicsBackend` (`GameConfig.GameData.forcedGraphicsApi`) overrides the option
  ("Graphics backend forced to {} by launch argument"). If the last start-up crashed, the option is reset (to
  `default`, or forced to `opengl`). With `default` it also asks `VulkanBackend.checkBackendAvailable()` up front.
  Then for each backend in order: `loadLibrary()`, `createDevice()`; on failure it logs "Failed to create backend
  {name}" and tries the next. The winner is logged: "Using graphics backend {name}, using drivers: {driver}", then
  the device name and "Using graphics device extensions: ...".
- In SIFTEC's and Brimstone's CI (Mesa llvmpipe, `default`), the result is OpenGL 4.5.

## Minimum OpenGL version: 3.3 core

`GlBackend.createWindow` asks SDL for a 3.3 context (`SDL_GL_CONTEXT_MAJOR_VERSION` 3, `MINOR` 3, core profile,
forward-compatible, sRGB-capable framebuffer); `GlBackend` has `VERSION_MAJOR = 3`, `VERSION_MINOR = 3`. `GlDevice`
reads the version back after creating the context and throws `BackendCreationException(OPENGL_MISSING)` below that.
No GL extension is required on top of 3.3: every one it looks for is optional:

| Extension | Used for in vanilla |
|---|---|
| `GL_ARB_buffer_storage` | `DeviceFeatures.persistentMapping`; `BufferStorage.Immutable` vs `Mutable`; `GlTransientMemory$PersistentMapping` vs `$Fallback` |
| `GL_ARB_multi_draw_indirect`, `GL_ARB_draw_indirect` | `DeviceFeatures.multiDrawIndirect`, `drawIndirect` |
| `GL_ARB_shader_draw_parameters`, `GL_ARB_base_instance` | `shaderDrawParameters`, `nonZeroFirstInstance` |
| `GL_ARB_direct_state_access` | `DirectStateAccess.Core` vs `.Emulated` |
| `GL_ARB_clip_control`, `GL_ARB_vertex_attrib_binding`, `GL_KHR_debug`, `GL_EXT_debug_label`, `GL_ARB_debug_output`, `GL_EXT_texture_filter_anisotropic` | depth range, vertex setup, debug labels, anisotropy |

## Vanilla's own driver heuristics (`GlHeuristics`)

`GlHeuristics` reads `GL_VENDOR`/`GL_RENDERER` and sets `DeviceInfo.hintsAndWorkarounds()`:

- `multiDrawIndirectHasKnownIssues`: renderer contains "intel" and not "arc" (Intel UHD / Iris / HD integrated).
- `anisotropyHasKnownIssues`: AMD.
- `writeToBufferIsSlow`: GL on D3D12 (renderer contains "D3D12").
- `isExplicitDepthRequired`: Apple silicon.
- `couldBeIntelGen7`: Intel HD 2500 / 4000 / "HD Graphics (BYT)" / names ending "HD Graphics".
- `guessDeviceType`: Intel → integrated (Arc → discrete); llvmpipe / "mesa offscreen" → CPU; virtgl → virtual.

## Where later packages hook (all backend-neutral unless noted)

| Purpose | Class and method |
|---|---|
| Draw calls | `FrontendRenderPass`: `drawIndexed`, `drawMultipleIndexed`, `draw`, `multiDrawIndexed` (2), `multiDraw` (2), `drawIndexedIndirect`, `drawIndirect`, each making one `RenderPassBackend` call |
| Buffer and texture writes | `FrontendCommandEncoder.writeToBuffer`, `writeToTexture(GpuTexture, ByteBuffer, mip, layer, x, y, w, h)` (the `NativeImage` overloads end there), `copyToBuffer`, `copyBufferToTexture` |
| Buffer creation | `FrontendGpuDevice.createBuffer` (size or initial data) |
| Per-frame transient memory | `TransientMemory` from `CommandEncoder.transientMemory()`; implementations `GlTransientMemory$PersistentMapping`, `GlTransientMemory$Fallback`, `VulkanTransientMemory` |
| Buffer mapping | `GpuBuffer.map(offset, length, read, write)`; `GlBuffer$Direct`, `VulkanGpuBuffer$Direct` |
| Frame boundary | `FrontendGpuSurface.present` |
| Capabilities | `RenderSystem.getDevice().getDeviceInfo()`: `name`, `vendorName`, `driverInfo`, `backendName`, `features()`, `limits()`, `underlyingExtensions()`, `hintsAndWorkarounds()`, `type()` |
| Pipelines | `FrontendGpuDevice.compilePipeline`, `RenderPass.setPipeline` |

Package 0's counters hook the first five rows and the frame boundary (`mixin/counters`), count only.
