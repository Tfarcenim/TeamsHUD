package com.t2pellet.teams.client;

import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class TeamsHUDClientForge {

    public static final IIngameOverlay compass = (gui, poseStack, partialTick, width, height) -> TeamsHUDClient.compass.render(poseStack);
    public static final IIngameOverlay status = (gui, poseStack, partialTick, width, height) -> TeamsHUDClient.status.render(poseStack);

    public static void init(IEventBus bus) {
        bus.addListener(TeamsHUDClientForge::setup);
        MinecraftForge.EVENT_BUS.addListener(TeamsHUDClientForge::clientTick);
        MinecraftForge.EVENT_BUS.addListener(TeamsHUDClientForge::clientDisconnect);
    }

    static void clientDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        TeamsHUDClient.clientDisconnect();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TeamsHUDClient.endClientTick();
        }
    }

    static void setup(FMLClientSetupEvent event) {
        TeamsHUDClient.registerKeybinds();
        OverlayRegistry.registerOverlayTop("compass",compass);
        OverlayRegistry.registerOverlayTop("status",status);
    }
}
