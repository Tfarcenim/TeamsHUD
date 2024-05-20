package com.t2pellet.teams.platform;

import com.t2pellet.teams.network.ClientPacketHandlerFabric;
import com.t2pellet.teams.network.PacketHandlerFabric;
import com.t2pellet.teams.network.client.S2CModPacket;
import com.t2pellet.teams.network.server.C2SModPacket;
import com.t2pellet.teams.platform.services.IPlatformHelper;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FabricPlatformHelper implements IPlatformHelper {
    Config<?> config = new FabricConfig<>();
    @Override
    public Platform getPlatform() {
        return Platform.FABRIC;
    }

    @Override
    public PhysicalSide getPhysicalSide() {
        switch (FabricLoader.getInstance().getEnvironmentType()) {
            case CLIENT -> {
                return PhysicalSide.CLIENT;
            }
            case SERVER -> {
                return PhysicalSide.SERVER;
            }
        }
        return null;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Config<?> getConfig() {
        return config;
    }

    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        msg.write(buf);
        ServerPlayNetworking.send(player, msg.id(), buf);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        msg.write(buf);
        ClientPlayNetworking.send(msg.id(), buf);
    }

    @Override
    public void registerKeyBinding(KeyMapping keyMapping) {
        KeyBindingHelper.registerKeyBinding(keyMapping);
    }

    @Override
    public <MSG extends S2CModPacket> void clientMessage(ResourceLocation id, Class<MSG> msgClass, BiConsumer<MSG, FriendlyByteBuf> writer, Function<FriendlyByteBuf, MSG> reader) {
        ClientPlayNetworking.registerGlobalReceiver(id, ClientPacketHandlerFabric.wrapS2C(reader));
    }

    @Override
    public <MSG extends C2SModPacket> void serverMessage(ResourceLocation id, Class<MSG> msgClass, BiConsumer<MSG, FriendlyByteBuf> writer, Function<FriendlyByteBuf, MSG> reader) {
        ServerPlayNetworking.registerGlobalReceiver(id, PacketHandlerFabric.wrapC2S(reader));
    }
}
