package com.t2pellet.teams.network.client;

import com.t2pellet.teams.client.TeamsHUDClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;


public class S2CTeamRequestedPacket implements S2CModPacket {


    CompoundTag tag = new CompoundTag();
    public S2CTeamRequestedPacket(String name, UUID id) {
        tag.putString(S2CTeamPlayerDataPacket.NAME_KEY, name);
        tag.putUUID(S2CTeamPlayerDataPacket.ID_KEY, id);
    }

    public S2CTeamRequestedPacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }


    @Override
    public void handleClient() {
        String name = tag.getString(S2CTeamPlayerDataPacket.NAME_KEY);
        UUID id = tag.getUUID(S2CTeamPlayerDataPacket.ID_KEY);
        TeamsHUDClient.handleTeamRequestedPacket(name,id);
    }
}
