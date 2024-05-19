package com.t2pellet.teams.client;

import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.core.ClientTeamDB;
import com.t2pellet.teams.client.ui.hud.CompassOverlay;
import com.t2pellet.teams.client.ui.hud.StatusOverlay;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.apache.commons.lang3.ArrayUtils;

public class TeamsHUDClient {

    public static final StatusOverlay status = new StatusOverlay();
    public static final CompassOverlay compass = new CompassOverlay();

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

    public static void clientDisconnect() {
        ClientTeam.INSTANCE.reset();
        ClientTeamDB.INSTANCE.clear();
    }

}
