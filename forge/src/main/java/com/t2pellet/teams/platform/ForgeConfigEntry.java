package com.t2pellet.teams.platform;

import net.minecraftforge.common.ForgeConfigSpec;

public abstract class ForgeConfigEntry<T> implements ConfigEntry<T> {

    protected final ForgeConfigSpec.ConfigValue<T> configValue;

    public ForgeConfigEntry(ForgeConfigSpec.ConfigValue<T> configValue) {

        this.configValue = configValue;
    }

}
