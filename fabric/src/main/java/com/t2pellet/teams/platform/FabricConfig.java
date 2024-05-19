package com.t2pellet.teams.platform;

import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.config.TeamsConfig;

import java.util.HashMap;
import java.util.Map;

public class FabricConfig<T> implements Config<T> {

    protected final Map<String,ConfigEntry<?>> configEntryMap = new HashMap<>();
    @Override
    public void init() {
        TeamsConfig config = TeamsHUDFabric.getConfig();
        configEntryMap.put(showInvisibleTeammates,new FabricConfigEntry<>(() -> config.showInvisibleTeammates));
        configEntryMap.put(friendlyFireEnabled,new FabricConfigEntry<>(()-> config.friendlyFireEnabled));
        configEntryMap.put(nameTagVisibility,new FabricConfigEntry<>(() -> config.nameTagVisibility));
        configEntryMap.put(colour,new FabricConfigEntry<>(() -> config.colour));
        configEntryMap.put(deathMessageVisibility,new FabricConfigEntry<>(() -> config.deathMessageVisibility));
        configEntryMap.put(collisionRule,new FabricConfigEntry<>(() -> config.collisionRule));
        configEntryMap.put(enableCompassHUD,new FabricConfigEntry<>(() -> config.enableCompassHUD));
        configEntryMap.put(enableStatusHUD,new FabricConfigEntry<>(() -> config.enableStatusHUD));
        configEntryMap.put(toastDuration,new FabricConfigEntry<>(() -> config.toastDuration));
    }

    @Override
    public ConfigEntry<T> getConfigEntry(String id) {
        return (ConfigEntry<T>) configEntryMap.get(id);
    }
}
