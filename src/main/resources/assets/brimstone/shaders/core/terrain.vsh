#version 330
#extension GL_ARB_separate_shader_objects : require

// Brimstone's terrain vertex shader. It reads Brimstone's 16-byte vertex and hands the fragment stage exactly the
// values vanilla's terrain vertex stage would, so the game's own terrain fragment shader (minecraft:core/terrain)
// draws the pixels. Layout of the vertex: com.meakaandre.brimstone.terrain.CompactVertex.

#include <minecraft:fog.glsl>
#include <minecraft:globals.glsl>
#include <minecraft:projection.glsl>
#include <minecraft:sample_lightmap.glsl>
#include <minecraft:terrainglobals.glsl>

layout(location = 0) in uvec4 BrimPosition;
layout(location = 1) in vec4 BrimColor;
layout(location = 2) in uvec2 BrimUv;

#ifdef BRIMSTONE_PLAIN
// one block per region of 8x4x8 sections: where the region starts, and each section's fade-in value
layout(std140) uniform BrimRegion {
    ivec4 RegionOrigin;
    vec4 SectionVisibility[64];
};
#else
// per section, from the instance stream (the same data vanilla's multi-draw path uses)
layout(location = 3) in ivec3 ChunkPosition;
layout(location = 4) in float ChunkVisibility;
#endif

uniform sampler2D Sampler2;

layout(location = 0) out float sphericalVertexDistance;
layout(location = 1) out float cylindricalVertexDistance;
layout(location = 2) out vec4 vertexColor;
layout(location = 3) out vec2 texCoord0;
layout(location = 4) out float chunkVisibility;

void main() {
    // 1/2048-block steps from -8: values on the model grid come back bit for bit
    vec3 inSection = vec3(BrimPosition.xyz) * (1.0 / 2048.0) - 8.0;

#ifdef BRIMSTONE_PLAIN
    int slot = int(BrimPosition.w & 255u);
    ivec3 sectionOrigin = RegionOrigin.xyz + ivec3(slot & 7, (slot >> 3) & 3, slot >> 5) * 16;
    float sectionVisibility = SectionVisibility[slot >> 2][slot & 3];
#else
    ivec3 sectionOrigin = ChunkPosition;
    float sectionVisibility = ChunkVisibility;
#endif

    // the same sum, in the same order, as the game's terrain shader: integers first, then the camera's fraction
    vec3 pos = inSection + (sectionOrigin - CameraBlockPos) + CameraOffset;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    sphericalVertexDistance = fog_spherical_distance(pos);
    cylindricalVertexDistance = fog_cylindrical_distance(pos);

    ivec2 light = ivec2(int(BrimPosition.w >> 8u), int(round(BrimColor.a * 255.0)));
    vertexColor = vec4(BrimColor.rgb, 1.0) * sample_lightmap(Sampler2, light);
    // 1/65536 steps; 65535 stands for exactly 1.0 (the atlas edge)
    vec2 uv = vec2(BrimUv) * (1.0 / 65536.0);
    if (BrimUv.x == 65535u) uv.x = 1.0;
    if (BrimUv.y == 65535u) uv.y = 1.0;
    texCoord0 = uv;

    // sections fade in from the fog colour after they appear, except close to the camera
    float dist = length(pos);
    chunkVisibility = mix(1.0, sectionVisibility, clamp((dist - 16.0) / 16.0, 0.0, 1.0));
}
