package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastRequested;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class TeamRequestedPacket extends ClientPacket {

    private static final String NAME_KEY = "playerName";
    private static final String ID_KEY = "playerId";

    public TeamRequestedPacket(String name, UUID id) {
        tag.putString(NAME_KEY, name);
        tag.putUUID(ID_KEY, id);
    }

    public TeamRequestedPacket(Minecraft client, FriendlyByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        String name = tag.getString(NAME_KEY);
        UUID id = tag.getUUID(ID_KEY);
        TeamsModClient.client.getToasts().addToast(new ToastRequested(ClientTeam.INSTANCE.getName(), name, id));
    }
}
