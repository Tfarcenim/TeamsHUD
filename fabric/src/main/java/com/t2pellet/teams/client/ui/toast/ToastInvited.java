package com.t2pellet.teams.client.ui.toast;

import net.minecraft.client.resources.language.I18n;

public class ToastInvited extends RespondableTeamToast {

    public ToastInvited(String team) {
        super(team);
    }

    @Override
    public String title() {
        return I18n.get("teams.toast.invite", team);
    }

}
