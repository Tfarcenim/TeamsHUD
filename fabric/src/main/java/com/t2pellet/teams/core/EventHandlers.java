package com.t2pellet.teams.core;

import com.t2pellet.teams.TeamsHUD;
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

    public static PlayerUpdateEvents.PlayerHealthUpdate playerHealthUpdate = (player, health, hunger) -> {
        Team team = TeamDB.INSTANCE.getTeam(player);
        if (team != null) {
            List<ServerPlayer> players = team.getOnlinePlayers().stream().filter(other -> !other.equals(player)).collect(Collectors.toList());
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.UPDATE), players);
        }
    };

}
