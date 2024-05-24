package com.t2pellet.teams;

import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.CommonPacketHandler;
import com.t2pellet.teams.network.client.S2CTeamDataPacket;
import com.t2pellet.teams.network.client.S2CTeamPlayerDataPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.advancements.Advancement;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.scores.Scoreboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class TeamsHUD {

    public static final String MODID = "teams";
    public static final String MOD_NAME = "TeamsHUD";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    static MinecraftServer server;

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        CommonPacketHandler.registerPackets();
    }

    public static MinecraftServer getServer() {
        return server;
    }

    public static Scoreboard getScoreboard() {
        return server.getScoreboard();
    }

    public static void onAdvancement(ServerPlayer player, Advancement advancement) {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.addAdvancement(advancement);
        }
    }

    public static void playerConnect(ServerPlayer player) {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.playerOnline(player, true);
        }
        // Send packets
        var teams = TeamDB.INSTANCE.getTeams().map(t -> t.name).toArray(String[]::new);
        var onlineTeams = TeamDB.INSTANCE.getTeams().filter(t -> t.getOnlinePlayers().stream().findAny().isPresent()).map(t -> t.name).toArray(String[]::new);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ADD, teams), player);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ONLINE, onlineTeams), player);
    }

    public static void playerDisconnect(ServerPlayer player) {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.playerOffline(player, true);
        }
    }

    public static void playerClone(ServerPlayer oldPlayer,ServerPlayer newPlayer,boolean alive) {
        Team team = TeamDB.INSTANCE.getTeam(oldPlayer);
        if (team != null) {
            team.playerOffline(oldPlayer, false);
            team.playerOnline(newPlayer, false);
        }
    }

    public static void onServerStopped(MinecraftServer server) {
        // Save teams
        try {
            File saveFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), "teams.dat");
            CompoundTag element = TeamDB.INSTANCE.toNBT();
            NbtIo.write(element, saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onServerStarted(MinecraftServer server) {
        // Get server instance
        TeamsHUD.server = server;
        // Load saved teams
        try {
            File saveFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), "teams.dat");
            CompoundTag element = NbtIo.read(saveFile);
            if (element != null) {
                TeamDB.INSTANCE.fromNBT(element);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onPlayerHealthUpdate(ServerPlayer player, float health, int hunger) {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            List<ServerPlayer> players = team.getOnlinePlayers().stream().filter(other -> !other.equals(player)).collect(Collectors.toList());
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.UPDATE), players);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID,path);
    }

}