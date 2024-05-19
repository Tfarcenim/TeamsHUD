package com.t2pellet.teams.client.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

@Environment(EnvType.CLIENT)
public class EventHandlers {

    public static ClientLoginConnectionEvents.Disconnect disconnect = (handler, client) -> {
        com.t2pellet.teams.client.core.ClientTeam.INSTANCE.reset();
        com.t2pellet.teams.client.core.ClientTeamDB.INSTANCE.clear();
    };

    public static ClientPlayConnectionEvents.Disconnect disconnect1 = (handler, client) -> {
        ClientTeam.INSTANCE.reset();
        ClientTeamDB.INSTANCE.clear();
    };
}
