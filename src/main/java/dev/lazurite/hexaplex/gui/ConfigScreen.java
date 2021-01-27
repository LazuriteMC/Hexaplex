package dev.lazurite.hexaplex.gui;

import dev.lazurite.hexaplex.config.Config;
import dev.lazurite.hexaplex.rendering.Profiles;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;

public final class ConfigScreen implements ConfigScreenFactory<Screen> {

    @Override
    public Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(new TranslatableText("config.hexaplex.title"))
            .setSavingRunnable(Config.INSTANCE::save);

        IntegerSliderEntry strengthOption = builder.entryBuilder()
            .startIntSlider(new TranslatableText("config.hexaplex.strength"), (int) (Config.INSTANCE.getStrength() * 100), 0, 100)
            .setDefaultValue(0)
            .setSaveConsumer(strength -> Config.INSTANCE.setStrength((double) strength / 100.0))
            .build();

        IntegerSliderEntry skewOption = builder.entryBuilder()
            .startIntSlider(new TranslatableText("config.hexaplex.skew"), (int) (Config.INSTANCE.getSkew() * 100), 0, 100)
            .setDefaultValue(50)
            .setSaveConsumer(skew -> Config.INSTANCE.setSkew((double) skew / 100.0))
            .build();

        EnumListEntry<Profiles> profileOption = builder.entryBuilder()
            .startEnumSelector(new TranslatableText("config.hexaplex.profile"), Profiles.class, Config.INSTANCE.getProfile())
            .setDefaultValue(Profiles.NORMAL)
            .setEnumNameProvider(
                anEnum -> {
                    strengthOption.setValue(strengthOption.getValue()); // force update
                    skewOption.setValue(skewOption.getValue()); // force update

                    if (anEnum.equals(Profiles.NORMAL)) {
                        return new TranslatableText(((SelectionListEntry.Translatable) anEnum).getKey());
                    } else if ((double) strengthOption.getValue() / 100.0 != 1.0) {
                        return new TranslatableText(((SelectionListEntry.Translatable) anEnum).getKey() + "omaly");
                    } else {
                        return new TranslatableText(((SelectionListEntry.Translatable) anEnum).getKey() + "opia");
                    }
                }
            )
            .setSaveConsumer(Config.INSTANCE::setProfile)
            .build();

        strengthOption.setTextGetter(
            strength -> {
                if (profileOption.getValue().equals(Profiles.NORMAL)) {
                    return new TranslatableText("config.hexaplex.strength.normal");
                } else {
                    return new TranslatableText("config.hexaplex.strength." + profileOption.getValue().toString(), strength);
                }
            }
        );

        skewOption.setTextGetter(
            skew -> {
                switch (profileOption.getValue()) {
                    case PROTAN:
                        return new TranslatableText("config.hexaplex.skew.protan", skew, 100 - skew);
                    case DEUTERAN:
                        return new TranslatableText("config.hexaplex.skew.deuteran", skew, 100 - skew);
                    case TRITAN:
                        return new TranslatableText("config.hexaplex.skew.tritan", skew, 100 - skew);
                    case NORMAL:
                    default:
                        return new TranslatableText("config.hexaplex.skew.normal");
                }
            }
        );

        builder.getOrCreateCategory(new TranslatableText("config.hexaplex.category"))
            .addEntry(profileOption)
            .addEntry(strengthOption)
            .addEntry(skewOption);

        return builder.build();
    }
}
