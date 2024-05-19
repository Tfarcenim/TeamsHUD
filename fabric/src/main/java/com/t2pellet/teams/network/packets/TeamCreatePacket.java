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

public class TeamCreatePacket extends ServerPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String PLAYER_KEY = "playerId";

    public TeamCreatePacket(String team, UUID player) {
        tag.putString(TEAM_KEY, team);
        tag.putUUID(PLAYER_KEY, player);
    }

    public TeamCreatePacket(MinecraftServer server, FriendlyByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        ServerPlayer player = TeamsHUD.getServer().getPlayerList().getPlayer(tag.getUUID(PLAYER_KEY));
        try {
            TeamDB.INSTANCE.addTeam(tag.getString(TEAM_KEY), player);
        } catch (Team.TeamException e) {
            TeamsHUDFabric.LOGGER.error(e.getMessage());
        }
    }
}
