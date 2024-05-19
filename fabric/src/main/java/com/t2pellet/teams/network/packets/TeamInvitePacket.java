package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.core.IHasTeam;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TeamInvitePacket extends ServerPacket {

    private static final String FROM_KEY = "fromId";
    private static final String TO_KEY = "toId";

    public TeamInvitePacket(UUID from, String to) {
        tag.putUUID(FROM_KEY, from);
        tag.putString(TO_KEY, to);
    }

    public TeamInvitePacket(MinecraftServer server, FriendlyByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        UUID from = tag.getUUID(FROM_KEY);
        UUID to = TeamsHUD.getServer().getProfileCache().get(tag.getString(TO_KEY)).orElseThrow().getId();

        ServerPlayer fromPlayer = TeamsHUD.getServer().getPlayerList().getPlayer(from);
        ServerPlayer toPlayer = TeamsHUD.getServer().getPlayerList().getPlayer(to);

        Team team = ((IHasTeam) fromPlayer).getTeam();
        if (team == null) {
            TeamsHUDFabric.LOGGER.error(fromPlayer.getName().getString() + " tried inviting " + toPlayer.getName().getString() + " but they are not in a team..");
        } else {
            try {
                TeamDB.INSTANCE.invitePlayerToTeam(toPlayer, team);
            } catch (Team.TeamException e) {
                TeamsHUDFabric.LOGGER.error(e.getMessage());
            }
        }
    }
}
