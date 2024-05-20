package com.t2pellet.teams.platform;

public interface Config {

    String showInvisibleTeammates = "show_invisible_teammates";
    String friendlyFireEnabled = "friendly_fire_enabled";
    String nameTagVisibility = "name_tag_visibility";
    String colour = "color";
    String deathMessageVisibility = "death_message_visibility";
    String collisionRule = "collision_rule";

    String enableCompassHUD = "enable_compass_hud";
    String enableStatusHUD = "enable_status_hud";
    String toastDuration = "toast_duration";
    void init();

    <T> ConfigEntry<T> getConfigEntry(String id);

}
