package com.t2pellet.teams.network;

import com.t2pellet.teams.network.client.S2CModPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class ClientHandler<MSG extends S2CModPacket<MSG>> implements ClientPlayNetworking.PlayChannelHandler {

    private final Function<FriendlyByteBuf, MSG> decodeFunction;

    public ClientHandler(Function<FriendlyByteBuf, MSG> decodeFunction) {
        this.decodeFunction = decodeFunction;
    }

    @Override
    public void receive(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
        MSG decode = decodeFunction.apply(buf);
        client.execute(decode::handleClient);
    }
}
