package com.t2pellet.teams.network;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.network.client.S2CModPacket;
import com.t2pellet.teams.network.server.C2SModPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class PacketHandlerForge {

    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(TeamsHUD.MODID, TeamsHUD.MODID), () -> "1.0", s -> true, s -> true);;

    public static <MSG extends S2CModPacket<MSG>> BiConsumer<MSG, Supplier<NetworkEvent.Context>> wrapS2C() {
        return ((msg, contextSupplier) -> {
            contextSupplier.get().enqueueWork(msg::handleClient);
            contextSupplier.get().setPacketHandled(true);
        });
    }

    public static <MSG extends C2SModPacket<MSG>> BiConsumer<MSG, Supplier<NetworkEvent.Context>> wrapC2S() {
        return ((msg, contextSupplier) -> {
            ServerPlayer player = contextSupplier.get().getSender();
            contextSupplier.get().enqueueWork(() -> msg.handleServer(player));
            contextSupplier.get().setPacketHandled(true);
        });
    }

    public static <MSG> void sendToClient(MSG packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendToServer(MSG packet) {
        INSTANCE.sendToServer(packet);
    }
}
