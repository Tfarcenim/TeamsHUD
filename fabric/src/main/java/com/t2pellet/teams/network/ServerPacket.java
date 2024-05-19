package com.t2pellet.teams.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;

public abstract class ServerPacket extends Packet {

    public ServerPacket(MinecraftServer server, FriendlyByteBuf byteBuf) {
        super(byteBuf);
        server.execute(this::execute);
    }

    protected ServerPacket() {
        super();
    }
}
