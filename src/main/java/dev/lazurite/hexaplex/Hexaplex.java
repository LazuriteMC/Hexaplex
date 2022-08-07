package dev.lazurite.hexaplex;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.api.SyntaxError;
import com.mojang.math.Matrix4f;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

// TODO: Error logging on load/save fails
@Config(name = Hexaplex.MOD_ID)
public final class Hexaplex implements ClientModInitializer, ConfigData, ModMenuApi {

    // region staticFields

    private static final Logger LOGGER = LogManager.getLogger("Hexaplex");
    static final String MOD_ID = "hexaplex";

    private static final List<JsonElement> MATRICES = new ArrayList<>(10);

    public static final Hexaplex INSTANCE = AutoConfig.register(Hexaplex.class, JanksonConfigSerializer::new).getConfig();
    public static boolean dirty = true;

    public static final ManagedShaderEffect FILTER = ShaderEffectManager.getInstance().manage(
            new ResourceLocation(Hexaplex.MOD_ID, "shaders/post/filter.json"),
            shader -> {
                Hexaplex.FILTER.setUniformValue(
                        UniformNames.RGB_TO_LMS.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.RGB_TO_LMS)
                );

                Hexaplex.FILTER.setUniformValue(
                        UniformNames.LMS_TO_RGB.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.LMS_TO_RGB)
                );

