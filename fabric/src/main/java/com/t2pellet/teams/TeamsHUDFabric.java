package com.t2pellet.teams;

import com.t2pellet.teams.command.TeamCommand;
import com.t2pellet.teams.config.TeamsConfig;
import com.t2pellet.teams.network.PacketHandlerFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

public class TeamsHUDFabric implements ModInitializer {

	private static TeamsConfig config;

	public static TeamsConfig getConfig() {
		return config;
	}

	@Override
	public void onInitialize() {
		TeamsHUD.LOGGER.info("Teams mod init!");

		ServerLifecycleEvents.SERVER_STARTED.register(TeamsHUD::onServerStarted);
		ServerLifecycleEvents.SERVER_STOPPED.register(TeamsHUD::onServerStopped);
		// Config registration
		AutoConfig.register(TeamsConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(TeamsConfig.class).getConfig();
		// Command registration
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			TeamCommand.register(dispatcher);
		});
		// Event hooks
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.player;
			TeamsHUD.playerConnect(player);
		});
		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayer player = handler.player;
			TeamsHUD.playerDisconnect(player);
		});
		ServerPlayerEvents.COPY_FROM.register(TeamsHUD::playerClone);
		PacketHandlerFabric.registerPackets();
		TeamsHUD.init();
	}
}
