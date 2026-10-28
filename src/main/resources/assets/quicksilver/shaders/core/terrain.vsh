#version 330
#extension GL_ARB_separate_shader_objects : require

// Quicksilver terrain, vertex stage. Feeds the game's own terrain fragment stage, so what it hands over (locations
// 0-4 below) and the uniform blocks both stages share are fixed by that stage. It reads Quicksilver's 16-byte vertex
// (positions and UVs on its grid; off-grid ones were rounded to it when the vertex was made). From the stored values,
// every value handed on must be bit-identical to what the game computes for the same inputs: the fog distances and the lightmap
// lookup are the game's own functions (its include files, used by reference), and the remaining operations keep the
// game's operand order where floating point would otherwise round differently ("order" below). Quicksilver's own code
// is only what its data needs: decoding the 16-byte vertex and finding the section's position.

#include <minecraft:fog.glsl>
#include <minecraft:globals.glsl>
#include <minecraft:projection.glsl>
#include <minecraft:sample_lightmap.glsl>
#include <minecraft:terrainglobals.glsl>

// the game's Improved Transparency stages that only need coverage (OIT_ALPHA_ONLY) have no lightmap bound
#ifndef OIT_ALPHA_ONLY
uniform sampler2D Sampler2;
#endif

layout(location = 0) in uvec4 QsCell;
layout(location = 1) in vec4 QsTint;
layout(location = 2) in uvec2 QsTexel;

#ifdef QUICKSILVER_PLAIN
layout(std140) uniform QsRegion {
    ivec4 qsRegionCorner;
    vec4 qsFade[64];
};
#else
// per-section data from the instance stream (binding 1), whose locations follow the vertex's own attributes
layout(location = 3) in ivec3 ChunkPosition;
layout(location = 4) in float ChunkVisibility;
#endif

layout(location = 0) out float qsEyeDistance;
layout(location = 1) out float qsColumnDistance;
layout(location = 2) out vec4 qsLitTint;
layout(location = 3) out vec2 qsTexel;
layout(location = 4) out float qsFadeIn;

void main() {
    vec3 local;
    vec4 tint;
    ivec2 lightPair;
    vec2 texel;
    uint slot;
    local = vec3(QsCell.xyz) * (1.0 / 2048.0) - 8.0;
    tint = vec4(QsTint.rgb, 1.0);
    lightPair = ivec2(int(QsCell.w >> 8u), int(round(QsTint.a * 255.0)));
    // 65535 is the atlas edge, exactly 1.0
    texel = mix(vec2(QsTexel) * (1.0 / 65536.0), vec2(1.0), equal(QsTexel, uvec2(65535u)));
    slot = QsCell.w & 255u;

    ivec3 corner;
    float fade;
#ifdef QUICKSILVER_PLAIN
    int s = int(slot);
    corner = qsRegionCorner.xyz + 16 * ivec3(s & 7, (s >> 3) & 3, s >> 5);
    fade = qsFade[s >> 2][s & 3];
#else
    corner = ChunkPosition;
    fade = ChunkVisibility;
#endif

    // order: the whole-block offset is added to the in-section position first, the camera's fraction last
    vec3 eye = (local + vec3(corner - CameraBlockPos)) + CameraOffset;
    // order: the two matrices are multiplied together before the point, as the game does
    gl_Position = ProjMat * ModelViewMat * vec4(eye, 1.0);

    qsEyeDistance = fog_spherical_distance(eye);
    qsColumnDistance = fog_cylindrical_distance(eye);
#ifdef OIT_ALPHA_ONLY
    qsLitTint = tint;
#else
    qsLitTint = tint * sample_lightmap(Sampler2, lightPair);
#endif
    qsTexel = texel;
    // sections fade in from the fog colour, except within 16 blocks of the camera (full at 32); order: the game's
    const float fullyVisibleWithin = 16.0;
    float reach = length(eye);
    qsFadeIn = mix(1.0, fade, clamp((reach - fullyVisibleWithin) / fullyVisibleWithin, 0.0, 1.0));
}
