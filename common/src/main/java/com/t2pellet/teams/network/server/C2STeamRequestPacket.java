package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.client.S2CTeamRequestedPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamRequestPacket implements C2SModPacket {


    String name;
    public C2STeamRequestPacket(String name) {
        this.name = name;
    }

    public C2STeamRequestPacket(FriendlyByteBuf byteBuf) {
        name = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        Team team = TeamDB.getOrMakeDefault(player.server).getTeam(name);
        if (team == null) {
            throw new IllegalArgumentException("Got request to join team " + name + ", but that team doesn't exist");
        } else {
            // Get first online player in list of seniority
            var playerManager = player.server.getPlayerList();
            ServerPlayer seniorPlayer = team.getPlayerUuids()
                    .filter(p -> playerManager.getPlayer(p) != null)
                    .map(playerManager::getPlayer)
                    .findFirst().orElseThrow();
            Services.PLATFORM.sendToClient(new S2CTeamRequestedPacket(name, player.getUUID()), seniorPlayer);
        }
    }
}
