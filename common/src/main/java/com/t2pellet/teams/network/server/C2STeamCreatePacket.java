package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamCreatePacket implements C2SModPacket {

    String team;

    public C2STeamCreatePacket(String team) {
        this.team =  team;
    }

    public C2STeamCreatePacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        try {
            TeamDB.INSTANCE.addTeam(team, player);
        } catch (Team.TeamException e) {
            TeamsHUD.LOGGER.error(e.getMessage());
        }
    }

    public static final ResourceLocation ID = new ResourceLocation(TeamsHUD.MODID,"team_create");

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
