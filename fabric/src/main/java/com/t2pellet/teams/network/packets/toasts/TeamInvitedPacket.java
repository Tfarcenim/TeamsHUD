package com.t2pellet.teams.network.packets.toasts;

import com.t2pellet.teams.client.TeamsModClient;
import com.t2pellet.teams.client.ui.toast.ToastInvited;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

public class TeamInvitedPacket extends ClientPacket {

    private static final String TEAM_KEY = "teamName";

    public TeamInvitedPacket(Team team) {
        tag.putString(TEAM_KEY, team.getName());
    }

    public TeamInvitedPacket(Minecraft client, FriendlyByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        String team = tag.getString(TEAM_KEY);
        TeamsModClient.client.getToasts().addToast(new ToastInvited(team));
    }
}
