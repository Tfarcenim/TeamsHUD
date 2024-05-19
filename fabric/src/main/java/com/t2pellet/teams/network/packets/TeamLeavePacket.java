package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TeamLeavePacket extends ServerPacket {

    private static final String PLAYER_KEY = "playerId";

    public TeamLeavePacket(UUID player) {
        tag.putUUID(PLAYER_KEY, player);
    }

    public TeamLeavePacket(MinecraftServer server, FriendlyByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        ServerPlayer player = TeamsHUDFabric.getServer().getPlayerList().getPlayer(tag.getUUID(PLAYER_KEY));
        try {
            TeamDB.INSTANCE.removePlayerFromTeam(player);
        } catch (Team.TeamException ex) {
            TeamsHUDFabric.LOGGER.error(ex.getMessage());
        }
    }
}
