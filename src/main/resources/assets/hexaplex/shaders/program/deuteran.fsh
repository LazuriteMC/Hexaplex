#version 120

uniform mat4 RGB_TO_LMS;
uniform mat4 LMS_TO_RGB;
uniform mat4 LMS_TO_LMSD;
uniform mat4 RGBD_ERR;

uniform sampler2D DiffuseSampler;
varying vec2 texCoord;

float vecMax(vec4 v) {
    return max(v.r, max(v.g, max(v.b, v.a)));
}

void main() {
    vec4 original = texture2D(DiffuseSampler, texCoord);

    vec4 simulated = RGB_TO_LMS * original;
    simulated = LMS_TO_LMSD * simulated;
    simulated = LMS_TO_RGB * simulated;

    simulated.rgb = simulated.rgb / vecMax(simulated);

    vec4 corrected = vec4(original.rgb - simulated.rgb, original.a);
    corrected = RGBD_ERR * corrected;

    gl_FragColor = vec4(original.rgb + corrected.rgb, original.a);
}
