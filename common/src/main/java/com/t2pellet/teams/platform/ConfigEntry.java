package com.t2pellet.teams.platform;

public interface ConfigEntry<T> {

    boolean getAsBoolean();
    double getAsDouble();
    int getAsInt();
    T get();
    <U> U getAs(Class<U> clazz);

}
