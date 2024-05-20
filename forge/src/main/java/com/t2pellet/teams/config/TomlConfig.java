package com.t2pellet.teams.config;

import com.t2pellet.teams.platform.MultiloaderConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.ForgeConfigSpec;

public class TomlConfig implements MultiloaderConfig {

    @Override
    public boolean showInvisibleTeammates() {
        return Server.showInvisibleTeammates.get();
    }

    @Override
    public boolean friendlyFireEnabled() {
        return Server.friendlyFireEnabled.get();
    }

    @Override
    public Team.Visibility nameTagVisibility() {
        return Server.nameTagVisibility.get();
    }

    @Override
    public ChatFormatting colour() {
        return Server.colour.get();
    }

    @Override
    public Team.Visibility deathMessageVisibility() {
        return Server.deathMessageVisibility.get();
    }

    @Override
    public Team.CollisionRule collisionRule() {
        return Server.collisionRule.get();
    }

    @Override
    public boolean enableCompassHUD() {
        return Client.enableCompassHUD.get();
    }

    @Override
    public boolean enableStatusHUD() {
        return Client.enableStatusHUD.get();
    }

    @Override
    public int toastDuration() {
        return Client.toastDuration.get();
    }

    public static class Server {
        public static ForgeConfigSpec.BooleanValue showInvisibleTeammates;
        public static ForgeConfigSpec.BooleanValue friendlyFireEnabled;
        public static ForgeConfigSpec.EnumValue<Team.Visibility> nameTagVisibility;
        public static ForgeConfigSpec.EnumValue<ChatFormatting> colour;
        public static ForgeConfigSpec.EnumValue<Team.Visibility> deathMessageVisibility;
        public static ForgeConfigSpec.ConfigValue<Team.CollisionRule> collisionRule;

        public Server(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            showInvisibleTeammates = builder.define("show_invisible_teammates",true);
            friendlyFireEnabled = builder.define("friendly_fire_enabled",false);
            nameTagVisibility = builder.defineEnum("name_tag_visibility", Team.Visibility.ALWAYS);
            colour = builder.defineEnum("colour",ChatFormatting.BOLD);
            deathMessageVisibility = builder.defineEnum("death_message_visibility", Team.Visibility.ALWAYS);
            collisionRule = builder.comment("Note that 'push own team' and 'push other teams' are swapped.").defineEnum("collision_rule", Team.CollisionRule.PUSH_OWN_TEAM);
            builder.pop();
        }
    }

    public static class Client {
        public static ForgeConfigSpec.BooleanValue enableCompassHUD;
        public static ForgeConfigSpec.BooleanValue enableStatusHUD;
        public static ForgeConfigSpec.IntValue toastDuration;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("visual");
            enableCompassHUD = builder.define("enable_compass_hud",true);
            enableStatusHUD = builder.define("enable_status_hud",true);
            toastDuration = builder.comment("How long teams toast notifications should last").defineInRange("toast_duration",5,0,100000000);
            builder.pop();
        }
    }
}
