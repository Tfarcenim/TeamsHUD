package com.t2pellet.teams.network.server;

import com.t2pellet.teams.network.ModPacket;
import net.minecraft.server.level.ServerPlayer;

public interface C2SModPacket<T> extends ModPacket<T> {

    void handleServer(ServerPlayer player);

}
