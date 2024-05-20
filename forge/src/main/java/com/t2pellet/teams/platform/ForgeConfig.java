package com.t2pellet.teams.platform;


import com.t2pellet.teams.config.TomlConfig;

import java.util.HashMap;
import java.util.Map;

public class ForgeConfig implements Config {

    public final Map<String,ForgeConfigEntry<?>> configEntryMap = new HashMap<>();


    @Override
    public void init() {
        configEntryMap.put("exposed",new ForgeDoubleConfigEntry(TomlConfig.exposed));
        configEntryMap.put("vulnerable",new ForgeDoubleConfigEntry(TomlConfig.vulnerable));

    }

    @Override
    public <T> ConfigEntry<T> getConfigEntry(String id) {
        return (ConfigEntry<T>) configEntryMap.get(id);
    }
}
