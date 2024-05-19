package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2CTeamClearPacket implements S2CModPacket {

    public S2CTeamClearPacket() {
    }

    public S2CTeamClearPacket(FriendlyByteBuf byteBuf) {
    }

    public static final ResourceLocation ID = new ResourceLocation(TeamsHUD.MODID,"team_clear");

    @Override
    public ResourceLocation id() {
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
