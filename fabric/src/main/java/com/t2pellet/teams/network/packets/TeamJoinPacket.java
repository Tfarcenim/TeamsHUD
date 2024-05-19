package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TeamJoinPacket extends ServerPacket {

    private static final String ID_KEY = "playerId";
    private static final String TEAM_KEY = "teamName";

    public TeamJoinPacket(UUID playerId, String team) {
        tag.putUUID(ID_KEY, playerId);
        tag.putString(TEAM_KEY, team);
    }

    public TeamJoinPacket(MinecraftServer server, FriendlyByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        UUID id = tag.getUUID(ID_KEY);
        ServerPlayer player = TeamsHUD.getServer().getPlayerList().getPlayer(id);
        String teamName = tag.getString(TEAM_KEY);
        Team team = TeamDB.INSTANCE.getTeam(teamName);
        try {
            TeamDB.INSTANCE.addPlayerToTeam(player, team);
        } catch (Team.TeamException ex) {
            TeamsHUDFabric.LOGGER.error("Failed to join team: " + teamName);
            TeamsHUDFabric.LOGGER.error(ex.getMessage());
        }
    }
}
