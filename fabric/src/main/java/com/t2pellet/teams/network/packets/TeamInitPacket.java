package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class TeamInitPacket extends ClientPacket {

    private static final String NAME_KEY = "teamName";
    private static final String PERMS_KEY = "teamPerms";

    public TeamInitPacket(String name, boolean hasPermissions) {
        tag.putString(NAME_KEY, name);
        tag.putBoolean(PERMS_KEY, hasPermissions);
    }

    public TeamInitPacket(Minecraft client, FriendlyByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        ClientTeam.INSTANCE.init(tag.getString(NAME_KEY), tag.getBoolean(PERMS_KEY));
    }
}
