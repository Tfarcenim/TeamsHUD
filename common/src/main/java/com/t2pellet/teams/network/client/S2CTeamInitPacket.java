package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.PacketLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2CTeamInitPacket implements S2CModPacket<S2CTeamInitPacket> {

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

    public static final PacketLocation<S2CTeamInitPacket> ID = new PacketLocation<>(new ResourceLocation(TeamsHUD.MODID,"team_init"), S2CTeamInitPacket.class);

    @Override
    public PacketLocation<S2CTeamInitPacket> id() {
        return ID;
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.init(name,perms);
    }
}
