package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.client.core.ClientTeamDB;
import com.t2pellet.teams.network.server.C2STeamCreatePacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TeamsCreateScreen extends TeamsInputScreen {

    private static final Component CREATE_TITLE = new TranslatableComponent("teams.menu.create.title");
    private static final Component CREATE_TEXT = new TranslatableComponent("teams.menu.create.text");

    public TeamsCreateScreen(Screen parent) {
        super(parent, CREATE_TITLE);
    }

    @Override
    protected Component getSubmitText() {
        return CREATE_TEXT;
    }

    @Override
    protected void onSubmit(Button widget) {
        minecraft.setScreen(new TeamsMainScreen(null));
        Services.PLATFORM.sendToServer(new C2STeamCreatePacket(inputField.getValue()));
    }

    @Override
    protected boolean submitCondition() {
        return !ClientTeamDB.INSTANCE.containsTeam(inputField.getValue());
    }
}
