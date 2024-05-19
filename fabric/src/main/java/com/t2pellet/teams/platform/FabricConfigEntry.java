package com.t2pellet.teams.platform;

import java.util.function.Supplier;

public class FabricConfigEntry<T> implements ConfigEntry<T> {
    Supplier<T> supplier;
    public FabricConfigEntry(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean getAsBoolean() {
        return (boolean)get();
    }

    @Override
    public double getAsDouble() {
        return (double)get();
    }

    @Override
    public int getAsInt() {
        return (int)get();
    }

    @Override
    public T get() {
        return supplier.get();
    }
}
