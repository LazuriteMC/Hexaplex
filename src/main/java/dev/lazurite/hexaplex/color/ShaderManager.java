package dev.lazurite.hexaplex.color;

import dev.lazurite.hexaplex.client.HexaplexClient;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

public final class ShaderManager {
    public enum NAMES {
        DEUTERAN,
        PROTAN,
        TRITAN
    }

    private static final ManagedShaderEffect DEUTERAN = ShaderEffectManager.getInstance().manage(
        new Identifier(HexaplexClient.MOD_ID, "shaders/post/deuteran.json"),
        shader -> {
            shader.setUniformValue(Matricies.NAMES.RGB_TO_LMS.name(),   Matricies.RGB_TO_LMS);
            shader.setUniformValue(Matricies.NAMES.LMS_TO_RGB.name(),   Matricies.LMS_TO_RGB);
            shader.setUniformValue(Matricies.NAMES.LMS_TO_LMSD.name(),  Matricies.LMS_TO_LMSD);
            shader.setUniformValue(Matricies.NAMES.RGBD_ERR.name(),     Matricies.RGBD_ERR);
        }
    );

    private static final ManagedShaderEffect PROTAN = ShaderEffectManager.getInstance().manage(
        new Identifier(HexaplexClient.MOD_ID, "shaders/post/protan.json"),
        shader -> {
            shader.setUniformValue(Matricies.NAMES.RGB_TO_LMS.name(),   Matricies.RGB_TO_LMS);
            shader.setUniformValue(Matricies.NAMES.LMS_TO_RGB.name(),   Matricies.LMS_TO_RGB);
            shader.setUniformValue(Matricies.NAMES.LMS_TO_LMSP.name(),  Matricies.LMS_TO_LMSP);
            shader.setUniformValue(Matricies.NAMES.RGBP_ERR.name(),     Matricies.RGBP_ERR);
        }
    );

    private static final ManagedShaderEffect TRITAN = ShaderEffectManager.getInstance().manage(
        new Identifier(HexaplexClient.MOD_ID, "shaders/post/tritan.json"),
        shader -> {
            shader.setUniformValue(Matricies.NAMES.RGB_TO_LMS.name(),   Matricies.RGB_TO_LMS);
            shader.setUniformValue(Matricies.NAMES.LMS_TO_RGB.name(),   Matricies.LMS_TO_RGB);
            shader.setUniformValue(Matricies.NAMES.LMS_TO_LMST.name(),  Matricies.LMS_TO_LMST);
            shader.setUniformValue(Matricies.NAMES.RGBT_ERR.name(),     Matricies.RGBT_ERR);
        }
    );

    public static void initialize(NAMES name) {
        ShaderEffectRenderCallback.EVENT.register(
            tickDelta -> {
                switch (name) {
                    case DEUTERAN:
                        DEUTERAN.render(tickDelta);
                        break;
                    case PROTAN:
                        PROTAN.render(tickDelta);
                        break;
                    case TRITAN:
                        TRITAN.render(tickDelta);
                        break;
                    default:
                        break;
                }
            }
        );
    }
}

