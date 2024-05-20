package com.t2pellet.teams.config;

import net.minecraft.ChatFormatting;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.ForgeConfigSpec;

public class TomlConfig {

    public static class Server {
        public static ForgeConfigSpec.BooleanValue showInvisibleTeammates = true;
        public static ForgeConfigSpec.BooleanValue friendlyFireEnabled = false;
        public static ForgeConfigSpec.EnumValue<Team.Visibility> nameTagVisibility = Team.Visibility.ALWAYS;
        public static ForgeConfigSpec.EnumValue<ChatFormatting> colour = ChatFormatting.BOLD;
        public static ForgeConfigSpec.EnumValue<Team.Visibility> deathMessageVisibility = Team.Visibility.ALWAYS;
        @Comment("Note that 'push own team' and 'push other teams' are swapped.")
        public Team.CollisionRule collisionRule = Team.CollisionRule.PUSH_OWN_TEAM;

        public Server(ForgeConfigSpec.Builder builder) {

        }

    }

    public static class Client {
        @ConfigEntry.Category("Visual")
        public boolean enableCompassHUD = true;
        @ConfigEntry.Category("Visual")
        public boolean enableStatusHUD = true;
        @ConfigEntry.Category("Visual")
        @Comment("How long teams toast notifications should last")
        public int toastDuration = 5;
    }
}
