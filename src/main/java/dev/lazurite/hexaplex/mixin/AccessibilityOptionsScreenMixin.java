package dev.lazurite.hexaplex.mixin;

import dev.lazurite.hexaplex.init.ClientInitializer;
import net.minecraft.client.gui.screen.options.AccessibilityOptionsScreen;
import net.minecraft.client.options.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccessibilityOptionsScreen.class)
public class AccessibilityOptionsScreenMixin {
    @Shadow private static Option[] OPTIONS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo info) {
        final Option[] OLD_OPTIONS = new Option[OPTIONS.length];

        System.arraycopy(OPTIONS, 0, OLD_OPTIONS, 0, OPTIONS.length);

        OPTIONS = new Option[OLD_OPTIONS.length + 2];

        System.arraycopy(OLD_OPTIONS, 0, OPTIONS, 0, OLD_OPTIONS.length);

        OPTIONS[OLD_OPTIONS.length] = ClientInitializer.PROFILE_OPTION;
        OPTIONS[OLD_OPTIONS.length + 1] = ClientInitializer.STRENGTH_OPTION;
    }
}
