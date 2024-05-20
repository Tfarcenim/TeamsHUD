package com.t2pellet.teams.network;

import com.t2pellet.teams.network.server.C2SModPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

import java.util.function.Function;

public class ServerHandler<MSG extends C2SModPacket<MSG>> implements ServerPlayNetworking.PlayChannelHandler {

    private final Function<FriendlyByteBuf, MSG> packetDecoder;

    public ServerHandler(Function<FriendlyByteBuf,MSG> packetDecoder) {
        this.packetDecoder = packetDecoder;
    }

    @Override
    public void receive(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
        MSG decode = packetDecoder.apply(buf);
        server.execute(() -> decode.handleServer(player));
    }
}
