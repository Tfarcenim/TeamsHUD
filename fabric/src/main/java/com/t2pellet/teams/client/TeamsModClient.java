package com.t2pellet.teams.client;

import com.t2pellet.teams.client.TeamsKeys.TeamsKey;
import com.t2pellet.teams.client.core.EventHandlers;
import com.t2pellet.teams.client.ui.hud.CompassOverlay;
import com.t2pellet.teams.client.ui.hud.StatusOverlay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class TeamsModClient implements ClientModInitializer {

    public static final Minecraft client = Minecraft.getInstance();
    public static final StatusOverlay status = new StatusOverlay();
    public static final CompassOverlay compass = new CompassOverlay();

    @Override
    public void onInitializeClient() {
        // Register HUDs
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            status.render(matrixStack);
            compass.render(matrixStack);
        });
        // Register keybinds
        for (TeamsKey key : TeamsKeys.KEYS) {
            key.register();
        }
        // Handle keybinds
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (var key : TeamsKeys.KEYS) {
                if (key.keyBinding.consumeClick()) {
                    key.onPress.execute(client);
                }
            }
        });
        // Register events
        ClientLoginConnectionEvents.DISCONNECT.register(EventHandlers.disconnect);
        ClientPlayConnectionEvents.DISCONNECT.register(EventHandlers.disconnect1);
    }

}
