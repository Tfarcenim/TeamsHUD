package com.t2pellet.teams.network;

import com.t2pellet.teams.network.server.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class PacketHandlerFabric {

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(C2STeamCreatePacket.ID,wrapC2S(C2STeamCreatePacket::new));
        ServerPlayNetworking.registerGlobalReceiver(C2STeamRequestPacket.ID,wrapC2S(C2STeamRequestPacket::new));
        ServerPlayNetworking.registerGlobalReceiver(C2STeamKickPacket.ID,wrapC2S(C2STeamKickPacket::new));
        ServerPlayNetworking.registerGlobalReceiver(C2STeamLeavePacket.ID,wrapC2S(C2STeamLeavePacket::new));
        ServerPlayNetworking.registerGlobalReceiver(C2STeamInvitePacket.ID,wrapC2S(C2STeamInvitePacket::new));
        ServerPlayNetworking.registerGlobalReceiver(C2STeamJoinPacket.ID,wrapC2S(C2STeamJoinPacket::new));
    }

    public static <MSG extends C2SModPacket> ServerPlayNetworking.PlayChannelHandler wrapC2S(Function<FriendlyByteBuf, MSG> decodeFunction) {
        return new ServerHandler<>(decodeFunction);
    }

}
