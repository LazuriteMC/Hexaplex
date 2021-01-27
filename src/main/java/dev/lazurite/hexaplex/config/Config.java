package dev.lazurite.hexaplex.config;

import dev.lazurite.hexaplex.Hexaplex;
import dev.lazurite.hexaplex.rendering.Profiles;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.AnnotatedSettings;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Setting;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Settings;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.FiberException;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Environment(EnvType.CLIENT)
@Settings(onlyAnnotated = true)
public final class Config {

    public static final Config INSTANCE = new Config();

    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(Hexaplex.MOD_ID + ".json");

    @Setting(name = "profile")
    private Profiles profile;

    @Setting(name = "strength")
    @Setting.Constrain.Range(min = 0.0, max = 1.0, step = 0.01)
    private double strength;

    @Setting(name = "skew")
    @Setting.Constrain.Range(min = 0.0, max = 1.0, step = 0.01)
    private double skew;

    private boolean dirty;

    private Config() {
        this.setProfile(Profiles.NORMAL);
        this.setStrength(0.0);
        this.setSkew(0.5);
        this.markDirty();
    }

    public void save() {
        try {
            FiberSerialization.serialize(
                ConfigTree.builder()
                    .applyFromPojo(
                        INSTANCE,
                        AnnotatedSettings.builder()
                            .collectOnlyAnnotatedMembers()
                            .build()
                    )
                    .build(),
                Files.newOutputStream(PATH),
                new JanksonValueSerializer(false)
            );
        } catch (IOException e) {
            Hexaplex.LOGGER.error("Error saving Hexaplex config!");
            e.printStackTrace();
        }
    }

    public void load() {
        if (Files.exists(PATH)) {
            try {
                FiberSerialization.deserialize(
                    ConfigTree.builder()
                        .applyFromPojo(
                            INSTANCE,
                            AnnotatedSettings.builder()
                                .collectOnlyAnnotatedMembers()
                                .build()
                        )
                        .build(),
                    Files.newInputStream(PATH),
                    new JanksonValueSerializer(false)
                );
            } catch (FiberException | IOException e) {
                Hexaplex.LOGGER.error("Error loading Hexaplex config!");
                e.printStackTrace();
            }
        } else {
            Hexaplex.LOGGER.info("Creating Hexaplex config.");
            this.save();
        }
    }

    public void setProfile(Profiles profile) {
        this.profile = profile;
        this.markDirty();
    }

    public Profiles getProfile() {
        return this.profile;
    }

    public void setStrength(double strength) {
        this.strength = strength;
        this.markDirty();
    }

    public double getStrength() {
        return this.strength;
    }

    public void setSkew(double skew) {
        this.skew = skew;
        this.markDirty();
    }

    public double getSkew() {
        return this.skew;
    }

    private void markDirty() {
        this.dirty = true;
    }

    public void markClean() {
        this.dirty = false;
    }

    public boolean isDirty() {
        return this.dirty;
    }
}
