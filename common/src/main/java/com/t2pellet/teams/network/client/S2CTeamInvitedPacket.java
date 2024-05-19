package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.ui.toast.ToastInvited;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.network.client.S2CModPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2CTeamInvitedPacket implements S2CModPacket {

    private final String team;

    public S2CTeamInvitedPacket(Team team) {
        this.team = team.getName();
    }

    public S2CTeamInvitedPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void handleClient() {
        Minecraft.getInstance().getToasts().addToast(new ToastInvited(team));
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

    public static final ResourceLocation ID = new ResourceLocation(TeamsHUD.MODID,"team_invited");

    @Override
    public ResourceLocation id() {
        return ID;
    }
}
