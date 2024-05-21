package com.t2pellet.teams.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;

public class TeamsHUDClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register HUDs
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            TeamsHUDClient.status.render(graphics);
            TeamsHUDClient.compass.render(graphics);
        });

        // Handle keybinds
        ClientTickEvents.END_CLIENT_TICK.register(client -> TeamsHUDClient.endClientTick());

        // Register events
        ClientLoginConnectionEvents.DISCONNECT.register((handler, client) -> TeamsHUDClient.clientDisconnect());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> TeamsHUDClient.clientDisconnect());

        ScreenEvents.AFTER_INIT.register(TeamsHUDClient::afterScreenInit);

        TeamsHUDClient.registerKeybinds();

    }
}
