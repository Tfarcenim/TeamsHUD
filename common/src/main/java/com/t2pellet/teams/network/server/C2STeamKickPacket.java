package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.PacketLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamKickPacket implements C2SModPacket<C2STeamKickPacket> {

    private static final String TEAM_KEY = "teamName";
    private static final String KICKED_KEY = "kickedId";

    String name;
    UUID toKick;

    public C2STeamKickPacket(String team, UUID playerToKick) {
        name = team;
        toKick = playerToKick;
    }

    public C2STeamKickPacket(FriendlyByteBuf byteBuf) {
        name = byteBuf.readUtf();
        toKick = byteBuf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeUUID(toKick);
    }

    public static final PacketLocation<C2STeamKickPacket> ID = new PacketLocation<>(new ResourceLocation(TeamsHUD.MODID,"team_kick"),C2STeamKickPacket.class);

    @Override
    public PacketLocation<C2STeamKickPacket> id() {
        return ID;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        Team team = TeamDB.INSTANCE.getTeam(name);
        if (player != null && team.playerHasPermissions(player)) {
            ServerPlayer kicked = TeamsHUD.getServer().getPlayerList().getPlayer(toKick);
            try {
                TeamDB.INSTANCE.removePlayerFromTeam(kicked);
            } catch (Team.TeamException ex) {
                TeamsHUD.LOGGER.error(ex.getMessage());
            }
        } else {
            TeamsHUD.LOGGER.error("Received packet to kick player, but the sender did not have permissions");
        }
    }
}
