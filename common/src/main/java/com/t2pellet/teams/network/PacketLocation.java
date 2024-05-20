package com.t2pellet.teams.network;

import net.minecraft.resources.ResourceLocation;

public record PacketLocation<T>(ResourceLocation id, Class<T> clazz) {}
