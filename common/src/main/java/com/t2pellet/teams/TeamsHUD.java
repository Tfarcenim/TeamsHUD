package com.t2pellet.teams;

import com.t2pellet.teams.core.ModTeam;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.CommonPacketHandler;
import com.t2pellet.teams.network.client.S2CTeamDataPacket;
import com.t2pellet.teams.network.client.S2CTeamPlayerDataPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        CommonPacketHandler.registerPackets();
    }

    public static void onAdvancement(ServerPlayer player, Advancement advancement) {
        TeamDB teamDB = TeamDB.getOrMakeDefault(player.server);
        ModTeam team = teamDB.getTeam(player);
        if (team != null) {
            team.addAdvancement(advancement);
        }
    }

    public static void playerConnect(ServerPlayer player) {
        TeamDB teamDB = TeamDB.getOrMakeDefault(player.server);
        ModTeam team = teamDB.getTeam(player);
        if (team != null) {
            team.playerOnline(player, true);
        }
        // Send packets
        var teams = teamDB.getTeams().map(t -> t.name).toArray(String[]::new);
        var onlineTeams = teamDB.getTeams().filter(t -> t.getOnlinePlayers().stream().findAny().isPresent()).map(t -> t.name).toArray(String[]::new);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ADD, teams), player);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ONLINE, onlineTeams), player);
    }

    public static void playerDisconnect(ServerPlayer player) {
        TeamDB teamDB = TeamDB.getOrMakeDefault(player.server);
        ModTeam team = teamDB.getTeam(player);
        if (team != null) {
            team.playerOffline(player, true);
        }
    }

    public static void playerClone(ServerPlayer oldPlayer,ServerPlayer newPlayer,boolean alive) {
        TeamDB teamDB = TeamDB.getOrMakeDefault(oldPlayer.server);
        ModTeam team = teamDB.getTeam(oldPlayer);
        if (team != null) {
            team.playerOffline(oldPlayer, false);
            team.playerOnline(newPlayer, false);
        }
    }

    public static void onPlayerHealthUpdate(ServerPlayer player, float health, int hunger) {
        ModTeam team = TeamDB.getOrMakeDefault(player.server).getTeam(player);
        if (team != null) {
            List<ServerPlayer> players = team.getOnlinePlayers().stream().filter(other -> !other.equals(player)).collect(Collectors.toList());
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.UPDATE), players);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID,path);
    }

    public static void onServerStopped(MinecraftServer server) {

    }

    public static void onServerStarted(MinecraftServer server) {

    }
}