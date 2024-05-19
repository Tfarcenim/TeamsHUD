package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.ServerPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class TeamKickPacket extends ServerPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String SENDER_KEY = "senderId";
    private static final String KICKED_KEY = "kickedId";

    public TeamKickPacket(String team, UUID sender, UUID playerToKick) {
        tag.putString(TEAM_KEY, team);
        tag.putUUID(SENDER_KEY, sender);
        tag.putUUID(KICKED_KEY, playerToKick);
    }

    public TeamKickPacket(MinecraftServer server, FriendlyByteBuf byteBuf) {
        super(server, byteBuf);
    }

    @Override
    public void execute() {
        Team team = TeamDB.INSTANCE.getTeam(tag.getString(TEAM_KEY));
        ServerPlayer sender = TeamsHUD.getServer().getPlayerList().getPlayer(tag.getUUID(SENDER_KEY));
        if (sender != null && team.playerHasPermissions(sender)) {
            ServerPlayer kicked = TeamsHUD.getServer().getPlayerList().getPlayer(tag.getUUID(KICKED_KEY));
            try {
                TeamDB.INSTANCE.removePlayerFromTeam(kicked);
            } catch (Team.TeamException ex) {
                TeamsHUDFabric.LOGGER.error(ex.getMessage());
            }
        } else {
            TeamsHUDFabric.LOGGER.error("Received packet to kick player, but the sender did not have permissions");
        }
    }
}
