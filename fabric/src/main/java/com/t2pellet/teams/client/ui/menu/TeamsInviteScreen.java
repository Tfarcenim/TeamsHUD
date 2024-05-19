package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastInviteSent;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamInvitePacket;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TeamsInviteScreen extends TeamsInputScreen {

    private static final Component TITLE_TEXT = new TranslatableComponent("teams.menu.invite.title");
    private static final Component INVITE_TEXT = new TranslatableComponent("teams.menu.invite.text");


    public TeamsInviteScreen(Screen parent) {
        super(parent, TITLE_TEXT);
    }

    @Override
    protected float getBackgroundScale() {
        return 1.0F;
    }

    @Override
    protected Component getSubmitText() {
        return INVITE_TEXT;
    }

    @Override
    protected void onSubmit(Button widget) {
        PacketHandler.INSTANCE.sendToServer(new TeamInvitePacket(minecraft.player.getUUID(), inputField.getValue()));
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
