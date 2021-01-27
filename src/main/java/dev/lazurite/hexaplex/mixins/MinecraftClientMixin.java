package dev.lazurite.hexaplex.mixins;

import dev.lazurite.hexaplex.rendering.BlitRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow private boolean paused;
    @Shadow private float pausedTickDelta;
    @Shadow @Final private RenderTickCounter renderTickCounter;

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gl/Framebuffer;endWrite()V"
        )
    )
    private void render(boolean tick, CallbackInfo info) {
        BlitRenderCallback.EVENT.invoker().renderBlit(this.paused ? this.pausedTickDelta : this.renderTickCounter.tickDelta);
    }
}
