package com.t2pellet.teams.network.packets;

import com.t2pellet.teams.client.core.ClientTeamDB;
import com.t2pellet.teams.network.ClientPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class TeamDataPacket extends ClientPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String TYPE_KEY = "type";

    public enum Type {
        ADD,
        REMOVE,
        ONLINE,
        OFFLINE,
        CLEAR
    }

    public TeamDataPacket(Type type, String... teams) {
        ListTag nbtList = new ListTag();
        for (var team : teams) {
            nbtList.add(StringTag.valueOf(team));
        }
        tag.put(TEAM_KEY, nbtList);
        tag.putString(TYPE_KEY, type.name());
    }

    public TeamDataPacket(Minecraft client, FriendlyByteBuf byteBuf) {
        super(client, byteBuf);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void execute() {
        Type type = Type.valueOf(tag.getString(TYPE_KEY));
        ListTag nbtList = tag.getList(TEAM_KEY, Tag.TAG_STRING);
        for (var elem : nbtList) {
            String team = elem.getAsString();
            switch (type) {
                case ADD -> ClientTeamDB.INSTANCE.addTeam(team);
                case REMOVE -> ClientTeamDB.INSTANCE.removeTeam(team);
                case ONLINE -> ClientTeamDB.INSTANCE.teamOnline(team);
                case OFFLINE -> ClientTeamDB.INSTANCE.teamOffline(team);
                case CLEAR -> ClientTeamDB.INSTANCE.clear();
            }
        }
    }

}
