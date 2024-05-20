package com.t2pellet.teams.platform;

import com.t2pellet.teams.client.TeamsHUDClient;
import com.t2pellet.teams.config.TomlConfig;
import com.t2pellet.teams.network.PacketHandlerForge;
import com.t2pellet.teams.network.PacketLocation;
import com.t2pellet.teams.network.client.S2CModPacket;
import com.t2pellet.teams.network.server.C2SModPacket;
import com.t2pellet.teams.platform.services.IPlatformHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.function.Function;

public class ForgePlatformHelper implements IPlatformHelper {

    protected final MultiloaderConfig config = new TomlConfig();

    @Override
    public Platform getPlatform() {
        return Platform.FORGE;
    }

    @Override
    public PhysicalSide getPhysicalSide() {
        switch (FMLEnvironment.dist) {
            case CLIENT -> {
                return PhysicalSide.CLIENT;
            }
            case DEDICATED_SERVER -> {
                return PhysicalSide.SERVER;
            }
        }
        return null;
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public MultiloaderConfig getConfig() {
        return config;
    }

    @Override
    public <MSG extends S2CModPacket<MSG>> void sendToClient(S2CModPacket<MSG> msg, ServerPlayer player) {
        PacketHandlerForge.sendToClient(msg,player);
    }

    @Override
    public <MSG extends C2SModPacket<MSG>> void sendToServer(C2SModPacket<MSG> msg) {
        PacketHandlerForge.sendToServer(msg);
    }

    @Override
    public void registerKeyBinding(KeyMapping keyMapping) {
        TeamsHUDClient.registerKeybinding(keyMapping);
    }

    int i;

    @Override
    public <MSG extends S2CModPacket<MSG>> void registerClientMessage(PacketLocation<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation.clazz(), MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

    @Override
    public <MSG extends C2SModPacket<MSG>> void registerServerMessage(PacketLocation<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation.clazz(), MSG::write, reader, PacketHandlerForge.wrapC2S());
    }
}