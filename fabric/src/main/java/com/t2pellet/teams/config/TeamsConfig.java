package com.t2pellet.teams.config;

import com.t2pellet.teams.TeamsHUD;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.minecraft.ChatFormatting;
import net.minecraft.world.scores.Team;

@Config(name = TeamsHUD.MODID)
public class TeamsConfig implements ConfigData {

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

}
