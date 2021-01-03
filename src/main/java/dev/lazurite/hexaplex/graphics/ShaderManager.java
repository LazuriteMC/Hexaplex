package dev.lazurite.hexaplex.graphics;

import dev.lazurite.hexaplex.config.Config;
import dev.lazurite.hexaplex.init.ClientInitializer;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

public final class ShaderManager {

    private ShaderManager() { }

    public enum Profiles {
        NORMAL("normal"),
        DEUTERAN("deuteran"),
        PROTAN("protan"),
        TRITAN("tritan");

        private final String name;

        Profiles(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static Profiles get(int index) {
            return values()[index % values().length];
        }
    }

    public static final ManagedShaderEffect FILTER = ShaderEffectManager.getInstance().manage(
        new Identifier(ClientInitializer.MOD_ID, "shaders/post/filter.json"),
        shader -> {
            ShaderManager.FILTER.setUniformValue("rgbToLms", ClientInitializer.getMatrix("rgbToLms").copyMatrix4f());
            ShaderManager.FILTER.setUniformValue("lmsToRgb", ClientInitializer.getMatrix("lmsToRgb").copyMatrix4f());

            ShaderManager.setUniforms();
        }
    );

    private static void setUniforms() {
        switch (Config.INSTANCE.getProfile()) {
            case DEUTERAN:
                ShaderManager.FILTER.setUniformValue("lmsToLmsc", ClientInitializer.getMatrix("lmsToLmsd").copyMatrix4f());
                ShaderManager.FILTER.setUniformValue("rgbcErr", ClientInitializer.getMatrix("rgbdErr").copyMatrix4f());
                break;
            case PROTAN:
                ShaderManager.FILTER.setUniformValue("lmsToLmsc", ClientInitializer.getMatrix("lmsToLmsp").copyMatrix4f());
                ShaderManager.FILTER.setUniformValue("rgbcErr", ClientInitializer.getMatrix("rgbpErr").copyMatrix4f());
                break;
            case TRITAN:
                ShaderManager.FILTER.setUniformValue("lmsToLmsc", ClientInitializer.getMatrix("lmsToLmst").copyMatrix4f());
                ShaderManager.FILTER.setUniformValue("rgbcErr", ClientInitializer.getMatrix("rgbtErr").copyMatrix4f());
                break;
            default:
                break;
        }
    }

    public static void registerRenderer() {
        BlitRenderCallback.EVENT.register(
            tickDelta -> {
                if (Config.INSTANCE.isDirty()) {
                    ShaderManager.setUniforms();
                    Config.INSTANCE.markClean();
                }

                if (!Config.INSTANCE.getProfile().equals(Profiles.NORMAL) && !(Config.INSTANCE.getStrength() == 0.0)) {
                    ShaderManager.FILTER.render(tickDelta);
                }
            }
        );
    }
}
