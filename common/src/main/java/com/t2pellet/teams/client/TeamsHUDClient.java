package com.t2pellet.teams.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.apache.commons.lang3.ArrayUtils;

public class TeamsHUDClient {

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

}
