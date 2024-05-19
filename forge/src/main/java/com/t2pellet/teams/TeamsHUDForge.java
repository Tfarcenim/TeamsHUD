package com.t2pellet.teams;

import net.minecraftforge.fml.common.Mod;

@Mod(TeamsHUD.MODID)
public class TeamsHUDForge {
    
    public TeamsHUDForge() {
    
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        TeamsHUD.LOGGER.info("Hello Forge world!");
        TeamsHUD.init();
        
    }
}