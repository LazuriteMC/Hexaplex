package dev.lazurite.hexaplex.color;

import dev.lazurite.hexaplex.helpers.Matrix4fHelper;
import net.minecraft.util.math.Matrix4f;

final class Matricies {
    enum NAMES {
        RGB_TO_LMS,
        LMS_TO_RGB,

        LMS_TO_LMSD,
        LMS_TO_LMSP,
        LMS_TO_LMST,

        RGBD_ERR,
        RGBP_ERR,
        RGBT_ERR
    }

    static final Matrix4f RGB_TO_LMS = new Matrix4f();
    static final Matrix4f LMS_TO_RGB = new Matrix4f();

    static final Matrix4f LMS_TO_LMSD = new Matrix4f();
    static final Matrix4f LMS_TO_LMSP = new Matrix4f();
    static final Matrix4f LMS_TO_LMST = new Matrix4f();

    static final Matrix4f RGBD_ERR = new Matrix4f();
    static final Matrix4f RGBP_ERR = new Matrix4f();
    static final Matrix4f RGBT_ERR = new Matrix4f();

    static {
        Matrix4fHelper.stuff(RGB_TO_LMS, new float[][] {
            { 17.8824f,   43.5161f,  4.11935f, 0.0f },
            { 3.45565f,   27.1554f,  3.86714f, 0.0f },
            { 0.0299566f, 0.184309f, 1.46709f, 0.0f },
            { 0.0f,       0.0f,      0.0f,     1.0f }
        });

        Matrix4fHelper.stuff(LMS_TO_RGB, new float[][] {
            { 17.8824f,   43.5161f,  4.11935f, 0.0f },
            { 3.45565f,   27.1554f,  3.86714f, 0.0f },
            { 0.0299566f, 0.184309f, 1.46709f, 0.0f },
            { 0.0f,       0.0f,      0.0f,     1.0f }
        });
        LMS_TO_RGB.invert();

        Matrix4fHelper.stuff(LMS_TO_LMSD, new float[][] {
            { 1.0f,      0.0f, 0.0f,     0.0f },
            { 0.494207f, 0.0f, 1.24827f, 0.0f },
            { 0.0f,      0.0f, 1.0f,     0.0f },
            { 0.0f,      0.0f, 0.0f,     1.0f }
        });

        Matrix4fHelper.stuff(LMS_TO_LMSP, new float[][] {
            { 0.0f, 2.02344f, -2.52581f, 0.0f },
            { 0.0f, 1.0f,       0.0f,    0.0f },
            { 0.0f, 0.0f,       1.0f,    0.0f },
            { 0.0f, 0.0f,       0.0f,    1.0f }
        });

        Matrix4fHelper.stuff(LMS_TO_LMST, new float[][] {
            { 1.0f,       0.0f,      0.0f, 0.0f },
            { 0.0f,       1.0f,      0.0f, 0.0f },
            { -0.395913f, 0.801109f, 0.0f, 0.0f },
            { 0.0f,       0.0f,      0.0f, 1.0f }
        });

        Matrix4fHelper.stuff(RGBD_ERR, new float[][] {
            { 1.0f, 0.7f, 0.0f, 0.0f },
            { 0.0f, 0.0f, 0.0f, 0.0f },
            { 0.0f, 0.7f, 1.0f, 0.0f },
            { 0.0f, 0.0f, 0.0f, 1.0f },
        });

        Matrix4fHelper.stuff(RGBP_ERR, new float[][] {
            { 0.0f, 0.0f, 0.0f, 0.0f },
            { 0.7f, 1.0f, 0.0f, 0.0f },
            { 0.7f, 0.0f, 1.0f, 0.0f },
            { 0.0f, 0.0f, 0.0f, 1.0f },
        });

        Matrix4fHelper.stuff(RGBT_ERR, new float[][] {
            { 1.0f, 0.0f, 0.7f, 0.0f },
            { 0.0f, 1.0f, 0.7f, 0.0f },
            { 0.0f, 0.0f, 0.0f, 0.0f },
            { 0.0f, 0.0f, 0.0f, 1.0f },
        });
    }
}
