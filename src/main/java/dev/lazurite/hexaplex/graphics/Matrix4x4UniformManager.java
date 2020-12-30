package dev.lazurite.hexaplex.graphics;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.lazurite.hexaplex.config.HexaplexConfig;
import dev.lazurite.hexaplex.util.Matrix4x4;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public final class Matrix4x4UniformManager {

    private Matrix4x4UniformManager() { }

    public enum Matrices {
        RGB_TO_LMS,
        LMS_TO_RGB,

        LMS_TO_LMSD,
        LMS_TO_LMSP,
        LMS_TO_LMST,

        LMSD_ERR,
        LMSP_ERR,
        LMST_ERR
    }

    private static final Map<String, JsonArray> MATRICES = new HashMap<>();

    public static void parseUniforms() {
        JsonElement json = new JsonParser().parse(new InputStreamReader(Matrix4x4UniformManager.class.getResourceAsStream("/assets/hexaplex/shaders/uniform/matrix4x4/filter.json")));

        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            MATRICES.put(entry.getKey(), entry.getValue().getAsJsonArray());
        }
    }

    public static Matrix4x4 get(Matrices name) {
        JsonArray array = MATRICES.get(name.name());

        float[] values = new float[array.size()];

        for (int i = 0; i < values.length; ++i) {
            JsonPrimitive primitive = array.get(i).getAsJsonPrimitive();

            if (primitive.isString()) {
                values[i] = (float) HexaplexConfig.INSTANCE.getStrength();
            } else {
                values[i] = primitive.getAsFloat();
            }
        }

        return new Matrix4x4(values);
    }
}
