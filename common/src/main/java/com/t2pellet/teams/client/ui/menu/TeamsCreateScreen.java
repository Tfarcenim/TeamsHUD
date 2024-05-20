package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.client.core.ClientTeamDB;
import com.t2pellet.teams.core.ModComponents;
import com.t2pellet.teams.network.server.C2STeamCreatePacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TeamsCreateScreen extends TeamsInputScreen {

    public TeamsCreateScreen(Screen parent) {
        super(parent, ModComponents.CREATE_TITLE);
    }

    @Override
    protected Component getSubmitText() {
        return ModComponents.CREATE_TEXT2;
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
