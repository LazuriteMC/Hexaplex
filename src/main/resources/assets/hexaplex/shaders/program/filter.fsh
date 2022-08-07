#version 150

uniform mat4 rgbToLms;
uniform mat4 lmsToRgb;
uniform mat4 lmsToLmsc;
uniform mat4 rgbcErr;

uniform sampler2D DiffuseSampler;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec4 original = texture(DiffuseSampler, texCoord);

    vec4 simulated = rgbToLms * original;
    simulated = lmsToLmsc * simulated;
    simulated = lmsToRgb * simulated;

    vec4 corrected = vec4(original.rgb - simulated.rgb, original.a);
    corrected = rgbcErr * corrected;

    fragColor = vec4(original.rgb + corrected.rgb, original.a);
}
