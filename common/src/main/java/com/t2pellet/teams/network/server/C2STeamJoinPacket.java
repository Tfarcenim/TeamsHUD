package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamJoinPacket implements C2SModPacket {

    String team;
    public C2STeamJoinPacket(String team) {
        this.team = team;
    }

    public C2STeamJoinPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }
    @Override
    public void handleServer(ServerPlayer player) {
        Team team = TeamDB.INSTANCE.getTeam(this.team);
        try {
            TeamDB.INSTANCE.addPlayerToTeam(player, team);
        } catch (Team.TeamException ex) {
            TeamsHUD.LOGGER.error("Failed to join team: " + team);
            TeamsHUD.LOGGER.error(ex.getMessage());
        }
    }
}
