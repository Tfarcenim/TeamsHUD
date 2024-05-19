package com.t2pellet.teams.network;

import com.t2pellet.teams.network.client.*;
import com.t2pellet.teams.network.client.S2CTeamInvitedPacket;
import com.t2pellet.teams.network.client.S2CTeamInviteSentPacket;
import com.t2pellet.teams.network.client.S2CTeamInitPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class ClientPacketHandlerFabric {

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamPlayerDataPacket.ID,wrapS2C(S2CTeamPlayerDataPacket::new));
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamDataPacket.ID,wrapS2C(S2CTeamDataPacket::new));
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamRequestedPacket.ID,wrapS2C(S2CTeamRequestedPacket::new));
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamClearPacket.ID,wrapS2C(S2CTeamClearPacket::new));
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamInvitedPacket.ID,wrapS2C(S2CTeamInvitedPacket::new));
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamInviteSentPacket.ID,wrapS2C(S2CTeamInviteSentPacket::new));
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamUpdatePacket.ID,wrapS2C(S2CTeamUpdatePacket::new));
        ClientPlayNetworking.registerGlobalReceiver(S2CTeamInitPacket.ID,wrapS2C(S2CTeamInitPacket::new));
    }

    public static <MSG extends S2CModPacket> ClientPlayNetworking.PlayChannelHandler wrapS2C(Function<FriendlyByteBuf,MSG> decodeFunction) {
        return new ClientHandler<>(decodeFunction);
    }

}
