#version 120

uniform mat4 RGB_TO_LMS;
uniform mat4 LMS_TO_RGB;
uniform mat4 LMS_TO_LMSC;
uniform mat4 LMSC_ERR;

uniform sampler2D DiffuseSampler;
varying vec2 texCoord;

float vecMax(vec4 v) {
    return max(v.r, max(v.g, max(v.b, v.a)));
}

void main() {
    vec4 original = texture2D(DiffuseSampler, texCoord);

    vec4 simulated = RGB_TO_LMS * original;
    simulated = LMS_TO_LMSC * simulated;
    simulated = LMS_TO_RGB * simulated;

    simulated = simulated / vecMax(simulated);

    vec4 corrected = vec4(original.rgb - simulated.rgb, original.a);
    corrected = LMSC_ERR * corrected;

    gl_FragColor = vec4(original.rgb + corrected.rgb, original.a);


//    // look into correcting while in lms
//    vec4 rgbOriginal = texture2D(DiffuseSampler, texCoord);
//    vec4 lmsOriginal = RGB_TO_LMS * rgbOriginal;
//
//    vec4 lmsSimulated = LMS_TO_LMSC * lmsOriginal;
//
//    vec4 lmsCorrected = vec4(lmsOriginal.rgb - lmsSimulated.rgb, lmsOriginal.a); // preserves alpha
//    lmsCorrected = LMSC_ERR * lmsCorrected;
//
//    vec4 rgbCorrected = LMS_TO_RGB * lmsCorrected;
//    rgbCorrected = rgbCorrected / vecMax(rgbCorrected);
//
//    gl_FragColor = vec4(rgbOriginal.rgb + rgbCorrected.rgb, rgbOriginal.a);
}
