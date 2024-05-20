package com.t2pellet.teams.core;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ModComponents {

    public static final Component CREATE_TITLE = translatable("teams.menu.create.title");
    public static final Component LONELY_MENU_TITLE = translatable("teams.menu.lonely.title");
    public static final Component CREATE_TEXT2 = translatable("teams.menu.create.text");
    public static final Component INVITE_TEXT = translatable("teams.menu.invite");
    public static final Component LEAVE_TEXT = translatable("teams.menu.leave");
    public static final Component GO_BACK_TEXT = translatable("teams.menu.return");
    public static final Component TEAMS_MENU_TITLE = translatable("teams.menu.title");
    public static final Component DEFAULT_TEXT = translatable("teams.menu.input");
    public static final Component CREATE_TEXT = translatable("teams.menu.create");
    public static final Component LONELY_TEXT = translatable("teams.menu.lonely.alone");
    public static final Component DUPLICATE_TEAM = translatable("teams.error.duplicateteam");
    public static final Component INVITE_TITLE_TEXT = translatable("teams.menu.invite.title");
    public static final Component INVITE_TEXT2 = translatable("teams.menu.invite.text");

    public static MutableComponent translatable(String key, Object... args) {
        return Component.translatable(key,args);
    }

    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }

}
