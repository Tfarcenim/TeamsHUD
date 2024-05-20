package com.t2pellet.teams;

import com.t2pellet.teams.client.TeamsHUDClient;
import com.t2pellet.teams.client.TeamsHUDClientForge;
import com.t2pellet.teams.command.TeamCommand;
import com.t2pellet.teams.config.TomlConfig;
import com.t2pellet.teams.network.PacketHandlerForge;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;

@Mod(TeamsHUD.MODID)
public class TeamsHUDForge {
    
    public TeamsHUDForge() {
        TeamsHUD.LOGGER.info("Teams forge mod init!");
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, SERVER_SPEC);
        IEventBus bus  = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::registerCommand);
        MinecraftForge.EVENT_BUS.addListener(this::login);
        MinecraftForge.EVENT_BUS.addListener(this::logout);
        MinecraftForge.EVENT_BUS.addListener(this::playerClone);

        if (FMLEnvironment.dist.isClient()) {
            TeamsHUDClientForge.init(bus);
        }
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
    
        // Use Forge to bootstrap the Common mod.
        TeamsHUD.init();
    }

    public static final TomlConfig.Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final TomlConfig.Server SERVER;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<TomlConfig.Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(TomlConfig.Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
        final Pair<TomlConfig.Server, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(TomlConfig.Server::new);
        SERVER_SPEC = specPair2.getRight();
        SERVER = specPair2.getLeft();
    }

    private void login(PlayerEvent.PlayerLoggedInEvent event) {
        TeamsHUD.playerConnect((ServerPlayer) event.getPlayer());
    }

    private void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        TeamsHUD.playerDisconnect((ServerPlayer) event.getPlayer());
    }

    private void playerClone(PlayerEvent.Clone event) {
        TeamsHUD.playerClone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getPlayer(),!event.isWasDeath());
    }

    private void registerCommand(RegisterCommandsEvent event) {
        TeamCommand.register(event.getDispatcher());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

}