                Hexaplex.INSTANCE.setUniforms();
            }
    );

    // endregion staticFields

    // region staticMethods

    /**
     * A utility method to convert enum names (AN_ENUM_NAME) to camel case (anEnumName).
     */
    private static String camelCaseEnumToString(final String toString) {
        final var builder = new StringBuilder();
        boolean capitalizeNext = false;

        for (final char c : toString.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                builder.append(capitalizeNext ? c : Character.toLowerCase(c));
                capitalizeNext = false;
            }
        }

        return builder.toString();
    }

    // endregion staticMethods

    // region memberFields

    private ProfileNames profile;
    private double strength;
    private double skew;

    // endregion memberFields

    // region memberMethods

    private void setProfile(final ProfileNames profile) {
        this.profile = profile;
        Hexaplex.dirty = true;
    }

    public ProfileNames getProfile() {
        return this.profile;
    }

    private void setStrength(final double strength) {
        this.strength = strength;
        Hexaplex.dirty = true;
    }

    public double getStrength() {
        return this.strength;
    }

    private void setSkew(final double skew) {
        this.skew = skew;
        Hexaplex.dirty = true;
    }

    private double getSkew() {
        return this.skew;
    }

    private Matrix4f getMatrix(final MatrixNames matrixName) {
        final var array = (JsonArray) Hexaplex.MATRICES.get(matrixName.ordinal());

        final var values = FloatBuffer.allocate(array.size());

        var variableCount = 0;

        for (int i = 0; i < values.capacity(); ++i) {
            final var primitive = (JsonPrimitive) array.get(i);

            if (primitive.getValue().equals("")) {
                if (variableCount == 0) {
                    // TODO Verify
                    values.put(Mth.clamp((float) (2 * Hexaplex.INSTANCE.getStrength() * Hexaplex.INSTANCE.getSkew()), 0.0f, (float) Hexaplex.INSTANCE.getStrength()));
                } else {
                    values.put(Mth.clamp((float) (2 * Hexaplex.INSTANCE.getStrength() * (1.0 - Hexaplex.INSTANCE.getSkew())), 0.0f, (float) Hexaplex.INSTANCE.getStrength()));
                }
                ++variableCount;
            } else {
                values.put(primitive.asFloat(0.0f));
            }
        }

        final var matrix = new Matrix4f();
        matrix.loadTransposed(values);
        return matrix;
    }

    public void setUniforms() {
        switch (Hexaplex.INSTANCE.getProfile()) {
            case PROTAN -> {
                Hexaplex.FILTER.setUniformValue(
                        UniformNames.LMS_TO_LMSC.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.LMS_TO_LMSP)
                );
                Hexaplex.FILTER.setUniformValue(
                        UniformNames.RGBC_ERR.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.RGBP_ERR)
                );
            }
            case DEUTERAN -> {
                Hexaplex.FILTER.setUniformValue(
                        UniformNames.LMS_TO_LMSC.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.LMS_TO_LMSD)
                );
                Hexaplex.FILTER.setUniformValue(
                        UniformNames.RGBC_ERR.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.RGBD_ERR)
                );
            }
            case TRITAN -> {
                Hexaplex.FILTER.setUniformValue(
                        UniformNames.LMS_TO_LMSC.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.LMS_TO_LMST)
                );
                Hexaplex.FILTER.setUniformValue(
                        UniformNames.RGBC_ERR.toString(),
                        Hexaplex.INSTANCE.getMatrix(MatrixNames.RGBT_ERR)
                );
            }
        }
    }

    // endregion memberMethods

    // region ClientModInitializer

    @Override
    public void onInitializeClient() {
        Hexaplex.LOGGER.info("Colorful!");

        try {
            Hexaplex.MATRICES.addAll(
                    Jankson.builder().build().load(
                            this.getClass().getResourceAsStream("/assets/hexaplex/shaders/uniform/matrix4f/filter.json")
                    ).values()
            );
        } catch (SyntaxError | IOException e) {
            Hexaplex.LOGGER.error("Error loading Hexaplex shader matrices!");
            throw new RuntimeException(e);
        }
    }

    // endregion ClientModInitializer

    // region ConfigData

    @Override
    public void validatePostLoad() {
        if (this.profile == null) {
            this.profile = ProfileNames.NORMAL;
        }

        this.strength = Mth.clamp(this.strength, 0.0, 1.0);
        this.skew = Mth.clamp(this.skew, 0.0, 1.0);

        Hexaplex.dirty = true;
    }

    // endregion ConfigData

    // region ModMenuApi

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(screen)
                    .setTitle(Component.translatable("config.hexaplex.title"))
                    .setSavingRunnable(AutoConfig.getConfigHolder(Hexaplex.class)::save); // TODO

            IntegerSliderEntry strengthOption = builder.entryBuilder()
                    .startIntSlider(Component.translatable("config.hexaplex.strength"), (int) (Hexaplex.INSTANCE.getStrength() * 100), 0, 100)
                    .setDefaultValue(0)
                    .setSaveConsumer(strength -> Hexaplex.INSTANCE.setStrength((double) strength / 100.0))
                    .build();

            IntegerSliderEntry skewOption = builder.entryBuilder()
                    .startIntSlider(Component.translatable("config.hexaplex.skew"), (int) (Hexaplex.INSTANCE.getSkew() * 100), 0, 100)
                    .setDefaultValue(50)
                    .setSaveConsumer(skew -> Hexaplex.INSTANCE.setSkew((double) skew / 100.0))
                    .build();

            EnumListEntry<ProfileNames> profileOption = builder.entryBuilder()
                    .startEnumSelector(Component.translatable("config.hexaplex.profile"), ProfileNames.class, Hexaplex.INSTANCE.getProfile())
                    .setDefaultValue(ProfileNames.NORMAL)
                    .setEnumNameProvider(
                            anEnum -> {
                                strengthOption.setValue(strengthOption.getValue()); // force update
                                skewOption.setValue(skewOption.getValue()); // force update

                                if (anEnum.equals(ProfileNames.NORMAL)) {
                                    return Component.translatable(((SelectionListEntry.Translatable) anEnum).getKey());
                                } else if ((double) strengthOption.getValue() / 100.0 != 1.0) {
                                    return Component.translatable(((SelectionListEntry.Translatable) anEnum).getKey() + "omaly");
                                } else {
                                    return Component.translatable(((SelectionListEntry.Translatable) anEnum).getKey() + "opia");
                                }
                            }
                    )
                    .setSaveConsumer(Hexaplex.INSTANCE::setProfile)
                    .build();

            strengthOption.setTextGetter(
                    strength -> {
                        if (profileOption.getValue().equals(ProfileNames.NORMAL)) {
                            return Component.translatable("config.hexaplex.strength.normal");
                        } else {
                            return Component.translatable("config.hexaplex.strength." + profileOption.getValue().toString(), strength);
                        }
                    }
            );

            skewOption.setTextGetter(
                    skew -> switch (profileOption.getValue()) {
                        case NORMAL -> Component.translatable("config.hexaplex.skew.normal");
                        case PROTAN -> Component.translatable("config.hexaplex.skew.protan", skew, 100 - skew);
                        case DEUTERAN -> Component.translatable("config.hexaplex.skew.deuteran", skew, 100 - skew);
                        case TRITAN -> Component.translatable("config.hexaplex.skew.tritan", skew, 100 - skew);
                    }
            );

            builder.getOrCreateCategory(Component.translatable("config.hexaplex.category"))
                    .addEntry(profileOption)
                    .addEntry(strengthOption)
                    .addEntry(skewOption);

            return builder.build();
        };
    }

    // endregion ModMenuApi

    // region enums

    public enum ProfileNames implements SelectionListEntry.Translatable {
        NORMAL,
        PROTAN,
        DEUTERAN,
        TRITAN;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }

        @Override
        public @NotNull String getKey() {
            return "config.hexaplex.profile." + this;
        }

    }

    private enum MatrixNames {
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
            return Hexaplex.camelCaseEnumToString(super.toString());
        }

    }

    private enum UniformNames {
        RGB_TO_LMS,
        LMS_TO_RGB,
        LMS_TO_LMSC,
        RGBC_ERR;

        @Override
        public String toString() {
            return Hexaplex.camelCaseEnumToString(super.toString());
        }

    }

    // endregion enums

}
