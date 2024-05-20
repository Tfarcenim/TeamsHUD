package com.t2pellet.teams.platform;

import net.minecraftforge.common.ForgeConfigSpec;

public class ForgeDoubleConfigEntry extends ForgeConfigEntry<Double> {
    public ForgeDoubleConfigEntry(ForgeConfigSpec.DoubleValue config) {
        super(config);
    }

    @Override
    public boolean getAsBoolean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getAsDouble() {
        return configValue.get();
    }

    @Override
    public int getAsInt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Double get() {
        return null;
    }

    @Override
    public <U> U getAs(Class<U> clazz) {
        return null;
    }
}
