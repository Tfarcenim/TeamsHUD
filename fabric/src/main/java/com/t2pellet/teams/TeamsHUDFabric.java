package com.t2pellet.teams;

import com.t2pellet.teams.command.TeamCommand;
import com.t2pellet.teams.config.TeamsConfig;
import com.t2pellet.teams.core.EventHandlers;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.events.AdvancementEvents;
import com.t2pellet.teams.events.PlayerUpdateEvents;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.PacketHandlerFabric;
import com.t2pellet.teams.network.TeamPackets;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.storage.LevelResource;

import java.io.File;
import java.io.IOException;

public class TeamsHUDFabric implements ModInitializer {

	private static TeamsConfig config;

	public static TeamsConfig getConfig() {
		return config;
	}

	@Override
	public void onInitialize() {
		TeamsHUD.LOGGER.info("Teams mod init!");

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			// Get server instance
			TeamsHUD.server = server;
			TeamsHUD.scoreboard = server.getScoreboard();
			// Load saved teams
			try {
				File saveFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), "teams.dat");
				CompoundTag element = NbtIo.read(saveFile);
				if (element != null) {
					TeamDB.INSTANCE.fromNBT(element);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			// Save teams
			try {
				File saveFile = new File(server.getWorldPath(LevelResource.ROOT).toFile(), "teams.dat");
				CompoundTag element = TeamDB.INSTANCE.toNBT();
				NbtIo.write(element, saveFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		// Config registration
		AutoConfig.register(TeamsConfig.class, JanksonConfigSerializer::new);
		config = AutoConfig.getConfigHolder(TeamsConfig.class).getConfig();
		// Packet registration
		PacketHandler.register(TeamPackets.class);
		// Command registration
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			TeamCommand.register(dispatcher);
		});
		// Event hooks
		ServerPlayConnectionEvents.JOIN.register(EventHandlers.playerConnect);
		ServerPlayConnectionEvents.DISCONNECT.register(EventHandlers.playerDisconnect);
		PlayerUpdateEvents.PLAYER_HEALTH_UPDATE.register(EventHandlers.playerHealthUpdate);
		PlayerUpdateEvents.PLAYER_COPY.register(EventHandlers.playerCopy);
		AdvancementEvents.ADVANCEMENT_GRANTED.register(EventHandlers.playerAdvancement);
		PacketHandlerFabric.registerPackets();
		TeamsHUD.init();
	}
}
