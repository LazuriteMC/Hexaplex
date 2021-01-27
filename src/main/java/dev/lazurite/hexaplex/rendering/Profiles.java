package dev.lazurite.hexaplex.rendering;

import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public enum Profiles implements SelectionListEntry.Translatable {
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
        return "config.hexaplex.profile." + toString();
    }
}

