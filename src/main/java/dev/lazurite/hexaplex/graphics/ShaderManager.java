package dev.lazurite.hexaplex.graphics;

import dev.lazurite.hexaplex.config.HexaplexConfig;
import dev.lazurite.hexaplex.rendering.BlitRenderCallback;
import dev.lazurite.hexaplex.init.ClientInitializer;
import dev.lazurite.hexaplex.util.Matrix4x4;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.util.Identifier;

public final class ShaderManager {

    private ShaderManager() { }

    private enum Uniforms {
        RGB_TO_LMS,
        LMS_TO_RGB,
        LMS_TO_LMSC,
        LMSC_ERR
    }

    public enum Profiles {
        NORMAL,
        DEUTERAN,
        PROTAN,
        TRITAN;

        public static Profiles get(int index) {
            return Profiles.values()[index % Profiles.values().length];
        }
    }

    public static final ManagedShaderEffect FILTER = ShaderEffectManager.getInstance().manage(
        new Identifier(ClientInitializer.MOD_ID, "shaders/post/filter.json"),
        shader -> ShaderManager.setUniforms()
    );

    private static void setUniforms() {
        ShaderManager.FILTER.setUniformValue(Uniforms.RGB_TO_LMS.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.RGB_TO_LMS).copyMatrix4f());
        ShaderManager.FILTER.setUniformValue(Uniforms.LMS_TO_RGB.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.LMS_TO_RGB).copyMatrix4f());

        switch (HexaplexConfig.INSTANCE.getProfile()) {
            case DEUTERAN:
                ShaderManager.FILTER.setUniformValue(Uniforms.LMS_TO_LMSC.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.LMS_TO_LMSD).copyMatrix4f());
                ShaderManager.FILTER.setUniformValue(Uniforms.LMSC_ERR.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.LMSD_ERR).copyMatrix4f());
                break;
            case PROTAN:
                ShaderManager.FILTER.setUniformValue(Uniforms.LMS_TO_LMSC.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.LMS_TO_LMSP).copyMatrix4f());
                ShaderManager.FILTER.setUniformValue(Uniforms.LMSC_ERR.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.LMSP_ERR).copyMatrix4f());
                break;
            case TRITAN:
                ShaderManager.FILTER.setUniformValue(Uniforms.LMS_TO_LMSC.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.LMS_TO_LMST).copyMatrix4f());
                ShaderManager.FILTER.setUniformValue(Uniforms.LMSC_ERR.name(), Matrix4x4UniformManager.get(Matrix4x4UniformManager.Matrices.LMST_ERR).copyMatrix4f());
                break;
            case NORMAL:
            default:
                Matrix4x4 identityMatrix = new Matrix4x4();
                identityMatrix.loadIdentity();

                ShaderManager.FILTER.setUniformValue(Uniforms.LMS_TO_LMSC.name(), identityMatrix.copyMatrix4f());
                ShaderManager.FILTER.setUniformValue(Uniforms.LMSC_ERR.name(), identityMatrix.copyMatrix4f());
                break;
        }
    }

    public static void registerRenderer() {
        BlitRenderCallback.EVENT.register(
            tickDelta -> {
                if (HexaplexConfig.INSTANCE.isDirty()) {
                    ShaderManager.setUniforms();
                    HexaplexConfig.INSTANCE.markClean();
                }

                ShaderManager.FILTER.render(tickDelta);
            }
        );
    }
}
