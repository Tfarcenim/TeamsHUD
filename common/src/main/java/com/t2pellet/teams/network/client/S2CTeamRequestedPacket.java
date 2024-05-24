package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastRequested;
import com.t2pellet.teams.network.PacketLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class S2CTeamRequestedPacket implements S2CModPacket<S2CTeamRequestedPacket> {

    private static final String NAME_KEY = "playerName";
    private static final String ID_KEY = "playerId";

    CompoundTag tag = new CompoundTag();
    public S2CTeamRequestedPacket(String name, UUID id) {
        tag.putString(NAME_KEY, name);
        tag.putUUID(ID_KEY, id);
    }

    public S2CTeamRequestedPacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }

    public static final PacketLocation<S2CTeamRequestedPacket> ID = new PacketLocation<>(TeamsHUD.id("team_requested"), S2CTeamRequestedPacket.class);

    @Override
    public PacketLocation<S2CTeamRequestedPacket> id() {
        return ID;
    }

    @Override
    public void handleClient() {
        String name = tag.getString(NAME_KEY);
        UUID id = tag.getUUID(ID_KEY);
        Minecraft.getInstance().getToasts().addToast(new ToastRequested(ClientTeam.INSTANCE.getName(), name, id));
    }
}
