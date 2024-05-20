package com.t2pellet.teams;

import com.t2pellet.teams.client.TeamsHUDClient;
import com.t2pellet.teams.client.TeamsHUDClientForge;
import com.t2pellet.teams.command.TeamCommand;
import com.t2pellet.teams.network.PacketHandlerForge;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(TeamsHUD.MODID)
public class TeamsHUDForge {
    
    public TeamsHUDForge() {
        TeamsHUD.LOGGER.info("Teams forge mod init!");
        IEventBus bus  = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        bus.addListener(this::registerCommand);

        MinecraftForge.EVENT_BUS.addListener(this::login);
        MinecraftForge.EVENT_BUS.addListener(this::logout);
        MinecraftForge.EVENT_BUS.addListener(this::playerClone);

        if (FMLEnvironment.dist.isClient()) {
            TeamsHUDClientForge.init(bus);
        }
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        TeamsHUD.init();
    }

    private void login(PlayerEvent.PlayerLoggedInEvent event) {
        TeamsHUD.playerConnect((ServerPlayer) event.getPlayer());
    }

    private void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        TeamsHUD.playerDisconnect((ServerPlayer) event.getPlayer());
    }

    private void playerClone(PlayerEvent.Clone event) {
        TeamsHUD.playerClone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getPlayer(),!event.isWasDeath());
    }

    private void registerCommand(RegisterCommandsEvent event) {
        TeamCommand.register(event.getDispatcher());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

}