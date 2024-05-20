package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.PacketLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamLeavePacket implements C2SModPacket<C2STeamLeavePacket> {


    public C2STeamLeavePacket() {}

    public C2STeamLeavePacket( FriendlyByteBuf byteBuf) {

    }

    @Override
    public void write(FriendlyByteBuf to) {

    }

    public static final PacketLocation<C2STeamLeavePacket> ID =  new PacketLocation<>(new ResourceLocation(TeamsHUD.MODID,"team_leave"), C2STeamLeavePacket.class);

    @Override
    public PacketLocation<C2STeamLeavePacket> id() {
        return ID;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        try {
            TeamDB.INSTANCE.removePlayerFromTeam(player);
        } catch (Team.TeamException ex) {
            TeamsHUD.LOGGER.error(ex.getMessage());
        }
    }
}
