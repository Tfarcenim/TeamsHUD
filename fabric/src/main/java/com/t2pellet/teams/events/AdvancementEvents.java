package com.t2pellet.teams.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementEvents {

    public static final Event<PlayerAdvancement> ADVANCEMENT_GRANTED = EventFactory.createArrayBacked(PlayerAdvancement.class,
            listeners -> (player, advancement) -> {
                for (PlayerAdvancement listener : listeners) {
                    listener.onPlayerAdvancement(player, advancement);
                }
            });

    @FunctionalInterface
    public interface PlayerAdvancement {
        void onPlayerAdvancement(ServerPlayer player, Advancement advancement);
    }

}
