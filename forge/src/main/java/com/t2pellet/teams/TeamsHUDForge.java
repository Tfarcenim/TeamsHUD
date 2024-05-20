package com.t2pellet.teams;

import com.t2pellet.teams.network.PacketHandlerForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TeamsHUD.MODID)
public class TeamsHUDForge {
    
    public TeamsHUDForge() {
        TeamsHUD.LOGGER.info("Teams forge mod init!");
        IEventBus bus  = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        TeamsHUD.init();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        PacketHandlerForge.registerPackets();
    }

}