package com.t2pellet.teams.network.client;

import com.t2pellet.teams.network.ModPacket;

public interface S2CModPacket<T> extends ModPacket<T> {

    void handleClient();

}
