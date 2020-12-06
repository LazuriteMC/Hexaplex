package dev.lazurite.hexaplex.client;

import dev.lazurite.hexaplex.color.ShaderManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HexaplexClient implements ClientModInitializer {
    public static final String MOD_ID = "hexaplex";

    @Override
    public void onInitializeClient() {
        ShaderManager.initialize(ShaderManager.NAMES.DEUTERAN);
    }
}
