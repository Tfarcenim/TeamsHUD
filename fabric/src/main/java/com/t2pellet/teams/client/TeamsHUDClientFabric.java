package com.t2pellet.teams.client;

import com.t2pellet.teams.client.TeamsKeys.TeamsKey;
import com.t2pellet.teams.network.ClientPacketHandlerFabric;
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
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            TeamsHUDClient.status.render(matrixStack);
            TeamsHUDClient.compass.render(matrixStack);
        });

        // Handle keybinds
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (var key : TeamsKeys.KEYS) {
                if (key.keyBinding.consumeClick()) {
                    key.onPress.execute(client);
                }
            }
        });

        // Register keybinds
        for (TeamsKey key : TeamsKeys.KEYS) {
            key.register();
        }

        // Register events
        ClientLoginConnectionEvents.DISCONNECT.register((handler, client) -> TeamsHUDClient.clientDisconnect());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> TeamsHUDClient.clientDisconnect());

        ScreenEvents.AFTER_INIT.register(TeamsHUDClient::afterScreenInit);
    }
}
