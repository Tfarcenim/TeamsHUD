package com.t2pellet.teams.platform;

import net.minecraft.ChatFormatting;
import net.minecraft.world.scores.Team;

public interface MultiloaderConfig {

    boolean showInvisibleTeammates();
    boolean friendlyFireEnabled();
    Team.Visibility nameTagVisibility();
    ChatFormatting colour();
    Team.Visibility deathMessageVisibility();
    Team.CollisionRule collisionRule();

    boolean enableCompassHUD();
    boolean enableStatusHUD();
    int toastDuration();
    boolean showHunger();
}
