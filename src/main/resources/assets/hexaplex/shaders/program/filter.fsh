#version 120

uniform mat4 rgbToLms;
uniform mat4 lmsToRgb;
uniform mat4 lmsToLmsc;
uniform mat4 rgbcErr;

uniform sampler2D DiffuseSampler;
varying vec2 texCoord;

void main() {
    vec4 original = texture2D(DiffuseSampler, texCoord);

    vec4 simulated = rgbToLms * original;
    simulated = lmsToLmsc * simulated;
    simulated = lmsToRgb * simulated;

    vec4 corrected = vec4(original.rgb - simulated.rgb, original.a);
    corrected = rgbcErr * corrected;

    gl_FragColor = vec4(original.rgb + corrected.rgb, original.a);
}
