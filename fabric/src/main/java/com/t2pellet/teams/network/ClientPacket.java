package com.t2pellet.teams.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public abstract class ClientPacket extends Packet {

    public ClientPacket(Minecraft client, FriendlyByteBuf byteBuf) {
        super(byteBuf);
        client.execute(this::execute);
    }

    protected ClientPacket() {
        super();
    }
}
