package com.t2pellet.teams.core;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.network.client.S2CTeamDataPacket;
import com.t2pellet.teams.network.client.S2CTeamInvitedPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class TeamDB extends SavedData {

    private static final String TEAMS_KEY = "teams";

    private Map<String, Team> teams = new HashMap<>();
    ServerLevel serverLevel;
    Scoreboard scoreboard;

    private TeamDB(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        scoreboard = serverLevel.getScoreboard();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        toNBT(compoundTag);
        return compoundTag;
    }

    public Stream<Team> getTeams() {
        return teams.values().stream();
    }

    public void addTeam(Team team) throws Team.TeamException {
        if (teams.containsKey(team.getName())) {
            throw new Team.TeamException(ModComponents.DUPLICATE_TEAM);
        }
        teams.put(team.getName(), team);
        List<ServerPlayer> players = serverLevel.getServer().getPlayerList().getPlayers();
        Services.PLATFORM.sendToClients(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ADD, team.name), players);
        setDirty();
    }

    public Team addTeam(String name, @Nullable ServerPlayer creator) throws Team.TeamException {
        if (creator != null && ((IHasTeam) creator).hasTeam()) {
            throw new Team.TeamException(ModComponents.translatable("teams.error.alreadyinteam", creator.getName().getString()));
        }
        Team team = new Team.Builder(name).complete(this);
        addTeam(team);
        if (creator != null) {
            team.addPlayer(creator);
        }
        List<ServerPlayer> players = creator.getServer().getPlayerList().getPlayers();
        Services.PLATFORM.sendToClients(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ONLINE, team.name), players);
        setDirty();
        return team;
    }

    public void removeTeam(Team team) {
        teams.remove(team.getName());
        MinecraftServer server = serverLevel.getServer();
        scoreboard.removePlayerTeam(scoreboard.getPlayerTeam(team.getName()));
        team.clear();
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        Services.PLATFORM.sendToClients(new S2CTeamDataPacket(S2CTeamDataPacket.Type.REMOVE, team.name), players);
        setDirty();
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
            throw new Team.TeamException(ModComponents.translatable("teams.error.alreadyinteam", player.getName().getString()));
        }
        Services.PLATFORM.sendToClient(new S2CTeamInvitedPacket(team), player);
    }

    public void addPlayerToTeam(ServerPlayer player, Team team) throws Team.TeamException {
        if (((IHasTeam) player).hasTeam()) {
            throw new Team.TeamException(ModComponents.translatable("teams.error.alreadyinteam", player.getName()));
        }
        team.addPlayer(player);
    }

    public void removePlayerFromTeam(ServerPlayer player) throws Team.TeamException {
        Team playerTeam = ((IHasTeam) player).getTeam();
        if (playerTeam == null) {
            throw new Team.TeamException(ModComponents.translatable("teams.error.notinteam", player.getName().getString()));
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
                addTeam(Team.fromNBT((CompoundTag) tag,this));
            } catch (Team.TeamException ex) {
                TeamsHUD.LOGGER.error("Failed to load team from NBT" + ex.getMessage());
            }
        }
    }

    public void toNBT(CompoundTag compound) {
        ListTag list = new ListTag();
        for (var team : teams.values()) {
            list.add(team.toNBT());
        }
        compound.put(TEAMS_KEY, list);
    }


    static TeamDB get(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(compoundTag -> loadStatic(compoundTag, serverLevel),TEAMS_KEY);
    }


    static TeamDB getOrMake(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel), () -> new TeamDB(serverLevel), TEAMS_KEY);
    }

    public static TeamDB getOrMakeDefault(MinecraftServer server) {
        return getOrMake(server.overworld());
    }

    public static TeamDB loadStatic(CompoundTag compoundTag,ServerLevel level) {
        TeamDB id = new TeamDB(level);
        id.fromNBT(compoundTag);
        return id;
    }

}
