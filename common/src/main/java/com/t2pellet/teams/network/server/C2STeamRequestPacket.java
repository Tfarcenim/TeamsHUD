package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.PacketLocation;
import com.t2pellet.teams.network.client.S2CTeamRequestedPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamRequestPacket implements C2SModPacket<C2STeamRequestPacket> {


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

    public static final PacketLocation<C2STeamRequestPacket> ID = new PacketLocation<>(TeamsHUD.id("team_request"), C2STeamRequestPacket.class);
    @Override
    public PacketLocation<C2STeamRequestPacket> id() {
        return ID;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        Team team = TeamDB.INSTANCE.getTeam(name);
        if (team == null) {
            throw new IllegalArgumentException("Got request to join team " + name + ", but that team doesn't exist");
        } else {
            // Get first online player in list of seniority
            var playerManager = TeamsHUD.getServer().getPlayerList();
            ServerPlayer seniorPlayer = team.getPlayerUuids()
                    .filter(p -> playerManager.getPlayer(p) != null)
                    .map(playerManager::getPlayer)
                    .findFirst().orElseThrow();
            Services.PLATFORM.sendToClient(new S2CTeamRequestedPacket(name, player.getUUID()), seniorPlayer);
        }
    }
}
