package com.t2pellet.teams.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.ScreenEvent;
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
        MinecraftForge.EVENT_BUS.addListener(TeamsHUDClientForge::addButton);
    }

    static void clientDisconnect(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        TeamsHUDClient.clientDisconnect();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TeamsHUDClient.endClientTick();
        }
    }

    static void addButton(ScreenEvent.InitScreenEvent.Post event) {
        TeamsHUDClient.afterScreenInit(Minecraft.getInstance(),event.getScreen(),Minecraft.getInstance().getWindow().getGuiScaledWidth(),Minecraft.getInstance().getWindow().getGuiScaledHeight());
    }

    static void setup(FMLClientSetupEvent event) {
        TeamsHUDClient.registerKeybinds();
        OverlayRegistry.registerOverlayTop("compass",compass);
        OverlayRegistry.registerOverlayTop("status",status);
    }
}
