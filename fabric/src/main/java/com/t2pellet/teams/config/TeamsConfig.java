package com.t2pellet.teams.config;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.platform.MultiloaderConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.ChatFormatting;
import net.minecraft.world.scores.Team;

@Config(name = TeamsHUD.MODID)
public class TeamsConfig implements ConfigData, MultiloaderConfig {

    @ConfigEntry.Category("Team Defaults")
    public boolean showInvisibleTeammates = true;
    @ConfigEntry.Category("Team Defaults")
    public boolean friendlyFireEnabled = false;
    @ConfigEntry.Category("Team Defaults")
    public Team.Visibility nameTagVisibility = Team.Visibility.ALWAYS;
    @ConfigEntry.Category("Team Defaults")
    public ChatFormatting colour = ChatFormatting.BOLD;
    @ConfigEntry.Category("Team Defaults")
    public Team.Visibility deathMessageVisibility = Team.Visibility.ALWAYS;
    @ConfigEntry.Category("Team Defaults")
    @Comment("Note that 'push own team' and 'push other teams' are swapped.")
    public Team.CollisionRule collisionRule = Team.CollisionRule.PUSH_OWN_TEAM;

    @ConfigEntry.Category("Visual")
    public boolean enableCompassHUD = true;
    @ConfigEntry.Category("Visual")
    public boolean enableStatusHUD = true;
    @ConfigEntry.Category("Visual")
    @Comment("How long teams toast notifications should last")
    public int toastDuration = 5;
    @ConfigEntry.Category("Visual")
    @Comment("Show other team members' hunger")
    public boolean showHunger = true;

    @Override
    public boolean showInvisibleTeammates() {
        return showInvisibleTeammates;
    }

    @Override
    public boolean friendlyFireEnabled() {
        return friendlyFireEnabled;
    }

    @Override
    public Team.Visibility nameTagVisibility() {
        return nameTagVisibility;
    }

    @Override
    public ChatFormatting colour() {
        return colour;
    }

    @Override
    public Team.Visibility deathMessageVisibility() {
        return deathMessageVisibility;
    }

    @Override
    public Team.CollisionRule collisionRule() {
        return collisionRule;
    }

    @Override
    public boolean enableCompassHUD() {
        return enableCompassHUD;
    }

    @Override
    public boolean enableStatusHUD() {
        return enableStatusHUD;
    }

    @Override
    public int toastDuration() {
        return toastDuration;
    }

    @Override
    public boolean showHunger() {
        return showHunger;
    }
}
