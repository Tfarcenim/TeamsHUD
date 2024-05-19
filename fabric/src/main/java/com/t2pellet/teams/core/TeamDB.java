package com.t2pellet.teams.core;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamDataPacket;
import com.t2pellet.teams.network.packets.toasts.TeamInvitedPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class TeamDB {

    public static final TeamDB INSTANCE = new TeamDB();
    private static final String TEAMS_KEY = "teams";

    private Map<String, Team> teams = new HashMap<>();

    private TeamDB() {
    }

    public Stream<Team> getTeams() {
        return teams.values().stream();
    }

    public void addTeam(Team team) throws Team.TeamException {
        if (teams.containsKey(team.getName())) {
            throw new Team.TeamException(new TranslatableComponent("teams.error.duplicateteam"));
        }
        teams.put(team.getName(), team);
        ServerPlayer[] players = TeamsHUD.getServer().getPlayerList().getPlayers().toArray(ServerPlayer[]::new);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ADD, team.name), players);
    }

    public Team addTeam(String name, @Nullable ServerPlayer creator) throws Team.TeamException {
        if (creator != null && ((IHasTeam) creator).hasTeam()) {
            throw new Team.TeamException(new TranslatableComponent("teams.error.alreadyinteam", creator.getName().getString()));
        }
        Team team = new Team.Builder(name).complete();
        addTeam(team);
        if (creator != null) {
            team.addPlayer(creator);
        }
        ServerPlayer[] players = TeamsHUD.getServer().getPlayerList().getPlayers().toArray(ServerPlayer[]::new);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ONLINE, team.name), players);
        return team;
    }

    public void removeTeam(Team team) {
        teams.remove(team.getName());
        TeamsHUD.getScoreboard().removePlayerTeam(TeamsHUD.getScoreboard().getPlayerTeam(team.getName()));
        team.clear();
        ServerPlayer[] players = TeamsHUD.getServer().getPlayerList().getPlayers().toArray(ServerPlayer[]::new);
        PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.REMOVE, team.name), players);
    }

    public boolean isEmpty() {
        return teams.isEmpty();
    }

    public boolean hasTeam(String team) {
        return teams.containsKey(team);
    }

    public Team getTeam(ServerPlayer player) {
        return ((IHasTeam) player).getTeam();
    }

    public Team getTeam(String name) {
        return teams.get(name);
    }

    public void invitePlayerToTeam(ServerPlayer player, Team team) throws Team.TeamException {
        if (((IHasTeam) player).hasTeam()) {
            throw new Team.TeamException(new TranslatableComponent("teams.error.alreadyinteam", player.getName().getString()));
        }
        PacketHandler.INSTANCE.sendTo(new TeamInvitedPacket(team), player);
    }

    public void addPlayerToTeam(ServerPlayer player, Team team) throws Team.TeamException {
        if (((IHasTeam) player).hasTeam()) {
            throw new Team.TeamException(new TranslatableComponent("teams.error.alreadyinteam", player.getName()));
        }
        team.addPlayer(player);
    }

    public void removePlayerFromTeam(ServerPlayer player) throws Team.TeamException {
        Team playerTeam = ((IHasTeam) player).getTeam();
        if (playerTeam == null) {
            throw new Team.TeamException(new TranslatableComponent("teams.error.notinteam", player.getName().getString()));
        }
        playerTeam.removePlayer(player);
        if (playerTeam.isEmpty()) {
            removeTeam(playerTeam);
        }
    }

    public void fromNBT(CompoundTag compound) {
        teams.clear();
        ListTag list = compound.getList(TEAMS_KEY, Tag.TAG_COMPOUND);
        for (var tag : list) {
            try {
                addTeam(Team.fromNBT((CompoundTag) tag));
            } catch (Team.TeamException ex) {
                TeamsHUD.LOGGER.error("Failed to load team from NBT" + ex.getMessage());
            }
        }
    }

    public CompoundTag toNBT() {
        CompoundTag compound = new CompoundTag();
        ListTag list = new ListTag();
        for (var team : teams.values()) {
            list.add(team.toNBT());
        }
        compound.put(TEAMS_KEY, list);
        return compound;
    }
}
