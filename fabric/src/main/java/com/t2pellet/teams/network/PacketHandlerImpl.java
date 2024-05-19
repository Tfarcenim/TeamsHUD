package com.t2pellet.teams.network;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.TeamsHUDFabric;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PacketHandlerImpl implements PacketHandler {

    private final Map<Class<? extends com.t2pellet.teams.network.Packet>, ResourceLocation> idMap;

    PacketHandlerImpl() {
        idMap = new HashMap<>();
    }

    @Override
    public <T extends com.t2pellet.teams.network.Packet> void registerPacket(ResourceLocation id, Class<T> packetClass) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (ClientPacket.class.isAssignableFrom(packetClass)) {
                ClientPlayNetworking.registerGlobalReceiver(id, (minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
                    try {
                        packetClass.getDeclaredConstructor(Minecraft.class, FriendlyByteBuf.class).newInstance(minecraftClient, packetByteBuf);
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                        TeamsHUD.LOGGER.error("Error: Failed to instantiate packet - " + id);
                    }
                });
            }
        }
        if (ServerPacket.class.isAssignableFrom(packetClass)) {
            ServerPlayNetworking.registerGlobalReceiver(id, (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
                try {
                    packetClass.getDeclaredConstructor(MinecraftServer.class, FriendlyByteBuf.class).newInstance(minecraftServer, packetByteBuf);
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    TeamsHUD.LOGGER.error("Error: Failed to instantiate packet - " + id);
                }
            });
        }
        idMap.put(packetClass, id);
    }

    @Override
    public void sendToServer(com.t2pellet.teams.network.Packet packet) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(data);
        ClientPlayNetworking.send(idMap.get(packet.getClass()), data);
    }

    @Override
    public void sendTo(com.t2pellet.teams.network.Packet packet, ServerPlayer player) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        packet.encode(data);
        ServerPlayNetworking.send(player, idMap.get(packet.getClass()), data);
    }

    @Override
    public void sendTo(com.t2pellet.teams.network.Packet packet, ServerPlayer... players) {
        if (players.length == 0) return;

        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        ResourceLocation id = idMap.get(packet.getClass());
        packet.encode(data);
        for (ServerPlayer player : players) ServerPlayNetworking.send(player, id, data);
    }

    @Override
    public void sendInRange(com.t2pellet.teams.network.Packet packet, Entity e, float range) {
        sendInArea(packet, e.getCommandSenderWorld(), e.blockPosition(), range);
    }

    @Override
    public void sendInArea(Packet packet, Level world, BlockPos pos, float range) {
        AABB box = new AABB(pos);
        List<ServerPlayer> nearbyPlayers = world.getEntitiesOfClass(ServerPlayer.class, box.inflate(range), p -> true);
        sendTo(packet, nearbyPlayers.toArray(new ServerPlayer[0]));
    }

}
