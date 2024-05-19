package com.t2pellet.teams.client.core;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.UUID;

@Environment(EnvType.CLIENT)
public interface ClientTeam {

    ClientTeam INSTANCE = new ClientTeamImpl();

    void init(String name, boolean hasPermissions);

    String getName();

    boolean hasPermissions();

    boolean isInTeam();

    boolean isTeamEmpty();

    List<Teammate> getTeammates();

    boolean hasPlayer(UUID player);

    void addPlayer(UUID player, String name, ResourceLocation skin, float health, int hunger);

    void updatePlayer(UUID player, float health, int hunger);

    void removePlayer(UUID player);

    List<Teammate> getFavourites();

    boolean isFavourite(Teammate player);

    void addFavourite(Teammate player);

    void removeFavourite(Teammate player);

    void reset();

    class Teammate {
        public final UUID id;
        public final String name;
        public final ResourceLocation skin;
        float health;
        int hunger;

        Teammate(UUID id, String name, ResourceLocation skin, float health, int hunger) {
            this.id = id;
            this.name = name;
            this.skin = skin;
            this.health = health;
            this.hunger = hunger;
        }

        public float getHealth() {
            return health;
        }

        public int getHunger() {
            return hunger;
        }
    }

}
