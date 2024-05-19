package com.t2pellet.teams.core;

import com.t2pellet.teams.events.AdvancementEvents;
import com.t2pellet.teams.events.PlayerUpdateEvents;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamDataPacket;
import com.t2pellet.teams.network.packets.TeamPlayerDataPacket;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

;

public class EventHandlers {

    private EventHandlers() {
    }

    public static ServerPlayConnectionEvents.Join playerConnect = (handler, sender, server) -> {
        // Mark online
        ServerPlayer player = handler.getPlayer();
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.playerOnline(player, true);
        }
        // Send packets
        var teams = TeamDB.INSTANCE.getTeams().map(t -> t.name).toArray(String[]::new);
        var onlineTeams = TeamDB.INSTANCE.getTeams().filter(t -> t.getOnlinePlayers().findAny().isPresent()).map(t -> t.name).toArray(String[]::new);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ADD, teams), player);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ONLINE, onlineTeams), player);
    };

    public static ServerPlayConnectionEvents.Disconnect playerDisconnect = (handler, server) ->  {
        ServerPlayer player = handler.getPlayer();
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.playerOffline(player, true);
        }
    };

    public static PlayerUpdateEvents.PlayerHealthUpdate playerHealthUpdate = (player, health, hunger) -> {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            ServerPlayer[] players = team.getOnlinePlayers().filter(other -> !other.equals(player)).toArray(ServerPlayer[]::new);
            PacketHandler.INSTANCE.sendTo(new TeamPlayerDataPacket(player, TeamPlayerDataPacket.Type.UPDATE), players);
        }
    };

    public static PlayerUpdateEvents.PlayerCopy playerCopy = (oldPlayer, newPlayer) -> {
        Team team = TeamDB.INSTANCE.getTeam(oldPlayer);
        if (team != null) {
            team.playerOffline(oldPlayer, false);
            team.playerOnline(newPlayer, false);
        }
    };

    public static AdvancementEvents.PlayerAdvancement playerAdvancement = (player, advancement) -> {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            team.addAdvancement(advancement);
        }
    };

}
