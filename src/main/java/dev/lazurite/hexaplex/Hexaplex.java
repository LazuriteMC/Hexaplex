package dev.lazurite.hexaplex;

import blue.endless.jankson.api.SyntaxError;
import dev.lazurite.hexaplex.config.Config;
import dev.lazurite.hexaplex.rendering.ShaderManager;
import dev.lazurite.hexaplex.rendering.MatrixLoader;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public final class Hexaplex implements ClientModInitializer {
    public static final String MOD_ID = "hexaplex";
    public static final Logger LOGGER = LogManager.getLogger("Hexaplex");

    @Override
    public void onInitializeClient() {
        try {
            MatrixLoader.loadMatrices(this.getClass().getResourceAsStream("/assets/hexaplex/shaders/uniform/matrix4x4/filter.json"));
        } catch (SyntaxError | IOException e) {
            Hexaplex.LOGGER.error("Error loading Hexaplex shader matrices!");
            e.printStackTrace();
        }

        ShaderManager.registerRenderer();
        Config.INSTANCE.load();
    }
}
