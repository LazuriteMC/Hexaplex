package dev.lazurite.hexaplex.config;

import dev.lazurite.hexaplex.graphics.ShaderManager;
import dev.lazurite.hexaplex.init.ClientInitializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.AnnotatedSettings;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Setting;
import io.github.fablabsmc.fablabs.api.fiber.v1.annotation.Settings;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.FiberException;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;

@Settings(onlyAnnotated = true)
public class Config {

    public static final Config INSTANCE = new Config();

    private boolean dirty;

    @Setting(name = "filterProfile")
    private ShaderManager.Profiles profile;

    @Setting(name = "filterStrength")
    @Setting.Constrain.Range(min = 0.0, max = 1.0, step = 0.01)
    private double strength;

    private Config() {
        this.profile = ShaderManager.Profiles.NORMAL;
        this.strength = 0.0;
        this.dirty = true;
    }

    private void markDirty() {
        this.dirty = true;
    }

    public void markClean() {
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void save() {
        try {
            FiberSerialization.serialize(
                ConfigTree.builder().applyFromPojo(INSTANCE, AnnotatedSettings.builder().collectOnlyAnnotatedMembers().build()).build(),
                Files.newOutputStream(FabricLoader.getInstance().getConfigDir().resolve("hexaplex.json")),
                new JanksonValueSerializer(false)
            );
        } catch (IOException e) {
            ClientInitializer.LOGGER.error("Error saving Hexaplex config.");
            e.printStackTrace();
        }
    }

    public void load() {
        if (Files.exists(FabricLoader.getInstance().getConfigDir().resolve("hexaplex.json"))) {
            try {
                FiberSerialization.deserialize(
                    ConfigTree.builder().applyFromPojo(INSTANCE, AnnotatedSettings.builder().collectOnlyAnnotatedMembers().build()).build(),
                    Files.newInputStream(FabricLoader.getInstance().getConfigDir().resolve("hexaplex.json")),
                    new JanksonValueSerializer(false)
                );
            } catch (IOException | FiberException e) {
                ClientInitializer.LOGGER.error("Error loading Hexaplex config.");
                e.printStackTrace();
            }
        } else {
            ClientInitializer.LOGGER.info("Creating Hexaplex config.");
            this.save();
        }
    }

    public void setProfile(ShaderManager.Profiles profile) {
        this.profile = profile;
        this.markDirty();
        this.save();
    }

    public ShaderManager.Profiles getProfile() {
        return this.profile;
    }

    public void setStrength(double strength) {
        this.strength = strength;
        this.save();
    }

    public double getStrength() {
        return this.strength;
    }
}
