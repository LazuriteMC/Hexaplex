package dev.lazurite.hexaplex.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lazurite.hexaplex.Hexaplex;
import dev.lazurite.hexaplex.config.Config;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public final class ShaderManager {

    private ShaderManager() { }

    private static final ManagedShaderEffect FILTER = ShaderEffectManager.getInstance().manage(
        new Identifier(Hexaplex.MOD_ID, "shaders/post/filter.json"),
        shader -> {
            ShaderManager.FILTER.setUniformValue(
                    UniformNames.RGB_TO_LMS.toString(),
                    MatrixLoader.getMatrix(MatrixLoader.MatrixNames.RGB_TO_LMS.toString()).copyMatrix4f()
            );

            ShaderManager.FILTER.setUniformValue(
                    UniformNames.LMS_TO_RGB.toString(),
                    MatrixLoader.getMatrix(MatrixLoader.MatrixNames.LMS_TO_RGB.toString()).copyMatrix4f()
            );

            ShaderManager.setUniforms();
        }
    );

    private static void setUniforms() {
        switch (Config.INSTANCE.getProfile()) {
            case PROTAN:
                ShaderManager.FILTER.setUniformValue(
                        UniformNames.LMS_TO_LMSC.toString(),
                        MatrixLoader.getMatrix(MatrixLoader.MatrixNames.LMS_TO_LMSP.toString()).copyMatrix4f()
                );

                ShaderManager.FILTER.setUniformValue(
                        UniformNames.RGBC_ERR.toString(),
                        MatrixLoader.getMatrix(MatrixLoader.MatrixNames.RGBP_ERR.toString()).copyMatrix4f()
                );
                break;

            case DEUTERAN:
                ShaderManager.FILTER.setUniformValue(
                        UniformNames.LMS_TO_LMSC.toString(),
                        MatrixLoader.getMatrix(MatrixLoader.MatrixNames.LMS_TO_LMSD.toString()).copyMatrix4f()
                );

                ShaderManager.FILTER.setUniformValue(
                        UniformNames.RGBC_ERR.toString(),
                        MatrixLoader.getMatrix(MatrixLoader.MatrixNames.RGBD_ERR.toString()).copyMatrix4f()
                );
                break;

            case TRITAN:
                ShaderManager.FILTER.setUniformValue(
                        UniformNames.LMS_TO_LMSC.toString(),
                        MatrixLoader.getMatrix(MatrixLoader.MatrixNames.LMS_TO_LMST.toString()).copyMatrix4f()
                );

                ShaderManager.FILTER.setUniformValue(
                        UniformNames.RGBC_ERR.toString(),
                        MatrixLoader.getMatrix(MatrixLoader.MatrixNames.RGBT_ERR.toString()).copyMatrix4f()
                );
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
                    RenderSystem.disableAlphaTest(); // idek
                    RenderSystem.disableBlend(); // idek

                    ShaderManager.FILTER.render(tickDelta);

                    RenderSystem.enableAlphaTest();
                    RenderSystem.enableBlend();
                }
            }
        );
    }

    private enum UniformNames {
        RGB_TO_LMS,
        LMS_TO_RGB,
        LMS_TO_LMSC,
        RGBC_ERR;

        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            boolean capitalizeNext = false;

            for (char c : super.toString().toCharArray()) {
                if (c == '_') {
                    capitalizeNext = true;
                } else {
                    builder.append(capitalizeNext ? c : Character.toLowerCase(c));
                    capitalizeNext = false;
                }
            }

            return builder.toString();
        }
    }
}
