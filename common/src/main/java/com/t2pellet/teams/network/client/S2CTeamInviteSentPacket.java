package com.t2pellet.teams.network.client;

import com.t2pellet.teams.client.TeamsHUDClient;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamInviteSentPacket implements S2CModPacket {

    String team;
    String player;

    public S2CTeamInviteSentPacket(String team, String player) {
        this.team = team;
        this.player = player;
    }

    public S2CTeamInviteSentPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
        player = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
        to.writeUtf(player);
    }

    @Override
    public void handleClient() {
        TeamsHUDClient.handleTeamInviteSentPacket(team,player);
    }
}
