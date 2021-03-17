package dev.lazurite.hexaplex.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface BlitRenderEvent {

    Event<BlitRenderEvent> EVENT = EventFactory.createArrayBacked(
            BlitRenderEvent.class,
            (events) -> (tickDelta) -> {
                for (BlitRenderEvent event : events) {
                    event.renderBlit(tickDelta);
                }
            }
    );

    void renderBlit(float tickDelta);

}
