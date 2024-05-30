package com.t2pellet.teams.network.client;

import com.t2pellet.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamClearPacket implements S2CModPacket {

    public S2CTeamClearPacket() {
    }

    public S2CTeamClearPacket(FriendlyByteBuf byteBuf) {
    }

    @Override
    public void write(FriendlyByteBuf to) {

    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.reset();
    }
}
