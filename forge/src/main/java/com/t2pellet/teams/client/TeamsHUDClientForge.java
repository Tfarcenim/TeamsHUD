package com.t2pellet.teams.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class TeamsHUDClientForge {

    public static final IGuiOverlay compass = (gui, poseStack, partialTick, width, height) -> TeamsHUDClient.compass.render(poseStack);
    public static final IGuiOverlay status = (gui, poseStack, partialTick, width, height) -> TeamsHUDClient.status.render(poseStack);

    public static void init(IEventBus bus) {
        bus.addListener(TeamsHUDClientForge::setup);
        bus.addListener(TeamsHUDClientForge::registerOverlays);
        MinecraftForge.EVENT_BUS.addListener(TeamsHUDClientForge::clientTick);
        MinecraftForge.EVENT_BUS.addListener(TeamsHUDClientForge::clientDisconnect);
        MinecraftForge.EVENT_BUS.addListener(TeamsHUDClientForge::addButton);
    }

    static void clientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        TeamsHUDClient.clientDisconnect();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TeamsHUDClient.endClientTick();
        }
    }

    static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("compass",compass);
        event.registerAboveAll("status",status);
    }

    static void addButton(ScreenEvent.Init.Post event) {
        TeamsHUDClient.afterScreenInit(Minecraft.getInstance(),event.getScreen(),Minecraft.getInstance().getWindow().getGuiScaledWidth(),Minecraft.getInstance().getWindow().getGuiScaledHeight());
    }

    static void setup(FMLClientSetupEvent event) {
        TeamsHUDClient.registerKeybinds();
    }
}
