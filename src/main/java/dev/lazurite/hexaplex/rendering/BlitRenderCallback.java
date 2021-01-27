package dev.lazurite.hexaplex.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@Environment(EnvType.CLIENT)
public interface BlitRenderCallback {
    Event<BlitRenderCallback> EVENT = EventFactory.createArrayBacked(
        BlitRenderCallback.class,
        (listeners) -> (tickDelta) -> {
            for (BlitRenderCallback handler : listeners) {
                handler.renderBlit(tickDelta);
            }
        }
    );

    void renderBlit(float tickDelta);
}
