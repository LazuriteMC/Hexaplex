package dev.lazurite.hexaplex.rendering;

import blue.endless.jankson.*;
import blue.endless.jankson.api.SyntaxError;
import dev.lazurite.hexaplex.config.Config;
import dev.lazurite.hexaplex.utilities.Matrix4x4;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class MatrixLoader {

    private MatrixLoader() { }

    private static final Map<String, JsonElement> MATRICES = new HashMap<>();

    public static void loadMatrices(InputStream in) throws IOException, SyntaxError {
        JsonObject object = Jankson.builder().build().load(in);

        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            MatrixLoader.MATRICES.put(entry.getKey(), entry.getValue());
        }
    }

    public static Matrix4x4 getMatrix(String name) {
        JsonArray array = (JsonArray) MatrixLoader.MATRICES.get(name);

        float[] values = new float[array.size()];

        int variableCount = 0;

        for (int i = 0; i < values.length; ++i) {
            JsonPrimitive primitive = (JsonPrimitive) array.get(i);

            if (primitive.getValue().equals("")) {
                if (variableCount == 0) {
                    values[i] = MathHelper.clamp((float) (2 * Config.INSTANCE.getStrength() * Config.INSTANCE.getSkew()), 0.0f, (float) Config.INSTANCE.getStrength());
                } else {
                    values[i] = MathHelper.clamp((float) (2 * Config.INSTANCE.getStrength() * (1.0 - Config.INSTANCE.getSkew())), 0.0f, (float) Config.INSTANCE.getStrength());
                }
                ++variableCount;
            } else {
                values[i] = primitive.asFloat(0.0f);
            }
        }

        return new Matrix4x4(values);
    }

    public enum MatrixNames {
        RGB_TO_XYZ,
        XYZ_TO_LMS,
        RGB_TO_LMS,
        LMS_TO_RGB,
        LMS_TO_LMSP,
        LMS_TO_LMSD,
        LMS_TO_LMST,
        RGBP_ERR,
        RGBD_ERR,
        RGBT_ERR;

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            boolean capitalizeNext = false;

            for (char c : super.toString().toCharArray()) {
                if (c == '_') {
                    capitalizeNext = true;
                } else {
                    builder.append(capitalizeNext ? c : Character.toLowerCase(c));

                    if (capitalizeNext) {
                        capitalizeNext = false;
                    }
                }
            }

            return builder.toString();
        }
    }
}
