package dev.lazurite.hexaplex.init;

import dev.lazurite.hexaplex.config.HexaplexConfig;
import dev.lazurite.hexaplex.graphics.Matrix4x4UniformManager;
import dev.lazurite.hexaplex.graphics.ShaderManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.CyclingOption;
import net.minecraft.client.options.DoubleOption;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ClientInitializer implements ClientModInitializer {
    public static final String MOD_ID = "hexaplex";

    public static final CyclingOption PROFILE_OPTION = new CyclingOption(
            "options.hexaplex.profile.title",
            (gameOptions, amount) -> HexaplexConfig.INSTANCE.setProfile(ShaderManager.Profiles.get(HexaplexConfig.INSTANCE.getProfile().ordinal() + amount)),
            (gameOptions, option) -> option.getGenericLabel(new TranslatableText("options.hexaplex.profile." + HexaplexConfig.INSTANCE.getProfile().name().toLowerCase()))
    );

    public static final DoubleOption STRENGTH_OPTION = new DoubleOption(
            "options.hexaplex.strength.title",
            0.0,
            1.0,
            0.01f,
            (gameOptions) -> HexaplexConfig.INSTANCE.getStrength(),
            (gameOptions, strength) -> HexaplexConfig.INSTANCE.setStrength(strength),
            (gameOptions, option) -> {
                option.setTooltip(MinecraftClient.getInstance().textRenderer.wrapLines(new TranslatableText("options.hexaplex.strength.tooltip"), 200));
                return option.getPercentLabel(option.getRatio(option.get(gameOptions)));
            }
    );

    @Override
    public void onInitializeClient() {
        Matrix4x4UniformManager.parseUniforms();
        ShaderManager.registerRenderer();
        HexaplexConfig.INSTANCE.load();
    }
}
