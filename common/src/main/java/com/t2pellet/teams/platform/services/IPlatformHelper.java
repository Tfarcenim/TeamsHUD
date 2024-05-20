package com.t2pellet.teams.platform.services;

import com.t2pellet.teams.network.PacketLocation;
import com.t2pellet.teams.network.client.S2CModPacket;
import com.t2pellet.teams.network.server.C2SModPacket;
import com.t2pellet.teams.platform.Config;
import com.t2pellet.teams.platform.PhysicalSide;
import com.t2pellet.teams.platform.Platform;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    Platform getPlatform();
    PhysicalSide getPhysicalSide();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    Config<?> getConfig();

    <MSG extends S2CModPacket<MSG>> void sendToClient(S2CModPacket<MSG> msg, ServerPlayer player);

    default <MSG extends S2CModPacket<MSG>> void sendToClients(S2CModPacket<MSG> msg, Collection<ServerPlayer> playerList) {
        playerList.forEach(player -> sendToClient(msg,player));
    }
    <MSG extends C2SModPacket<MSG>> void sendToServer(C2SModPacket<MSG> msg);

    void registerKeyBinding(KeyMapping keyMapping);

    <MSG extends S2CModPacket<MSG>> void registerClientMessage(PacketLocation<MSG> packetLocation, BiConsumer<MSG, FriendlyByteBuf> writer, Function<FriendlyByteBuf,MSG> reader);

    <MSG extends C2SModPacket<MSG>> void registerServerMessage(PacketLocation<MSG> packetLocation, BiConsumer<MSG, FriendlyByteBuf> writer, Function<FriendlyByteBuf,MSG> reader);

}