package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2CTeamInitPacket implements S2CModPacket {

    private final String name;
    private final boolean perms;

    public S2CTeamInitPacket(String name, boolean hasPermissions) {
        this.name = name;
        this.perms = hasPermissions;
    }

    public S2CTeamInitPacket(FriendlyByteBuf byteBuf) {
        name = byteBuf.readUtf();
        perms = byteBuf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeBoolean(perms);
    }

    public static final ResourceLocation ID = new ResourceLocation(TeamsHUD.MODID,"team_init");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.init(name,perms);
    }
}