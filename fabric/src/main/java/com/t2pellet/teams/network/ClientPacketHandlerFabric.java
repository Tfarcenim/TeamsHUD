package com.t2pellet.teams.network;

import com.t2pellet.teams.network.client.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class ClientPacketHandlerFabric {

    public static <MSG extends S2CModPacket<MSG>> ClientPlayNetworking.PlayChannelHandler wrapS2C(Function<FriendlyByteBuf,MSG> decodeFunction) {
        return new ClientHandler<>(decodeFunction);
    }

}
