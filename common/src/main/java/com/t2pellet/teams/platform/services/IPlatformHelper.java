package com.t2pellet.teams.platform.services;

import com.t2pellet.teams.network.client.S2CModPacket;
import com.t2pellet.teams.network.server.C2SModPacket;
import com.t2pellet.teams.platform.Config;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

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

    void sendToClient(S2CModPacket msg, ServerPlayer player);

    default void sendToClients(S2CModPacket msg, Collection<ServerPlayer> playerList) {
        playerList.forEach(player -> sendToClient(msg,player));
    }
    void sendToServer(C2SModPacket msg);

    void registerKeyBinding(Optional<KeyMapping> keyMapping);

}