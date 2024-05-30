package com.t2pellet.teams.network.client;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.TeamsHUDClient;
import com.t2pellet.teams.network.PacketLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamUpdatePacket implements S2CModPacket<S2CTeamUpdatePacket> {

    public enum Action {
        JOINED,
        LEFT
    }

    private static final String TEAM_KEY = "teamName";
    private static final String PLAYER_KEY = "playerName";
    private static final String ACTION_KEY = "action";
    private static final String LOCAL_KEY = "local";

    CompoundTag tag = new CompoundTag();

    public S2CTeamUpdatePacket(String team, String player, Action action, boolean isLocal) {
        tag.putString(TEAM_KEY, team);
        tag.putString(PLAYER_KEY, player);
        tag.putString(ACTION_KEY, action.name());
        tag.putBoolean(LOCAL_KEY, isLocal);
    }

    public S2CTeamUpdatePacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }

    public static final PacketLocation<S2CTeamUpdatePacket> ID = new PacketLocation<>(TeamsHUD.id("team_update"), S2CTeamUpdatePacket.class);

    @Override
    public PacketLocation<S2CTeamUpdatePacket> id() {
        return ID;
    }

    @Override
    public void handleClient() {
        String team = tag.getString(TEAM_KEY);
        String player = tag.getString(PLAYER_KEY);
        S2CTeamUpdatePacket.Action action = S2CTeamUpdatePacket.Action.valueOf(tag.getString(ACTION_KEY));
        boolean isLocal = tag.getBoolean(LOCAL_KEY);
        TeamsHUDClient.handleTeamUpdatePacket(team,player,action,isLocal);
    }
}
