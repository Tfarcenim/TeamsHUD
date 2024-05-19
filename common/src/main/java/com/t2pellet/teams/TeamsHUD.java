package com.t2pellet.teams;

import com.t2pellet.teams.platform.Services;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.scores.Scoreboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TeamsHUD {

    public static final String MODID = "teams";
    public static final String MOD_NAME = "TeamsHUD";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    static MinecraftServer server;
    static Scoreboard scoreboard;

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        Services.PLATFORM.getConfig().init();
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static Scoreboard getScoreboard() {
        return scoreboard;
    }
}