package com.t2pellet.teams.network.packets;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TeamPlayerDataPacket extends ClientPacket {

    private static final String ID_KEY = "playerUuid";
    private static final String NAME_KEY = "playerName";
    private static final String SKIN_KEY = "playerSkin";
    private static final String SKIN_SIG_KEY = "playerSkinSignature";
    private static final String HEALTH_KEY = "playerHealth";
    private static final String HUNGER_KEY = "playerHunger";
    private static final String TYPE_KEY = "actionType";

    public enum Type {
        ADD,
        UPDATE,
        REMOVE,
    }

    public TeamPlayerDataPacket(ServerPlayer player, Type type) {
        var health = player.getHealth();
        var hunger = player.getFoodData().getFoodLevel();
        tag.putUUID(ID_KEY, player.getUUID());
        tag.putString(TYPE_KEY, type.toString());
        switch (type) {
            case ADD -> {
                tag.putString(NAME_KEY, player.getName().getString());
                var properties = player.getGameProfile().getProperties();
                Property skin = null;
                if (properties.containsKey("textures")) {
                    skin = properties.get("textures").iterator().next();
                }
                tag.putString(SKIN_KEY, skin != null ? skin.getValue() : "");
                tag.putString(SKIN_SIG_KEY, skin != null ?
                        skin.getSignature() != null ? skin.getSignature() : ""
                        : "");
                tag.putFloat(HEALTH_KEY, health);
                tag.putInt(HUNGER_KEY, hunger);
            }
            case UPDATE -> {
                tag.putFloat(HEALTH_KEY, health);
                tag.putInt(HUNGER_KEY, hunger);
            }
        }
    }

    public TeamPlayerDataPacket(Minecraft client, FriendlyByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        UUID uuid = tag.getUUID(ID_KEY);
        switch (Type.valueOf(tag.getString(TYPE_KEY))) {
            case ADD -> {
                if (ClientTeam.INSTANCE.hasPlayer(uuid)) return;

                String name = tag.getString(NAME_KEY);
                float health = tag.getFloat(HEALTH_KEY);
                int hunger = tag.getInt(HUNGER_KEY);

                // Get skin data
                String skinVal = tag.getString(SKIN_KEY);
                String skinSig = tag.getString(SKIN_SIG_KEY);
                // Force download
                if (!skinVal.isEmpty()) {
                    GameProfile dummy = new GameProfile(UUID.randomUUID(), "");
                    dummy.getProperties().put("textures", new Property("textures", skinVal, skinSig));
                    TeamsModClient.client.getSkinManager().registerSkins(dummy, (type, id, texture) -> {
                        if (type == MinecraftProfileTexture.Type.SKIN) {
                            ClientTeam.INSTANCE.addPlayer(uuid, name, id, health, hunger);
                        }
                    }, false);
                } else {
                    ClientTeam.INSTANCE.addPlayer(uuid, name, DefaultPlayerSkin.getDefaultSkin(uuid), health, hunger);
                }
            }
            case UPDATE -> {
                float health = tag.getFloat(HEALTH_KEY);
                int hunger = tag.getInt(HUNGER_KEY);
                ClientTeam.INSTANCE.updatePlayer(uuid, health, hunger);
            }
            case REMOVE -> {
                ClientTeam.INSTANCE.removePlayer(uuid);
            }
        }
    }
}
