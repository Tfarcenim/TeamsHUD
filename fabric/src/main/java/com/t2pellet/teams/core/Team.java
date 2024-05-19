package com.t2pellet.teams.core;

import com.mojang.authlib.GameProfile;
import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.mixin.AdvancementAccessor;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamClearPacket;
import com.t2pellet.teams.network.packets.TeamDataPacket;
import com.t2pellet.teams.network.packets.TeamInitPacket;
import com.t2pellet.teams.network.packets.TeamPlayerDataPacket;
import com.t2pellet.teams.network.packets.toasts.TeamUpdatePacket;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;

import java.util.*;
import java.util.stream.Stream;

;

public class Team extends net.minecraft.world.scores.Team {

    public final String name;
    private Set<UUID> players;
    private Map<UUID, ServerPlayer> onlinePlayers;
    private final Set<Advancement> advancements = new LinkedHashSet<>();
    private PlayerTeam scoreboardTeam;

    Team(String name) {
        this.name = name;
        players = new HashSet<>();
        onlinePlayers = new HashMap<>();
        scoreboardTeam = TeamsHUDFabric.getScoreboard().getPlayerTeam(name);
        if (scoreboardTeam == null) {
            scoreboardTeam = TeamsHUDFabric.getScoreboard().addPlayerTeam(name);
        }
    }

    public UUID getOwner() {
        return players.stream().findFirst().orElseThrow();
    }

