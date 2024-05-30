package com.t2pellet.teams.network.client;

import com.mojang.authlib.properties.Property;
import com.t2pellet.teams.client.TeamsHUDClient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class S2CTeamPlayerDataPacket implements S2CModPacket {

    public static final String ID_KEY = "playerUuid";
    public static final String NAME_KEY = "playerName";
    public static final String SKIN_KEY = "playerSkin";
    public static final String SKIN_SIG_KEY = "playerSkinSignature";
    public static final String HEALTH_KEY = "playerHealth";
    public static final String HUNGER_KEY = "playerHunger";
    public static final String TYPE_KEY = "actionType";

    public enum Type {
        ADD,
        UPDATE,
        REMOVE,
    }

    CompoundTag tag = new CompoundTag();

    public S2CTeamPlayerDataPacket(ServerPlayer player, Type type) {
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

    public S2CTeamPlayerDataPacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }

    @Override
    public void handleClient() {
        TeamsHUDClient.handleTeamPlayerDataPacket(tag);
    }
}
