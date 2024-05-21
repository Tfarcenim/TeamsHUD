package com.t2pellet.teams.client.ui.toast;

import com.t2pellet.teams.client.TeamsKeys;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;

public abstract class RespondableTeamToast extends TeamToast {

    private boolean responded = false;

    public RespondableTeamToast(String team) {
        super(team);
    }

    public void respond() {
        responded = true;
    }

    @Override
    public String subTitle() {
        String rejectKey = TeamsKeys.REJECT.getLocalizedName();
        String acceptKey = TeamsKeys.ACCEPT.getLocalizedName();
        return I18n.get("teams.toast.respond", rejectKey, acceptKey);
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
        if (responded) {
            return Visibility.HIDE;
        }
        return super.render(graphics, manager, startTime);
    }
}