    public boolean playerHasPermissions(ServerPlayer player) {
        return getOwner().equals(player.getUUID()) || player.hasPermissions(2);
    }
    public Stream<ServerPlayer> getOnlinePlayers() {
        return onlinePlayers.values().stream();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean hasPlayer(ServerPlayer player) {
        return hasPlayer(player.getUUID());
    }

    public boolean hasPlayer(UUID player) {
        return players.contains(player);
    }

    public void addPlayer(ServerPlayer player) {
        addPlayer(player.getUUID());
    }

    public void removePlayer(ServerPlayer player) {
        removePlayer(player.getUUID());
    }

    public void clear() {
        var playersCopy = new ArrayList<>(players);
        playersCopy.forEach(this::removePlayer);
        advancements.clear();
    }

    public void addAdvancement(Advancement advancement) {
        advancements.add(advancement);
    }

    public Set<Advancement> getAdvancements() {
        return advancements;
    }

    void playerOnline(ServerPlayer player, boolean sendPackets) {
        onlinePlayers.put(player.getUUID(), player);
        ((IHasTeam) player).setTeam(this);
        // Packets
        if (sendPackets) {
            PacketHandler.INSTANCE.sendTo(new TeamInitPacket(name, playerHasPermissions(player)), player);
            if (onlinePlayers.size() == 1) {
                var players = TeamsHUDFabric.getServer().getPlayerList().getPlayers().toArray(ServerPlayer[]::new);
                PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.ONLINE, name), players);
            }
            var players = getOnlinePlayers().toArray(ServerPlayer[]::new);
            PacketHandler.INSTANCE.sendTo(new TeamPlayerDataPacket(player, TeamPlayerDataPacket.Type.ADD), players);
            for (var teammate : players) {
                PacketHandler.INSTANCE.sendTo(new TeamPlayerDataPacket(teammate, TeamPlayerDataPacket.Type.ADD), player);
            }
        }
        // Advancement Sync
        for (Advancement advancement : getAdvancements()) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        }
    }

    public Stream<UUID> getPlayerUuids() {
        return players.stream();
    }

    void playerOffline(ServerPlayer player, boolean sendPackets) {
        onlinePlayers.remove(player.getUUID());
        // Packets
        if (sendPackets) {
            if (isEmpty()) {
                var players = TeamsHUDFabric.getServer().getPlayerList().getPlayers().toArray(ServerPlayer[]::new);
                PacketHandler.INSTANCE.sendTo(new TeamDataPacket(TeamDataPacket.Type.OFFLINE, name), players);
            }
            var players = getOnlinePlayers().toArray(ServerPlayer[]::new);
            PacketHandler.INSTANCE.sendTo(new TeamPlayerDataPacket(player, TeamPlayerDataPacket.Type.REMOVE), players);
        }
    }

    private void addPlayer(UUID player) {
        players.add(player);
        String playerName = getNameFromUUID(player);
        // Scoreboard
        var playerScoreboardTeam = TeamsHUDFabric.getScoreboard().getPlayersTeam(playerName);
        if (playerScoreboardTeam == null || !playerScoreboardTeam.isAlliedTo(scoreboardTeam)) {
            TeamsHUDFabric.getScoreboard().addPlayerToTeam(playerName, scoreboardTeam);
        }
        var playerEntity = TeamsHUDFabric.getServer().getPlayerList().getPlayer(player);
        if (playerEntity != null) {
            // Packets
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.JOINED, true), playerEntity);
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.JOINED, false), getOnlinePlayers().toArray(ServerPlayer[]::new));
            playerOnline(playerEntity, true);
            // Advancement Sync
            Set<Advancement> advancements = ((AdvancementAccessor) playerEntity.getAdvancements()).getVisibleAdvancements();
            for (Advancement advancement : advancements) {
                if (playerEntity.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    addAdvancement(advancement);
                }
            }
        }
    }

    private void removePlayer(UUID player) {
        players.remove(player);
        String playerName = getNameFromUUID(player);
        // Scoreboard
        var playerScoreboardTeam = TeamsHUDFabric.getScoreboard().getPlayersTeam(playerName);
        if (playerScoreboardTeam != null && playerScoreboardTeam.isAlliedTo(scoreboardTeam)) {
            TeamsHUDFabric.getScoreboard().removePlayerFromTeam(playerName, scoreboardTeam);
        }
        // Packets
        var playerEntity = TeamsHUDFabric.getServer().getPlayerList().getPlayer(player);
        if (playerEntity != null) {
            playerOffline(playerEntity, true);
            PacketHandler.INSTANCE.sendTo(new TeamClearPacket(), playerEntity);
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.LEFT, true), playerEntity);
            PacketHandler.INSTANCE.sendTo(new TeamUpdatePacket(name, playerName, TeamUpdatePacket.Action.LEFT, false), getOnlinePlayers().toArray(ServerPlayer[]::new));
            ((IHasTeam) playerEntity).setTeam(null);
        }
    }

    private String getNameFromUUID(UUID id) {
        return TeamsHUDFabric.getServer().getProfileCache().get(id).map(GameProfile::getName).orElseThrow();
    }

    static Team fromNBT(CompoundTag compound) {
        Team team = new Builder(compound.getString("name"))
                .setColour(ChatFormatting.getByName(compound.getString("colour")))
                .setCollisionRule(CollisionRule.byName(compound.getString("collision")))
                .setDeathMessageVisibilityRule(Visibility.byName(compound.getString("deathMessages")))
                .setNameTagVisibilityRule(Visibility.byName(compound.getString("nameTags")))
                .setFriendlyFireAllowed(compound.getBoolean("friendlyFire"))
                .setShowFriendlyInvisibles(compound.getBoolean("showInvisible"))
                .complete();

        ListTag players = compound.getList("players", Tag.TAG_STRING);
        for (var elem : players) {
            team.addPlayer(UUID.fromString(elem.getAsString()));
        }

        ListTag advancements = compound.getList("advancement", Tag.TAG_STRING);
        for (var adv : advancements) {
            ResourceLocation id = ResourceLocation.tryParse(adv.getAsString());
            team.addAdvancement(TeamsHUDFabric.getServer().getAdvancements().getAdvancement(id));
        }

        return team;
    }

    CompoundTag toNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("name", name);
        compound.putString("colour", scoreboardTeam.getColor().getName());
        compound.putString("collision", scoreboardTeam.getCollisionRule().name);
        compound.putString("deathMessages", scoreboardTeam.getDeathMessageVisibility().name);
        compound.putString("nameTags", scoreboardTeam.getNameTagVisibility().name);
        compound.putBoolean("friendlyFire", scoreboardTeam.isAllowFriendlyFire());
        compound.putBoolean("showInvisible", scoreboardTeam.canSeeFriendlyInvisibles());

        ListTag playerList = new ListTag();
        for (var player : players) {
            playerList.add(StringTag.valueOf(player.toString()));
        }
        compound.put("players", playerList);

        ListTag advList = new ListTag();
        for (var advancement : advancements) {
            advList.add(StringTag.valueOf(advancement.getId().toString()));
        }
        compound.put("advancements", advList);

        return compound;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MutableComponent getFormattedName(Component name) {
        return scoreboardTeam.getFormattedName(name);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        return scoreboardTeam.canSeeFriendlyInvisibles();
    }

    public void setShowFriendlyInvisibles(boolean value) {
        scoreboardTeam.setSeeFriendlyInvisibles(value);
    }

    @Override
    public boolean isAllowFriendlyFire() {
        return scoreboardTeam.isAllowFriendlyFire();
    }

    public void setFriendlyFireAllowed(boolean value) {
        scoreboardTeam.setAllowFriendlyFire(value);
    }

    @Override
    public Visibility getNameTagVisibility() {
        return scoreboardTeam.getNameTagVisibility();
    }

    public void setNameTagVisibilityRule(Visibility value) {
        scoreboardTeam.setNameTagVisibility(value);
    }

    @Override
    public ChatFormatting getColor() {
        return scoreboardTeam.getColor();
    }

    public void setColour(ChatFormatting colour) {
        scoreboardTeam.setColor(colour);
    }

    @Override
    public Collection<String> getPlayers() {
        return scoreboardTeam.getPlayers();
    }

    @Override
    public Visibility getDeathMessageVisibility() {
        return scoreboardTeam.getDeathMessageVisibility();
    }

    public void setDeathMessageVisibilityRule(Visibility value) {
        scoreboardTeam.setDeathMessageVisibility(value);
    }

    @Override
    public CollisionRule getCollisionRule() {
        return scoreboardTeam.getCollisionRule();
    }

    public void setCollisionRule(CollisionRule value) {
        scoreboardTeam.setCollisionRule(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Team team && Objects.equals(team.getName(), this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }


    public static class TeamException extends Exception {
        public TeamException(Component message) {
            super(message.getString());
        }
    }

    public static class Builder {

        private final String name;
        private boolean showFriendlyInvisibles = TeamsHUDFabric.getConfig().showInvisibleTeammates;
        private boolean friendlyFireAllowed = TeamsHUDFabric.getConfig().friendlyFireEnabled;
        private Visibility nameTagVisibilityRule = TeamsHUDFabric.getConfig().nameTagVisibility;
        private ChatFormatting colour = TeamsHUDFabric.getConfig().colour;
        private Visibility deathMessageVisibilityRule = TeamsHUDFabric.getConfig().deathMessageVisibility;
        private CollisionRule collisionRule = TeamsHUDFabric.getConfig().collisionRule;

        public Builder(String name) {
            this.name = name;
        }

        public Builder setShowFriendlyInvisibles(boolean showFriendlyInvisibles) {
            this.showFriendlyInvisibles = showFriendlyInvisibles;
            return this;
        }

        public Builder setFriendlyFireAllowed(boolean friendlyFireAllowed) {
            this.friendlyFireAllowed = friendlyFireAllowed;
            return this;
        }

        public Builder setNameTagVisibilityRule(Visibility nameTagVisibilityRule) {
            this.nameTagVisibilityRule = nameTagVisibilityRule;
            return this;
        }

        public Builder setColour(ChatFormatting colour) {
            this.colour = colour;
            return this;
        }

        public Builder setDeathMessageVisibilityRule(Visibility deathMessageVisibilityRule) {
            this.deathMessageVisibilityRule = deathMessageVisibilityRule;
            return this;
        }

        public Builder setCollisionRule(CollisionRule collisionRule) {
            this.collisionRule = collisionRule;
            return this;
        }

        public Team complete() {
            Team team = new Team(name);
            team.setShowFriendlyInvisibles(showFriendlyInvisibles);
            team.setFriendlyFireAllowed(friendlyFireAllowed);
            team.setNameTagVisibilityRule(nameTagVisibilityRule);
            team.setColour(colour);
            team.setDeathMessageVisibilityRule(deathMessageVisibilityRule);
            team.setCollisionRule(collisionRule);
            return team;
        }

    }
}
