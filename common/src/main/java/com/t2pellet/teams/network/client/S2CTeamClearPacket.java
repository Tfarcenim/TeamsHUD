package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.PacketLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2CTeamClearPacket implements S2CModPacket<S2CTeamClearPacket> {

    public S2CTeamClearPacket() {
    }

    public S2CTeamClearPacket(FriendlyByteBuf byteBuf) {
    }

    public static final PacketLocation<S2CTeamClearPacket> ID = new PacketLocation<>(TeamsHUD.id("team_clear"),S2CTeamClearPacket.class);

    @Override
    public PacketLocation<S2CTeamClearPacket> id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf to) {

    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.reset();
    }
}
