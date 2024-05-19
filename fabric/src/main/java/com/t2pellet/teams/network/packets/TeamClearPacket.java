package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class TeamClearPacket extends ClientPacket {

    public TeamClearPacket() {
    }

    public TeamClearPacket(Minecraft client, FriendlyByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        ClientTeam.INSTANCE.reset();
    }
}
