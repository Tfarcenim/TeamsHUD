package com.t2pellet.teams.core;

import com.t2pellet.teams.events.AdvancementEvents;
import com.t2pellet.teams.events.PlayerUpdateEvents;
import com.t2pellet.teams.network.client.S2CTeamDataPacket;
import com.t2pellet.teams.network.client.S2CTeamPlayerDataPacket;
import com.t2pellet.teams.platform.Services;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

;import java.util.List;
import java.util.stream.Collectors;

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
        var onlineTeams = TeamDB.INSTANCE.getTeams().filter(t -> t.getOnlinePlayers().stream().findAny().isPresent()).map(t -> t.name).toArray(String[]::new);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ADD, teams), player);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ONLINE, onlineTeams), player);
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
            List<ServerPlayer> players = team.getOnlinePlayers().stream().filter(other -> !other.equals(player)).collect(Collectors.toList());
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.UPDATE), players);
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
