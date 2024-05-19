package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamLeavePacket implements C2SModPacket {


    public C2STeamLeavePacket() {}

    public C2STeamLeavePacket( FriendlyByteBuf byteBuf) {

    }

    @Override
    public void write(FriendlyByteBuf to) {

    }

    public static final ResourceLocation ID =  new ResourceLocation(TeamsHUD.MODID,"team_leave");

    @Override
    public ResourceLocation id() {
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
