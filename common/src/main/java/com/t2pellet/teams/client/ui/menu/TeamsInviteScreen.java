package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastInviteSent;
import com.t2pellet.teams.core.ModComponents;
import com.t2pellet.teams.network.server.C2STeamInvitePacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class TeamsInviteScreen extends TeamsInputScreen {


    public TeamsInviteScreen(Screen parent) {
        super(parent, ModComponents.INVITE_TITLE_TEXT);
    }
    @Override
    protected Component getSubmitText() {
        return ModComponents.INVITE_TEXT2;
    }

    @Override
    protected void onSubmit(Button widget) {
        Services.PLATFORM.sendToServer(new C2STeamInvitePacket(inputField.getValue()));
        minecraft.getToasts().addToast(new ToastInviteSent(ClientTeam.INSTANCE.getName(), inputField.getValue()));
        minecraft.setScreen(parent);
    }

    @Override
    protected boolean submitCondition() {
        String clientName = minecraft.player.getName().getString();
        return minecraft.getConnection().getOnlinePlayers()
                .stream()
                .anyMatch(entry -> !entry.getProfile().getName().equals(clientName) && entry.getProfile().getName().equals(inputField.getValue()));
    }
}
