package dev.lazurite.hexaplex.init;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import dev.lazurite.hexaplex.config.Config;
import dev.lazurite.hexaplex.graphics.ShaderManager;
import dev.lazurite.hexaplex.util.Matrix4x4;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class ClientInitializer implements ClientModInitializer {
    public static final String MOD_ID = "hexaplex";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private static final Map<String, JsonArray> MATRICES = new HashMap<>();

    public static final CyclingOption PROFILE_OPTION = new CyclingOption(
            "options.hexaplex.profile.title",
            (gameOptions, amount) -> Config.INSTANCE.setProfile(ShaderManager.Profiles.get(Config.INSTANCE.getProfile().ordinal() + amount)),
            (gameOptions, option) -> option.getGenericLabel(new TranslatableText("options.hexaplex.profile." + Config.INSTANCE.getProfile().getName()))
    );

    public static final DoubleOption STRENGTH_OPTION = new DoubleOption(
            "options.hexaplex.strength.title",
            0.0,
            1.0,
            0.01f,
            (gameOptions) -> Config.INSTANCE.getStrength(),
            (gameOptions, strength) -> Config.INSTANCE.setStrength(strength),
            (gameOptions, option) -> {
                option.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("options.hexaplex.strength.tooltip"), 200));
                return option.getPercentLabel(option.getRatio(option.get(gameOptions)));
            }
    );

    @Override
    public void onInitializeClient() {
        ClientInitializer.loadMatrices();
        ShaderManager.registerRenderer();
        Config.INSTANCE.load();
    }

    private static void loadMatrices() {
        JsonElement json = new JsonParser().parse(new InputStreamReader(ClientInitializer.class.getResourceAsStream("/assets/hexaplex/shaders/uniform/matrix4x4/filter.json")));

        for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
            MATRICES.put(entry.getKey(), entry.getValue().getAsJsonArray());
        }
    }

    public static Matrix4x4 getMatrix(String name) {
        JsonArray array = MATRICES.get(name);

        float[] values = new float[array.size()];

        for (int i = 0; i < values.length; ++i) {
            JsonPrimitive primitive = array.get(i).getAsJsonPrimitive();

            if (primitive.isString()) {
                values[i] = (float) Config.INSTANCE.getStrength();
            } else {
                values[i] = primitive.getAsFloat();
            }
        }

        return new Matrix4x4(values);
    }
}
