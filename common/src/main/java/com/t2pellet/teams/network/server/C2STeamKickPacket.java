package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.ModTeam;
import com.t2pellet.teams.core.TeamDB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamKickPacket implements C2SModPacket {

    String name;
    UUID toKick;

    public C2STeamKickPacket(String team, UUID playerToKick) {
        name = team;
        toKick = playerToKick;
    }

    public C2STeamKickPacket(FriendlyByteBuf byteBuf) {
        name = byteBuf.readUtf();
        toKick = byteBuf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeUUID(toKick);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        ModTeam team = TeamDB.getOrMakeDefault(player.server).getTeam(name);
        if (player != null && team.playerHasPermissions(player)) {
            ServerPlayer kicked = player.server.getPlayerList().getPlayer(toKick);
            try {
                TeamDB.getOrMakeDefault(player.server).removePlayerFromTeam(kicked);
            } catch (ModTeam.TeamException ex) {
                TeamsHUD.LOGGER.error(ex.getMessage());
            }
        } else {
            TeamsHUD.LOGGER.error("Received packet to kick player, but the sender did not have permissions");
        }
    }
}
