package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.ui.toast.ToastInviteSent;
import com.t2pellet.teams.network.client.S2CModPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class S2CTeamInviteSentPacket implements S2CModPacket {

    String team;
    String player;

    public S2CTeamInviteSentPacket(String team, String player) {
        this.team = team;
        this.player = player;
    }

    public S2CTeamInviteSentPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
        player = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
        to.writeUtf(player);
    }

    public static final ResourceLocation ID = new ResourceLocation(TeamsHUD.MODID,"team_invite_sent");

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void handleClient() {
        Minecraft.getInstance().getToasts().addToast(new ToastInviteSent(team, player));
    }
}
