package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastRequested;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public class S2CTeamRequestedPacket implements S2CModPacket {

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

    public static final ResourceLocation ID = new ResourceLocation(TeamsHUD.MODID,"team_requested");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleClient() {
        String name = tag.getString(NAME_KEY);
        UUID id = tag.getUUID(ID_KEY);
        Minecraft.getInstance().getToasts().addToast(new ToastRequested(ClientTeam.INSTANCE.getName(), name, id));
    }
}
