#version 330
#extension GL_ARB_separate_shader_objects : require

// Brimstone terrain, vertex stage. Feeds the game's own terrain fragment stage, so what it hands over (locations
// 0-4 below) and the uniform blocks both stages share are fixed by that stage. Two vertex layouts are read:
// BRIM_WIDE (the game's 28-byte layout plus a section slot) and, by default, Brimstone's 16-byte one. Every value
// handed on must be bit-identical to what the game computes for the same block: the fog distances and the lightmap
// lookup are the game's own functions (its include files, used by reference), and the remaining operations keep the
// game's operand order where floating point would otherwise round differently ("order" below). Brimstone's own code
// is only what its data needs: decoding the 16-byte vertex and finding the section's position.

#include <minecraft:fog.glsl>
#include <minecraft:globals.glsl>
#include <minecraft:projection.glsl>
#include <minecraft:sample_lightmap.glsl>
#include <minecraft:terrainglobals.glsl>

uniform sampler2D Sampler2;

#ifdef BRIM_WIDE
layout(location = 0) in vec3 BrimCorner;
layout(location = 1) in vec4 BrimShade;
layout(location = 2) in vec2 BrimAtlasUv;
layout(location = 3) in ivec2 BrimLight;
layout(location = 4) in uvec2 BrimSlot;
#else
layout(location = 0) in uvec4 BrimCell;
layout(location = 1) in vec4 BrimTint;
layout(location = 2) in uvec2 BrimTexel;
#endif

#ifdef BRIMSTONE_PLAIN
layout(std140) uniform BrimRegion {
    ivec4 brimRegionCorner;
    vec4 brimFade[64];
};
#else
// per-section data from the instance stream (binding 1), whose locations follow the vertex's own attributes
#ifdef BRIM_WIDE
layout(location = 5) in ivec3 ChunkPosition;
layout(location = 6) in float ChunkVisibility;
#else
layout(location = 3) in ivec3 ChunkPosition;
layout(location = 4) in float ChunkVisibility;
#endif
#endif

layout(location = 0) out float brimEyeDistance;
layout(location = 1) out float brimColumnDistance;
layout(location = 2) out vec4 brimLitTint;
layout(location = 3) out vec2 brimTexel;
layout(location = 4) out float brimFadeIn;

void main() {
    vec3 local;
    vec4 tint;
    ivec2 lightPair;
    vec2 texel;
    uint slot;
#ifdef BRIM_WIDE
    local = BrimCorner;
    tint = BrimShade;
    lightPair = BrimLight;
    texel = BrimAtlasUv;
    slot = BrimSlot.x;
#else
    local = vec3(BrimCell.xyz) * (1.0 / 2048.0) - 8.0;
    tint = vec4(BrimTint.rgb, 1.0);
    lightPair = ivec2(int(BrimCell.w >> 8u), int(round(BrimTint.a * 255.0)));
    // 65535 is the atlas edge, exactly 1.0
    texel = mix(vec2(BrimTexel) * (1.0 / 65536.0), vec2(1.0), equal(BrimTexel, uvec2(65535u)));
    slot = BrimCell.w & 255u;
#endif

    ivec3 corner;
    float fade;
#ifdef BRIMSTONE_PLAIN
    int s = int(slot);
    corner = brimRegionCorner.xyz + 16 * ivec3(s & 7, (s >> 3) & 3, s >> 5);
    fade = brimFade[s >> 2][s & 3];
#else
    corner = ChunkPosition;
    fade = ChunkVisibility;
#endif

    // order: the whole-block offset is added to the in-section position first, the camera's fraction last
    vec3 eye = (local + vec3(corner - CameraBlockPos)) + CameraOffset;
    // order: the two matrices are multiplied together before the point, as the game does
    gl_Position = ProjMat * ModelViewMat * vec4(eye, 1.0);

    brimEyeDistance = fog_spherical_distance(eye);
    brimColumnDistance = fog_cylindrical_distance(eye);
    brimLitTint = tint * sample_lightmap(Sampler2, lightPair);
    brimTexel = texel;
    // sections fade in from the fog colour, except within 16 blocks of the camera (full at 32); order: the game's
    const float fullyVisibleWithin = 16.0;
    float reach = length(eye);
    brimFadeIn = mix(1.0, fade, clamp((reach - fullyVisibleWithin) / fullyVisibleWithin, 0.0, 1.0));
}
