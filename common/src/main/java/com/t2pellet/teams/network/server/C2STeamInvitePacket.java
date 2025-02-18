package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.IHasTeam;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.PacketLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamInvitePacket implements C2SModPacket<C2STeamInvitePacket> {


    String to;
    public C2STeamInvitePacket(String to) {
        this.to = to;
    }

    public C2STeamInvitePacket(FriendlyByteBuf byteBuf) {
        to = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(this.to);
    }

    public static final PacketLocation<C2STeamInvitePacket> ID = new PacketLocation<>(new ResourceLocation(TeamsHUD.MODID,"team_invite"),C2STeamInvitePacket.class);

    @Override
    public PacketLocation<C2STeamInvitePacket> id() {
        return ID;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        UUID to = player.server.getProfileCache().get(this.to).orElseThrow().getId();

        ServerPlayer toPlayer = TeamsHUD.getServer().getPlayerList().getPlayer(to);

        Team team = ((IHasTeam) player).getTeam();
        if (team == null) {
            TeamsHUD.LOGGER.error(player.getName().getString() + " tried inviting " + toPlayer.getName().getString() + " but they are not in a team..");
        } else {
            try {
                TeamDB.INSTANCE.invitePlayerToTeam(toPlayer, team);
            } catch (Team.TeamException e) {
                TeamsHUD.LOGGER.error(e.getMessage());
            }
        }
    }
}
